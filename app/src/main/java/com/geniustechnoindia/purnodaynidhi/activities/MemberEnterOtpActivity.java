package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.helper.SMSReceiver;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MemberEnterOtpActivity extends AppCompatActivity implements View.OnClickListener,SMSReceiver.OTPReceiveListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private EditText mEt_enterOtp;
    private Button mBtn_proceed;

    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_enter_otp);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();



        startSMSListener();

    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mEt_enterOtp = findViewById(R.id.et_activity_member_enter_otp);
        mBtn_proceed = findViewById(R.id.btn_activity_member_enter_otp_proceed);
    }

    private void bindEventHandlers() {
        mBtn_proceed.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mTv_toolbarTitle.setText("Enter OTP");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_proceed) {
            if (mEt_enterOtp.getText().toString().trim().length() > 0) {
                if (TempData.tempOtp.trim().length() > 0) {
                    if (mEt_enterOtp.getText().toString().equals(TempData.tempOtp)) {
                        startActivity(new Intent(MemberEnterOtpActivity.this, MemberChangePasswordOneTimeActivity.class));
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        Toast.makeText(this, "Invalid OTP..Please try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(new Intent(MemberEnterOtpActivity.this, MemberCodeForOTPActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            } else {
                mEt_enterOtp.setError("Enter otp");
                mEt_enterOtp.requestFocus();
            }
        }
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberEnterOtpActivity.this, LoginOptionsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MemberEnterOtpActivity.this, LoginOptionsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onOTPReceived(String otp) {
        String myOtp = otp.substring(35, 39);
        //showToast("OTP Received: " + myOtp);
        mEt_enterOtp.setText(myOtp);
        //mTv_countDown.setVisibility(View.GONE);
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }
}
