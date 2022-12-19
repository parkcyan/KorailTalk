package com.example.korailtalk;

import android.os.Bundle;
import android.view.View;

import com.example.korailtalk.databinding.ActivityMainBinding;
import com.example.korailtalk.ticketing.TicketingFragment;
import com.example.korailtalk.ticketing.data.NodeVO;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    ActivityMainBinding b;
    TicketingFragment ticketingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.toolbar.ivBack.setVisibility(View.INVISIBLE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("nodeList", (ArrayList<NodeVO>) getIntent().getSerializableExtra("nodeList"));

        ticketingFragment = new TicketingFragment();
        ticketingFragment.setArguments(bundle);

        setBnv();
    }

    private void setBnv() {
        addFragment(R.id.container_main, ticketingFragment);
        b.bnvMain.bnvMain.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bnv_main_tic) {
                if (ticketingFragment != null) {
                    showFragment(ticketingFragment);
                }
            } else if (item.getItemId() == R.id.bnv_main_seasontic) {

            } else if (item.getItemId() == R.id.bnv_main_goods) {

            } else if (item.getItemId() == R.id.bnv_main_checktic) {

            }
            return true;
        });
    }

    public void showBnv(boolean show) {
        if (show) b.bnvMain.bnvMain.setVisibility(View.VISIBLE);
        else b.bnvMain.bnvMain.setVisibility(View.GONE);
    }

    @Override
    protected String setToolbarTitle() {
        return "승차권 예매";
    }

    @Override
    protected View getLayoutResource() {
        b = ActivityMainBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

}