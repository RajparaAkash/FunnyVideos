package com.exmple.funnyvideo.Model;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPrefrence {

    public static final String USER_PREFS = "USER PREFS";
    public SharedPreferences appSharedPref;
    public SharedPreferences.Editor prefEditor;

    public String select = "device";




    public AppPrefrence(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences( USER_PREFS, 0 );
        this.appSharedPref = sharedPreferences;
        this.prefEditor = sharedPreferences.edit();
    }

    public String get_select() {
        return this.appSharedPref.getString(this.select, "one");
    }

    public void set_select(String str) {
        this.prefEditor.putString(this.select, str).apply();
    }

    public boolean isSelected(int position) {
        return appSharedPref.getBoolean("selected_" + position, false);
    }

    public void set_select(int position) {
        prefEditor.putBoolean("selected_" + position, true).apply();
    }

    public void remove_select(int position) {
        prefEditor.remove("selected_" + position).apply();
    }
}