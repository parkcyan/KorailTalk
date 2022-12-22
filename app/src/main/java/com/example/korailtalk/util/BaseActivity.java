package com.example.korailtalk.util;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.korailtalk.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected static boolean ticketingFinish = false;
    protected static int bnvClickFromLookup = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbar = findViewById(R.id.tv_toolbar);
        tvToolbar.setText(setToolbarTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    protected void setFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    protected void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(id, fragment).commit();
    }

    protected void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    protected void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    protected abstract String setToolbarTitle();

    protected abstract View getLayoutResource();

}
