package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberChangePasswordOneTimeActivity extends AppCompatActivity implements View.OnClickListener {
    //views
    private Button mBtn_proceed;
    private EditText mEt_enterPassword;

    // toolbar
    private Toolbar mToolBar;
    private TextView mTv_toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_change_password_one_time);


        setViewReferences();
        bindEventHandlers();
        setupToolbar();
    }

    private void setViewReferences() {
        mBtn_proceed = findViewById(R.id.btn_activity_member_change_password_proceed);
        mEt_enterPassword = findViewById(R.id.et_activity_member_change_password_one_time);

        mToolBar = findViewById(R.id.custom_toolbar);
        mTv_toolbarText = findViewById(R.id.toolbar_title);
    }

    private void bindEventHandlers() {
        mBtn_proceed.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mTv_toolbarText.setText("Change password");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_proceed) {
            if (mEt_enterPassword.getText().toString().trim().length() > 0) {
                changePasswordFirstTime(APILinks.UPDATE_PASSWORD_ONE_TIME);
            } else {
                mEt_enterPassword.setError("Enter password");
                mEt_enterPassword.requestFocus();
            }
        }
    }

    private void changePasswordFirstTime(String url) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberCode", TempData.tempMemberCode);
        hashMap.put("password", mEt_enterPassword.getText().toString());
        new PostDataParserObjectResponse(MemberChangePasswordOneTimeActivity.this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.getString("status").equals("1")) {            // success
                            Toast.makeText(MemberChangePasswordOneTimeActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MemberChangePasswordOneTimeActivity.this, LoginOptionsActivity.class));
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } else {                                                        // failed
                            Toast.makeText(MemberChangePasswordOneTimeActivity.this, "Error updating password", Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(MemberChangePasswordOneTimeActivity.this, LoginOptionsActivity.class));
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
        startActivity(new Intent(MemberChangePasswordOneTimeActivity.this, LoginOptionsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
