package com.example.korailtalk.ticketing.data;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.korailtalk.R;

public class QtySet {
    private int qty;
    private TextView qtyTv;
    private ImageButton qtyMin, qtyPls;

    public QtySet(int qty, TextView qtyTv, ImageButton qtyMin, ImageButton qtyPls) {
        this.qty = qty;
        this.qtyTv = qtyTv;
        this.qtyMin = qtyMin;
        this.qtyPls = qtyPls;
    }

    public void setMinusEnable() {
        qtyMin.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
    }

    public void setMinusDisable() {
        qtyMin.setImageResource(R.drawable.ic_baseline_remove_circle_24);
    }

    public void setPlusEnable() {
        qtyPls.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
    }

    public void setPlusDisable() {
        qtyPls.setImageResource(R.drawable.ic_baseline_add_circle_24);
    }

    public void refreshTv() {
        qtyTv.setText(String.valueOf(qty));
    }

    public void operateQty(boolean plus) {
        if (plus) qty++;
        else if (qty > 0) qty--;
        refreshTv();
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public TextView getQtyTv() {
        return qtyTv;
    }

    public void setQtyTv(TextView qtyTv) {
        this.qtyTv = qtyTv;
    }

    public ImageButton getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(ImageButton qtyMin) {
        this.qtyMin = qtyMin;
    }

    public ImageButton getQtyPls() {
        return qtyPls;
    }

    public void setQtyPls(ImageButton qtyPls) {
        this.qtyPls = qtyPls;
    }

}