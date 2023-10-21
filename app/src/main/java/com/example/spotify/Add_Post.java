package com.example.spotify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

public class Add_Post extends Activity {
    TextView btn_addpost;
    EditText p_caption;
    ListView lv_postslist;
    ImageView p_image;
    ArrayAdapter<PostModel> postsArrayAdapter;
    DataBaseHelper dataBaseHelper;
    Bitmap ImageToStore;
    final static int PICK_IMAGE_REQUEST = 100;
    Uri ImageFilePath;
    PostModel postmodel;
    TextView txt_titel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost);

        btn_addpost = findViewById(R.id.btn_addpost);
        p_caption = findViewById(R.id.p_caption);
        p_image = findViewById(R.id.p_image);
        txt_titel = findViewById(R.id.txt_titel);

        dataBaseHelper = new DataBaseHelper(Add_Post.this);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        get_intentdata();

        btn_addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_focuse();
                try {
                    String caption = p_caption.getText().toString().trim();
                    if (!caption.isEmpty() || ImageToStore != null) {
                        PostModel postModel = new PostModel(caption, getBytes(ImageToStore));
                        boolean isInserted = false;
                        if (getIntent().hasExtra("selet_id")) {
                            postmodel.setCaption(caption);
                            postmodel.setImg(getBytes(ImageToStore));
                            isInserted = dataBaseHelper.UpdateOne(postmodel);
                        } else {
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
                        Toast.makeText(Add_Post.this, "Please Enter Image or Caption", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(Add_Post.this, "Enter valid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void get_intentdata() {

        if (getIntent().hasExtra("selet_id")) {
            btn_addpost.setText("Edit post");
            txt_titel.setText("Edit post");

            postmodel = dataBaseHelper.get_postdata(getIntent().getIntExtra("selet_id", 0));
            if (postmodel.getImg() != null) {
                if (postmodel.getImg() != null) ImageToStore = getImage(postmodel.getImg());
                if (ImageToStore != null) p_image.setImageBitmap(ImageToStore);
            }
            p_caption.setText(postmodel.getCaption().toString());
        }
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    public void ChooseImage(View ObjectView) {
        remove_focuse();
       /* try {
            Intent ObjectIntent = new Intent();
            ObjectIntent.setType("image/*");
            ObjectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(ObjectIntent, "Select Image"), PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/


        ImagePicker.with(this)
                .galleryOnly()
                .compress(500)
                .crop(1F, 1F)
                .maxResultSize(600, 600)
                .start(PICK_IMAGE_REQUEST);
    }

    public void remove_focuse() {
        p_caption.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(p_caption.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                ImageFilePath = data.getData();
                ImageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageFilePath);
                p_image.setImageBitmap(ImageToStore);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
