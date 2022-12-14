package com.example.korailtalk.ticketing;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.RvTicBinding;
import com.example.korailtalk.node.Node;

import java.util.ArrayList;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.NodeViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Node> nodeList;
    private final TicketingFragment fragment;
    boolean search;
    private final String[] sfl = {"가까운역", "최근검색구간", "주요역", "ㄱ", "ㄴ", "ㄷ", "ㅁ", "ㅂ",
            "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅌ", "ㅍ", "ㅎ"};
    int index = 0;
    int sflIndex = 0;
    int itemCount;

    public NodeAdapter(LayoutInflater inflater, ArrayList<Node> nodeList,
                       TicketingFragment fragment, Boolean search, int itemCount) {
        this.inflater = inflater;
        this.nodeList = nodeList;
        this.fragment = fragment;
        this.search = search;
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (search) return new NodeViewHolder(inflater.inflate(R.layout.rv_ticsearch, parent, false));
        else return new NodeViewHolder(inflater.inflate(R.layout.rv_tic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NodeViewHolder holder, int position) {
        if (!search && index != nodeList.size()) {
            if (nodeList.get(index) != null) {
                holder.tvNode1.setText(nodeList.get(index++).nodename);
                if (nodeList.get(index) != null) {
                    holder.tvNode2.setText(nodeList.get(index++).nodename);
                }
            } else {
                holder.tvFl.setVisibility(View.VISIBLE);
                holder.llNodeSelect.setVisibility(View.GONE);
                holder.tvFl.setText(sfl[sflIndex++]);
                index++;
            }
        } else if (search) {
            index = holder.getAdapterPosition() * 2;
            holder.tvNode1.setText(nodeList.get(index).nodename);
            if (nodeList.size() > index + 1) {
                holder.tvNode2.setText(nodeList.get(index + 1).nodename);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (search) {
            if (nodeList.size() % 2 == 1) return nodeList.size() / 2 + 1;
            else return nodeList.size() / 2;
        } else return itemCount;
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
        return v -> {
            TextView tv = (TextView) v;
            if (!tv.getText().toString().equals("")) fragment.citySelect(tv.getText().toString());
            fragment.keyboardDown();
        };
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llNodeSelect;
        TextView tvFl, tvNode1, tvNode2;

        public NodeViewHolder(@NonNull View v) {
            super(v);
            tvFl = v.findViewById(R.id.tv_fl);
            tvNode1 = v.findViewById(R.id.tv_node1);
            tvNode2 = v.findViewById(R.id.tv_node2);
            llNodeSelect = v.findViewById(R.id.ll_nodeselect);
            tvNode1.setOnClickListener(onNodeClick());
            tvNode2.setOnClickListener(onNodeClick());
            v.setOnClickListener(view -> fragment.keyboardDown());
        }

    }

}
