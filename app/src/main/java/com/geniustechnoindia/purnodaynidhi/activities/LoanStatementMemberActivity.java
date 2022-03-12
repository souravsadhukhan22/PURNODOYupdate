package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.AppData;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.SelectedAccount;
import com.geniustechnoindia.purnodaynidhi.others.TempLoanData;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class LoanStatementMemberActivity extends AppCompatActivity implements View.OnClickListener {

    // Toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextView mTv_loanCodeList;
    private PopupMenu popupMenuLoanCode;

    private TextView mTv_loanHolderName;
    private TextView mTv_memberCode;
    private TextView mTv_loanAmt;
    private TextView mTv_loanDate;
    private TextView mTv_loanTerm;
    private TextView mTv_emiMode;
    private TextView mTv_emiAmt;
    private TextView mTv_emiPayMode;

    // vars
    private String stmtHolderName = "";
    private String stmtGenericCode = "";
    private String stmtLoanReqAmt = "";
    private String stmtLoanCode = "";
    private String stmtLoanDate = "";
    private String stmtLoanTerm = "";
    private String stmtEmiMode = "";
    private String stmtEmiAmt = "";
    private String stmtLoanFeeePayMode = "";
    private String stmtIsApprove = "";

    private String stmtHolderAddress = "";
    private String stmtHolderPh = "";
    private String stmtHolderDob = "";
    private String stmtDateOfEntry = "";
    private String stmtLoanApprovedAmt = "";
    private String stmtEmiPercentage = "";

    private String stmtNetLoanAmt = "";

    private Button mBtn_viewLoanEmiStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_statement_member_activity);

        setViewReferences();
        bindEventHandlers();

        TempLoanData.emiAmount = 0.0;

        // Toolbar
        setSupportActionBar(mToolbar);
        mTv_toolbarTitle.setText("Loan Statement");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Popup menu select account
        popupMenuLoanCode = new PopupMenu(this, mTv_loanCodeList);                    // TODO Change menu options

        // TODO Get menu options first
        getLoanCodeMenuOptions(GlobalStore.GlobalValue.getMemberCode());

        popupMenuLoanCode.setOnMenuItemClickListener(item -> {
            mTv_loanCodeList.setText(item.getTitle());
            SelectedAccount.loanCode = String.valueOf(item.getTitle());
            getLoanStatement((String) item.getTitle());
            return true;
        });
    }


    private boolean getLoanStatement(String loanCode) {
        Connection cn = new SqlManager().getSQLConnection();
        boolean rValue = false;
        String ut = "";
        AppData ad;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanDetails(?)}");
                smt.setString("@LoanCode", loanCode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        mTv_loanHolderName.setText(rs.getString("HolderName"));
                        mTv_memberCode.setText(GlobalStore.GlobalValue.getMemberCode());
                        mTv_loanAmt.setText(rs.getString("LoanApproveAmount"));
                        mTv_loanDate.setText(rs.getString("LoanDate"));
                        mTv_loanTerm.setText(rs.getString("LoanTerm"));
                        TempLoanData.loanTerm = Integer.parseInt(rs.getString("LoanTerm"));
                        mTv_emiMode.setText(rs.getString("EMIMode"));
                        mTv_emiAmt.setText(rs.getString("EMIAmount"));
                        TempLoanData.emiAmount = Double.parseDouble(rs.getString("EMIAmount"));
                        //mTv_emiPayMode.setText(rs.getString(""));
                        rValue = true;
                    }
                } else {
                    Toast.makeText(this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            rValue = false;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    private boolean getLoanCodeMenuOptions(String memberCode) {
        Connection cn = new SqlManager().getSQLConnection();
        boolean rValue = false;
        String ut = "";
        AppData ad;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetMemberLoanCode(?)}");
                smt.setString("@MemberCode", memberCode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        popupMenuLoanCode.getMenu().add(rs.getString("LoanCode"));
                        rValue = true;
                    }
                } else {
                    Toast.makeText(this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            rValue = false;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    /*private void getLoanStatement(String memberCode, String loanCode) {
        new GetDataParserArray(LoanStatementCustomerActivity.this, APILinks.CUSTOMER_LOAN_INFO + "loanCode=" + loanCode + "&userName=" + memberCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            stmtLoanCode = jsonObject.getString("LoanCode");
                            mTv_memberCode.setText(jsonObject.getString("GenericCode"));
                            mTv_loanHolderName.setText(jsonObject.getString("HolderName"));
                            stmtHolderAddress = jsonObject.getString("HolderAddress");
                            stmtHolderPh = jsonObject.getString("HolderPhoneNo");
                            stmtHolderDob = jsonObject.getString("HolderDOB");
                            mTv_loanDate.setText(jsonObject.getString("LoanDate"));
                            stmtDateOfEntry = jsonObject.getString("DateOfEntry");
                            mTv_loanTerm.setText(jsonObject.getString("LoanTerm"));
                            mTv_emiMode.setText(jsonObject.getString("EMIMode"));
                            mTv_loanAmt.setText(jsonObject.getString("LoanRequestAmount"));
                            stmtLoanApprovedAmt = jsonObject.getString("LoanApproveAmount");
                            stmtEmiPercentage = jsonObject.getString("EMIPercentage");
                            mTv_emiAmt.setText(jsonObject.getString("EMIAmount"));
                            stmtNetLoanAmt = jsonObject.getString("NetLoanAmount");
                            mTv_emiPayMode.setText(jsonObject.getString("LoanFeePayMode"));
                            stmtIsApprove = jsonObject.getString("IsApprove");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // TODO No record found
                    }
                }
            }
        });
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LoanStatementMemberActivity.this, MemberDashboardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_loanHolderName = findViewById(R.id.tv_loan_statement_customer_activity_holder_name);
        mTv_memberCode = findViewById(R.id.tv_loan_statement_customer_activity_member_code);
        mTv_loanAmt = findViewById(R.id.tv_loan_statement_customer_activity_loan_amount);
        mTv_loanDate = findViewById(R.id.tv_loan_statement_customer_activity_loan_date);
        mTv_loanTerm = findViewById(R.id.tv_loan_statement_customer_activity_loan_term);
        mTv_emiMode = findViewById(R.id.tv_loan_statement_customer_activity_emi_mode);
        mTv_emiAmt = findViewById(R.id.tv_loan_statement_customer_activity_emi_amnt);
        mTv_emiPayMode = findViewById(R.id.tv_loan_statement_customer_activity_pay_mode);

        // views
        mTv_loanCodeList = findViewById(R.id.tv_activity_customer_loan_statement_dropdown);
        mBtn_viewLoanEmiStatement = findViewById(R.id.btn_activity_loan_statement_member_activity);
    }

    private void bindEventHandlers() {
        mTv_loanCodeList.setOnClickListener(this);
        mBtn_viewLoanEmiStatement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_loanCodeList) {
            popupMenuLoanCode.show();
        } else if (v == mBtn_viewLoanEmiStatement) {
            startActivity(new Intent(LoanStatementMemberActivity.this, LoanEmiStatementMemberActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(LoanStatementMemberActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
