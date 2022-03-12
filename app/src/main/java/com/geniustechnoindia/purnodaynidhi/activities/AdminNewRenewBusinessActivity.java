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
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAdminNewRenewAdapter;
import com.geniustechnoindia.purnodaynidhi.model.AdminNewRenewBusinessSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminNewRenewBusinessActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn_fromDate, mBtn_toDate, mBtn_submit;
    private Spinner mSp_business;
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private RecyclerView recyclerView;

    private DatePickerDialog datePickerDialog_fromDate;
    private DatePickerDialog datePickerDialog_toDate;
    private Calendar calendar;

    private LinearLayout mLl_newRenewBuss;

    private RadioGroup mRadioGroup;
    private RadioButton mRadio_newBusi, mRadio_reNewBusi;

    private String mStr_fromDate = "", mStr_toDate = "", mStr_itemSelected = "TEST";
    private String[] mStrArr = {"New Business", "Renew Business"};
    private int intFromDate = 0;
    private int intTodate = 0;

    private AdapterAdminNewRenewAdapter adapterArrangerNewRenewAdapter;
    private ArrayList<AdminNewRenewBusinessSetGet> arrayList = new ArrayList<>();
    AdminNewRenewBusinessSetGet arrangerNewRenewBusinessSetGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_renew_business);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, mStrArr);
        mSp_business.setAdapter(arrayAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mRadio_newBusi.isChecked()) {
                    mStr_itemSelected = "New Business";
                }
                if (mRadio_reNewBusi.isChecked()) {
                    mStr_itemSelected = "Renew Business";
                }
            }
        });

    }

    private void setViewReferences() {
        mBtn_fromDate = findViewById(R.id.btn_activity_admin_new_renew_from_date);
        mBtn_toDate = findViewById(R.id.btn_activity_admin_new_renew_to_date);
        mBtn_submit = findViewById(R.id.btn_activity_admin_new_renew_submit);
        mSp_business = findViewById(R.id.sp_activity_admin_new_renew);

        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.rv_activity_admin_new_renew);

        mRadioGroup = findViewById(R.id.radio_group_new_renew_business);
        mRadio_newBusi = findViewById(R.id.radio_button_new_renew_business_new_business);
        mRadio_reNewBusi = findViewById(R.id.radio_button_new_renew_business_renew_business);

        mLl_newRenewBuss = findViewById(R.id.ll_arr_new_renew_busi);
    }

    private void bindEventHandlers() {
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == mBtn_fromDate) {
            calendar = Calendar.getInstance();
            int current_from_year = calendar.get(Calendar.YEAR);
            int current_from_month = calendar.get(Calendar.MONTH);
            int current_from_day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog_fromDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_fromDate = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                    mBtn_fromDate.setText(String.valueOf(dayOfMonth) + ":" + String.valueOf(month) + ":" + String.valueOf(year));
                    intFromDate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, current_from_year, current_from_month, current_from_day);
            datePickerDialog_fromDate.show();
        }

        if (v == mBtn_toDate) {
            calendar = Calendar.getInstance();
            int current_to_year = calendar.get(Calendar.YEAR);
            int current_to_month = calendar.get(Calendar.MONTH);
            int current_to_day = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog_toDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_toDate = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                    mBtn_toDate.setText(String.valueOf(dayOfMonth) + ":" + String.valueOf(month) + ":" + String.valueOf(year));
                    intTodate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, current_to_year, current_to_month, current_to_day);
            datePickerDialog_toDate.show();
        }
        if (v == mBtn_submit) {
            arrayList.clear();
           /* mSp_business.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mStr_itemSelected = mSp_business.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });*/

            if (intFromDate != 0) {
                if (intTodate != 0) {
                    if (!mStr_itemSelected.equals("TEST")) {
                        try {
                            getDetails(APILinks.ARRANGER_NEW_RENEW_BUSINESS_DETAILS + "all" + "&fDate=" + intFromDate + "&tDate=" + intTodate + "&bType=" + mStr_itemSelected + "&userName=");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, "Select Business", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter To Date", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter From Date", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getDetails(String url) {
        arrayList.clear();
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        mLl_newRenewBuss.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            arrangerNewRenewBusinessSetGet = new AdminNewRenewBusinessSetGet();
                            arrangerNewRenewBusinessSetGet.setPolicyCode(jsonObject.getString("PolicyCode"));
                            arrangerNewRenewBusinessSetGet.setName(jsonObject.getString("ApplicantName"));
                            arrangerNewRenewBusinessSetGet.setTerm(jsonObject.getString("Term"));
                            arrangerNewRenewBusinessSetGet.setAmount(jsonObject.getString("Amount"));
                            arrangerNewRenewBusinessSetGet.setDepositAmt(jsonObject.getString("DepositeAmount"));
                            arrangerNewRenewBusinessSetGet.setMaturityAmt(jsonObject.getString("MaturityAmount"));

                            arrayList.add(arrangerNewRenewBusinessSetGet);
                        }

                        adapterArrangerNewRenewAdapter = new AdapterAdminNewRenewAdapter(AdminNewRenewBusinessActivity.this, arrayList);
                        recyclerView.setAdapter(adapterArrangerNewRenewAdapter);

                    } else {
                        Toast.makeText(AdminNewRenewBusinessActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(AdminNewRenewBusinessActivity.this, AdminDashboardActivity.class));
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
        startActivity(new Intent(AdminNewRenewBusinessActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}


