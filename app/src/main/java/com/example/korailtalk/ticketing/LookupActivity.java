package com.example.korailtalk.ticketing;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.korailtalk.BaseActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.ActivityLookupBinding;
import com.example.korailtalk.node.NodeRoom;
import com.example.korailtalk.ticketing.adapter.TrainAdapter;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class LookupActivity extends BaseActivity {

    private ActivityLookupBinding b;
    private LookupActivity activity;
    private Context context;
    private Timestamp tsDate;
    private NodeRoom nodeRoom;
    private ArrayList<TrainVO> trainList;
    private String depNode;
    private String arrNode;
    private TrainVO selectedTrain;
    private int[] qtyArr;
    private boolean specialSeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = getApplicationContext();
        b.toolbar.ivBack.setOnClickListener((v -> finish()));

        Intent intent = getIntent();
        depNode = intent.getStringExtra("depNode");
        arrNode = intent.getStringExtra("arrNode");
        qtyArr = intent.getIntArrayExtra("qtyArr");
        tsDate = (Timestamp) intent.getSerializableExtra("date");
        nodeRoom = new NodeRoom(this, getTrainHandler());

        // api로부터 1월달 열차 가격이 잘 안나오는 문제 때문에 1개월전 데이터를 불러옴
        tsDate = Util.timestampOperator(tsDate, Calendar.MONTH, -1);
        nodeRoom.getTrainFromApi(depNode, arrNode, tsDate);
        tsDate = Util.timestampOperator(tsDate, Calendar.MONTH, 1);

        b.tvDep.setText(intent.getStringExtra("depNode"));
        b.tvArr.setText(intent.getStringExtra("arrNode"));
        b.tvDate.setText(Util.dateFormat(tsDate, "yyyy년 M월 d일 (E)"));

        // 이전날, 다음날 조회

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        b.spGrade.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(context, R.array.seat, android.R.layout.simple_spinner_dropdown_item);
        b.spSeat.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(context, R.array.trans, android.R.layout.simple_spinner_dropdown_item);
        b.spTrans.setAdapter(adapter);

        // 결제
        b.tvSelect.setOnClickListener(onSelect());
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

    /////////////////////////////////////////////////////////////////////////////////////////////

    private Handler getTrainHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == NodeRoom.GET_TRAIN_SUCCESS) {
                    trainList = (ArrayList<TrainVO>) msg.obj;
                    if (trainList.isEmpty()) {
                        b.rlProgress.setVisibility(View.GONE);
                        b.clNottrain.setVisibility(View.VISIBLE);
                    } else {
                        Util.setRecyclerView(context, b.rvTrain, new TrainAdapter(activity, trainList), true);
                        b.rvTrain.post(() -> b.rlProgress.setVisibility(View.GONE));
                    }
                } else if (msg.what == NodeRoom.GET_TRAIN_FAILED) {
                    b.rlProgress.setVisibility(View.GONE);
                    b.clNottrain.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public void trainSelect(TrainVO train, boolean special) {
        selectedTrain = train;
        specialSeat = special;
        b.tvSelect.setVisibility(View.VISIBLE);
        if (selectedTrain.getAdultcharge().equals("매진")) {
            b.tvSelect.setBackgroundColor(ContextCompat.getColor(activity, R.color.aliceblue));
            b.tvSelect.setTextColor(ContextCompat.getColor(context, R.color.main6));
        } else {
            b.tvSelect.setBackgroundColor(ContextCompat.getColor(activity, R.color.main6));
            b.tvSelect.setTextColor(ContextCompat.getColor(context, R.color.main));
        }
    }

    private View.OnClickListener onSelect() {
        return view -> {
            if (!selectedTrain.getAdultcharge().equals("매진")) {
                Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra("train", selectedTrain);
                intent.putExtra("special", specialSeat);
                intent.putExtra("tsDate", tsDate);
                intent.putExtra("qtyArr", qtyArr);
                startActivity(intent);
            }
        };
    }

}