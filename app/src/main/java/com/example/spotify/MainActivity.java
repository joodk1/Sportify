package com.example.spotify;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_add;
    RecyclerView rv_posts;
    PostAdapter AdapterObject;
    DataBaseHelper dataBaseHelper;
    String UserName;
    ArrayList<PostModel> viewList = new ArrayList<>();
    TextView ly_nodata;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        rv_posts = findViewById(R.id.rv_posts);
        ly_nodata = findViewById(R.id.ly_nodata);
        UserName = getIntent().getStringExtra("UserName");


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Post.class);
                startActivity(intent);
            }
        });
    }


    public void getData() {
        AdapterObject = new PostAdapter(MainActivity.this, viewList, new PostAdapter.OnClick() {
            @Override
            public void OnEditClick(PostModel postmodel) {
                Intent intent = new Intent(MainActivity.this, Add_Post.class);
                intent.putExtra("selet_id", postmodel.getId());
                startActivity(intent);
            }

            @Override
            public void OnDeleteClick(PostModel postObject) {


                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete Post");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dataBaseHelper.DeleteOne(postObject);
                        viewList = dataBaseHelper.getEveryone();
                        if (viewList.size() > 0) ly_nodata.setVisibility(View.GONE);
                        else ly_nodata.setVisibility(View.VISIBLE);
                        Collections.reverse(viewList);
                        AdapterObject.ischnagelist(viewList);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();




            }
        });
        rv_posts.setHasFixedSize(true);
        rv_posts.setLayoutManager(new LinearLayoutManager(this));
        rv_posts.setAdapter(AdapterObject);
    }

    @Override
    protected void onResume() {
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        viewList = (ArrayList<PostModel>) dataBaseHelper.getEveryone();
        Collections.reverse(viewList);

        if (viewList.size() > 0) {
            getData();
            ly_nodata.setVisibility(View.GONE);
        } else ly_nodata.setVisibility(View.VISIBLE);


        super.onResume();
    }
}
