package com.example.korailtalk.ticketing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.RvTrainBinding;
import com.example.korailtalk.ticketing.LookupActivity;
import com.example.korailtalk.ticketing.data.TrainVO;

import java.util.ArrayList;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {

    private LookupActivity activity;
    private ArrayList<TrainVO> trainList;
    private LayoutInflater inflater;
    private int selectedPosition = -1;

    public TrainAdapter(LookupActivity activity, ArrayList<TrainVO> trainList) {
        this.activity = activity;
        this.trainList = trainList;
        inflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrainViewHolder(inflater.inflate(R.layout.rv_train, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        TrainVO train = trainList.get(position);
        holder.b.tvTrain.setText(train.getTraingradename());
        holder.b.tvTrainno.setText(train.getTrainno());
        holder.b.tvDep.setText(train.getDepplacename());
        holder.b.tvArr.setText(train.getArrplacename());
        holder.b.tvDeptime.setText(train.getDepplandtime());
        holder.b.tvArrtime.setText(train.getArrplandtime());
        holder.b.tvCharge.setText(train.getAdultcharge());
        if (train.getAdultcharge().equals("매진")) {
            holder.b.tvCharge.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_red));
            holder.b.tvCharges.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_red));
            holder.b.tvCharge.setTextColor(ContextCompat.getColor(activity, R.color.red));
            holder.b.tvCharges.setTextColor(ContextCompat.getColor(activity, R.color.red));
        } else holder.b.tvCharge.setText(train.getAdultcharge());
        if (train.getAdultscharge() == null) {
            holder.b.tvCharges.setVisibility(View.INVISIBLE);
            holder.b.tvNotktx.setVisibility(View.VISIBLE);
        } else holder.b.tvCharges.setText(trainList.get(position).getAdultscharge());
        if (selectedPosition == position) {
            holder.itemView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_selected));
            holder.b.tvCharge.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_main_selected));
            holder.b.tvCharges.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_main_selected));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            if (train.getAdultcharge().equals("매진")) {
                holder.b.tvCharge.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_red));
                holder.b.tvCharges.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_red));
            } else {
                holder.b.tvCharge.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_main));
                holder.b.tvCharges.setBackground(ContextCompat.getDrawable(activity, R.drawable.round_background_main));
            }
        }
    }

    @Override
    public int getItemCount() {
        return trainList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class TrainViewHolder extends RecyclerView.ViewHolder {

        RvTrainBinding b;

        public TrainViewHolder(@NonNull View v) {
            super(v);
            b = RvTrainBinding.bind(v);
            b.tvCharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        selectedPosition = getAdapterPosition();
                        activity.trainSelect(trainList.get(getAdapterPosition()), false);
                        notifyDataSetChanged();
                    }
                }
            });
            b.tvCharges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        selectedPosition = getAdapterPosition();
                        activity.trainSelect(trainList.get(getAdapterPosition()), true);
                        notifyDataSetChanged();
                    }
                }
            });

        }
    }
}
