package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.databinding.ActivityArrAboutUsBinding;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONException;
import org.json.JSONObject;

public class ArrAboutUsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private ActivityArrAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArrAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        getDetails();

    }

    private void getDetails(){
        new GetDataParserArray(this, APILinks.GET_COMPANY_DETAILS, true, response -> {
            if(response != null){
                if(response.length() > 0){
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        binding.tvAuthorityCapital.setText("Authority capital: Rs." + jsonObject.getDouble("AuthorityCaptial"));
                        binding.tvPaidUpCapital.setText("Paid up capital: Rs. " + String.valueOf(jsonObject.getDouble("PaidUpCapital")));
                        binding.tvCompanyName.setText("Company name: " + jsonObject.getString("CompanyName"));
                        binding.tvCompanyRegAddress.setText("Registered address: " + jsonObject.getString("RegisteredAddress"));
                        binding.tvDateOfIncorp.setText("Reg date: " + jsonObject.getString("DateOfRegistration"));
                        binding.tvCin.setText("CINT: " + jsonObject.getString("CIN"));
                        binding.tvPanNo.setText("PAN no.: " + jsonObject.getString("PANNo"));
                        jsonObject.getString("TANNo");
                        binding.tvStateofReg.setText("State of registration: " + jsonObject.getString("StateOfReg"));
                        binding.tvPhone.setText("Phone: " + jsonObject.getString("Phone"));
                        binding.tvLandline.setText("Landline: " + jsonObject.getString("LandLine"));
                        binding.tvCompanyEmail.setText("Email ID: " + jsonObject.getString("Email"));
                        binding.tvWebsite.setText("Website: " + jsonObject.getString("Website"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ArrAboutUsActivity.this, "Details not found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ArrAboutUsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
    }

    private void bindEventHandlers(){}

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("About Us");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrAboutUsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        super.onBackPressed();
        Intent intent = new Intent(ArrAboutUsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}