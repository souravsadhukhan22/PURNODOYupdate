package com.geniustechnoindia.purnodaynidhi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geniustechnoindia.purnodaynidhi.activities.LoginOptionsActivity;
import com.geniustechnoindia.purnodaynidhi.dl.LoginManagement;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.util.OpenLinks;
import com.google.android.material.textfield.TextInputEditText;

public class AssociateLoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputEditText txtUserName, txtPassword;
    private TextView mTv_devByGen;
    private Boolean rememberStatus=false;
    private CheckBox mCheckBox_remember;
    private SharedPreferences sharedPreferencesarranger;

    private boolean validate() {
        boolean valid = false;
        String _userName, _password;

        _userName = txtUserName.getText().toString();
        _password = txtPassword.getText().toString();

        if (_userName.isEmpty() || _password.isEmpty()) {
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    private void login() {
        if (!this.validate()) {
            Toast.makeText(this, "User name and Password should not be empty", Toast.LENGTH_SHORT).show();
        } else {
            //true
            //btnLogin.setEnabled(false);
            final ProgressDialog progressDialog = new ProgressDialog(com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.this,
                   ProgressDialog.THEME_HOLO_DARK);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            final String _userName, _password;

            _userName = txtUserName.getText().toString();
            _password = txtPassword.getText().toString();

            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            // onLoginSuccess(_userName,_password);
                            LoginManagement um = new LoginManagement();
                            if (um.isLoginAgentSuccessful(_userName, _password)) {
                                onLoginSuccess();
                                TempData.TempArrangerCodeLogin=_userName;
                                //finish();
                            } else {

                                Toast.makeText(com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                            }
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }

    private void onLoginSuccess() {

        if(rememberStatus){
            SharedPreferences.Editor editor=sharedPreferencesarranger.edit();
            editor.putString("REMEMBER","TRUE");
            editor.putString("USERNAME",txtUserName.getText().toString().trim());
            editor.putString("PASSWORD",txtPassword.getText().toString().trim());
            editor.commit();
        }else{
            SharedPreferences.Editor editor=sharedPreferencesarranger.edit();
            editor.putString("REMEMBER","FALSE");
            editor.putString("USERNAME",txtUserName.getText().toString().trim());
            editor.putString("PASSWORD",txtPassword.getText().toString().trim());
            editor.commit();
        }

        startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName = findViewById(R.id.loginUserName);
        txtPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnlogin);
        mTv_devByGen = findViewById(R.id.tv_activity_login_developed_by);
        mCheckBox_remember=findViewById(R.id.check_box_arranger_login_remember_me);
        mTv_devByGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OpenLinks(com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.this).openGeniusTechnology();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        sharedPreferencesarranger=getSharedPreferences("ARRANGERLOGIN", Context.MODE_PRIVATE);
        mCheckBox_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rememberStatus=true;
                }
                else{
                    rememberStatus=false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.this, LoginOptionsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
