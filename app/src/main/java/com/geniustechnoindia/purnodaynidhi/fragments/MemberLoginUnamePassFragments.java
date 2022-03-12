package com.geniustechnoindia.purnodaynidhi.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.activities.MemberDashboardActivity;
import com.geniustechnoindia.purnodaynidhi.dl.LoginManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class MemberLoginUnamePassFragments extends Fragment implements View.OnClickListener {

    // views
    private TextInputEditText mTie_username;
    private TextInputEditText mTie_password, mTie_phoneNumber;
    private TextInputLayout mTil_phoneNo;

    private Button mBtn_login;

    private CheckBox mCb_rememberMe;
    private SharedPreferences sharedPreferences;

    private boolean rememberStatus = false;
    private SharedPreferences sharedPreferences_loginSelection;

    private TextView mTvDateOfBirth;

    // Datepicker
    private Calendar mCalendar;
    private int currentDay, currentMonth, currentYear;
    private int memberDOB = 0;

    private String phoneNumber = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_name_pass_login, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewReferences();
        bindEventHandlers();

        sharedPreferences = getActivity().getSharedPreferences("MemberLogin", MODE_PRIVATE);

        rememberStatus = true;

        if(sharedPreferences.getString("REMEMBER","FALSE").equals("TRUE")){
            phoneNumber = sharedPreferences.getString("PHONE_NUMBER","");
            memberDOB = Integer.parseInt(sharedPreferences.getString("DOB","0"));
            mTil_phoneNo.setVisibility(View.GONE);
            mTvDateOfBirth.setVisibility(View.GONE);
            mTie_username.setText(sharedPreferences.getString("MEMBERCODE",""));
            mTie_username.setEnabled(false);
        }

        /*mCb_rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rememberStatus = isChecked;
        });*/

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);
    }

    private void setViewReferences() {
        mTie_username = getActivity().findViewById(R.id.tie_activity_member_login_username);
        mTie_password = getActivity().findViewById(R.id.tie_activity_member_login_password);
        mBtn_login = getActivity().findViewById(R.id.btn_activity_customer_login);
        mCb_rememberMe = getActivity().findViewById(R.id.cb_activity_member_login_remember_me);
        mTvDateOfBirth = getActivity().findViewById(R.id.tvLoginDOB);
        mTie_phoneNumber = getActivity().findViewById(R.id.tie_activity_phone_number);
        mTil_phoneNo = getActivity().findViewById(R.id.til_activity_phone_number);
    }

    private void bindEventHandlers() {
        mBtn_login.setOnClickListener(this);
        mTvDateOfBirth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_login) {
            if(sharedPreferences.getString("REMEMBER","FALSE").equals("FALSE")){
                phoneNumber = mTie_phoneNumber.getText().toString().trim();
            }
            if (mTie_username.getText().toString().trim().length() > 0) {
                if (mTie_password.getText().toString().trim().length() > 0) {
                    if (phoneNumber.length() > 0) {
                        if (memberDOB != 0) {
                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();
                            new Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            boolean loginStatus = new LoginManagement().isLoginMemberSuccessful(mTie_username.getText().toString(), mTie_password.getText().toString(), memberDOB, phoneNumber);
                                            if (loginStatus) {
                                                SharedPreferences.Editor editorrrr = sharedPreferences.edit();
                                                if (rememberStatus) {
                                                    editorrrr.putString("REMEMBER", "TRUE");
                                                } else {
                                                    editorrrr.putString("REMEMBER", "FALSE");
                                                }
                                                if(sharedPreferences.getString("REMEMBER","FALSE").equals("FALSE")){
                                                    editorrrr.putString("MEMBERCODE", mTie_username.getText().toString());
                                                    editorrrr.putString("MEMBERPASSWORD", mTie_password.getText().toString());
                                                    editorrrr.putString("PHONE_NUMBER", mTie_phoneNumber.getText().toString());
                                                    editorrrr.putString("MEMBERCODEFORMPIN", mTie_username.getText().toString());
                                                    editorrrr.putString("DOB", String.valueOf(memberDOB));
                                                    editorrrr.apply();
                                                }


                                                /*sharedPreferences_loginSelection = getActivity().getSharedPreferences("LoginSelection", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences_loginSelection.edit();
                                                editor.putString("IsMemberLogin", "TRUE");
                                                editor.apply();*/
                                                startActivity(new Intent(getActivity(), MemberDashboardActivity.class));
                                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                getActivity().finish();
                                            } else {
                                                showInvalidLoginDialog();
                                            }
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);
                        } else {
                            mTvDateOfBirth.performClick();
                            Toast.makeText(getActivity(), "Select Date of Birth", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mTie_phoneNumber.setError("Enter phone number");
                        mTie_phoneNumber.requestFocus();
                    }
                } else {
                    mTie_password.setError("Enter password");
                    mTie_password.requestFocus();
                }
            } else {
                mTie_username.setError("Enter username");
                mTie_username.requestFocus();
            }
        } else if (v == mTvDateOfBirth) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
                month += 1;
                mTvDateOfBirth.setText(dayOfMonth + "-" + month + "-" + year);
                memberDOB = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
            }, currentYear, currentMonth, currentDay);
            mCalendar.set(currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
            datePickerDialog.show();
        }
    }

    private void showInvalidLoginDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!");
        builder.setMessage("Invalid Username or Password");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(false);
            }
        });
        builder.show();
    }
}
