package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

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

import java.sql.Timestamp;


public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder>{

    private final LayoutInflater inflater;
    private final TicketingFragment fragment;
    private final Timestamp[] timesArr;
    private int prePosition = 0;

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
        if (position != 0) {
            holder.tvWeek.setText(week);
        } else {
            holder.ivToday.setVisibility(View.VISIBLE);
            holder.vBack.setVisibility(View.VISIBLE);
            holder.tvDepday.setVisibility(View.VISIBLE);
            holder.tvDay.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.white));
        }
        if (week.equals("토")) {
            holder.tvWeek.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.skyblue));
            holder.tvDay.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.skyblue));
        } else if (week.equals("일")) {
            holder.tvWeek.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.red));
            holder.tvDay.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.red));
        }
        if (day.equals("1")) {
            holder.ivSlash.setVisibility(View.VISIBLE);
            holder.tvMonth.setVisibility(View.VISIBLE);
            holder.tvMonth.setText(Util.dateFormat(timesArr[position], "M"));
            if (week.equals("토")) holder.tvMonth.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.skyblue));
            else if (week.equals("일")) holder.tvMonth.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.red));
        }
//        holder.itemView.setOnClickListener(v -> {
//            fragment.onDateClick(timesArr[position], prePosition, position);
//            prePosition = holder.getAdapterPosition();
//        });
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

        TextView tvWeek, tvMonth, tvDay, tvDepday;
        ImageView ivSlash, ivToday;
        View vBack;
        Handler handler;

        public DateViewHolder(@NonNull View v) {
            super(v);
            tvWeek = v.findViewById(R.id.tv_week);
            tvMonth = v.findViewById(R.id.tv_month);
            tvDay = v.findViewById(R.id.tv_day);
            tvDepday = v.findViewById(R.id.tv_depday);
            ivSlash = v.findViewById(R.id.iv_slash);
            ivToday = v.findViewById(R.id.iv_today);
            vBack = v.findViewById(R.id.v_back);
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    handler.sendMessage(handler.obtainMessage(1, getAdapterPosition()));
//                }
//            });
        }

//        public Handler handler() {
//            return new Handler(Looper.getMainLooper()) {
//                @Override
//                public void handleMessage(@NonNull Message msg) {
//                    super.handleMessage(msg);
//                    if (msg.what == 1) {
//                        Log.d(TAG, "handleMessage: " + msg.obj);
//                    }
//                }
//            };
//        }
    }

}