package com.rtw181204.androidproject;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoTradeWriteActivity extends BaseActivity {

    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference database;
    // [END declare_database_ref]

    private EditText mTitleField;
    private EditText mBodyField;
    private FloatingActionButton mSubmitButton;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
    String date = simpleDateFormat.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_trade_write);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("장터작성");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        database = FirebaseDatabase.getInstance().getReference();
        mTitleField = findViewById(R.id.fieldTitle);
        mBodyField = findViewById(R.id.fieldBody);
        mSubmitButton = findViewById(R.id.fabSubmitPost);
    }


    private void submitPost(){
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();

        if(TextUtils.isEmpty(title)){
            mTitleField.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        setEditingEnabled(false);
        Toast.makeText(this, "글 작성중...", Toast.LENGTH_SHORT).show();

        final String userId = getUid();


        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user == null){
                    Toast.makeText(InfoTradeWriteActivity.this,
                            "Error: could not fetch user.",Toast.LENGTH_SHORT).show();
                }
                else{
                    G.bead+=1;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    G.beadHistory = sdf.format(new Date());



                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("bead", G.bead);


                    mDatabase.child("users").child(userId).updateChildren(taskMap);




                    // FirebaseDatabase.getInstance().getReference("users").child(userId).setValue(new User(G.nickName, G.id, G.bead+2, G.date, G.attCheck));
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", "장터게시판");
                    map.put("content","1개");
                    map.put("time",G.beadHistory);


                    database.child("users").child(userId).child("beadHistory").push().setValue(map);


                    writeNewPost(userId, user.username, title, body);
                }

                setEditingEnabled(true);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                setEditingEnabled(true);
            }
        });



    }

    private void setEditingEnabled(boolean enabled){
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);

        if(enabled){
            mSubmitButton.show();
        }
        else {
            mSubmitButton.hide();
        }
    }

    private void writeNewPost(String userId, String username, String title, String body) {

        String key = mDatabase.child("trade").child("posts").push().getKey();
        Post post = new Post(userId, username, title, body, date);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/trade/posts/"+key, postValues);
        childUpdates.put("/trade/user-posts/"+userId +"/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


    public void clickSubmitPost(View view) {

        submitPost();
    }

}
