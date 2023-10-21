package com.example.spotify;

import android.graphics.Bitmap;

import java.util.Arrays;

public class PostModel {
    private int Pid;
    private String username, caption;
    byte[] img;

    public PostModel(String caption, byte[] img) {
        this.caption = caption;
        this.img = img;
    }
    public PostModel(int id, String username, String caption, byte[] img) {
        this.Pid = id;
        this.username = username;
        this.caption = caption;
        this.img=img;
    }

    public int getId() {
        return Pid;
    }

    public void setId(int id) {
        this.Pid = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "Pid=" + Pid +
                ", username='" + username + '\'' +
                ", caption='" + caption + '\'' +
                ", img=" + Arrays.toString(img) +
                '}';
    }
}
