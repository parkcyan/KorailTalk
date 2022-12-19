package com.example.korailtalk;

import android.os.Bundle;
import android.view.View;

import com.example.korailtalk.databinding.ActivityMainBinding;
import com.example.korailtalk.ticketing.TicketingFragment;

public class MainActivity extends BaseActivity {

    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBnv();
        b.toolbar.ivBack.setVisibility(View.INVISIBLE);
    }

    private void setBnv() {
        setFragment(R.id.container_main, new TicketingFragment());
        b.bnvMain.bnvMain.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bnv_main_tic) {
                setFragment(R.id.container_main, new TicketingFragment());
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