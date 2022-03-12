package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class MemberApplyForLoanActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private TextView mTv_loanProperty,mTv_loanDeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_apply_for_loan);


        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
    }

    private void setViewReferences(){
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mTv_loanProperty = findViewById(R.id.tv_activity_member_apply_for_loan_property);
        mTv_loanDeposit = findViewById(R.id.tv_activity_member_apply_for_loan_deposit);
    }

    private void bindEventHandlers(){
        mTv_loanProperty.setOnClickListener(this);
        mTv_loanDeposit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==mTv_loanProperty){
            startActivity(new Intent(MemberApplyForLoanActivity.this,MemberLoanAganistPropertyActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if(v==mTv_loanDeposit){
            startActivity(new Intent(MemberApplyForLoanActivity.this,MemberLoanAganistDepositActivity.class));
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
        mTv_toolbarTitle.setText("Apply Loan");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberApplyForLoanActivity.this, MemberLoanActivity.class);
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
        Intent intent = new Intent(MemberApplyForLoanActivity.this, MemberLoanActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}