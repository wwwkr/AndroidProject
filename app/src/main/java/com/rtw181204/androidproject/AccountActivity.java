package com.rtw181204.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountActivity extends BaseActivity {

    EditText et_id, et_pass, et_passChk, et_nick;

    String sId, sPass, sPassChk, sNick;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    CheckBox checkBox;

    TextView tvLinkify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        et_id =findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_password);
        et_passChk = findViewById(R.id.et_passwordChk);
        et_nick = findViewById(R.id.et_nickname);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        checkBox = findViewById(R.id.checkbox);
        tvLinkify = findViewById(R.id.tv);

        String text = "본인은 14세 이상이며 개인정보 처리방침을 확인하였으며 동의합니다.";
        tvLinkify.setText(text);



        Linkify.TransformFilter  mTransform = new Linkify.TransformFilter(){

            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Pattern pattern1 = Pattern.compile("개인정보 처리방침");

        Linkify.addLinks(tvLinkify, pattern1, "https://blog.naver.com/xodn1003/221488720800",null,mTransform);





    }


    public void clickAcc(View view) {

            checkAccount();


    }

    void checkAccount(){
        //사용자가 입력하는 email, password를 가져온다.

        sId = et_id.getText().toString();
        sPass =et_pass.getText().toString();
        sPassChk = et_passChk.getText().toString();
        sNick = et_nick.getText().toString();


        if(!sPass.equals(sPassChk)){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i = 0 ; i < G.checkNick.size(); i++){

            if(sNick.equals(G.checkNick.get(i)))
            {
                Toast.makeText(this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                return;
            }


        }

        if(!checkBox.isChecked()){
            Toast.makeText(this, "개인정보 처리방침에 동의하지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUser();
    }


    private void registerUser(){

        if(TextUtils.isEmpty(sId)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(sPass)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(sId, sPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            G.attCheck= false;
                            G.patner = false;
                            G.applypatner = false;
                            G.bead = 0 ;
                            G.manager = false;
                            User user = new User(sNick, task.getResult().getUser().getEmail(), G.bead, null, G.attCheck, G.patner, G.applypatner, G.manager);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    G.nickName = sNick;
                                    G.id = task.getResult().getUser().getEmail();

                                    finish();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            });

                        } else {
                            //에러발생시

                            Toast.makeText(AccountActivity.this, "등록 에러!" +" 에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });


    }




}
