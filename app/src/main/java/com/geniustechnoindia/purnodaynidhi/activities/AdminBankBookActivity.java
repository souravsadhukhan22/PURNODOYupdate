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
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminBankBook;
import com.geniustechnoindia.purnodaynidhi.bean.AdminInfoStaticData;
import com.geniustechnoindia.purnodaynidhi.model.AdminBankBookSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminBankBookActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Button mBtn_fromDate, mBtn_toDate, mBtn_submit;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mInt_currDay=0,mInt_currMonth=0,mInt_currYear=0;
    private String mStr_currDay="",mStr_currMonth="",mStr_currYear="";
    private String mStr_fromDate="",mStr_toDate="";
    private RecyclerView mRv_bankBook;

    private AdapterAdminBankBook adapterAdminBankBook;
    private AdminBankBookSetGet setGet;
    private ArrayList<AdminBankBookSetGet> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bank_book);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
        layoutManagers();
        adapters();
    }

    private void layoutManagers(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_bankBook.setLayoutManager(linearLayoutManager);
    }

    private void adapters(){
        arrayList=new ArrayList<>();
        adapterAdminBankBook=new AdapterAdminBankBook(AdminBankBookActivity.this,arrayList);
        mRv_bankBook.setAdapter(adapterAdminBankBook);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mBtn_fromDate = findViewById(R.id.btn_activity_admin_bank_book_from_date);
        mBtn_toDate = findViewById(R.id.btn_activity_admin_bank_book_to_date);
        mBtn_submit = findViewById(R.id.btn_activity_admin_bank_book_submit);
        mRv_bankBook=findViewById(R.id.rv_activity_admin_bank_book_details);
    }

    private void bindEventHandlers() {
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
                arrayList.clear();
                getCashBookDetails(APILinks.GET_ADMIN_CASH_BOOK_DETAILS+ AdminInfoStaticData.getAdminUserName()+"&FDate="+mStr_fromDate+"&TDate="+mStr_toDate);
            }else{
                Toast.makeText(this, "Select from date and to date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCashBookDetails(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            setGet=new AdminBankBookSetGet();
                            JSONObject jsonObject=response.getJSONObject(i);
                            setGet.setIfcID(jsonObject.getString("OfficeId"));
                            setGet.setbName(jsonObject.getString("bname"));
                            setGet.settDate(jsonObject.getString("tdate"));
                            setGet.setBankCode(String.valueOf(jsonObject.getInt("BankCode")));
                            setGet.setBankName(jsonObject.getString("BankName"));
                            setGet.setPurCode(String.valueOf(jsonObject.getInt("Purcode")));
                            setGet.setNarration(jsonObject.getString("Narration"));
                            setGet.setDebit(String.valueOf(jsonObject.getInt("Debit")));
                            setGet.setCredit(String.valueOf(jsonObject.getInt("Credit")));
                            setGet.setrNo(String.valueOf(jsonObject.getInt("RNo")));

                            arrayList.add(setGet);
                        }
                        adapterAdminBankBook.notifyDataSetChanged();
                    }else{
                        Toast.makeText(AdminBankBookActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
        mTv_toolbarTitle.setText("Bank Book");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AdminBankBookActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminBankBookActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
