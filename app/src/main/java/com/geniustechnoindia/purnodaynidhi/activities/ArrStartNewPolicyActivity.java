package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.geniustechnoindia.purnodaynidhi.model.RelationSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ArrStartNewPolicyActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText mTie_memberCode;
    private EditText mTv_memberName, mTv_gurdianName, mTv_relation, mTv_dob, mTv_address, mTv_state, mTv_pin, mTv_phone;
    private EditText mTv_planAmount, mTv_depositAmount, mTv_maturityAmount, mTv_bonusAmount, mTv_misReturn, mTv_regAmount;
    private EditText mTv_payByCashAmount, mTv_remarks;
    private Button mBtn_submit;
    private Spinner mSp_planCode, mSp_planTable, mSp_planMode, mSp_misMode;
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private TextInputEditText mEt_term;
    private TextView mTv_selectInvestmentDate, mTv_maturityDate;
    private Button mBtn_calculateMaturity;

    // Plan code spinner
    private ArrayList<PlanCodeSetGet> arrayListPlanCode;
    private PlanCodeSetGet planCodeSetGet;

    private ArrayList<PlanTableSetGet> arrayListPlanTable;
    private PlanTableSetGet planTableSetGet;

    private TextView mTv_planMode;
    private TextView mTv_misMode;
    private Button mBtn_search;
    private Button mBtn_calculate;

    private static int sterm, mterm = 0, minAmount, roi;
    private static double mAmount;

    private TextInputEditText mTie_nomineeMemberCode, mTie_nomineeMemberName;
    private TextView mTv_nomineeDob;
    private Spinner mSp_nomineeRelation;

    DatePickerDialog datePickerDialog;
    private int mYear, mMonth, mDay;
    private String nomineeDOB = "";
    private String investmentDate = "";

    private String modeFlag = "";

    private RelationSetGet relationSetGet;
    private ArrayList<RelationSetGet> arrayListRelation;

    private int maturityDateInt = 0;


    private TextInputEditText mTie_secondApplicantName;
    private TextView mTv_secondApplicantDob;
    private Spinner mSp_secondApplicantRelation;
    private String secondApplicantDob = "";

    private String nomineeRelation = "";
    private String secondApplicantRelation = "";

    private String sTable = "";

    private String planCode = "";
    private String pTable = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_start_new_policy);

        setViewReferences();
        disableEdittextEdit();
        bindEventHandlers();
        setUpToolbar();

        // Spinner plan code

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


        arrayListRelation = new ArrayList<>();
        getNomineeRelation(APILinks.GET_RELATION);

        mSp_nomineeRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListRelation != null) {
                    if (arrayListRelation.size() > 1) {
                        if (position > 0) {
                            nomineeRelation = arrayListRelation.get(position).getRelationName();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSp_secondApplicantRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListRelation != null) {
                    if (arrayListRelation.size() > 1) {
                        if (position > 0) {
                            secondApplicantRelation = arrayListRelation.get(position).getRelationName();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getNomineeRelation(String url) {
        new GetDataParserArray(ArrStartNewPolicyActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            relationSetGet = new RelationSetGet();
                            arrayListRelation.add(relationSetGet);
                            for (int i = 0; i < response.length(); i++) {
                                relationSetGet = new RelationSetGet();
                                JSONObject jsonObject = response.getJSONObject(i);
                                relationSetGet.setRelationName(jsonObject.getString("RelationName"));
                                relationSetGet.setRelationId(jsonObject.getInt("RelationId"));
                                arrayListRelation.add(relationSetGet);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter arrayAdapterRelation = new ArrayAdapter(ArrStartNewPolicyActivity.this, R.layout.item_select_relation, arrayListRelation);
                    arrayAdapterRelation.setDropDownViewResource(R.layout.item_spinner);
                    //arrayAdapterPlanCode.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    mSp_nomineeRelation.setAdapter(arrayAdapterRelation);

                    ArrayAdapter arrayAdapterSecondApplicantRelation = new ArrayAdapter(ArrStartNewPolicyActivity.this, R.layout.item_select_relation, arrayListRelation);
                    arrayAdapterSecondApplicantRelation.setDropDownViewResource(R.layout.item_spinner);
                    //arrayAdapterPlanCode.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    mSp_secondApplicantRelation.setAdapter(arrayAdapterSecondApplicantRelation);
                }
            }
        });
    }

    private void getPlanTable(String url) {
        new GetDataParserArray(ArrStartNewPolicyActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    arrayListPlanTable.clear();
                    if (response.length() > 0) {
                        planTableSetGet = new PlanTableSetGet();
                        /*planCodeSetGet.setSName("");
                        planCodeSetGet.setAFlag("");
                        planCodeSetGet.setFullName("");
                        planCodeSetGet.setPrefix("");
                        planCodeSetGet.setModeFlag("");*/
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
                ArrayAdapter arrayAdapterPlanTable = new ArrayAdapter(ArrStartNewPolicyActivity.this, R.layout.item_select_plan_code, arrayListPlanTable);
                arrayAdapterPlanTable.setDropDownViewResource(R.layout.item_spinner);
                //arrayAdapterPlanCode.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                mSp_planTable.setAdapter(arrayAdapterPlanTable);
            }

        });
    }

    private void getPlanCode(String url) {
        new GetDataParserArray(ArrStartNewPolicyActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
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
                ArrayAdapter arrayAdapterPlanCode = new ArrayAdapter(ArrStartNewPolicyActivity.this, R.layout.item_select_plan_code, arrayListPlanCode);
                arrayAdapterPlanCode.setDropDownViewResource(R.layout.item_spinner);
                //arrayAdapterPlanCode.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                mSp_planCode.setAdapter(arrayAdapterPlanCode);
            }
        });
    }

    private void setViewReferences() {
        mTie_memberCode = findViewById(R.id.et_activity_new_policy_create_member_code);
        mTv_memberName = findViewById(R.id.tv_activity_new_policy_create_member_name);
        mTv_gurdianName = findViewById(R.id.tv_activity_new_policy_create_gurdian_name);
        mTv_relation = findViewById(R.id.tv_activity_new_policy_create_relation);
        mTv_dob = findViewById(R.id.tv_activity_new_policy_create_dob);
        mTv_address = findViewById(R.id.tv_activity_new_policy_create_address);
        mTv_state = findViewById(R.id.tv_activity_new_policy_create_state);
        mTv_pin = findViewById(R.id.tv_activity_new_policy_create_pin);
        mTv_phone = findViewById(R.id.tv_activity_new_policy_create_phone);
        mTv_planAmount = findViewById(R.id.tv_activity_new_policy_create_plan_amount);
        mTv_depositAmount = findViewById(R.id.tv_activity_new_policy_create_deposit_amount);
        mTv_maturityAmount = findViewById(R.id.tv_activity_new_policy_create_maturity_amount);
        mTv_bonusAmount = findViewById(R.id.tv_activity_new_policy_create_bonus_amount);
        mTv_misReturn = findViewById(R.id.tv_activity_new_policy_create_mis_return);
        mTv_regAmount = findViewById(R.id.tv_activity_new_policy_create_reg_amount);
        mTv_payByCashAmount = findViewById(R.id.tv_activity_new_policy_create_pay_by_cash_amount);
        mTv_remarks = findViewById(R.id.tv_activity_new_policy_create_pay_by_cash_remarks);
        mBtn_submit = findViewById(R.id.btn_activity_new_policy_create_submit);
        mTv_misMode = findViewById(R.id.tv_activity_new_policy_create_mis_mode);
        mBtn_search = findViewById(R.id.btn_activity_new_policy_create_search);
        mBtn_calculate = findViewById(R.id.btn_activity_new_policy_create_calculate);
        mEt_term = findViewById(R.id.tv_activity_new_policy_create_plan_term);
        mTie_nomineeMemberCode = findViewById(R.id.tv_activity_new_policy_create_nominee_code);
        mTie_nomineeMemberName = findViewById(R.id.tv_activity_new_policy_create_nominee_name);
        mTv_nomineeDob = findViewById(R.id.tv_activity_new_policy_create_nominee_dob);
        mSp_nomineeRelation = findViewById(R.id.sp_activity_new_policy_create_nominee_relation);
        mBtn_calculateMaturity = findViewById(R.id.btn_activity_new_policy_create_calculate_maturity_date);
        mTv_selectInvestmentDate = findViewById(R.id.tv_activity_new_policy_create_select_investment_date);
        mTv_maturityDate = findViewById(R.id.tv_activity_new_policy_create_maturity_date);

        mTie_secondApplicantName = findViewById(R.id.tv_activity_new_policy_create_second_applicant_name);
        mTv_secondApplicantDob = findViewById(R.id.tv_activity_new_policy_create_second_applicant_dob);
        mSp_secondApplicantRelation = findViewById(R.id.sp_activity_new_policy_create_second_applicant_relation);

        mTv_planMode = findViewById(R.id.tv_activity_new_policy_create_mode);

        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mSp_planCode = findViewById(R.id.spinner_activity_new_policy_create_plan_code);
        mSp_planTable = findViewById(R.id.spinner_activity_new_policy_create_plan_table);
        mSp_planMode = findViewById(R.id.spinner_activity_new_policy_create_plan_mode);
        mSp_misMode = findViewById(R.id.spinner_activity_new_policy_create_mis_mode);
    }

    private void disableEdittextEdit() {
        mTv_memberName.setEnabled(false);
        mTv_gurdianName.setEnabled(false);
        mTv_relation.setEnabled(false);
        mTv_dob.setEnabled(false);
        mTv_address.setEnabled(false);
        mTv_state.setEnabled(false);
        mTv_pin.setEnabled(false);
        mTv_phone.setEnabled(false);
        //mTv_planAmount.setEnabled(false);
        mTv_depositAmount.setEnabled(false);
        mTv_maturityAmount.setEnabled(false);
        mTv_bonusAmount.setEnabled(false);
        mTv_misReturn.setEnabled(false);
        //mTv_regAmount.setEnabled(false);
        mTv_payByCashAmount.setEnabled(false);
        //mTv_remarks.setEnabled(false);
        mEt_term.setEnabled(false);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
        mBtn_search.setOnClickListener(this);
        mBtn_calculate.setOnClickListener(this);
        mTv_nomineeDob.setOnClickListener(this);
        mTv_selectInvestmentDate.setOnClickListener(this);
        mBtn_calculateMaturity.setOnClickListener(this);
        mTv_secondApplicantDob.setOnClickListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Policy Create");
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_submit) {
            if (mTie_memberCode.getText().toString().trim().length() > 0) {
                if (!planCode.equals("")) {
                    if (mterm != 0 || !modeFlag.equals("") || !sTable.equals("")) {
                        if (mTv_planMode.getText().toString().trim().length() > 0) {
                            if (mTv_planAmount.getText().toString().trim().length() > 0) {
                                if (mEt_term.getText().toString().trim().length() > 0) {
                                    if (mTv_depositAmount.getText().toString().trim().length() > 0) {
                                        if (mTv_maturityAmount.getText().toString().trim().length() > 0) {
                                            if (!investmentDate.equals("")) {
                                                if (maturityDateInt != 0) {
                                                    if (mTv_payByCashAmount.getText().toString().trim().length() > 0) {
                                                        createPolicy(APILinks.CREATE_POLICY);
                                                    } else {
                                                        Toast.makeText(this, "Please reload the page", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    mTv_selectInvestmentDate.performLongClick();
                                                    Toast.makeText(this, "Select investment date", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                mTv_selectInvestmentDate.performLongClick();
                                                Toast.makeText(this, "Select investment date", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(this, "Click calculate again", Toast.LENGTH_SHORT).show();
                                            mEt_term.requestFocus();
                                        }


                                    } else {
                                        Toast.makeText(this, "Click calculate again", Toast.LENGTH_SHORT).show();
                                        mEt_term.requestFocus();
                                    }
                                } else {
                                    Toast.makeText(this, "Click calculate again", Toast.LENGTH_SHORT).show();
                                    mEt_term.requestFocus();
                                }
                            } else {
                                mTv_planAmount.setError("Enter amount");
                                mTv_planAmount.requestFocus();
                            }

                        } else {
                            Toast.makeText(this, "Select plan or code", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Select plan", Toast.LENGTH_SHORT).show();
                        mSp_planTable.performClick();
                    }
                } else {
                    mSp_planCode.performClick();
                    Toast.makeText(this, "Select plan code", Toast.LENGTH_SHORT).show();
                }
            } else {
                mTie_memberCode.setError("Enter Member code");
                mTie_memberCode.requestFocus();
            }


        } else if (v == mBtn_search) {
            if (mTie_memberCode.getText().toString().trim().length() > 0) {
                // TODO get member details
                getMemberDetails(APILinks.GET_MEMBER_DETAILS_POLICY_CREATE + mTie_memberCode.getText().toString());
            } else {
                mTie_memberCode.setError("Please Enter Member Code");
            }
        } else if (v == mBtn_calculate) {
            if (mTv_planAmount.getText().toString().trim().length() > 0) {
                // TODO Calculate deposit amount
                mTv_payByCashAmount.setText(mTv_planAmount.getText().toString());
                mEt_term.setText(String.valueOf(sterm));
                mTv_depositAmount.setText(String.valueOf(sterm * Integer.parseInt(mTv_planAmount.getText().toString())));
                mTv_maturityAmount.setText(String.valueOf(mAmount * Double.parseDouble(mTv_depositAmount.getText().toString())));
            } else {
                mTv_planAmount.setError("Enter amount");
                mTv_planAmount.requestFocus();
            }
        } else if (v == mTv_nomineeDob) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(ArrStartNewPolicyActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mTv_nomineeDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    nomineeDOB = Integer.toString(year) + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();

        } else if (v == mTv_selectInvestmentDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(ArrStartNewPolicyActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mTv_selectInvestmentDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    investmentDate = Integer.toString(year) + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (v == mBtn_calculateMaturity) {
            if (mTv_selectInvestmentDate.getText().toString().trim().length() > 0) {
                if (!investmentDate.equals("")) {
                    // todo get maturity date -----------------------------------------------------
                    if (!modeFlag.equals("") && mterm != 0) {
                        getMaturityDate(APILinks.GET_MATURITY_DATE + "mode=" + modeFlag + "&dateofcom=" + investmentDate + "&term=" + mterm);
                    } else {
                        mSp_planTable.performClick();
                    }

                } else {
                    mTv_selectInvestmentDate.performLongClick();
                    Toast.makeText(this, "Select investment date", Toast.LENGTH_SHORT).show();
                }
            } else {
                mTv_selectInvestmentDate.performLongClick();
                Toast.makeText(this, "Select investment date", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mTv_secondApplicantDob) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(ArrStartNewPolicyActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mTv_secondApplicantDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    secondApplicantDob = Integer.toString(year) + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    private void getMaturityDate(String url) {
        new GetDataParserArray(ArrStartNewPolicyActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            maturityDateInt = jsonObject.getInt("dateint");
                            mTv_maturityDate.setText(jsonObject.getString("dateformat"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getMemberDetails(String url) {
        new GetDataParserArray(ArrStartNewPolicyActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            mTv_memberName.setText(jsonObject.getString("MemberName"));
                            mTv_gurdianName.setText(jsonObject.getString("Father"));
                            mTv_dob.setText(jsonObject.getString("MemberDOB"));
                            mTv_address.setText(jsonObject.getString("Address"));
                            mTv_state.setText(jsonObject.getString("State"));
                            mTv_pin.setText(jsonObject.getString("Pincode"));
                            mTv_phone.setText(jsonObject.getString("Phone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    private void createPolicy(String url) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("arrangerCode", GlobalStore.GlobalValue.getUserName());
        if (mTie_memberCode.getText().toString().trim().length() > 0) {
            hashMap.put("memberCode", mTie_memberCode.getText().toString());
        } else {
            hashMap.put("memberCode", "");
        }

        if (mTie_nomineeMemberCode.getText().toString().trim().length() > 0) {
            hashMap.put("nomineeCode", mTie_nomineeMemberCode.getText().toString().trim());
        } else {
            hashMap.put("nomineeCode", "");
        }
        hashMap.put("dateofcom", investmentDate);
        hashMap.put("sname", planCode);
        hashMap.put("maturityDate", String.valueOf(maturityDateInt));
        if (mTie_secondApplicantName.getText().toString().trim().length() > 0) {
            hashMap.put("secondApplicantName", mTie_secondApplicantName.getText().toString());
        } else {
            hashMap.put("secondApplicantName", "");
        }
        if (!secondApplicantDob.equals("")) {
            hashMap.put("secondApplicantDateOfBirth", secondApplicantDob);
        } else {
            hashMap.put("secondApplicantDateOfBirth", "20000101");
        }

        hashMap.put("secondApplicantRelation", secondApplicantRelation);

        if (mTie_nomineeMemberName.getText().toString().trim().length() > 0) {
            hashMap.put("nomineeName", mTie_nomineeMemberName.getText().toString());
        } else {
            hashMap.put("nomineeName", "");
        }

        if (!nomineeDOB.equals("")) {
            hashMap.put("nomineeDateOfBirth", nomineeDOB);
        } else {
            hashMap.put("nomineeDateOfBirth", "20000101");
        }
        hashMap.put("nomineeRelation", nomineeRelation);
        hashMap.put("planCode", planCode);
        hashMap.put("pTable", sTable);
        hashMap.put("term", String.valueOf(mterm));
        hashMap.put("mode", modeFlag);

        if (mTv_payByCashAmount.getText().toString().trim().length() > 0) {
            hashMap.put("amount", mTv_payByCashAmount.getText().toString());
        } else {
            hashMap.put("amount", "0");
        }
        if (mTv_depositAmount.getText().toString().trim().length() > 0) {
            hashMap.put("depositeAmount", mTv_depositAmount.getText().toString());
        } else {
            hashMap.put("depositeAmount", "0");
        }

        if (mTv_maturityAmount.getText().toString().trim().length() > 0) {
            hashMap.put("maturityAmount", mTv_maturityAmount.getText().toString());
        } else {
            hashMap.put("maturityAmount", "0");
        }

        if (mTv_regAmount.getText().toString().trim().length() > 0) {
            hashMap.put("regAmount", mTv_regAmount.getText().toString());
        } else {
            hashMap.put("regAmount", "0");
        }
        if (mTv_remarks.getText().toString().trim().length() > 0) {
            hashMap.put("remarks", mTv_remarks.getText().toString());
        } else {
            hashMap.put("remarks", "");
        }
        new PostDataParserObjectResponse(ArrStartNewPolicyActivity.this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getBoolean("status")) {
                            showDialog("Success!", "Policy has been created successfully", true);
                        } else {
                            showDialog("Failed!", "Policy creation failed", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showDialog(String title, String message, final boolean status) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (status) {
                    dialog.dismiss();
                    startActivity(new Intent(ArrStartNewPolicyActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
                } else {
                    dialog.dismiss();
                }
            }
        }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                startActivity(new Intent(ArrStartNewPolicyActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ArrStartNewPolicyActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
