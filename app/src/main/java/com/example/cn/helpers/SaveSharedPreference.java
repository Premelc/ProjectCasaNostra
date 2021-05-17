package com.example.cn.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.cn.model.Korisnik;
import com.google.gson.Gson;

public class SaveSharedPreference {
    static final String sessionUser = "Session";

    static SharedPreferences getSharedPreferences (Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setSessionUser(Context context, Korisnik user){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString(sessionUser, json);
        editor.commit();
    }

    public static Korisnik getSessionUser(Context context){
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(sessionUser, "");

        Korisnik user = gson.fromJson(json, Korisnik.class);

        return user;
    }
}
