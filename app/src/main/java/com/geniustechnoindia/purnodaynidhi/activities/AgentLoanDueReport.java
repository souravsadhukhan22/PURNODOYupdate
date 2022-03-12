package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterLoanDueReport;
import com.geniustechnoindia.purnodaynidhi.model.SetGetLoanDueReport;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

public class AgentLoanDueReport extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private TextView mTv_fDate;
    private TextView mTv_tDate;
    private EditText mEt_loanCode;
    private Button mBtn_show;

    private ProgressBar mPb_proggress;

    private EditText mEt_enterOfficeId;
    private Button mBtn_showAll;

    // vars
    private int fdate = 0;
    private int tdate = 0;
    int mYear, mMonth, mDay;
    private int currentDay, currentMonth, currentYear;
    private Calendar mCalendar;

    private Connection cn;

    private ArrayList<SetGetLoanDueReport> mArrayListDueReport;
    private RecyclerView mRv_loanDueReport;
    private AdapterLoanDueReport adapterLoanDueReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_loan_due_report);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbarTitle.setText("Loan Due Report");

        mArrayListDueReport = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_loanDueReport.setLayoutManager(linearLayoutManager);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);

        mTv_fDate = findViewById(R.id.tv_activity_agent_loan_due_report_fdate);
        mTv_tDate = findViewById(R.id.tv_activity_agent_loan_due_report_tdate);
        mEt_loanCode = findViewById(R.id.ev_activity_agent_loan_due_report_enter_loan_code);
        mBtn_show = findViewById(R.id.btn_activity_agent_loan_due_report_show);
        mRv_loanDueReport = findViewById(R.id.rv_activity_loan_due_report);

        mPb_proggress = findViewById(R.id.pb_activity_agent_loan_due_report);

        //mEt_enterOfficeId = findViewById(R.id.et_activity_agent_loan_due_report_enter_office_id);
        mBtn_showAll = findViewById(R.id.btn_activity_agent_loan_due_report_show_all);
    }

    private void bindEventHandlers() {
        mTv_fDate.setOnClickListener(this);
        mTv_tDate.setOnClickListener(this);
        mBtn_show.setOnClickListener(this);
        mBtn_showAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_fDate) {
            selectFDate();
        } else if (v == mTv_tDate) {
            selectTDate();
        } else if (v == mBtn_show) {
            if (mTv_fDate.getText().toString().trim().length() > 0) {
                if (mTv_tDate.getText().toString().trim().length() > 0) {
                    mArrayListDueReport.clear();
                    getLoanDueReport(fdate, tdate, mEt_loanCode.getText().toString());
                } else {
                    mTv_tDate.performClick();
                    Toast.makeText(this, "Select tDate", Toast.LENGTH_SHORT).show();
                }
            } else {
                mTv_fDate.performClick();
                Toast.makeText(this, "Select fDate", Toast.LENGTH_SHORT).show();
            }

        } /*else if (v == mBtn_showAll) {
            if (mTv_fDate.getText().toString().trim().length() > 0) {
                if (mTv_tDate.getText().toString().trim().length() > 0) {
                    mArrayListDueReport.clear();
                    showAllData(fdate, tdate, GlobalStore.GlobalValue.getOfficeID());
                } else {
                    mTv_tDate.performClick();
                    Toast.makeText(this, "Select tDate", Toast.LENGTH_SHORT).show();
                }
            } else {
                mTv_fDate.performClick();
                Toast.makeText(this, "Select fDate", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void showAllData(int fDate, int tDate, String officeId) {
        cn = new SqlManager().getSQLConnection();
        SetGetLoanDueReport setGetLoanDueReport = null;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanDueReportByOfficeId(?,?,?)}");
                smt.setInt("@Fdate", fDate);
                smt.setInt("@Tdate", tDate);
                smt.setString("@OfficeID", officeId);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        setGetLoanDueReport = new SetGetLoanDueReport();
                        setGetLoanDueReport.setLoanCode(rs.getString("LoanCode"));
                        setGetLoanDueReport.setApplicantName(rs.getString("ApplicantName"));
                        setGetLoanDueReport.setPhNo(rs.getString("HolderPhoneNo"));
                        setGetLoanDueReport.setTotalDueEmiNo(rs.getString("TotalDueEMINo"));
                        setGetLoanDueReport.setTotalDueEmiAmnt(rs.getString("TotalDueEMIAmount"));
                        mArrayListDueReport.add(setGetLoanDueReport);
                    }
                    adapterLoanDueReport = new AdapterLoanDueReport(AgentLoanDueReport.this, mArrayListDueReport);
                    mRv_loanDueReport.setAdapter(adapterLoanDueReport);
                } else {
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }


    public void getLoanDueReport(int fDate, int tDate, String memberCode) {
        mPb_proggress.setVisibility(View.VISIBLE);
        cn = new SqlManager().getSQLConnection();
        mArrayListDueReport.clear();
        SetGetLoanDueReport setGetLoanDueReport = null;

        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanDueReport(?,?,?)}");
                smt.setInt("@Fdate", fDate);
                smt.setInt("@Tdate", tDate);
                smt.setString("@arrCode", GlobalStore.GlobalValue.getUserName());
                //smt.setString("@MemberCode", memberCode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        setGetLoanDueReport = new SetGetLoanDueReport();
                        setGetLoanDueReport.setLoanCode(rs.getString("LoanCode"));
                        setGetLoanDueReport.setApplicantName(rs.getString("ApplicantName"));
                        setGetLoanDueReport.setPhNo(rs.getString("HolderPhoneNo"));
                        setGetLoanDueReport.setTotalDueEmiNo(rs.getString("NoDueEMI"));
                        setGetLoanDueReport.setTotalDueEmiAmnt(rs.getString("TotalDueEMIAmount"));
                        mArrayListDueReport.add(setGetLoanDueReport);
                    }
                    adapterLoanDueReport = new AdapterLoanDueReport(AgentLoanDueReport.this, mArrayListDueReport);
                    mRv_loanDueReport.setAdapter(adapterLoanDueReport);
                    mPb_proggress.setVisibility(View.GONE);
                } else {
                    mPb_proggress.setVisibility(View.GONE);
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                mPb_proggress.setVisibility(View.GONE);
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            mPb_proggress.setVisibility(View.GONE);
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectFDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AgentLoanDueReport.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                mTv_fDate.setText(dayOfMonth + "-" + month + "-" + year);
                fdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void selectTDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AgentLoanDueReport.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;

                mTv_tDate.setText(dayOfMonth + "-" + month + "-" + year);
                tdate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
            }
        }, currentYear, currentMonth, currentDay);
        mCalendar.set(currentYear, currentMonth, currentDay);

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AgentLoanDueReport.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AgentLoanDueReport.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}

