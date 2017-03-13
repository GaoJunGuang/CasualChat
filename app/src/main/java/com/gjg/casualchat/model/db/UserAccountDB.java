package com.gjg.casualchat.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gjg.casualchat.model.dao.UserAccountTable;

/**
 * Created by JunGuang_Gao
 * on 2017/2/13  22:30.
 * 类描述：
 */

public class UserAccountDB extends SQLiteOpenHelper {
    public UserAccountDB(Context context) {
        super(context, "account.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库表的语句
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
