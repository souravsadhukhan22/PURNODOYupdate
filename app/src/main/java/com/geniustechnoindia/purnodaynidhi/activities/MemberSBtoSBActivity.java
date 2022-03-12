package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_API_PASSWORD;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_SENDER_ID;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_USER_NAME;

public class MemberSBtoSBActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private Spinner mSp_myAccountCode;
    private TextView mTv_myAccountName;
    private TextView mTv_myMobileNumber;
    private TextView mTv_myCurrentBalance;

    private EditText mEt_recipientCode;
    private Button mBtn_searchRecipient;

    private TextView mTv_recipientName;
    private TextView mTv_recipientNumber;
    private TextView mTv_recipientMemberCode;
    private TextView mTv_recipientAccountNumber;

    private EditText mEt_enterAmount;
    private Button mBtn_enterAmountNext;

    private EditText mEt_enteredOtp;
    private Button mBtn_transfer;

    private EditText mEt_enterRemarks;

    // SMS User
    private static String msgBody = "";

    private static String senderMobileNumber = "";
    private static String generatedOtp = "";
    private static String selectedAccountCode = "";
    private static double myCurrentBalance = 0.0;

    // vars
    private static int savingsAccountFound = 0;
    private List<String> sbAccountArray;

    private LinearLayout mLl_amountAndRoot;
    private LinearLayout mLl_recipientDetailsRoot;

    private boolean savingsStatus = false;
    private boolean savingsRecepientStatus = false;
    Double CurrantBalance;

    private LinearLayout mLl_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_sbto_sb);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        mLl_otp.setVisibility(View.GONE);

        loadSavingsAccountCodes();

        mSp_myAccountCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedAccountCode = sbAccountArray.get(position);
                    getSavingsAccountSummaryByAccountCode(sbAccountArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadSavingsAccountCodes() {
        sbAccountArray = new ArrayList<String>();
        getSavingsAccount(GlobalStore.GlobalValue.getMemberCode());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.custom_spinner, sbAccountArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_myAccountCode.setAdapter(adapter);
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mSp_myAccountCode = findViewById(R.id.sp_activity_member_sb_to_sb);
        mTv_myAccountName = findViewById(R.id.tv_activity_member_sb_to_sb_my_account_name);
        mTv_myMobileNumber = findViewById(R.id.tv_activity_member_sb_to_sb_my_account_mobile_number);
        mTv_myCurrentBalance = findViewById(R.id.tv_activity_member_sb_to_sb_my_account_current_balance);
        mEt_recipientCode = findViewById(R.id.et_activity_member_sb_to_sb_recipient_account_code);
        mBtn_searchRecipient = findViewById(R.id.btn_activity_member_sb_to_sb_search_by_recipient_account);

        mTv_recipientName = findViewById(R.id.tv_activity_member_sb_to_sb_recipient_name);
        mTv_recipientNumber = findViewById(R.id.tv_activity_member_sb_to_sb_recipient_number);
        mTv_recipientMemberCode = findViewById(R.id.tv_activity_member_sb_to_sb_member_code);
        mTv_recipientAccountNumber = findViewById(R.id.tv_activity_member_sb_to_sb_account_no);

        mEt_enterAmount = findViewById(R.id.et_activity_member_sb_to_sb_enter_amount);
        mBtn_enterAmountNext = findViewById(R.id.btn_activity_member_sb_to_sb_enter_amount_next);

        mEt_enteredOtp = findViewById(R.id.et_activity_member_sb_to_sb_entered_otp);
        mBtn_transfer = findViewById(R.id.btn_activity_member_sb_to_sb_transfer);

        mEt_enterRemarks = findViewById(R.id.et_activity_member_sb_to_sb_enter_remarks);

        mLl_amountAndRoot = findViewById(R.id.ll_activity_member_sb_to_sb_otp_and_money_transfer_root);
        mLl_recipientDetailsRoot = findViewById(R.id.ll_activity_member_sb_to_sb_recipient_root);

        mLl_otp=findViewById(R.id.ll_activity_member_sb_to_sb_otp);
    }

    private void bindEventHandlers() {
        mBtn_searchRecipient.setOnClickListener(this);
        mBtn_enterAmountNext.setOnClickListener(this);
        mBtn_transfer.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mTv_toolbarTitle.setText("Member fund transfer");
        }
    }

    private void getRecepientDetails(String accountCode) {
        new GetDataParserArray(MemberSBtoSBActivity.this, APILinks.GET_SAVINGS_ACCOUNT_SUM_BY_ACCOUNT_CODE + accountCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    try {
                        if (response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(0);
                            mTv_recipientMemberCode.setText(jsonObject.getString("FirstApplicantMemberCode"));
                            mTv_recipientName.setText(jsonObject.getString("FirstApplicantName"));
                            mTv_recipientNumber.setText(jsonObject.getString("FirstApplicantPhone"));
                            mTv_recipientAccountNumber.setText(mEt_recipientCode.getText().toString());
                            jsonObject.getString("AccountOpeningDate");
                            jsonObject.getString("AccountAccessType");
                            jsonObject.getDouble("CurrantBalance");
                            savingsStatus = true;
                            savingsRecepientStatus = true;
                            mLl_recipientDetailsRoot.setVisibility(View.VISIBLE);
                        } else {
                            savingsStatus = false;
                            savingsRecepientStatus = false;
                            mLl_recipientDetailsRoot.setVisibility(View.GONE);
                            Toast.makeText(MemberSBtoSBActivity.this, "No related account found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    savingsStatus = false;
                    savingsRecepientStatus = false;
                    mLl_recipientDetailsRoot.setVisibility(View.GONE);
                    Toast.makeText(MemberSBtoSBActivity.this, "No related account found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_searchRecipient) {
            if (mEt_recipientCode.getText().toString().trim().length() > 0) {
                getRecepientDetails(mEt_recipientCode.getText().toString());
            } else {
                mEt_recipientCode.setError("Enter recipient code");
                mEt_recipientCode.requestFocus();
            }
        } else if (v == mBtn_enterAmountNext) {
            if (mEt_enterAmount.getText().toString().trim().length() > 0) {
                if (myCurrentBalance > (100 + Double.parseDouble(mEt_enterAmount.getText().toString()))) {
                    if (Double.parseDouble(mEt_enterAmount.getText().toString()) <= 10000) {
                        if (!senderMobileNumber.equals("")) {
                            if (generateOtp(senderMobileNumber)) {
                                if (!generatedOtp.equals("")) {
                                    // TODO Set visible OTP field
                                    sendSmsOTPToSenderNumber(generatedOtp, senderMobileNumber);
                                } else {
                                    Toast.makeText(this, "OTP not generated", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Failed to generate OTP", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Sender mobile number not found. Please reload this page or check your account information", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mEt_enterAmount.setError("Transaction limit : Rs. 10000");
                        mEt_enterAmount.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "Not enough balance", Toast.LENGTH_SHORT).show();
                }

            } else {
                mEt_enterAmount.setError("Please enter amount");
                mEt_enterAmount.requestFocus();
            }
        } else if (v == mBtn_transfer) {
            if (mEt_enteredOtp.getText().toString().trim().length() > 0) {
                if (mEt_enteredOtp.getText().toString().equals(generatedOtp)) {
                    showConfirmationDialog("Please confirm", "Are you sure you want to transfer the money?");
                } else {
                    mEt_enteredOtp.setError("OTP doesn't match");
                    mEt_enteredOtp.requestFocus();
                }
            } else {
                mEt_enteredOtp.setError("Enter OTP");
                mEt_enteredOtp.requestFocus();
            }
        }
    }

    private void showConfirmationDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberSBtoSBActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Proceed with the transfer
                sendMoneyToServer(selectedAccountCode, mTv_recipientAccountNumber.getText().toString(), "" + mEt_enterRemarks.getText().toString(), Double.parseDouble(mEt_enterAmount.getText().toString()), GlobalStore.GlobalValue.getMemberCode());
                /*if (err.getErrorCode() > 0) {
                    // Error
                    mLl_recipientDetailsRoot.setVisibility(View.VISIBLE);
                    Toast.makeText(MemberSBtoSBActivity.this, err.getErrorCode() + "", Toast.LENGTH_SHORT).show();
                } else {
                    mLl_recipientDetailsRoot.setVisibility(View.GONE);
                    clearAllData();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MemberSBtoSBActivity.this, MemberSBtoSBActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }*/

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void clearAllData() {
        selectedAccountCode = "";
        mTv_recipientAccountNumber.setText("");
        mEt_enterRemarks.setText("");
        mEt_enterAmount.setText("");

        mTv_recipientName.setText("");
        mTv_recipientNumber.setText("");
        mTv_recipientMemberCode.setText("");
        mTv_recipientAccountNumber.setText("");
        mEt_recipientCode.setText("");
        //TODO Clear all data

        sbAccountArray.clear();
        getSavingsAccount(GlobalStore.GlobalValue.getMemberCode());
    }

    public void sendMoneyToServer(String senderAccountCode, String recipientAccountCode, String remarks, double amount, String userName) {
        HashMap hashMap = new HashMap();
        hashMap.put("accountCode", senderAccountCode);
        hashMap.put("recipientAccountCode", recipientAccountCode);
        hashMap.put("remarks", remarks);
        hashMap.put("amount", String.valueOf(amount));
        hashMap.put("username", userName);
        new PostDataParserObjectResponse(MemberSBtoSBActivity.this, APILinks.SEND_MONEY_TO_SERVER, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("stringMessage").equals("Success")) {
                            mLl_recipientDetailsRoot.setVisibility(View.GONE);
                            clearAllData();
                            Toast.makeText(MemberSBtoSBActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                            sendSmsConfirmationSender(String.valueOf(amount),GlobalStore.GlobalValue.getPhone(),senderAccountCode);
                            startActivity(new Intent(MemberSBtoSBActivity.this, MemberSBtoSBActivity.class));
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } else if (response.getString("stringMessage").equals("low")) {
                            mLl_recipientDetailsRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(MemberSBtoSBActivity.this, "Balance low. Minimum balance is 200", Toast.LENGTH_LONG).show();
                        }
                        else {
                            mLl_recipientDetailsRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(MemberSBtoSBActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private void sendSmsConfirmationSender(String amount, String phoneNumber,String senderAccountCode) {
        Date d = new Date();
        CharSequence date  = DateFormat.format("MMMM d, yyyy ", d.getTime());
        Double avalAmount=CurrantBalance-Double.parseDouble(amount) ;
        msgBody = "Dear Customer, transfer of Rs - "+amount+" is successful, Date- "+date+" , and debited from your savings account. A/C- no. "+senderAccountCode+" , Aval Bal. "+avalAmount+" ,  Purnoday Nidhi Limited.";

       // msgBody = "Dear Customer,Transfer of Rs - " + amount + " is successful and debited from your savings account, "+ R.string.App_Full_Name +".";
        String smsUrl = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + SMS_USER_NAME + "&password=" + SMS_API_PASSWORD + "&to=" + phoneNumber + "&senderid=" + SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text&datetime=2017-03-24%2014%3A03%3A14";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(smsUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                /*startActivity(new Intent(RenewalCollectionActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();*/
                sendSmsConfirmationReceiver(amount,mTv_recipientNumber.getText().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                sendSmsConfirmationReceiver(amount,mTv_recipientNumber.getText().toString());
            }
        });
    }



    private void sendSmsConfirmationReceiver(String amount, String phoneNumber) {
        //
        msgBody = "Dear Customer,Transfer of Rs - " + amount + " is successful and credited to your savings account, " + R.string.App_Full_Name +".";
        String smsUrl = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + SMS_USER_NAME + "&password=" + SMS_API_PASSWORD + "&to=" + phoneNumber + "&senderid=" + SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text&datetime=2017-03-24%2014%3A03%3A14";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(smsUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                /*startActivity(new Intent(RenewalCollectionActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    // Get savings account code
    private void getSavingsAccount(String memberCode) {
        sbAccountArray.add("Select your account");
        new GetDataParserArray(MemberSBtoSBActivity.this, APILinks.GET_SAVINGS_ACCOUNT_INFO_BY_MEMBER_CODE + memberCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                //JSONObject jsonObject = response.getJSONObject(i);
                                savingsAccountFound = 1;
                                sbAccountArray.add(response.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        savingsAccountFound = 0;
                        Toast.makeText(MemberSBtoSBActivity.this, "No savings account found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getSavingsAccountSummaryByAccountCode(String accountCode) {
        new GetDataParserArray(MemberSBtoSBActivity.this, APILinks.GET_SAVINGS_ACCOUNT_SUM_BY_ACCOUNT_CODE + accountCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        jsonObject.getString("FirstApplicantMemberCode");
                        mTv_myAccountName.setText(jsonObject.getString("FirstApplicantName"));
                        mTv_myMobileNumber.setText(jsonObject.getString("FirstApplicantPhone"));
                        senderMobileNumber = jsonObject.getString("FirstApplicantPhone");
                        jsonObject.getString("AccountOpeningDate");
                        jsonObject.getString("AccountAccessType");
                        myCurrentBalance = jsonObject.getDouble("CurrantBalance");
                        mTv_myCurrentBalance.setText(jsonObject.getDouble("CurrantBalance") + "");
                        CurrantBalance= jsonObject.getDouble("CurrantBalance");
                        savingsStatus = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    savingsStatus = false;
                }
            }
        });
    }

    // Generate OTP
    private boolean generateOtp(String phone) {
        Connection cn = new SqlManager().getSQLConnection();
        boolean rValue = false;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GenerateMobileOTP(?)}");
                smt.setString("@Phone", phone);
                //smt.execute();
                ResultSet rs = smt.executeQuery();
                while (rs.next()) {
                    generatedOtp = rs.getString("OTP");                                 // TODO Send this OTP to phone number
                    rValue = true;
                }
            }
        } catch (Exception ex) {
            rValue = false;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    // test comment

    private void sendSmsOTPToSenderNumber(String otp, String mobileNumber) {
        String msgBody = "Dear Customer, Your OTP for  SB to SB money transfer is " + otp + ".";
        String SMS_URL = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + SMS_USER_NAME + "&password=" + SMS_API_PASSWORD + "&to=" + mobileNumber + "&senderid=" + SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SMS_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MemberSBtoSBActivity.this, "An OTP has been sent to your mobile number..Please Enter the OTP to proceed.", Toast.LENGTH_SHORT).show();
                mLl_otp.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MemberSBtoSBActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberSBtoSBActivity.this, MemberDashboardActivity.class));
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
        startActivity(new Intent(MemberSBtoSBActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void minimumBal(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Minimum SB Balance");
        builder.setMessage("Currently, " + R.string.App_Full_Name + " members need to maintain a minimum balance of Rs 200 in their Savings Account.");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", (dialog, which) -> builder.setCancelable(true));
        builder.show();
    }
}


