package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterSavingsAccountStatement;
import com.geniustechnoindia.purnodaynidhi.bean.LoanData;
import com.geniustechnoindia.purnodaynidhi.model.SavingsStatementSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdminSBAccTransReportActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // RecyclerView
    private RecyclerView mRv_savingsAccountStatement;
    private AdapterSavingsAccountStatement mAdapterSavingsAccountStatement;
    private ArrayList<SavingsStatementSetGet> mArrayList;
    private SavingsStatementSetGet savingsStatementSetGet;
    private EditText mEt_sbAcc;
    private Button mBtn_submit;
    private String mStr_sbAccCode="";
    private TextView mTv_sbBal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sbacc_trans_report);

        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Savings Statement");

        //getSavingsAccountStatement(SelectedAccount.savingsCode);           // TODO Savings account number

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_savingsAccountStatement.setLayoutManager(linearLayoutManager);

    }

    /**
     * Get savings account statement
     */
    private void getSavingsAccountStatement(String accountNumber) {
        int mInt_depo=0,mInt_with=0;
        Connection cn = new SqlManager().getSQLConnection();
        LoanData loanData = new LoanData();
        mArrayList = new ArrayList<>();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetSavingsAccountMiniStatement(?)}");
                smt.setString("@SBAccount", accountNumber);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                int i=1;
                while (rs.next()) {
                    savingsStatementSetGet = new SavingsStatementSetGet();
                    savingsStatementSetGet.setSlNo(String.valueOf(i));
                    i++;
                    //rs.getString("VrNo");
                    //rs.getString("MemberCode");
                    savingsStatementSetGet.settDate(rs.getString("TDate"));
                    //savingsStatementSetGet.setParticular(rs.getString("Particular"));
                    savingsStatementSetGet.setPaymode(rs.getString("PayMode"));
                    savingsStatementSetGet.setDepositAmount(rs.getString("DepositAmount"));
                    savingsStatementSetGet.setWithdrawlAmount(rs.getString("WithdrawlAmount"));
                    //savingsStatementSetGet.setBalance(rs.getString("Balance"));
                    mInt_depo=mInt_depo+rs.getInt("DepositAmount");
                    mInt_with=mInt_with+rs.getInt("WithdrawlAmount");
                    mArrayList.add(savingsStatementSetGet);
                    loanData.setErrorCode(0);

                }
                mTv_sbBal.setText("Current SB Balance : "+String.valueOf(mInt_depo-mInt_with));
                mAdapterSavingsAccountStatement = new AdapterSavingsAccountStatement(AdminSBAccTransReportActivity.this, mArrayList);
                mRv_savingsAccountStatement.setAdapter(mAdapterSavingsAccountStatement);
            } else {
                loanData.setErrorCode(2);
                loanData.setErrorString("Network related problem.");
            }
        } catch (Exception ex) {
            loanData.setErrorCode(1);
            loanData.setErrorString(ex.getMessage().toString());
        }
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mRv_savingsAccountStatement = findViewById(R.id.rv_activity_admin_sb_account_statement);
        mEt_sbAcc=findViewById(R.id.et_activity_admin_sbacc_trans_report_sb_code);
        mBtn_submit=findViewById(R.id.btn_activity_admin_sbacc_trans_submit);

        mTv_sbBal=findViewById(R.id.tv_admin_sb_acc_trans_eport_sb_bal);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtn_submit){
            if(mEt_sbAcc.getText().toString().length()>0){
                mStr_sbAccCode=mEt_sbAcc.getText().toString().trim();
                getSavingsAccountStatement(mStr_sbAccCode);
            }else{
                mEt_sbAcc.setError("Enter SB Account Code");
                mEt_sbAcc.requestFocus();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(AdminSBAccTransReportActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminSBAccTransReportActivity.this, AdminDashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
