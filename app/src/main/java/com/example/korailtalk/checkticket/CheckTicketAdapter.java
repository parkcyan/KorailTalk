package com.example.korailtalk.checkticket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.RvTicketBinding;
import com.example.korailtalk.room.ticket.Ticket;
import com.example.korailtalk.util.Util;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CheckTicketAdapter extends RecyclerView.Adapter<CheckTicketAdapter.CheckTicketViewHolder> {

    private final LayoutInflater inflater;
    private final CheckTicketFragment fragment;
    private final ArrayList<Ticket> ticketList;

    public CheckTicketAdapter(CheckTicketFragment fragment, ArrayList<Ticket> ticketList) {
        this.fragment = fragment;
        inflater = fragment.getLayoutInflater();
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public CheckTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckTicketViewHolder(inflater.inflate(R.layout.rv_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckTicketViewHolder holder, int position) {
        RvTicketBinding b = holder.b;
        Ticket ticket = ticketList.get(position);
        int[] qtyArr = ticket.getQtyArr();
        int sum = 0;
        for (int i : qtyArr) sum += i;
        String qty = "스마트티켓 " + sum + "매";
        String trainInfo = !ticket.getSpecialSeat() ? "일반실" : "특실";
        trainInfo = trainInfo + " | 순방향 | " + ticket.getTicketKind();

        b.tvDate.setText(Util.dateFormat(Timestamp.valueOf(ticket.getTsDate()), "yyyy-M-d (E)"));
        b.tvQty.setText(qty);
        b.tvDepnode.setText(ticket.getDepNode());
        b.tvArrnode.setText(ticket.getArrNode());
        b.tvDeptime.setText(ticket.getDepTime());
        b.tvArrtime.setText(ticket.getArrTime());
        b.tvTraininfo.setText(ticket.getTrainInfo());
        b.tvTrainnum.setText(ticket.getTrainNum());
        b.tvSeat.setText(ticket.getSeat() + "A");
        b.tvTicketinfo.setText(trainInfo);
        b.tvTicketnum.setText(ticket.getTicketNum());
        b.tvTicketingdate.setText(Util.dateFormat(Timestamp.valueOf(ticket.getTicketingDate())));
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class CheckTicketViewHolder extends RecyclerView.ViewHolder {

        RvTicketBinding b;

        public CheckTicketViewHolder(@NonNull View v) {
            super(v);
            b = RvTicketBinding.bind(v);
            b.tvRefund.setOnClickListener(v1 ->
                    fragment.refund(ticketList.get(getAdapterPosition())));
        }
    }
}
