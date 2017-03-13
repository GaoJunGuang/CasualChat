package com.gjg.casualchat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by JunGuang_Gao
 * on 2017/2/12  23:11.
 * 类描述：工具集类
 */

public class Tools {
    public static void  showToastMessage(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
