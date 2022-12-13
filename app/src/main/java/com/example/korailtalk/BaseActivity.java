package com.example.korailtalk;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.korailtalk.ticketing.TicketingFragment;

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    TextView tv_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        setToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText(setToolbarTitle());
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    protected void newFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    protected abstract String setToolbarTitle();

    protected abstract int getLayoutResource();

}
