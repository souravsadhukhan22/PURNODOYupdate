package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterBeneficiaryList;
import com.geniustechnoindia.purnodaynidhi.listeners.Listener;
import com.geniustechnoindia.purnodaynidhi.model.MemberBeneficiarySetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserArrayResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberBeneficiaryListActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbar;

    // views
    private RecyclerView mRv_beneficiaryList;
    private MemberBeneficiarySetGet memberBeneficiarySetGet;
    private ArrayList<MemberBeneficiarySetGet> arrayListBeneList;
    private AdapterBeneficiaryList adapterBeneficiaryList;

    // savings code spinner
    private Spinner mSp_savingsCodes;
    private ArrayList<String> arrayListSavingsCodes;

    private String selectedSavingsAccountCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_beneficiary_list);
        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_beneficiaryList.setLayoutManager(linearLayoutManager);

        arrayListSavingsCodes = new ArrayList<>();
        getSavingsList();

        arrayListBeneList = new ArrayList<>();
        adapterBeneficiaryList = new AdapterBeneficiaryList(MemberBeneficiaryListActivity.this,arrayListBeneList);
        getBeneficiaryList(GlobalStore.GlobalValue.getPhone());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.custom_spinner_view, arrayListSavingsCodes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_savingsCodes.setAdapter(adapter);

        adapterBeneficiaryList.setOnClick(new Listener() {
            @Override
            public void onClick(int position) {
                if(!selectedSavingsAccountCode.equals("")){
                    TempData.moneyTrasferBeneID = arrayListBeneList.get(position).getBENEID();
                    TempData.moneyTrasferBeneName = arrayListBeneList.get(position).getBENENAME();
                    TempData.moneyTrasferMyPhone = GlobalStore.GlobalValue.getPhone();
                    TempData.moneyTransferSelectedSavingsAccountCode = selectedSavingsAccountCode;
                    startActivity(new Intent(MemberBeneficiaryListActivity.this,MemberTransferMoneyFinalActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    mSp_savingsCodes.performClick();
                    Toast.makeText(MemberBeneficiaryListActivity.this, "Please select SB Acc. code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSp_savingsCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    selectedSavingsAccountCode = arrayListSavingsCodes.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSavingsList(){
        arrayListSavingsCodes.add("Select SB Account");
        new GetDataParserArray(MemberBeneficiaryListActivity.this, APILinks.GET_SAVINGS_ACCOUNT_CODE + GlobalStore.GlobalValue.getMemberCode(), true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    try {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                arrayListSavingsCodes.add(response.getString(i));
                            }
                        } else {
                            Toast.makeText(MemberBeneficiaryListActivity.this, "No savings account found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getBeneficiaryList(String mobileNumber) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobileNumber", mobileNumber);
        new PostDataParserArrayResponse(MemberBeneficiaryListActivity.this, APILinks.GET_BENEFICIARY_LIST, hashMap, true, new PostDataParserArrayResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            memberBeneficiarySetGet = new MemberBeneficiarySetGet();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                memberBeneficiarySetGet.setBENEID(jsonObject.getString("BENEID"));
                                memberBeneficiarySetGet.setBENENAME(jsonObject.getString("BENENAME"));
                                memberBeneficiarySetGet.setBANKNAME(jsonObject.getString("BANKNAME"));
                                memberBeneficiarySetGet.setBRANCHNAME(jsonObject.getString("BRANCHNAME"));
                                memberBeneficiarySetGet.setIFSCCODE(jsonObject.getString("IFSCCODE"));
                                memberBeneficiarySetGet.setACCOUNTNO(jsonObject.getString("ACCOUNTNO"));
                                memberBeneficiarySetGet.setMOBILE(jsonObject.getString("MOBILE"));
                                memberBeneficiarySetGet.setBENESTATUS(jsonObject.getString("BENESTATUS"));
                                memberBeneficiarySetGet.setIFSCSTATUS(jsonObject.getString("IFSCSTATUS"));
                                memberBeneficiarySetGet.setVERIFIEDBENE(jsonObject.getString("VERIFIEDBENE"));
                                memberBeneficiarySetGet.setBENENAMESTATUS(jsonObject.getString("BENENAMESTATUS"));
                                arrayListBeneList.add(memberBeneficiarySetGet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterBeneficiaryList.notifyDataSetChanged();
                        mRv_beneficiaryList.setAdapter(adapterBeneficiaryList);

                    } else {
                        Toast.makeText(MemberBeneficiaryListActivity.this, "No beneficiary found..Please add beneficiary", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbar = findViewById(R.id.toolbar_title);

        mRv_beneficiaryList = findViewById(R.id.rv_activity_member_beneficiary_list);
        mSp_savingsCodes = findViewById(R.id.sp_activity_member_beneficiary_list_savings_list);
    }

    private void bindEventHandlers() {
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mTv_toolbar.setText("Beneficiary list");
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberBeneficiaryListActivity.this, MemberMoneyTransferActivity.class);
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
        startActivity(new Intent(MemberBeneficiaryListActivity.this, MemberMoneyTransferActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
