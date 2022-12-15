package com.example.korailtalk.ticketing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder>{

    private final LayoutInflater inflater;
    private final TicketingFragment fragment;
    private final String[] time = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

    public TimeAdapter(TicketingFragment fragment) {
        this.fragment = fragment;
        inflater = fragment.getLayoutInflater();
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeViewHolder(inflater.inflate(R.layout.rv_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        holder.tvTime.setText(time[position]);
    }

    @Override
    public int getItemCount() {
        return time.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;

        public TimeViewHolder(@NonNull View v) {
            super(v);
            tvTime = v.findViewById(R.id.tv_time);
        }
    }
}
