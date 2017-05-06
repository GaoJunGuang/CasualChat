package com.gjg.casualchat.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gjg.casualchat.R;
import com.gjg.casualchat.model.Model;
import com.gjg.casualchat.model.bean.UserInfo;
import com.gjg.casualchat.utils.Tools;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JunGuang_Gao
 * on 2017/2/12  20:58.
 */

public class LoginActivity extends Activity {
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.bt_login)
    Button btLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        et_username = (EditText) this.findViewById(R.id.et_username);
        et_password = (EditText) this.findViewById(R.id.et_password);

    }


    @OnClick(R.id.bt_register)
    public void register(View view) {
        final String register_username = et_username.getText().toString();
        final String register_password = et_password.getText().toString();
        if (register_username.length() < 4 || register_username.length() > 10) {
            Tools.showToastMessage(this, "用户名的长度为4-10");
            return;
        }
        if (register_password.length() < 6 || register_password.length() > 12) {
            Tools.showToastMessage(this, "密码的长度为6-12");
            return;
        }
        Pattern var3 = Pattern.compile("^[a-zA-Z0-9_@#]+$");
        boolean isRight = var3.matcher(register_password).find();
        if (!isRight) {
            Tools.showToastMessage(this, "密码只能是数字、字母、下划线、@和#");
            return;
        }
        //校验用户名和密码
        if (TextUtils.isEmpty(register_username) || TextUtils.isEmpty(register_password)) {
            Tools.showToastMessage(this, "用户名或密码不能为空");
            return;
        }

        //去服务器注册账号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(register_username, register_password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showToastMessage(LoginActivity.this, "注册成功,请直接登录！");
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showToastMessage(LoginActivity.this, "注册失败，请重新注册");
                        }
                    });

                }
            }
        });

    }

    @OnClick(R.id.bt_login)
    public void login(View view) {
        final String login_username = et_username.getText().toString();
        final String login_password = et_password.getText().toString();
        //校验用户名和密码
        if (TextUtils.isEmpty(login_username) || TextUtils.isEmpty(login_password)) {
            Tools.showToastMessage(this, "用户名或密码不能为空");
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
                                Tools.showToastMessage(LoginActivity.this, "登录失败" + s);
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
