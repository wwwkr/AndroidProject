package com.rtw181204.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BeadAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<BeadList> members;

    public BeadAdapter(LayoutInflater inflater, ArrayList<BeadList> members) {
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
            convertView = inflater.inflate(R.layout.listview_bead, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        TextView tvContent = convertView.findViewById(R.id.tv_content);

        TextView tvTime = convertView.findViewById(R.id.tv_time);

        BeadList beadList = members.get(position);


        tvTitle.setText(beadList.title);
        tvContent.setText(beadList.content);

        tvTime.setText(beadList.time);



        return convertView;
    }
}
