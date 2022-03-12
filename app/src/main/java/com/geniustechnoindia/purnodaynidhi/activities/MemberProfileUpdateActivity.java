package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MemberProfileUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private EditText mEt_memberCode;
    private EditText mEt_name,mEt_father,mEt_address,mEt_state,mEt_pincode,mEt_phone,mEt_email,mEt_gender;
    private Button mBtn_dob,mBtn_submit,mBtn_search;
    private String mStr_updatedDOB="",mStr_previousDOB="";
    private Spinner mSp_bloodGr;
    private ArrayList<String> arraylistBloodGroup;
    private String mStr_bloodGr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile_update);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        arraylistBloodGroup = new ArrayList<>();
        arraylistBloodGroup.add("");
        arraylistBloodGroup.add("A+");
        arraylistBloodGroup.add("A-");
        arraylistBloodGroup.add("B+");
        arraylistBloodGroup.add("B-");
        arraylistBloodGroup.add("O+");
        arraylistBloodGroup.add("O-");
        arraylistBloodGroup.add("AB+");
        arraylistBloodGroup.add("AB-");
        arraylistBloodGroup.add("Don't Know");

        ArrayAdapter<String> adapterBloodGroup = new ArrayAdapter<String>(
                this, R.layout.spinner_hint, arraylistBloodGroup);
        adapterBloodGroup.setDropDownViewResource(R.layout.spinner_hint);
        mSp_bloodGr.setAdapter(adapterBloodGroup);

        getMemberDetails(APILinks.GET_MEMBER_DETAILS_UPDATE+ GlobalStore.GlobalValue.getMemberCode());

        mSp_bloodGr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    mStr_bloodGr=mSp_bloodGr.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);

        mEt_name=findViewById(R.id.et_activity_member_profile_update_name);
        mEt_father=findViewById(R.id.et_activity_member_profile_update_father);
        mEt_address=findViewById(R.id.et_activity_member_profile_update_address);
        mEt_state=findViewById(R.id.et_activity_member_profile_update_state);
        mEt_pincode=findViewById(R.id.et_activity_member_profile_update_pincode);
        mEt_phone=findViewById(R.id.et_activity_member_profile_update_phone);
        mEt_email=findViewById(R.id.et_activity_member_profile_update_email);
        mEt_gender=findViewById(R.id.et_activity_member_profile_update_gender);
        mBtn_dob=findViewById(R.id.btn_activity_member_profile_update_dob);
        mBtn_submit=findViewById(R.id.btn_activity_member_profile_update_submit);
        mEt_memberCode=findViewById(R.id.et_activity_member_profile_update_member_code);
        mBtn_search=findViewById(R.id.btn_activity_member_profile_update_search);
        mSp_bloodGr=findViewById(R.id.sp_activity_member_profile_update_blood_gr);
    }

    private void bindEventHandlers(){
        mBtn_dob.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mBtn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtn_search){
            if(mEt_memberCode.getText().toString().trim().length()>0){
                //getMemberDetails(APILinks.GET_MEMBER_DETAILS_UPDATE+mEt_memberCode.getText().toString().trim());
                //getMemberDetails(APILinks.GET_MEMBER_DETAILS_UPDATE+ GlobalStore.GlobalValue.getMemberCode());
            }else{
                mEt_memberCode.setError("Enter Member Code");
                mEt_memberCode.requestFocus();
            }
        }
        if(v==mBtn_dob){
            int curr_day=0,curr_month=0,curr_year=0;
            Calendar calendar= Calendar.getInstance();
            curr_day=calendar.get(Calendar.DAY_OF_MONTH);
            curr_month=calendar.get(Calendar.MONTH);
            curr_year=calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog=new DatePickerDialog(MemberProfileUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mStr_updatedDOB= String.valueOf(year)+ String.format("%02d",month)+ String.format("%02d",dayOfMonth);
                    mBtn_dob.setText(String.valueOf(dayOfMonth)+" : "+ String.valueOf(month)+" : "+ String.valueOf(year));
                }
            },curr_year,curr_month,curr_day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
        if(v==mBtn_submit){
            updateMemberDetails(APILinks.UPDATE_MEMBER_DETAILS);
        }
    }

    private void getMemberDetails(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        JSONObject jsonObject=response.getJSONObject(0);
                        mEt_name.setText(jsonObject.getString("MemberName"));
                        mBtn_dob.setText(jsonObject.getString("DateOfBirthFormatted"));
                        mStr_previousDOB=jsonObject.getString("DateOfBirthFormatted");
                        mStr_updatedDOB = jsonObject.getString("MemberDOB");
                        mEt_father.setText(jsonObject.getString("Father"));
                        mEt_address.setText(jsonObject.getString("Address"));
                        mEt_state.setText(jsonObject.getString("State"));
                        mEt_pincode.setText(jsonObject.getString("Pincode"));
                        mEt_phone.setText(jsonObject.getString("Phone"));
                        mEt_email.setText(jsonObject.getString("Email"));
                        mEt_gender.setText(jsonObject.getString("Gender"));
                        int pos=getbloodGrSpinnerPos(jsonObject.getString("BloodGr"));
                        mStr_bloodGr=jsonObject.getString("BloodGr");
                        mSp_bloodGr.setSelection(pos);
                        //mEt_bloodGr.setText(jsonObject.getString("BloodGr"));

                    }else{
                        Toast.makeText(MemberProfileUpdateActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private int getbloodGrSpinnerPos(String value){
        int limit = arraylistBloodGroup.size();
        for(int i=1;i<limit;i++){
            if(value.equals(arraylistBloodGroup.get(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private void updateMemberDetails(String url){
        HashMap<String, String> hashMap=new HashMap();
        hashMap.put("MCode", GlobalStore.GlobalValue.getMemberCode());
        hashMap.put("MName",mEt_name.getText().toString());
        /*if(mStr_updatedDOB.length()>0){*/
        hashMap.put("MDob",mStr_updatedDOB);
       /* }else{
            hashMap.put("MDob",mStr_previousDOB);
        }*/
        hashMap.put("Father",mEt_father.getText().toString());
        hashMap.put("Address",mEt_address.getText().toString());
        hashMap.put("State",mEt_state.getText().toString());
        hashMap.put("Pincode",mEt_pincode.getText().toString());
        hashMap.put("Email",mEt_email.getText().toString());
        hashMap.put("Phone",mEt_phone.getText().toString());
        hashMap.put("BloodGr",mStr_bloodGr);
        hashMap.put("Gender",mEt_gender.getText().toString());

        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if(response!=null){
                        if(response.getString("status").equals("1")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MemberProfileUpdateActivity.this);
                            builder.setTitle("Success")
                                    .setMessage("Submitted Successfully, Wait for Approval")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(MemberProfileUpdateActivity.this, MemberDashboardActivity.class));
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        }
                                    }).show();
                        }else{
                            Toast.makeText(MemberProfileUpdateActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MemberProfileUpdateActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Edit & Update Member Details");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MemberProfileUpdateActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberProfileUpdateActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
