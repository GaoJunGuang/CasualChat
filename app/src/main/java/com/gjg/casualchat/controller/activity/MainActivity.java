package com.gjg.casualchat.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gjg.casualchat.R;
import com.gjg.casualchat.controller.fragment.ChatFragment;
import com.gjg.casualchat.controller.fragment.ContactListFragment;
import com.gjg.casualchat.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment=null;
                switch (checkedId){
                    case R.id.rb_conversation:
                        fragment=chatFragment;
                        break;
                    case R.id.rb_contact:
                        fragment=contactListFragment;
                        break;
                    case R.id.rb_setting:
                        fragment=settingFragment;
                        break;
                }
                //实现fragment的切换
                switchFragment(fragment);
            }
        });

        // 默认选择会话列表页面
        rg_main.check(R.id.rb_conversation);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main,fragment);

        //transaction.addToBackStack("name");//压栈

        transaction.commit();

    }

    private void initData() {

        chatFragment = new ChatFragment();

        contactListFragment = new ContactListFragment();

        settingFragment = new SettingFragment();
    }

    private void initView() {
        rg_main = (RadioGroup)findViewById(R.id.rg_main);

    }

   /*@Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {//出栈
            finish();
        }
    }*/

    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            long secondTime= System.currentTimeMillis();
            if(secondTime-firstTime>1000){
                firstTime=secondTime;
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                return true;
            }else {
                finish();
            }


        }
        return super.onKeyDown(keyCode, event);
    }
}
