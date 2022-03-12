package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.ActiveSearchLoanDetailsSetGet;
import com.geniustechnoindia.purnodaynidhi.model.ActiveSearchPolicyDetailsSetGet;
import com.geniustechnoindia.purnodaynidhi.model.ActiveSearchSBDetailsSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrangerActiveMemberSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private EditText mEt_memberCode;
    private Button mBtn_submit;

    private Spinner mSp_savings;
    private ActiveSearchSBDetailsSetGet savingsSetGet;
    private ArrayList<ActiveSearchSBDetailsSetGet> arrayListSbDetails;
    private TextView mTv_accCode, mTv_name, mTv_type;

    private Spinner mSp_loan;
    private ActiveSearchLoanDetailsSetGet loanSetGet;
    private ArrayList<ActiveSearchLoanDetailsSetGet> arrayListLoanDetails;
    private TextView mTv_loanName, mTv_loanTerm, mTv_loanEMIMode, mTv_loanEMIAmt;

    private Spinner mSp_policy;
    private ActiveSearchPolicyDetailsSetGet policySetGet;
    private ArrayList<ActiveSearchPolicyDetailsSetGet> arrayListPolicyDetails;
    private TextView mTv_policyName, mTv_policyPlan, mTv_policyTerm, mTv_policymode, mTv_policyamt;


    private LinearLayout mLl_loanRoot;
    private ImageView iv_loanDetails;
    private LinearLayout mLl_policyRoot;
    private ImageView iv_policyDetails;
    private LinearLayout mLl_savingsDetails;
    private ImageView iv_savingsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_active_member_search);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();

        arrayListSbDetails = new ArrayList<>();
        arrayListLoanDetails = new ArrayList<>();
        arrayListPolicyDetails = new ArrayList<>();

        mSp_savings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTv_accCode.setText(arrayListSbDetails.get(position).getAccCode());
                mTv_type.setText(arrayListSbDetails.get(position).getAccType());
                mTv_name.setText(arrayListSbDetails.get(position).getAppliName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSp_loan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTv_loanName.setText(arrayListLoanDetails.get(position).getName());
                mTv_loanTerm.setText(arrayListLoanDetails.get(position).getLoanTerm());
                mTv_loanEMIMode.setText(arrayListLoanDetails.get(position).getEmiMode());
                mTv_loanEMIAmt.setText(arrayListLoanDetails.get(position).getEmiAmt());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSp_policy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTv_policyName.setText(arrayListPolicyDetails.get(position).getName());
                mTv_policyPlan.setText(arrayListPolicyDetails.get(position).getPlan());
                mTv_policyTerm.setText(arrayListPolicyDetails.get(position).getTerm());
                mTv_policymode.setText(arrayListPolicyDetails.get(position).getMode());
                mTv_policyamt.setText(arrayListPolicyDetails.get(position).getAmount());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mEt_memberCode = findViewById(R.id.et_activity_active_member_search_member_code);
        mBtn_submit = findViewById(R.id.btn_activity_active_member_search_submit);
        mSp_savings = findViewById(R.id.sp_activity_active_member_search_savings);
        mSp_loan = findViewById(R.id.sp_activity_active_member_search_loan);
        mSp_policy = findViewById(R.id.sp_activity_active_member_search_policy);

        mTv_accCode = findViewById(R.id.tv_activity_active_member_search_acc_code);
        mTv_name = findViewById(R.id.tv_activity_active_member_search_name);
        mTv_type = findViewById(R.id.tv_activity_active_member_search_type);

        mTv_loanName = findViewById(R.id.tv_activity_active_member_search_loan_name);
        mTv_loanTerm = findViewById(R.id.tv_activity_active_member_search_loan_term);
        mTv_loanEMIMode = findViewById(R.id.tv_activity_active_member_search_loan_emi_mode);
        mTv_loanEMIAmt = findViewById(R.id.tv_activity_active_member_search_loan_emi_amt);

        mTv_policyName = findViewById(R.id.tv_active_member_search_policy_name);
        mTv_policyPlan = findViewById(R.id.tv_active_member_search_policy_plan);
        mTv_policyTerm = findViewById(R.id.tv_active_member_search_policy_term);
        mTv_policymode = findViewById(R.id.tv_active_member_search_policy_mode);
        mTv_policyamt = findViewById(R.id.tv_active_member_search_policy_amt);

        mLl_loanRoot = findViewById(R.id.ll_active_member_search_loan_details);
        iv_loanDetails = findViewById(R.id.iv_active_member_search_loan_details);
        mLl_policyRoot = findViewById(R.id.ll_active_member_search_polic_details);
        iv_policyDetails = findViewById(R.id.iv_active_member_search_policy_details);
        mLl_savingsDetails = findViewById(R.id.ll_activive_member_search_savings_details);
        iv_savingsDetails = findViewById(R.id.iv_active_member_search_savings_details);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mBtn_submit) {
            checkIfMemberBlocked(mEt_memberCode.getText().toString());
        }
    }

    private void checkIfMemberBlocked(String memberCode) {
        new GetDataParserObject(ArrangerActiveMemberSearchActivity.this, APILinks.IS_MEMBER_BLOCKED + memberCode, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getBoolean("IsBlock")) {
                            Toast.makeText(ArrangerActiveMemberSearchActivity.this, "Member blocked", Toast.LENGTH_SHORT).show();
                            mLl_savingsDetails.setVisibility(View.GONE);
                            mLl_loanRoot.setVisibility(View.GONE);
                            mLl_policyRoot.setVisibility(View.GONE);
                        } else {
                            getSavingsDetails(APILinks.GET_ACTIVE_SB_ACCOUNTS + mEt_memberCode.getText().toString());
                            getloanDetails(APILinks.GET_ACTIVE_LOAN_ACCOUNTS + mEt_memberCode.getText().toString());
                            getPolicyDetails(APILinks.GET_ACTIVE_POLICY_ACCOUNTS + mEt_memberCode.getText().toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getSavingsDetails(String url) {
        new GetDataParserArray(this, url, false, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        mLl_savingsDetails.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            savingsSetGet = new ActiveSearchSBDetailsSetGet();
                            savingsSetGet.setAccCode(jsonObject.getString("AccountCode"));
                            savingsSetGet.setAccType(jsonObject.getString("AccountAccessType"));
                            savingsSetGet.setAppliName(jsonObject.getString("FirstApplicantName"));
                            arrayListSbDetails.add(savingsSetGet);
                        }
                        ArrayAdapter<ActiveSearchSBDetailsSetGet> arrayAdapter = new ArrayAdapter<ActiveSearchSBDetailsSetGet>(ArrangerActiveMemberSearchActivity.this, android.R.layout.simple_spinner_item, arrayListSbDetails);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSp_savings.setAdapter(arrayAdapter);
                    } else {
                        iv_savingsDetails.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void getloanDetails(String url) {
        new GetDataParserArray(this, url, false, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        mLl_loanRoot.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            loanSetGet = new ActiveSearchLoanDetailsSetGet();
                            loanSetGet.setLoanCode(jsonObject.getString("LoanCode"));
                            loanSetGet.setName(jsonObject.getString("HolderName"));
                            loanSetGet.setLoanTerm(jsonObject.getString("LoanTerm"));
                            loanSetGet.setEmiMode(jsonObject.getString("EMIMode"));
                            loanSetGet.setEmiAmt(jsonObject.getString("EMIAmount"));
                            arrayListLoanDetails.add(loanSetGet);
                        }
                        ArrayAdapter<ActiveSearchLoanDetailsSetGet> arrayAdapter = new ArrayAdapter<ActiveSearchLoanDetailsSetGet>(ArrangerActiveMemberSearchActivity.this, android.R.layout.simple_spinner_item, arrayListLoanDetails);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSp_loan.setAdapter(arrayAdapter);
                    } else {
                        iv_loanDetails.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPolicyDetails(String url) {
        new GetDataParserArray(this, url, false, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        mLl_policyRoot.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            policySetGet = new ActiveSearchPolicyDetailsSetGet();
                            policySetGet.setPolicyCode(jsonObject.getString("PolicyCode"));
                            policySetGet.setName(jsonObject.getString("ApplicantName"));
                            policySetGet.setPlan(jsonObject.getString("Plan"));
                            policySetGet.setTerm(jsonObject.getString("Term"));
                            policySetGet.setMode(jsonObject.getString("Mode"));
                            policySetGet.setAmount(jsonObject.getString("Amount"));
                            policySetGet.setMaturityAmt(jsonObject.getString("MaturityAmount"));

                            arrayListPolicyDetails.add(policySetGet);
                        }
                        ArrayAdapter<ActiveSearchPolicyDetailsSetGet> arrayAdapter = new ArrayAdapter<ActiveSearchPolicyDetailsSetGet>(ArrangerActiveMemberSearchActivity.this, android.R.layout.simple_spinner_item, arrayListPolicyDetails);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSp_policy.setAdapter(arrayAdapter);
                    } else {
                        iv_policyDetails.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setUpTollBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Active Services");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrangerActiveMemberSearchActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        //super.onBackPressed();
        Intent intent = new Intent(ArrangerActiveMemberSearchActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
