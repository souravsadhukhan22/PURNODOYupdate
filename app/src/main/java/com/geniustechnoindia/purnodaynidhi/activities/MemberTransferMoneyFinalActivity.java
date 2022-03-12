package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberTransferMoneyFinalActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextView mTv_myMobileNumber;
    private TextView mTv_beneficiaryCode;
    private TextView mTv_customerName;
    private EditText mEt_amount;
    private EditText mEt_remarks;
    private Button mBtn_transfer;

    private TextView mTv_sbAccCode;
    private TextView mTv_balance;

    private String remarks = "";

    private double mySbBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_transfer_money_final);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        mTv_myMobileNumber.setText(TempData.moneyTrasferMyPhone);
        mTv_customerName.setText(TempData.moneyTrasferBeneName);
        mTv_beneficiaryCode.setText(TempData.moneyTrasferBeneID);
        mTv_sbAccCode.setText(TempData.moneyTransferSelectedSavingsAccountCode);

        getSavingsAccountDetails(GlobalStore.GlobalValue.getMemberCode(), TempData.moneyTransferSelectedSavingsAccountCode);
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_myMobileNumber = findViewById(R.id.tv_activity_member_transfer_money_final_my_mobile_number);
        mTv_beneficiaryCode = findViewById(R.id.tv_activity_member_transfer_money_final_beneficiary_code);
        mTv_customerName = findViewById(R.id.tv_activity_member_transfer_money_final_customer_name);
        mEt_amount = findViewById(R.id.et_activity_member_transfer_money_final_enter_amount);
        mBtn_transfer = findViewById(R.id.btn_activity_member_transfer_money_final_transfer_money);
        mEt_remarks = findViewById(R.id.et_activity_member_transfer_money_final_remarks);

        mTv_sbAccCode = findViewById(R.id.tv_activity_member_money_transfer_final_acc_code);
        mTv_balance = findViewById(R.id.tv_activity_member_money_transfer_final_balance);
    }

    private void bindEventHandlers() {
        mBtn_transfer.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mTv_toolbarTitle.setText("Transfer money");
        }
    }

    private void getSavingsAccountDetails(String memberCode, String accountCode) {
        new GetDataParserArray(MemberTransferMoneyFinalActivity.this, APILinks.GET_SAVINGS_ACCOUNT_DETAILS + "memberCode=" + memberCode + "&accountCode=" + accountCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            mySbBalance = jsonObject.getDouble("CurrantBalance");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mTv_balance.setText(mySbBalance + "");
                    } else {
                        Toast.makeText(MemberTransferMoneyFinalActivity.this, "Savings account not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_transfer) {
            if (mTv_myMobileNumber.getText().toString().trim().length() > 0) {
                if (mTv_beneficiaryCode.getText().toString().trim().length() > 0) {
                    if (mTv_customerName.getText().toString().trim().length() > 0) {
                        if (mEt_amount.getText().toString().trim().length() > 0) {
                            if(mEt_remarks.getText().toString().trim().length() > 0){
                                remarks = mEt_remarks.getText().toString();
                            } else {
                                remarks = "Money Transfer";
                            }
                            transferMoney();
                        } else {
                            mEt_amount.setError("Enter amount");
                            mEt_amount.requestFocus();
                        }
                    } else {
                        Toast.makeText(this, "Customer name not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Beneficiary code not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mobile number not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void transferMoney() {
        // TODO edit empty string
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobileNumber", GlobalStore.GlobalValue.getPhone());
        hashMap.put("beneficiaryCode", TempData.moneyTrasferBeneID);
        hashMap.put("amount", mEt_amount.getText().toString());
        hashMap.put("memberRequestId", GlobalStore.GlobalValue.getMemberCode() + GlobalStore.GlobalValue.getPhone());
        hashMap.put("customerName", mTv_customerName.getText().toString());
        hashMap.put("customerSarvahithaSBAccountCode", TempData.moneyTransferSelectedSavingsAccountCode);
        hashMap.put("remarks", remarks);
        long time = System.currentTimeMillis();
        String uNameLastFourChar = GlobalStore.GlobalValue.getMemberCode().substring(GlobalStore.GlobalValue.getMemberCode().length() - 5);
        String uniqueId = uNameLastFourChar + String.valueOf(time);
        hashMap.put("transactionID", uniqueId);

        hashMap.put("uniqueRechTranID", uniqueId);
        hashMap.put("userName", GlobalStore.GlobalValue.getMemberCode());
        hashMap.put("transferChargableMoney", "5");
        new PostDataParserObjectResponse(MemberTransferMoneyFinalActivity.this, APILinks.REQUEST_MONEY_TRANSFER, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        String returnStatus = response.getString("returnStatus");           // success or failed
                        String accountBalance = response.getString("sbAccountBalance");     // pass or failed
                        String insertionSbTrans = response.getString("insertionSBTrans");   // success or failed
                        String insertionSbDetails = response.getString("insertionSBDetailsTrans");  // pass or failed

                        if (accountBalance.equals("pass")) {
                            if (returnStatus.equals("success")) {
                                if (insertionSbTrans.equals("success")) {
                                    if (insertionSbDetails.equals("pass")) {
                                        showConfirmation("Success!", "Congrats! The transaction was successful", "success");
                                    } else {
                                        showConfirmation("Failed!", "Transaction failed! Error 3784", "failed");                        // API SB Details insertion failed
                                    }
                                } else {
                                    showConfirmation("Failed!", "Transaction failed! Error 1256", "failed");                            // API SB Insertion successful
                                }
                            } else {
                                showConfirmation("Failed!", "Your transaction was not successful", "failed");
                            }
                        } else {
                            showConfirmation("Failed!", "Your account balance is low", "failed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showConfirmation(String title, String message, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberTransferMoneyFinalActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (status.equals("success")) {
                    // TODO Clear all data
                    // TODO Dismiss dialog
                    startActivity(new Intent(MemberTransferMoneyFinalActivity.this, MemberBeneficiaryListActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    dialog.dismiss();
                } else if (status.equals("failed")) {
                    dialog.dismiss();
                }
            }
        }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberTransferMoneyFinalActivity.this, MemberBeneficiaryListActivity.class);
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
        startActivity(new Intent(MemberTransferMoneyFinalActivity.this, MemberBeneficiaryListActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
