package com.rtw181204.androidproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Page3Fragment extends Fragment {


    String[] items = new String[]{"닉네임변경", "비밀번호변경", "로그아웃", "계정탈퇴"};
    ImageView btnMyPage, btnBead, btnProfile, btnQuestion;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference profilesRef;

    ProgressDialog progressDialog;
    EditText et;
    EditText etId;
    Button btn;

    Button btnPsData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_page3,container,false);

        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(getContext());



        btnMyPage = view.findViewById(R.id.btn_myPage);
        btnBead = view.findViewById(R.id.btn_bead);
        btnProfile = view.findViewById(R.id.btn_myProfile);
        btnQuestion = view.findViewById(R.id.btn_question);

        btnMyPage.setOnClickListener(listener);
        btnBead.setOnClickListener(listener);
        btnProfile.setOnClickListener(listener);
        btnQuestion.setOnClickListener(listener);

        et = new EditText(getContext());
        et.setHint("E-mail address");
        et.setEms(18);


        etId = new EditText(getContext());
        etId.setHint("변경할 닉네임");
        etId.setEms(18);


        btn = new Button(getContext());

        btnPsData = view.findViewById(R.id.btn_PsData);

        btnPsData.setOnClickListener(listener2);




        return view;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()){

                case R.id.btn_myPage:

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {



                            if(items[index].equals("닉네임변경")){




                                AlertDialog.Builder builder4 = new AlertDialog.Builder(getContext()).setTitle("닉네임 변경").setMessage("닉네임을 변경하시겠습니까?")
                                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

                                                builder1.setTitle("닉네임 변경").setMessage("변경하실 닉네임을 입력해주세요.");

                                                if (etId.getParent() != null)
                                                    ((ViewGroup) etId.getParent()).removeView(etId);
                                                builder1.setView(etId);
                                                builder1.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if(etId.getText().toString().trim().length()==0){
                                                            Toast.makeText(getContext(), "최소 1글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        for(int i = 0 ; i < G.checkNick.size(); i++){


                                                           if(etId.getText().toString().equals(G.checkNick.get(i)))
                                                                 {
                                                                     Toast.makeText(getContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                                                     return;
                                                                 }
                                                            }
                                                        progressDialog.setMessage("처리중입니다. 잠시 기다려주세요...");
                                                        progressDialog.show();

                                                        String nickName = etId.getText().toString().trim();

                                                        G.nickName = nickName;

                                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                                        Map<String, Object> taskMap = new HashMap<String, Object>();
                                                        taskMap.put("username", G.nickName);

                                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(), "닉네임이 " + G.nickName +" 으로 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                                                progressDialog.cancel();


                                                            }
                                                        });




                                                    }
                                                }).setPositiveButton("취소",null).show();
                                            }
                                        }).setPositiveButton("취소",null);

                                builder4.show();





                            }else if(items[index].equals("비밀번호변경")){


                                AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext()).setTitle("비밀번호 변경").setMessage("비밀번호를 변경하시겠습니까?")
                                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (et.getParent() != null)
                                                    ((ViewGroup) et.getParent()).removeView(et);

                                                new AlertDialog.Builder(getContext()).setTitle("비밀번호 변경").setMessage("등록한 E-mail을 입력해주세요.").setView(et).setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if(TextUtils.isEmpty(et.getText().toString())){
                                                            Toast.makeText(getContext(), "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        progressDialog.setMessage("처리중입니다. 잠시 기다려주세요...");
                                                        progressDialog.show();
                                                        //비밀번호 재설정 이메일 보내기

                                                        String emailAddress = et.getText().toString().trim();

                                                        firebaseAuth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(), "이메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                                                                getActivity().finish();
                                                                startActivity(new Intent(getContext(), LoginActivity.class));
                                                            }
                                                        });

                                                        firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCanceledListener(new OnCanceledListener() {
                                                            @Override
                                                            public void onCanceled() {
                                                                Toast.makeText(getContext(), "이메일을 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                                progressDialog.cancel();
                                                            }
                                                        });



                                                    }
                                                }).setPositiveButton("취소",null).show();
                                            }
                                        }).setPositiveButton("취소",null);

                                builder3.show();



                            }else if(items[index].equals("로그아웃")){

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext()).setTitle("로그아웃").setMessage("로그아웃하시겠습니까?").setCancelable(false).setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseAuth.signOut();
                                        Toast.makeText(getContext(), "로그아웃되셨습니다.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                        getActivity().finish();
                                    }
                                }).setPositiveButton("취소",null);

                                builder1.show();




                            }else{//계정탈퇴



                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("계정을 삭제하시겠습니까?").setCancelable(false).setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        final String uid = user.getUid();
                                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                               firebaseDatabase = FirebaseDatabase.getInstance();
                                               profilesRef = firebaseDatabase.getReference();
                                               profilesRef.child("users").child(uid).setValue(null);


                                                Toast.makeText(getContext(), "계정이 삭제되었습니다.", Toast.LENGTH_LONG).show();

//                                                FirebaseDatabase.getInstance().getReference().child("applypatner").child(uid).removeValue();


                                                startActivity(new Intent(getContext(), LoginActivity.class));
                                            }
                                        });
                                    }
                                }).setPositiveButton("취소",null).show();



                            }
                        }
                    });

                    builder.show();
                    break;


                case R.id.btn_bead:


                        startActivity(new Intent(getContext(), BeadHistoryActivity.class));

                    break;

                case R.id.btn_myProfile:


                    new AlertDialog.Builder(getContext()).setTitle("내 프로필").setMessage("아이디 : "+G.id + "\n\n"+"닉네임 : "+G.nickName + "\n\n"+ "구　슬 : "+G.bead+"개\n\n").show();
                    break;

                case R.id.btn_question:


                    startActivity(new Intent(getContext(), MyPostsActivity.class));

                    break;
            }


        }
    };

    View.OnClickListener listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){



                case R.id.btn_PsData:

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/xodn1003/221488720800")));
                    break;
            }
        }
    };




}
