package com.example.spotify.UiActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotify.DataBaseHelper;
import com.example.spotify.HelperClass.Constant;
import com.example.spotify.HelperClass.SharePrefrence;
import com.example.spotify.MainActivity;
import com.example.spotify.ModelClass.UserDataClass;
import com.example.spotify.R;

public class SingupActivity extends AppCompatActivity {

    EditText et_name, et_age, et_username, et_email, et_password;
    TextView sign_up;
    DataBaseHelper dataBaseHelper;
    SharePrefrence sharePrefrence;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        dataBaseHelper = new DataBaseHelper(SingupActivity.this);
        sharePrefrence = new SharePrefrence(this);

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        sign_up = findViewById(R.id.sign_up);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void check_singupData() {

        int age = Integer.parseInt(et_age.getText().toString().trim());

        if (et_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (et_age.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Age", Toast.LENGTH_SHORT).show();
            return;
        } else if (age < 15) {
            Toast.makeText(this, "User Must Be At Least 15 Years Old", Toast.LENGTH_SHORT).show();
            return;
        } else if (et_username.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidEmail(et_email.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (et_password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (et_password.getText().toString().trim().length() < 8) {
            Toast.makeText(this, "Password Must Contain At Least 8 Characters", Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);


        UserDataClass userDataClass = new UserDataClass(0, et_name.getText().toString().trim(),
                et_age.getText().toString().trim(), et_username.getText().toString().trim(),
                et_email.getText().toString().trim(), et_password.getText().toString().trim());

        if (!dataBaseHelper.check_user(et_username.getText().toString().trim())) {
            long result = dataBaseHelper.add_User_Data(userDataClass);
            if (result != -1) {
                userDataClass.User_ID = (int) result;
                sharePrefrence.set_userdata(userDataClass);
                Toast.makeText(this, "Sign-up Successful", Toast.LENGTH_SHORT).show();
                Constant.userdata = sharePrefrence.get_userdata();
                startActivity(new Intent(SingupActivity.this, MainActivity.class));
                finishAffinity();
            } else {
                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Username is taken", Toast.LENGTH_SHORT).show();
        }
    }

    public void Singup_Click(View view) {
        check_singupData();
    }

    public Boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}