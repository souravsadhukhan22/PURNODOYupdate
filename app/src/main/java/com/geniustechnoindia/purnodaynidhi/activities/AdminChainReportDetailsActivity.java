package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminChainReportDetails;
import com.geniustechnoindia.purnodaynidhi.model.ChainReportDetailsSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminChainReportDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtn_fromDate, mBtn_toDate,mBtn_submit;
    private Spinner mSp_spinner;

    private RecyclerView mRv_chain;

    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private TextView mEt_username;

    private DatePickerDialog datepick_fromDate, datepick_toDate;
    private Calendar calendar;
    private LinearLayout mLl_detailsHead,mLl_rvRoot;

    private int intFromDate=0;
    private int intTodate=0;

    private String mStr_fromDate = "", mStr_toDate = "",mStr_itemSelected="TEST";
    private String[] mStrArr = {"Self", "Team"};

    private RadioButton mRadio_self,mRadio_team;
    private RadioGroup mRadioGr;

    AdapterAdminChainReportDetails adapterArrangerChainReportDetails;
    ArrayList<ChainReportDetailsSetGet> arrayList = new ArrayList<>();
    ChainReportDetailsSetGet chainReportDetailsSetGet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chain_report_details);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, mStrArr);
        //mSp_spinner.setAdapter(arrayAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_chain.setLayoutManager(linearLayoutManager);

        mRadioGr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mRadio_self.isChecked()){
                    mStr_itemSelected="Self";
                }
                if(mRadio_team.isChecked()){
                    mStr_itemSelected="Team";
                }
            }
        });

    }

    private void setViewReferences() {
        mBtn_fromDate = findViewById(R.id.btn_activity_admin_chain_report_details_from_date);
        mBtn_toDate = findViewById(R.id.btn_activity_admin_chain_report_details_to_date);
        mRv_chain = findViewById(R.id.rv_activity_admin_chain_report_details);
        mBtn_submit=findViewById(R.id.btn_activity_admin_chain_report_details_submit);
        mLl_detailsHead=findViewById(R.id.ll_activity_chain_report_details_rv_toor_deading);
        mLl_rvRoot=findViewById(R.id.ll_arr_chain_report_rv);

        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);

        mRadio_self=findViewById(R.id.radio_admin_chain_report_details_self);
        mRadio_team=findViewById(R.id.radio_admin_chain_report_details_team);
        mRadioGr=findViewById(R.id.radio_group_admin_chain_report_details);

        mEt_username=findViewById(R.id.et_admin_chain_report_username);

    }

    private void bindEventHandlers() {
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
    }

    private void setUpToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Chain Report");
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_fromDate) {
            calendar = Calendar.getInstance();
            int curr_from_year = calendar.get(Calendar.YEAR);
            int curr_from_month = calendar.get(Calendar.MONTH);
            int curr_from_day = calendar.get(Calendar.DAY_OF_MONTH);

            datepick_fromDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_fromDate = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                    mBtn_fromDate.setText(String.valueOf(dayOfMonth)+":"+ String.valueOf(month)+":"+ String.valueOf(year));
                    intFromDate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, curr_from_year, curr_from_month, curr_from_day);
            datepick_fromDate.show();
        }

        if (v == mBtn_toDate) {
            calendar = Calendar.getInstance();
            int curr_to_year = calendar.get(Calendar.YEAR);
            int curr_to_month = calendar.get(Calendar.MONTH);
            int curr_to_day = calendar.get(Calendar.DAY_OF_MONTH);

            datepick_toDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_toDate = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                    mBtn_toDate.setText(String.valueOf(dayOfMonth)+":"+ String.valueOf(month)+":"+ String.valueOf(year));
                    intTodate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));

                }
            }, curr_to_year, curr_to_month, curr_to_day);
            datepick_toDate.show();
        }
        if(v==mBtn_submit){
            arrayList.clear();
///////////////////////////
            /*mSp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mStr_itemSelected=mSp_spinner.getSelectedItem().toString();
                    String s=mStr_itemSelected;
                    Toast.makeText(ArrangerChainReportDetailsActivity.this, mStr_itemSelected, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });*/
//////////////////////////////////////////
            if(intFromDate!=0){
                if(intTodate!=0){
                    if(mEt_username.getText().toString().trim().length()>0){
                    if(!mStr_itemSelected.equals("TEST")){
                        getDetails(APILinks.ARRANGER_TEAM_CHAIN_DETAILS+mStr_itemSelected+"&aCode="+ mEt_username.getText().toString().trim() +"&fromDate="+intFromDate+"&toDate="+intTodate);
                    }else{
                        Toast.makeText(this, "Select Self or Team", Toast.LENGTH_SHORT).show();
                    }
                }else{
                        Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();}
                }else{
                    Toast.makeText(this, "Enter To Date", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Enter From Date", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getDetails(String url) {
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        mLl_detailsHead.setVisibility(View.VISIBLE);
                        mLl_rvRoot.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            chainReportDetailsSetGet = new ChainReportDetailsSetGet();
                            chainReportDetailsSetGet.setArrangerCode(jsonObject.getString("ArrangerCode"));
                            chainReportDetailsSetGet.setArrangerName(jsonObject.getString("ArrangerName"));
                            chainReportDetailsSetGet.setRenewalAmount(String.valueOf(jsonObject.getInt("RenewalAmount")));
                            chainReportDetailsSetGet.setSlNo(String.valueOf(i+1));
                            arrayList.add(chainReportDetailsSetGet);
                        }
                        adapterArrangerChainReportDetails = new AdapterAdminChainReportDetails(AdminChainReportDetailsActivity.this, arrayList);
                        mRv_chain.setAdapter(adapterArrangerChainReportDetails);
                    }else{
                        Toast.makeText(AdminChainReportDetailsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(AdminChainReportDetailsActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminChainReportDetailsActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}


