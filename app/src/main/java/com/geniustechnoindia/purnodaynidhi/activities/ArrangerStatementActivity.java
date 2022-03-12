package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class ArrangerStatementActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView toolbarTitle;

    // views
    private LinearLayout mLl_savingsAccountDetails;
    private LinearLayout mLl_loanDetails;
    private LinearLayout mLl_policyDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_statements);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

    }

    private void setViewReferences(){
        // toolbar title
        mToolbar = findViewById(R.id.custom_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mLl_savingsAccountDetails = findViewById(R.id.ll_activity_arranger_statements_savings_account_details);
        mLl_loanDetails = findViewById(R.id.ll_activity_arranger_statements_savings_loan_details);
        mLl_policyDetails = findViewById(R.id.ll_activity_arranger_statements_policy_account_details);
    }

    private void bindEventHandlers(){
        mLl_savingsAccountDetails.setOnClickListener(this);
        mLl_loanDetails.setOnClickListener(this);
        mLl_policyDetails.setOnClickListener(this);
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarTitle.setText("View Statements");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrangerStatementActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mLl_savingsAccountDetails){
            startActivity(new Intent(ArrangerStatementActivity.this, AgentSavingsStatementActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if(v == mLl_loanDetails){
            startActivity(new Intent(ArrangerStatementActivity.this, ArrangerLoanStatementAndPrint.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if(v == mLl_policyDetails){
            startActivity(new Intent(ArrangerStatementActivity.this, ArrangerPolicyRenewViewActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(ArrangerStatementActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
