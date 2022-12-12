package com.example.korailtalk.ticketing;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.korailtalk.node.NodeRoom;
import com.example.korailtalk.MainActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.databinding.FragmentTicketingBinding;
import com.example.korailtalk.node.Node;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketingFragment extends Fragment {

    private FragmentTicketingBinding b;
    private Context context;
    private boolean isRoundTrip = false;
    private NodeRoom nodeRoom;
    private NodeAdapter adapter;
    private final HashMap<TextView, View> underlineMap = new HashMap<>();
    private final ImageView[] ivSfl = new ImageView[15];
    private final TextView[] tvSfl = new TextView[15];
    private final String[] ns = {"가", "나", "다", "마", "바", "사", "자", "차", "타", "파", "하", "힣"};
    private final String[] sfl = {"가", "최", "주", "ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ",
            "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅌ", "ㅍ", "ㅎ"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentTicketingBinding.inflate(inflater, container, false);
        context = getContext();

        underlineMap.put(b.tvTicDep, b.vTicDep);
        underlineMap.put(b.tvTicArr, b.vTicArr);

        nodeRoom = new NodeRoom(context, getNodeNHandler());
        nodeRoom.getMainNodes();

        b.tlTic.addTab(b.tlTic.newTab().setText("편도"));
        b.tlTic.addTab(b.tlTic.newTab().setText("왕복"));
        b.tlTic.addOnTabSelectedListener(onTabSelect());

        b.llTicNodefold.setOnClickListener(view -> {
            b.tlTic.setVisibility(View.VISIBLE);
            b.llTicHide.setVisibility(View.VISIBLE);
            b.llTic.setVisibility(View.GONE);
            ((MainActivity) getActivity()).showBnv(true);
        });

        b.tvTicDep.setOnClickListener(onCityClick());
        b.tvTicArr.setOnClickListener(onCityClick());

        b.etTicNode.addTextChangedListener(edtTextChange());

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

        b.llTicSfl.setOnTouchListener(onSflTouch());

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private Handler getNodeNHandler() {
        return new Handler(Looper.getMainLooper()) {
            boolean getAllNodes = false, getNodeCountBetween = false;
            ArrayList<Node> nodeArr;
            ArrayList<Integer> nodeIndexArr;

            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    adapter = new NodeAdapter(getLayoutInflater(), (ArrayList<Node>) msg.obj);
                    b.rvTicMain.setAdapter(adapter);
                    b.rvTicMain.setLayoutManager(lm);
                    nodeRoom.getAllNodes();
                } else if (msg.what == 2) {
                    getAllNodes = true;
                    nodeArr = (ArrayList<Node>) msg.obj;
                }
                else if (msg.what == 3) {
                    getNodeCountBetween = true;
                    nodeIndexArr = (ArrayList<Integer>) msg.obj;
                }
                if (getAllNodes && getNodeCountBetween) {
                    for (int i = 0; i < nodeIndexArr.size() - 1; i++) {
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        b.llTicNodeselect.addView(makeNodeSelectText(sfl[3 + i]));
                        ArrayList<Node> arr = new ArrayList<>();
                        for (int j = nodeIndexArr.get(i); j < nodeIndexArr.get(i + 1); j++) {
                            arr.add(nodeArr.get(j));
                        }
                        RecyclerView rv = new RecyclerView(context);
                        adapter = new NodeAdapter(getLayoutInflater(), arr);
                        rv.setAdapter(adapter);
                        rv.setLayoutManager(lm);
                        rv.setLayoutParams(lp);
                        rv.setNestedScrollingEnabled(false);
                        b.llTicNodeselect.addView(rv);
                    }
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
    }

    private TextWatcher edtTextChange() {
        return new TextWatcher() {
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
        };
    }

    private GestureDetector.OnGestureListener sflScroll() {
        return new GestureDetector.OnGestureListener() {
            float sflHeight = 0;
            int index = 0;

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                sflHeight = b.llTicSfl.getHeight();
                index = (int) (motionEvent.getY() / (sflHeight / 15));
                b.rlTicDark.setVisibility(View.VISIBLE);
                b.tvTicSfl.setText(tvSfl[index].getText());
                setCircleVis(index);
                setTextWhite(index);
                b.llTicSfl.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button_lightgray));
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
                    setTextWhite(index);
                } else if (index < 0) b.tvTicSfl.setText(tvSfl[0].getText());
                else b.tvTicSfl.setText(tvSfl[14].getText());
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

    private View.OnTouchListener onSflTouch() {
        return new View.OnTouchListener() {
            final GestureDetector gesture = new GestureDetector(context, sflScroll());
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gesture.onTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    b.rlTicDark.setVisibility(View.GONE);
                    setTextGray();
                    b.llTicSfl.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button));
                }
                return true;
            }
        };
    }

    private TextView makeNodeSelectText(String text) {
        TextView tv = new TextView(context);
        tv.setTextAppearance(R.style.tvstyle_nodeselect);
        tv.setText(text);
        return tv;
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