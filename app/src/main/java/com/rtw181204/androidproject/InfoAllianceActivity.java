package com.rtw181204.androidproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoAllianceActivity extends AppCompatActivity {


    EditText etCompany, etName, etNum;

    ImageView iv;

    Uri imgUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_alliance);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("제휴 신청");

        etCompany = findViewById(R.id.et_company);
        etName = findViewById(R.id.et_name);
        etNum = findViewById(R.id.et_num);
        iv = findViewById(R.id.btn_load);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("제휴신청 유의사항").setMessage("제휴신청 시 필수 기입항목 \n\n 1. 업체명\n 2.대표자명 \n 3.번호 \n 4.사업자 등록증\n\n 위 항목들이 누락됐을 시 제휴신청에 불가능합니다.")
        .setNegativeButton("확인",null);
    }



    public void clickUpload(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 10);

    }

    public void clickOK(View view) {
        if(imgUri==null){
            Toast.makeText(this, "사업자등록증을 첨부하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String company = etCompany.getText().toString();
        String name = etName.getText().toString();
        String num  =etNum.getText().toString();


        if(TextUtils.isEmpty(company)){
            Toast.makeText(this, "업체명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "대표자명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(num)){
            Toast.makeText(this, "번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();


            return;
        }


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("applypatner/"+uid);

        ref.push().setValue(new Patner(company, name, num ,uid, G.id, G.nickName, imgUri.toString()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = sdf.format(new Date()) + ".png"; //오늘 날짜로 만들어라


        StorageReference imgRef = FirebaseStorage.getInstance().getReference(company+"/"+fileName);


        Toast.makeText(InfoAllianceActivity.this, "제휴업소 신청 중...", Toast.LENGTH_SHORT).show();
        UploadTask uploadTask = imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(InfoAllianceActivity.this, "제휴업소 신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



                G.applypatner = true;
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("applypatner", G.applypatner);


                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(taskMap);

                finish();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){

            case 10:

                if(resultCode==RESULT_OK){
                    imgUri = data.getData();
                    Glide.with(this).load(imgUri).into(iv);
                }
                break;
        }
    }


}
