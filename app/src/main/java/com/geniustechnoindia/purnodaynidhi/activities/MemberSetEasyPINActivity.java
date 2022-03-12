package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberSetEasyPINActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private TextInputEditText mTie_enterMPIN;
    private TextInputEditText mTie_confirmMPin;
    private Button mBtn_confirm;

    // Toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_set_easy_pin);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Set mPin");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(MemberSetEasyPINActivity.this, MemberDashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewReferences() {
        mTie_enterMPIN = findViewById(R.id.tie_activity_set_easy_pin_enter_pin);
        mTie_confirmMPin = findViewById(R.id.tie_activity_set_easy_pin_confirm_pin);
        mBtn_confirm = findViewById(R.id.btn_activity_set_easy_pin_confirm);
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
    }

    private void bindEventHandlers() {
        mBtn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_confirm) {
            // TODO Set mPIN
            if (mTie_enterMPIN.getText().toString().trim().length() > 0) {
                if (mTie_confirmMPin.getText().toString().trim().length() > 0) {
                    if(mTie_enterMPIN.getText().toString().equals(mTie_confirmMPin.getText().toString())){
                        updateMPin(APILinks.INSERT_OR_UPDATE_MPIN + "memberCode=" + GlobalStore.GlobalValue.getMemberCode() + "&mPin=" + mTie_enterMPIN.getText().toString());
                    } else {
                        mTie_confirmMPin.setError("Confirm PIN");
                        mTie_confirmMPin.requestFocus();
                    }
                } else {
                    mTie_confirmMPin.requestFocus();
                    mTie_confirmMPin.setError("Confirm mPin");
                }
            } else {
                mTie_enterMPIN.setError("Enter mPin");
                mTie_enterMPIN.requestFocus();
            }
        }
    }

    private void updateMPin(String url) {
        HashMap hashMap = new HashMap();
        new PostDataParserObjectResponse(MemberSetEasyPINActivity.this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        String returnStatus = response.getString("returnStatus");
                        if (returnStatus.equals("Success")) {
                            Toast.makeText(MemberSetEasyPINActivity.this, "Your mPIN has been set successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MemberSetEasyPINActivity.this, LoginOptionsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(MemberSetEasyPINActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
