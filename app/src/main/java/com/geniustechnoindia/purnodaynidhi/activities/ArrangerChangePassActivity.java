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

import com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.dl.LoginManagement;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.util.MyDialogs;

import org.json.JSONObject;

import java.util.HashMap;

public class ArrangerChangePassActivity extends AppCompatActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_arranger_change_pass);

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


    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mEt_currentPassword = findViewById(R.id.et_activity_settings_current_password_arranger);
        mBtn_authenticate = findViewById(R.id.btn_activity_settings_authenticate_arranger);
        mLl_authenticateRoot = findViewById(R.id.ll_activity_settings_authenticate_root_arranger);

        mLl_newPasswordRoot = findViewById(R.id.ll_activity_settings_new_password_root_arranger);
        mEt_newPassword = findViewById(R.id.et_activity_settings_new_password_arranger);
        mEt_confirmNewPassword = findViewById(R.id.et_activity_settings_confirm_new_password_arranger);
        mBtn_changePassword = findViewById(R.id.btn_activity_settings_change_password_arranger);
    }

    private void bindEventHandlers() {
        mBtn_authenticate.setOnClickListener(this);
        mBtn_changePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_authenticate) {
            if (mEt_currentPassword.getText().toString().trim().length() > 0) {
                checkCurrentPassword(TempData.TempArrangerCodeLogin, mEt_currentPassword.getText().toString());
            } else {
                mEt_currentPassword.setError("Enter current password");
                mEt_currentPassword.requestFocus();
            }
        } else if (v == mBtn_changePassword) {
            if (mEt_newPassword.getText().toString().trim().length() > 0) {
                if (mEt_confirmNewPassword.getText().toString().trim().length() > 0) {
                    if (mEt_confirmNewPassword.getText().toString().trim().equals(mEt_newPassword.getText().toString().trim())) {
                        // TODO Continue with changing password
                        sendNewPasswordToServer(TempData.TempArrangerCodeLogin, mEt_newPassword.getText().toString());
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
        HashMap hashMap=new HashMap();
        hashMap.put("arrangerCode",TempData.TempArrangerCodeLogin);
        hashMap.put("password",mEt_confirmNewPassword.getText().toString().trim());
        new PostDataParserObjectResponse(this, APILinks.ARRANGER_SET_NEW_PASS, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if(response!=null){
                        Toast.makeText(ArrangerChangePassActivity.this, "Password change successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ArrangerChangePassActivity.this, AssociateLoginActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();
                    }else{
                        Toast.makeText(ArrangerChangePassActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });

    }           /**
     * Check if the current password matches with the user entered password
     */
    private void checkCurrentPassword (final String userName, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(ArrangerChangePassActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        boolean loginStatus = new LoginManagement().isLoginAgentSuccessful(userName, password);
                        if (!loginStatus) {
                            new MyDialogs(ArrangerChangePassActivity.this).myDialog("Warning!", "Authentication failed..Please try again");
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
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrangerChangePassActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed () {
        //super.onBackPressed();
        Intent intent = new Intent(ArrangerChangePassActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
