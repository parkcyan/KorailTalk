package com.example.korailtalk.ticketing;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.RvTicBinding;
import com.example.korailtalk.node.Node;

import java.util.ArrayList;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.NodeViewHolder> {

    LayoutInflater inflater;
    ArrayList<Node> nodeArr;
    Context context;
    TicketingFragment fragment;

    public NodeAdapter(LayoutInflater inflater, ArrayList<Node> nodeArr, Context context, TicketingFragment fragment) {
        this.inflater = inflater;
        this.nodeArr = nodeArr;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NodeViewHolder(inflater.inflate(R.layout.rv_tic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NodeViewHolder holder, int position) {
        int index = holder.getAdapterPosition() * 2;
        holder.tv_tic_nodeleft.setText(nodeArr.get(index).nodename);
        holder.tv_tic_nodeleft.setOnClickListener(onNodeClick());
        if (nodeArr.size() > index + 1) {
            holder.tv_tic_noderight.setText(nodeArr.get(index + 1).nodename);
            holder.tv_tic_noderight.setOnClickListener(onNodeClick());
        }
    }

    @Override
    public int getItemCount() {
        if (nodeArr.size() % 2 == 1) return nodeArr.size() / 2 + 1;
        else return nodeArr.size() / 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private View.OnClickListener onNodeClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                fragment.citySelect(tv.getText().toString());
            }
        };
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tic_nodeleft, tv_tic_noderight;

        public NodeViewHolder(@NonNull View v) {
            super(v);
            tv_tic_nodeleft = v.findViewById(R.id.tv_tic_nodeleft);
            tv_tic_noderight = v.findViewById(R.id.tv_tic_noderight);
        }
    }
}
