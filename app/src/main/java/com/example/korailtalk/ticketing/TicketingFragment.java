package com.example.korailtalk.ticketing;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.korailtalk.MainActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.databinding.FragmentTicketingBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public class TicketingFragment extends Fragment {

    private FragmentTicketingBinding b;
    private HashMap<TextView, View> underlineMap = new HashMap<>();
    private Context context;
    private View.OnClickListener onCityClick;
    private boolean isRoundTrip = false;
    private GestureDetector detector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentTicketingBinding.inflate(inflater, container, false);
        context = getContext();

        underlineMap.put(b.tvTicDep, b.vTicDep);
        underlineMap.put(b.tvTicArr, b.vTicArr);

        b.tlTic.addTab(b.tlTic.newTab().setText("편도"));
        b.tlTic.addTab(b.tlTic.newTab().setText("왕복"));
        b.tlTic.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isRoundTrip = tab.getPosition() == 1;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        b.ivTicNodefold.setOnClickListener(view -> {
            b.tlTic.setVisibility(View.VISIBLE);
            b.llTicHide.setVisibility(View.VISIBLE);
            b.llTic.setVisibility(View.GONE);
        });

        onCityClick = view -> {
            TextView tvClick = (TextView) view;
            TextView tvNotClick = view.getId() == R.id.tv_tic_arr ? b.tvTicDep : b.tvTicArr;
            View ulClick = underlineMap.get(tvClick);
            View ulNotClick = underlineMap.get(tvNotClick);
            b.tlTic.setVisibility(View.GONE);
            b.llTicHide.setVisibility(View.GONE);
            b.llTic.setVisibility(View.VISIBLE);
            tvClick.setTextColor(ContextCompat.getColor(context, R.color.main));
            tvNotClick.setTextColor(ContextCompat.getColor(context, R.color.gray2));
            ulClick.getLayoutParams().width = tvClick.getWidth();
            ulClick.setLayoutParams(ulClick.getLayoutParams());
            ulNotClick.getLayoutParams().width = 0;
            ulNotClick.setLayoutParams(ulNotClick.getLayoutParams());
        };

        b.tvTicDep.setOnClickListener(onCityClick);
        b.tvTicArr.setOnClickListener(onCityClick);

        b.etTicNode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                b.ivTicNodeclear.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        b.ivTicNodeclear.setOnClickListener(view -> b.etTicNode.setText(""));

        TextView[] searchFirstLetter = {b.tvTicSfl1, b.tvTicSfl2, b.tvTicSfl3, b.tvTicSfl4,
                b.tvTicSfl5, b.tvTicSfl6, b.tvTicSfl7, b.tvTicSfl8, b.tvTicSfl9,
                b.tvTicSfl10, b.tvTicSfl11, b.tvTicSfl12, b.tvTicSfl13, b.tvTicSfl14, b.tvTicSfl15};

        detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            float sflHeight = 0;
            int index = 0;

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                sflHeight = b.llTicSfl.getHeight();
                index = (int) (motionEvent.getY() / (sflHeight / 15));
                b.rlTicDark.setVisibility(View.VISIBLE);
                b.tvTicSfl.setText(searchFirstLetter[index].getText());
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                index = (int) (motionEvent1.getY() / (sflHeight / 15));
                if (index >= 0 && index <= 14) b.tvTicSfl.setText(searchFirstLetter[index].getText());
                else if (index < 0) b.tvTicSfl.setText(searchFirstLetter[0].getText());
                else b.tvTicSfl.setText(searchFirstLetter[14].getText());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });

        b.llTicSfl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    b.rlTicDark.setVisibility(View.GONE);
                }
                return true;
            }
        });

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

}