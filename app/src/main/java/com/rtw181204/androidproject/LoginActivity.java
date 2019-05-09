package com.rtw181204.androidproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

        EditText et_id, et_pass;

        TextView btn_findPass, btn_Acc;


        String sId, sPass;

        ProgressDialog progressDialog;
            //define firebase object
         FirebaseAuth firebaseAuth;


         EditText et;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            firebaseAuth = FirebaseAuth.getInstance();


            Log.i("TAG", "AAA");

            if(firebaseAuth.getCurrentUser()!=null){


                loadData();

                finish();

                startActivity(new Intent(this, MainActivity.class));
            }

            et_id = findViewById(R.id.et_id);
            et_pass = findViewById(R.id.et_password);
            progressDialog = new ProgressDialog(this);

            et = new EditText(this);
            et.setHint("E-mail address");
            et.setEms(18);


    }

    private void userLogin(){
        sId = et_id.getText().toString().trim();
        sPass = et_pass.getText().toString().trim();

        if(TextUtils.isEmpty(sId)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(sPass)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(sId, sPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {

                            loadData();
                            Log.i("TAG", "로그인 "+ G.bead);

                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!"+ " 로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void clickLogin(View view) {

        userLogin();


    }

    //비밀번호찾기
    public void clickFP(View view) {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this).setTitle("비밀번호 찾기").setMessage("비밀번호를 찾으시겠습니까?")
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new AlertDialog.Builder(LoginActivity.this).setTitle("비밀번호 찾기").setMessage("등록한 E-mail을 입력해주세요.").setView(et).setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog.setMessage("처리중입니다. 잠시 기다려주세요...");
                                progressDialog.show();
                                //비밀번호 재설정 이메일 보내기
                                String emailAddress = et.getText().toString().trim();

                                firebaseAuth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(LoginActivity.this, "이메일을 전송했습니다.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        Toast.makeText(LoginActivity.this, "이메일을 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                });



                            }
                        }).setPositiveButton("취소",null).show();
                    }
                }).setPositiveButton("취소",null);

        builder3.show();


    }


    //회원가입
    public void clickAcc(View view) {

        Intent intent = new Intent(this,AccountActivity.class);

        startActivity(intent);

    }



    void loadData(){
        String uid =  firebaseAuth.getCurrentUser().getUid();




        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                if(user==null)return;
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

}
