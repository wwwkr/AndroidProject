package com.rtw181204.androidproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {


    ImageView iv;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        iv = findViewById(R.id.iv);

        Animation ani = AnimationUtils.loadAnimation(this,R.anim.appear_logo);
        iv.startAnimation(ani);



        timer.schedule(task,3000);



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



            if(firebaseAuth.getCurrentUser()!=null){


                Log.i("TAG",firebaseAuth.getCurrentUser().getEmail());

                String uid =  firebaseAuth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        User user = dataSnapshot.getValue(User.class);

                        if(user!=null){
                            G.bead = user.bead;
                            G.id = user.email;
                            G.nickName = user.username;

                            G.date =user.date;
                            G.attCheck = user.check;
                            G.patner = user.patner;
                            G.applypatner = user.applypatner;

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

           /*     if(G.patner && !G.patnerUpload){

                    G.patnerUpload = true;

                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("patnerlist", G.nickName);
                    FirebaseDatabase.getInstance().getReference().child("patnerlist").push().updateChildren(taskMap);


                    Map<String, Object> taskMap2 = new HashMap<String, Object>();
                    taskMap.put("patnerupload", G.patnerUpload);

                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).updateChildren(taskMap2);

                }*/

                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            User user = snapshot.getValue(User.class);

                            if(user.patner){
                                G.patnerList.add(user.username);
                            }

                            G.checkNick.add(user.username);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        };


    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            if(G.patner){
                G.applypatner =G.patner;
            }

            finish();
        }
    };



}
