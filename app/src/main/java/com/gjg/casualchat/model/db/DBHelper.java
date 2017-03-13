package com.gjg.casualchat.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gjg.casualchat.model.bean.ContactTable;
import com.gjg.casualchat.model.dao.InviteTable;

/**
 * Created by JunGuang_Gao
 * on 2017/2/19  19:51.
 * 类描述：
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建联系人的表
        db.execSQL(ContactTable.CREATE_TAB);

        // 创建邀请信息的表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
