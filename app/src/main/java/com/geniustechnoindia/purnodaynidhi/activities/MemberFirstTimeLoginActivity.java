package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.dl.LoginManagement;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;
import com.geniustechnoindia.purnodaynidhi.util.NoChangingBackgroundTextInputLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberFirstTimeLoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextInputEditText mTie_username;
    private TextInputEditText mTie_password;

    private TextView mTv_forgotPassword;

    private NoChangingBackgroundTextInputLayout mTil_password;

    private Button mBtn_login;

    private static final String TAG = "MemberFirstTimeLoginAct";

    private static final int RESOLVE_HINT = 107;

    private GoogleApiClient googleApiClient;

    private boolean isPassAlreadyChanged;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_first_time_login);


        setViewReferences();
        bindEventHandlers();

        mTil_password.setVisibility(View.GONE);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
    }

    private void setViewReferences() {
        mTie_username = findViewById(R.id.tie_activity_member_login_username);
        mTie_password = findViewById(R.id.tie_activity_member_login_password);
        mBtn_login = findViewById(R.id.btn_activity_customer_login);
        mTil_password = findViewById(R.id.til_activity_login_password);
        mTv_forgotPassword = findViewById(R.id.tv_activity_member_login_forgot_password);
    }

    private void bindEventHandlers() {
        mBtn_login.setOnClickListener(this);
        mTv_forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_login) {
            if (mTie_username.getText().toString().trim().length() > 0) {
                // TODO Check first time pass changed or not
                checkIfPassChangedFirstTimeOrNot(APILinks.CHECK_IF_PASS_CHANGED + mTie_username.getText().toString());
                if (mTil_password.getVisibility() == View.VISIBLE) {
                    if (mTie_username.getText().toString().trim().length() > 0) {
                        if (mTie_password.getText().toString().trim().length() > 0) {

                            final ProgressDialog progressDialog = new ProgressDialog(MemberFirstTimeLoginActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            boolean loginStatus = new LoginManagement().isLoginMemberSuccessful(mTie_username.getText().toString(), mTie_password.getText().toString(),0,null);
                                            if (loginStatus) {
                                                sharedPreferences = getSharedPreferences("MemberLogin", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("REMEMBER", "TRUE");
                                                editor.putString("MEMBERCODE", mTie_username.getText().toString());
                                                editor.apply();
                                                Toast.makeText(MemberFirstTimeLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MemberFirstTimeLoginActivity.this, MemberDashboardActivity.class));
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                finish();
                                            } else {
                                                showInvalidLoginDialog();
                                            }
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);
                        } else {
                            mTie_password.setError("Enter password");
                            mTie_password.requestFocus();
                        }
                    } else {
                        mTie_username.setError("Enter username");
                        mTie_username.requestFocus();
                    }
                } else {

                }
            } else {
                mTie_username.setError("Enter username");
                mTie_username.requestFocus();
            }
        } else if (v == mTv_forgotPassword) {
            if (mTie_username.getText().toString().trim().length() > 0) {
                checkIfMemberCodeExistsAndSendOtp(APILinks.SEND_OTP_BY_MEMBER_CODE + mTie_username.getText().toString());
            } else {
                mTie_username.setError("Enter member code");
                mTie_username.requestFocus();
            }
        }
    }

   /* private void checkIfPassChangedFirstTimeOrNot(String url) {
        new GetDataParserArray(MemberFirstTimeLoginActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            if (response.getJSONObject(0).getBoolean("IsPassChangedApp")) {

                                // TODO Pass already changed..continue with the simple flow..show field to enter password
                                TempData.phoneNumber = response.getJSONObject(0).getString("Phone");
                                isPassAlreadyChanged = true;

                                if(mTil_password.getVisibility() != View.VISIBLE){
                                    getCreadenticalApiClient(true);
                                }
                            } else if (!response.getJSONObject(0).getBoolean("IsPassChangedApp")) {
                                // TODO Password not changed already
                                TempData.phoneNumber = response.getJSONObject(0).getString("Phone");
                                isPassAlreadyChanged = false;
                                getCreadenticalApiClient(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MemberFirstTimeLoginActivity.this, "Member Code Not Found", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }*/


    private void checkIfPassChangedFirstTimeOrNot(String url) {
        new GetDataParserArray(MemberFirstTimeLoginActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            JSONObject jsonObject=response.getJSONObject(0);
                            if (jsonObject.getBoolean("IsPassChangedApp")) {
                                // TODO Pass already changed..continue with the simple flow..show field to enter password
                                mTil_password.setVisibility(View.VISIBLE);
                                mTie_username.setEnabled(false);
                            } else if (!jsonObject.getBoolean("IsPassChangedApp")) {
                                // TODO Password not changed already
                                mTil_password.setVisibility(View.GONE);
                                checkIfMemberCodeExistsAndSendOtp(APILinks.SEND_OTP_BY_MEMBER_CODE + mTie_username.getText().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(MemberFirstTimeLoginActivity.this, "Member Code Not Found", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void checkIfMemberCodeExistsAndSendOtp(String url) {
        new GetDataParserObject(MemberFirstTimeLoginActivity.this, url, true, response -> {
            if (response != null) {
                try {
                    if (!response.getString("OTP").equals("Not Exists")) {
                        //OtpSentDialog();
                        // TODO Start activity to enter otp
                        TempData.tempOtp = response.getString("OTP");
                        TempData.tempMemberCode = mTie_username.getText().toString();
                        Toast.makeText(MemberFirstTimeLoginActivity.this, "An OTP has been sent to your number", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MemberFirstTimeLoginActivity.this, MemberEnterOtpActivity.class));
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        Toast.makeText(MemberFirstTimeLoginActivity.this, "Mobile number doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showInvalidLoginDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Invalid Username or Password");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(false);
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberFirstTimeLoginActivity.this, LoginOptionsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void getCreadenticalApiClient(boolean isPassAlreadyChanged) {
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

    // Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
                if (isPassAlreadyChanged) {
                    if (credential.getId().contains(TempData.phoneNumber)) {
                        mTil_password.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "Registered mobile no. not found in your phone.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (credential.getId().contains(TempData.phoneNumber)) {
                        mTil_password.setVisibility(View.GONE);
                        //checkIfMemberCodeExistsAndSendOtp(APILinks.SEND_OTP_BY_MEMBER_CODE_AUTO_READ + mTie_username.getText().toString());
                        checkIfMemberCodeExistsAndSendOtp(APILinks.SEND_OTP_BY_MEMBER_CODE + mTie_username.getText().toString());
                    } else {
                        Toast.makeText(this, "Registered mobile no. not found in your phone.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
