package com.example.korailtalk.ticketing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.Util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder>{

    private final LayoutInflater inflater;
    private final TicketingFragment fragment;
    private final Timestamp[] timesArr;

    public DateAdapter(TicketingFragment fragment, Timestamp[] timesArr) {
        this.fragment = fragment;
        this.timesArr = timesArr;
        inflater = fragment.getLayoutInflater();
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DateViewHolder(inflater.inflate(R.layout.rv_date, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String week = Util.dateFormat(timesArr[position], "E");
        String day = Util.dateFormat(timesArr[position], "d");
        holder.tvDay.setText(day);
        holder.tvWeek.setText(week);
        if (week.equals("토")) {
            holder.tvWeek.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.skyblue));
            holder.tvDay.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.skyblue));
        } else if (week.equals("일")) {
            holder.tvWeek.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.red));
            holder.tvDay.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return timesArr.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {

        TextView tvWeek, tvMonth, tvDay;

        public DateViewHolder(@NonNull View v) {
            super(v);
            tvWeek = v.findViewById(R.id.tv_week);
            tvMonth = v.findViewById(R.id.tv_month);
            tvDay = v.findViewById(R.id.tv_day);
        }
    }
}
