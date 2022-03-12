package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv_chainReportDetails, mTv_newRenewBusi;
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Button mBtn_logout;
    private SharedPreferences sharedPreferences_admin;

    private TextView mTv_savingsAccLedger, mTv_memberSearch, mTv_investSearch, mTv_loanAccSearch, mTv_loanApproval, mTv_loanPayReport;
    private TextView mTv_execCollReport, mTv_savingsAccTransReport, mTv_mobileCollReport, mTv_accLedgerReport;
    private TextView mTv_bankBook,mTv_cashSheet,mTv_bankTransReport,mTv_loanPaymentReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
    }

    private void setViewReferences() {
        mTv_chainReportDetails = findViewById(R.id.tv_admin_dash_chain_report);
        mTv_newRenewBusi = findViewById(R.id.tv_admin_dash_new_renew_report);
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mBtn_logout = findViewById(R.id.btn_admin_dashboard_logout);

        mTv_savingsAccLedger = findViewById(R.id.tv_admin_dash_savings_acc_ledger);
        mTv_memberSearch = findViewById(R.id.tv_admin_dash_member_search);
        mTv_investSearch = findViewById(R.id.tv_admin_dash_investment_search);
        mTv_loanAccSearch = findViewById(R.id.tv_admin_dash_loan_acc_search);
        mTv_loanApproval = findViewById(R.id.tv_admin_dash_loan_approval);
        mTv_loanPayReport = findViewById(R.id.tv_admin_dash_loan_payment_report);
        mTv_execCollReport = findViewById(R.id.tv_admin_dash_executive_collection_report);
        mTv_savingsAccTransReport = findViewById(R.id.tv_admin_dash_account_savings_trans_report);
        mTv_mobileCollReport = findViewById(R.id.tv_admin_dash_mobile_collection_report);
        mTv_accLedgerReport = findViewById(R.id.tv_activity_admin_dashboard_account_ledger_report);
        mTv_bankBook=findViewById(R.id.tv_admin_dash_executive_bank_book);
        mTv_cashSheet=findViewById(R.id.tv_admin_dash_executive_cash_sheet);
        mTv_bankTransReport=findViewById(R.id.tv_admin_dash_executive_bank_trans_report);
        mTv_loanPaymentReport=findViewById(R.id.tv_admin_dash_executive_loan_payment_report);
    }

    private void bindEventHandlers() {
        mTv_chainReportDetails.setOnClickListener(this);
        mTv_newRenewBusi.setOnClickListener(this);
        mBtn_logout.setOnClickListener(this);

        mTv_savingsAccLedger.setOnClickListener(this);
        mTv_memberSearch.setOnClickListener(this);
        mTv_investSearch.setOnClickListener(this);
        mTv_loanAccSearch.setOnClickListener(this);
        mTv_loanApproval.setOnClickListener(this);
        mTv_loanPayReport.setOnClickListener(this);
        mTv_execCollReport.setOnClickListener(this);
        mTv_savingsAccTransReport.setOnClickListener(this);
        mTv_mobileCollReport.setOnClickListener(this);
        mTv_accLedgerReport.setOnClickListener(this);
        mTv_bankBook.setOnClickListener(this);
        mTv_cashSheet.setOnClickListener(this);
        mTv_bankTransReport.setOnClickListener(this);
        mTv_loanPaymentReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_chainReportDetails) {
            startActivity(new Intent(AdminDashboardActivity.this, AdminChainReportDetailsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        if (v == mTv_newRenewBusi) {
            startActivity(new Intent(AdminDashboardActivity.this, AdminNewRenewBusinessActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        if (v == mBtn_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
            builder.setMessage("Want to Logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences_admin = getSharedPreferences("ADMINLOGIN", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences_admin.edit();
                            editor.clear();
                            editor.commit();
                            startActivity(new Intent(AdminDashboardActivity.this, AdminLoginActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

        if (v == mTv_savingsAccLedger) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == mTv_memberSearch) {
            startActivity(new Intent(AdminDashboardActivity.this,AdminMemberSearchActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
            //Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == mTv_investSearch) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == mTv_loanAccSearch) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == mTv_loanApproval) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == mTv_loanPayReport) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == mTv_execCollReport) {
            startActivity(new Intent(AdminDashboardActivity.this, AdminExecutiveCollectionReportActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        if (v == mTv_savingsAccTransReport) {
            startActivity(new Intent(AdminDashboardActivity.this,AdminSBAccTransReportActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if (v == mTv_mobileCollReport) {
            startActivity(new Intent(AdminDashboardActivity.this,AdminMobileCollReportActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        if (v == mTv_accLedgerReport) {
            startActivity(new Intent(AdminDashboardActivity.this, AdminAccLedgerReportActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }

        if(v==mTv_bankBook){
            startActivity(new Intent(AdminDashboardActivity.this,AdminBankBookActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }

        if(v==mTv_cashSheet){
            startActivity(new Intent(AdminDashboardActivity.this,AdminCashSheetActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }

        if(v==mTv_bankTransReport){
            startActivity(new Intent(AdminDashboardActivity.this,AdminBankTransReportActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }

        if(v==mTv_loanPaymentReport){
            startActivity(new Intent(AdminDashboardActivity.this,AdminLoanPaymentReportActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Admin Dashboard");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(AdminDashboardActivity.this, LoginOptionsActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
