package com.geniustechnoindia.purnodaynidhi.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialogs {

    public Context context;

    public MyDialogs(Context context){
        this.context = context;
    }

    public void myDialog(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
