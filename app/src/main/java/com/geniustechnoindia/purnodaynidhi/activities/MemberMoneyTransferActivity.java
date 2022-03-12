package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberMoneyTransferActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextView mTv_memberMobileNumber;
    private Button mBtn_checkIfMemberIsRegistered;

    // Create wallet form
    private TextView mTv_createWalletMobileNumber;
    private EditText mEt_createWalletFirstName;
    private EditText mEt_createWalletMiddleName;
    private EditText mEt_createWalletLastName;
    private Button mBtn_createWalletCreate;

    private LinearLayout mLl_otpValidationFormWalletAdd;

    private TextView mTv_mobileNumberOtpValidation;
    private EditText mEt_enterOtp;
    private Button mBtn_validateOtp;

    private LinearLayout mLl_createWalletRoot;

    // beneficiary
    private EditText mEt_beneficiaryName;
    private EditText mEt_beneficiaryMobile;
    private EditText mEt_accountNumber;
    private EditText mEt_ifscCode;
    private EditText mEt_bankName;
    private EditText mEt_branchName;

    private LinearLayout mLl_addBeneficiaryRoot;

    private Button mBtn_addBeneficiary;

    private Button mBtn_addMainBeneficiary;
    private Button mBtn_viewBeneficiaryList;

    // verify add beneficiary
    private TextView mTv_myMobileNumberAddBeneficiaryOtp;
    private EditText mEt_otpVerifyAddBeneficiary;
    private Button mBtn_addBeneficiaryValidate;
    private LinearLayout mLl_addBeneficiaryOtpValidationRoot;

    private ImageView mIv_addbenefClose;

    private LinearLayout mLl_mainButtonsRoot;

    private String addedBeneficiaryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_money_transfer);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        //GlobalStore.GlobalValue.setPhone("8981199989");             // TODO Remove this

        mTv_memberMobileNumber.setText(GlobalStore.GlobalValue.getPhone());
        mTv_mobileNumberOtpValidation.setText(GlobalStore.GlobalValue.getPhone());
        mTv_myMobileNumberAddBeneficiaryOtp.setText(GlobalStore.GlobalValue.getPhone());
        mTv_createWalletMobileNumber.setText(GlobalStore.GlobalValue.getPhone());
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_memberMobileNumber = findViewById(R.id.tv_activity_member_money_transfer_mobile_number);
        mBtn_checkIfMemberIsRegistered = findViewById(R.id.btn_activity_member_money_transfer_check_if_registered);

        mTv_createWalletMobileNumber = findViewById(R.id.tv_activity_member_money_transfer_create_wallet_mobile_number);
        mEt_createWalletFirstName = findViewById(R.id.et_activity_member_money_transfer_create_wallet_first_name);
        mEt_createWalletMiddleName = findViewById(R.id.et_activity_member_money_transfer_create_wallet_middle_name);
        mEt_createWalletLastName = findViewById(R.id.et_activity_member_money_transfer_create_wallet_last_name);
        mBtn_createWalletCreate = findViewById(R.id.btn_activity_member_money_transfer_create_wallet);

        mTv_mobileNumberOtpValidation = findViewById(R.id.tv_activity_member_money_transfer_mobile_number_of_otp_validation);
        mEt_enterOtp = findViewById(R.id.et_activity_member_money_transfer_enter_otp);
        mBtn_validateOtp = findViewById(R.id.btn_activity_member_money_transfer_validate_otp);

        mLl_createWalletRoot = findViewById(R.id.ll_activity_member_money_transfer_create_wallet_root);

        mLl_otpValidationFormWalletAdd = findViewById(R.id.ll_activity_member_money_transfer_create_wallet_otp_validation_form);

        // beneficiary
        mEt_beneficiaryName = findViewById(R.id.et_activity_member_money_transfer_add_beneficiary_name);
        mEt_beneficiaryMobile = findViewById(R.id.et_activity_member_money_transfer_add_beneficiary_mobile_number);
        mEt_accountNumber = findViewById(R.id.et_activity_member_money_transfer_add_beneficiary_account_no);
        mEt_ifscCode = findViewById(R.id.et_activity_member_money_transfer_add_beneficiary_ifsc_code);
        mEt_bankName = findViewById(R.id.et_activity_member_money_transfer_add_beneficiary_bank_name);
        mEt_branchName = findViewById(R.id.et_activity_member_money_transfer_create_wallet_branch_name);
        mBtn_addBeneficiary = findViewById(R.id.btn_activity_member_money_transfer_add_beneficiary_add);
        mLl_addBeneficiaryRoot = findViewById(R.id.ll_activity_member_money_transfer_add_beneficiary_root);

        // Add beneficiary validate
        mTv_myMobileNumberAddBeneficiaryOtp = findViewById(R.id.tv_activity_member_money_transfer_mobile_number_add_beneficiary_otp_validation);
        mEt_otpVerifyAddBeneficiary = findViewById(R.id.et_activity_member_money_transfer_enter_otp_add_beneficiary_otp_validation);
        mBtn_addBeneficiaryValidate = findViewById(R.id.btn_activity_member_money_transfer_enter_otp_add_beneficiary_validate);
        mLl_addBeneficiaryOtpValidationRoot = findViewById(R.id.ll_activity_member_money_transfer_otp_validation_form_add_beneficiary_root);

        mBtn_addMainBeneficiary = findViewById(R.id.btn_activity_member_money_transfer_add_beneficiary_main);
        mBtn_viewBeneficiaryList = findViewById(R.id.btn_activity_member_money_transfer_view_beneficiary_list);

        mLl_mainButtonsRoot = findViewById(R.id.ll_activity_member_money_transfer_main_buttons_root);

        mIv_addbenefClose = findViewById(R.id.iv_activity_member_money_transfer_add_beneficiary_close);
    }

    private void bindEventHandlers() {
        mBtn_checkIfMemberIsRegistered.setOnClickListener(this);
        mBtn_validateOtp.setOnClickListener(this);
        mBtn_addBeneficiary.setOnClickListener(this);
        mBtn_addBeneficiaryValidate.setOnClickListener(this);
        mBtn_createWalletCreate.setOnClickListener(this);
        mBtn_addMainBeneficiary.setOnClickListener(this);
        mBtn_viewBeneficiaryList.setOnClickListener(this);
        mIv_addbenefClose.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mTv_toolbarTitle.setText("Money Transfer");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_checkIfMemberIsRegistered) {
            if (mTv_memberMobileNumber.getText().toString().trim().length() > 0) {
                checkWalletExistOrNotAndOTPValidated(APILinks.CHECK_WALLET_EXISTS_OR_NOT, mTv_memberMobileNumber.getText().toString());
            } else {
                Toast.makeText(this, "Mobile number not found.\nPlease reload this page again or register your SB account with mobile number", Toast.LENGTH_LONG).show();
            }
        } else if (v == mBtn_createWalletCreate) {
            if (mTv_createWalletMobileNumber.getText().toString().trim().length() > 0) {
                if (mEt_createWalletFirstName.getText().toString().trim().length() > 0) {
                    if (mEt_createWalletLastName.getText().toString().trim().length() > 0) {
                        createWallet(GlobalStore.GlobalValue.getPhone(), mEt_createWalletFirstName.getText().toString(), mEt_createWalletMiddleName.getText().toString(), mEt_createWalletLastName.getText().toString());
                    } else {
                        mEt_createWalletLastName.setError("Enter last name");
                        mEt_createWalletLastName.requestFocus();
                    }
                } else {
                    mEt_createWalletFirstName.setText("Enter first name");
                    mEt_createWalletFirstName.requestFocus();
                }
            } else {
                Toast.makeText(this, "Mobile number not found.\nPlease reload this page again or register your SB account with mobile number", Toast.LENGTH_LONG).show();
            }
        } else if (v == mBtn_validateOtp) {
            if (mEt_enterOtp.getText().toString().trim().length() > 0) {
                if (mTv_createWalletMobileNumber.getText().toString().trim().length() > 0) {
                    // TODO Validate OTP API
                    validateOtpCreateWallet(mTv_memberMobileNumber.getText().toString(), mEt_enterOtp.getText().toString());
                } else {
                    Toast.makeText(this, "Member mobile number not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                mEt_enterOtp.setError("Enter OTP");
                mEt_enterOtp.requestFocus();
            }
        } else if (v == mBtn_addBeneficiary) {
            if (mTv_memberMobileNumber.getText().toString().trim().length() > 0) {
                if (mEt_beneficiaryName.getText().toString().trim().length() > 0) {
                    if (mEt_beneficiaryMobile.getText().toString().trim().length() > 0) {
                        if (mEt_beneficiaryMobile.getText().toString().trim().length() == 10) {
                            if (mEt_accountNumber.getText().toString().trim().length() > 0) {
                                if (mEt_ifscCode.getText().toString().trim().length() > 0) {
                                    if (mEt_bankName.getText().toString().trim().length() > 0) {
                                        if (mEt_branchName.getText().toString().trim().length() > 0) {
                                            // TODO Add beneficiary
                                            addBeneficiary(GlobalStore.GlobalValue.getPhone(), mEt_beneficiaryName.getText().toString(), mEt_beneficiaryMobile.getText().toString(), mEt_accountNumber.getText().toString(), mEt_ifscCode.getText().toString(), mEt_bankName.getText().toString(), mEt_branchName.getText().toString());
                                        } else {
                                            mEt_branchName.setError("Enter branch name");
                                            mEt_branchName.requestFocus();
                                        }
                                    } else {
                                        mEt_bankName.setError("Enter bank name");
                                        mEt_bankName.requestFocus();
                                    }
                                } else {
                                    mEt_ifscCode.setError("Enter IFSC code");
                                    mEt_ifscCode.requestFocus();
                                }
                            } else {
                                mEt_accountNumber.setError("Enter account number");
                                mEt_accountNumber.requestFocus();
                            }
                        } else {
                            mEt_beneficiaryMobile.setError("Enter mobile number");
                            mEt_beneficiaryMobile.requestFocus();
                        }
                    } else {
                        mEt_beneficiaryMobile.setError("Enter number");
                        mEt_beneficiaryMobile.requestFocus();
                    }
                } else {
                    mEt_beneficiaryName.setError("Enter name");
                    mEt_beneficiaryName.requestFocus();
                }
            } else {
                Toast.makeText(this, "Member Mobile number not found.. Please reload this page or add mobile number to your SB account", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mBtn_addMainBeneficiary) {
            mLl_addBeneficiaryRoot.setVisibility(View.VISIBLE);
        } else if (v == mBtn_viewBeneficiaryList) {
            startActivity(new Intent(MemberMoneyTransferActivity.this, MemberBeneficiaryListActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (v == mIv_addbenefClose) {
            mLl_addBeneficiaryRoot.setVisibility(View.GONE);
        } else if (v == mBtn_addBeneficiaryValidate) {
            if (mEt_otpVerifyAddBeneficiary.getText().toString().trim().length() > 0) {
                if (!GlobalStore.GlobalValue.getPhone().equals("")) {
                    if (!addedBeneficiaryId.equals("")) {
                        validateOtpAddBeneficiary(GlobalStore.GlobalValue.getPhone(), addedBeneficiaryId, mEt_otpVerifyAddBeneficiary.getText().toString());
                    } else {
                        Toast.makeText(this, "Beneficiary ID not found. Please try adding beneficiary again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                mEt_otpVerifyAddBeneficiary.setError("Enter OTP");
                mEt_otpVerifyAddBeneficiary.requestFocus();
            }
        }
    }

    private void addBeneficiary(String customerMobileNumber, String beneficiaryName, String beneficiaryNumber, String accountNumber, String ifscCode, String bankName, String branchName) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("customerMobileNumber", customerMobileNumber);
        hashMap.put("beneficiaryName", beneficiaryName);
        hashMap.put("beneficiaryNumber", beneficiaryNumber);
        hashMap.put("accountNumber", accountNumber);
        hashMap.put("ifscCode", ifscCode);
        hashMap.put("bankName", bankName);
        hashMap.put("branchName", branchName);
        new PostDataParserObjectResponse(MemberMoneyTransferActivity.this, APILinks.ADD_BENEFICIARY_LINK, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("returnStatus").equals("success")) {
                            // TODO Show beneficiary otp ui
                            addedBeneficiaryId = response.getString("beneficiaryId");
                            mLl_addBeneficiaryOtpValidationRoot.setVisibility(View.VISIBLE);
                        } else {
                            // Send
                            mLl_addBeneficiaryOtpValidationRoot.setVisibility(View.GONE);
                            Toast.makeText(MemberMoneyTransferActivity.this, "Beneficiary not added. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Create wallet OTP
    // TODO ************** Check this api again
    private void validateOtpCreateWallet(String mobileNumber, String otp) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobileNumber", mobileNumber);
        hashMap.put("otp", otp);
        new PostDataParserObjectResponse(MemberMoneyTransferActivity.this, APILinks.OTP_CREATE_WALLET, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("returnStatus").equals("success")) {
                            mLl_createWalletRoot.setVisibility(View.GONE);
                            mLl_otpValidationFormWalletAdd.setVisibility(View.GONE);
                            mLl_mainButtonsRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(MemberMoneyTransferActivity.this, "Wallet successfully created", Toast.LENGTH_SHORT).show();
                        } else {
                            mLl_createWalletRoot.setVisibility(View.GONE);
                            mLl_otpValidationFormWalletAdd.setVisibility(View.VISIBLE);
                            mLl_mainButtonsRoot.setVisibility(View.GONE);
                            Toast.makeText(MemberMoneyTransferActivity.this, "Wallet creation failed. Please enter correct OTP or try again.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    // Add beneficiary OTP
    private void validateOtpAddBeneficiary(String mobileNumber, String beneficiaryId, String otp) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobileNumber", mobileNumber);
        hashMap.put("beneficiaryId", beneficiaryId);
        hashMap.put("otp", otp);
        new PostDataParserObjectResponse(MemberMoneyTransferActivity.this, APILinks.OTP_ADD_BENEFICIARY, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("returnStatus").equals("success")) {
                            mLl_addBeneficiaryRoot.setVisibility(View.GONE);
                            mLl_addBeneficiaryOtpValidationRoot.setVisibility(View.GONE);
                            mLl_mainButtonsRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(MemberMoneyTransferActivity.this, "Beneficiary added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            mLl_addBeneficiaryRoot.setVisibility(View.VISIBLE);
                            mLl_addBeneficiaryOtpValidationRoot.setVisibility(View.VISIBLE);
                            mLl_mainButtonsRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(MemberMoneyTransferActivity.this, "Failed to add beneficiary. Please enter correct OTP or try again.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void checkWalletExistOrNotAndOTPValidated(String url, String mobileNumber) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobileNumber", mobileNumber);
        new PostDataParserObjectResponse(MemberMoneyTransferActivity.this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        String returnStatus = response.getString("returnStatus");
                        String otpValidate = response.getString("otpValidate");
                        String walletExists = response.getString("walletExists");
                        if (returnStatus.equals("success")) {           // Api response success                                    // Mobile number has wallet, check if otp validated
                            if (walletExists.equals("yes")) {
                                if (otpValidate.equals("yes")) {
                                    // TODO All success
                                    mLl_mainButtonsRoot.setVisibility(View.VISIBLE);
                                    mLl_createWalletRoot.setVisibility(View.GONE);
                                    mLl_otpValidationFormWalletAdd.setVisibility(View.GONE);
                                } else if (otpValidate.equals("no")) {
                                    // TODO Registered, but otp not validated
                                    mLl_mainButtonsRoot.setVisibility(View.GONE);
                                    mLl_createWalletRoot.setVisibility(View.GONE);
                                    mLl_otpValidationFormWalletAdd.setVisibility(View.VISIBLE);
                                }
                            } else if (walletExists.equals("no")) {
                                // TODO Wallet does not exist
                                mLl_createWalletRoot.setVisibility(View.VISIBLE);
                                mLl_otpValidationFormWalletAdd.setVisibility(View.GONE);
                            }

                            /*if (otpValidate.equals("yes")) {                                          // Work with the normal flow
                                // TODO Everything is OK...UI for beneficiary add
                                mLl_mainButtonsRoot.setVisibility(View.VISIBLE);
                                mLl_createWalletRoot.setVisibility(View.GONE);
                                mLl_addBeneficiaryRoot.setVisibility(View.GONE);
                                mLl_otpValidationFormWalletAdd.setVisibility(View.VISIBLE);
                                //mLl_addBeneficiaryRoot.setVisibility(View.VISIBLE);
                            } else if (otpValidate.equals("no")) {                                    // Send OTP
                                // TODO Change UI for OTP..show wallet creation form
                                mLl_otpValidationFormWalletAdd.setVisibility(View.VISIBLE);
                                mLl_createWalletRoot.setVisibility(View.GONE);
                                mLl_addBeneficiaryRoot.setVisibility(View.GONE);
                                mLl_mainButtonsRoot.setVisibility(View.GONE);
                            }*/
                        } else if (returnStatus.equals("failed")) {                                        // Api response failed                                                               // Mobile number doesn't have wallet, opt for wallet creation
                            /*mTv_createWalletMobileNumber.setText(GlobalStore.GlobalValue.getPhone());
                            mLl_createWalletRoot.setVisibility(View.VISIBLE);*/
                            Toast.makeText(MemberMoneyTransferActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void createWallet(String myMobileNumber, String myFirstName, String myMiddleName, String myLastName) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobileNumber", myMobileNumber);
        hashMap.put("firstName", myFirstName);
        hashMap.put("middleName", myMiddleName);
        hashMap.put("lastName", myLastName);
        new PostDataParserObjectResponse(MemberMoneyTransferActivity.this, APILinks.CREATE_WALLET, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("returnStatus").equals("success")) {
                            mLl_otpValidationFormWalletAdd.setVisibility(View.VISIBLE);
                            mLl_createWalletRoot.setVisibility(View.GONE);
                        } else {
                            mLl_otpValidationFormWalletAdd.setVisibility(View.GONE);
                            mLl_createWalletRoot.setVisibility(View.VISIBLE);
                            Toast.makeText(MemberMoneyTransferActivity.this, "Already registered", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                    }
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberMoneyTransferActivity.this, MemberDashboardActivity.class));
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
        startActivity(new Intent(MemberMoneyTransferActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
