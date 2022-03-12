package com.geniustechnoindia.purnodaynidhi.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.google.android.material.textfield.TextInputEditText;

public class MemberContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextInputEditText mTie_name;
    private TextInputEditText mTie_mobileNumber;
    private TextInputEditText mTie_area;
    private TextInputEditText mTie_interestedPlan;

    private Button mBtn_contactUs;

    private LinearLayout mLl_number58;
    private LinearLayout mLl_number65;
    private LinearLayout mLl_number14;

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_member);

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

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTie_name = findViewById(R.id.tie_activity_contact_us_your_name);
        mTie_mobileNumber = findViewById(R.id.tie_activity_contact_us_mobile_number);
        mTie_area = findViewById(R.id.tie_activity_contact_us_area);
        mTie_interestedPlan = findViewById(R.id.tie_activity_contact_us_interested_plan);

        mBtn_contactUs = findViewById(R.id.btn_activity_contact_us_contact);

        mLl_number58 = findViewById(R.id.ll_activity_contact_us_number_58);
        mLl_number65 = findViewById(R.id.ll_activity_contact_us_number_65);
        mLl_number14 = findViewById(R.id.ll_activity_contact_us_number_14);

        imageView = findViewById(R.id.iv_activity_contact_us_whatsapp);
    }

    private void bindEventHandlers() {
        mBtn_contactUs.setOnClickListener(this);
        mLl_number58.setOnClickListener(this);
        mLl_number65.setOnClickListener(this);
        mLl_number14.setOnClickListener(this);
        imageView.setOnClickListener(this);
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
                                    new String[]{""});

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
        } else if (v == mLl_number58) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        } else if (v == mLl_number65) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        } else if (v == mLl_number14) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"));
            startActivity(intent);
        } else if (v == imageView) {
            try {
                whatsapp(MemberContactUsActivity.this, "");
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                Uri uri = Uri.parse("smsto:" + "91");
                //sendIntent.setData(Uri.parse("smsto:"+"+91"));
                //sendIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+918011158076" +"&text="+"This is my text to send."));
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.putExtra(Intent.ACTION_SENDTO,uri);

                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);*/


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /////////////////////////////////////////////

    @SuppressLint("NewApi")
    public static void whatsapp(Activity activity, String phone) {
        String formattedNumber = phone;//Util.formatPhone(phone);
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            sendIntent.putExtra("jid", formattedNumber + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            //Toast.makeText(activity, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, "Please Install Whatsapp" , Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberContactUsActivity.this, MemberDashboardActivity.class);
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
        Intent intent = new Intent(MemberContactUsActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
