package com.example.korailtalk.ticketing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.korailtalk.R;
import com.google.android.material.tabs.TabLayout;

public class TicketingFragment extends Fragment {

    View v;
    TabLayout tl_tic;
    TextView tv_tic_dep, tv_tic_arr;
    LinearLayout ll_tic;
    boolean isRoundTrip = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ticketing, container, false);
        tl_tic = v.findViewById(R.id.tl_tic);
        tv_tic_dep = v.findViewById(R.id.tv_tic_dep);
        tv_tic_arr = v.findViewById(R.id.tv_tic_arr);
        ll_tic = v.findViewById(R.id.ll_tic);
        tl_tic.addTab(tl_tic.newTab().setText("편도"));
        tl_tic.addTab(tl_tic.newTab().setText("왕복"));
        tl_tic.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isRoundTrip = tab.getPosition() == 1;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tv_tic_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_tic.setVisibility(View.VISIBLE);
            }
        });
        ll_tic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_tic.setVisibility(View.GONE);
            }
        });
        return v;
    }

}