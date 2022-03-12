package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class MemberPolicyActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_myPolicy,mTv_createNewPolicy,mTv_investmentCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_policy);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mTv_myPolicy = findViewById(R.id.tv_activity_member_policy_my_policy);
        mTv_createNewPolicy = findViewById(R.id.tv_activity_member_policy_start_new_policy);
        mTv_investmentCalc = findViewById(R.id.tv_activity_member_policy_investment_calc);
    }

    private void bindEventHandlers() {
        mTv_myPolicy.setOnClickListener(this);
        mTv_createNewPolicy.setOnClickListener(this);
        mTv_investmentCalc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mTv_myPolicy){
            startActivity(new Intent(MemberPolicyActivity.this,MemberMyPolicyActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if(v==mTv_createNewPolicy){
            startActivity(new Intent(MemberPolicyActivity.this,MemberCreateNewPolicy.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if(v==mTv_investmentCalc){
            startActivity(new Intent(MemberPolicyActivity.this,MemberInvestmentCalcActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Member Policy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberPolicyActivity.this, MemberDashboardActivity.class);
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
        super.onBackPressed();
        Intent intent = new Intent(MemberPolicyActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}