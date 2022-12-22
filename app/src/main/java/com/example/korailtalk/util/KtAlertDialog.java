package com.example.korailtalk.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.korailtalk.R;
import com.example.korailtalk.databinding.DialogAlertBinding;

public class KtAlertDialog {

    private DialogAlertBinding b;
    private Dialog dialog;
    private Context context;

    public KtAlertDialog(Context context, LayoutInflater inflater, String title, String content,
                         OnAlertDialogClickListener callback) {
        this.context = context;
        dialog = new Dialog(context);
        b = DialogAlertBinding.inflate(inflater);
        dialog.setContentView(b.getRoot());
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 100;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0);
        dialog.setCancelable(false);

        b.tvTitle.setText(title);
        b.tvContent.setText(content);
        b.tvNo.setOnClickListener(v -> callback.setOnClickNo(this));
        b.tvYes.setOnClickListener(v -> callback.setOnClickYes(this));
    }

    public KtAlertDialog setPurple() {
        b.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.purple));
        b.tvNo.setBackgroundColor(ContextCompat.getColor(context, R.color.purple3));
        b.tvYes.setBackgroundColor(ContextCompat.getColor(context, R.color.purple3));
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnAlertDialogClickListener {
        void setOnClickYes(KtAlertDialog dialog);
        void setOnClickNo(KtAlertDialog dialog);
    }

}
