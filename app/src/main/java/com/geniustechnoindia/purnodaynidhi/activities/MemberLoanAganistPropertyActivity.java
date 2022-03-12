package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberLoanAganistPropertyActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private TextView mTv_mCode, mTv_mName, mTv_dob, mTv_addr, mTv_phone, mTv_email, mTv_pan;
    private EditText mEt_amount,mEt_time,mEt_propertyDetails;
    private Button mBtn_applyLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_loan_aganist_property);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        getMemberDetails(APILinks.GET_MEMBER_DETAILS_BY_MCODE+ GlobalStore.GlobalValue.getMemberCode());
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mTv_mCode = findViewById(R.id.tv_activity_member_loan_against_property_mcode);
        mTv_mName = findViewById(R.id.tv_activity_member_loan_against_property_mname);
        mTv_dob = findViewById(R.id.tv_activity_member_loan_against_property_mdob);
        mTv_addr = findViewById(R.id.tv_activity_member_loan_against_property_addr);
        mTv_phone = findViewById(R.id.tv_activity_member_loan_against_property_phone);
        mTv_email = findViewById(R.id.tv_activity_member_loan_against_property_email);
        mTv_pan = findViewById(R.id.tv_activity_member_loan_against_property_pan);
        mEt_amount=findViewById(R.id.et_activity_member_loan_aganist_property_enter_amt);
        mEt_time=findViewById(R.id.et_activity_member_loan_aganist_property_enter_time);
        mEt_propertyDetails=findViewById(R.id.et_activity_member_loan_aganist_property_enter_property_details);
        mBtn_applyLoan=findViewById(R.id.btn_activity_member_loan_aganist_property_apply);
    }

    private void bindEventHandlers() {
        mBtn_applyLoan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtn_applyLoan){
            if(mEt_amount.getText().toString().length()>0){
                if(mEt_time.getText().toString().length()>0){
                    if(mEt_propertyDetails.getText().toString().length()>0){
                        sendLoanApplicationToServer(APILinks.MEMBER_APPLY_FOR_LOAN);
                    }else{
                        mEt_propertyDetails.setError("Enter Proprty Details");
                        mEt_propertyDetails.requestFocus();
                    }
                }else{
                    mEt_time.setError("Enter Months");
                    mEt_time.requestFocus();
                }
            }else{
                mEt_amount.setError("Enter amount");
                mEt_amount.requestFocus();
            }
        }
    }


    private void sendLoanApplicationToServer(String url){
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("memberCode",mTv_mCode.getText().toString());
        hashMap.put("loanAmt",mEt_amount.getText().toString());
        hashMap.put("loanTimeForMonths",mEt_time.getText().toString());
        hashMap.put("propertyDetails",mEt_propertyDetails.getText().toString());
        hashMap.put("policyCode","");
        hashMap.put("loanTag","BY_PROPERTY");

        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if(response.getString("returnStatus").equals("Success")){
                        Toast.makeText(MemberLoanAganistPropertyActivity.this, "Loan Applied Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MemberLoanAganistPropertyActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void getMemberDetails(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    JSONObject jsonObject=response.getJSONObject(0);
                    mTv_mCode.setText(jsonObject.getString("MemberCode"));
                    mTv_mName.setText(jsonObject.getString("MemberName"));
                    mTv_dob.setText(jsonObject.getString("MemberDOB"));
                    mTv_addr.setText(jsonObject.getString("Address")+" "+jsonObject.getString("State")+" "+jsonObject.getString("Pincode"));
                    mTv_phone.setText(jsonObject.getString("Phone"));
                    mTv_email.setText(jsonObject.getString("Email"));
                    mTv_pan.setText(jsonObject.getString("PANNo"));

                }catch (Exception e){e.printStackTrace();}
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
        mTv_toolbarTitle.setText("Loan aganist Property");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberLoanAganistPropertyActivity.this, MemberDashboardActivity.class);
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
        Intent intent = new Intent(MemberLoanAganistPropertyActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}