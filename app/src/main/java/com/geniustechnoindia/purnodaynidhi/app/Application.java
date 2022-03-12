package com.geniustechnoindia.purnodaynidhi.app;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.geniustechnoindia.purnodaynidhi.payment.Environment;

public class Application extends android.app.Application {
    public static Context con;
    public static Boolean alertStat = false;
    public Environment environment;

    public static Boolean getAlertStatus() {
        return alertStat;
    }

    public static void setAlertStatus(Boolean alertStatus) {
        alertStat = alertStatus;
    }


    public static final String TAG = Application.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    // public DbHelper myDbHelper;

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        environment = Environment.PRODUCTION;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getCurrentActivity() {
        return con;
    }

    public static void setCurrentActivity(Context context) {
        con = context;
    }
}
