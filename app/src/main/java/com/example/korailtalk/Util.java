package com.example.korailtalk;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Util {

    private Util() {}

    public static String dateFormat(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        return sdf.format(time);
    }

    public static String dateFormat(Timestamp time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.KOREA);
        return sdf.format(time);
    }

    public static int dateFormatInt(Timestamp time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.KOREA);
        return Integer.parseInt(sdf.format(time));
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static void setRecyclerView(Context context, RecyclerView rv, RecyclerView.Adapter<?> adapter, boolean orientation) {
        RecyclerView.LayoutManager lm;
        if (orientation) lm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        else lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv.setAdapter(adapter);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(null);
    }

    public static void changeInvisible(View[] viewArr, int visibility) {
        for (View v : viewArr) {
            if (v.getVisibility() == View.VISIBLE) v.setVisibility(visibility);
            else v.setVisibility(View.VISIBLE);
        }
    }

}
