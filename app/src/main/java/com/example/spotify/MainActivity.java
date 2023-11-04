package com.example.spotify;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.spotify.Adapter.ProfileAdapter;
import com.example.spotify.HelperClass.Constant;
import com.example.spotify.HelperClass.SharePrefrence;
import com.example.spotify.ModelClass.UserDataClass;
import com.example.spotify.UiActivity.LoginOptionActivity;
//import com.example.spotify.UiActivity.SelectPostActivity;
//import com.example.spotify.UiActivity.ShowCommentActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_add;
    RecyclerView rv_posts;
    PostAdapter postAdapter;
    DataBaseHelper dataBaseHelper;
    ArrayList<PostModel> viewList = new ArrayList<>();
    TextView ly_noData, txt_userData;
    ImageView bt_logout;
    SharePrefrence sharePrefrence;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        rv_posts = findViewById(R.id.rv_posts);
        ly_noData = findViewById(R.id.ly_nodata);
        bt_logout = findViewById(R.id.bt_logout);
        txt_userData = findViewById(R.id.txt_userdata);

        sharePrefrence = new SharePrefrence(this);
        txt_userData.setText("Welcome " + Constant.userdata.User_Name + "!");


        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Log-out");
                alert.setMessage("Are you sure you want to log-out?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sharePrefrence.clear();
                        startActivity(new Intent(MainActivity.this, LoginOptionActivity.class));
                        finishAffinity();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Post.class);
                startActivity(intent);
            }
        });
    }

    public void getData() {
        postAdapter = new PostAdapter(MainActivity.this, viewList, new PostAdapter.OnClick() {
            @Override
            public void OnEditClick(PostModel postmodel) {
                Intent intent = new Intent(MainActivity.this, Add_Post.class);
                intent.putExtra("select_id", postmodel.getPid());
                startActivity(intent);
            }

            @Override
            public void OnDeleteClick(PostModel postObject) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete Post");
                alert.setMessage("Are you sure you want to delete this post?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dataBaseHelper.DeleteOne(postObject);
                        viewList = dataBaseHelper.getEveryone();
                        if (viewList.size() > 0) ly_noData.setVisibility(View.GONE);
                        else ly_noData.setVisibility(View.VISIBLE);
                        Collections.reverse(viewList);
                        postAdapter.isChangeList(viewList);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }

            @Override
            public void OnLikeClick(PostModel postObject) {
                String likeData = postObject.getLike();
                if (likeData.contains(String.valueOf(Constant.userdata.User_ID))) {
                    likeData = likeData.replace("," + Constant.userdata.User_ID, "");
                    if (dataBaseHelper.UpdateOne_likePost(postObject.getPid(), likeData)) {
                        postObject.setLike(likeData);
                    }
                } else {
                    likeData = likeData + "," + Constant.userdata.User_ID;
                    if (dataBaseHelper.UpdateOne_likePost(postObject.getPid(), likeData)) {
                        postObject.setLike(likeData);
                    }
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void OnPostClick(PostModel postObject) {
                startActivity(new Intent(MainActivity.this, MainActivity.class)
                        .putExtra("postmodel", new Gson().toJson(postObject)));
            }
        });
        rv_posts.setHasFixedSize(true);
        rv_posts.setLayoutManager(new LinearLayoutManager(this));
        rv_posts.setAdapter(postAdapter);
    }



    @Override
    protected void onResume() {
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        viewList = (ArrayList<PostModel>) dataBaseHelper.getEveryone();
        Collections.reverse(viewList);

        if (viewList.size() > 0) {
            getData();
            ly_noData.setVisibility(View.GONE);
        } else ly_noData.setVisibility(View.VISIBLE);
        super.onResume();
    }
}
