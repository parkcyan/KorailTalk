package com.example.korailtalk;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.korailtalk.checkTicket.CheckTicketFragment;
import com.example.korailtalk.databinding.ActivityMainBinding;
import com.example.korailtalk.ticketing.TicketingFragment;
import com.example.korailtalk.util.BaseActivity;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding b;
    private TicketingFragment ticketingFragment;
    private CheckTicketFragment checkTicketFragment;
    private int fragmentIndex = 0;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b.toolbar.ivBack.setVisibility(View.INVISIBLE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("nodeList", getIntent().getSerializableExtra("nodeList"));

        ticketingFragment = new TicketingFragment();
        ticketingFragment.setArguments(bundle);

        fragments = new Fragment[]{ticketingFragment, null, null, null};

        setBnv();
    }

    private void setBnv() {
        addFragment(R.id.container_main, ticketingFragment);
        b.bnvMain.bnvMain.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bnv_main_tic && ticketingFragment != null)
                changeFragment(0);
            else if (item.getItemId() == R.id.bnv_main_seasontic) {

            } else if (item.getItemId() == R.id.bnv_main_goods) {

            } else if (item.getItemId() == R.id.bnv_main_checktic) {
                if (checkTicketFragment == null) {
                    checkTicketFragment = new CheckTicketFragment();
                    fragments[3] = checkTicketFragment;
                    addFragment(R.id.container_main, checkTicketFragment);
                }
                if (checkTicketFragment != null) changeFragment(3);
            }
            return true;
        });
    }

    public void showBnv(boolean show) {
        if (show) b.bnvMain.bnvMain.setVisibility(View.VISIBLE);
        else b.bnvMain.bnvMain.setVisibility(View.GONE);
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
        if (ticketingFinish) {
            ticketingFinish = false;
            b.bnvMain.bnvMain.setSelectedItemId(R.id.bnv_main_checktic);
        }
    }

    private void changeFragment(int index) {
        hideFragment(fragments[fragmentIndex]);
        showFragment(fragments[index]);
        fragmentIndex = index;
    }

}