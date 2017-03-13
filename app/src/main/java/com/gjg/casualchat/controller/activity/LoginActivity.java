package com.gjg.casualchat.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gjg.casualchat.R;
import com.gjg.casualchat.model.Model;
import com.gjg.casualchat.model.bean.UserInfo;
import com.gjg.casualchat.utils.Tools;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by JunGuang_Gao
 * on 2017/2/12  20:58.
 */

public class LoginActivity extends Activity {
    private EditText et_username;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_username= (EditText) this.findViewById(R.id.et_username);
        et_password= (EditText) this.findViewById(R.id.et_password);

    }


    public void register(View view){
        final String register_username=et_username.getText().toString();
        final String register_password=et_password.getText().toString();
        //校验用户名和密码
        if (TextUtils.isEmpty(register_username)||TextUtils.isEmpty(register_password)){
            Tools.showToastMessage(this,"用户名或密码不能为空");
            return;
        }

        //去服务器注册账号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(register_username,register_password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showToastMessage(LoginActivity.this,"注册成功,请直接登录！");
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showToastMessage(LoginActivity.this,"注册失败，请重新注册");
                        }
                    });

                }
            }
        });

    }

    public void login(View view){
        final String login_username=et_username.getText().toString();
        final String login_password=et_password.getText().toString();
        //校验用户名和密码
        if (TextUtils.isEmpty(login_username)||TextUtils.isEmpty(login_password)){
            Tools.showToastMessage(this,"用户名或密码不能为空");
            return;
        }

        //登录账户
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(login_username, login_password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Model.getInstance().loginSuccess(new UserInfo(login_username));

                        // 保存用户账号信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(login_username));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 提示登录成功
                                Tools.showToastMessage(LoginActivity.this, "登录成功");

                                // 跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                startActivity(intent);

                                finish();
                            }
                        });

                    }

                    @Override
                    public void onError(int i, final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 提示登录失败
                                Tools.showToastMessage(LoginActivity.this, "登录失败"+s);
                            }
                        });


                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }
}
