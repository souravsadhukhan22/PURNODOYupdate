package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminCashSheet;
import com.geniustechnoindia.purnodaynidhi.bean.AdminInfoStaticData;
import com.geniustechnoindia.purnodaynidhi.model.AdminCashSheetSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminCashSheetActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Button mBtn_fromDate,mBtn_toDate,mBtn_submit;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mInt_currDay=0,mInt_currMonth=0,mInt_currYear=0;
    private String mStr_fromDate="",mStr_toDate="";
    private RecyclerView mRv_cashSheet;
    private AdapterAdminCashSheet adapter;
    private AdminCashSheetSetGet setGet;
    private ArrayList<AdminCashSheetSetGet> arrayList;

    private TextView mTv_openingBalance,mTv_closingBalance,mTv_receivedTotalAmt,mTv_paymentTotalAmt;
    private Double mDouble_totalPaid=0.0,mDouble_totalReceived=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cash_sheet);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
        layoutManagers();
        adapters();

    }

    private void layoutManagers(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_cashSheet.setLayoutManager(linearLayoutManager);
    }

    private void adapters(){
        arrayList=new ArrayList<>();
        adapter=new AdapterAdminCashSheet(AdminCashSheetActivity.this,arrayList);
        mRv_cashSheet.setAdapter(adapter);
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mBtn_fromDate=findViewById(R.id.btn_activity_admin_cash_sheet_from_date);
        mBtn_toDate=findViewById(R.id.btn_activity_admin_cash_sheet_to_date);
        mBtn_submit=findViewById(R.id.btn_activity_admin_cash_sheet_submit);
        mRv_cashSheet=findViewById(R.id.rv_activity_admin_cash_sheet_details);
        mTv_openingBalance=findViewById(R.id.tv_activity_admin_cash_sheet_opening_balance);
        mTv_closingBalance=findViewById(R.id.tv_activity_admin_cash_sheet_closing_balance);

        mTv_receivedTotalAmt=findViewById(R.id.tv_activity_admin_cash_sheet_received_total_amt);
        mTv_paymentTotalAmt=findViewById(R.id.tv_activity_admin_cash_sheet_payment_total_amt);

    }

    private void bindEventHandlers(){
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
            if(!mStr_fromDate.equals("") && !mStr_toDate.equals("")){
                getCashSheetDetails(APILinks.GET_ADMIN_CASH_SHEET_DETAILS+ AdminInfoStaticData.getAdminOfcId() +"&FromDate="+mStr_fromDate+"&ToDate="+mStr_toDate);
            }else{
                Toast.makeText(this, "Select from date and to date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCashSheetDetails(String url){
        arrayList.clear();
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        JSONObject jsonObject=response.getJSONObject(0);
                        mTv_openingBalance.setText(String.valueOf(jsonObject.getDouble("IAmount")));
                        jsonObject=response.getJSONObject(response.length()-1);
                        mTv_closingBalance.setText(String.valueOf(jsonObject.getDouble("EAmount")));
                        for(int i=1;i<response.length()-1;i++){
                            setGet=new AdminCashSheetSetGet();
                            jsonObject=response.getJSONObject(i);
                            setGet.setReceivedName(jsonObject.getString("IName"));
                            setGet.setReceivedAmt(jsonObject.getDouble("IAmount"));
                            setGet.setPaymentName(jsonObject.getString("EName"));
                            setGet.setPaymentAmt(jsonObject.getDouble("EAmount"));
                            mDouble_totalReceived=mDouble_totalReceived+jsonObject.getDouble("IAmount");
                            mDouble_totalPaid=mDouble_totalPaid+jsonObject.getDouble("EAmount");
                            arrayList.add(setGet);
                        }
                        adapter.notifyDataSetChanged();
                        mTv_receivedTotalAmt.setText(String.valueOf(mDouble_totalReceived));
                        mTv_paymentTotalAmt.setText(String.valueOf(mDouble_totalPaid));

                    }else{
                        Toast.makeText(AdminCashSheetActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Cash Sheet");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AdminCashSheetActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminCashSheetActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
