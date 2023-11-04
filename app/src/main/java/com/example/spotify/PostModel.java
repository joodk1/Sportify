package com.example.spotify;

public class PostModel {
    private int Pid, userid;
    private String caption;
    private String username = "";
    String img, like;

    public PostModel(String caption, String img) {
        this.caption = caption;
        this.img = img;
    }
    public PostModel(int id, int userid, String username,  String caption, String like, String img) {
        this.Pid = id;
        this.userid = userid;
        this.username = username;
        this.caption = caption;
        this.like = like;
        this.img = img;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getLike() {
        return like;
    }
    public void setLike(String like) {
        this.like = like;
    }

    public void setId(int id) {
        this.Pid = id;
    }

    public int getPid() {
        return Pid;
    }
    public void setPid(int pid) {
        Pid = pid;
    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }

}
