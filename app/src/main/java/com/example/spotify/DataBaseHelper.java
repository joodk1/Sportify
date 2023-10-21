package com.example.spotify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DDBNAME = "Postsss.db";
    public static final String POSTS_TABLE = "POSTS_Table";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_POST_CAPTION = "POST_CAPTION";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_POST_PHOTO = "img";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DDBNAME, null, 1);
    }

    // when creating the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + POSTS_TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_POST_CAPTION + " TEXT, " + COLUMN_POST_PHOTO + " BLOB" + ")";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    // when upgrading
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean addOne(PostModel postModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, postModel.getUsername());
        cv.put(COLUMN_POST_CAPTION, postModel.getCaption());
        cv.put(COLUMN_POST_PHOTO, postModel.getImg());
        //cv.put(COLUMN_ID, postModel.getPid());
        long insert = db.insert(POSTS_TABLE, null, cv);
        db.close();
        return insert != -1;
    }


    public boolean UpdateOne(PostModel postModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, postModel.getUsername());
        cv.put(COLUMN_POST_CAPTION, postModel.getCaption());
        cv.put(COLUMN_POST_PHOTO, postModel.getImg());

        long insert = db.update(POSTS_TABLE, cv, COLUMN_ID + " = ?", new String[]{"" + postModel.getPid()});
        db.close();
        return insert != -1;
    }


    public void DeleteOne(PostModel studentMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + POSTS_TABLE + " where " + COLUMN_ID + "='" + studentMod.getId() + "'");
        db.close();
        //close
    }

    public ArrayList<PostModel> getEveryone() {
        ArrayList<PostModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //  String queryString = "Select * from " + POSTS_TABLE;
        Cursor cursor = db.rawQuery("SELECT * FROM " + POSTS_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                returnList.add(
                        new PostModel(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getBlob(3)
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return returnList;
    }

    public PostModel get_postdata(int selet_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + POSTS_TABLE + " WHERE " + COLUMN_ID + " = " + selet_id;

        Cursor cursor = db.rawQuery(query, null);

        PostModel postmodel = new PostModel("",null);

        if (cursor.moveToFirst()) {
            do {
                postmodel = new PostModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getBlob(3));
            } while (cursor.moveToNext());
        }
        return postmodel;
    }
}