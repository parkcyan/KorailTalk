package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.korailtalk.BaseActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.ActivityLookupBinding;
import com.example.korailtalk.node.Node;
import com.example.korailtalk.node.NodeRoom;
import com.example.korailtalk.ticketing.adapter.TrainAdapter;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.sql.Timestamp;
import java.util.ArrayList;

public class LookupActivity extends BaseActivity {

    private ActivityLookupBinding b;
    private LookupActivity activity;
    private Context context;
    private Timestamp tsDate;
    private NodeRoom nodeRoom;
    private ArrayList<TrainVO> trainList;
    private String depNode;
    private String arrNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = getApplicationContext();
        Intent intent = getIntent();
        nodeRoom = new NodeRoom(this, getTrainHandler());
        depNode = intent.getStringExtra("depNode");
        arrNode = intent.getStringExtra("arrNode");
        tsDate = (Timestamp) intent.getSerializableExtra("date");
        nodeRoom.getTrainFromApi(depNode, arrNode, Util.dateFormatInt(tsDate, "yyyyMMdd"), 1);
        b.tvDep.setText(intent.getStringExtra("depNode"));
        b.tvArr.setText(intent.getStringExtra("arrNode"));
        b.tvDate.setText(Util.dateFormat(tsDate, "yyyy년 M월 d일 (E)"));
    }

    @Override
    protected String setToolbarTitle() {
        return "열차 조회";
    }

    @Override
    protected View getLayoutResource() {
        b = ActivityLookupBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    private Handler getTrainHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == NodeRoom.GET_TRAIN_SUCCESS) {
                    trainList = (ArrayList<TrainVO>) msg.obj;
                    Util.setRecyclerView(context, b.rvTrain, new TrainAdapter(activity, trainList), true);
                    b.rvTrain.post(() -> b.rlProgress.setVisibility(View.GONE));
                } else if (msg.what == NodeRoom.GET_TRAIN_FAILED) {
                    b.rlProgress.setVisibility(View.GONE);
                    b.clNottrain.setVisibility(View.VISIBLE);
                }
            }
        };
    }
}