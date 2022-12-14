package com.example.korailtalk.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.DialogMainBinding;

public class KtDialog {

    private DialogMainBinding b;
    private Dialog dialog;
    private Context context;

    public KtDialog(Context context, LayoutInflater inflater, String title, String content) {
        this.context = context;
        dialog = new Dialog(context);
        b = DialogMainBinding.inflate(inflater);
        dialog.setContentView(b.getRoot());
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 100;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0);
        dialog.setCancelable(false);

        b.tvTitle.setText(title);
        b.tvContent.setText(content);
        b.tvConfirm.setOnClickListener(v -> dialog.dismiss());
    }

    public KtDialog setPurple() {
        b.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.purple));
        b.tvConfirm.setBackgroundColor(ContextCompat.getColor(context, R.color.purple3));
        return this;
    }

    public void show() {
        dialog.show();
    }

}
