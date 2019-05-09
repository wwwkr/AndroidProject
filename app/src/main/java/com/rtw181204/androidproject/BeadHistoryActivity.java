package com.rtw181204.androidproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BeadHistoryActivity extends AppCompatActivity {

    ArrayList<BeadList> members = new ArrayList<>();

    ListView listView;

    BeadAdapter adapter;

    DatabaseReference beadRef;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bead_history);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("구슬내역");

        Toast.makeText(this, "구슬내역", Toast.LENGTH_SHORT).show();

        listView = findViewById(R.id.listview);

        adapter = new BeadAdapter(getLayoutInflater(), members);


        listView.setAdapter(adapter);




        beadRef = FirebaseDatabase.getInstance().getReference();


        beadRef.child("users").child(uid).child("beadHistory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    BeadList list = snapshot.getValue(BeadList.class);

                    members.add(0,list);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }



}
