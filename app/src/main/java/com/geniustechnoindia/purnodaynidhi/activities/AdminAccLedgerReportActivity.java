package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;


public class AdminAccLedgerReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_savingsAcc, mTv_bankReport, mTv_cashAcc, mTv_recurrDeposit, mTv_loanType, mTv_depositType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_acc_ledger_report);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_savingsAcc = findViewById(R.id.tv_activity_admin_acc_ledger_savings_acc);
        mTv_bankReport = findViewById(R.id.tv_activity_admin_acc_ledger_bank_report);
        mTv_cashAcc = findViewById(R.id.tv_activity_admin_acc_ledger_cash_acc);
        mTv_recurrDeposit = findViewById(R.id.tv_activity_admin_acc_ledger_recurring_deposit);
        mTv_loanType = findViewById(R.id.tv_activity_admin_acc_ledger_loan_types);
        mTv_depositType = findViewById(R.id.tv_activity_admin_acc_ledger_deposit_types);
    }

    private void bindEventHandlers() {
        mTv_savingsAcc.setOnClickListener(this);
        mTv_bankReport.setOnClickListener(this);
        mTv_cashAcc.setOnClickListener(this);
        mTv_recurrDeposit.setOnClickListener(this);
        mTv_loanType.setOnClickListener(this);
        mTv_depositType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_savingsAcc) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == mTv_bankReport) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == mTv_cashAcc) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == mTv_recurrDeposit) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == mTv_loanType) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == mTv_depositType) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Account Ledger");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AdminAccLedgerReportActivity.this, AdminDashboardActivity.class);
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
        startActivity(new Intent(AdminAccLedgerReportActivity.this, AdminDashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
