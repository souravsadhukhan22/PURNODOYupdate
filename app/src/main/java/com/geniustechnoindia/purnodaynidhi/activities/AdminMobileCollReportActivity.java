package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;

import java.util.Calendar;

public class AdminMobileCollReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Button mBtn_fromDate, mBtn_toDate, mBtn_submit;
    private EditText mEt_arrCode;
    private DatePickerDialog datePickerDialog;
    private int int_currDay = 0, int_currMonth = 0, int_currYear = 0;
    private Calendar calendar;
    private String mStr_fromDate = "", mStr_toDate = "";

    private TextView mTv_loanAmount, mTv_investmentAmount,mTv_savingsAmount,mTv_totalAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mobile_coll_report);

        setViewReferences();
        bindEventHandlers();
        setUpToolBar();

        calendar = Calendar.getInstance();
        int_currDay = calendar.get(Calendar.DAY_OF_MONTH);
        int_currMonth = calendar.get(Calendar.MONTH);
        int_currYear = calendar.get(Calendar.YEAR);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mBtn_fromDate = findViewById(R.id.btn_activity_admin_mobile_coll_report_from_date);
        mBtn_toDate = findViewById(R.id.btn_activity_admin_mobile_coll_report_to_date);
        mBtn_submit = findViewById(R.id.btn_activity_admin_mobile_coll_report_submit);
        mEt_arrCode = findViewById(R.id.et_activity_admin_mobile_coll_report_arr_code);

        mTv_loanAmount = findViewById(R.id.tv_activity_admin_mobile_coll_report_loan_amount);
        mTv_investmentAmount = findViewById(R.id.tv_activity_admin_mobile_coll_report_investment_amount);
        mTv_savingsAmount=findViewById(R.id.tv_activity_admin_mobile_coll_report_savings_amount);
        mTv_totalAmt=findViewById(R.id.tv_activity_admin_mobile_coll_report_total_amount);
    }

    private void bindEventHandlers() {
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_fromDate) {
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_fromDate = String.valueOf(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth);
                    mBtn_fromDate.setText(String.valueOf(dayOfMonth)+" : "+ String.valueOf(month)+" : "+ String.valueOf(year));
                }
            }, int_currYear, int_currMonth, int_currDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }

        if (v == mBtn_toDate) {
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_toDate = String.valueOf(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth);
                    mBtn_toDate.setText(String.valueOf(dayOfMonth)+" : "+ String.valueOf(month)+" : "+ String.valueOf(year));
                }
            }, int_currYear, int_currMonth, int_currDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }

        if (v == mBtn_submit) {
            if (mStr_fromDate.length() > 0 && mStr_toDate.length() > 0) {
                if (mEt_arrCode.getText().toString().trim().length() > 0) {
                    getCollectionReport(APILinks.ADMIN_MOBILE_COLLECTION + mEt_arrCode.getText().toString() + "&fdate="+mStr_fromDate + "&tdate=" + mStr_toDate);
                } else {
                    mEt_arrCode.setError("Enter Arranger Code");
                    mEt_arrCode.requestFocus();
                }
            } else {
                Toast.makeText(this, "Choose from or to date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCollectionReport(String url) {
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        mTv_loanAmount.setText(response.getJSONObject(0).getString("LAmt"));
                        mTv_investmentAmount.setText(response.getJSONObject(0).getString("IAmt"));
                        mTv_savingsAmount.setText(response.getJSONObject(0).getString("SAmt"));
                        int totalAmt=response.getJSONObject(0).getInt("LAmt")+response.getJSONObject(0).getInt("IAmt")+response.getJSONObject(0).getInt("SAmt");
                        mTv_totalAmt.setText(String.valueOf(totalAmt));
                    }else{
                        Toast.makeText(AdminMobileCollReportActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpToolBar() {
        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Mobile Collection Report");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(AdminMobileCollReportActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminMobileCollReportActivity.this, AdminDashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
