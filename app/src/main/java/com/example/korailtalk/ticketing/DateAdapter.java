package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.Util;
import com.example.korailtalk.databinding.RvDateBinding;

import java.sql.Timestamp;


public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private final LayoutInflater inflater;
    private final TicketingFragment fragment;
    private final Timestamp[] timesArr;
    private int selectedPosition = 0;
    private final Context context;

    public DateAdapter(TicketingFragment fragment, Timestamp[] timesArr) {
        this.fragment = fragment;
        this.timesArr = timesArr;
        inflater = fragment.getLayoutInflater();
        context = fragment.getContext();
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
        holder.b.tvDay.setText(day);
        if (position != 0) {
            holder.b.tvWeek.setText(week);
        } else {
            holder.b.tvDepday.setVisibility(View.VISIBLE);
            holder.b.ivToday.setVisibility(View.VISIBLE);
        }

        if (position == selectedPosition) {
            holder.b.vBack.setVisibility(View.VISIBLE);
            holder.b.tvDepday.setVisibility(View.VISIBLE);
            holder.b.tvDay.setTextColor(ContextCompat.getColor(context, R.color.white));
            if (day.equals("1")) holder.b.ivSlash.setVisibility(View.INVISIBLE);
            if (position == 0) {
                holder.b.ivToday.setImageResource(R.drawable.ic_baseline_circle_24);
                holder.b.tvDepday.setText("출발일");
                holder.b.tvDepday.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        } else {
            holder.b.vBack.setVisibility(View.INVISIBLE);
            holder.b.tvDay.setTextColor(ContextCompat.getColor(context, R.color.black));
            setColorWeek(holder.b.tvWeek, holder.b.tvDay, week);
            if (day.equals("1")) holder.b.ivSlash.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.b.ivToday.setImageResource(R.drawable.ic_baseline_circle_main);
                holder.b.tvDepday.setText("오늘");
                holder.b.tvDepday.setTextColor(ContextCompat.getColor(context, R.color.main3));
            } else holder.b.tvDepday.setVisibility(View.INVISIBLE);
        }

        if (day.equals("1") && position != selectedPosition) {
            holder.b.ivSlash.setVisibility(View.VISIBLE);
            holder.b.tvMonth.setVisibility(View.VISIBLE);
            holder.b.tvMonth.setText(Util.dateFormat(timesArr[position], "M"));
            if (week.equals("토"))
                holder.b.tvMonth.setTextColor(ContextCompat.getColor(context, R.color.skyblue));
            else if (week.equals("일"))
                holder.b.tvMonth.setTextColor(ContextCompat.getColor(context, R.color.red));
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

    private void setColorWeek(TextView tvWeek, TextView tvDay, String week) {
        if (week.equals("토")) {
            tvWeek.setTextColor(ContextCompat.getColor(context, R.color.skyblue));
            tvDay.setTextColor(ContextCompat.getColor(context, R.color.skyblue));
        } else if (week.equals("일")) {
            tvWeek.setTextColor(ContextCompat.getColor(context, R.color.red));
            tvDay.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {

        RvDateBinding b;

        public DateViewHolder(@NonNull View v) {
            super(v);
            b = RvDateBinding.bind(v);
            v.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    fragment.onDateClick(timesArr[getAdapterPosition()], getAdapterPosition());
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

    }

}
