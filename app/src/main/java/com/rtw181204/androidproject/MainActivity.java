package com.rtw181204.androidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager pager;
    MyAdapter adapter;



    String[] titles = new String[]{"구슬","정보방","설정"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tabLayout = findViewById(R.id.layout_tab);

        pager = findViewById(R.id.pager);
        adapter = new MyAdapter(getSupportFragmentManager());


        pager.setAdapter(adapter);


        //뷰페이저와 TabLayout을 연동하기
        tabLayout.setupWithViewPager(pager);

        //액션바에 서브타이틀 주기

        getSupportActionBar().setTitle("구슬");


        //탭이 변경되었을 때 타이틀 변경
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                getSupportActionBar().setTitle(titles[tab.getPosition()]);



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //아이콘을 주려면 별도의 추가작업 필요
        for(int i = 0 ; i<3; i++){
            tabLayout.getTabAt(i).setIcon(R.drawable.tab1_bead+i);


        }
        tabLayout.setBackgroundColor(Color.WHITE);

        Log.i("TAG", "메인 "+G.bead);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){

            startActivity(new Intent(this,LoginActivity.class));
            finish();


        }




    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);

        builder3.setMessage("앱을 종료하시겠습니까?");
        builder3.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder3.setPositiveButton("취소",null);
        builder3.show();
    }


    void loadData(){

        String uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);




                if(user==null)return;

                Log.i("AAA", "메인로드 "+G.nickName +  " , "+ G.manager);
                G.bead = user.bead;
                G.id = user.email;
                G.nickName = user.username;

                G.date =user.date;
                G.attCheck = user.check;
                G.patner = user.patner;
                G.applypatner = user.applypatner;
                G.manager = user.manager;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }







}
