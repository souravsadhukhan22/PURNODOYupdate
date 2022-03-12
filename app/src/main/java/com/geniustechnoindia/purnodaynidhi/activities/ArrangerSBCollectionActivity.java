package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
//import com.geniustechnoindia.purnodaynidhi.others.TempPrintSavingsData;
import com.geniustechnoindia.purnodaynidhi.others.TempPrintSavingsData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ArrangerSBCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEt_accNumber, mEt_amount, mEt_remarks;
    private Button mBtn_deposit,mBtn_withdrawl, mBtn_search;
    private Toolbar mToolbar;
    private TextView mTv_holderName, mTv_toolbarTitle, mTv_balance,mTvWalletBalance;
    private LinearLayout ll_balance;
    private String mStr_accountNo;
    private String mStr_phone = "";

    private CheckBox mCb_print;
    //BluetoothAdapter bluetoothAdapter;
    private Boolean printStatus=false;
    private ImageView mIv_memberImg;

    private int mInt_sbBalance=0,mInt_withdrawlAmt=0;
    private String mStr_memberCode="";

    private Boolean mBool_status=false,mBool_banalceStatus=false;
    private String mStr_TagDepoWith="";
    // SMS
    private static String msgBody = "";
    private static final String SMS_USERNAME = "";
    private static final String SMS_API_PASSWORD = "";
    private static final String SMS_SENDER_ID = "";
    private SharedPreferences sharedPreferences_printSBInfo;
    private SharedPreferences.Editor editor;

    private double walletBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_s_b_collection);

        setViewReferences();
        bindEventHandlers();
        setUpToolBar();

        getWalletBalance(APILinks.GET_ARRANGER_WALLET_BALANCE + GlobalStore.GlobalValue.getUserName());

        sharedPreferences_printSBInfo=getSharedPreferences("SBInfoLastData", Context.MODE_PRIVATE);
        editor=sharedPreferences_printSBInfo.edit();

        /*bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }*/

      /*  mCb_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bluetoothAdapter.isEnabled()) {
                    try {
                        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                        if (pairedDevices.size() == 0) {
                            Toast.makeText(ArrangerSBCollectionActivity.this, "Please Pair your Bluetooth device", Toast.LENGTH_LONG).show();
                        } else {
                            if (isChecked) {
                                printStatus = true;
                            } else {
                                printStatus = false;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
    }

    private void setViewReferences() {
        mEt_accNumber = findViewById(R.id.et_activity_agent_savings_deposit_account_number);
        mTv_holderName = findViewById(R.id.et_activity_agent_savings_deposit_holder_name);
        mEt_amount = findViewById(R.id.et_activity_agent_savings_deposit_amount);
        mBtn_deposit = findViewById(R.id.btn_activity_agent_savings_deposit_deposit);
        mBtn_withdrawl = findViewById(R.id.btn_activity_agent_savings_deposit_withdrawl);
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mBtn_search = findViewById(R.id.btn_activity_agent_savings_deposit_search);
        mTv_balance = findViewById(R.id.tv_activity_agent_savings_deposit_balance);
        ll_balance = findViewById(R.id.ll_activity_agent_savings_deposit_balance);
        mEt_remarks = findViewById(R.id.et_activity_agent_savings_deposit_remarks);
        mCb_print=findViewById(R.id.cb_activity_sb_collection_print);
        mIv_memberImg=findViewById(R.id.iv_activity_arranger_sb_coll_member_image);
        mTvWalletBalance = findViewById(R.id.tvWalletBalance);
    }

    private void bindEventHandlers() {
        mBtn_deposit.setOnClickListener(this);
        mBtn_withdrawl.setOnClickListener(this);
        mBtn_search.setOnClickListener(this);
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Savings Deposit");
    }

    private void getWalletBalance(String walletURL) {
        new GetDataParserObject(ArrangerSBCollectionActivity.this, walletURL, true, response -> {
            try {
                if (response != null) {
                    walletBalance = response.getDouble("bal");
                    mTvWalletBalance.setText("Rs. " + walletBalance);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_deposit) {
            if (mEt_amount.getText().toString().trim().length() > 0) {
                if(walletBalance > Double.parseDouble(mEt_amount.getText().toString().trim())){
                    if (mEt_accNumber.getText().toString().trim().equals(mStr_accountNo)) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("sbAccCode", mEt_accNumber.getText().toString().trim());
                        hashMap.put("amount", mEt_amount.getText().toString().trim());
                        if(mEt_remarks.getText().toString().length()>0){
                            hashMap.put("remarks", mEt_remarks.getText().toString());
                        }
                        else{
                            hashMap.put("remarks", "no remarks");
                        }
                        hashMap.put("arrangerCode", GlobalStore.GlobalValue.getUserName());
                        hashMap.put("Type","d");
                        new PostDataParserObjectResponse(this, APILinks.SAVINGS_DEPOSIT_CASH_TO_SB, hashMap, true, response -> {
                            try {
                                if (response.getBoolean("Status")) {
                                    //myDialogConfirmation(1, "Success", "Money successfully added");
                                    Toast.makeText(ArrangerSBCollectionActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    TempPrintSavingsData.amt=mEt_amount.getText().toString();
                                    TempPrintSavingsData.typeofTrans="Deposit";
                                    int currBal=Integer.parseInt(TempPrintSavingsData.amt)+Integer.parseInt(TempPrintSavingsData.prevBal);
                                    TempPrintSavingsData.currBal=String.valueOf(currBal);

                                    editor.putString("CurrDate",TempPrintSavingsData.currDate);
                                    editor.putString("CurrTime",TempPrintSavingsData.currTime);
                                    editor.putString("AccNo",TempPrintSavingsData.sbAccNumber);
                                    editor.putString("HolderName",TempPrintSavingsData.accHolderName);
                                    editor.putString("PrevBal",TempPrintSavingsData.prevBal);
                                    editor.putString("Amount",TempPrintSavingsData.amt);
                                    editor.putString("PresentBal",TempPrintSavingsData.currBal);
                                    editor.putString("TransType",TempPrintSavingsData.typeofTrans);
                                    editor.commit();



                                    startActivity(new Intent(ArrangerSBCollectionActivity.this, ArrangerSBCollectionActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    if (mStr_phone.length() > 0) {
                                        //sendSmsOnSuccessfulDeposit(mEt_amount.getText().toString().trim(), mEt_accNumber.getText().toString().trim(), mStr_phone,"deposit to");
                                    } else {
                                        //Toast.makeText(ArrangerSBCollectionActivity.this, "Phone Number not available", Toast.LENGTH_SHORT).show();
                                    }

                                    if(printStatus){
                                        //startActivity(new Intent(ArrangerSBCollectionActivity.this, PrintSBInformationActivity.class));
                                        //finish();
                                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }else{
                                        startActivity(new Intent(ArrangerSBCollectionActivity.this, ArrangerSBCollectionActivity.class));
                                        finish();
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }

                                /*if(printStatus){
                                    startActivity(new Intent(ArrangerSBCollectionActivity.this, PrintSBInformationActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }else{
                                    startActivity(new Intent(ArrangerSBCollectionActivity.this, ArrangerSBCollectionActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }*/

                                } else {
                                    myDialogConfirmation(0, "Failed", "Failed to add money");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Toast.makeText(ArrangerSBCollectionActivity.this, "Account Number Mismatch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this,"Not enough balance in wallet",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ArrangerSBCollectionActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == mBtn_withdrawl) {
            if (mEt_amount.getText().toString().trim().length() > 0) {
                if (mEt_accNumber.getText().toString().trim().equals(mStr_accountNo)) {
                    mInt_withdrawlAmt=Integer.parseInt(mEt_amount.getText().toString());

                    if((mInt_sbBalance-200) > mInt_withdrawlAmt || true){
                        HashMap hashMap = new HashMap();
                        hashMap.put("sbAccCode", mEt_accNumber.getText().toString().trim());
                        hashMap.put("amount", mEt_amount.getText().toString().trim());
                        if(mEt_remarks.getText().toString().length()>0){
                            hashMap.put("remarks", mEt_remarks.getText().toString());
                        }
                        else{
                            hashMap.put("remarks", "no remarks");
                        }
                        hashMap.put("arrangerCode", GlobalStore.GlobalValue.getUserName());
                        hashMap.put("Type","w");
                        new PostDataParserObjectResponse(this, APILinks.SAVINGS_DEPOSIT_CASH_TO_SB, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
                            @Override
                            public void onGetResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("Status")) {
                                        if(response.getBoolean("BalanceStatus")){
                                            //myDialogConfirmation(1, "Success", "Money successfully withdrawl");
                                            Toast.makeText(ArrangerSBCollectionActivity.this, "Success", Toast.LENGTH_LONG).show();
                                            TempPrintSavingsData.amt=mEt_amount.getText().toString();
                                            TempPrintSavingsData.typeofTrans="Withdrawl";
                                            int currBal=Integer.parseInt(TempPrintSavingsData.prevBal)-Integer.parseInt(TempPrintSavingsData.amt);
                                            TempPrintSavingsData.currBal=String.valueOf(currBal);

                                            editor.putString("CurrDate",TempPrintSavingsData.currDate);
                                            editor.putString("CurrTime",TempPrintSavingsData.currTime);
                                            editor.putString("AccNo",TempPrintSavingsData.sbAccNumber);
                                            editor.putString("HolderName",TempPrintSavingsData.accHolderName);
                                            editor.putString("PrevBal",TempPrintSavingsData.prevBal);
                                            editor.putString("Amount",TempPrintSavingsData.amt);
                                            editor.putString("PresentBal",TempPrintSavingsData.currBal);
                                            editor.putString("TransType",TempPrintSavingsData.typeofTrans);
                                            editor.commit();

                                            startActivity(new Intent(ArrangerSBCollectionActivity.this, ArrangerSBCollectionActivity.class));
                                            finish();
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                            if (mStr_phone.length() > 0) {
                                                //sendSmsOnSuccessfulDeposit(mEt_amount.getText().toString().trim(), mEt_accNumber.getText().toString().trim(), mStr_phone,"withdrawl from");
                                            } else {
                                                //Toast.makeText(ArrangerSBCollectionActivity.this, "Phone Number not available", Toast.LENGTH_SHORT).show();
                                            }

                                            if(printStatus){
                                                //startActivity(new Intent(ArrangerSBCollectionActivity.this, PrintSBInformationActivity.class));
                                                //finish();
                                                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }else{
                                                startActivity(new Intent(ArrangerSBCollectionActivity.this, ArrangerSBCollectionActivity.class));
                                                finish();
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }

                                        }else{
                                            Toast.makeText(ArrangerSBCollectionActivity.this, "Balance Status Low", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        myDialogConfirmation(0, "Error", "Something went wrong");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(this, "Low Balance", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(ArrangerSBCollectionActivity.this, "Account Number Mismatch", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ArrangerSBCollectionActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
            }
        }


        if (v == mBtn_search) {
            //APILinks.GET_SAVINGS_LEDGER_DETAILS;
            if (mEt_accNumber.getText().toString().trim().length() > 0) {
                mStr_memberCode="";
                new GetDataParserArray(this, APILinks.GET_SAVINGS_LEDGER_DETAILS + mEt_accNumber.getText().toString().trim(), true, new GetDataParserArray.OnGetResponseListener() {
                    @Override
                    public void onGetResponse(JSONArray response) {
                        try {
                            if (response != null) {
                                if (response.length() > 0) {
                                    mEt_accNumber.setEnabled(false);
                                    ll_balance.setVisibility(View.VISIBLE);
                                    mStr_accountNo = mEt_accNumber.getText().toString().trim();
                                    JSONObject jsonObject = response.getJSONObject(0);
                                    mStr_memberCode=jsonObject.getString("MemberCode");
                                    getMemberImage(APILinks.GET_MEMBER_PIC_BY_MEMBER_CODE+mStr_memberCode);
                                    mTv_holderName.setText(jsonObject.getString("MemberName"));
                                    mTv_balance.setText(String.valueOf((int)jsonObject.getDouble("SavingBalance")));
                                    mStr_phone = jsonObject.getString("Phone");
                                    mInt_sbBalance=(int)Double.parseDouble(jsonObject.getString("SavingBalance"));
                                    TempPrintSavingsData.companyName="";
                                    TempPrintSavingsData.companyPh="";
                                    TempPrintSavingsData.sbAccNumber=mEt_accNumber.getText().toString();
                                    TempPrintSavingsData.accHolderName=jsonObject.getString("MemberName");
                                    TempPrintSavingsData.currDate=jsonObject.getString("FormattedDate");
                                    TempPrintSavingsData.currTime=jsonObject.getString("CurrTime");
                                    TempPrintSavingsData.prevBal=String.valueOf(mInt_sbBalance);

                                } else {
                                    ll_balance.setVisibility(View.GONE);
                                    Toast.makeText(ArrangerSBCollectionActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ll_balance.setVisibility(View.GONE);
                                Toast.makeText(ArrangerSBCollectionActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Toast.makeText(ArrangerSBCollectionActivity.this, "Enter Acc No", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getMemberImage(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        mIv_memberImg.setVisibility(View.VISIBLE);
                        String pic=response.get(0).toString();
                        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        mIv_memberImg.setImageBitmap(decodedByte);
                    }else{
                        mIv_memberImg.setVisibility(View.INVISIBLE);
                        //Toast.makeText(ArrangerActiveMemberSearchActivity.this, "No Pic Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void myDialogConfirmation(final int status, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (status == 1) {
                            mEt_accNumber.getText().clear();
                            mEt_amount.getText().clear();
                            mEt_remarks.getText().clear();
                            recreate();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    private void sendSmsOnSuccessfulDeposit(String depositAmnt, String sbAcCode, String phoneNo,String depoOrwith) {
        int mInt_prevBalance=0,mInt_currentBalance=0;
        mInt_prevBalance=(int)Double.parseDouble(mTv_balance.getText().toString().trim());
        if(depoOrwith.equals("deposit to")){
            mInt_currentBalance=mInt_prevBalance+Integer.parseInt(depositAmnt);
        }
        if(depoOrwith.equals("withdrawl from")){
            mInt_currentBalance=mInt_prevBalance-Integer.parseInt(depositAmnt);
        }

        msgBody = "Dear Customer, your "+depoOrwith+" of amount Rs. " + depositAmnt + " has been "+depoOrwith+" your SB Account " + sbAcCode +
                ".Total Balance is Rs."+String.valueOf(mInt_currentBalance)+".Thank you." + getResources().getString(R.string.App_Full_Name);
        //msgBody = "Dear Customer, Your savings account " + mEt_accountCode.getText().toString() + "has been credited with " + mEt_depositAmount.getText().toString() + ",Nidhi Limited.";
        String smsUrl = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + SMS_USERNAME + "&password=" + SMS_API_PASSWORD + "&to=" + phoneNo + "&senderid=" + SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(smsUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               /* Toast.makeText(ArrangerSBCollectionActivity.this, "A confirmation message has been sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ArrangerSBCollectionActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();*/

                if(printStatus){
                    /*startActivity(new Intent(ArrangerSBCollectionActivity.this, PrintSBInformationActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
                }else{
                    startActivity(new Intent(ArrangerSBCollectionActivity.this, ArrangerSBCollectionActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ArrangerSBCollectionActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ArrangerSBCollectionActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
