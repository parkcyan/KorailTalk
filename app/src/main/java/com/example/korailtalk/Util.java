package com.example.korailtalk;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private Util() {}

    public static String dateFormat(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static void setRecyclerView(Context context, RecyclerView rv, RecyclerView.Adapter<?> adapter, boolean orientation) {
        RecyclerView.LayoutManager lm;
        if (orientation) lm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv.setAdapter(adapter);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(null);
    }

    public static void changeInvisible(View[] viewArr, int visibility) {
        for (View v : viewArr) {
            if (v.getVisibility() == View.VISIBLE) v.setVisibility(visibility);
            else v.setVisibility(View.VISIBLE);
        }
    }

}
