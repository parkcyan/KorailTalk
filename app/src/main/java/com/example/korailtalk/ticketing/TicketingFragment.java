package com.example.korailtalk.ticketing;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.korailtalk.Util;
import com.example.korailtalk.node.NodeRoom;
import com.example.korailtalk.MainActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.databinding.FragmentTicketingBinding;
import com.google.android.material.tabs.TabLayout;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TicketingFragment extends Fragment {

    private TicketingFragment fragment;
    private FragmentTicketingBinding b;
    private Context context;
    private boolean isRoundTrip = false;
    private boolean isDepArr = true;
    private NodeRoom nodeRoom;
    private NodeAdapter adapter;
    // sfl = search first letter
    private final ImageView[] ivSfl = new ImageView[15];
    private final TextView[] tvSfl = new TextView[15];
    private final int[] sflPosition = new int[15];
    private final String[] sfl = {"가", "최", "주", "ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ",
            "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅌ", "ㅍ", "ㅎ"};
    private final Calendar cal = Calendar.getInstance();
    private Timestamp tsDate = new Timestamp(System.currentTimeMillis());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentTicketingBinding.inflate(inflater, container, false);
        context = getContext();
        fragment = this;

        nodeRoom = new NodeRoom(context, getNodeHandler());

        /* 탭 */
        b.tlRoundTrip.addTab(b.tlRoundTrip.newTab().setText("편도"));
        b.tlRoundTrip.addTab(b.tlRoundTrip.newTab().setText("왕복"));
        b.tlRoundTrip.addOnTabSelectedListener(onTabSelect());

        /* 역 선택창 */
        b.llNodefold.setOnClickListener(view -> nodeFold());

        adapter = new NodeAdapter(fragment);
        b.rvNode.setItemViewCacheSize(30);
        Util.setRecyclerView(context, b.rvNode, adapter, true);

        nodeExpand();
        b.rvNode.post(() -> {
            b.rvNode.getLayoutParams().height = b.llSfl.getHeight();
            b.rvNode.setLayoutParams(b.rvNode.getLayoutParams());
            nodeFold();
        });

        int position = 0;
        for (int i = 0; i < NodeRoom.nodeListForRv.size(); i++) {
            if (NodeRoom.nodeListForRv.get(i).getViewType() == 1) {
                sflPosition[position++] = i;
            }
        }

        for (int i = 0; i < ivSfl.length; i++) {
            ImageView iv = makeSflCircle();
            TextView tv = makeSflText();
            tv.setText(sfl[i]);
            b.llSfl.addView(tv);
            b.llSflc.addView(iv);
            tvSfl[i] = tv;
            ivSfl[i] = iv;
        }

        b.llSfl.setOnTouchListener(onSflTouch());
        b.etNode.addTextChangedListener(edtTextChange());
        b.ivNodeclear.setOnClickListener(view -> b.etNode.setText(""));

        /* 역 확인창 */
        b.rlDep.setOnClickListener(onCityClick());
        b.rlArr.setOnClickListener(onCityClick());

        /* 출발일 */
        setDate();
        //b.llDate.setOnClickListener(optionClick());

        Timestamp[] timesArr = new Timestamp[31];

        timesArr[0] = tsDate;
        for (int i = 1; i < timesArr.length; i++) {
            cal.add(Calendar.DATE, 1);
            timesArr[i] = new Timestamp(cal.getTime().getTime());
        }
        Util.setRecyclerView(context, b.rvDate, new DateAdapter(this, timesArr), false);
        Util.setRecyclerView(context, b.rvTime, new TimeAdapter(this), false);
        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private void setDate() {
        b.tvDate.setText(Util.dateFormat(tsDate, "yyyy년 M월 d일 (E) HH:mm"));
    }

    private Handler getNodeHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == NodeRoom.SEARCH_SUCCESS) {
                    adapter = new NodeAdapter(fragment, (ArrayList<NodeForRv>) msg.obj);
                    Util.setRecyclerView(context, b.rvSearch, adapter, true);
                }
            }
        };
    }

    private TabLayout.OnTabSelectedListener onTabSelect() {
        return new TabLayout.OnTabSelectedListener() {
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
        };
    }

    private View.OnClickListener onCityClick() {
        return view -> {
            TextView tvClick, tvNotClick;
            View ulClick, ulNotClick;
            if (view.getId() == R.id.rl_dep) {
                tvClick = b.tvDep;
                tvNotClick = b.tvArr;
                ulClick = b.vDep;
                ulNotClick = b.vArr;
            } else {
                tvClick = b.tvArr;
                tvNotClick = b.tvDep;
                ulClick = b.vArr;
                ulNotClick = b.vDep;
            }
            tvClick.setTextColor(ContextCompat.getColor(context, R.color.main));
            tvNotClick.setTextColor(ContextCompat.getColor(context, R.color.gray2));
            ulClick.getLayoutParams().width = tvClick.getWidth();
            ulClick.setLayoutParams(ulClick.getLayoutParams());
            ulNotClick.getLayoutParams().width = 0;
            ulNotClick.setLayoutParams(ulNotClick.getLayoutParams());
            isDepArr = view.getId() == R.id.rl_dep;
            nodeExpand();
        };
    }

    public void citySelect(String node) {
        if (isDepArr) b.tvDep.setText(node);
        else b.tvArr.setText(node);
        b.etNode.setText("");
        nodeFold();
    }

    private TextWatcher edtTextChange() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                b.ivNodeclear.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable edt) {
                if (edt.toString().equals("")) {
                    b.flFicNodeselect.setVisibility(View.VISIBLE);
                    b.rvSearch.setVisibility(View.GONE);
                } else {
                    nodeRoom.searchNodes(edt.toString());
                    b.rvSearch.setVisibility(View.VISIBLE);
                    b.flFicNodeselect.setVisibility(View.GONE);
                }
            }
        };
    }

    public void keyboardDown() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(b.rvNode.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private View.OnTouchListener onSflTouch() {
        return new View.OnTouchListener() {
            final GestureDetector gesture = new GestureDetector(context, onSflScroll());

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gesture.onTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    b.rlDark.setVisibility(View.GONE);
                    setTextGray();
                    b.llSfl.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button));
                }
                return true;
            }
        };
    }

    private GestureDetector.OnGestureListener onSflScroll() {
        return new GestureDetector.OnGestureListener() {
            float sflHeight = 0;
            int index = 0;

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                sflHeight = b.llSfl.getHeight();
                index = (int) (motionEvent.getY() / (sflHeight / 15));
                b.rlDark.setVisibility(View.VISIBLE);
                b.tvSfl.setText(tvSfl[index].getText());
                setCircleVis(index);
                setTextWhite(index);
                ((LinearLayoutManager) b.rvNode.getLayoutManager()).scrollToPositionWithOffset(sflPosition[index], 0);
                b.llSfl.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button_lightgray));
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
                    b.tvSfl.setText(tvSfl[index].getText());
                    setCircleVis(index);
                    setTextWhite(index);
                    ((LinearLayoutManager) b.rvNode.getLayoutManager()).scrollToPositionWithOffset(sflPosition[index], 0);
                } else if (index < 0) b.tvSfl.setText(tvSfl[0].getText());
                else b.tvSfl.setText(tvSfl[14].getText());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        };
    }

    private void nodeFold() {
        b.tlRoundTrip.setVisibility(View.VISIBLE);
        b.llHide.setVisibility(View.VISIBLE);
        b.llSelect.setVisibility(View.VISIBLE);
        b.llNode.setVisibility(View.GONE);
        b.tvDep.setTextColor(ContextCompat.getColor(context, R.color.main));
        b.tvArr.setTextColor(ContextCompat.getColor(context, R.color.main));
        b.vArr.getLayoutParams().width = 0;
        b.vArr.setLayoutParams(b.vArr.getLayoutParams());
        b.vDep.getLayoutParams().width = 0;
        b.vDep.setLayoutParams(b.vDep.getLayoutParams());
        ((MainActivity) getActivity()).showBnv(true);
    }

    private void nodeExpand() {
        b.tlRoundTrip.setVisibility(View.GONE);
        b.llHide.setVisibility(View.GONE);
        b.llSelect.setVisibility(View.GONE);
        b.llNode.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).showBnv(false);
    }

    private View.OnClickListener optionClick() {
        return view -> {
            if (view.getVisibility() == View.GONE) {
                b.llQty.setVisibility(View.GONE);
                b.llOption.setVisibility(View.GONE);
                b.llDate.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        };
    }

    public void onDateClick(Timestamp date) {
        StringBuilder dateStr = new StringBuilder(Util.dateFormat(date, "yyyy-MM-dd HH:mm:ss"));
        dateStr.replace(11, dateStr.length(),"00:00:00");
        tsDate = Timestamp.valueOf(dateStr.toString());
        setDate();
    }

    public void onTimeClick(String time) {
        StringBuilder dateStr = new StringBuilder(Util.dateFormat(tsDate, "yyyy-MM-dd HH:mm:ss"));
        dateStr.replace(11, dateStr.length(),time + ":00:00");
        tsDate = Timestamp.valueOf(dateStr.toString());
        setDate();
    }

    private TextView makeSflText() {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        tv.setTextColor(ContextCompat.getColor(context, R.color.gray3));
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

    private void setTextWhite(int index) {
        for (int i = 0; i < tvSfl.length; i++) {
            if (i != index) tvSfl[i].setTextColor(ContextCompat.getColor(context, R.color.gray3));
            else tvSfl[i].setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    private void setTextGray() {
        for (TextView textView : tvSfl) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.gray3));
        }
    }

}