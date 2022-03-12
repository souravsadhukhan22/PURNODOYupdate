package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

public class InvestmentPlanDetailsActivity extends AppCompatActivity {
    private String mStr_planCode = "";
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_scheme, mTv_term, mTv_mode, mTv_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_plan_details);

        mStr_planCode = getIntent().getExtras().getString("PLANCODE");

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();
        getPlanListDetails(APILinks.GET_INVESTMENT_PLAN_DETAILS + mStr_planCode);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_scheme = findViewById(R.id.tv_investment_plan_scheme);
        mTv_term = findViewById(R.id.tv_investment_plan_term);
        mTv_mode = findViewById(R.id.tv_investment_plan_mode);
        mTv_amount = findViewById(R.id.tv_investment_plan_amount);
    }

    private void bindEventHandlers() {
    }


    private void setUpTollBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (mStr_planCode.equals("DRD")) {
            mTv_toolbarTitle.setText("Daily Deposit");
        }
        if (mStr_planCode.equals("FD")) {
            mTv_toolbarTitle.setText("Fixed Deposit");
        }
        if (mStr_planCode.equals("MIS")) {
            mTv_toolbarTitle.setText("MIS Deposit");
        }
        if (mStr_planCode.equals("RD")) {
            mTv_toolbarTitle.setText("Recurring Deposit");
        }

    }

    private void getPlanListDetails(String url) {
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = response.getJSONObject(0);
                        mTv_scheme.setText(jsonObject.getString("Scheme"));
                        mTv_term.setText(jsonObject.getString("STerm"));
                        mTv_mode.setText(jsonObject.getString("ModeFlag"));
                        mTv_amount.setText(jsonObject.getString("MinAmount"));
                    } else {
                        Toast.makeText(InvestmentPlanDetailsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(InvestmentPlanDetailsActivity.this, PlanDetailsActivity.class));
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
        startActivity(new Intent(InvestmentPlanDetailsActivity.this, PlanDetailsActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
