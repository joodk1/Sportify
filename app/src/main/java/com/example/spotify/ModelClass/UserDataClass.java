package com.example.spotify.ModelClass;

public class UserDataClass {


   public int User_ID = 0;
   public String User_Name = "";
   public String  User_Age = "";
   public String  User_Uname = "";
   public String  User_Email = "";
   public String  User_Password = "";


    public UserDataClass() {

    }

    public UserDataClass(int user_Id, String user_Name, String user_Age, String user_Uname, String user_Email, String user_Password) {
        User_ID = user_Id;
        User_Name = user_Name;
        User_Age = user_Age;
        User_Uname = user_Uname;
        User_Email = user_Email;
        User_Password = user_Password;
    }
}
