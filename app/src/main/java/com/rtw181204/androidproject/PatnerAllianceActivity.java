package com.rtw181204.androidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatnerAllianceActivity extends AppCompatActivity {

    ArrayList<Patner> members = new ArrayList<>();

    ListView listView;

    AllianceAdapter adapter;

    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patner_alliance);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("제휴업소 신청내역");


        listView = findViewById(R.id.listview);

        adapter = new AllianceAdapter(getLayoutInflater(), members);


        listView.setAdapter(adapter);




        ref = FirebaseDatabase.getInstance().getReference("applypatner/");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                if(dataSnapshot.getValue(Patner.class)==null){
                    Toast.makeText(PatnerAllianceActivity.this, "내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Patner patner = snapshot.getValue(Patner.class);

                    members.add(patner);

                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {


                TextView tvUid = view.findViewById(R.id.tv_uid);
                TextView tvNickname = view.findViewById(R.id.tv_nickname);
                TextView tvCompany = view.findViewById(R.id.tv_company);

                final String uid = tvUid.getText().toString();
                final String nickname = tvNickname.getText().toString();
                final String company = tvCompany.getText().toString();

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                final CharSequence[] item = new CharSequence[]{"승인", "삭제"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(PatnerAllianceActivity.this);


                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(item[which].equals("승인")){



                            Map<String, Object> taskMap = new HashMap<String, Object>();
                            G.patner = true;
                            taskMap.put("patner", G.patner);

                            ref.child("users").child(uid).updateChildren(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(PatnerAllianceActivity.this, nickname+ "님의 제휴신청이 승인되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });



                        }else {


                            Toast.makeText(PatnerAllianceActivity.this, nickname+ "님의 제휴신청 글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            ref.child("applypatner").child(uid).removeValue();


                            if(G.patner){
                                G.applypatner = false;
                                Map<String, Object> taskMap = new HashMap<String, Object>();
                                taskMap.put("applypatner", G.applypatner);

                                FirebaseDatabase.getInstance().getReference("users").child(uid).updateChildren(taskMap);


                            }



//                            FirebaseStorage.getInstance().getReference(company).delete();

                            members.remove(position);
                            adapter.notifyDataSetChanged();

                        }




                    }
                }).show();
                return false;
            }
        });


    }


}
