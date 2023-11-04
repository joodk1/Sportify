package com.example.spotify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.spotify.ModelClass.UserDataClass;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Posts.db";
    public static final String POSTS_TABLE = "POSTS_Table";
    public static final String COLUMN_USERID = "POST_User_ID";
    public static final String COLUMN_USER_NAME = "POST_User_Name";
    public static final String COLUMN_POST_CAPTION = "POST_Caption";
    public static final String COLUMN_POST_LIKE = "POST_Like";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_POST_PHOTO = "POST_Image";

    public static final String USER_TABLE = "User_Table";
    public static final String USER_ID = "User_Id";
    public static final String USER_NAME = "User_Name";
    public static final String USER_AGE = "User_Age";
    public static final String USER_UNAME = "User_Uname";
    public static final String USER_EMAIL = "User_Email";
    public static final String USER_PASSWORD = "User_Password";
    public String userUname;


    public DataBaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    // when creating the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + POSTS_TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERID + " TEXT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_POST_CAPTION + " TEXT, "
                + COLUMN_POST_LIKE + " TEXT, "
                + COLUMN_POST_PHOTO + " TEXT" + ")";
        sqLiteDatabase.execSQL(createTableStatement);

        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME + " TEXT, "
                + USER_AGE + " TEXT, "
                + USER_UNAME + " TEXT, "
                + USER_EMAIL + " TEXT, "
                + USER_PASSWORD + " TEXT" + ")";

        sqLiteDatabase.execSQL(createUserTableStatement);
    }


    // when upgrading
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + POSTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public boolean addOne(PostModel postModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERID, String.valueOf(postModel.getUserid()));
        cv.put(COLUMN_USER_NAME, String.valueOf(postModel.getUsername()));
        cv.put(COLUMN_POST_CAPTION, postModel.getCaption());
        cv.put(COLUMN_POST_LIKE, "");
        cv.put(COLUMN_POST_PHOTO, postModel.getImg());
        long insert = db.insert(POSTS_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public boolean check_user(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + USER_TABLE + " WHERE " + USER_UNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }


    public long add_User_Data(UserDataClass userDataClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, userDataClass.User_Name);
        cv.put(USER_AGE, userDataClass.User_Age);
        cv.put(USER_UNAME, userDataClass.User_Uname);
        cv.put(USER_EMAIL, userDataClass.User_Email);
        cv.put(USER_PASSWORD, userDataClass.User_Password);
        long insert = db.insert(USER_TABLE, null, cv);
        db.close();
        return insert;
    }

    public ArrayList<UserDataClass> login_user(String username, String password) {

        ArrayList<UserDataClass> userDataClasses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_UNAME + "=? AND " + USER_PASSWORD + "=?", new String[]{username, password});

        if (cursor.moveToFirst()) {
            do {
                userDataClasses.add(
                        new UserDataClass(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5)
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userDataClasses;
    }


    public boolean UpdateOne(PostModel postModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_POST_CAPTION, postModel.getCaption());
        cv.put(COLUMN_POST_PHOTO, postModel.getImg());

        long insert = db.update(POSTS_TABLE, cv, COLUMN_ID + " = ?", new String[]{"" + postModel.getPid()});
        db.close();
        return insert != -1;
    }


    public boolean UpdateOne_likePost(int pid, String like) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_POST_LIKE, like);

        long insert = db.update(POSTS_TABLE, cv, COLUMN_ID + " = ?", new String[]{"" + pid});
        db.close();
        return insert != -1;
    }


    public void DeleteOne(PostModel studentMod) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + POSTS_TABLE + " where " + COLUMN_ID + "='" + studentMod.getPid() + "'");
        db.close();
        //close
    }

    public ArrayList<PostModel> getEveryone() {
        ArrayList<PostModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + POSTS_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                returnList.add(
                        new PostModel(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5)
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return returnList;
    }

    public PostModel get_postData(int select_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + POSTS_TABLE + " WHERE " + COLUMN_ID + " = " + select_id;

        PostModel postModel;
        try (Cursor cursor = db.rawQuery(query, null)) {

            postModel = new PostModel("", null);

            if (cursor.moveToFirst()) {
                do {
                    postModel = new PostModel(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5));
                } while (cursor.moveToNext());
            }
        }
        return postModel;
    }
}