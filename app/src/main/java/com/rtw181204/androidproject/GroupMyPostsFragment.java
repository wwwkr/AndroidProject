package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class GroupMyPostsFragment extends GroupPostListFragment {

    public GroupMyPostsFragment(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("group").child("user-posts").child(getUid());
    }
}
