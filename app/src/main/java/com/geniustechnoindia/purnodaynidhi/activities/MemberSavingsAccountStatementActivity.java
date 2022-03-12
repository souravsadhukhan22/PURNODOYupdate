package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterSavingsAccountStatement;
import com.geniustechnoindia.purnodaynidhi.bean.LoanData;
import com.geniustechnoindia.purnodaynidhi.model.SavingsStatementSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.SelectedAccount;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

public class MemberSavingsAccountStatementActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // RecyclerView
    private RecyclerView mRv_savingsAccountStatement;
    private AdapterSavingsAccountStatement mAdapterSavingsAccountStatement;
    private ArrayList<SavingsStatementSetGet> mArrayList;
    private SavingsStatementSetGet savingsStatementSetGet;
    private TextView mTv_currentBal;

    private Button mBtn_fromDate, mBtn_toDate, mBtn_submit;
    private DatePickerDialog datePickerDialog;
    private int int_currDay = 0, int_currMonth = 0, int_currYear = 0;
    private Calendar calendar;
    private String mStr_fromDate = "", mStr_toDate = "";
    private CheckBox mCb_date,mCb_showAll;
    private LinearLayout mLl_dateRoot;
    private int mInt_currDay=0,mInt_currMonth=0,mInt_currYear=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_savings_account_statement);

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

                  // TODO Savings account number

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_savingsAccountStatement.setLayoutManager(linearLayoutManager);


        mCb_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mCb_showAll.setChecked(false);
                    mLl_dateRoot.setVisibility(View.VISIBLE);
                }else{
                    mLl_dateRoot.setVisibility(View.GONE);
                }
            }
        });

        mCb_showAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    mCb_date.setChecked(false);
                    getSavingsAccountStatement(SelectedAccount.savingsCode);
                }

            }
        });

    }

    /**
     * Get savings account statement
     */
    private void getSavingsAccountStatement(String accountNumber) {
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
                int totalDeposit=0,totalWithdrawl=0;
                while (rs.next()) {
                    savingsStatementSetGet = new SavingsStatementSetGet();
                    //savingsStatementSetGet.setSlNo(rs.getString("tNo"));
                    savingsStatementSetGet.setSlNo(String.valueOf(i));
                    i++;
                    //rs.getString("VrNo");
                    //rs.getString("MemberCode");
                    savingsStatementSetGet.settDate(rs.getString("TDate"));
                    //savingsStatementSetGet.setParticular(rs.getString("Particular"));
                    savingsStatementSetGet.setPaymode(rs.getString("PayMode"));
                    savingsStatementSetGet.setDepositAmount(rs.getString("DepositAmount"));
                    savingsStatementSetGet.setWithdrawlAmount(rs.getString("WithdrawlAmount"));
                    totalDeposit=totalDeposit+(int) Double.parseDouble(rs.getString("DepositAmount"));
                    totalWithdrawl=totalWithdrawl+(int) Double.parseDouble(rs.getString("WithdrawlAmount"));

                    //savingsStatementSetGet.setBalance(rs.getString("Balance"));

                    mArrayList.add(savingsStatementSetGet);
                    loanData.setErrorCode(0);
                }
                int bal=totalDeposit-totalWithdrawl;
                mTv_currentBal.setText("Current Balance -"+ String.valueOf(bal));
                mAdapterSavingsAccountStatement = new AdapterSavingsAccountStatement(MemberSavingsAccountStatementActivity.this, mArrayList);
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

    private void getSavingsAccountStatementByDate(String accountNumber,String fromDate,String toDate) {
        Connection cn = new SqlManager().getSQLConnection();
        LoanData loanData = new LoanData();
        mArrayList = new ArrayList<>();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetSavingsAccountMiniStatementByDate(?,?,?)}");
                smt.setString("@SBAccount", accountNumber);
                smt.setString("@fromDate", fromDate);
                smt.setString("@toDate", toDate);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                int i=1;
                int totalDeposit=0,totalWithdrawl=0;
                while (rs.next()) {
                    savingsStatementSetGet = new SavingsStatementSetGet();
                    //savingsStatementSetGet.setSlNo(rs.getString("tNo"));
                    savingsStatementSetGet.setSlNo(String.valueOf(i));
                    i++;
                    //rs.getString("VrNo");
                    //rs.getString("MemberCode");
                    savingsStatementSetGet.settDate(rs.getString("TDate"));
                    //savingsStatementSetGet.setParticular(rs.getString("Particular"));
                    savingsStatementSetGet.setPaymode(rs.getString("PayMode"));
                    savingsStatementSetGet.setDepositAmount(rs.getString("DepositAmount"));
                    savingsStatementSetGet.setWithdrawlAmount(rs.getString("WithdrawlAmount"));
                    totalDeposit=totalDeposit+(int) Double.parseDouble(rs.getString("DepositAmount"));
                    totalWithdrawl=totalWithdrawl+(int) Double.parseDouble(rs.getString("WithdrawlAmount"));

                    //savingsStatementSetGet.setBalance(rs.getString("Balance"));

                    mArrayList.add(savingsStatementSetGet);
                    loanData.setErrorCode(0);
                }
                int bal=totalDeposit-totalWithdrawl;
                mTv_currentBal.setText("Current Balance -"+ String.valueOf(bal));
                mAdapterSavingsAccountStatement = new AdapterSavingsAccountStatement(MemberSavingsAccountStatementActivity.this, mArrayList);
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
        mTv_currentBal = findViewById(R.id.tv_member_sb_acc_statement_current_bal);
        // views
        mRv_savingsAccountStatement = findViewById(R.id.rv_activity_savings_account_statement);

        mCb_date=findViewById(R.id.cb_activity_member_sb_st_date);
        mLl_dateRoot=findViewById(R.id.ll_activity_member_sb_st_date_root);
        mCb_showAll=findViewById(R.id.cb_activity_member_sb_show_all);

        mBtn_fromDate=findViewById(R.id.btn_activity_member_sb_st_from_date);
        mBtn_toDate=findViewById(R.id.btn_activity_member_sb_st_to_date);
        mBtn_submit=findViewById(R.id.btn_activity_member_sb_st_submit_date);
    }

    private void bindEventHandlers() {
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_fromDate) {
            calendar=calendar.getInstance();
            mInt_currDay=calendar.get(Calendar.DAY_OF_MONTH);
            mInt_currMonth=calendar.get(Calendar.MONTH);
            mInt_currYear=calendar.get(Calendar.YEAR);
            datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_fromDate= String.valueOf(year)+ String.format("%02d",month)+ String.format("%02d",dayOfMonth);
                    mBtn_fromDate.setText(String.format("%02d",dayOfMonth)+" : "+ String.format("%02d",month)+" : "+ String.valueOf(year));
                }
            },mInt_currYear,mInt_currMonth,mInt_currDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }

        if (v == mBtn_toDate) {
            calendar=calendar.getInstance();
            mInt_currDay=calendar.get(Calendar.DAY_OF_MONTH);
            mInt_currMonth=calendar.get(Calendar.MONTH);
            mInt_currYear=calendar.get(Calendar.YEAR);
            datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_toDate= String.valueOf(year)+ String.format("%02d",month)+ String.format("%02d",dayOfMonth);
                    mBtn_toDate.setText(String.format("%02d",dayOfMonth)+" : "+ String.format("%02d",month)+" : "+ String.valueOf(year));
                }
            },mInt_currYear,mInt_currMonth,mInt_currDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
        if(v==mBtn_submit){
            if(mStr_fromDate.length()>0 && mStr_toDate.length()>0){
                getSavingsAccountStatementByDate(SelectedAccount.savingsCode,mStr_fromDate,mStr_toDate);
            }else{
                Toast.makeText(this, "Choose Date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MemberSavingsAccountStatementActivity.this, MemberSavingsAccountActivity.class));
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
        startActivity(new Intent(MemberSavingsAccountStatementActivity.this, MemberSavingsAccountActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
