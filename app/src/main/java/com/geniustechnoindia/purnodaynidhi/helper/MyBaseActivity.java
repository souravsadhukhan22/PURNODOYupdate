package com.geniustechnoindia.purnodaynidhi.helper;

import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;


public class MyBaseActivity extends AppCompatActivity {
    public static boolean inactivityTimeOut = false;
    public static final long DISCONNECT_TIMEOUT = 600000; // 5 min = 5 * 60 * 1000 ms = 300000

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            // TODO Logout user
            /*SharedPreferences sharedPreferences = getSharedPreferences("MemberLogin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("REMEMBER", "FALSE");
            editor.putString("MEMBERCODE", "");
            editor.putString("MEMBERPASSWORD", "");
            editor.commit();

            startActivity(new Intent(MyBaseActivity.this, MemberLoginActivity.class));
            finish();*/
            //autoLogout();
            inactivityTimeOut = true;
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
        inactivityTimeOut = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
        inactivityTimeOut = false;
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onStop() {
        super.onStop();
        *//*stopDisconnectTimer();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();*//*
        //TODO If the app is in background onStop will be called

    }*/


}
