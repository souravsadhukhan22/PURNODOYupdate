package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.dl.AdminLoginProcess;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEt_userName, mEt_passWord;
    private CheckBox mCheckbox_remember;
    private Button mBtn_login;
    private SharedPreferences mSharedPreferences_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

       /* try{
            Intent intent=getIntent();
            String flag=intent.getExtras().getString("FLAG");
            if(flag.equals("TRUE")){
                SharedPreferences sharedPreferences_admin=getSharedPreferences("ADMINLOGIN",Context.MODE_PRIVATE);
                String username=sharedPreferences_admin.getString("USERNAME","");
                String password=sharedPreferences_admin.getString("PASSWORD","");
                performLogIn(username,password);
            }
        }catch (Exception e){e.printStackTrace();}*/

        setViewReferences();
        bindEventHandler();


        mSharedPreferences_admin = getSharedPreferences("ADMINLOGIN", Context.MODE_PRIVATE);
        mCheckbox_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mEt_userName.getText().toString().trim().length() > 0) {
                        if (mEt_passWord.getText().toString().trim().length() > 0) {
                            SharedPreferences.Editor editor = mSharedPreferences_admin.edit();
                            editor.putString("ADMINREMEMBERFLAG", "TRUE");
                            editor.putString("USERNAME", mEt_userName.getText().toString().trim());
                            editor.putString("PASSWORD", mEt_passWord.getText().toString().trim());
                            editor.commit();
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setViewReferences() {
        mEt_userName = findViewById(R.id.et_activity_admin_login_username);
        mEt_passWord = findViewById(R.id.et_activity_admin_login_password);
        mCheckbox_remember = findViewById(R.id.check_box_admin_login_remember_me);
        mBtn_login = findViewById(R.id.btn_activity_admin_login_submit);
    }

    private void bindEventHandler() {
        mBtn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_login) {
            if (mEt_userName.getText().toString().trim().length() > 0) {
                if (mEt_passWord.getText().toString().trim().length() > 0) {
                    performLogIn(mEt_userName.getText().toString().trim(), mEt_passWord.getText().toString().trim());
                } else {
                    mEt_passWord.setError("Enter Password");
                    mEt_passWord.requestFocus();
                }
            } else {
                mEt_userName.setError("Enter Username");
                mEt_userName.requestFocus();
            }
        }
    }

    private void performLogIn(final String str_username, final String str_password) {

        AdminLoginProcess adminLoginProcess=new AdminLoginProcess(AdminLoginActivity.this,str_username,str_password);

        adminLoginProcess.adminLoginProcessJSON();

        /*HashMap hashMap = new HashMap();
        hashMap.put("username", str_username);
        hashMap.put("pass", str_password);
        new PostDataParserArrayResponse(this, APILinks.ADMIN_LOGIN, hashMap, true, new PostDataParserArrayResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        if (response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(0);
                            if (jsonObject.getString("UserName").equals(str_username) || jsonObject.getString("Password").equals(str_password)) {
                                AdminInfoStaticData.setAdminUserName(jsonObject.getString("UserName"));
                                AdminInfoStaticData.setAdminPassword(jsonObject.getString("Password"));
                                AdminInfoStaticData.setAdminUserTypeId(jsonObject.getString("UserTypeID"));
                                AdminInfoStaticData.setAdminOfcId(jsonObject.getString("OfficeID"));
                                AdminInfoStaticData.setAdminIsActivate(jsonObject.getString("IsActive"));
                                AdminInfoStaticData.setAdminId(jsonObject.getString("id"));
                                onLoginSuccess();
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "Invalid Username Or Password", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(AdminLoginActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    public void callThis(boolean status){
        if(status){
            //Toast.makeText(this, String.valueOf(status), Toast.LENGTH_SHORT).show();
            onLoginSuccess();
        }else{
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    private void onLoginSuccess() {
        startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AdminLoginActivity.this, LoginOptionsActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
