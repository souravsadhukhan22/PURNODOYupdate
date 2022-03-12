package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

public class ArrProfileDetailsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private TextView mTv_arrCode,mTv_name,mTv_doj,mTv_dob,mTv_father,mTv_addr,mTv_email,mTv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_profile_details);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();
        getArrProfileDetails(APILinks.GET_ARR_PROFILE_DETAILS+ GlobalStore.GlobalValue.getUserName());
    }

    private void setViewReferences(){
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mTv_arrCode=findViewById(R.id.tv_activity_arr_profile_details_arr_code);
        mTv_name=findViewById(R.id.tv_activity_arr_profile_details_name);
        mTv_doj=findViewById(R.id.tv_activity_arr_profile_details_doj);
        mTv_dob=findViewById(R.id.tv_activity_arr_profile_details_dob);
        mTv_father=findViewById(R.id.tv_activity_arr_profile_details_father);
        mTv_addr=findViewById(R.id.tv_activity_arr_profile_details_addr);
        mTv_email=findViewById(R.id.tv_activity_arr_profile_details_email);
        mTv_phone=findViewById(R.id.tv_activity_arr_profile_details_phone);

    }

    private void bindEventHandlers(){}

    public void getArrProfileDetails(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        JSONObject jsonObject=response.getJSONObject(0);
                        mTv_arrCode.setText(jsonObject.getString("ArrangerCode"));
                        mTv_name.setText(jsonObject.getString("ArrangerName"));
                        mTv_doj.setText(jsonObject.getString("DateOfJoin"));
                        mTv_dob.setText(jsonObject.getString("DOB"));
                        mTv_father.setText(jsonObject.getString("Father"));
                        mTv_addr.setText(jsonObject.getString("Address"));
                        mTv_email.setText(jsonObject.getString("Email"));
                        mTv_phone.setText(jsonObject.getString("Phone"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }


    private void setUpTollBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Profile");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrProfileDetailsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(ArrProfileDetailsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}