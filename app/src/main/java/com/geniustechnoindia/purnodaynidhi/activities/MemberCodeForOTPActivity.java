package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberCodeForOTPActivity extends AppCompatActivity implements View.OnClickListener {
    // Views
    private EditText mEt_memberCode;
    private Button mBtn_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_code_for_o_t_p);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();
    }

    private void setViewReferences() {
        mEt_memberCode = findViewById(R.id.et_activity_member_code_for_otp);
        mBtn_proceed = findViewById(R.id.btn_activity_code_for_otp_proceed);
    }

    private void bindEventHandlers() {
        mBtn_proceed.setOnClickListener(this);
    }

    private void setupToolbar() {
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_proceed) {
            if (mEt_memberCode.getText().toString().trim().length() > 0) {
                checkIfPassChanged(APILinks.CHECK_IF_PASS_CHANGED + mEt_memberCode.getText().toString());
            } else {
                mEt_memberCode.setError("Enter member code");
                mEt_memberCode.requestFocus();
            }
        }
    }

    private void checkIfPassChanged(String url) {
        new GetDataParserObject(MemberCodeForOTPActivity.this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("status").equals("1")) {     // success already changed...proceed to login screen

                        } else {                                                   // Not changed proceed yet
                            checkIfMemberCodeExistsAndSendOtp(APILinks.SEND_OTP_BY_MEMBER_CODE + mEt_memberCode.getText().toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                startActivity(new Intent(MemberCodeForOTPActivity.this, LoginOptionsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkIfMemberCodeExistsAndSendOtp(String url) {
        new GetDataParserObject(MemberCodeForOTPActivity.this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (!response.getString("OTP").equals("Not Exists")) {
                            //OtpSentDialog();
                            // TODO Start activity to enter otp
                            TempData.tempOtp = response.getString("OTP");
                            TempData.tempMemberCode = mEt_memberCode.getText().toString();
                            Toast.makeText(MemberCodeForOTPActivity.this, "An OTP has been sent to your number", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MemberCodeForOTPActivity.this, MemberEnterOtpActivity.class));
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } else {
                            Toast.makeText(MemberCodeForOTPActivity.this, "Mobile number doesn't exist.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
