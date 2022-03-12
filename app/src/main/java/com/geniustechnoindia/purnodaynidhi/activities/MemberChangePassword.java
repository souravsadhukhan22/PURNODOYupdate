package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.dl.LoginManagement;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.geniustechnoindia.purnodaynidhi.util.MyDialogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberChangePassword extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private EditText mEt_currentPassword;
    private Button mBtn_authenticate;

    private LinearLayout mLl_authenticateRoot;

    private LinearLayout mLl_newPasswordRoot;
    private EditText mEt_newPassword;
    private EditText mEt_confirmNewPassword;
    private Button mBtn_changePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_change_password);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Change password");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberChangePassword.this, MemberDashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mEt_currentPassword = findViewById(R.id.et_activity_settings_current_password);
        mBtn_authenticate = findViewById(R.id.btn_activity_settings_authenticate);
        mLl_authenticateRoot = findViewById(R.id.ll_activity_settings_authenticate_root);

        mLl_newPasswordRoot = findViewById(R.id.ll_activity_settings_new_password_root);
        mEt_newPassword = findViewById(R.id.et_activity_settings_new_password);
        mEt_confirmNewPassword = findViewById(R.id.et_activity_settings_confirm_new_password);
        mBtn_changePassword = findViewById(R.id.btn_activity_settings_change_password);
    }

    private void bindEventHandlers() {
        mBtn_authenticate.setOnClickListener(this);
        mBtn_changePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_authenticate) {
            if (mEt_currentPassword.getText().toString().trim().length() > 0) {
                checkCurrentPassword(GlobalStore.GlobalValue.getMemberCode(), mEt_currentPassword.getText().toString());
            } else {
                mEt_currentPassword.setError("Enter current password");
                mEt_currentPassword.requestFocus();
            }
        } else if (v == mBtn_changePassword) {
            if (mEt_newPassword.getText().toString().trim().length() > 0) {
                if (mEt_confirmNewPassword.getText().toString().trim().length() > 0) {
                    if (mEt_confirmNewPassword.getText().toString().equals(mEt_newPassword.getText().toString())) {
                        // TODO Continue with changing password
                        sendNewPasswordToServer(GlobalStore.GlobalValue.getMemberCode(), mEt_newPassword.getText().toString());
                    } else {
                        mEt_confirmNewPassword.setError("Password doesn't match");
                        mEt_confirmNewPassword.requestFocus();
                    }
                } else {
                    mEt_confirmNewPassword.setError("Confirm new password");
                    mEt_confirmNewPassword.requestFocus();
                }
            } else {
                mEt_newPassword.setError("Enter new password");
                mEt_newPassword.requestFocus();
            }
        }
    }


    /**
     * Send new password to update
     */
    private void sendNewPasswordToServer(String userName, String newPassword) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("memberCode", userName);
        hashMap.put("memberNewPass", newPassword);
        new PostDataParserObjectResponse(MemberChangePassword.this, APILinks.MEMBER_UPDATE_PASSWORD_LINK, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        String status = response.getString("returnStatus");
                        if (status.equals("Success")) {
                            Toast.makeText(MemberChangePassword.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MemberChangePassword.this, MemberDashboardActivity.class));
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } else {
                            Toast.makeText(MemberChangePassword.this, "Error updating password.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Check if the current password matches with the user entered password
     */
    private void checkCurrentPassword(final String userName, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(MemberChangePassword.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        boolean loginStatus = new LoginManagement().isLoginMemberSuccessful(userName, password,0,null);
                        if (!loginStatus) {
                            new MyDialogs(MemberChangePassword.this).myDialog("Warning!", "Authentication failed..Please try again");
                        } else {
                            // TODO: Show layout to enter new password
                            mLl_authenticateRoot.setVisibility(View.GONE);
                            mLl_newPasswordRoot.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(MemberChangePassword.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
