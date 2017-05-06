package com.gjg.casualchat.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gjg.casualchat.R;
import com.gjg.casualchat.model.Model;
import com.gjg.casualchat.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gjg.casualchat.R.id.bt_add_add;
import static com.gjg.casualchat.R.id.et_add_name;
import static com.gjg.casualchat.R.id.rl_add;
import static com.gjg.casualchat.R.id.tv_add_find;
import static com.gjg.casualchat.R.id.tv_add_name;

public class AddContactActivity extends Activity {

    @BindView(tv_add_find)
    TextView tvAddFind;
    @BindView(et_add_name)
    EditText etAddName;
    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(tv_add_name)
    TextView tvAddName;
    @BindView(bt_add_add)
    Button btAddAdd;
    @BindView(rl_add)
    RelativeLayout rlAdd;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);


    }


    @OnClick(tv_add_find)
    public void searchFriend(View view) {
        find();
    }

    @OnClick(bt_add_add)
    public void addFriend(View view) {
        add();
    }

    @OnClick(R.id.iv_back)
    public void backTo(View view) {
        finish();
    }

    // 查找按钮的处理
    private void find() {
        // 获取输入的用户名称
        final String name = etAddName.getText().toString();

        // 校验输入的名称
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AddContactActivity.this, "输入的用户名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 去服务器判断当前用户是否存在
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去服务器判断当前查找的用户是否存在
                userInfo = new UserInfo(name);

                // 更新UI显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rlAdd.setVisibility(View.VISIBLE);
                        tvAddName.setText(userInfo.getName());
                    }
                });
            }
        });


    }

    // 添加按钮处理
    private void add() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(), "请求加您为好友");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "发送邀请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}
