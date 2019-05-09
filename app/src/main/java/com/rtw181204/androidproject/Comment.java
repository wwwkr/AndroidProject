package com.rtw181204.androidproject;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public String date;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text, String date) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.date = date;
    }

}
// [END comment_class]
