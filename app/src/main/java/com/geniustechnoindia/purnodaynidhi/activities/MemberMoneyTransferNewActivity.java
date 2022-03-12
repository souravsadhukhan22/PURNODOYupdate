package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterMemMoneyTransNewBenList;
import com.geniustechnoindia.purnodaynidhi.model.SetGetMemMoneyTransNewBenList;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.SearchableSpinner;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberMoneyTransferNewActivity extends AppCompatActivity implements View.OnClickListener, AdapterMemMoneyTransNewBenList.OnItemClicked,AdapterView.OnItemSelectedListener{
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    // check cus by mobile
    private EditText mEt_mobileNo;
    private Button mBtn_mobileNoCheck;
    private String mStr_mobStatusCode = "", mStr_mobStatusMsg = "";
    //add cus
    private LinearLayout mLl_addCusRoot;
    private EditText mEt_cusFirstName, mEt_cusLastName, mEt_cusAName, mEt_cusAddr1, mEt_cusAddr2, mEt_cusCity, mEt_cusState, mEt_cusCountry, mEt_cusPinCode;
    private Button mBtn_addCus;
    // cus create otp
    private LinearLayout mLl_cusCreateOTP;
    private EditText mEt_cusCreateOTP;
    private Button mBtn_cusCreateOTPSubmit;
    // active cus status
    private LinearLayout mLl_cusDetailsRoot;
    private TextView mTv_cusName, mTv_cusStatus, mTv_cusLimitStatus;
    //benificiary
    private LinearLayout mLl_beneficiaryRoot, mLl_addBeneficiaryRoot;
    private Button mBtn_addBeneficiary, mBtn_viewBeneficiary, mBtn_addBen;
    private EditText mEt_beneficName, mEt_beneficMobile, mEt_beneficAccNo, mEt_beneficIFSCCode;
    // bankid
    private SearchableSpinner  mSp_bankList;
    private ArrayList<String> arrayList_bankID = new ArrayList<>();
    private ArrayList<String> arrayList_bankName = new ArrayList<>();
    private String mStr_selectedBankID = "";
    private LinearLayout mLl_beneficiaryOTPRoot;
    private EditText mEt_beneficiaryOTP;
    private Button mBtn_beneficiaryOTPSubmit;
    private String mStr_beneID = "";
    // view beneficiary
    private RecyclerView mRv_viewBenefiList;
    private AdapterMemMoneyTransNewBenList adapterMemMoneyTransNewBenList;
    private SetGetMemMoneyTransNewBenList setGetMemMoneyTransNewBenList;
    private ArrayList<SetGetMemMoneyTransNewBenList> arrayList_memMoneyTransNewBenList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_money_transfer_new);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        // Searchable spinner
        mSp_bankList.setTitle("Select bank");

        getMemMobile(APILinks.GET_MEM_MOBILE+ GlobalStore.GlobalValue.getMemberCode());
        getBankID(APILinks.GET_BANK_LIST);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MemberMoneyTransferNewActivity.this);
        mRv_viewBenefiList.setLayoutManager(linearLayoutManager);
        adapterMemMoneyTransNewBenList = new AdapterMemMoneyTransNewBenList(MemberMoneyTransferNewActivity.this, arrayList_memMoneyTransNewBenList);
        mRv_viewBenefiList.setAdapter(adapterMemMoneyTransNewBenList);
        adapterMemMoneyTransNewBenList.setOnClick(MemberMoneyTransferNewActivity.this);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mEt_mobileNo = findViewById(R.id.et_activity_member_money_trans_new_mobile_no);
        mBtn_mobileNoCheck = findViewById(R.id.btn_activity_member_money_trans_new_mobile_no_check);

        mLl_addCusRoot = findViewById(R.id.ll_activity_member_money_trans_new_add_customer_root);
        mEt_cusFirstName = findViewById(R.id.et_activity_member_money_trans_new_cus_first_name);
        mEt_cusLastName = findViewById(R.id.et_activity_member_money_trans_new_cus_last_name);
        mEt_cusAName = findViewById(R.id.et_activity_member_money_trans_new_cus_a_name);
        mEt_cusAddr1 = findViewById(R.id.et_activity_member_money_trans_new_cus_addr1);
        mEt_cusAddr2 = findViewById(R.id.et_activity_member_money_trans_new_cus_addr2);
        mEt_cusCity = findViewById(R.id.et_activity_member_money_trans_new_cus_city);
        mEt_cusState = findViewById(R.id.et_activity_member_money_trans_new_cus_state);
        mEt_cusCountry = findViewById(R.id.et_activity_member_money_trans_new_cus_country);
        mEt_cusPinCode = findViewById(R.id.et_activity_member_money_trans_new_cus_pincode);
        mBtn_addCus = findViewById(R.id.btn_activity_member_money_trans_new_cus_add);

        mLl_cusCreateOTP = findViewById(R.id.ll_activity_member_money_trans_new_cus_create_otp_root);
        mEt_cusCreateOTP = findViewById(R.id.et_activity_member_money_trans_new_cus_otp);
        mBtn_cusCreateOTPSubmit = findViewById(R.id.btn_activity_member_money_trans_new_cus_otp_submit);

        mLl_cusDetailsRoot = findViewById(R.id.ll_activity_member_money_trans_new_active_customer_details);
        mTv_cusName = findViewById(R.id.tv_activity_member_money_trans_new_cus_name);
        mTv_cusStatus = findViewById(R.id.tv_activity_member_money_trans_new_cus_status);
        mTv_cusLimitStatus = findViewById(R.id.tv_activity_member_money_trans_new_cus_trans_limit_details);

        mLl_beneficiaryRoot = findViewById(R.id.ll_activity_member_money_trans_new_active_beneficiary_root);
        mBtn_addBeneficiary = findViewById(R.id.btn_activity_member_money_trans_new_add_beneficiary);
        mBtn_addBen = findViewById(R.id.btn_activity_member_money_trans_new_add_ben);
        mBtn_viewBeneficiary = findViewById(R.id.btn_activity_member_money_trans_new_view_beneficiary);
        mLl_addBeneficiaryRoot = findViewById(R.id.ll_activity_member_money_trans_new_active_add_beneficiary_root);
        mEt_beneficName = findViewById(R.id.et_activity_member_money_trans_new_bennefic_name);
        mEt_beneficMobile = findViewById(R.id.et_activity_member_money_trans_new_bennefic_mobile);
        mEt_beneficAccNo = findViewById(R.id.et_activity_member_money_trans_new_bennefic_acc_no);
        mEt_beneficIFSCCode = findViewById(R.id.et_activity_member_money_trans_new_bennefic_ifsc);

        mSp_bankList = findViewById(R.id.sp_activity_member_money_trans_new_bank_list);
        mLl_beneficiaryOTPRoot = findViewById(R.id.ll_activity_member_money_trans_new_active_add_beneficiary_otp_root);
        mEt_beneficiaryOTP = findViewById(R.id.et_activity_member_money_trans_new_bennefic_add_otp);
        mBtn_beneficiaryOTPSubmit = findViewById(R.id.btn_activity_member_money_trans_new_bennefic_add_otp_submit);

        mRv_viewBenefiList = findViewById(R.id.rv_activity_member_money_trans_new_beneficiary_list);
    }

    private void bindEventHandlers() {
        mBtn_mobileNoCheck.setOnClickListener(this);
        mBtn_addCus.setOnClickListener(this);
        mBtn_cusCreateOTPSubmit.setOnClickListener(this);
        mBtn_addBeneficiary.setOnClickListener(this);
        mBtn_viewBeneficiary.setOnClickListener(this);
        mBtn_addBen.setOnClickListener(this);
        mBtn_beneficiaryOTPSubmit.setOnClickListener(this);
        mSp_bankList.setOnItemSelectedListener(this);
    }

    private void getMemMobile(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        JSONObject jsonObject=response.getJSONObject(0);
                        mEt_mobileNo.setText(jsonObject.getString("Phone"));
                        mEt_mobileNo.setEnabled(false);
                    }else{
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "No Registered Phone Number Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position, boolean isPay, boolean isDel) {
        if (isPay) {
            Toast.makeText(this, arrayList_memMoneyTransNewBenList.get(position).getBeneName(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,MemberMoneyTransferNewTransMoneyActivity.class);
            intent.putExtra("customerNo",mEt_mobileNo.getText().toString());
            intent.putExtra("beneID",arrayList_memMoneyTransNewBenList.get(position).getBeneID());
            intent.putExtra("beneName",arrayList_memMoneyTransNewBenList.get(position).getBeneName());
            intent.putExtra("beneMobile",arrayList_memMoneyTransNewBenList.get(position).getBeneMobile());
            intent.putExtra("beneBankID",arrayList_memMoneyTransNewBenList.get(position).getBeneBankID());
            intent.putExtra("beneBankName",arrayList_memMoneyTransNewBenList.get(position).getBeneBankName());
            intent.putExtra("beneAccNo",arrayList_memMoneyTransNewBenList.get(position).getBeneAccNo());
            intent.putExtra("beneIFSC",arrayList_memMoneyTransNewBenList.get(position).getBeneIFSC());
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            startActivity(intent);
            finish();
        }
        if (isDel) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Want to delete Benificiary ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteBeneficiary(APILinks.DELETE_BENIFICIARY + mEt_mobileNo.getText().toString() + "&beneId=" + arrayList_memMoneyTransNewBenList.get(position).getBeneID());
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_mobileNoCheck) {
            if (mEt_mobileNo.getText().toString().length() > 0) {
                mEt_mobileNo.setEnabled(false);
                checkCustomerExistorNot(APILinks.GET_CUSTOMER_BY_MOB_NO + mEt_mobileNo.getText().toString());
            } else {
                mEt_mobileNo.setError("Enter Mobile No");
                mEt_mobileNo.requestFocus();
            }
        }
        if (v == mBtn_addCus) {
            if (mEt_cusFirstName.getText().toString().length() > 0) {
                if (mEt_cusLastName.getText().toString().length() > 0) {
                    if (mEt_cusAName.getText().toString().length() > 0) {
                        if (mEt_cusAddr1.getText().toString().length() > 0) {
                            if (mEt_cusAddr2.getText().toString().length() > 0) {
                                if (mEt_cusCity.getText().toString().length() > 0) {
                                    if (mEt_cusState.getText().toString().length() > 0) {
                                        if (mEt_cusCountry.getText().toString().length() > 0) {
                                            if (mEt_cusPinCode.getText().toString().length() > 0) {
                                                addCustomer(APILinks.ADD_CUSTOMER);
                                            } else {
                                                mEt_cusPinCode.setError("Enter Pincode");
                                                mEt_cusPinCode.requestFocus();
                                            }
                                        } else {
                                            mEt_cusCountry.setError("Enter Country");
                                            mEt_cusCountry.requestFocus();
                                        }
                                    } else {
                                        mEt_cusState.setError("Enter State");
                                        mEt_cusState.requestFocus();
                                    }
                                } else {
                                    mEt_cusCity.setError("Enter City");
                                    mEt_cusCity.requestFocus();
                                }
                            } else {
                                mEt_cusAddr2.setError("Enter Addr 2");
                                mEt_cusAddr2.requestFocus();
                            }
                        } else {
                            mEt_cusAddr1.setError("Enter Addr 1");
                            mEt_cusAddr1.requestFocus();
                        }
                    } else {
                        mEt_cusAName.setError("Enter A Name");
                        mEt_cusAName.requestFocus();
                    }
                } else {
                    mEt_cusLastName.setError("Enter Last Name");
                    mEt_cusLastName.requestFocus();
                }
            } else {
                mEt_cusFirstName.setError("Enter First Name");
                mEt_cusFirstName.requestFocus();
            }
        }
        if (v == mBtn_cusCreateOTPSubmit) {
            if (mEt_cusCreateOTP.getText().toString().length() > 0) {
                validateNewCusCreateOTP(APILinks.VALIDATE_CUS_CREATE_OTP + mEt_mobileNo.getText().toString() + "&otp=" + mEt_cusCreateOTP.getText().toString());
            } else {
                mEt_cusCreateOTP.setError("Enter OTP");
                mEt_cusCreateOTP.requestFocus();
            }
        }
        if (v == mBtn_addBeneficiary) {
            mLl_addBeneficiaryRoot.setVisibility(View.VISIBLE);
            mRv_viewBenefiList.setVisibility(View.GONE);
        }
        if (v == mBtn_addBen) {
            if (mEt_beneficName.getText().toString().length() > 0) {
                if (mEt_beneficMobile.getText().toString().length() > 0) {
                    if(!mStr_selectedBankID.equals("")){
                        if (mEt_beneficAccNo.getText().toString().length() > 0) {
                            if (mEt_beneficIFSCCode.getText().toString().length() > 0) {
                                addBeneficiary(APILinks.ADD_BENIFICIARY);
                            } else {
                                mEt_beneficIFSCCode.setError("Enter IFSC Code");
                                mEt_beneficIFSCCode.requestFocus();
                            }
                        } else {
                            mEt_beneficAccNo.setError("Enter Acc No");
                            mEt_beneficAccNo.requestFocus();
                        }
                    } else {
                        mSp_bankList.performClick();
                        Toast.makeText(this, "Select bank", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mEt_beneficMobile.setError("Enter Mobile No");
                    mEt_beneficMobile.requestFocus();
                }
            } else {
                mEt_beneficName.setError("Enter Name");
                mEt_beneficName.requestFocus();
            }
        }
        if (v == mBtn_beneficiaryOTPSubmit) {
            if (mEt_beneficiaryOTP.getText().toString().length() > 0) {
                addBeneficiaryWithOTP(APILinks.ADD_BENIFICIARY_VALIDATE_WITH_OTP + mEt_mobileNo.getText().toString() + "&beneId=" + mStr_beneID +
                        "&otp=" + mEt_beneficiaryOTP.getText().toString());
            } else {
                mEt_beneficiaryOTP.setError("Enter OTP");
                mEt_beneficiaryOTP.requestFocus();
            }
        }
        if (v == mBtn_viewBeneficiary) {
            mRv_viewBenefiList.setVisibility(View.VISIBLE);
            mLl_addBeneficiaryRoot.setVisibility(View.GONE);
            showBeneficiaryList(APILinks.GET_ALL_BENIFICIARY + mEt_mobileNo.getText().toString());
        }
    }

    private void checkCustomerExistorNot(String url) {
        new GetDataParserObject(this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject("NTDRESP");
                        mStr_mobStatusCode = jsonObject.getString("STATUSCODE");
                        mStr_mobStatusMsg = jsonObject.getString("STATUSMSG");
                        if (mStr_mobStatusCode.equals("1020")) {
                            mLl_addCusRoot.setVisibility(View.VISIBLE);
                        } else if (mStr_mobStatusCode.equals("0")) {
                            mLl_cusDetailsRoot.setVisibility(View.VISIBLE);
                            mTv_cusName.setText("Name - " + jsonObject.getString("FNAME") + " " + jsonObject.getString("LNAME"));
                            mTv_cusStatus.setText("Status : " + jsonObject.getString("STATUSDESC"));
                            mTv_cusLimitStatus.setText("Limit : " + jsonObject.getString("LIMIT") +
                                    " ::: USED : " + jsonObject.getString("USED") + " ::: REMAIN : " + jsonObject.getString("REMAIN"));
                            mLl_beneficiaryRoot.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "Customer Exist Report", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addCustomer(String url) {
        HashMap hashMap = new HashMap();
        hashMap.put("mobileNo", mEt_mobileNo.getText().toString());
        hashMap.put("fName", mEt_cusFirstName.getText().toString());
        hashMap.put("lName", mEt_cusLastName.getText().toString());
        hashMap.put("aName", mEt_cusAName.getText().toString());
        hashMap.put("add1", mEt_cusAddr1.getText().toString());
        hashMap.put("add2", mEt_cusAddr2.getText().toString());
        hashMap.put("city", mEt_cusCity.getText().toString());
        hashMap.put("state", mEt_cusState.getText().toString());
        hashMap.put("country", mEt_cusCountry.getText().toString());
        hashMap.put("pincode", mEt_cusPinCode.getText().toString());

        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject("NTDRESP");
                        if (jsonObject.getString("STATUSCODE").equals("0") || jsonObject.getString("STATUSCODE").equals("1025")) {
                            mLl_cusCreateOTP.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void validateNewCusCreateOTP(String url) {
        new GetDataParserObject(this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject("NTDRESP");
                        if (jsonObject.getString("STATUSCODE").equals("0")) {
                            showCustomDialog(jsonObject.getString("STATUSMSG"), "Phone No Registeded");
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "OTP Report", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addBeneficiary(String url) {
        HashMap hashMap = new HashMap();
        hashMap.put("customerno", mEt_mobileNo.getText().toString());
        hashMap.put("name", mEt_beneficName.getText().toString());
        hashMap.put("mobileno", mEt_beneficMobile.getText().toString());
        hashMap.put("bankid", mStr_selectedBankID);
        hashMap.put("accno", mEt_beneficAccNo.getText().toString());
        hashMap.put("ifsc", mEt_beneficIFSCCode.getText().toString());
        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject("NTDRESP");
                        if (jsonObject.getString("STATUSCODE").equals("0")) {
                            mLl_beneficiaryOTPRoot.setVisibility(View.VISIBLE);
                            mStr_beneID = jsonObject.getString("BENEID");
                        } else {
                            Toast.makeText(MemberMoneyTransferNewActivity.this, jsonObject.getString("STATUSMSG"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "Beneficiary Add Prob", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addBeneficiaryWithOTP(String url) {
        new GetDataParserObject(this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject("NTDRESP");
                        if (jsonObject.getString("STATUSCODE").equals("0")) {
                            showCustomDialog("Success", jsonObject.getString("STATUSMSG"));
                        } else {
                            Toast.makeText(MemberMoneyTransferNewActivity.this, jsonObject.getString("STATUSMSG"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "Ben Add OTP report", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBankID(String url) {
        arrayList_bankID.clear();
        arrayList_bankName.clear();
        arrayList_bankID.add("");
        arrayList_bankName.add("");
        new GetDataParserObject(this, url, true, response -> {
            try {
                if (response != null) {
                    JSONArray jsonArray = response.getJSONObject("NTDRESP").getJSONArray("BANKLIST");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayList_bankName.add(jsonObject.getString("BANKNAME"));
                        arrayList_bankID.add(jsonObject.getString("BANKID"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MemberMoneyTransferNewActivity.this, R.layout.spinner_hint_bank_id, arrayList_bankName);
                    mSp_bankList.setAdapter(arrayAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showCustomDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (title.equals("Success")) {
                            startActivity(new Intent(MemberMoneyTransferNewActivity.this, MemberMoneyTransferNewActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showBeneficiaryList(String url) {
        arrayList_memMoneyTransNewBenList.clear();
        new GetDataParserObject(this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getJSONObject("NTDRESP").getString("STATUSCODE").equals("0")) {
                            JSONArray jsonArray = response.getJSONObject("NTDRESP").getJSONArray("BENELIST");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                setGetMemMoneyTransNewBenList = new SetGetMemMoneyTransNewBenList();
                                setGetMemMoneyTransNewBenList.setBeneID(jsonObject.getString("BENEID"));
                                setGetMemMoneyTransNewBenList.setBeneName(jsonObject.getString("BENENAME"));
                                setGetMemMoneyTransNewBenList.setBeneMobile(jsonObject.getString("MOBILENO"));
                                setGetMemMoneyTransNewBenList.setBeneBankID(jsonObject.getString("BANKID"));
                                setGetMemMoneyTransNewBenList.setBeneBankName(jsonObject.getString("BANKNAME"));
                                setGetMemMoneyTransNewBenList.setBeneAccNo(jsonObject.getString("ACCNO"));
                                setGetMemMoneyTransNewBenList.setBeneIFSC(jsonObject.getString("IFSC"));
                                setGetMemMoneyTransNewBenList.setBeneStatus(jsonObject.getString("STATUS"));

                                arrayList_memMoneyTransNewBenList.add(setGetMemMoneyTransNewBenList);
                            }
                            adapterMemMoneyTransNewBenList.notifyDataSetChanged();
                        }else{
                            Toast.makeText(MemberMoneyTransferNewActivity.this, response.getJSONObject("NTDRESP").getString("STATUSMSG"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "Show benefi report", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteBeneficiary(String url) {
        new GetDataParserObject(this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject("NTDRESP");
                        if (jsonObject.getString("STATUSCODE").equals("0")) {
                            showCustomDialog("Success", "Benificiary Delete Successful");
                            //Toast.makeText(MemberMoneyTransferNewActivity.this, jsonObject.getString("STATUSMSG"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MemberMoneyTransferNewActivity.this, jsonObject.getString("STATUSMSG"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MemberMoneyTransferNewActivity.this, "Ben Delete report", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        startActivity(new Intent(MemberMoneyTransferNewActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberMoneyTransferNewActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            mStr_selectedBankID = arrayList_bankID.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}