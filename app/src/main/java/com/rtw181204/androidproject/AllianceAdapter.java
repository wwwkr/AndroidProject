package com.rtw181204.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AllianceAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Patner> members;

    public AllianceAdapter(LayoutInflater inflater, ArrayList<Patner> members) {
        this.inflater = inflater;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = inflater.inflate(R.layout.listview_alliance, parent , false);
        }

        TextView tvCompany = convertView.findViewById(R.id.tv_company);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvId = convertView.findViewById(R.id.tv_id);
        TextView tvNickname = convertView.findViewById(R.id.tv_nickname);
        TextView tvNum = convertView.findViewById(R.id.tv_num);
        TextView tvUid = convertView.findViewById(R.id.tv_uid);

        ImageView iv = convertView.findViewById(R.id.iv);


        Patner patner = members.get(position);


        tvCompany.setText(patner.company);
        tvName.setText(patner.name);
        tvId.setText(patner.id);
        tvNickname.setText(patner.nickName);
        tvNum.setText(patner.num);
        tvUid.setText(patner.uid);

        Glide.with(convertView).load(patner.imgUri).into(iv);





        return convertView;
    }
}
