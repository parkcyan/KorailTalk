package com.example.korailtalk.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.korailtalk.BaseActivity;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.ActivityCheckBinding;
import com.example.korailtalk.ticketing.data.TrainRvVO;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Random;

public class CheckActivity extends BaseActivity {

    ActivityCheckBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.toolbar.ivBack.setOnClickListener((v -> finish()));

        Intent intent = getIntent();
        TrainRvVO train = (TrainRvVO) intent.getSerializableExtra("train");
        boolean specialSeat = intent.getBooleanExtra("special", false);
        Timestamp tsDate = (Timestamp) intent.getSerializableExtra("tsDate");
        int[] qtyArr = intent.getIntArrayExtra("qtyArr");
        int qty = 0;
        for (int i : qtyArr) qty += i;
        DecimalFormat df = new DecimalFormat("###,###원");

        b.tvDate.setText(Util.dateFormat(tsDate, "yyyy년 M월 d일 (E)"));
        String trainGradeNo = train.getTraingradename() + " " + train.getTrainno();
        b.tvTraingrade.setText(trainGradeNo);
        b.tvDep.setText(train.getDepplacename());
        b.tvArr.setText(train.getArrplacename());
        b.tvDeptime.setText(train.getDepplandtime());
        b.tvArrtime.setText(train.getArrplandtime());

        Random rand = new Random();
        int trainIndex = rand.nextInt(7) + 1;
        int seatIndex = rand.nextInt(14) + 1;
        String tvSeat = trainIndex + "호차 " + seatIndex + "A";
        if (qty > 1) tvSeat = tvSeat + " 외 " + (qty - 1) + "석";
        b.tvSeat.setText(tvSeat);

        int charge = specialSeat ? Util.roundCharge(train.getCharge() * 1.4) : train.getCharge();
        int discountCharge = qtyArr[0] * charge + qtyArr[1] * Util.roundCharge(charge * 0.5);
        if (qtyArr[2] > 1) discountCharge += (qtyArr[2] - 1) * Util.roundCharge(charge * 0.25);
        discountCharge += qtyArr[3] * Util.roundCharge(charge * 0.7)
                + qtyArr[4] * Util.roundCharge(charge * 0.5)
                + qtyArr[5] * Util.roundCharge(charge * 0.7);
        int totalCharge = charge * qty;

        b.tvCharge.setText(df.format(discountCharge));
        b.tvChargebottom.setText(df.format(discountCharge));

        String discountStr = df.format(totalCharge) + " (총 " + df.format(totalCharge - discountCharge) + ")";
        b.tvDiscount.setText(discountStr);

        b.tvCancel.setOnClickListener(v -> finish());

    }

    @Override
    protected String setToolbarTitle() {
        return "결제";
    }

    @Override
    protected View getLayoutResource() {
        b = ActivityCheckBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }
}