package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.AppIntentData;

public class PlanDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Button mBtn_dailyDeposit, mBtn_fixedDeposit, mBtn_mis, mBtn_recurringDeposit;
    private String str_context = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();

        //Intent intent=getIntent();
        //str_context=intent.getExtras().getString("FLAGCONTEXT");
        str_context = AppIntentData.select_context;
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mBtn_dailyDeposit = findViewById(R.id.btn_activity_plan_details_daily_deposit);
        mBtn_fixedDeposit = findViewById(R.id.btn_activity_plan_details_fixed_deposit);
        mBtn_mis = findViewById(R.id.btn_activity_plan_details_mis);
        mBtn_recurringDeposit = findViewById(R.id.btn_activity_plan_details_recurring);
    }

    private void bindEventHandlers() {
        mBtn_dailyDeposit.setOnClickListener(this);
        mBtn_fixedDeposit.setOnClickListener(this);
        mBtn_mis.setOnClickListener(this);
        mBtn_recurringDeposit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_dailyDeposit) {
            Intent intent = new Intent(PlanDetailsActivity.this, InvestmentPlanDetailsActivity.class);
            intent.putExtra("PLANCODE", "DRD");
            startActivity(intent);
        }

        if (v == mBtn_fixedDeposit) {
            Intent intent = new Intent(PlanDetailsActivity.this, InvestmentPlanDetailsActivity.class);
            intent.putExtra("PLANCODE", "FD");
            startActivity(intent);
        }
        if (v == mBtn_mis) {
            Intent intent = new Intent(PlanDetailsActivity.this, InvestmentPlanDetailsActivity.class);
            intent.putExtra("PLANCODE", "MIS");
            startActivity(intent);
        }
        if (v == mBtn_recurringDeposit) {
            Intent intent = new Intent(PlanDetailsActivity.this, InvestmentPlanDetailsActivity.class);
            intent.putExtra("PLANCODE", "RD");
            startActivity(intent);
        }
    }

    private void setUpTollBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Investment Plan Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (str_context.equals("ARRANGER")) {
                    startActivity(new Intent(PlanDetailsActivity.this, MainActivity.class));
                }
                if (str_context.equals("MEMBER")) {
                    startActivity(new Intent(PlanDetailsActivity.this, MemberDashboardActivity.class));
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
            startActivity(new Intent(PlanDetailsActivity.this, MainActivity.class));
        }
        if (str_context.equals("MEMBER")) {
            startActivity(new Intent(PlanDetailsActivity.this, MemberDashboardActivity.class));
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
