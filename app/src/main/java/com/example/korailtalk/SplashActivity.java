package com.example.korailtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.korailtalk.data.NodeDAO;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loading();
    }

    private void loading() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            new NodeDAO(getApplicationContext()).getNode();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }, 2000);
    }

}