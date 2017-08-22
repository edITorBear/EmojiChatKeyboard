package com.kuro.emojichatkeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by nnv on 2017/8/17.
 */

public class SPUtil {

    private static final String NAME = "lexing";
    public static final String DEVICE_ID = "device_id";
    public static final String USER = "user";
    public static final String SPEECH = "speech";
    public static final String UPDATE = "update";
    public static final String PHONE = "phone";
    public static final String SUB_BUSINESS = "sub_business";
    public static final String KEYBOARD_HEIGHT = "keyboard_height";
    public static final String RULE = "rule";

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        value = Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, null);
        return value != null ? new String(Base64.decode(value, Base64.DEFAULT)) : null;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void putInteger(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInteger(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static void clear(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public static <T> T getObject(Context context, String key, Class<T> clazz) {
        String value = getString(context, key);
        return new Gson().fromJson(value, clazz);
    }

    public static <T> T getObject(Context context, String key, Type type) {
        String value = getString(context, key);
        return new Gson().fromJson(value, type);
    }

}
