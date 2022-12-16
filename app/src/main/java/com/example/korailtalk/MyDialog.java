package com.example.korailtalk;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.example.korailtalk.databinding.DialogMainBinding;

public class MyDialog {

    DialogMainBinding b;
    Dialog dialog;
    String title, content;

    public MyDialog(Context context, LayoutInflater inflater, String title, String content) {
        dialog = new Dialog(context);
        b = DialogMainBinding.inflate(inflater);
        dialog.setContentView(b.getRoot());
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 100;
        dialog.getWindow().setAttributes(lp);;
        dialog.getWindow().setDimAmount(0);
        dialog.setCancelable(false);

        this.title = title;
        this.content = content;
        b.tvTitle.setText(title);
        b.tvContent.setText(content);
        b.tvConfirm.setOnClickListener(v -> dialog.dismiss());
    }

    public void show() {
        dialog.show();
    }

}
