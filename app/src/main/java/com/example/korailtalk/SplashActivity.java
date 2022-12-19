package com.example.korailtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.korailtalk.node.NodeRoom;
import com.example.korailtalk.ticketing.data.NodeVO;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    NodeRoom nodeRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == NodeRoom.GET_NODE_SUCCESS) nodeRoom.getNodeForRv();
                else if (msg.what == NodeRoom.GET_LIST_FOR_RV) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("nodeList", (ArrayList<NodeVO>) msg.obj);
                    startActivity(intent);
                    finish();
                }
            }
        };
        nodeRoom = new NodeRoom(this, handler);
        nodeRoom.checkNodeTable();
    }

}