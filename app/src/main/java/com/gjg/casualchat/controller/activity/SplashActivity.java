package com.gjg.casualchat.controller.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjg.casualchat.R;
import com.gjg.casualchat.model.Model;
import com.gjg.casualchat.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

/**
 * Created by JunGuang_Gao on 2017/2/12.
 */

public class SplashActivity extends Activity {

    private static final int SLEEP_TIME = 1000;
    private TextView tv_logo_text;
    private ImageView iv_logo;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //若activity退出就不处理消息
            if(isFinishing()){
                return;
            }

            // 判断进入主页面还是登录页面
            goMainOrLogin();
        }
    };

    private void goMainOrLogin() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断之前是否登录过
                if(EMClient.getInstance().isLoggedInBefore()){
                    //获取用户信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());

                    if (account==null) {
                        //跳转到登录页面
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }else {
                        Model.getInstance().loginSuccess(account);
                        //跳转到主页面
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }


                }else {
                    //跳转到登录页面
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);

                }
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LinearLayout linearLayout= (LinearLayout) this.findViewById(R.id.ll_splash);
        TextView version= (TextView) this.findViewById(R.id.tv_version);
        tv_logo_text= (TextView) this.findViewById(R.id.tv_logo_text);
        iv_logo= (ImageView) this.findViewById(R.id.iv_logo);
        version.setText(getAppVersion());
        //设置动画透明度
//        AlphaAnimation animation=new AlphaAnimation(0.3f,1.0f);
//        animation.setDuration(500);
//        linearLayout.startAnimation(animation);
        startLogoAnimTxt();
        startLogoAnimBg();
        handler.sendMessageDelayed(Message.obtain(),SLEEP_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }

    private String getAppVersion() {
        String versionInfo="";
        String packageName=this.getPackageName();
        try {
            String versionName=this.getPackageManager().getPackageInfo(packageName,0).versionName;
            versionInfo="随聊 "+versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionInfo;
    }

    /**
     * logo文字的动画
     */
    private void startLogoAnimTxt() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_top_in);
        tv_logo_text.startAnimation(animation);
        ObjectAnimator.ofFloat(iv_logo,"alpha",0.0f,1.0f).setDuration(1300).start();//背景圆圈渐变的动画
    }

    /**
     * logo背景的动画
     */
    private void startLogoAnimBg() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                if (fraction >= 0.6) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(1300);
                    animatorSet.playTogether(ObjectAnimator.ofFloat(iv_logo, "scaleX", new float[]{1.0f, 1.25f, 0.75f, 1.15f, 1.0f}),
                            ObjectAnimator.ofFloat(iv_logo, "scaleY", new float[]{1.0f, 0.75f, 1.25f, 0.85f, 1.0f}));
                    animatorSet.start();

                }
            }

        });
        valueAnimator.start();
    }
}
