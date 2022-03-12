package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class MemberRechargeOption extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView toolbarTitle;

    private LinearLayout mLl_mobileRecharge;
    private LinearLayout mLl_dthRecharge;
    private LinearLayout mLl_datacardRecharge;
    private LinearLayout mLl_electricityRecharge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_recharge_option);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
    }


    @Override
    public void onClick(View v) {
        if(v==mLl_mobileRecharge){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberRechargeOption.this,MemberMobileRechargeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        }
        if(v==mLl_dthRecharge){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberRechargeOption.this,MemberDTHRechargeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        }
        if(v==mLl_datacardRecharge){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberRechargeOption.this,MemberDataCardRechargeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        if(v==mLl_electricityRecharge){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberRechargeOption.this,MemberElectricityRechargeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        }
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        toolbarTitle=findViewById(R.id.toolbar_title);

        mLl_mobileRecharge=findViewById(R.id.ll_activity_member_recharge_option_activity_mobile_recharge);
        mLl_dthRecharge=findViewById(R.id.ll_activity_member_recharge_option_activity_dth);
        mLl_datacardRecharge=findViewById(R.id.ll_activity_member_recharge_option_activity_datacard_recharge);
        mLl_electricityRecharge=findViewById(R.id.ll_activity_member_recharge_option_activity_electricity);
    }

    private void bindEventHandlers(){
        mLl_mobileRecharge.setOnClickListener(this);
        mLl_dthRecharge.setOnClickListener(this);
        mLl_datacardRecharge.setOnClickListener(this);
        mLl_electricityRecharge.setOnClickListener(this);
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarTitle.setText("Recharge Option");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberRechargeOption.this, MemberDashboardActivity.class);
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
        Intent intent = new Intent(MemberRechargeOption.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
