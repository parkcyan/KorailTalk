package com.example.korailtalk;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.korailtalk.ticketing.TicketingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity {

    BottomNavigationView bnv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnv_main = findViewById(R.id.bnv_main);
        newFragment(R.id.container_main, new TicketingFragment());
        bnv_main.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bnv_main_tic) {
                newFragment(R.id.container_main, new TicketingFragment());
            } else if (item.getItemId() == R.id.bnv_main_seasontic) {

            } else if (item.getItemId() == R.id.bnv_main_goods) {

            } else if (item.getItemId() == R.id.bnv_main_checktic) {

            }
            return true;
        });
    }

    @Override
    protected String setToolbarTitle() {
        return "승차권 예매";
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

}