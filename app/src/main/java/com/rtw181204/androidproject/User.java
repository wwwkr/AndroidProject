package com.rtw181204.androidproject;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public int bead;
    public String date;
    public boolean check;
    public boolean patner;
    public boolean applypatner;
    public boolean manager;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, int bead, String date, boolean check, boolean patner , boolean applypatner, boolean manager) {
        this.username = username;
        this.email = email;
        this.bead = bead;
        this.date = date;
        this.check = check;
        this.patner = patner;
        this.applypatner = applypatner;
        this.manager = manager;


    }

}
// [END blog_user_class]
