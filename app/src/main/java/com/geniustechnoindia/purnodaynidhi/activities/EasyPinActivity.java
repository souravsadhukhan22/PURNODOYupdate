package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.AppData;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.HashMap;

public class EasyPinActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv_userName;
    private EditText mTie_mPin;
    private Button mBtn_login;
    private SharedPreferences sharedPreferences;

    private String memberForMpin;

    private TextView mTv_forgotPin;

    private GoogleApiClient googleApiClient;

    private static final int RESOLVE_HINT = 107;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_pin);

        setViewReferences();
        bindEventHandlers();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        sharedPreferences = getSharedPreferences("MemberLogin", MODE_PRIVATE);

        memberForMpin = sharedPreferences.getString("MEMBERCODE", "1234567");

        if (sharedPreferences.getString("MEMBERCODE", "1234567").equals("1234567")) {
            Toast.makeText(this, "mPin not set, Please Login with your Member code and Password and set mPin", Toast.LENGTH_SHORT).show();
        } else {
            mTv_userName.setText(memberForMpin);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_login) {
            if (mTv_userName.getText().toString().trim().length() > 0) {
                if (mTie_mPin.getText().toString().trim().length() > 0) {
                    // TODO Check if mPin is set or not
                    checkIfMPinIsSetOrNot(APILinks.CHECK_MPIN_SET_OR_NOT + mTv_userName.getText().toString());
                } else {
                    mTie_mPin.setError("Enter mPin");
                    mTie_mPin.requestFocus();
                }
            } else {
                Toast.makeText(EasyPinActivity.this, "Username should not be blank", Toast.LENGTH_SHORT).show();
            }
        } else if (v == mTv_forgotPin) {
            if (mTv_userName.getText().toString().trim().length() > 0) {
                startActivity(new Intent(EasyPinActivity.this, ForgotPinActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                Toast.makeText(EasyPinActivity.this, "Please Login with username and password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkIfMPinIsSetOrNot(String url) {
        new GetDataParserArray(EasyPinActivity.this, url, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    try {
                        String status = response.getString(0);
                        if (status.equals("yes")) {
                            // TODO Login with mPin
                            performLoginWithEasyPin(APILinks.EASY_PIN_LOGIN, mTv_userName.getText().toString(), mTie_mPin.getText().toString());
                        } else if (status.equals("no")) {
                            Toast.makeText(EasyPinActivity.this, "mPin not set. Please login with Membercode and Password, then set mPin", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(EasyPinActivity.this, "Member code not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performLoginWithEasyPin(String url, String memberCode, String easyPin) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberCode", memberCode);
        hashMap.put("mPin", easyPin);

        new PostDataParserObjectResponse(EasyPinActivity.this, url, hashMap, true, response -> {
            if (response != null) {
                try {
                    if (!response.getString("MemberCode").equals("")) {
                        // TODO Perform login

                        AppData ad = new AppData();
                        ad.setMemberCode(response.getString("MemberCode"));
                        ad.setMemberName(response.getString("MemberName"));
                        ad.setOfficeID(response.getString("OfficeID"));
                        ad.setDateOfJoin(response.getString("DateOfJoin"));
                        ad.setDateOfEnt(response.getString("DateOfEnt"));
                        ad.setMemberDob(response.getString("MemberDOB"));
                        ad.setAppEasyPinSetOrNot(response.getString("appEasyPinIsSetOrNot"));
                        ad.setAppEasyPin(response.getString("appEasyPin"));
                        ad.setPhone(response.getString("phone"));
                        GlobalStore.GlobalValue = ad;

                        getCreadenticalApiClient();
                    } else {
                        Toast.makeText(EasyPinActivity.this, "Invalid Member code or mPin", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCreadenticalApiClient() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices

                if (credential.getId().contains(GlobalStore.GlobalValue.getPhone())) {
                    startActivity(new Intent(EasyPinActivity.this, MemberDashboardActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {
                    Toast.makeText(this, "Registered mobile no. not found in your phone.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }


    private void setViewReferences() {
        mTv_userName = findViewById(R.id.tv_activity_easy_pin_login_username);
        mTie_mPin = findViewById(R.id.tie_activity_easy_pin_login_four_digit_pin);
        mBtn_login = findViewById(R.id.btn_activity_easy_pin_login);
        mTv_forgotPin = findViewById(R.id.tv_activity_easy_pin_forgot_pin);
    }

    private void bindEventHandlers() {
        mBtn_login.setOnClickListener(this);
        mTv_forgotPin.setOnClickListener(this);
    }
}
