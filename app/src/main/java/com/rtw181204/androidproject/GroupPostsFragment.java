package com.rtw181204.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class GroupPostsFragment extends GroupPostListFragment {
    public GroupPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("group").child("posts")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}
