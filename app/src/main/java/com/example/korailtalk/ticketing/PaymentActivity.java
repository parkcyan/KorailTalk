package com.example.korailtalk.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.korailtalk.BaseActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.ActivityPaymentBinding;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.sql.Timestamp;
import java.text.DecimalFormat;

public class PaymentActivity extends BaseActivity {

    private ActivityPaymentBinding b;
    private int[] qtyArr;
    private boolean specialSeat;
    private TrainVO train;
    private Timestamp tsDate;
    private CheckFragment checkFragment;
    private PaymentFragment paymentFragment;
    private boolean isOnCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        checkFragment = new CheckFragment();
        paymentFragment = new PaymentFragment();
        b.toolbar.ivBack.setOnClickListener((v -> finish()));

        qtyArr = intent.getIntArrayExtra("qtyArr");
        specialSeat = intent.getBooleanExtra("special", false);
        train = (TrainVO) intent.getSerializableExtra("train");
        tsDate = (Timestamp) intent.getSerializableExtra("tsDate");

        Bundle bundle = setBundle();
        checkFragment.setArguments(bundle);
        paymentFragment.setArguments(bundle);
        addFragment(R.id.container_payment, checkFragment);
        addFragment(R.id.container_payment, paymentFragment);
        hideFragment(paymentFragment);

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

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putIntArray("qtyArr" , qtyArr);
        bundle.putBoolean("special", specialSeat);
        bundle.putSerializable("train", train);
        bundle.putSerializable("tsDate", tsDate);
        bundle.putInt("discountCharge", getDiscountCharge());
        return bundle;
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
        return v -> {
            if (isOnCheck) {
                hideFragment(checkFragment);
                showFragment(paymentFragment);
                isOnCheck = false;
                b.tvCancel.setText("이전");
                b.tvNext.setText("결제/발권");
                b.tvCancel.setOnClickListener(view -> backToCheckFragment());
            } else paymentFragment.payment();
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

    public void setBottomVisibility(boolean visibility) {
        if (visibility) b.llBottom.setVisibility(View.VISIBLE);
        else b.llBottom.setVisibility(View.GONE);
    }

    public void ticketingFinish() {
        ticketingFinish = true;
        finish();
    }

}