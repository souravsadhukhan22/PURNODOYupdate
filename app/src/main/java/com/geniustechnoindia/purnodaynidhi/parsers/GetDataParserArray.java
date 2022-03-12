package com.geniustechnoindia.purnodaynidhi.parsers;

import android.app.AlertDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.app.AppController;
import com.geniustechnoindia.purnodaynidhi.util.MyCustomProgressDialog;
import com.geniustechnoindia.purnodaynidhi.util.Util;

import org.json.JSONArray;


public class GetDataParserArray {

    AlertDialog dialog;

    private void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public GetDataParserArray(final Context context, String url, final boolean flag, final OnGetResponseListener listner) {
        if (!Util.isConnected(context)) {
            Util.showSnakBar(context, context.getResources().getString(R.string.internectconnectionerror));
            listner.onGetResponse(null);
            return;
        }
        if (flag) {
            dialog = MyCustomProgressDialog.ctor(context);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait...");
            showpDialog();
        }
        final JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    listner.onGetResponse(response);
                } catch (Exception e) {
                    listner.onGetResponse(null);
                    e.printStackTrace();
                }
                if (flag)
                    hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (flag)
                    hidepDialog();
                Util.showSnakBar(context, context.getResources().getString(R.string.networkerror));
                listner.onGetResponse(null);
                VolleyLog.d("Error: " + error.getMessage());
            }
        });
        jsonArrayReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayReq);
    }

    public interface OnGetResponseListener {
        void onGetResponse(JSONArray response);
    }
}
