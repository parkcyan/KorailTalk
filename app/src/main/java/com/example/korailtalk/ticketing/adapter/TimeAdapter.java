package com.example.korailtalk.ticketing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.RvTimeBinding;
import com.example.korailtalk.ticketing.TicketingFragment;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder>{

    private final LayoutInflater inflater;
    private final TicketingFragment fragment;
    private final String[] time = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private int selectedPosition;

    public TimeAdapter(TicketingFragment fragment, int hour) {
        this.fragment = fragment;
        inflater = fragment.getLayoutInflater();
        selectedPosition = hour;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeViewHolder(inflater.inflate(R.layout.rv_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        holder.b.tvTime.setText(time[position]);
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(fragment.getContext(), R.color.skyblue));
            holder.b.tvTime.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.white));
            holder.b.tvHour.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(fragment.getContext(), R.color.lightgray));
            holder.b.tvTime.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.black));
            holder.b.tvHour.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.black));
        }

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

    public class TimeViewHolder extends RecyclerView.ViewHolder {

        RvTimeBinding b;

        public TimeViewHolder(@NonNull View v) {
            super(v);
            b = RvTimeBinding.bind(v);
            v.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION && fragment.onTimeClick(time[getAdapterPosition()])) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
