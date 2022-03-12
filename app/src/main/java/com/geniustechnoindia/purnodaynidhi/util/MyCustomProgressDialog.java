package com.geniustechnoindia.purnodaynidhi.util;

import android.app.ProgressDialog;
import android.content.Context;

public class MyCustomProgressDialog extends ProgressDialog {
    public static ProgressDialog ctor(Context context) {
        MyCustomProgressDialog dialog = new MyCustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public MyCustomProgressDialog(Context context) {
        super(context);
    }

    public MyCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
}
