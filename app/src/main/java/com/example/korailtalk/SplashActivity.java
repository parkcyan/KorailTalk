package com.example.korailtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.korailtalk.sqlite.CityDTO;
import com.example.korailtalk.sqlite.MyDatabase;
import com.example.korailtalk.sqlite.CityDAO;
import com.example.korailtalk.sqlite.NodeDTO;
import com.example.korailtalk.sqlite.NodeDAO;

public class SplashActivity extends AppCompatActivity {

    TextView tv_splash_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_status = findViewById(R.id.tv_splash_status);
        loading(this);
    }

    private void loading(Context context) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            CityDAO cityDAO = new CityDAO(context);
            NodeDAO nodeDAO = new NodeDAO(context);

            tv_splash_status.setText("도시 정보를 받아오는 중...");
            cityDAO.initCity();
            tv_splash_status.setText("도시 정보를 받아오는 중...");

            tv_splash_status.setText("역 정보를 받아오는 중...");
            nodeDAO.initNode();

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);
    }

}