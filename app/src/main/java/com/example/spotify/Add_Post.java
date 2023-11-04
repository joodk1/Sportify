package com.example.spotify;

import static com.example.spotify.HelperClass.Constant.getPathFromUri;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.spotify.HelperClass.Constant;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class Add_Post extends Activity {
    final static int PICK_IMAGE_REQUEST = 100;
    TextView btn_addpost, txt_title;
    EditText p_caption;
    ImageView p_image;
    DataBaseHelper dataBaseHelper;
    PostModel postmodel;
    String ImageFilePath = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost);

        btn_addpost = findViewById(R.id.btn_addpost);
        p_caption = findViewById(R.id.p_caption);
        p_image = findViewById(R.id.p_image);
        txt_title = findViewById(R.id.txt_titel);
        p_image.setImageResource(R.drawable.image);

        dataBaseHelper = new DataBaseHelper(Add_Post.this);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        get_intentData();

        btn_addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_focus();
                try {
                    String caption = p_caption.getText().toString().trim();
                    if (!caption.isEmpty() || ImageFilePath != null) {
                        PostModel postModel = new PostModel(caption, ImageFilePath);
                        boolean isInserted = false;
                        if (getIntent().hasExtra("select_id")) {
                            postmodel.setCaption(caption);
                            postmodel.setImg(ImageFilePath);
                            isInserted = dataBaseHelper.UpdateOne(postmodel);
                        } else {
                            postModel.setUserid(Constant.userdata.User_ID);
                            postModel.setUsername(Constant.userdata.User_Name);
                            isInserted = dataBaseHelper.addOne(postModel);
                        }

                        if (isInserted) {
                            Toast.makeText(Add_Post.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Add_Post.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Add_Post.this, "Failed to add post", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Add_Post.this, "Please add a picture or text", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Add_Post.this, "Enter valid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void get_intentData() {

        if (getIntent().hasExtra("select_id")) {
            btn_addpost.setText("Edit post");
            txt_title.setText("Confirm");

            postmodel = dataBaseHelper.get_postData(getIntent().getIntExtra("select_id", 0));
            if (postmodel.getImg() != null) {
                if (postmodel.getImg() != null) {
                    ImageFilePath = postmodel.getImg();
                    Glide.with(this).load(ImageFilePath).into(p_image);
                }
            }
            p_caption.setText(postmodel.getCaption().toString());
        }
    }


    public void ChooseImage(View ObjectView) {
        remove_focus();
        ImagePicker.with(this)
                .galleryOnly()
                .compress(500)
                .crop(1F, 1F)
                .saveDir(getExternalFilesDir("image"))
                .maxResultSize(600, 600)
                .start(PICK_IMAGE_REQUEST);
    }

    public void remove_focus() {
        p_caption.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(p_caption.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {

                ImageFilePath = getPathFromUri(Add_Post.this, data.getData());
                Glide.with(this).load(ImageFilePath).into(p_image);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
