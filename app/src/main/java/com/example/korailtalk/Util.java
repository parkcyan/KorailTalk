package com.example.korailtalk;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static int roundCharge(double number) {
        return (int) Math.round(number / 100.0) * 100;
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getTimestampFromDouble(double time) {
        BigDecimal bd = BigDecimal.valueOf(time);
        StringBuilder ts = new StringBuilder();
        StringBuilder sb = new StringBuilder(bd.toPlainString());
        // 20221201061200 -> 2022-12-01 06:12:00
        ts.append(sb.substring(0, 4)).append("-").append(sb.substring(4, 6)).append("-").append(sb.substring(6, 8))
                .append(" ").append(sb.substring(8, 10)).append(":").append(sb.substring(10, 12)).append(":")
                .append(sb.substring(12));
        return Timestamp.valueOf(ts.toString());
    }

    public static Timestamp timestampOperator(Timestamp time, int date, int number) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(date, number);
        return new Timestamp(cal.getTime().getTime());
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

    public static int getScrollPosition(View scrollView, View view) {
        int[] svLocation = new int[2];
        int[] viewLocation = new int[2];
        scrollView.getLocationInWindow(svLocation);
        view.getLocationInWindow(viewLocation);
        return viewLocation[1] - svLocation[1] + scrollView.getScrollY();
    }

    public static void changeInvisible(View[] viewArr, int visibility) {
        for (View v : viewArr) {
            if (v.getVisibility() == View.VISIBLE) v.setVisibility(visibility);
            else v.setVisibility(View.VISIBLE);
        }
    }

}
