package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TradeMyPostsFragment extends TradePostListFragment {

    public TradeMyPostsFragment(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("trade").child("user-posts").child(getUid());
    }
}
