package com.example.korailtalk.checkticket;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.FragmentCheckticketBinding;
import com.example.korailtalk.room.ticket.Ticket;
import com.example.korailtalk.room.ticket.TicketRoom;
import com.example.korailtalk.util.Util;

import java.util.ArrayList;

public class CheckTicketFragment extends Fragment {

    private FragmentCheckticketBinding b;
    private TicketRoom ticketRoom;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentCheckticketBinding.inflate(inflater, container, false);
        context = getContext();
        ticketRoom = new TicketRoom(getContext(), getTicketHandler());
        ticketRoom.getTickets();
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
                if (msg.what == TicketRoom.GET_LIST_TICKETS) {
                    ArrayList<Ticket> ticketList = (ArrayList<Ticket>) msg.obj;
                    Util.setRecyclerView(context, b.rvCheckticket, new CheckTicketAdapter(
                            CheckTicketFragment.this, ticketList), true);
                    b.rvCheckticket.post(() -> b.rlProgress.setVisibility(View.GONE));
                    if (b.tlTicketkind.getTabCount() != 2) {
                        b.tlTicketkind.addTab(b.tlTicketkind.newTab().setText("승차권 (" + ticketList.size() + ")"));
                        b.tlTicketkind.addTab(b.tlTicketkind.newTab().setText("정기권 · 패스"));
                    }
                }
            }
        };
    }

    public void refreshTicketRv() {
        b.rlProgress.setVisibility(View.VISIBLE);
        ticketRoom.getTickets();
    }

}