package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminBankTrans;
import com.geniustechnoindia.purnodaynidhi.bean.AdminInfoStaticData;
import com.geniustechnoindia.purnodaynidhi.model.AdminBankTransSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminBankTransReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private Spinner mSp_accList;
    private Button mBtn_fromDate,mBtn_toDate,mBtn_submit;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mInt_currDay=0,mInt_currMonth=0,mInt_currYear=0;
    private String mStr_fromDate="",mStr_toDate="";
    private String mStr_selectedAccCode="",mStr_selectedUCode="";

    private ArrayList<String> arrayList_acc=new ArrayList<>();
    private ArrayList<String> arrayList_uCode=new ArrayList<>();

    private AdapterAdminBankTrans adapter_bankTrans;
    private AdminBankTransSetGet setGet;
    private ArrayList<AdminBankTransSetGet> arrayList_statement;
    private RecyclerView mRv_bankTrans;

    private ArrayList<String> arrayList_ofcName=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bank_trans_report);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
        layoutManagers();
        adapters();

        arrayList_acc.add("");
        arrayList_uCode.add("");

        getAccUserWise(APILinks.GET_ADMIN_ACC_USER_WISE + AdminInfoStaticData.getAdminId());
        getOffcList(APILinks.GET_OFC_LIST);

        mSp_accList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    mStr_selectedAccCode=mSp_accList.getSelectedItem().toString();
                    mStr_selectedUCode=arrayList_uCode.get(position).toString();
                    getBankTransReport(APILinks.GET_ADMIN_BANK_TRANS_REPORT+arrayList_ofcName.get(0).toString()+"&Acno="+mStr_selectedUCode+"&Fdate="+mStr_fromDate+"&Tdate="+mStr_toDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void layoutManagers(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_bankTrans.setLayoutManager(linearLayoutManager);
    }

    private void adapters(){
        arrayList_statement =new ArrayList<>();
        adapter_bankTrans =new AdapterAdminBankTrans(AdminBankTransReportActivity.this, arrayList_statement);
        mRv_bankTrans.setAdapter(adapter_bankTrans);
    }

    private void getBankTransReport(String url){
        arrayList_statement.clear();
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            setGet=new AdminBankTransSetGet();
                            setGet.setDate(jsonObject.getString("VDATE"));
                            setGet.setDebit(String.valueOf(jsonObject.getInt("DEBIT")));
                            setGet.setCredit(String.valueOf(jsonObject.getInt("CREDIT")));
                            setGet.setNarration(jsonObject.getString("NARRATION"));

                            arrayList_statement.add(setGet);
                        }
                        adapter_bankTrans.notifyDataSetChanged();
                    }else{
                        Toast.makeText(AdminBankTransReportActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void getOffcList(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            arrayList_ofcName.add(jsonObject.getString("OfficeID"));
                        }
                    }else{
                        Toast.makeText(AdminBankTransReportActivity.this, "No Office list found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void getAccUserWise(String ur){
        new GetDataParserArray(this, ur, true, response -> {
            try{
                if(response.length()>0){
                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject=response.getJSONObject(i);
                        if(jsonObject.getString("BOC").equals("B")){
                            arrayList_acc.add(jsonObject.getString("Name"));
                            arrayList_uCode.add(String.valueOf(jsonObject.getInt("UCode")));
                        }
                    }
                }else{
                    Toast.makeText(AdminBankTransReportActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
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
                if(arrayList_acc.size()>1){
                    ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.spinner_hint_one,arrayList_acc);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_hint);
                    mSp_accList.setAdapter(arrayAdapter);
                }
            }else{
                Toast.makeText(this, "Select From date and To date", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mSp_accList=findViewById(R.id.sp_activity_admin_bank_trans_acc_list);
        mBtn_submit=findViewById(R.id.btn_activity_admin_bank_trans_submit);
        mBtn_fromDate=findViewById(R.id.btn_activity_admin_bank_trans_from_date);
        mBtn_toDate=findViewById(R.id.btn_activity_admin_bank_trans_to_date);
        mRv_bankTrans=findViewById(R.id.rv_activity_admin_bank_trans_details);
    }

    private void bindEventHandlers(){
        mBtn_submit.setOnClickListener(this);
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Bank Trans Report");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AdminBankTransReportActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminBankTransReportActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
