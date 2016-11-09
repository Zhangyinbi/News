package com.example.viewpager.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
public class PrefUtils {
    public static final String PREF_NAME="config";

    public static String getString(Context ctx, String key, String DefaultValue) {
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getString(key,DefaultValue);
    }public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp=ctx.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
       sp.edit().putString(key,value).commit();
    }

}

