package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MISCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    // views
    private TextInputEditText mTie_depositAmount;
    private TextInputEditText mTie_annualInterestRate;
    private TextInputEditText mTie_durationInYears;

    private TextView mTv_compoundingInterestTitle;
    private TextView mTv_monthlyInterest;
    private TextView mTv_totalInterest;
    private TextView mTv_totalAmount;

    private Spinner mSp_compundIn;

    private LinearLayout mLl_resultRoot;

    private Button mBtn_reset;
    private Button mBtn_calculate;

    private List<String> spinnerArrayCompounding;

    // Toolbar
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private int currentSelectedItem = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_calculator);

        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbarTitle.setText("MIS Calculator");

        // Populate Spinner
        spinnerArrayCompounding = new ArrayList<>();
        spinnerArrayCompounding.add("Monthly");
        spinnerArrayCompounding.add("Quarterly");
        spinnerArrayCompounding.add("Half yearly");
        spinnerArrayCompounding.add("Yearly");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayCompounding);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_compundIn.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mSp_compundIn.setAdapter(spinnerAdapter);

        mSp_compundIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        currentSelectedItem = position;
                        break;
                    case 1:
                        currentSelectedItem = position;
                        break;
                    case 2:
                        currentSelectedItem = position;
                        break;
                    case 3:
                        currentSelectedItem = position;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MISCalculatorActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewReferences() {
        mTie_depositAmount = findViewById(R.id.tie_activity_mis_calculator_deposit_amount);
        mTie_annualInterestRate = findViewById(R.id.tie_activity_mis_calculator_annual_interest_rate);
        mTie_durationInYears = findViewById(R.id.tie_activity_mis_calculator_duration_in_years);

        mTv_compoundingInterestTitle = findViewById(R.id.tv_activity_mis_calculator_compounding_interest_title);
        mTv_monthlyInterest = findViewById(R.id.tv_activity_mis_calculator_monthly_interest);
        mTv_totalInterest = findViewById(R.id.tv_activity_mis_calculator_total_interest);
        mTv_totalAmount = findViewById(R.id.tv_activity_mis_calculator_total_amount);

        mLl_resultRoot = findViewById(R.id.ll_activity_mis_calculator_root);

        mSp_compundIn = findViewById(R.id.sp_activity_mis_calculator_compounding);

        mBtn_reset = findViewById(R.id.btn_activity_mis_calculator_reset);
        mBtn_calculate = findViewById(R.id.btn_activity_mis_calculator_calculate);

        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
    }

    private void bindEventHandlers() {
        mBtn_reset.setOnClickListener(this);
        mBtn_calculate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_calculate) {
            if (mTie_depositAmount.getText().toString().trim().length() > 0) {
                if (mTie_annualInterestRate.getText().toString().trim().length() > 0) {
                    if (mTie_durationInYears.getText().toString().trim().length() > 0) {
                        switch (currentSelectedItem) {
                            case 0: {
                                mTv_compoundingInterestTitle.setText("Monthly Interest");
                                calculateMonthly(mTie_depositAmount.getText().toString(), mTie_durationInYears.getText().toString(), mTie_annualInterestRate.getText().toString(), "monthly");
                                mLl_resultRoot.setVisibility(View.VISIBLE);
                                break;
                            }
                            case 1: {
                                mTv_compoundingInterestTitle.setText("Quarterly Interest");
                                calculateMonthly(mTie_depositAmount.getText().toString(), mTie_durationInYears.getText().toString(), mTie_annualInterestRate.getText().toString(), "quarterly");
                                mLl_resultRoot.setVisibility(View.VISIBLE);
                                break;
                            }
                            case 2: {
                                mTv_compoundingInterestTitle.setText("Half yearly Interest");
                                calculateMonthly(mTie_depositAmount.getText().toString(), mTie_durationInYears.getText().toString(), mTie_annualInterestRate.getText().toString(), "halfyearly");
                                mLl_resultRoot.setVisibility(View.VISIBLE);
                                break;
                            }
                            case 3: {
                                mTv_compoundingInterestTitle.setText("Yearly Interest");
                                calculateMonthly(mTie_depositAmount.getText().toString(), mTie_durationInYears.getText().toString(), mTie_annualInterestRate.getText().toString(), "Yearly");
                                mLl_resultRoot.setVisibility(View.VISIBLE);
                                break;
                            }
                        }

                    } else {
                        mTie_durationInYears.setError("Enter duration");
                        mTie_durationInYears.requestFocus();
                    }
                } else {
                    mTie_annualInterestRate.setError("Enter interest rate");
                    mTie_annualInterestRate.requestFocus();
                }
            } else {
                mTie_depositAmount.setError("Enter amount");
                mTie_depositAmount.requestFocus();
            }
        } else if (v == mBtn_reset) {
            mTie_depositAmount.setText("");
            mTie_durationInYears.setText("");
            mTie_annualInterestRate.setText("");
            mLl_resultRoot.setVisibility(View.GONE);
        }
    }

    private void calculateMonthly(String depositAmount, String duraion, String interestRate, String compounding) {
        double depositAmnt = Double.parseDouble(depositAmount);
        double duration = Double.parseDouble(duraion);
        double intRate = Double.parseDouble(interestRate);

        // (p * t * r ) / 100
        double interestReceive = 0.0;
        double totalMonths = (duration * 12);

        if (compounding.equals("monthly")) {
            interestReceive = (depositAmnt * duration * intRate) / 100;
            double monthlyIncome = (interestReceive / totalMonths);                                    // Monthly income
            double totalInterest = totalMonths * monthlyIncome;
            mTv_monthlyInterest.setText((Math.round(monthlyIncome)) + "");
            mTv_totalInterest.setText(Math.round(totalInterest) + "");
            double totalAmount = depositAmnt + totalInterest;
            mTv_totalAmount.setText(Math.round(totalAmount) + "");
        } else if (compounding.equals("quarterly")) {                                               // Quarterly income
            interestReceive = ((depositAmnt * duration * intRate) / 100) * 3;
            double monthlyIncome = (interestReceive / totalMonths);
            double calculatedDuration = ((duration * 12) / 3);
            double totalInterest = calculatedDuration * monthlyIncome;
            mTv_monthlyInterest.setText((Math.round(monthlyIncome) + ""));
            mTv_totalInterest.setText(Math.round(totalInterest) + "");
            double totalAmount = depositAmnt + totalInterest;
            mTv_totalAmount.setText(Math.round(totalAmount) + "");
        } else if (compounding.equals("halfyearly")) {                                              // Half yearly income
            interestReceive = ((depositAmnt * duration * intRate) / 100) * 6;
            double monthlyIncome = (interestReceive / totalMonths);
            double calculatedDuration = ((duration * 12) / 6);
            double totalInterest = calculatedDuration * monthlyIncome;
            mTv_monthlyInterest.setText((Math.round(monthlyIncome) + ""));
            mTv_totalInterest.setText(Math.round(totalInterest) + "");
            double totalAmount = depositAmnt + totalInterest;
            mTv_totalAmount.setText(Math.round(totalAmount) + "");
        } else if (compounding.equals("Yearly")) {                                                  // Yearly income
            interestReceive = ((depositAmnt * duration * intRate) / 100) * 12;
            double monthlyIncome = (interestReceive / totalMonths);
            double totalInterest = duration * monthlyIncome;
            mTv_monthlyInterest.setText((Math.round(monthlyIncome)) + "");
            mTv_totalInterest.setText(Math.round(totalInterest) + "");
            double totalAmount = depositAmnt + totalInterest;
            mTv_totalAmount.setText(Math.round(totalAmount) + "");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MISCalculatorActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
