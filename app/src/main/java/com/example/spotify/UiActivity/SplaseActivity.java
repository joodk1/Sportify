package com.example.spotify.UiActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotify.HelperClass.Constant;
import com.example.spotify.HelperClass.SharePrefrence;
import com.example.spotify.MainActivity;
import com.example.spotify.ModelClass.UserDataClass;
import com.example.spotify.R;

public class SplaseActivity extends AppCompatActivity {

    SharePrefrence sharePrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splase);
        sharePrefrence = new SharePrefrence(this);
        Constant.userdata = new UserDataClass();
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (sharePrefrence.sharedPref.contains("userdata") && !sharePrefrence.get_userdata().toString().isEmpty()) {
                    Constant.userdata = sharePrefrence.get_userdata();
                    startActivity(new Intent(SplaseActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplaseActivity.this, LoginOptionActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}