package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TipMyPostsFragment extends TipPostListFragment {

    public TipMyPostsFragment(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("tip").child("user-posts").child(getUid());
    }
}
