package com.example.korailtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.korailtalk.room.node.NodeRoom;
import com.example.korailtalk.room.ticket.TicketRoom;
import com.example.korailtalk.ticketing.data.NodeVO;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private NodeRoom nodeRoom;
    private TicketRoom ticketRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler(Looper.getMainLooper()) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == NodeRoom.GET_NODE_SUCCESS) nodeRoom.getNodeForRv();
                else if (msg.what == NodeRoom.GET_LIST_FOR_RV) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("nodeList", (ArrayList<NodeVO>) msg.obj);
                    ticketRoom.checkTicketTable();
                } else if (msg.what == TicketRoom.GET_TICKET_SUCCESS) {
                    startActivity(intent);
                    finish();
                }
            }
        };
        nodeRoom = new NodeRoom(this, handler);
        ticketRoom = new TicketRoom(this, handler);
        nodeRoom.checkNodeTable();
    }

}