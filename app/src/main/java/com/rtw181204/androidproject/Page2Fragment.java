package com.rtw181204.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Page2Fragment extends Fragment {

    Button btn_trade, btn_group, btn_work, btn_tip, btn_alliance;
    FloatingActionButton btnWrite;

    Intent intent = null;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_page2,container,false);


        btn_trade = view.findViewById(R.id.btn_trade);
        btn_group = view.findViewById(R.id.btn_group);
        btn_work = view.findViewById(R.id.btn_work);
        btn_tip = view.findViewById(R.id.btn_tip);
        btnWrite = view.findViewById(R.id.fabNewPost);
//        btn_alliance = view.findViewById(R.id.btn_alliance);


        btn_trade.setOnClickListener(listener);
        btn_group.setOnClickListener(listener);
        btn_work.setOnClickListener(listener);
        btn_tip.setOnClickListener(listener);
        btnWrite.setOnClickListener(listener);
//        btn_alliance.setOnClickListener(listener);






        mPagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {

            private final Fragment[] mFragments = new Fragment[]{

                    new PatnerPostsFragment(),
//                    new PatnerMyPostsFragment(),
//                    new PatnerMyTopPostsFragment()
            };

            private final String[] mFragmentNames = new String[]{

                    getString(R.string.patner),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        mViewPager = view.findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);






        return view;

    }



    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.btn_trade:
                    if(intent==null){
                        intent = new Intent(getContext(),InfoTradeActivity.class);
                        startActivity(intent);
                        intent=null;
                    }


                    break;

                case R.id.btn_group:
                    if(intent==null){
                        intent = new Intent(getContext(),InfoGroupActivity.class);

                        startActivity(intent);
                        intent=null;
                    }

                    break;

                case R.id.btn_work:
                    if(intent==null){
                        intent = new Intent(getContext(),InfoWorkActivity.class);
                        startActivity(intent);
                        intent=null;
                    }

                    break;

                case R.id.btn_tip:
                    if(intent==null){
                        intent = new Intent(getContext(),InfoTipActivity.class);
                        startActivity(intent);
                        intent=null;
                    }

                    break;

                case R.id.fabNewPost:
                    if(intent==null){

                        if(!G.patner){
                            Toast.makeText(getContext(), "제휴업소만 작성 가능한 게시판입니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent = new Intent(getContext(),InfoPatnerWriteActivity.class);
                        startActivity(intent);
                        intent=null;
                    }

                    break;

//                case R.id.btn_alliance:
//                    if(intent==null){
//                        intent = new Intent(getContext(),InfoAllianceActivity.class);
//                        startActivity(intent);
//                        intent=null;
//                    }
//                    break;
            }
        }


    };











}
