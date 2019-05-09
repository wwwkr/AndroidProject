package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class WorkMyPostsFragment extends WorkPostListFragment {

    public WorkMyPostsFragment(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("work").child("user-posts").child(getUid());
    }
}
