package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterLoanEmiStatement;
import com.geniustechnoindia.purnodaynidhi.bean.LoanEMIBrakupData;
import com.geniustechnoindia.purnodaynidhi.model.SetGetLoanEMI;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.SelectedAccount;
import com.geniustechnoindia.purnodaynidhi.others.TempLoanData;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LoanEmiStatementMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;


    private RecyclerView mRv_emiStatement;
    private AdapterLoanEmiStatement adapterLoanEmiStatement;

    private TextView mTv_totalEmiPaid;
    private TextView mTv_dueTerm;

    private int noOfPaidTerm = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_emi_statement_member);


        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Loan EMI statement");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_emiStatement.setLayoutManager(linearLayoutManager);

        getLoanEMIStatement(SelectedAccount.loanCode);          // TODO Get loan code
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(LoanEmiStatementMemberActivity.this, LoanStatementMemberActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // M0000019
    // L000001

    public ArrayList<SetGetLoanEMI> getLoanEMIStatement(String loanCode) {
        Connection cn = new SqlManager().getSQLConnection();
        ArrayList<SetGetLoanEMI> arrayList = new ArrayList<SetGetLoanEMI>();
        SetGetLoanEMI setGetLoanEmi;

        LoanEMIBrakupData bData = null;
        CallableStatement smt = null;
        double totalPaid = 0.0;
        try {
            if (cn != null) {
                smt = cn.prepareCall("{call ADROID_GetLoanEmiStatement(?)}");
                smt.setString("@LOANCODE", loanCode);
                smt.execute();
                //ResultSet rs=smt.getResultSet();
                ResultSet rs = smt.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        setGetLoanEmi = new SetGetLoanEMI();
                        setGetLoanEmi.setLoanCode(rs.getString("LoanCode"));
                        setGetLoanEmi.setEmiNo(rs.getString("EMINo"));

                        String emiDueDate = rs.getString("EMIDueDate");
                        Date emiDueDateNew = null;
                        try {
                            emiDueDateNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(emiDueDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedEmiDueDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(emiDueDateNew);
                        setGetLoanEmi.setEmiDueDate(formattedEmiDueDate);

                        String paymentDate = rs.getString("PaymentDate");
                        Date paymentDateNew = null;
                        try {
                            paymentDateNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(paymentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedPaymentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(paymentDateNew);
                        setGetLoanEmi.setPaymentDate(formattedPaymentDate);

                        setGetLoanEmi.setEmiAmount(rs.getString("EMIAmount"));
                        setGetLoanEmi.setCurrentBalance(rs.getString("CurrantBalance"));
                        setGetLoanEmi.setPayMode(rs.getString("PayMode"));
                        setGetLoanEmi.setRemarks(rs.getString("Remarks"));
                        arrayList.add(setGetLoanEmi);
                        noOfPaidTerm ++;
                        totalPaid = totalPaid + TempLoanData.emiAmount;
                        mTv_totalEmiPaid.setText("Rs. " + totalPaid);
                    }
                    int dueT = TempLoanData.loanTerm - noOfPaidTerm;
                    mTv_dueTerm.setText(dueT + "");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoanEmiStatementMemberActivity.this);
                    builder.setMessage("Statement not found for this account");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(LoanEmiStatementMemberActivity.this, LoanStatementMemberActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }).show();
                }

                adapterLoanEmiStatement = new AdapterLoanEmiStatement(LoanEmiStatementMemberActivity.this, arrayList);
                mRv_emiStatement.setAdapter(adapterLoanEmiStatement);
            } else {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }


    private void setViewReferences() {
        mRv_emiStatement = findViewById(R.id.rv_activity_loan_emi_statement_member);
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mTv_totalEmiPaid = findViewById(R.id.tv_activity_loan_emi_statement_member);
        mTv_dueTerm = findViewById(R.id.tv_activity_loan_emi_statement_due_term);

    }

    private void bindEventHandlers() {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(LoanEmiStatementMemberActivity.this, LoanStatementMemberActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
