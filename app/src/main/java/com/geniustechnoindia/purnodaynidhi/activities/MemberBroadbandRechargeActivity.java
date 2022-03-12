package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.ProviderNewSetGet;
import com.geniustechnoindia.purnodaynidhi.model.ProviderSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.SearchableSpinner;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_API_PASSWORD;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_SENDER_ID;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_USER_NAME;

public class MemberBroadbandRechargeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private Button mBtn_submit;
    private TextInputEditText mTie_customerNumber;
    private TextInputEditText mTie_customerMobileNumber;
    private TextInputEditText mTie_remarks;
    private TextView mTv_inputHint;
    private TextView mTv_currentBalance;
    private SearchableSpinner mSs_providerSearchableSpinner;
    private TextInputEditText mTie_rechargeAmount;
    private LinearLayout mLl_selectContact;
    private TextView mTv_selectSavingsAccount;
    private TextInputEditText mTie_mobileNumber;

    // Spinner
    private ProviderSetGet providerSetGet;
    private ProviderNewSetGet providerNewSetGet;
    private ArrayList<ProviderNewSetGet> mArrayListProviderSetGet;

    // Static code
    private static final int PICK_CONTACT_REQUEST = 23;

    // variables
    private String providerId = "";
    private String providerName = "";
    private String mobileNumber = "";

    private PopupMenu popupMenuSelectSavingsAccount;
    private static String accountCode = "";
    private static String memberCode = "";
    private static String amount = "";
    private static String reqId = "";

    private double sbCurrentBalance = 0.0;
    private String reqTypeDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadband_rechargenew);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();


        mArrayListProviderSetGet = new ArrayList<>();

        // Searchable spinner
        mSs_providerSearchableSpinner.setTitle("Select Provider");

        // Popup menu select account
        popupMenuSelectSavingsAccount = new PopupMenu(this, mTv_selectSavingsAccount);     // TODO Change menu options

        // TODO Get menu options first
        getMenuOptions(APILinks.GET_SAVINGS_ACCOUNT_CODE + GlobalStore.GlobalValue.getMemberCode());

        popupMenuSelectSavingsAccount.setOnMenuItemClickListener(item -> {
            mTv_selectSavingsAccount.setText(item.getTitle());
            accountCode = (String) item.getTitle();
            getCurrentBalance(accountCode);
            return true;
        });

        loadElectricityBillOpCodes(APILinks.GET_OPERATOR_CODES_NEW + "Broadband%20Bill");
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTv_toolbarTitle.setText("Broadband Bill");
    }

    private void getMenuOptions(String url) {
        new GetDataParserArray(MemberBroadbandRechargeActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    /*popupMenuAccountNumber.getMenu().add("19020100001");
                    popupMenuAccountNumber.getMenu().add("19020100002");*/
                    try {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                popupMenuSelectSavingsAccount.getMenu().add(response.getString(i));
                            }
                        } else {
                            Toast.makeText(MemberBroadbandRechargeActivity.this, "No savings account found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getCurrentBalance(String accCode){
        new GetDataParserArray(MemberBroadbandRechargeActivity.this, APILinks.GET_SB_CURRENT_BALANCE + accCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if(response != null){
                    if(response.length() > 0){
                        try{
                            sbCurrentBalance = response.getDouble(0);
                            //mTv_currentBalance.setText("Rs. " + String.valueOf(sbCurrentBalance));
                            mTv_currentBalance.setText("Rs. " + String.format("%.2f", sbCurrentBalance));
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void loadElectricityBillOpCodes(String url) {
        new GetDataParserArray(MemberBroadbandRechargeActivity.this, url, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        //providerNewSetGet = new ProviderNewSetGet();
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            providerNewSetGet = new Gson().fromJson(jsonObject.toString(), ProviderNewSetGet.class);
                            mArrayListProviderSetGet.add(providerNewSetGet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mSs_providerSearchableSpinner.setAdapter(new ArrayAdapter<ProviderNewSetGet>(MemberBroadbandRechargeActivity.this, android.R.layout.simple_spinner_dropdown_item, mArrayListProviderSetGet));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_submit) {
            if (!accountCode.equals("")) {
                if (mTie_customerNumber.getText().toString().trim().length() > 0) {
                    if (!providerId.equals("")) {

                        if(mTie_mobileNumber.getText().toString().trim().length() > 0){
                            if (mTie_rechargeAmount.getText().toString().trim().length() > 0) {
                                memberCode = GlobalStore.GlobalValue.getUserName();
                                amount = mTie_rechargeAmount.getText().toString();
                                reqId = memberCode + mTie_customerNumber.getText().toString();
                                String rechargeFor = "Electricity," + providerName;
                                //sendRechargeDataToApi(accountCode, mTie_customerNumber.getText().toString(), memberCode, providerId, providerName, amount, reqId, "", rechargeFor, "", "", providerId, providerName, "Electricity");

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("AccountCode",accountCode);
                                if(mTie_remarks.getText().toString().trim().length() > 0){
                                    hashMap.put("Reamrks", mTie_remarks.getText().toString());
                                } else {
                                    hashMap.put("Reamrks", "Broadband payment from SB");
                                }
                                hashMap.put("UserName",GlobalStore.GlobalValue.getMemberCode());
                                hashMap.put("ProviderName",providerName);
                                hashMap.put("ProviderCode",providerId);
                                hashMap.put("ReqTypeDesc",reqTypeDesc);
                                hashMap.put("RefNo",GlobalStore.GlobalValue.getMemberCode() + System.currentTimeMillis());
                                hashMap.put("CustNo",mTie_customerNumber.getText().toString());
                                hashMap.put("RefMobNo",mTie_mobileNumber.getText().toString());
                                hashMap.put("AMT",amount);
                                hashMap.put("STV","0");
                                hashMap.put("PCode","741121");
                                hashMap.put("LAT","23.3017");
                                hashMap.put("LONG","88.5302");
                                hashMap.put("APIREQTYPE", "BILLPAY");

                                payBill(MemberBroadbandRechargeActivity.this,APILinks.PAY_BILL,hashMap);
                            } else {
                                mTie_rechargeAmount.setError("Enter recharge amount");
                                mTie_rechargeAmount.requestFocus();
                            }
                        } else {
                            mTie_mobileNumber.setError("Enter mobile number");
                            mTie_mobileNumber.requestFocus();
                        }




                    } else {
                        Toast.makeText(this, "Select provider", Toast.LENGTH_SHORT).show();
                        mSs_providerSearchableSpinner.performClick();
                    }
                } else {
                    mTie_customerNumber.setError("Enter Consumer ID");
                    mTie_customerNumber.requestFocus();
                }
            } else {
                mTv_selectSavingsAccount.performLongClick();
                Toast.makeText(this, "Please select savings Acc", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mLl_selectContact) {
            pickContact();
        } else if (v == mTv_selectSavingsAccount) {
            popupMenuSelectSavingsAccount.show();
        }
    }


    /**
     * Send recharge amount data to server
     */
    private void payBill(Context context, String URL, HashMap<String, String> hashMap){
        new PostDataParserObjectResponse(context, URL, hashMap, true, response -> {
            if(response != null){
                /*{
                    "APIStatus": true,
                        "IsBalanceLow": false,
                        "IsRechargeRecorded": false
                }*/
                try {
                    if (response.getBoolean("APIStatus")) {
                        if (!response.getBoolean("IsBalanceLow")) {
                            if (response.getBoolean("IsRechargeRecorded")) {
                                showConfirmationDialog("Success", "Your request for Broadband payment has been recorded successfully.", true);
                            } else {
                                showConfirmationDialog("Failed", "Failed to record your Broadband payment request.", false);
                            }
                        } else {
                            showConfirmationDialog("Failed", "You don't have enough balance in your savings account.", false);
                        }
                    } else {
                        showConfirmationDialog("Failed", "Something went wrong. Please try again later.", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showConfirmationDialog(String title, String message, boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberBroadbandRechargeActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", ((dialog, which) -> {
            dialog.dismiss();
            if (status) {
                sendSmsConfirmation(mTie_rechargeAmount.getText().toString(), GlobalStore.GlobalValue.getPhone());
            }
        })).show();
    }

    private static String msgBody = "";

    private void sendSmsConfirmation(String rechargeAmt, String phoneNumber) {
        msgBody = "Dear Customer, Broadband payment of Rs- " + rechargeAmt + " is successful and debited from your savings account, "+ R.string.App_Full_Name +".";
        String smsUrl = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + SMS_USER_NAME + "&password=" + SMS_API_PASSWORD + "&to=" + phoneNumber + "&senderid=" + SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text&datetime=2017-03-24%2014%3A03%3A14";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(smsUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                startActivity(new Intent(MemberBroadbandRechargeActivity.this, MemberBroadbandRechargeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                startActivity(new Intent(MemberBroadbandRechargeActivity.this, MemberBroadbandRechargeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }


    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);             // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String selectedNumber = cursor.getString(column).replaceAll("\\s+", "");             // Remove any spaces
                String selectedNumberNew = selectedNumber.replaceAll("-", "");                       // Remove any dashes
                mTie_customerMobileNumber.setText(selectedNumberNew.substring(selectedNumberNew.length() - 10));      // Max length should not be more than 10
            }
        }
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mBtn_submit = findViewById(R.id.btn_activity_electricity_recharge_proceed_to_pay_bill);
        mTie_customerNumber = findViewById(R.id.tie_activity_customer_number_recharge_customer_number);
        //mTie_customerMobileNumber = findViewById(R.id.tie_activity_dth_recharge_customer_mobile_number);
        mSs_providerSearchableSpinner = findViewById(R.id.ss_activity_electricity_bill_select_provider);
        mTie_rechargeAmount = findViewById(R.id.tv_activity_electricity_bill_recharge_amount);
        mTie_remarks = findViewById(R.id.tie_activity_electricity_bill_recharge_remarks);
        mTv_currentBalance = findViewById(R.id.tv_activity_wallet_recharge_current_balance);
        mTv_inputHint = findViewById(R.id.input_hint);

        mLl_selectContact = findViewById(R.id.ll_activity_dth_recharge_customer_select_mobile_number);
        mTv_selectSavingsAccount = findViewById(R.id.tv_activity_electricity_recharge_select_savings_account);

        mTie_mobileNumber = findViewById(R.id.tie_activity_member_broadband_recharge_mobile_no);



    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
        //mLl_selectContact.setOnClickListener(this);
        mSs_providerSearchableSpinner.setOnItemSelectedListener(this);
        mTv_selectSavingsAccount.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberBroadbandRechargeActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberBroadbandRechargeActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        providerId = mArrayListProviderSetGet.get(position).getCODE();
        providerName = mArrayListProviderSetGet.get(position).getNAME();
        reqTypeDesc = mArrayListProviderSetGet.get(position).getTYPE();
        if(!mArrayListProviderSetGet.get(position).getREMARKS().equals("")){
            mTv_inputHint.setText(mArrayListProviderSetGet.get(position).getREMARKS());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}