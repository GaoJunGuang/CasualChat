package com.gjg.casualchat.controller.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gjg.casualchat.R;
import com.gjg.casualchat.controller.activity.LoginActivity;
import com.gjg.casualchat.model.Model;
import com.gjg.casualchat.model.SettingModel;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by JunGuang_Gao
 * on 2017/2/14  22:29.
 * 类描述：
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private Button bt_setting_out;
    /**
     * new message notification
     */
    private RelativeLayout rl_switch_notification;
    /**
     * sound
     */
    private RelativeLayout rl_switch_sound;
    /**
     * vibration
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * speaker
     */
    private RelativeLayout rl_switch_speaker;
    private TextView textview1, textview2,tv_scan;
    private EaseSwitchButton notifySwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;
    private EaseSwitchButton speakerSwitch;
    private SettingModel settingsModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.fragment_setting,null);
        initView(view);
        return view;
    }

    private void initListener() {
        rl_switch_notification.setOnClickListener(this);
        rl_switch_sound.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        rl_switch_speaker.setOnClickListener(this);
        tv_scan.setOnClickListener(this);
    }

    private void initView(View view) {
        bt_setting_out = (Button) view.findViewById(R.id.bt_setting_out);

        rl_switch_notification = (RelativeLayout) view.findViewById(R.id.rl_switch_notification);
        rl_switch_sound = (RelativeLayout) view.findViewById(R.id.rl_switch_sound);
        rl_switch_vibrate = (RelativeLayout) view.findViewById(R.id.rl_switch_vibrate);
        rl_switch_speaker = (RelativeLayout) view.findViewById(R.id.rl_switch_speakers);

        textview1 = (TextView) view.findViewById(R.id.textview1);
        textview2 = (TextView) view.findViewById(R.id.textview2);
        tv_scan= (TextView) view.findViewById(R.id.tv_scan);

        notifySwitch = (EaseSwitchButton) view.findViewById(R.id.switch_notification);
        soundSwitch = (EaseSwitchButton) view.findViewById(R.id.switch_sound);
        vibrateSwitch = (EaseSwitchButton) view.findViewById(R.id.switch_vibrate);
        speakerSwitch = (EaseSwitchButton) view.findViewById(R.id.switch_speaker);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();

    }

    private void initData() {
        settingsModel =new SettingModel();

        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            notifySwitch.openSwitch();
        } else {
            notifySwitch.closeSwitch();
        }

        // sound notification is switched on or not?
        if (settingsModel.getSettingMsgSound()) {
            soundSwitch.openSwitch();
        } else {
            soundSwitch.closeSwitch();
        }

        // vibrate notification is switched on or not?
        if (settingsModel.getSettingMsgVibrate()) {
            vibrateSwitch.openSwitch();
        } else {
            vibrateSwitch.closeSwitch();
        }

        // the speaker is switched on or not?
        if (settingsModel.getSettingMsgSpeaker()) {
            speakerSwitch.openSwitch();
        } else {
            speakerSwitch.closeSwitch();
        }

        // 在button上显示当前用户名称
        bt_setting_out.setText("退出登录 (" + EMClient.getInstance().getCurrentUser() + ")");

        // 退出登录的逻辑处理
        bt_setting_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        // 登录环信服务器退出登录
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                                // 关闭DBHelper
                                //Model.getInstance().getDbManager().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 更新ui显示
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();

                                        // 回到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);

                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });

                            }

                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "退出失败" + s, Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_switch_notification:
                if (notifySwitch.isSwitchOpen()) {
                    notifySwitch.closeSwitch();
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);
                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifySwitch.openSwitch();
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case R.id.rl_switch_sound:
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.rl_switch_vibrate:
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_speakers:
                if (speakerSwitch.isSwitchOpen()) {
                    speakerSwitch.closeSwitch();
                    settingsModel.setSettingMsgSpeaker(false);
                } else {
                    speakerSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;

            case R.id.tv_scan:
                Intent intent=new Intent(getActivity(),CaptureActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");


            Dialog dialog=new Dialog(getActivity());
            dialog.setTitle("扫描结果");
            TextView textView=new TextView(getActivity());
            textView.setText(result);
            dialog.setContentView(textView);
            dialog.show();
        }
    }
}
