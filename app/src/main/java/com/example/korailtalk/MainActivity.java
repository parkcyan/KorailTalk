package com.example.korailtalk;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.korailtalk.checkticket.CheckTicketFragment;
import com.example.korailtalk.databinding.ActivityMainBinding;
import com.example.korailtalk.ticketing.TicketingFragment;
import com.example.korailtalk.util.BaseActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding b;
    private int preFragmentIndex = 0;
    private Fragment[] fragmentArr;
    private final ArrayList<BnvItem> bnvItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.toolbar.ivBack.setVisibility(View.INVISIBLE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("nodeList", getIntent().getSerializableExtra("nodeList"));

        TicketingFragment ticketingFragment = new TicketingFragment();
        ticketingFragment.setArguments(bundle);

        fragmentArr = new Fragment[]{ticketingFragment, null, null, null};

        // 다른 fragment에서 뒤로가면 첫번째 fragment로 이동
        b.toolbar.ivBack.setOnClickListener(v -> b.bnv.rlTic.performClick());
        b.bnv.rlTic.setOnClickListener(onBnvClick(0));
        b.bnv.rlSeasontic.setOnClickListener(onBnvClick(1));
        b.bnv.rlTour.setOnClickListener(onBnvClick(2));
        b.bnv.rlChecktic.setOnClickListener(onBnvClick(3));

        bnvItemList.add(new BnvItem(b.bnv.ivTic, b.bnv.tvTic, R.drawable.bnvitem1, R.drawable.bnvitem1_selected));
        bnvItemList.add(new BnvItem(b.bnv.ivSeasontic, b.bnv.tvSeasontic, R.drawable.bnvitem2, R.drawable.bnvitem2_selected));
        bnvItemList.add(new BnvItem(b.bnv.ivTour, b.bnv.tvTour, R.drawable.bnvitem3, R.drawable.bnvitem3_selected));
        bnvItemList.add(new BnvItem(b.bnv.ivChecktic, b.bnv.tvChecktic, R.drawable.bnvitem4, R.drawable.bnvitem4_selected));

        addFragment(R.id.container_main, ticketingFragment);
    }

    @Override
    protected String setToolbarTitle() {
        return "승차권 예매";
    }

    @Override
    protected View getLayoutResource() {
        b = ActivityMainBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 승차권 예매 완료후 MainActivity로 돌아오면 승차권 확인창으로 바로이동
        if (ticketingFinish) {
            ticketingFinish = false;
            b.bnv.rlChecktic.performClick();
        }
        if (bnvClickFromLookup == 0) b.bnv.rlTic.performClick();
        else if (bnvClickFromLookup == 1) b.bnv.rlSeasontic.performClick();
        else if (bnvClickFromLookup == 2) b.bnv.rlTour.performClick();
        else if (bnvClickFromLookup == 3) b.bnv.rlChecktic.performClick();
        bnvClickFromLookup = -1;
    }

    @Override
    public void onBackPressed() {
        // 다른 fragment에서 뒤로가면 첫번째 fragment로 이동
        if (preFragmentIndex != 0) {
            b.bnv.rlTic.performClick();
            preFragmentIndex = 0;
        } else super.onBackPressed();
    }

    public View.OnClickListener onBnvClick(int index) {
        return v -> {
            bnvColorChange(index);
            setFragment(index);
            b.toolbar.ivBack.setVisibility(View.VISIBLE);
            if (index == 0) {
                b.toolbar.tvToolbar.setText("승차권 예매");
                b.toolbar.ivBack.setVisibility(View.INVISIBLE);
            } else if (index == 1) b.toolbar.tvToolbar.setText("할인 · 정기권");
            else if (index == 2) b.toolbar.tvToolbar.setText("관광상품");
            else if (index == 3) b.toolbar.tvToolbar.setText("승차권 확인");
        };
    }

    private void bnvColorChange(int index) {
        for (int i = 0; i < bnvItemList.size(); i++) {
            if (i == preFragmentIndex && preFragmentIndex != index) {
                bnvItemList.get(i).iv.setImageResource(bnvItemList.get(i).normalImg);
                bnvItemList.get(i).tv.setTextColor(ContextCompat.getColor(this, R.color.black));
            } else if (i == index) {
                bnvItemList.get(i).iv.setImageResource(bnvItemList.get(i).selectedImg);
                bnvItemList.get(i).tv.setTextColor(ContextCompat.getColor(this, R.color.bnvcolor));
            }
        }
    }

    private void setFragment(int index) {
        if (fragmentArr[index] == null) {
            if (index == 1) fragmentArr[1] = new SeasonTicketFragment();
            else if (index == 2) fragmentArr[2] = new TourFragment();
            else if (index == 3) fragmentArr[3] = new CheckTicketFragment();
            addFragment(R.id.container_main, fragmentArr[index]);
            changeFragment(index);
        } else {
            changeFragment(index);
            if (index == 3) ((CheckTicketFragment) fragmentArr[3]).refreshTicketRv();
        }
    }

    private void changeFragment(int index) {
        hideFragment(fragmentArr[preFragmentIndex]);
        showFragment(fragmentArr[index]);
        preFragmentIndex = index;
    }

    public void showBnv(boolean show) {
        if (show) b.bnv.llBnv.setVisibility(View.VISIBLE);
        else b.bnv.llBnv.setVisibility(View.GONE);
    }

    private static class BnvItem {
        private ImageView iv;
        private TextView tv;
        private int normalImg, selectedImg;

        private BnvItem(ImageView iv, TextView tv, int normalImg, int selectedImg) {
            this.iv = iv;
            this.tv = tv;
            this.normalImg = normalImg;
            this.selectedImg = selectedImg;
        }
    }

}