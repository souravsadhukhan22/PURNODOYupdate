package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class MemberLoanActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_myLoan,mTv_applyLoan,mTv_loanCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_loan);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mTv_myLoan = findViewById(R.id.tv_activity_member_loan_my_loan);
        mTv_applyLoan = findViewById(R.id.tv_activity_member_loan_start_new_loan);
        mTv_loanCalc = findViewById(R.id.tv_activity_member_loan_investment_calc);
    }

    private void bindEventHandlers() {
        mTv_myLoan.setOnClickListener(this);
        mTv_applyLoan.setOnClickListener(this);
        mTv_loanCalc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mTv_applyLoan){
            startActivity(new Intent(MemberLoanActivity.this,MemberApplyForLoanActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if(v==mTv_myLoan){
            startActivity(new Intent(MemberLoanActivity.this,MemberMyLoanActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if(v==mTv_loanCalc){
            startActivity(new Intent(MemberLoanActivity.this,LoanEMICalculatorActivity.class));
            //startActivity(new Intent(MemberLoanActivity.this, LoanEmiCalculatorActivity.class));
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
        mTv_toolbarTitle.setText("Member Loan");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberLoanActivity.this, MemberDashboardActivity.class);
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
        Intent intent = new Intent(MemberLoanActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}