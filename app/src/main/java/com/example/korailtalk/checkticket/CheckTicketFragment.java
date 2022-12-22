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
import com.example.korailtalk.util.KtAlertDialog;
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
                    b.rvCheckticket.post(() -> {
                        b.rlProgress.setVisibility(View.GONE);
                        if (ticketList.size() == 0) b.clNoticket.setVisibility(View.VISIBLE);
                    });
                    b.tlTicketkind.getTabAt(0).setText("승차권 (" + ticketList.size() + ")");
                } else if (msg.what == TicketRoom.DELETE_TICKET_SUCCESS) refreshTicketRv();
            }
        };
    }

    public void refund(Ticket ticket) {
        new KtAlertDialog(context, getLayoutInflater(), "이용안내", getString(R.string.refund), new KtAlertDialog.OnAlertDialogClickListener() {
            @Override
            public void setOnClickYes(KtAlertDialog dialog) {
                ticketRoom.deleteTicket(ticket);
                dialog.dismiss();
            }
            @Override
            public void setOnClickNo(KtAlertDialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    public void refreshTicketRv() {
        b.clNoticket.setVisibility(View.GONE);
        b.rlProgress.setVisibility(View.VISIBLE);
        ticketRoom.getTickets();
    }

}