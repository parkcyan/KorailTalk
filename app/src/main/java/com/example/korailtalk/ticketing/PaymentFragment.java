package com.example.korailtalk.ticketing;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korailtalk.R;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.FragmentPaymentBinding;
import com.example.korailtalk.ticketing.data.TrainRvVO;

import java.sql.Timestamp;
import java.text.DecimalFormat;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding b;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentPaymentBinding.inflate(inflater, container, false);
        intent = ((PaymentActivity) getActivity()).getPaymentIntent();

        TrainRvVO train = (TrainRvVO) intent.getSerializableExtra("train");
        boolean specialSeat = intent.getBooleanExtra("special", false);
        int[] qtyArr = intent.getIntArrayExtra("qtyArr");

        b.tlMethod.addTab(b.tlMethod.newTab().setText("카드결제"));
        b.tlMethod.addTab(b.tlMethod.newTab().setText("간편결제"));

        int qty = 0;
        for (int i : qtyArr) qty += i;
        DecimalFormat df = new DecimalFormat("###,###원");

        int charge = specialSeat ? Util.roundCharge(train.getCharge() * 1.4) : train.getCharge();
        int totalCharge = charge * qty;
        int discountCharge = ((PaymentActivity) getActivity()).getDiscountCharge();
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