package com.gjg.casualchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gjg.casualchat.CCApplication;

/**
 * Created by JunGuang_Gao
 * on 2017/2/19  22:32.
 * 类描述：
 */

public class SpUtils {

    private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
    private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
    private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
    private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";

    public static final String IS_NEW_INVITE = "is_new_invite";// 新的邀请标记
    private static SpUtils instance = new SpUtils();
    private static SharedPreferences mSp;

    private SpUtils() {

    }

    // 单例
    public static SpUtils getInstance() {
//        context.getSharedPreferences()
        if (mSp == null) {
            mSp = CCApplication.getGlobalApplication().getSharedPreferences("im", Context.MODE_PRIVATE);
        }

        return instance;
    }

    // 保存
    public void save(String key, Object value) {

        if (value instanceof String) {
            mSp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    // 获取数据的方法
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    // 获取boolean数据
    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    // 获取int类型数据
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }


    public void setSettingMsgNotification(boolean paramBoolean) {
        mSp.edit().putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
        mSp.edit().apply();
    }

    public boolean getSettingMsgNotification() {
        return mSp.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        mSp.edit().putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
        mSp.edit().apply();
    }

    public boolean getSettingMsgSound() {

        return mSp.getBoolean(SHARED_KEY_SETTING_SOUND, true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        mSp.edit().putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
        mSp.edit().apply();
    }

    public boolean getSettingMsgVibrate() {
        return mSp.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        mSp.edit().putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
        mSp.edit().apply();
    }

    public boolean getSettingMsgSpeaker() {
        return mSp.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
    }
}
