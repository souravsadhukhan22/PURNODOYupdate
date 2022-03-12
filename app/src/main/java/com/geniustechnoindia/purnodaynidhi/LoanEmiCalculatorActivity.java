package com.geniustechnoindia.purnodaynidhi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.adapters.AdapterLoanCalculator;
import com.geniustechnoindia.purnodaynidhi.model.SetGetEMIBrakupData;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LoanEmiCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private TextView mTv_months,mTv_loanAmt;
    private SeekBar mSb_months,mSb_loanAmt;
    private Button mBtn_submit;
    private ArrayList<SetGetEMIBrakupData> arrayList_emiBrakupData=new ArrayList<>();
    private SetGetEMIBrakupData setGetEMIBrakupData;
    private AdapterLoanCalculator adapterLoanCalculator;
    private RecyclerView mRv_emiDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_emi_calculator);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        mSb_months.setMax(36);
        mSb_months.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTv_months.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSb_loanAmt.setMax(200000);
        mSb_loanAmt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int stepSize = 1000;
                progress = ((int) Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);
                mTv_loanAmt.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_emiDetails.setLayoutManager(linearLayoutManager);
        adapterLoanCalculator=new AdapterLoanCalculator(com.geniustechnoindia.purnodaynidhi.LoanEmiCalculatorActivity.this,arrayList_emiBrakupData);
        mRv_emiDetails.setAdapter(adapterLoanCalculator);
    }


    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mTv_months = findViewById(R.id.tv_activity_loan_emi_calculator_months);
        mSb_months = findViewById(R.id.sb_activity_loan_emi_calculator_months);
        mTv_loanAmt = findViewById(R.id.tv_activity_loan_emi_calculator_amt);
        mSb_loanAmt = findViewById(R.id.sb_activity_loan_emi_calculator_amt);
        mBtn_submit = findViewById(R.id.btn_activity_loan_emi_cal_submit);
        mRv_emiDetails = findViewById(R.id.rv_activity_loan_emi_calculator_emi_details);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==mBtn_submit){
            if(mTv_loanAmt.getText().toString().length()>0 && mTv_months.getText().toString().length()>0){
                getLoanEMIBrakupForEMICalculator(Integer.parseInt(mTv_loanAmt.getText().toString()),20, Integer.parseInt(mTv_months.getText().toString()),"Mly.");
            }else{

            }
        }
    }

    private void getLoanEMIBrakupForEMICalculator(int loanAmt, int roi, int period, String mode){
        arrayList_emiBrakupData.clear();
        Connection cn;
        cn= new SqlManager().getSQLConnection();
        try{
            if(cn!=null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanEMIBrakupForEMICalculator_NEW(?,?,?,?)}");
                smt.setInt("@LoanAmount",loanAmt);
                smt.setInt("@InterestRate",roi);
                smt.setInt("@LoanPeriod",period);
                smt.setString("@Mode",mode);
                smt.execute();

                ResultSet rs=smt.executeQuery();
                while(rs.next()){
                    setGetEMIBrakupData=new SetGetEMIBrakupData();
                    setGetEMIBrakupData.setPeriod(String.valueOf(rs.getInt("PERIOD")));
                    setGetEMIBrakupData.setPayDate(rs.getString("PAYDATE"));
                    setGetEMIBrakupData.setPayment(String.valueOf(rs.getInt("PAYMENT")));
                    setGetEMIBrakupData.setInterest(String.valueOf(rs.getInt("INTEREST")));
                    setGetEMIBrakupData.setPrincipal(String.valueOf(rs.getInt("PRINCIPAL")));
                    setGetEMIBrakupData.setCurrentBal(String.valueOf(rs.getInt("CURRENT_BALANCE")));
                    arrayList_emiBrakupData.add(setGetEMIBrakupData);
                }
                adapterLoanCalculator.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Calculator");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.LoanEmiCalculatorActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.LoanEmiCalculatorActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}