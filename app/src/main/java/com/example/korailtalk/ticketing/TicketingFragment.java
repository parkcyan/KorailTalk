package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

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
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.korailtalk.Util;
import com.example.korailtalk.node.NodeRoom;
import com.example.korailtalk.MainActivity;
import com.example.korailtalk.R;
import com.example.korailtalk.databinding.FragmentTicketingBinding;
import com.example.korailtalk.node.Node;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketingFragment extends Fragment {

    private TicketingFragment fragment;
    private FragmentTicketingBinding b;
    private Context context;
    private boolean isRoundTrip = false;
    private boolean isDepArr = true;
    private NodeRoom nodeRoom;
    private NodeAdapter adapter;
    private final HashMap<TextView, View> underlineMap = new HashMap<>();
    // sfl = search first letter
    private final ImageView[] ivSfl = new ImageView[15];
    private final TextView[] tvSfl = new TextView[15];
    private final String[] sfl = {"가", "최", "주", "ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ",
            "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅌ", "ㅍ", "ㅎ"};


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentTicketingBinding.inflate(inflater, container, false);
        context = getContext();
        fragment = this;

        underlineMap.put(b.tvDep, b.vDep);
        underlineMap.put(b.tvArr, b.vArr);

        nodeRoom = new NodeRoom(context, getNodeHandler());
        nodeRoom.getMainNodes();

        b.tlRoundTrip.addTab(b.tlRoundTrip.newTab().setText("편도"));
        b.tlRoundTrip.addTab(b.tlRoundTrip.newTab().setText("왕복"));
        b.tlRoundTrip.addOnTabSelectedListener(onTabSelect());

        b.llNodefold.setOnClickListener(view -> nodeFold());

        b.tvDep.setOnClickListener(onCityClick());
        b.tvArr.setOnClickListener(onCityClick());

        b.etNode.addTextChangedListener(edtTextChange());

        b.ivNodeclear.setOnClickListener(view -> b.etNode.setText(""));

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

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private Handler getNodeHandler() {
        return new Handler(Looper.getMainLooper()) {
            ArrayList<Node> nodeList = new ArrayList<>();
            ArrayList<Node> nodeMain;
            ArrayList<Node> nodeAll;
            ArrayList<Integer> nodeIndexArr;

            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    nodeMain = (ArrayList<Node>) msg.obj;
                    nodeRoom.getAllNodes();
                } else if (msg.what == 2) {
                    nodeAll = (ArrayList<Node>) msg.obj;
                    nodeRoom.getNodeCountBetween();
                } else if (msg.what == 3) {
                    nodeIndexArr = (ArrayList<Integer>) msg.obj;
                    nodeList.add(null); // 가까운역
                    nodeList.add(null); // 최근검색구간
                    nodeList.add(null); // 주요역
                    nodeList.addAll(nodeMain);
                    nodeList.addAll(nodeAll);
                    int index = 0;
                    int onlyLeft = nodeMain.size() % 2;
                    for (int i = 0; i < nodeIndexArr.size(); i++) {
                        if (i != nodeIndexArr.size() - 1)
                            nodeList.add(3 + nodeMain.size() + nodeIndexArr.get(i) + index++, null);
                        if (i != 0 && (nodeIndexArr.get(i) - nodeIndexArr.get(i - 1)) % 2 != 0)
                            onlyLeft++;
                    }
                    int itemCount = sfl.length + (nodeList.size() - sfl.length - onlyLeft) / 2 + onlyLeft;
                    adapter = new NodeAdapter(getLayoutInflater(), nodeList, fragment, false, itemCount);
                    Util.setRecyclerView(context, b.rvMain, adapter, true);
                } else if (msg.what == 4) {
                    adapter = new NodeAdapter(getLayoutInflater(), (ArrayList<Node>) msg.obj, fragment, true, 0);
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
            TextView tvClick = (TextView) view;
            TextView tvNotClick = view.getId() == R.id.tv_arr ? b.tvDep : b.tvArr;
            View ulClick = underlineMap.get(tvClick);
            View ulNotClick = underlineMap.get(tvNotClick);
            tvClick.setTextColor(ContextCompat.getColor(context, R.color.main));
            tvNotClick.setTextColor(ContextCompat.getColor(context, R.color.gray2));
            ulClick.getLayoutParams().width = tvClick.getWidth();
            ulClick.setLayoutParams(ulClick.getLayoutParams());
            ulNotClick.getLayoutParams().width = 0;
            ulNotClick.setLayoutParams(ulNotClick.getLayoutParams());
            isDepArr = view.getId() == R.id.tv_dep;
            nodeExpand();
        };
    }

    public void citySelect(String node) {
        if (isDepArr) b.tvDep.setText(node);
        else b.tvArr.setText(node);
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

    private void nodeFold() {
        b.tlRoundTrip.setVisibility(View.VISIBLE);
        b.llHide.setVisibility(View.VISIBLE);
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
        b.llNode.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).showBnv(false);
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