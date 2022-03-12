package com.geniustechnoindia.purnodaynidhi.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.activities.ForgotPinActivity;
import com.geniustechnoindia.purnodaynidhi.activities.MemberDashboardActivity;
import com.geniustechnoindia.purnodaynidhi.bean.AppData;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class MemberLoginEasyPINFragments extends Fragment implements View.OnClickListener {

    private TextView mTv_userName;
    private EditText mTie_mPin;
    private Button mBtn_login;
    private SharedPreferences sharedPreferences;

    private String memberForMpin;

    private TextView mTv_forgotPin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_easy_pin_login, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewReferences();
        bindEventHandlers();

        sharedPreferences = getActivity().getSharedPreferences("MemberLogin", MODE_PRIVATE);

        memberForMpin = sharedPreferences.getString("MEMBERCODEFORMPIN", "1234567");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * When the fragments becomes visible
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (sharedPreferences.getString("MEMBERCODEFORMPIN", "1234567").equals("1234567")) {
                Toast.makeText(getActivity(), "mPin not set, Please Login with your Membercode and Password and set mPin", Toast.LENGTH_SHORT).show();
            } else {
                mTv_userName.setText(memberForMpin);
            }
        } else {
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_login) {
            if (mTv_userName.getText().toString().trim().length() > 0) {
                if (mTie_mPin.getText().toString().trim().length() > 0) {
                    // TODO Check if mPin is set or not
                    checkIfMPinIsSetOrNot(APILinks.CHECK_MPIN_SET_OR_NOT + mTv_userName.getText().toString());
                } else {
                    mTie_mPin.setError("Enter mPin");
                    mTie_mPin.requestFocus();
                }
            } else {
                Toast.makeText(getActivity(), "Username should not be blank", Toast.LENGTH_SHORT).show();
            }
        } else if(v == mTv_forgotPin){
            if(mTv_userName.getText().toString().trim().length() > 0){
                startActivity(new Intent(getActivity(), ForgotPinActivity.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                Toast.makeText(getActivity(), "Please Login with username and password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkIfMPinIsSetOrNot(String url) {
        new GetDataParserArray(getActivity(), url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            String status = response.getString(0);
                            if (status.equals("yes")) {
                                // TODO Login with mPin
                                performLoginWithEasyPin(APILinks.EASY_PIN_LOGIN, mTv_userName.getText().toString(), mTie_mPin.getText().toString());
                            } else if (status.equals("no")) {
                                Toast.makeText(getActivity(), "mPin not set. Please login with Membercode and Password, then set mPin", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Member code not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void performLoginWithEasyPin(String url, String memberCode, String easyPin) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberCode", memberCode);
        hashMap.put("mPin", easyPin);

        new PostDataParserObjectResponse(getActivity(), url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (!response.getString("MemberCode").equals("")) {
                            // TODO Perform login
                            AppData ad = new AppData();
                            ad.setMemberCode(response.getString("MemberCode"));
                            ad.setMemberName(response.getString("MemberName"));
                            ad.setOfficeID(response.getString("OfficeID"));
                            ad.setDateOfJoin(response.getString("DateOfJoin"));
                            ad.setDateOfEnt(response.getString("DateOfEnt"));
                            ad.setMemberDob(response.getString("MemberDOB"));
                            ad.setAppEasyPinSetOrNot(response.getString("appEasyPinIsSetOrNot"));
                            ad.setAppEasyPin(response.getString("appEasyPin"));
                            GlobalStore.GlobalValue = ad;
                            startActivity(new Intent(getActivity(), MemberDashboardActivity.class));
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Invalid Membercode or mPin", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setViewReferences() {
        mTv_userName = getActivity().findViewById(R.id.tv_fragment_easy_pin_login_username);
        mTie_mPin = getActivity().findViewById(R.id.tie_fragment_easy_pin_login_four_digit_pin);
        mBtn_login = getActivity().findViewById(R.id.btn_fragment_easy_pin_login);
        mTv_forgotPin = getActivity().findViewById(R.id.tv_fragment_easy_pin_forgot_pin);
    }

    private void bindEventHandlers() {
        mBtn_login.setOnClickListener(this);
        mTv_forgotPin.setOnClickListener(this);
    }
}
