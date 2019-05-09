package com.rtw181204.androidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Page1Fragment extends Fragment {



    Button btn_history, btn_atten, btn_tip, btn_company, btnPatner ,btnPatnerAlliance;
    TextView tvBead;
    ImageView btn;


    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DatabaseReference database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_page1,container,false);


        btn_history = view.findViewById(R.id.btn_history);
        btn_atten = view.findViewById(R.id.btn_atten);
        btn_tip = view.findViewById(R.id.btn_tip);
        btn_company = view.findViewById(R.id.btn_company);

        tvBead = view.findViewById(R.id.tv_bead);
        btn = view.findViewById(R.id.btn);
        btnPatner = view.findViewById(R.id.btn_patner);
        btnPatnerAlliance = view.findViewById(R.id.btn_patnerAlliance);


        btn_history.setOnClickListener(listener);
        btn_atten.setOnClickListener(listener);
        btn_tip.setOnClickListener(listener);
        btn_company.setOnClickListener(listener);

        btnPatner.setOnClickListener(listener);
        btnPatnerAlliance.setOnClickListener(listener);

        database = FirebaseDatabase.getInstance().getReference();
//
//      String uid = getUid();


        Log.i("TAG", "프래그먼트 " + G.attCheck);


        Log.i("AAA", G.nickName + " , " + G.manager);

        if(G.manager){
            btnPatnerAlliance.setVisibility(View.VISIBLE);



        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBead.setText(G.bead+"");
            }
        });


        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                tvBead.setText(G.bead+"");
                if(G.manager){
                    btnPatnerAlliance.setVisibility(View.VISIBLE);
                }



                //TEST///
                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        G.patnerList.clear();
                        G.checkNick.clear();

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

                //TEST

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                tvBead.setText(G.bead+"");

                if(G.manager){
                    btnPatnerAlliance.setVisibility(View.VISIBLE);
                }
                //TEST///
                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        G.patnerList.clear();
                        G.checkNick.clear();

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

                //TEST


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        tvBead.setText(G.bead+"");

        return view;
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.btn_history:

                    Intent intent = new Intent(getContext(),BeadHistoryActivity.class);


                    startActivity(intent);



                    break;


                case R.id.btn_atten:


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("출석체크");
                    builder1.setMessage("출석체크를 하시겠습니까?");
                    builder1.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(G.attCheck){
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
                                String date = simpleDateFormat.format(new Date());


                                int before =  Integer.parseInt(G.date);
                                int after = Integer.parseInt(date);
                                if( before+1 <= after){
                                    G.attCheck = false;
                                }


                            }

                            if(G.attCheck){
                                Toast.makeText(getContext(), "이미 출석체크를 하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            G.attCheck = true;

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
                            String date = simpleDateFormat.format(new Date());

                            G.date = date;


                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:dd");
                            G.beadHistory = sdf.format(new Date());

                            G.bead+=10;
                            tvBead.setText(G.bead+"");

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                            Map<String, Object> taskMap = new HashMap<String, Object>();
                            taskMap.put("bead", G.bead);
                            taskMap.put("date", G.date);
                            taskMap.put("check", G.attCheck);

                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "구슬 10개가 적립되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });




                            //FirebaseDatabase.getInstance().getReference("users").child(uid).child("beadHistory").push().setValue(new BeadList("출석체크", "10개", G.beadHistory));

                            Map<String, Object> map = new HashMap<>();
                            map.put("title", "출석체크");
                            map.put("content","10개");
                            map.put("time",G.beadHistory);


                            database.child("users").child(uid).child("beadHistory").push().setValue(map);





                        }
                    });
                    builder1.setPositiveButton("취소",null);

                    builder1.show();
                    break;

                case R.id.btn_tip:


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("구슬 모으는법");
                    builder.setMessage("1. 출석체크 1일 1회(구슬 10개 획득)\n\n2. 게시물 작성\n    팁 게시판 1회 3개, 그 외 게시판 1개 지급");
                    builder.setPositiveButton("확인",null);
                    builder.show();

                    break;

                case R.id.btn_company:

                    if(G.patner) {
                        Toast.makeText(getContext(), "제휴업소 " + G.nickName + "님 반갑습니다.", Toast.LENGTH_SHORT).show();
                    }


                    AlertDialog.Builder builder4 = new AlertDialog.Builder(getContext());
                    builder4.setTitle("제휴업소 목록");

                    StringBuffer buffer = new StringBuffer();

                    for(int i = 0 ; i < G.patnerList.size() ; i ++){
                        buffer.append(G.patnerList.get(i)+ "\n");
                    }

                    builder4.setMessage(buffer.toString());
                    builder4.setNegativeButton("확인", null);
                    builder4.show();



                    break;

                case R.id.btn_patner:

                    if(G.patner) {
                        Toast.makeText(getContext(), "이미 제휴업소로 등록되어있습니다. ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(G.applypatner){
                        Toast.makeText(getContext(), "이미 제휴업소 신청중입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext());
                    builder3.setTitle("제휴업소");
                    builder3.setMessage("현재 제휴업소가 아닙니다.\n\n제휴업소를 신청하시겠습니까?");
                    builder3.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getContext(), InfoAllianceActivity.class));
                        }
                    });
                    builder3.setPositiveButton("취소",null);
                    builder3.show();

                    break;

                case R.id.btn_patnerAlliance:

                    startActivity(new Intent(getContext(),PatnerAllianceActivity.class));
                    break;



            }

        }
    };


}
