package com.example.korailtalk.ticketing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.node.NodeRoom;

import java.util.ArrayList;

public class NodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private final TicketingFragment fragment;
    private final ArrayList<NodeVO> nodeList;

    public NodeAdapter(TicketingFragment fragment) {
        this.fragment = fragment;
        inflater = fragment.getLayoutInflater();
        nodeList = NodeRoom.nodeListForRv;
    }

    public NodeAdapter(TicketingFragment fragment, ArrayList<NodeVO> nodeList) {
        this.fragment = fragment;
        inflater = fragment.getLayoutInflater();
        this.nodeList = nodeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NodeViewHolder(inflater.inflate(R.layout.rv_node, parent, false));
        } else if (viewType == 1) {
            return new FlViewHolder(inflater.inflate(R.layout.rv_fl, parent, false));
        } else return new NodeViewHolder(inflater.inflate(R.layout.rv_nodesearch, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NodeViewHolder) {
            ((NodeViewHolder) holder).tvNode1.setText(nodeList.get(position).getNode1());
            ((NodeViewHolder) holder).tvNode2.setText(nodeList.get(position).getNode2());
        } else if (holder instanceof FlViewHolder) {
            ((TextView) holder.itemView).setText(nodeList.get(position).getNode1());
        }
    }

    @Override
    public int getItemCount() {
        return nodeList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return nodeList.get(position).getViewType();
    }

    private View.OnClickListener onNodeClick() {
        return v -> {
            TextView tv = (TextView) v;
            if (!tv.getText().toString().equals("")) fragment.citySelect(tv.getText().toString());
            fragment.keyboardDown();
        };
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {

        TextView tvNode1, tvNode2;

        public NodeViewHolder(@NonNull View v) {
            super(v);
            tvNode1 = v.findViewById(R.id.tv_node1);
            tvNode2 = v.findViewById(R.id.tv_node2);
            tvNode1.setOnClickListener(onNodeClick());
            tvNode2.setOnClickListener(onNodeClick());
            v.setOnClickListener(view -> fragment.keyboardDown());
        }

    }

    public static class FlViewHolder extends RecyclerView.ViewHolder {

        public FlViewHolder(@NonNull View v) {
            super(v);
        }
    }

}
