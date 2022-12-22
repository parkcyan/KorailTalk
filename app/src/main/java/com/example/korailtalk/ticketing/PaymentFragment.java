package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.korailtalk.R;
import com.example.korailtalk.room.ticket.Ticket;
import com.example.korailtalk.room.ticket.TicketRoom;
import com.example.korailtalk.util.KtDialog;
import com.example.korailtalk.util.Util;
import com.example.korailtalk.databinding.FragmentPaymentBinding;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding b;
    private EditText[] etArr;
    private final int[] maxLength = {4, 4, 4, 4, 2, 4, 2, 6};
    private Timestamp tsDate;
    private TicketRoom ticketRoom;
    private TrainVO train;
    private Bundle bundle;
    private boolean specialSeat;
    private int qty = 0;
    private int[] qtyArr;
    private String ticketNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentPaymentBinding.inflate(inflater, container, false);
        bundle = getArguments();
        ticketRoom = new TicketRoom(getContext(), getTicketHandler());

        train = (TrainVO) bundle.getSerializable("train");
        specialSeat = bundle.getBoolean("special");
        qtyArr = bundle.getIntArray("qtyArr");
        tsDate = (Timestamp) bundle.getSerializable("tsDate");
        
        // 탭
        b.tlMethod.addTab(b.tlMethod.newTab().setText("카드결제"));
        b.tlMethod.addTab(b.tlMethod.newTab().setText("간편결제"));
        
        // 티켓정보
        for (int i : qtyArr) qty += i;
        DecimalFormat df = new DecimalFormat("###,###원");

        int charge = specialSeat ? Util.roundCharge(train.getCharge() * 1.4) : train.getCharge();
        int totalCharge = charge * qty;
        int discountCharge = bundle.getInt("discountCharge");
        b.tvCharge.setText(df.format(discountCharge));

        String discountStr = df.format(totalCharge) + " (총 " + df.format(totalCharge - discountCharge) + " 할인)";
        b.tvDiscount.setText(discountStr);
        
        // 결제 입력창
        etArr = new EditText[]{b.etCardnum1, b.etCardnum2, b.etCardnum3, b.etCardnum4, b.etCardvalimonth, b.etCardvaliyear,
                b.etCardpw, b.etIdentifynum};
        
        b.llClear.setOnClickListener(v -> {
            for (EditText et : etArr) et.setText("");
            etArr[0].requestFocus();
        });

        for (int i = 0; i < etArr.length; i++) {
            etArr[i].addTextChangedListener(setTextWatcher(i));
        }
        
        b.rbNormal.setChecked(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.mycard, android.R.layout.simple_spinner_dropdown_item);
        b.spMycard.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.installment,
                android.R.layout.simple_spinner_dropdown_item);
        b.spInstallment.setAdapter(adapter);
        b.spMycard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) b.llClear.performClick();
                else if (position == 1) {
                    b.etCardnum1.setText("1234");
                    b.etCardnum2.setText("1234");
                    b.etCardnum3.setText("1234");
                    b.etCardnum4.setText("1234");
                    b.etCardvalimonth.setText("12");
                    b.etCardvaliyear.setText("2028");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        // 티켓번호 랜덤 생성
        Random rand = new Random();
        ticketNum = (rand.nextInt(10000) + 80000) + "-" + (rand.nextInt(1000) + 1000) +
                "-" + (rand.nextInt(10000) + 10000) + "-" + (rand.nextInt(10) + 10);

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private Handler getTicketHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == TicketRoom.INSERT_TICKET_SUCCESS) {
                    ((PaymentActivity) getActivity()).ticketingFinish();
                }
            }
        };
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
        String ticketKind;
        if (qtyArr[0] != 0) ticketKind = "어른";
        else if (qtyArr[3] != 0 || qtyArr[4]!= 0 || qtyArr[5]!= 0) ticketKind = "어른(할인)";
        else ticketKind = "어린이";
        Ticket ticket = new Ticket(Util.getCurrentTime().toString(), tsDate.toString(),
                train.getDepplacename(), train.getArrplacename(), train.getDepplandtime(),
                train.getArrplandtime(), train.getTraingradename() + " " + train.getTrainno(),
                bundle.getString("trainNum"), bundle.getString("seat"), specialSeat, ticketKind,
                ticketNum, qtyArr);
        ticketRoom.addTicket(ticket);
    }

    private void paymentError(String content) {
        new KtDialog(getContext(), getLayoutInflater(), "이용안내",
                content).setPurple().show();
    }

    private void paymentError(String content, EditText editText) {
        new KtDialog(getContext(), getLayoutInflater(), "이용안내",
                content).setPurple().show();
        editText.requestFocus();
    }
}