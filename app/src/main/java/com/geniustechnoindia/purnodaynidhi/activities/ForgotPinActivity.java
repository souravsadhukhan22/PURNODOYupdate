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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPinActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private Button mBtn_submit;
    private EditText mEt_enterMobileNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTv_toolbarTitle.setText("Forgot password?");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ForgotPinActivity.this, EasyPinActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mEt_enterMobileNumber = findViewById(R.id.et_activity_forgot_password_enter_mobile_number);
        mBtn_submit = findViewById(R.id.btn_activity_forgot_password_submit);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_submit) {
            if (mEt_enterMobileNumber.getText().toString().trim().length() > 0) {
                checkIfNumberExistsAndSendPinAs(APILinks.FORGOT_PIN_API_LINK + mEt_enterMobileNumber.getText().toString());
            } else {
                mEt_enterMobileNumber.setError("Enter mobile number");
                mEt_enterMobileNumber.requestFocus();
            }
        }
    }

    private void checkIfNumberExistsAndSendPinAs(String url) {
        new GetDataParserObject(ForgotPinActivity.this, url, true, new GetDataParserObject.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (!response.getString("OTP").equals("Not Exists")) {
                            OtpSentDialog();
                        } else {
                            Toast.makeText(ForgotPinActivity.this, "Mobile number doesn't exists.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void OtpSentDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPinActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Message sent");
        builder.setMessage("An  with the password has been sent to your registered mobile number.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
                startActivity(new Intent(ForgotPinActivity.this, MemberLoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }).show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ForgotPinActivity.this, EasyPinActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
