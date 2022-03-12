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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminExecutiveCollecReport;
import com.geniustechnoindia.purnodaynidhi.bean.AdminData;
import com.geniustechnoindia.purnodaynidhi.model.AdminExecutiveCollecReportSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminExecutiveCollectionReportActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEt_arrCode;
    private Button mBtn_fromDate, mBtn_toDate, mBtn_submit;
    private RecyclerView mRv_details;
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private DatePickerDialog datePickerDialogFromDate, datePickerDialogToDate;
    private Calendar calendar;
    private int curr_year, curr_month, curr_day;

    private int intFromDate = 0, intToDate = 0;

    private AdapterAdminExecutiveCollecReport adapter;
    private AdminExecutiveCollecReportSetGet setGet;
    private ArrayList<AdminExecutiveCollecReportSetGet> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_executive_collection_report);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_details.setLayoutManager(linearLayoutManager);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mEt_arrCode = findViewById(R.id.et_activity_admin_executive_collection_arranger_code);
        mBtn_fromDate = findViewById(R.id.btn_activity_admin_executive_collection_from_date);
        mBtn_toDate = findViewById(R.id.btn_activity_admin_executive_collection_to_date);
        mRv_details = findViewById(R.id.rv_acrivity_admin_executive_collection_report_details);
        mBtn_submit = findViewById(R.id.btn_activity_admin_executive_collection_submit);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_submit) {
            if (mEt_arrCode.getText().toString().length() > 0) {
                if (intFromDate != 0) {
                    if (intToDate != 0) {
                        getExecutiveCollecDetails(APILinks.ADMIN_EXECUTIVE_COLLECTION_REPORT + mEt_arrCode.getText().toString().trim() + "&fdate=" + intFromDate + "&tdate=" + intToDate + "&officeid=" + AdminData.adminOfcID);
                    } else {
                        Toast.makeText(this, "Choose To Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Choose From Date", Toast.LENGTH_SHORT).show();
                }
            } else {
                mEt_arrCode.setError("Enter Account Code");
            }
        }
        if (v == mBtn_fromDate) {
            calendar = Calendar.getInstance();
            curr_year = calendar.get(Calendar.YEAR);
            curr_month = calendar.get(Calendar.MONTH);
            curr_day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialogFromDate = new DatePickerDialog(AdminExecutiveCollectionReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mBtn_fromDate.setText(String.valueOf(dayOfMonth) + ":" + String.valueOf(month) + ":" + String.valueOf(year));
                    intFromDate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, curr_year, curr_month, curr_day);
            datePickerDialogFromDate.show();
        }

        if (v == mBtn_toDate) {
            calendar = Calendar.getInstance();
            curr_year = calendar.get(Calendar.YEAR);
            curr_month = calendar.get(Calendar.MONTH);
            curr_day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialogToDate = new DatePickerDialog(AdminExecutiveCollectionReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mBtn_toDate.setText(String.valueOf(dayOfMonth) + ":" + String.valueOf(month) + ":" + String.valueOf(year));
                    intToDate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, curr_year, curr_month, curr_day);
            datePickerDialogToDate.show();
        }
    }

    private void getExecutiveCollecDetails(String url) {
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            setGet = new AdminExecutiveCollecReportSetGet();
                            JSONObject jsonObject = response.getJSONObject(i);
                            setGet.setPolicyCode(jsonObject.getString("PolicyCode"));
                            setGet.setInstNo(jsonObject.getString("InstNo"));
                            setGet.setAmount(jsonObject.getString("Amount"));

                            Date tradePayDate = null;
                            try {
                                tradePayDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(jsonObject.getString("DateofRenewal"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String dateOfRenew = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(tradePayDate);
                            setGet.setDateOfRenewal(dateOfRenew);

                            //setGet.setDateOfRenewal(jsonObject.getString("DateofRenewal"));
                            setGet.setUserName(jsonObject.getString("UserName"));
                            arrayList.add(setGet);
                        }
                        adapter = new AdapterAdminExecutiveCollecReport(AdminExecutiveCollectionReportActivity.this, arrayList);
                        mRv_details.setAdapter(adapter);
                    } else {
                        Toast.makeText(AdminExecutiveCollectionReportActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Business Report");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AdminExecutiveCollectionReportActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminExecutiveCollectionReportActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
