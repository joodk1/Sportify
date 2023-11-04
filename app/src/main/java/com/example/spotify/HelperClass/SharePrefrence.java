package com.example.spotify.HelperClass;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.spotify.ModelClass.UserDataClass;
import com.google.gson.Gson;

public class SharePrefrence {

    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;

    public SharePrefrence(Activity activity) {
        sharedPref = activity.getSharedPreferences("Spotify", 0);
        editor = sharedPref.edit();
    }

    public UserDataClass get_userdata() {
        return new Gson().fromJson(sharedPref.getString("userdata", ""), UserDataClass.class);
    }

    public void set_userdata(UserDataClass userDataClass) {
        editor.putString("userdata", new Gson().toJson(userDataClass));
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
