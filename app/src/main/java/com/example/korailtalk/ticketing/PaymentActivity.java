package com.example.korailtalk.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.korailtalk.BaseActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.ActivityPaymentBinding;
import com.example.korailtalk.ticketing.data.TrainRvVO;

import java.text.DecimalFormat;

public class PaymentActivity extends BaseActivity {

    private ActivityPaymentBinding b;
    private int[] qtyArr;
    private boolean specialSeat;
    private TrainRvVO train;
    private Intent intent;
    private CheckFragment checkFragment;
    private PaymentFragment paymentFragment;
    private boolean isOnCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.toolbar.ivBack.setOnClickListener((v -> finish()));
        intent = getIntent();
        checkFragment = new CheckFragment();
        paymentFragment = new PaymentFragment();
        addFragment(R.id.container_payment, checkFragment);
        addFragment(R.id.container_payment, paymentFragment);
        hideFragment(paymentFragment);

        qtyArr = intent.getIntArrayExtra("qtyArr");
        specialSeat = intent.getBooleanExtra("special", false);
        train = (TrainRvVO) intent.getSerializableExtra("train");

        DecimalFormat df = new DecimalFormat("###,###원");

        b.tvChargebottom.setText(df.format(getDiscountCharge()));
        b.tvCancel.setOnClickListener(v -> finish());
        b.tvNext.setOnClickListener(onNext());
    }

    @Override
    protected String setToolbarTitle() {
        return "결제";
    }

    @Override
    protected View getLayoutResource() {
        b = ActivityPaymentBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    public Intent getPaymentIntent() {
        return intent;
    }

    @Override
    public void onBackPressed() {
        if (!isOnCheck && checkFragment != null) backToCheckFragment();
        else super.onBackPressed();
    }

    public int getDiscountCharge() {
        int charge = specialSeat ? Util.roundCharge(train.getCharge() * 1.4) : train.getCharge();
        int discountCharge = qtyArr[0] * charge + qtyArr[1] * Util.roundCharge(charge * 0.5);
        if (qtyArr[2] > 1) discountCharge += (qtyArr[2] - 1) * Util.roundCharge(charge * 0.25);
        discountCharge += qtyArr[3] * Util.roundCharge(charge * 0.7)
                + qtyArr[4] * Util.roundCharge(charge * 0.5)
                + qtyArr[5] * Util.roundCharge(charge * 0.7);
        return discountCharge;
    }

    public View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFragment(checkFragment);
                showFragment(paymentFragment);
                isOnCheck = false;
                b.tvCancel.setText("이전");
                b.tvNext.setText("결제/발권");
                b.tvCancel.setOnClickListener(view -> backToCheckFragment());
            }
        };
    }

    public void backToCheckFragment() {
        hideFragment(paymentFragment);
        showFragment(checkFragment);
        b.tvCancel.setText("예약취소");
        b.tvNext.setText("다음");
        isOnCheck = true;
        b.tvCancel.setOnClickListener(v -> finish());
    }

}