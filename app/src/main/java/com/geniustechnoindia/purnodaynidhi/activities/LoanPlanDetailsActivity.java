package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.AppIntentData;

public class LoanPlanDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private String str_context = "";

    private Button mBtn_indvLoan, mBtn_grLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_plan_details);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();

        str_context = AppIntentData.select_context_loan_plan;
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mBtn_indvLoan = findViewById(R.id.btn_activity_loan_plan_details_rd_plan);
        mBtn_grLoan = findViewById(R.id.btn_activity_loan_plan_details_fd_plan);
    }

    private void bindEventHandlers() {
        mBtn_indvLoan.setOnClickListener(this);
        mBtn_grLoan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_indvLoan) {
            startActivity(new Intent(LoanPlanDetailsActivity.this,LoanPlanDetailsIndivLoanActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if (v == mBtn_grLoan) {
            startActivity(new Intent(LoanPlanDetailsActivity.this,LoanPlanDetailsGroupLoanActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
        mTv_toolbarTitle.setText("Loan Plan Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (str_context.equals("ARRANGER")) {
                    startActivity(new Intent(LoanPlanDetailsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
                }
                if (str_context.equals("MEMBER")) {
                    startActivity(new Intent(LoanPlanDetailsActivity.this, MemberDashboardActivity.class));
                }
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
        if (str_context.equals("ARRANGER")) {
            startActivity(new Intent(LoanPlanDetailsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
        }
        if (str_context.equals("MEMBER")) {
            startActivity(new Intent(LoanPlanDetailsActivity.this, MemberDashboardActivity.class));
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
