package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class WorkMyTopPostsFragment extends WorkPostListFragment {
    public WorkMyTopPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("work").child("user-posts").child(myUserId)
                .orderByChild("starCount");
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }
}
