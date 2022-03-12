package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class ArrPolicyInvestmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private LinearLayout mLl_policyRenew, mLl_startNewPolicy, mLl_searchPolicy,mLl_investmentCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_policy_investment);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mLl_policyRenew = findViewById(R.id.ll_activity_arr_policy_investment_policy_renewal);
        mLl_startNewPolicy = findViewById(R.id.ll_activity_arr_policy_investment_start_new_policy);
        mLl_searchPolicy = findViewById(R.id.ll_activity_arr_policy_investment_search_policy);
        mLl_investmentCalc = findViewById(R.id.ll_activity_arr_policy_investment_investment_calculator);
    }

    private void bindEventHandlers() {
        mLl_policyRenew.setOnClickListener(this);
        mLl_startNewPolicy.setOnClickListener(this);
        mLl_searchPolicy.setOnClickListener(this);
        mLl_investmentCalc.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mLl_policyRenew) {
            Intent intent = new Intent(getApplicationContext(), com.geniustechnoindia.purnodaynidhi.RenewalCollectionActivity.class);
            //Intent intent = new Intent(ArrPolicyInvestmentActivity.this, ArrRenewalCollectionManualActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_startNewPolicy) {
            Intent intent = new Intent(ArrPolicyInvestmentActivity.this, ArrStartNewPolicyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mLl_searchPolicy){
            Intent intent = new Intent(ArrPolicyInvestmentActivity.this, ArrSearchPolicyByNameActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mLl_investmentCalc){
            Intent intent = new Intent(ArrPolicyInvestmentActivity.this, ArrInvestmentCalcActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }


    private void setUpTollBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Policy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrPolicyInvestmentActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        Intent intent = new Intent(ArrPolicyInvestmentActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}