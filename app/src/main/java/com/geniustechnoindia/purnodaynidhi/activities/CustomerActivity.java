package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {

    // views
    private Toolbar mToolbar;
    private TextView mTv_toolbar;

    private TextView mTv_aboutUs;

    private TextView mTv_goldLoan;
    private TextView mTv_mortgageLoan;
    private TextView mTv_termDeposits;
    private TextView mTv_savingsDeposits;
    private TextView mTv_recurringDeposits;
    private TextView mTv_monthlyIncome;

    private TextView mTv_showSelectedInfo;

    // vars
    private static String aboutUsBodyStr = "";
    private static String showGoldLoanStr = "";
    private static String mortgageLoanStr = "";
    private static String termDepositsStr = "";
    private static String savingsDepositStr = "";
    private static String recurringDepositStr = "";
    private static String monthlyIncomeStr = "";

    private ImageView mIv_selectedInfoImage;

    private LinearLayout mLl_selectedInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbar.setText(getString(R.string.App_Full_Name));

        // ----- ABOUT
        /*aboutUsBodyStr = "<b> NIDHI LIMITED Head Office (Chandigarh)</b> Incorporated under company act 2013, under Nidhi\n" +
                "Rules 2014, by the innovative leadership of inspired professional s in this revolutionary market trend.\n" +
                "A company declared as a Nidhi <b>(NBFC)</b> under section 620(A) of the Companies Act,1956. Nidhi\n" +
                "working under the guide line of R.B.I\n" +
                "<b> is our Trademark and Brand Name. We have works in Four States (PB, HP, HR&amp;UP) on\n" +
                "the</b>\n" +
                "Name of  (SAHYOG/ SAHARSH/SURAKSHA/VIKAS) Nidhi Ltd.";

        showGoldLoanStr = "<b> Nidhi Ltd. Gold loans may be availed for any amount between Rs.1,000 to a maximum of\n" +
                "Rs.2,00,000Lakhs.</b>\n" +
                "Loans are available for periods ranging from one month to one year. You can repay earlier than the scheduled as\n" +
                "you desire.";

        mortgageLoanStr = "<b> Nidhi Ltd. Mortgage Loan (ML).</b>\n" +
                "Are you looking to expand your business/ to get your beloved child married/to give better higher education to your\n" +
                "Child.Loan Amount is depending upon Documentation.";

        termDepositsStr = "<b>Depositors can choose from a wide range of deposit products</b>\n" +
                "with maturities ranging from 06-60 months at competitive rates of interest and with different features to suit the\n" +
                "investment needs of individual Members. Now you can starts Fixed Deposit with Minimum Amount of Rs 5000/-\n" +
                "ROI Starts with (9.50-12.50)% for 1ys to 5 ys.";

        savingsDepositStr = "<b> Nidhi Ltd. Saving account encourages savings habit among salary earners and others who have\n" +
                "fixed income.</b>\n" +
                "It enables the depositor to earn income by way of highest interest. And also provides you a better service. Now\n" +
                "you starts saving Account just with Minimum Balance of Rs 500/- &amp; Rate of Interest is 5.0% per Year.";

        recurringDepositStr = "<b>Recurring deposit account is opened by those who want to save regularly for a certain period of time and\n" +
                "earn a higher interest rate.</b>\n" +
                "In recurring deposit account Minimum. than the scheduled as you desire. Now you can start just Rs 500 and\n" +
                "More with Minimum 1ys and Maximum 5ys against ROI (10.0-12.5) %.";

        monthlyIncomeStr = "<b>Monthly Income Plan is a traditional participating product, which provides deferred regular income to the\n" +
                "customer for 5 years.</b>\n" +
                "Monthly Income Plan is a traditional participating product, which provides you long term of regular Benefit 1% Per\n" +
                "Month of Deposit Amount &amp; also provides you 10% of Bonus with your Principle Amount at on your Maturity.";
*/

        // about us
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mTv_aboutUs.setText(Html.fromHtml(aboutUsBodyStr, Html.FROM_HTML_MODE_LEGACY));
        } else {
            mTv_aboutUs.setText(Html.fromHtml(aboutUsBodyStr));
        }

        // ----- GOLD LOANS


    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbar = findViewById(R.id.toolbar_title);
        mTv_aboutUs = findViewById(R.id.tv_activity_customer_about_us);

        mTv_goldLoan = findViewById(R.id.tv_activity_customer_gold_loan);
        mTv_mortgageLoan = findViewById(R.id.tv_activity_customer_mortgage_loan);
        mTv_termDeposits = findViewById(R.id.tv_activity_customer_term_deposit);
        mTv_savingsDeposits = findViewById(R.id.tv_activity_customer_savings_deposit);
        mTv_recurringDeposits = findViewById(R.id.tv_activity_customer_recurring_deposit);
        mTv_monthlyIncome = findViewById(R.id.tv_activity_customer_monthly_income);

        mIv_selectedInfoImage = findViewById(R.id.iv_activity_customer_selected_info_image);

        mTv_showSelectedInfo = findViewById(R.id.tv_activity_show_customer_gold_loan_plan);

        mLl_selectedInfo = findViewById(R.id.ll_selected_info_layout_root);
    }

    private void bindEventHandlers() {
        mTv_goldLoan.setOnClickListener(this);
        mTv_mortgageLoan.setOnClickListener(this);
        mTv_termDeposits.setOnClickListener(this);
        mTv_savingsDeposits.setOnClickListener(this);
        mTv_recurringDeposits.setOnClickListener(this);
        mTv_monthlyIncome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_goldLoan) {
            // ic_gold
            mTv_goldLoan.setBackgroundResource(R.drawable.background_white_border);
            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_termDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_red_border);

            mIv_selectedInfoImage.setBackgroundResource(R.drawable.ic_gold);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTv_showSelectedInfo.setText(Html.fromHtml(showGoldLoanStr, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTv_showSelectedInfo.setText(Html.fromHtml(showGoldLoanStr));
            }
            mLl_selectedInfo.setVisibility(View.VISIBLE);
        } else if (v == mTv_mortgageLoan) {
            // ic_mortgage
            mTv_goldLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_white_border);
            mTv_termDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_red_border);

            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_white_border);
            mIv_selectedInfoImage.setBackgroundResource(R.drawable.ic_mortgage);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTv_showSelectedInfo.setText(Html.fromHtml(mortgageLoanStr, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTv_showSelectedInfo.setText(Html.fromHtml(mortgageLoanStr));
            }
            mLl_selectedInfo.setVisibility(View.VISIBLE);
        } else if (v == mTv_termDeposits) {
            // ic_term
            mTv_goldLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_termDeposits.setBackgroundResource(R.drawable.background_white_border);
            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_red_border);


            mTv_termDeposits.setBackgroundResource(R.drawable.background_white_border);
            mIv_selectedInfoImage.setBackgroundResource(R.drawable.ic_term);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTv_showSelectedInfo.setText(Html.fromHtml(termDepositsStr, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTv_showSelectedInfo.setText(Html.fromHtml(termDepositsStr));
            }
            mLl_selectedInfo.setVisibility(View.VISIBLE);
        } else if (v == mTv_savingsDeposits) {
            // ic_savings
            mTv_goldLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_termDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_white_border);
            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_red_border);


            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_white_border);
            mIv_selectedInfoImage.setBackgroundResource(R.drawable.ic_savings);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTv_showSelectedInfo.setText(Html.fromHtml(savingsDepositStr, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTv_showSelectedInfo.setText(Html.fromHtml(savingsDepositStr));
            }
            mLl_selectedInfo.setVisibility(View.VISIBLE);
        } else if (v == mTv_recurringDeposits) {
            // ic_recurring
            mTv_goldLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_termDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_white_border);
            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_red_border);

            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_white_border);
            mIv_selectedInfoImage.setBackgroundResource(R.drawable.ic_recurring);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTv_showSelectedInfo.setText(Html.fromHtml(recurringDepositStr, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTv_showSelectedInfo.setText(Html.fromHtml(recurringDepositStr));
            }
            mLl_selectedInfo.setVisibility(View.VISIBLE);
        } else if (v == mTv_monthlyIncome) {
            // ic_monthly
            mTv_goldLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_mortgageLoan.setBackgroundResource(R.drawable.background_red_border);
            mTv_termDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_savingsDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_recurringDeposits.setBackgroundResource(R.drawable.background_red_border);
            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_white_border);


            mTv_monthlyIncome.setBackgroundResource(R.drawable.background_white_border);
            mIv_selectedInfoImage.setBackgroundResource(R.drawable.ic_monthly);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTv_showSelectedInfo.setText(Html.fromHtml(monthlyIncomeStr, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTv_showSelectedInfo.setText(Html.fromHtml(monthlyIncomeStr));
            }
            mLl_selectedInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_menu_contact_us) {
            startActivity(new Intent(CustomerActivity.this, ContactUsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(CustomerActivity.this, CustomerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(CustomerActivity.this, LoginOptionsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
