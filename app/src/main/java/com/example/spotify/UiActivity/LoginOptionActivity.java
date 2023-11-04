package com.example.spotify.UiActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotify.DataBaseHelper;
import com.example.spotify.HelperClass.Constant;
import com.example.spotify.HelperClass.SharePrefrence;
import com.example.spotify.MainActivity;
import com.example.spotify.ModelClass.UserDataClass;
import com.example.spotify.R;

import java.util.ArrayList;

public class LoginOptionActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    EditText et_username, et_password;
    SharePrefrence sharePrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        dataBaseHelper = new DataBaseHelper(LoginOptionActivity.this);
        sharePrefrence = new SharePrefrence(this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

    }

    public void OnLoginOption(View view) {
        if (view.getId() == R.id.btn_login) {
            check_loginData();
        } else if (view.getId() == R.id.bt_singup) {
            startActivity(new Intent(LoginOptionActivity.this, SingupActivity.class));
        }
    }

    private void check_loginData() {

        clear_focus();

        if (et_username.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
        } else if (et_password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<UserDataClass> userdatalist = new ArrayList<>();
            userdatalist = dataBaseHelper.login_user(et_username.getText().toString().trim(), et_password.getText().toString().trim());

            if (userdatalist.size() > 0) {
                sharePrefrence.set_userdata(userdatalist.get(0));
                Constant.userdata = sharePrefrence.get_userdata();
                startActivity(new Intent(LoginOptionActivity.this, MainActivity.class));
                finishAffinity();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clear_focus() {
        et_username.clearFocus();
        et_password.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
    }
}