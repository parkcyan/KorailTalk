package com.example.korailtalk.ticketing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        holder.b.tvTrain.setText(trainList.get(position).getTraingradename());
        holder.b.tvTrainno.setText(trainList.get(position).getTrainno());
        holder.b.tvDep.setText(trainList.get(position).getDepplacename());
        holder.b.tvArr.setText(trainList.get(position).getArrplacename());
        holder.b.tvDeptime.setText(trainList.get(position).getDepplandtime());
        holder.b.tvArrtime.setText(trainList.get(position).getArrplandtime());
        holder.b.tvCharge.setText(String.valueOf(trainList.get(position).getAdultcharge()));
        holder.b.tvCharges.setText(String.valueOf(trainList.get(position).getAdultcharge()));
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
        }
    }
}
