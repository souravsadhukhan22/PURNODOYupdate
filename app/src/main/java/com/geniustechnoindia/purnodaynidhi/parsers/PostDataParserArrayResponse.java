package com.geniustechnoindia.purnodaynidhi.parsers;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.app.AppController;
import com.geniustechnoindia.purnodaynidhi.util.MyCustomProgressDialog;
import com.geniustechnoindia.purnodaynidhi.util.Util;

import org.json.JSONArray;

import java.util.Map;


/**
 * When the response is in array
 * */

public class PostDataParserArrayResponse {
    ProgressDialog dialog;

    private void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    /** If request is array **/
    public PostDataParserArrayResponse(final Context context, String url, final Map<String, String> params, final boolean flag, final OnGetResponseListner listner) {
        if (!Util.isConnected(context)) {
            Util.showSnakBar(context,context.getResources().getString(R.string.internectconnectionerror));
            //TastyToast.makeText(context, "No internet connections.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            listner.onGetResponse(null);
            return;
        }
        if (flag) {
            dialog = MyCustomProgressDialog.ctor(context);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait...");
            showpDialog();
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Util util = new Util();
                            JSONArray jobj = util.getJsonArray(response);
                            listner.onGetResponse(jobj);
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
                Util.showSnakBar(context,context.getResources().getString(R.string.networkerror));
                listner.onGetResponse(null);
                VolleyLog.d("Error: " + error.getMessage());
                //TastyToast.makeText(context, "Network error.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (AppData.sToken != null) {
                    headers.put("token", AppData.sToken);
                }
                return headers;
            }*/
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public interface OnGetResponseListner {
        void onGetResponse(JSONArray response);
    }

}
