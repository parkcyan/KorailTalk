package com.example.korailtalk.ticketing;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    private ImageView[] ivSfl = new ImageView[15];
    private TextView[] tvSfl = new TextView[15];
    private final String[] sfl = {"가", "최", "주", "ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ",
            "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅌ", "ㅍ", "ㅎ"};

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

        b.llTicNodefold.setOnClickListener(view -> {
            b.tlTic.setVisibility(View.VISIBLE);
            b.llTicHide.setVisibility(View.VISIBLE);
            b.llTic.setVisibility(View.GONE);
            ((MainActivity) getActivity()).showBnv(true);
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
            ((MainActivity) getActivity()).showBnv(false);
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

        for (int i = 0; i < ivSfl.length; i++) {
            ImageView iv = makeSflCircle();
            TextView tv = makeSflText();
            tv.setText(sfl[i]);
            b.llTicSfl.addView(tv);
            b.llTicSflc.addView(iv);
            tvSfl[i] = tv;
            ivSfl[i] = iv;
        }

        detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            float sflHeight = 0;
            int index = 0;

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                sflHeight = b.llTicSfl.getHeight();
                index = (int) (motionEvent.getY() / (sflHeight / 15));
                b.rlTicDark.setVisibility(View.VISIBLE);
                b.tvTicSfl.setText(tvSfl[index].getText());
                setCircleVis(index);
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
                if (index >= 0 && index <= 14) {
                    b.tvTicSfl.setText(tvSfl[index].getText());
                    setCircleVis(index);
                }
                else if (index < 0) {
                    b.tvTicSfl.setText(tvSfl[0].getText());
                }
                else {
                    b.tvTicSfl.setText(tvSfl[14].getText());
                }
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

    private TextView makeSflText() {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        tv.setTextColor(ContextCompat.getColor(context, R.color.gray2));
        tv.setTextSize(11);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(lp);
        return tv;
    }

    private ImageView makeSflCircle() {
        ImageView iv = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER;
        iv.setVisibility(View.INVISIBLE);
        iv.setImageResource(R.drawable.ic_baseline_circle_24);
        iv.setLayoutParams(lp);
        return iv;
    }

    private void setCircleVis(int index) {
        for (int i = 0; i < ivSfl.length; i++) {
            if (i != index) ivSfl[i].setVisibility(View.INVISIBLE);
            else ivSfl[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

}