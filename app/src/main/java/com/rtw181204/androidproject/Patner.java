package com.rtw181204.androidproject;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Patner {

    public String company;
    public String name;
    public String num;
    public String uid;
    public String id;
    public String nickName;
    public String imgUri;



    public Patner() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Patner(String company, String name, String num, String uid, String id, String nickName, String imgUri) {
        this.company = company;
        this.name = name;
        this.num = num;
        this.uid = uid;
        this.id = id;
        this.nickName = nickName;
        this.imgUri = imgUri;
    }
}
// [END blog_user_class]
