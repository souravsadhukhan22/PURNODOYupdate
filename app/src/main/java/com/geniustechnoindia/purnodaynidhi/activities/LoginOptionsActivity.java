package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity;
import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.dl.AdminLoginProcess;
import com.geniustechnoindia.purnodaynidhi.dl.LoginManagement;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONException;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class LoginOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private Button mBtn_contactUs;
    // views
    private TextView mTv_memberLogin;
    private TextView mTv_customerLogin;
    private TextView mTv_associateLogin;
    private TextView mTv_adminLogin;
    private TextView mTv_aboutUS;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesArranger;
    private Boolean rememberStatusArranger = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Login Options");

        sharedPreferences = getSharedPreferences("MemberLogin", MODE_PRIVATE);
        sharedPreferencesArranger = getSharedPreferences("ARRANGERLOGIN", Context.MODE_PRIVATE);
        rememberStatusArranger = sharedPreferencesArranger.getString("REMEMBER", "").equals("TRUE");

        //getMacAddr();

        SharedPreferences sharedPreferences_loginSelection=getSharedPreferences("LoginSelection", MODE_PRIVATE);
        String selection = sharedPreferences_loginSelection.getString("IsMemberLogin","");
       /* if(selection.equals("TRUE")){
            mTv_adminLogin.setVisibility(View.GONE);mTv_associateLogin.setVisibility(View.GONE);
            if (sharedPreferences.getString("REMEMBER", "FALSE").equals("TRUE")) {      // IF remember me is set to TRUE, then auto login user and open Member Home menu

                final ProgressDialog progressDialog = new ProgressDialog(LoginOptionsActivity.this, ProgressDialog.THEME_HOLO_DARK);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                boolean loginStatus = new LoginManagement().isLoginMemberSuccessful(sharedPreferences.getString("MEMBERCODE", ""), sharedPreferences.getString("MEMBERPASSWORD", ""));
                                if (loginStatus) {
                                    startActivity(new Intent(LoginOptionsActivity.this, MemberDashboardActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                } else {
                                    startActivity(new Intent(LoginOptionsActivity.this, MemberLoginActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                                progressDialog.dismiss();
                            }
                        }, 1);

            } else if (sharedPreferences.getString("REMEMBER", "FALSE").equals("FALSE")) {
                startActivity(new Intent(LoginOptionsActivity.this, MemberLoginActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }*/
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mTv_memberLogin = findViewById(R.id.tv_login_options_activity_member_login);
        mTv_associateLogin = findViewById(R.id.tv_login_options_activity_associate_login);
        mTv_customerLogin = findViewById(R.id.tv_login_options_activity_customer_login);
        mTv_adminLogin = findViewById(R.id.tv_login_options_activity_admin_login);

        mBtn_contactUs = findViewById(R.id.btn_avtivity_login_option_contact_us);
        mTv_aboutUS = findViewById(R.id.tv_login_options_activity_about_company);
    }

    private void bindEventHandlers() {
        mTv_customerLogin.setOnClickListener(this);
        mTv_associateLogin.setOnClickListener(this);
        mTv_memberLogin.setOnClickListener(this);
        mBtn_contactUs.setOnClickListener(this);
        mTv_adminLogin.setOnClickListener(this);
        mTv_aboutUS.setOnClickListener(this);
    }

    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                Toast.makeText(LoginOptionsActivity.this, res1.toString(), Toast.LENGTH_SHORT).show();
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_customerLogin) {
            startActivity(new Intent(LoginOptionsActivity.this, CustomerActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (v == mTv_associateLogin) {

            if (rememberStatusArranger) {

                LoginManagement um = new LoginManagement();
                String username = sharedPreferencesArranger.getString("USERNAME", "");
                String password = sharedPreferencesArranger.getString("PASSWORD", "");

                if (um.isLoginAgentSuccessful(username, password)) {
                    TempData.TempArrangerCodeLogin = username;
                    startActivity(new Intent(LoginOptionsActivity.this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    startActivity(new Intent(LoginOptionsActivity.this, AssociateLoginActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            } else {
                startActivity(new Intent(LoginOptionsActivity.this, AssociateLoginActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        } else if (v == mTv_memberLogin) {

            startActivity(new Intent(LoginOptionsActivity.this, MemberLoginActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            // Login with OTP**** When you get template, just uncomment the below section
            /*startActivity(new Intent(LoginOptionsActivity.this, MemberFirstTimeLoginActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/





            //startActivity(new Intent(LoginOptionsActivity.this, MemberLoginActivity.class));

            /*
            if (sharedPreferences.getString("REMEMBER", "FALSE").equals("TRUE")) {      // IF remember me is set to TRUE, then auto login user and open Member Home menu

                final ProgressDialog progressDialog = new ProgressDialog(LoginOptionsActivity.this, ProgressDialog.THEME_HOLO_DARK);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                boolean loginStatus = new LoginManagement().isLoginMemberSuccessful(sharedPreferences.getString("MEMBERCODE", ""), sharedPreferences.getString("MEMBERPASSWORD", ""));
                                if (loginStatus) {
                                    startActivity(new Intent(LoginOptionsActivity.this, MemberDashboardActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                } else {
                                    startActivity(new Intent(LoginOptionsActivity.this, MemberLoginActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                                progressDialog.dismiss();
                            }
                        }, 3000);

            } else if (sharedPreferences.getString("REMEMBER", "FALSE").equals("FALSE")) {
                startActivity(new Intent(LoginOptionsActivity.this, MemberLoginActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
*/


        } else if (v == mTv_adminLogin) {
            SharedPreferences sharedPreferences_admin = getSharedPreferences("ADMINLOGIN", Context.MODE_PRIVATE);
            if (sharedPreferences_admin.getString("ADMINREMEMBERFLAG", "").equals("TRUE")) {
                String username = sharedPreferences_admin.getString("USERNAME", "");
                String password = sharedPreferences_admin.getString("PASSWORD", "");
                AdminLoginProcess adminLoginProcess = new AdminLoginProcess(LoginOptionsActivity.this, username, password);
                adminLoginProcess.adminLoginProcessJSON();
            } else {
                startActivity(new Intent(LoginOptionsActivity.this, AdminLoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        } else if (v == mBtn_contactUs) {
            startActivity(new Intent(LoginOptionsActivity.this, ContactUsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mTv_aboutUS){
            startActivity(new Intent(LoginOptionsActivity.this, AboutCompanyActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }


    private void checkIfMPinIsSetOrNot(String url) {
        new GetDataParserArray(LoginOptionsActivity.this, url, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    try {
                        String status = response.getString(0);
                        if (status.equals("yes")) {
                            // TODO Login with mPin
                            if (sharedPreferences.getBoolean("MPIN", false)) {
                                startActivity(new Intent(LoginOptionsActivity.this, EasyPinActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                startActivity(new Intent(LoginOptionsActivity.this, MemberFirstTimeLoginActivity.class));
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }

                        } else if (status.equals("no")) {
                            startActivity(new Intent(LoginOptionsActivity.this, MemberFirstTimeLoginActivity.class));
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginOptionsActivity.this, "Member code not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void callThis(boolean status) {
        if (status) {
            startActivity(new Intent(LoginOptionsActivity.this, AdminDashboardActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(LoginOptionsActivity.this, AdminLoginActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    private void performAutoLogin() {

    }
}
