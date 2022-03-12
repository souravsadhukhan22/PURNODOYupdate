package com.geniustechnoindia.purnodaynidhi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.geniustechnoindia.purnodaynidhi.activities.LoginOptionsActivity;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.util.OpenLinks;

import org.json.JSONArray;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {
// from sullair
    private TextView mTv_splashVersion;
    private LinearLayout mLl_openGTech;
    private LinearLayout mLl_devByGen;
    public static String updated_app_ver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setViewReferences();
        bindEventHandlers();

        mTv_splashVersion.setText("Version " + BuildConfig.VERSION_NAME);

        isUpdateAvail();
        //callWaitMethod(false);

    }

    private void setViewReferences() {
        mTv_splashVersion = findViewById(R.id.splash_version);
        mLl_openGTech = findViewById(R.id.ll_open_genius_technology_links);
    }

    private void bindEventHandlers() {
        mTv_splashVersion.setOnClickListener(this);
        mLl_openGTech.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLl_openGTech) {
            new OpenLinks(this).openGeniusTechnology();
        }
    }

    public void isUpdateAvail() {
        new GetDataParserArray(this, APILinks.IS_UPDATE_AVAILABLE + com.geniustechnoindia.purnodaynidhi.BuildConfig.VERSION_NAME, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject(0);
                        updated_app_ver = jsonObject.getString("VersionName");
                        if (jsonObject.getInt("isAvailable") == 1) {
                            callWaitMethod(true);
                        } else {
                            callWaitMethod(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callWaitMethod(Boolean flag) {
        if (flag) {
            YoYo.with(Techniques.StandUp)
                    .duration(3000)
                    .repeat(0)
                    .playOn(findViewById(R.id.tv_splash_activity_title));

            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, UpdateAppActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, secondsDelayed * 2000);
        } else {
            YoYo.with(Techniques.StandUp)
                    .duration(3000)
                    .repeat(0)
                    .playOn(findViewById(R.id.tv_splash_activity_title));

            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, LoginOptionsActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }, secondsDelayed * 2000);
        }
    }
}
