package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.korailtalk.MainActivity;
import com.example.korailtalk.util.BaseActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.util.KtDialog;
import com.example.korailtalk.util.Util;
import com.example.korailtalk.databinding.ActivityLookupBinding;
import com.example.korailtalk.room.node.NodeRoom;
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

        b.tvDep.setText(intent.getStringExtra("depNode"));
        b.tvArr.setText(intent.getStringExtra("arrNode"));
        b.tvDate.setText(Util.dateFormat(tsDate, "yyyy??? M??? d??? (E)"));

        // ?????????, ????????? ??????
        setTvNextDayBottomText();
        b.tvNextday.setOnClickListener(changeDate(true));
        b.tvPreday.setOnClickListener(changeDate(false));
        b.tvNextdaybottom.setOnClickListener(changeDate(true));

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        b.spGrade.setAdapter(adapter);
        b.spGrade.setOnItemSelectedListener(selectTrainGrade());
        adapter = ArrayAdapter.createFromResource(context, R.array.seat, android.R.layout.simple_spinner_dropdown_item);
        b.spSeat.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(context, R.array.trans, android.R.layout.simple_spinner_dropdown_item);
        b.spTrans.setAdapter(adapter);

        // bnv
        b.bnv.ivTic.setImageResource(R.drawable.bnvitem1);
        b.bnv.tvTic.setTextColor(ContextCompat.getColor(context, R.color.black));
        b.bnv.rlTic.setOnClickListener(onBnvClick(0));
        b.bnv.rlSeasontic.setOnClickListener(onBnvClick(1));
        b.bnv.rlTour.setOnClickListener(onBnvClick(2));
        b.bnv.rlChecktic.setOnClickListener(onBnvClick(3));

        // ??????
        b.tvSelect.setOnClickListener(onSelect());
    }

    @Override
    protected String setToolbarTitle() {
        return "?????? ??????";
    }

    @Override
    protected View getLayoutResource() {
        b = ActivityLookupBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ????????? ?????? ????????? ??? ??????????????? ???????????? MainActivity??? ?????????
        if (ticketingFinish) finish();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    // api????????? ??????????????? ?????????????????? ????????? ??? ????????? ?????? ??????????????? ???????????? ?????????
    public void getTrain() {
        b.svTrain.setVisibility(View.GONE);
        b.rlProgress.setVisibility(View.VISIBLE);
        tsDate = Util.timestampOperator(tsDate, Calendar.YEAR, -1);
        tsDate = Util.timestampOperator(tsDate, Calendar.MONTH, -6);
        nodeRoom.getTrainFromApi(depNode, arrNode, tsDate);
        tsDate = Util.timestampOperator(tsDate, Calendar.YEAR, 1);
        tsDate = Util.timestampOperator(tsDate, Calendar.MONTH, 6);
        b.tvSelect.setVisibility(View.GONE);
    }

    public void getTrain(String trainGrade) {
        b.svTrain.setVisibility(View.GONE);
        b.rlProgress.setVisibility(View.VISIBLE);
        tsDate = Util.timestampOperator(tsDate, Calendar.YEAR, -1);
        tsDate = Util.timestampOperator(tsDate, Calendar.MONTH, -6);
        nodeRoom.getTrainFromApi(depNode, arrNode, tsDate, trainGrade);
        tsDate = Util.timestampOperator(tsDate, Calendar.YEAR, 1);
        tsDate = Util.timestampOperator(tsDate, Calendar.MONTH, 6);
        b.tvSelect.setVisibility(View.GONE);
    }

    private Handler getTrainHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == NodeRoom.GET_TRAIN_SUCCESS) {
                    trainList = (ArrayList<TrainVO>) msg.obj;
                    if (trainList.isEmpty()) {
                        b.svTrain.setVisibility(View.GONE);
                        b.rlProgress.setVisibility(View.GONE);
                        b.clNottrain.setVisibility(View.VISIBLE);
                    } else {
                        Util.setRecyclerView(context, b.rvTrain, new TrainAdapter(activity, trainList), true);
                        b.svTrain.setVisibility(View.VISIBLE);
                        b.rvTrain.post(() -> b.rlProgress.setVisibility(View.GONE));
                    }
                } else if (msg.what == NodeRoom.GET_TRAIN_FAILED) {
                    b.svTrain.setVisibility(View.GONE);
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
        if (selectedTrain.getAdultcharge().equals("??????")) {
            b.tvSelect.setBackgroundColor(ContextCompat.getColor(activity, R.color.aliceblue));
            b.tvSelect.setTextColor(ContextCompat.getColor(context, R.color.main6));
        } else {
            b.tvSelect.setBackgroundColor(ContextCompat.getColor(activity, R.color.main6));
            b.tvSelect.setTextColor(ContextCompat.getColor(context, R.color.main));
        }
    }

    private View.OnClickListener onSelect() {
        return view -> {
            if (!selectedTrain.getAdultcharge().equals("??????")) {
                Intent intent = new Intent(activity, PaymentActivity.class);
                intent.putExtra("train", selectedTrain);
                intent.putExtra("special", specialSeat);
                intent.putExtra("tsDate", tsDate);
                intent.putExtra("qtyArr", qtyArr);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener changeDate(boolean next) {
        return view -> {
            if (!next &&
                    Util.timestampOperator(tsDate, Calendar.DATE, -1).getTime() < Util.getCurrentTime().getTime()) {
                StringBuilder sb = new StringBuilder(Util.getCurrentTime().toString());
                sb.replace(11, sb.length(), "00:00:00");
                if (Util.timestampOperator(tsDate, Calendar.DATE, -1).getTime() == Timestamp.valueOf(sb.toString()).getTime()) {
                    tsDate = Util.getCurrentTime();
                } else {
                    new KtDialog(this, getLayoutInflater(),
                            "????????????", "?????? ??????????????? ???????????? ??? ????????????.").show();
                    return;
                }
            } else {
                if (next) tsDate = Util.timestampOperator(tsDate, Calendar.DATE, 1);
                else tsDate = Util.timestampOperator(tsDate, Calendar.DATE, -1);
                StringBuilder sb = new StringBuilder(tsDate.toString());
                sb.replace(11, sb.length(), "00:00:00");
                tsDate = Timestamp.valueOf(sb.toString());
            }
            b.svTrain.setVisibility(View.GONE);
            b.rlProgress.setVisibility(View.VISIBLE);
            getTrain();
            setTvNextDayBottomText();
            b.tvDate.setText(Util.dateFormat(tsDate, "yyyy??? M??? d??? (E)"));
        };
    }

    private void setTvNextDayBottomText() {
        String tvNextdaybottom = "????????? (" +
                Util.dateFormat(Util.timestampOperator(tsDate, Calendar.DATE, 1), "M??? d???") + ") ????????????";
        b.tvNextdaybottom.setText(tvNextdaybottom);
    }

    private View.OnClickListener onBnvClick(int index) {
        return v -> {
            bnvClickFromLookup = index;
            finish();
        };
    }

    private AdapterView.OnItemSelectedListener selectTrainGrade() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) getTrain();
                else if (position == 1) getTrain("KTX");
                else if (position == 2) getTrain("?????????");
                else if (position == 3) getTrain("?????????");
                else if (position == 4) getTrain("ITX-??????");
                else if (position == 5) getTrain("??????");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

}