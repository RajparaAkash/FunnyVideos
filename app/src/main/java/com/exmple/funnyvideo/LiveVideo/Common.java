package com.exmple.funnyvideo.LiveVideo;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Common {
    public static void setPreferenceString(Context c, String pref, String val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
//        return pref;
    }

    public static void setPreferenceString(Context c, String pref, boolean val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();
        //return pref;
    }

    public static String getPreferenceString(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref,
                val);
    }

    public static Boolean getPreferenceString(Context c, String pref, boolean val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(pref,
                val);
    }

}