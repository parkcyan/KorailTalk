package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.korailtalk.MyDialog;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.FragmentPaymentBinding;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding b;
    private EditText[] etArr;
    private final int[] maxLength = {4, 4, 4, 4, 2, 4, 2, 6};
    private Timestamp tsDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentPaymentBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();

        TrainVO train = (TrainVO) bundle.getSerializable("train");
        boolean specialSeat = bundle.getBoolean("special");
        int[] qtyArr = bundle.getIntArray("qtyArr");
        tsDate = (Timestamp) bundle.getSerializable("tsDate");

        b.tlMethod.addTab(b.tlMethod.newTab().setText("카드결제"));
        b.tlMethod.addTab(b.tlMethod.newTab().setText("간편결제"));

        int qty = 0;
        for (int i : qtyArr) qty += i;
        DecimalFormat df = new DecimalFormat("###,###원");

        int charge = specialSeat ? Util.roundCharge(train.getCharge() * 1.4) : train.getCharge();
        int totalCharge = charge * qty;
        int discountCharge = bundle.getInt("discountCharge");
        b.tvCharge.setText(df.format(discountCharge));

        String discountStr = df.format(totalCharge) + " (총 " + df.format(totalCharge - discountCharge) + " 할인)";
        b.tvDiscount.setText(discountStr);

        etArr = new EditText[]{b.etCardnum1, b.etCardnum2, b.etCardnum3, b.etCardnum4, b.etCardvalimonth, b.etCardvaliyear,
                b.etCardpw, b.etIdentifynum};

        b.llClear.setOnClickListener(v -> {
            for (EditText et : etArr) et.setText("");
            etArr[0].requestFocus();
        });

        for (int i = 0; i < etArr.length; i++) {
            etArr[i].addTextChangedListener(setTextWatcher(i));
        }

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private TextWatcher setTextWatcher(int index) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (index != 7 && editable.toString().length() == maxLength[index])
                    etArr[index + 1].requestFocus();
            }
        };
    }

    public void payment() {
        for (int i = 0; i < 4; i++) {
            if (etArr[i].getText().toString().length() != maxLength[i]) {
                paymentError("카드번호를 잘못 입력하셨거나,\n철도공사에서 결제할 수 없는 카드입니다.");
                return;
            }
        }
        String year = b.etCardvaliyear.getText().toString();
        String month = b.etCardvalimonth.getText().toString();
        if (month.equals("") || Integer.parseInt(month) > 12) {
            paymentError("유효기간 '월'을 정확히 입력해주세요. ", b.etCardvalimonth);
            return;
        }
        if (year.equals("")) {
            paymentError("유효기간 '연도'를 정확히 입력해주세요. ", b.etCardvaliyear);
            return;
        }
        Log.d(TAG, "payment: " + year + "-" + month + "-01 00:00:00");
        if (tsDate.getTime() > Timestamp.valueOf(year + "-" + month + "-01 00:00:00").getTime()) {
            paymentError("유효기간을 잘못 입력하셨거나,\n유효기간이 만료된 카드입니다.");
            return;
        }
        if (b.etCardpw.getText().toString().length() != 2) {
            paymentError("비밀번호 2자리를 입력해주세요.", b.etCardpw);
            return;
        }
        if (b.etIdentifynum.getText().toString().length() != 6) {
            paymentError("인증번호 6자리를 입력해주세요.", b.etIdentifynum);
            return;
        }
        StringBuilder sb = new StringBuilder(b.etIdentifynum.getText().toString());
        try {
            Timestamp.valueOf("19" + sb.substring(0, 2) + "-" + sb.substring(2, 4)
                    + "-" + sb.substring(4, 6) + " 00:00:00");
        } catch (IllegalArgumentException e) {
            paymentError("인증번호가 올바르지 않습니다. ", b.etIdentifynum);
            return;
        }
        if (!b.cbAgree.isChecked()) {
            paymentError("개인정보 수집 및 이용동의에 동의하시기\n바랍니다.");
            return;
        }

    }
    // 개인정보 수집 및 이용동의에 동의하시기 // 바랍니다.
    private void paymentError(String content) {
        new MyDialog(getContext(), getLayoutInflater(), "이용안내",
                content).setPurple().show();
    }

    private void paymentError(String content, EditText editText) {
        new MyDialog(getContext(), getLayoutInflater(), "이용안내",
                content).setPurple().show();
        editText.requestFocus();
    }
}