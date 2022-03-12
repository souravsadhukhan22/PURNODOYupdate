package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminLoanPay;
import com.geniustechnoindia.purnodaynidhi.model.AdminLoanPaySetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminLoanPaymentReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private Button mBtn_fromDate,mBtn_toDate,mBtn_submit,mBtn_submitLoanCode;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mInt_currDay=0,mInt_currMonth=0,mInt_currYear=0;
    private String mStr_fromDate="",mStr_toDate="";

    private Spinner mSp_searchBy;
    private String mStr_searchBy="",mStr_ofcCode="";

    private ArrayList<String> arrayList_ofcCode=new ArrayList<>();

    private Spinner mSp_ofcCode;

    private ArrayList<String> arrayList_searchBy=new ArrayList<>();

    private LinearLayout mLl_ofcRoot,mLl_loanRoot;

    private EditText mEt_loanCode;
    private RecyclerView mRv_loanPayDetails;

    private AdapterAdminLoanPay adapter;
    private AdminLoanPaySetGet setGet;
    private ArrayList<AdminLoanPaySetGet> arrayList_loanDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_loan_payment_report);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
        layoutManagers();
        adapters();

        arrayList_searchBy.add("");
        arrayList_searchBy.add("Office");
        arrayList_searchBy.add("Loan Code");

        mSp_searchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    mStr_searchBy=mSp_searchBy.getSelectedItem().toString();
                    if(position==1){
                        mLl_ofcRoot.setVisibility(View.VISIBLE);
                        mLl_loanRoot.setVisibility(View.GONE);
                        getOffcList(APILinks.GET_OFC_LIST);
                    }
                    if(position==2){
                        mLl_ofcRoot.setVisibility(View.GONE);
                        mLl_loanRoot.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSp_ofcCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    mStr_ofcCode=mSp_ofcCode.getSelectedItem().toString();
                    getDetailsByOfcCode(APILinks.GET_ADMIN_LOAN_DETAILS_BY_OFC_CODE+"office wise"+"&LoanCode="+""+"&OfficeID="+mStr_ofcCode+"&FDate="+mStr_fromDate+"&TDate="+mStr_toDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void layoutManagers(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_loanPayDetails.setNestedScrollingEnabled(false);
        mRv_loanPayDetails.setLayoutManager(linearLayoutManager);
    }

    private void adapters(){
        arrayList_loanDetails =new ArrayList<>();
        adapter =new AdapterAdminLoanPay(AdminLoanPaymentReportActivity.this, arrayList_loanDetails);
        mRv_loanPayDetails.setAdapter(adapter);
    }

    private void getOffcList(String url){
        arrayList_ofcCode.clear();
        arrayList_ofcCode.add("");
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        arrayList_ofcCode.add("All");
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            arrayList_ofcCode.add(jsonObject.getString("OfficeID"));
                        }
                        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(AdminLoanPaymentReportActivity.this,R.layout.spinner_hint_one,arrayList_ofcCode );
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_hint);
                        mSp_ofcCode.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(AdminLoanPaymentReportActivity.this, "No Office list found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void getDetailsByOfcCode(String url){
        arrayList_loanDetails.clear();
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            setGet=new AdminLoanPaySetGet();
                            JSONObject jsonObject=response.getJSONObject(i);
                            setGet.setLoanCode(jsonObject.getString("LOANCODE"));
                            setGet.setHolderName(jsonObject.getString("HOLDERNAME"));
                            setGet.setLoanDate(jsonObject.getString("LOANDATE"));
                            setGet.setLoanTerm(String.valueOf(jsonObject.getInt("LOANTERM")));
                            setGet.setEmiMode(jsonObject.getString("EMIMODE"));
                            setGet.setLoanApproveAmt(String.valueOf(jsonObject.getInt("LOANAPPROVEAMOUNT")));
                            setGet.setEmiAmt(String.valueOf(jsonObject.getInt("LOANAPPROVEAMOUNT")));
                            setGet.setEmiNo(String.valueOf(jsonObject.getInt("EMINO")));
                            setGet.setPaidAmt(String.valueOf(jsonObject.getInt("PAIDAMOUNT")));

                            arrayList_loanDetails.add(setGet);
                        }

                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(AdminLoanPaymentReportActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }


    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mBtn_fromDate=findViewById(R.id.btn_activity_admin_loan_payment_from_date);
        mBtn_toDate=findViewById(R.id.btn_activity_admin_loan_payment_to_date);
        mBtn_submit=findViewById(R.id.btn_activity_admin_loan_payment_submit);

        mSp_searchBy=findViewById(R.id.sp_activity_admin_loan_payment_report_ofc_or_loan);
        mSp_ofcCode=findViewById(R.id.sp_activity_admin_loan_payment_report_ofc_code);
        mLl_ofcRoot=findViewById(R.id.ll_activity_admin_loan_payment_report_ofc);
        mLl_loanRoot=findViewById(R.id.ll_activity_admin_loan_payment_report_loan);
        mBtn_submitLoanCode=findViewById(R.id.btn_activity_admin_loan_payment_report_submit_loan_code);
        mEt_loanCode=findViewById(R.id.et_activity_admin_loan_payment_report_loan_code);
        mRv_loanPayDetails=findViewById(R.id.rv_activity_admin_loan_payment_details);
    }

    private void bindEventHandlers(){
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mBtn_submitLoanCode.setOnClickListener(this);
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
            if(!mStr_fromDate.equals("") && !mStr_toDate.equals("")){
                Context context;
                ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_hint_one,arrayList_searchBy );
                arrayAdapter.setDropDownViewResource(R.layout.spinner_hint);
                mSp_searchBy.setAdapter(arrayAdapter);
            }else{
                Toast.makeText(this, "Select from date and to date", Toast.LENGTH_SHORT).show();
            }
        }

        if(v==mBtn_submitLoanCode){
            if(mEt_loanCode.getText().toString().length()>0){
                getDetailsByOfcCode(APILinks.GET_ADMIN_LOAN_DETAILS_BY_OFC_CODE+""+"&LoanCode="+mEt_loanCode.getText().toString()+"&OfficeID="+""+"&FDate="+mStr_fromDate+"&TDate="+mStr_toDate);
            }else{
                mEt_loanCode.setError("Enter Loan Code");
                mEt_loanCode.requestFocus();
            }
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Loan Payment Report");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AdminLoanPaymentReportActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminLoanPaymentReportActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
