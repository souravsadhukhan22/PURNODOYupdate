package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ArrMemberUpdateInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private EditText mEt_memberName, mEt_fatherName, mEt_address, mEt_phoneNo, mEt_email, mEt_gender, mEt_idProofno, mEt_addrProofno;
    private EditText mEt_memberCode;
    private Button mBtn_dateOfBirth, mBtn_search, mBtn_submitSave;
    private String mStr_dateOfBirth = "", mStr_defaultDOB = "";
    private LinearLayout mLl_viewProfileRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_member_update_info);

        setViewReferences();
        bindEventHandlers();
        setUpToolBar();
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mEt_memberName = findViewById(R.id.et_activity_arr_edit_member_details_member_name);
        mEt_fatherName = findViewById(R.id.et_activity_arr_edit_member_details_dather_name);
        mEt_address = findViewById(R.id.et_activity_arr_edit_member_details_address);
        mEt_phoneNo = findViewById(R.id.et_activity_arr_edit_member_details_phone_no);
        mEt_email = findViewById(R.id.et_activity_arr_edit_member_details_email);
        mEt_gender = findViewById(R.id.et_activity_arr_edit_member_details_gender);
        mEt_idProofno = findViewById(R.id.et_activity_arr_edit_member_details_identity_proof_no);
        mEt_addrProofno = findViewById(R.id.et_activity_arr_edit_member_details_address_proof_no);

        mBtn_dateOfBirth = findViewById(R.id.btn_activity_arr_edit_member_details_date_of_birth);
        mBtn_search = findViewById(R.id.btn_activity_arr_edit_member_details_search);
        mEt_memberCode = findViewById(R.id.et_activity_arr_edit_member_details_member_code);
        mBtn_submitSave = findViewById(R.id.btn_activity_arr_edit_member_details_submit_save);
        mLl_viewProfileRoot = findViewById(R.id.ll_activity_arr_edit_member_details_root);
    }

    private void bindEventHandlers() {
        mBtn_dateOfBirth.setOnClickListener(this);
        mBtn_search.setOnClickListener(this);
        mBtn_submitSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtn_dateOfBirth) {
            Calendar calendar = Calendar.getInstance();
            int curr_day = 0, curr_month = 0, curr_year = 0;
            curr_day = calendar.get(Calendar.DAY_OF_MONTH);
            curr_month = calendar.get(Calendar.MONTH);
            curr_year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(ArrMemberUpdateInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month++;
                    mBtn_dateOfBirth.setText(dayOfMonth + " : " + month + " : " + year);
                    mStr_dateOfBirth = String.valueOf(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth);
                }
            }, curr_year, curr_month, curr_day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
        if (view == mBtn_search) {
            if (mEt_memberCode.getText().toString().trim().length() > 0) {
                mEt_memberCode.setEnabled(false);
                memberDetailsProcess();
            } else {
                mEt_memberCode.setError("Enter Member Code");
                mEt_memberCode.requestFocus();
            }
        }
        if (view == mBtn_submitSave) {
            sendUpdateToServer();
        }
    }

    private void memberDetailsProcess() {
        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String n = GlobalStore.GlobalValue.getMemberCode();
                        if (getmemberDetails(mEt_memberCode.getText().toString().trim())) {
                            // Show visibility
                            mLl_viewProfileRoot.setVisibility(View.VISIBLE);
                        } else {
                            mLl_viewProfileRoot.setVisibility(View.GONE);
                            Toast.makeText(ArrMemberUpdateInfoActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public boolean getmemberDetails(String memberCode) {
        Connection cn = new SqlManager().getSQLConnection();
        CallableStatement smt = null;

        try {
            if (cn != null) {
                smt = cn.prepareCall("{call ADROID_GetMemberDetails(?)}");
                smt.setString("@MemberCode", memberCode);
                smt.execute();
                //ResultSet rs=smt.getResultSet();
                ResultSet rs = smt.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        rs.getString("MemberCode");
                        mEt_memberName.setText(rs.getString("MemberName"));

                        /*String dateOfJoin = String.valueOf(rs.getInt("DateOfJoin"));
                        Date dateOfJoinNew = null;
                        try {
                            dateOfJoinNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(dateOfJoin);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedDateOfJoin = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(dateOfJoinNew);
                        mEt_date.setText(formattedDateOfJoin);*/

                        String dateOfBirth = String.valueOf(rs.getInt("MemberDOB"));
                        mStr_defaultDOB = dateOfBirth;
                        Date dateOfBirthNew = null;
                        try {
                            dateOfBirthNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(dateOfBirth);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedDateOfBirth = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(dateOfBirthNew);
                        mBtn_dateOfBirth.setText(formattedDateOfBirth);

                        mEt_fatherName.setText(rs.getString("Father"));
                        mEt_address.setText(rs.getString("Address"));
                        rs.getString("Pincode");
                        mEt_email.setText(rs.getString("Email"));
                        mEt_phoneNo.setText(rs.getString("Phone"));
                        rs.getString("MemberType");
                        rs.getString("BloodGr");
                        mEt_gender.setText(rs.getString("Gender"));
                        rs.getString("Occupation");
                        rs.getString("Education");
                        mEt_idProofno.setText(rs.getString("IdentityProof"));
                        mEt_addrProofno.setText(rs.getString("AddrProof"));
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.getMessage().toString();
        }
        return true;
    }

    private void sendUpdateToServer() {
        HashMap hashMap = new HashMap();
        hashMap.put("MemberCode", mEt_memberCode.getText().toString().trim());
        hashMap.put("Name", mEt_memberName.getText().toString().trim());
        if (!mStr_dateOfBirth.equals("")) {
            hashMap.put("DOB", mStr_dateOfBirth);
        } else {
            hashMap.put("DOB", mStr_defaultDOB);
        }
        hashMap.put("Father", mEt_fatherName.getText().toString().trim());
        hashMap.put("Address", mEt_address.getText().toString().trim());
        hashMap.put("Phone", mEt_phoneNo.getText().toString().trim());
        hashMap.put("Email", mEt_email.getText().toString().trim());
        hashMap.put("Sex", mEt_gender.getText().toString().trim());
        hashMap.put("IdProofNo", mEt_idProofno.getText().toString().trim());
        hashMap.put("AddrProofNo", mEt_addrProofno.getText().toString().trim());

        new PostDataParserObjectResponse(this, APILinks.ARR_MEMBER_DETAILS_UPDATE, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try {
                    if (response != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ArrMemberUpdateInfoActivity.this);
                        builder.setTitle("Success")
                                .setMessage("Submitted Successfully")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(ArrMemberUpdateInfoActivity.this, ArrMemberUpdateInfoActivity.class));
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                    }
                                }).show();
                    } else {
                        Toast.makeText(ArrMemberUpdateInfoActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Edit Member Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrMemberUpdateInfoActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(ArrMemberUpdateInfoActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
