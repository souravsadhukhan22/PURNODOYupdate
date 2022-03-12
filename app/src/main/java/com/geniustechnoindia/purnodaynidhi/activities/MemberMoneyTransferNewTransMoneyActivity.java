package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MemberMoneyTransferNewTransMoneyActivity extends AppCompatActivity implements View.OnClickListener  {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private String mStr_customerNo, mStr_beneID, mStr_beneName, mStr_beneMobile, mStr_beneBankID, mStr_beneBankName, mStr_beneAccNo, mStr_beneIFSC;
    private Spinner mSp_sbAccList;
    private ArrayList<String> arrayList_sbAcc = new ArrayList<>();
    private ArrayList<String> arrayList_sbAccBal = new ArrayList<>();
    private String mStr_selectedSBAcc = "";
    private int mInt_selectedSBBal = 0;
    private TextView mTv_sbBal;
    private LinearLayout mLl_transDetailsRoot;
    private EditText mEt_amount, mEt_remarks;
    private Button mBtn_transferMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_money_transfer_new_trans_money);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();
        getdateTimeMill();
        mStr_customerNo = getIntent().getExtras().getString("customerNo");
        mStr_beneID = getIntent().getExtras().getString("beneID");
        mStr_beneName = getIntent().getExtras().getString("beneName");
        mStr_beneMobile = getIntent().getExtras().getString("beneMobile");
        mStr_beneBankID = getIntent().getExtras().getString("beneBankID");
        mStr_beneBankName = getIntent().getExtras().getString("beneID");
        mStr_beneAccNo = getIntent().getExtras().getString("beneID");
        mStr_beneIFSC = getIntent().getExtras().getString("beneID");


        getSBAccDetails(APILinks.GET_SAVINGS_ACCOUNT_INFO_BY_MEMBER_CODE_NEW + GlobalStore.GlobalValue.getMemberCode());

        mSp_sbAccList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mLl_transDetailsRoot.setVisibility(View.VISIBLE);
                    mTv_sbBal.setText("SB Balance : " + arrayList_sbAccBal.get(position));
                    mStr_selectedSBAcc = arrayList_sbAcc.get(position);
                    mInt_selectedSBBal = Integer.parseInt(arrayList_sbAccBal.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mSp_sbAccList = findViewById(R.id.sp_activity_meber_money_trans_new_trans_money_sb);
        mTv_sbBal = findViewById(R.id.tv_activity_member_money_trans_new_trans_money_sb_bal);
        mLl_transDetailsRoot = findViewById(R.id.ll_activity_member_money_trans_new_trans_money_details_root);
        mEt_amount = findViewById(R.id.et_activity_member_money_trans_new_trans_money_amt);
        mEt_remarks = findViewById(R.id.et_activity_member_money_trans_new_trans_money_remarks);
        mBtn_transferMoney = findViewById(R.id.btn_activity_member_money_trans_new_trans_money_transfer);
    }

    private void bindEventHandlers() {
        mBtn_transferMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_transferMoney) {
            if(Integer.parseInt(mEt_amount.getText().toString())>100){
                if (mEt_amount.getText().toString().length() > 0) {
                    if (mEt_remarks.getText().toString().length() > 0 || true) {
                        if (mInt_selectedSBBal > Integer.parseInt(mEt_amount.getText().toString())) {
                            moneyTransferProcess(APILinks.MONEY_TRANS_PROCESS);
                        } else {
                            Toast.makeText(this, "Low Balance", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mEt_remarks.setError("Enter Remarks");
                        mEt_remarks.requestFocus();
                    }
                } else {
                    mEt_amount.setError("Enter Amount");
                    mEt_amount.requestFocus();
                }
            }else{
                Toast.makeText(this, "Min Amount 101", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void moneyTransferProcess(String url) {
        HashMap hashMap = new HashMap();
        hashMap.put("MEMBERCODE", GlobalStore.GlobalValue.getMemberCode());
        hashMap.put("SBACCNO", mStr_selectedSBAcc);
        hashMap.put("CUSTOMERNO", mStr_customerNo);
        hashMap.put("BENEID", mStr_beneID);
        hashMap.put("AMT", mEt_amount.getText().toString());
        hashMap.put("TRNTYPE", "1");
        hashMap.put("IMPS_SCHEDULE", "");
        hashMap.put("REFNO", getdateTimeMill());
        hashMap.put("CHN", "APP");
        hashMap.put("CUR", "INR");
        hashMap.put("AG_LAT", "22.59579962574618");
        hashMap.put("AG_LONG", "88.40321907150629");
        hashMap.put("REMARKS", mEt_remarks.getText().toString());

        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getBoolean("tranStatus")) {
                            Toast.makeText(MemberMoneyTransferNewTransMoneyActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MemberMoneyTransferNewTransMoneyActivity.this, MemberMoneyTransferNewActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            Toast.makeText(MemberMoneyTransferNewTransMoneyActivity.this, response.getString("dbErrorMsg"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewTransMoneyActivity.this, "Trans report", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getSBAccDetails(String url) {
        arrayList_sbAcc.clear();
        arrayList_sbAccBal.clear();
        arrayList_sbAcc.add("");
        arrayList_sbAccBal.add("");
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            arrayList_sbAcc.add(jsonObject.getString("AccountCode"));
                            arrayList_sbAccBal.add(String.valueOf(jsonObject.getInt("Bal")));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MemberMoneyTransferNewTransMoneyActivity.this, R.layout.spinner_hint_select_sb_acc, arrayList_sbAcc);
                        mSp_sbAccList.setAdapter(arrayAdapter);
                    } else {
                        Toast.makeText(MemberMoneyTransferNewTransMoneyActivity.this, "NO SB Account Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getdateTimeMill() {//Getting the current date
        Date date = new Date();
        //This method returns the time in millis
        long timeMilli = date.getTime();
        //creating Calendar instance
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long timeMilli2 = calendar.getTimeInMillis();
        return String.valueOf(timeMilli2);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTv_toolbarTitle.setText("Money Transfer");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberMoneyTransferNewTransMoneyActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberMoneyTransferNewTransMoneyActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}