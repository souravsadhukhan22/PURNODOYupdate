package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.google.android.material.textfield.TextInputEditText;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextInputEditText mTie_name;
    private TextInputEditText mTie_mobileNumber;
    private TextInputEditText mTie_area;
    private TextInputEditText mTie_interestedPlan;

    private Button mBtn_contactUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Contact Us");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ContactUsActivity.this, CustomerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(ContactUsActivity.this, CustomerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTie_name = findViewById(R.id.tie_activity_contact_us_your_name);
        mTie_mobileNumber = findViewById(R.id.tie_activity_contact_us_mobile_number);
        mTie_area = findViewById(R.id.tie_activity_contact_us_area);
        mTie_interestedPlan = findViewById(R.id.tie_activity_contact_us_interested_plan);

        mBtn_contactUs = findViewById(R.id.btn_activity_contact_us_contact);
    }

    private void bindEventHandlers() {
        mBtn_contactUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_contactUs) {
            if (mTie_name.getText().toString().trim().length() > 0) {
                if (mTie_mobileNumber.getText().toString().trim().length() > 0) {
                    if (mTie_area.getText().toString().trim().length() > 0) {
                        if (mTie_interestedPlan.getText().toString().trim().length() > 0) {


                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL,
                                    new String[]{""});                  // TODO Add email

                            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                    mTie_name.getText().toString() + "-" + mTie_interestedPlan.getText().toString());

                            emailIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Name : " + mTie_name.getText().toString() + "\n" +
                                            "Mobile number : " + mTie_mobileNumber.getText().toString() + "\n" +
                                            "Area : " + mTie_area.getText().toString() + "\n" +
                                            "Interested plan : " + mTie_interestedPlan.getText().toString());

                            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(Intent.createChooser(
                                        emailIntent, "Send mail..."));
                            }

                        } else {
                            mTie_interestedPlan.setError("Enter interested plan");
                            mTie_interestedPlan.requestFocus();
                        }
                    } else {
                        mTie_area.setError("Enter area");
                        mTie_area.requestFocus();
                    }
                } else {
                    mTie_mobileNumber.setError("Enter mobile number");
                    mTie_mobileNumber.requestFocus();
                }
            } else {
                mTie_name.setError("Enter name");
                mTie_name.requestFocus();
            }
        }
    }
}
