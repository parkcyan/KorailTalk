package com.example.korailtalk.ticketing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korailtalk.util.Util;
import com.example.korailtalk.databinding.FragmentCheckBinding;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Random;

public class CheckFragment extends Fragment {

    FragmentCheckBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentCheckBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();

        TrainVO train = (TrainVO) bundle.getSerializable("train");
        boolean specialSeat = bundle.getBoolean("special");
        Timestamp tsDate = (Timestamp) bundle.getSerializable("tsDate");
        int[] qtyArr = bundle.getIntArray("qtyArr");

        b.tvDate.setText(Util.dateFormat(tsDate, "yyyy년 M월 d일 (E)"));
        String trainGradeNo = train.getTraingradename() + " " + train.getTrainno();
        b.tvTraingrade.setText(trainGradeNo);
        b.tvDep.setText(train.getDepplacename());
        b.tvArr.setText(train.getArrplacename());
        b.tvDeptime.setText(train.getDepplandtime());
        b.tvArrtime.setText(train.getArrplandtime());

        String tvSeat = bundle.getString("trainNum") + "호차 " + bundle.getString("seat") + "A";
        int qty = 0;
        for (int i : qtyArr) qty += i;
        if (qty > 1) tvSeat = tvSeat + " 외 " + (qty - 1) + "석";
        b.tvSeat.setText(tvSeat);

        DecimalFormat df = new DecimalFormat("###,###원");
        int charge = specialSeat ? Util.roundCharge(train.getCharge() * 1.4) : train.getCharge();
        int totalCharge = charge * qty;
        int discountCharge = bundle.getInt("discountCharge");
        b.tvCharge.setText(df.format(discountCharge));

        String discountStr = df.format(totalCharge) + " (총 " + df.format(totalCharge - discountCharge) + " 할인)";
        b.tvDiscount.setText(discountStr);

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}