package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoanStatementMemberNewActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Spinner mSp_loanList;
    private ArrayList<String> arrayList_loanCodes=new ArrayList<>();
    private TextView mTv_loanDate, mTv_loanType, mTv_loanCode, mTv_memberCode, mTv_name, mTv_fatherName, mTv_addr, mTv_loanAmt, mTv_recoveryAmt, mTv_interestAmt, mTv_emiAmt,
            mTv_emiMode,mTv_totalEMI, mTv_paidEMI, mTv_paidMode, mTv_lastPaidEMIDate, nextPaidEmiDate, mTv_totalDeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_statement_member_new);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
        getActiveLoanCodes(APILinks.GET_ACTIVE_LOAN_CODE_BY_MCODE+ GlobalStore.GlobalValue.getMemberCode());

        mSp_loanList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    getLoanDetails(APILinks.GET_MEMBER_LOAN_DETAILS + arrayList_loanCodes.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_loanDate = findViewById(R.id.tv_loan_st_member_loan_date);
        mTv_loanType = findViewById(R.id.tv_loan_st_member_loan_type);
        mTv_loanCode = findViewById(R.id.tv_loan_st_member_loan_code);
        mTv_memberCode = findViewById(R.id.tv_loan_st_member_member_code);
        mTv_name = findViewById(R.id.tv_loan_st_member_name);
        mTv_fatherName = findViewById(R.id.tv_loan_st_member_father);
        mTv_addr = findViewById(R.id.tv_loan_st_member_addr);
        mTv_loanAmt = findViewById(R.id.tv_loan_st_member_loan_amt);
        mTv_recoveryAmt = findViewById(R.id.tv_loan_st_member_recovery_amt);
        mTv_interestAmt = findViewById(R.id.tv_loan_st_member_interest_amt);
        mTv_totalEMI = findViewById(R.id.tv_loan_st_member_no_of_emi);
        mTv_emiAmt = findViewById(R.id.tv_loan_st_member_emi_amt);
        mTv_emiMode = findViewById(R.id.tv_loan_st_member_emi_mode);
        mTv_paidEMI = findViewById(R.id.tv_loan_st_member_paid_emi);
        mTv_paidMode = findViewById(R.id.tv_loan_st_member_paid_emi_mode);
        mTv_lastPaidEMIDate = findViewById(R.id.tv_loan_st_member_paid_emi_date);
        nextPaidEmiDate = findViewById(R.id.tv_loan_st_member_paid_emi_next_date);
        mTv_totalDeposit = findViewById(R.id.tv_loan_st_member_total_deposit);

        mSp_loanList = findViewById(R.id.sp_activity_loan_statement_member_loan_list);

    }

    private void bindEventHandlers() {
    }

    private void getActiveLoanCodes(String url){
        arrayList_loanCodes.clear();
        arrayList_loanCodes.add("");
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            arrayList_loanCodes.add(jsonObject.getString("LoanCode"));
                        }
                        ArrayAdapter arrayAdapter=new ArrayAdapter(LoanStatementMemberNewActivity.this,R.layout.spinner_hint_one,arrayList_loanCodes);
                        mSp_loanList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(LoanStatementMemberNewActivity.this, "No Loan Found", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void getLoanDetails(String url) {
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        JSONObject jsonObject = response.getJSONObject(0);
                        mTv_loanCode.setText(jsonObject.getString("LoanCode"));
                        mTv_loanDate.setText(jsonObject.getString("LoanDate"));
                        mTv_loanType.setText(jsonObject.getString("EMIMode"));
                        mTv_memberCode.setText(jsonObject.getString("GenericCode"));
                        mTv_name.setText(jsonObject.getString("HolderName"));
                        mTv_fatherName.setText(jsonObject.getString("Father"));
                        mTv_addr.setText(jsonObject.getString("Addr"));
                        mTv_loanAmt.setText(String.valueOf(jsonObject.getInt("LoanApproveAmount")));
                        int recoveryAmt = jsonObject.getInt("LoanTerm") * jsonObject.getInt("EMIAmount");
                        mTv_recoveryAmt.setText(String.valueOf(recoveryAmt));
                        mTv_interestAmt.setText(String.valueOf(recoveryAmt - jsonObject.getInt("LoanApproveAmount")));
                        mTv_emiAmt.setText(String.valueOf(jsonObject.getInt("EMIAmount")));
                        mTv_totalEMI.setText(String.valueOf(jsonObject.getInt("LoanTerm")));
                        mTv_paidEMI.setText(String.valueOf(jsonObject.getInt("maxPaidEMI")));
                        mTv_emiMode.setText(jsonObject.getString("EMIMode"));
                        mTv_paidMode.setText(jsonObject.getString("PayMode"));
                        mTv_lastPaidEMIDate.setText(jsonObject.getString("lastPaidEMIDate"));
                        nextPaidEmiDate.setText(jsonObject.getString("NextEMIDate"));
                        mTv_totalDeposit.setText(String.valueOf(jsonObject.getInt("maxPaidEMI") * jsonObject.getInt("EMIAmount")));
                    } else {
                        Toast.makeText(LoanStatementMemberNewActivity.this, "No Details Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Loan Statement");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                startActivity(new Intent(LoanStatementMemberNewActivity.this, MemberDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoanStatementMemberNewActivity.this, MemberDashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}