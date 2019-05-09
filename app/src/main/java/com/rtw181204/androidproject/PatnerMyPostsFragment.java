package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class PatnerMyPostsFragment extends PatnerPostListFragment {

    public PatnerMyPostsFragment(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("patner").child("user-posts").child(getUid());
    }
}
