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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.PlanCodeSetGet;
import com.geniustechnoindia.purnodaynidhi.model.PlanTableSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MemberInvestmentCalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private Spinner mSp_planCode,mSp_planTable;
    private ArrayList<PlanCodeSetGet> arrayListPlanCode;
    private PlanCodeSetGet planCodeSetGet;
    private ArrayList<PlanTableSetGet> arrayListPlanTable;
    private PlanTableSetGet planTableSetGet;

    private String planCode = "";
    private String pTable = "";

    private TextView mTv_planMode,mTv_misMode;
    private static int sterm, mterm = 0, minAmount, roi;
    private static double mAmount;
    private String modeFlag = "";
    private String sTable = "";

    private EditText mEt_enterDepositAmount,mEt_term,mEt_depositAmt;
    private TextView mTv_maturityAmt;
    private Button mBtn_calculate;

    private TextView mTv_selectInvestmentDate;
    private Button mBtn_getDate;
    private String investmentDate = "";
    private TextView mTv_maturityDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_investment_calculator);



        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        arrayListPlanCode = new ArrayList<>();
        arrayListPlanTable = new ArrayList<>();
        getPlanCode(APILinks.GET_PLAN_NAME_CODE);

        mSp_planCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListPlanCode.size() > 0) {
                    if (position != 0) {
                        mTv_planMode.setText(arrayListPlanCode.get(position).getModeFlag());
                        if (!arrayListPlanCode.get(position).getSName().equals("FD")) {
                            mTv_misMode.setText(arrayListPlanCode.get(position).getModeFlag());
                            planCode = arrayListPlanCode.get(position).getSName();
                        } else {
                            mTv_misMode.setText("");
                        }
                        getPlanTable(APILinks.GET_PLAN_TABLE + arrayListPlanCode.get(position).getSName());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        mSp_planTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListPlanTable.size() > 0) {
                    if (position != 0) {
                        sterm = arrayListPlanTable.get(position).getSTerm();
                        mterm = arrayListPlanTable.get(position).getMTerm();
                        minAmount = arrayListPlanTable.get(position).getMinAmount();
                        mAmount = arrayListPlanTable.get(position).getMAmt();
                        roi = arrayListPlanTable.get(position).getROI();
                        modeFlag = arrayListPlanTable.get(position).getModeFlag();
                        sTable = arrayListPlanTable.get(position).getSTable();

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mSp_planCode = findViewById(R.id.spinner_activity_member_investment_cal_plan_code);
        mSp_planTable=findViewById(R.id.spinner_activity_member_investment_cal_plan_table);
        mTv_planMode=findViewById(R.id.tv_activity_member_investment_calculator_plan_mode);
        mTv_misMode=findViewById(R.id.tv_activity_member_investment_calculator_mis_mode);
        mEt_enterDepositAmount=findViewById(R.id.et_activity_member_investment_calculator_enter_deposit_amount);
        mBtn_calculate=findViewById(R.id.btn_activity_member_investment_calculator_calculate);
        mEt_term=findViewById(R.id.et_activity_member_investment_calculator_term);
        mEt_depositAmt = findViewById(R.id.et_activity_member_investment_calculator_deposit_amount);
        mTv_maturityAmt = findViewById(R.id.et_activity_member_investment_calculator_maturity_amount);
        mTv_selectInvestmentDate = findViewById(R.id.tv_activity_member_investment_calculator_investment_date);
        mBtn_getDate=findViewById(R.id.btn_activity_member_investment_calculator_get_date);
        mTv_maturityDate=findViewById(R.id.tv_activity_member_investment_calculator_maturity_date);
    }

    private void bindEventHandlers(){
        mBtn_calculate.setOnClickListener(this);
        mTv_selectInvestmentDate.setOnClickListener(this);
        mBtn_getDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mBtn_calculate){
            if(sTable.length()>0){
                if (mEt_enterDepositAmount.getText().toString().trim().length() > 0) {
                    //mTv_payByCashAmount.setText(mTv_planAmount.getText().toString());
                    mEt_term.setText(String.valueOf(sterm));
                    mEt_depositAmt.setText(String.valueOf(sterm * Integer.parseInt(mEt_enterDepositAmount.getText().toString())));
                    mTv_maturityAmt.setText(String.format("%.2f",mAmount * Double.parseDouble(mEt_depositAmt.getText().toString())));
                } else {
                    mEt_enterDepositAmount.setError("Enter amount");
                    mEt_enterDepositAmount.requestFocus();
                }
            }else{
                Toast.makeText(this, "Select Plan Table", Toast.LENGTH_SHORT).show();
            }


        }
        if(view == mTv_selectInvestmentDate){
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog;
            int mYear=0,mMonth=0,mDay=0;
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(MemberInvestmentCalculatorActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mTv_selectInvestmentDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    investmentDate = Integer.toString(year) + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if(view==mBtn_getDate){
            getMaturityDate(APILinks.GET_MATURITY_DATE + "mode=" + modeFlag + "&dateofcom=" + investmentDate + "&term=" + mterm);
        }
    }

    private void getMaturityDate(String url) {
        new GetDataParserArray(MemberInvestmentCalculatorActivity.this, url, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        //maturityDateInt = jsonObject.getInt("dateint");
                        mTv_maturityDate.setText(jsonObject.getString("dateformat"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getPlanCode(String url) {
        new GetDataParserArray(MemberInvestmentCalculatorActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    arrayListPlanCode.clear();
                    if (response.length() > 0) {
                        planCodeSetGet = new PlanCodeSetGet();
                        planCodeSetGet.setSName("");
                        planCodeSetGet.setAFlag("");
                        planCodeSetGet.setFullName("");
                        planCodeSetGet.setPrefix("");
                        planCodeSetGet.setModeFlag("");
                        arrayListPlanCode.add(planCodeSetGet);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                planCodeSetGet = new PlanCodeSetGet();
                                JSONObject jsonObject = response.getJSONObject(i);
                                planCodeSetGet.setSName(jsonObject.getString("SName"));
                                planCodeSetGet.setAFlag(jsonObject.getString("AFlag"));
                                planCodeSetGet.setFullName(jsonObject.getString("FullName"));
                                planCodeSetGet.setPrefix(jsonObject.getString("Prefix"));
                                planCodeSetGet.setModeFlag(jsonObject.getString("ModeFlag"));
                                arrayListPlanCode.add(planCodeSetGet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                ArrayAdapter arrayAdapterPlanCode = new ArrayAdapter(MemberInvestmentCalculatorActivity.this, R.layout.item_select_plan_code, arrayListPlanCode);
                arrayAdapterPlanCode.setDropDownViewResource(R.layout.item_spinner);
                //arrayAdapterPlanCode.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                mSp_planCode.setAdapter(arrayAdapterPlanCode);
            }
        });
    }

    private void getPlanTable(String url) {
        new GetDataParserArray(MemberInvestmentCalculatorActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    arrayListPlanTable.clear();
                    if (response.length() > 0) {
                        planTableSetGet = new PlanTableSetGet();
                        arrayListPlanTable.add(planTableSetGet);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                planTableSetGet = new PlanTableSetGet();
                                JSONObject jsonObject = response.getJSONObject(i);
                                planTableSetGet.setScheme(jsonObject.getString("Scheme"));
                                planTableSetGet.setSTable(jsonObject.getString("STable"));
                                pTable = jsonObject.getString("STable");
                                planTableSetGet.setSTerm(jsonObject.getInt("STerm"));
                                planTableSetGet.setMTerm(jsonObject.getInt("MTerm"));
                                planTableSetGet.setMinAmount(jsonObject.getInt("MinAmount"));
                                planTableSetGet.setMAmt(jsonObject.getDouble("MAmt"));
                                planTableSetGet.setMisMonthlyRent(jsonObject.getInt("MisMonthlyRent"));
                                planTableSetGet.setPrefix(jsonObject.getString("Prefix"));
                                planTableSetGet.setROI(jsonObject.getInt("ROI"));
                                planTableSetGet.setMultyOf(jsonObject.getInt("MultyOf"));
                                planTableSetGet.setModeFlag(jsonObject.getString("ModeFlag"));
                                planTableSetGet.setMlyMat(jsonObject.getInt("MlyMat"));
                                planTableSetGet.setQlyMat(jsonObject.getInt("QlyMat"));
                                planTableSetGet.setHlyMat(jsonObject.getInt("HlyMat"));
                                planTableSetGet.setYlyMat(jsonObject.getInt("YlyMat"));
                                arrayListPlanTable.add(planTableSetGet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                ArrayAdapter arrayAdapterPlanTable = new ArrayAdapter(MemberInvestmentCalculatorActivity.this, R.layout.item_select_plan_code, arrayListPlanTable);
                arrayAdapterPlanTable.setDropDownViewResource(R.layout.item_spinner);
                //arrayAdapterPlanCode.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                mSp_planTable.setAdapter(arrayAdapterPlanTable);
            }

        });
    }


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Investment Calculator");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberInvestmentCalculatorActivity.this, MemberDashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MemberInvestmentCalculatorActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

