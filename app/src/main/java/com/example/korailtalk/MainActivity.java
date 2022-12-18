package com.example.korailtalk;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.korailtalk.databinding.ActivityMainBinding;
import com.example.korailtalk.ticketing.TicketingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity {

    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBnv();
        b.toolbar.ivBack.setVisibility(View.INVISIBLE);
    }

    private void setBnv() {
        newFragment(R.id.container_main, new TicketingFragment());
        b.bnvMain.bnvMain.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bnv_main_tic) {
                newFragment(R.id.container_main, new TicketingFragment());
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