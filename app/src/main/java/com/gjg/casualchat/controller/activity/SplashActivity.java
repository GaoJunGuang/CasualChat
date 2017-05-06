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

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gjg.casualchat.R.id.iv_logo;

/**
 * Created by JunGuang_Gao on 2017/2/12.
 */

public class SplashActivity extends Activity {

    private static final int SLEEP_TIME = 2500;

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_logo_text)
    TextView tvLogoText;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_splash)
    LinearLayout llSplash;

    private MyHandler handler = new MyHandler(this);

    static class MyHandler extends Handler {

        private final WeakReference<SplashActivity> mActivity;

        public MyHandler(SplashActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            //若activity退出就不处理消息
            if (mActivity.get() != null) {

                SplashActivity splashActivity = mActivity.get();
                if (splashActivity.isFinishing()) {
                    return;
                }

                // 判断进入主页面还是登录页面
                splashActivity.goMainOrLogin();

            }


        }
    }

    private void goMainOrLogin() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断之前是否登录过
                if (EMClient.getInstance().isLoggedInBefore()) {
                    //获取用户信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());

                    if (account == null) {
                        //跳转到登录页面
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);

                    } else {
                        Model.getInstance().loginSuccess(account);
                        //跳转到主页面
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }


                } else {
                    //跳转到登录页面
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
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
        ButterKnife.bind(this);

        tvVersion.setText(getAppVersion());
        //设置动画透明度
//        AlphaAnimation animation=new AlphaAnimation(0.3f,1.0f);
//        animation.setDuration(500);
//        linearLayout.startAnimation(animation);

        startLogoAnimBg();
        startLogoAnimTxt();

        handler.sendMessageDelayed(Message.obtain(), SLEEP_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }

    private String getAppVersion() {
        String versionInfo = "";
        String packageName = this.getPackageName();
        try {
            String versionName = this.getPackageManager().getPackageInfo(packageName, 0).versionName;
            versionInfo = "随聊 " + versionName;
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
        tvLogoText.startAnimation(animation);
        ObjectAnimator.ofFloat(iv_logo, "alpha", 0.0f, 1.0f).setDuration(1300).start();//背景圆圈渐变的动画
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
                //分度值是动画执行的百分比。区别于AnimatedValue。
                float fraction = valueAnimator.getAnimatedFraction();
                if (fraction >= 0.4) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(1500);
                    animatorSet.playTogether(ObjectAnimator.ofFloat(ivLogo, "scaleX", new float[]{1.0f, 1.20f, 0.75f, 1.0f}),
                            ObjectAnimator.ofFloat(ivLogo, "scaleY", new float[]{1.0f, 0.75f, 1.25f, 0.85f, 1.0f}));
                    animatorSet.start();

                }
            }

        });
        valueAnimator.start();
    }
}
