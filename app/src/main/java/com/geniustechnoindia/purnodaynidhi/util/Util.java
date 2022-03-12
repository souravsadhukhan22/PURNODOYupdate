package com.geniustechnoindia.purnodaynidhi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static NetworkInfo networkInfo;
    private static int countryCode;
    static Context c = null;

    /**
     * Is there internet connection
     */

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // test for connection for WIFI
        if (networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected()) {
            return true;
        }

        networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // test for connection for Mobile
        return networkInfo != null
                && networkInfo.isAvailable()
                && networkInfo.isConnected();

    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public JSONArray getJsonArray(String responce) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responce);
        } catch (Exception e) {

        }
        return jsonArray;
    }

    public JSONObject getJsonObject(String responce) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responce);
        } catch (Exception e) {

        }
        return jsonObject;
    }





    public static final boolean isValidPhoneNumber(CharSequence target) {
        return target.length() != 10 || !android.util.Patterns.PHONE.matcher(target).matches();
    }

    public static String changeAnyDateFormat(String reqdate, String dateformat, String reqformat) {
        //String	date1=reqdate;

        if (reqdate.equalsIgnoreCase("") || dateformat.equalsIgnoreCase("") || reqformat.equalsIgnoreCase(""))
            return "";
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        String changedate = "";
        Date dt = null;
        if (!reqdate.equals("") && !reqdate.equals("null")) {
            try {
                dt = format.parse(reqdate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //SimpleDateFormat your_format = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat your_format = new SimpleDateFormat(reqformat);
            changedate = your_format.format(dt);
        }
        return changedate;
    }

    public static void showSnakBar(final Context context, String message) {
        if (c!=context) {
            Snackbar.with(context) // context
                    .text(message) // text to display
                    .actionLabel("Try Again") // action button label
                    .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                    .animation(true)
                    .actionListener(new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            Activity a = (Activity) context;
                            Intent i = a.getIntent();
                            a.overridePendingTransition(0, 0);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            a.finish();
                            a.overridePendingTransition(0, 0);
                            a.startActivity(i);

                        }
                    })
                    .show((Activity) context);
        }
        c=context;

    }
    public static void showSnakBar(final Context context, String message, final View view) {
        //if (c!=context) {

        Snackbar.with(context) // context
                .text(message) // text to display
                .actionLabel("Try Again") // action button label
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .animation(true)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                        view.performClick();

                    }
                })
                .show((Activity) context);
        //}
        // c=context;
    }
}
