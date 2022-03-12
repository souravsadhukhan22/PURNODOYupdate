package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.AppIntentData;
import com.geniustechnoindia.purnodaynidhi.helper.MyBaseActivity;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDashboardActivity extends MyBaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private LinearLayout mLl_savingsAccount;
    private LinearLayout mLl_investmentAccount;
    private LinearLayout mLl_loanStatement;
    private LinearLayout mLl_loanEmiStatement;
    private LinearLayout mLl_rechargeOption;
    private LinearLayout mLl_root;
    private LinearLayout mLl_planDetails;
    private LinearLayout mLl_loanPlanDetails;

    private TextView mTv_welcomeMessage;

    private LinearLayout mLl_mobileRecharge;
    private LinearLayout mLl_dthRecharge;
    private LinearLayout mLl_dataCardRecharge;
    private LinearLayout mLl_electricityBill;
    private LinearLayout mLl_moneyTransfer;
    private LinearLayout mLl_contactUs;
    //private LinearLayout mLl_profileUpdate;
    private LinearLayout mLl_landLine;
    private LinearLayout mLl_gasBill;
    private LinearLayout mLl_sbToSbTransfer;
    private LinearLayout mLl_waterBill;
    private TextView mTv_loanPayment;
    private TextView mTv_investmentCalculator;

    private TextView mTv_news;


    private LinearLayout mLl_logout;
    private Spinner spinnerSelectCalculator;

    // Side nav drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    // Nav header
    private View navHeader;
    private CircleImageView mIv_navHeaderuserImage;
    private TextView mTv_userName;

    private static String mPinCancelClick = "";

    boolean doubleBackToExitPressedOnce = false;

    private SharedPreferences sharedPreferences;
    private Switch mPin;

    private TextView mTv_policy, mTv_loan,mTv_contactUS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_dashboard_activity);

        setViewReferences();
        bindEventHandlers();

        //getNewUpdateFlash(APILinks.GET_NEWS_FLASH);

        /*if (inactivityTimeOut) {
            // TODO Show not cancellable dialog that user has been auto logged out
            showAutoLogoutDialog(this);
        }*/


        getMemberImage(APILinks.GET_MEMBER_PIC_BY_MEMBER_CODE+GlobalStore.GlobalValue.getMemberCode());

        sharedPreferences = getSharedPreferences("MemberLogin", MODE_PRIVATE);

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Member Dashboard");

        String name = GlobalStore.GlobalValue.getMemberName();

        mTv_welcomeMessage.setText("Welcome " + GlobalStore.GlobalValue.getMemberName());

        if (mPinCancelClick.equals("clicked")) {
            showDialogToSetEasyPin("Set Easy PIN", "Now you can set a 4 digit mPIN and login with that mPIN");
        }

        // nav header
        navHeader = navigationView.getHeaderView(0);
        mIv_navHeaderuserImage = navHeader.findViewById(R.id.iv_nav_header_user_image);
        mTv_userName = navHeader.findViewById(R.id.tv_nav_header_user_image);
        mTv_userName.setText(GlobalStore.GlobalValue.getMemberName());

        // Side nav spinner
        spinnerSelectCalculator = (Spinner) navigationView.getMenu().findItem(R.id.nav_drawer_change_language).getActionView();
        final String[] language = {"Calculator", "MIS Calculator"};
        spinnerSelectCalculator.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        // spinner in nav drawer
        spinnerSelectCalculator.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, language));
        spinnerSelectCalculator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    if (position == 1) {
                        startActivity(new Intent(MemberDashboardActivity.this, MISCalculatorActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        setNavigationViewListener();


        /** Drawer Icon toggle **/
        toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); /** Drawer Icon toggle **/
        toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        drawerLayout.setScrimColor(Color.TRANSPARENT);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            private float scaleFactor = 6f;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                mLl_root.setTranslationX(slideX);
                //mLl_root.setScaleX(1 - (slideOffset / scaleFactor));
                //mLl_root.setScaleY(1 - (slideOffset / scaleFactor));
                mLl_root.setTranslationX(slideX);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        mPin = (Switch) navigationView.getMenu().getItem(2).getActionView();
        mPin.setOnCheckedChangeListener((buttonView, isChecked) -> checkIfMPinIsSetOrNot(APILinks.CHECK_MPIN_SET_OR_NOT + GlobalStore.GlobalValue.getMemberCode(), isChecked));

        if (sharedPreferences.getBoolean("MPIN", false)) {
            mPin.setChecked(true);
            //mPin.setChecked(true);
        } else {
            mPin.setChecked(false);
        }




        /*if (GlobalStore.GlobalValue.getAppEasyPinSetOrNot().equals("")) {

        }*/
    }


    private void getMemberImage(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        mIv_navHeaderuserImage.setVisibility(View.VISIBLE);
                        String pic=response.get(0).toString();
                        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        mIv_navHeaderuserImage.setImageBitmap(decodedByte);
                    }else{
                        mIv_navHeaderuserImage.setVisibility(View.INVISIBLE);
                        //Toast.makeText(ArrangerActiveMemberSearchActivity.this, "No Pic Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }


    private void showDialogToSetEasyPin(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberDashboardActivity.this);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MemberDashboardActivity.this, MemberSetEasyPINActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                mPinCancelClick = "clicked";
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPinCancelClick = "clicked";
                dialog.cancel();
            }
        }).show();

    }

    private void showAutoLogoutDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_auto_logout);
        dialog.show();

        //Button mBtn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button mBtn_ok = dialog.findViewById(R.id.btn_ok);

        /*mBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });*/
        mBtn_ok.setOnClickListener(v -> {
            context.startActivity(new Intent(MemberDashboardActivity.this, MemberLoginActivity.class));
            finish();
        });
    }

    // Drawer navigation
    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MemberDashboardActivity.this);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        navigationView = findViewById(R.id.nav_view);

        // drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);

        mLl_root = findViewById(R.id.ll_activity_member_dahsboard_root);

        // views
        mLl_savingsAccount = findViewById(R.id.ll_activity_customer_dashboard_savings_account);
        mLl_investmentAccount = findViewById(R.id.ll_activity_customer_dashboard_investment_account);
        mLl_loanStatement = findViewById(R.id.ll_activity_customer_dashboard_loan_statement);
        mLl_loanEmiStatement = findViewById(R.id.ll_activity_customer_dashboard_loan_emi_statement);
        mTv_welcomeMessage = findViewById(R.id.tv_activity_member_dashboard_welcome_message);
        mLl_rechargeOption = findViewById(R.id.ll_activity_customer_dashboard_recharge_option);
        mLl_moneyTransfer = findViewById(R.id.ll_activity_customer_dashboard_money_transfer);
        mLl_logout = findViewById(R.id.ll_activity_member_dashboard_logout);
        mLl_planDetails = findViewById(R.id.ll_activity_customer_dashboard_plan_details);
        mLl_loanPlanDetails = findViewById(R.id.ll_activity_customer_dashboard_loan_plan_details);
        mLl_gasBill = findViewById(R.id.ll_activity_customer_dashboard_gas_bill);
        mTv_investmentCalculator = findViewById(R.id.tv_activity_member_dashboard_investment_calculator);

        mLl_mobileRecharge = findViewById(R.id.ll_activity_customer_dashboard_mobile_recharge);
        mLl_dataCardRecharge = findViewById(R.id.ll_activity_customer_dashboard_datacard);
        mLl_dthRecharge = findViewById(R.id.ll_activity_customer_dashboard_dth_recharge);
        mLl_electricityBill = findViewById(R.id.ll_activity_customer_dashboard_electricity);
        mLl_contactUs = findViewById(R.id.ll_activity_customer_dashboard_contact_us);

        mLl_landLine = findViewById(R.id.ll_activity_customer_dashboard_landline);

        mTv_news = findViewById(R.id.tv_activity_member_dashboard_news);
        mLl_sbToSbTransfer = findViewById(R.id.ll_activity_customer_dashboard_sb_to_sb_transfer);
        mTv_loanPayment = findViewById(R.id.tv_activity_member_dashboard_loan_payment);
        //mLl_profileUpdate=findViewById(R.id.ll_activity_customer_dashboard_profile_update);
        mTv_policy = findViewById(R.id.tv_activity_member_dashboard_policy);
        mTv_loan = findViewById(R.id.tv_activity_member_dashboard_loan);
        mTv_contactUS = findViewById(R.id.tv_member_dashboard_contact_us);
        mLl_waterBill = findViewById(R.id.ll_activity_customer_dashboard_water_bill);
    }

    private void bindEventHandlers() {
        mLl_savingsAccount.setOnClickListener(this);
        mLl_investmentAccount.setOnClickListener(this);
        mLl_loanStatement.setOnClickListener(this);
        mLl_loanEmiStatement.setOnClickListener(this);
        mLl_logout.setOnClickListener(this);
        mLl_rechargeOption.setOnClickListener(this);
        mLl_moneyTransfer.setOnClickListener(this);

        mLl_mobileRecharge.setOnClickListener(this);
        mLl_dataCardRecharge.setOnClickListener(this);
        mLl_dthRecharge.setOnClickListener(this);
        mLl_electricityBill.setOnClickListener(this);
        mLl_contactUs.setOnClickListener(this);
        mLl_planDetails.setOnClickListener(this);
        mLl_loanPlanDetails.setOnClickListener(this);
        //mLl_profileUpdate.setOnClickListener(this);
        mLl_landLine.setOnClickListener(this);
        mLl_gasBill.setOnClickListener(this);
        mLl_sbToSbTransfer.setOnClickListener(this);
        mTv_loanPayment.setOnClickListener(this);
        mTv_investmentCalculator.setOnClickListener(this);
        mTv_policy.setOnClickListener(this);
        mTv_loan.setOnClickListener(this);
        mTv_contactUS.setOnClickListener(this);
        mLl_waterBill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLl_savingsAccount) {
            if (!inactivityTimeOut) {
                startActivity(new Intent(MemberDashboardActivity.this, MemberSavingsAccountActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                showAutoLogoutDialog(this);
            }
        } else if (v == mLl_investmentAccount) {
            if (!inactivityTimeOut) {
                startActivity(new Intent(MemberDashboardActivity.this, InvestmentAccountMemberActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                showAutoLogoutDialog(this);
            }
        } else if (v == mLl_loanStatement) {
            startActivity(new Intent(MemberDashboardActivity.this, LoanStatementMemberActivity.class));
            //startActivity(new Intent(MemberDashboardActivity.this, LoanStatementMemberNewActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            //Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        } else if (v == mLl_loanEmiStatement) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        } else if (v == mLl_logout) {
            if (!inactivityTimeOut) {
                memberLogout();
            } else {
                showAutoLogoutDialog(this);
            }
        } else if (v == mLl_rechargeOption) {
            //Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MemberDashboardActivity.this, MemberRechargeOption.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_moneyTransfer) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberDashboardActivity.this, MemberMoneyTransferNewActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        } else if (v == mLl_mobileRecharge) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberDashboardActivity.this, MemberMobileRechargeNewActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        } else if (v == mLl_dthRecharge) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberDashboardActivity.this, MemberDTHRechargeNewActivity.class));
            overridePendingTransition(R.anim.fade_in,ll_activity_customer_dashboard_landline R.anim.fade_out);
            finish();*/
        } else if (v == mLl_dataCardRecharge) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberDashboardActivity.this, MemberBroadbandRechargeActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        } else if (v == mLl_electricityBill) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberDashboardActivity.this, MemberElectricityBillActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        } else if (v == mLl_contactUs) {
            //Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MemberDashboardActivity.this, MemberContactUsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_planDetails) {
            AppIntentData.select_context = "MEMBER";
            Intent intent = new Intent(MemberDashboardActivity.this, PlanDetailsActivity.class);
            //intent.putExtra("FLAGCONTEXT","MEMBER");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_loanPlanDetails) {
            AppIntentData.select_context_loan_plan = "MEMBER";
            startActivity(new Intent(MemberDashboardActivity.this, LoanPlanDetailsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } /*else if(v==mLl_profileUpdate){
            startActivity(new Intent(MemberDashboardActivity.this, MemberProfileUpdateActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }*/ else if (v == mLl_landLine) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
            /*startActivity(new Intent(MemberDashboardActivity.this, MemberLandlineBillPaymentActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        } else if (v == mLl_gasBill) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
           /* startActivity(new Intent(MemberDashboardActivity.this, MemberGasBillActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        } else if (v == mLl_sbToSbTransfer) {
            startActivity(new Intent(MemberDashboardActivity.this, MemberSBtoSBActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mTv_loanPayment) {
            //startActivity(new Intent(MemberDashboardActivity.this, MemberLoanPaymentActivity.class));
            startActivity(new Intent(MemberDashboardActivity.this, MemberLoanEmiPaymentActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mTv_investmentCalculator) {
            startActivity(new Intent(MemberDashboardActivity.this, MemberInvestmentCalculatorActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mTv_policy){
            startActivity(new Intent(MemberDashboardActivity.this, MemberPolicyActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v== mTv_loan){
            startActivity(new Intent(MemberDashboardActivity.this, MemberLoanActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mTv_contactUS){
            startActivity(new Intent(MemberDashboardActivity.this, MemberContactUSCustomActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if(v == mLl_waterBill){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
           /* startActivity(new Intent(MemberDashboardActivity.this, MemberWaterBillPaymentActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();*/
        }
    }

    private void memberLogout() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberDashboardActivity.this);
        alertDialogBuilder.setTitle("Logout?");
        alertDialogBuilder.setMessage("Are you sure you want to sign out your account?\n\nYou will be redirected to login screen.");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** OTP SCREEN WILL OPEN ON APP STARTUP */
                SharedPreferences sharedPreferences = getSharedPreferences("MemberLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("REMEMBER", "FALSE");
                editor.putString("MEMBERCODE", "");
                editor.putString("MEMBERPASSWORD", "");
                editor.commit();

                startActivity(new Intent(MemberDashboardActivity.this, LoginOptionsActivity.class));
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogBuilder.setCancelable(true);
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MemberDashboardActivity.this, MemberLoginActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_drawer_member_my_profile:
                if (!inactivityTimeOut) {
                    startActivity(new Intent(MemberDashboardActivity.this, MemberProfileViewActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.nav_drawer_member_agent_login:
                if (!inactivityTimeOut) {
                    startActivity(new Intent(MemberDashboardActivity.this, com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.nav_drawer_member_contact_us:
                if (!inactivityTimeOut) {
                    startActivity(new Intent(MemberDashboardActivity.this, MemberContactUsActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            /*case R.id.nav_drawer_member_apply_now:
                if (!inactivityTimeOut) {
                    startActivity(new Intent(MemberDashboardActivity.this, ApplyNowSavingsAccountActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;*/
            case R.id.nav_drawer_member_set_easy_pin:
                startActivity(new Intent(MemberDashboardActivity.this, MemberSetEasyPINActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.nav_drawer_member_logout:
                if (!inactivityTimeOut) {
                    memberLogout();
                } else {
                    showAutoLogoutDialog(this);
                }
                break;
            case R.id.nav_drawer_mis_calculator:
                startActivity(new Intent(MemberDashboardActivity.this, MISCalculatorActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.nav_drawer_change_pin:
                startActivity(new Intent(MemberDashboardActivity.this, MemberChangePassword.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.nav_enable_mpin:

                break;
        }
        return true;
    }


    private void checkIfMPinIsSetOrNot(String url, boolean statusMpin) {
        new GetDataParserArray(MemberDashboardActivity.this, url, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    try {
                        String status = response.getString(0);
                        if (status.equals("yes")) {
                            // TODO Login with mPin
                            if (statusMpin) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("MPIN", true);
                                editor.apply();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("MPIN", false);
                                editor.apply();
                            }

                        } else if (status.equals("no")) {
                            Toast.makeText(this, "MPin not set", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MemberDashboardActivity.this, MemberSetEasyPINActivity.class));
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MemberDashboardActivity.this, "Member code not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (sharedPreferences.getString("REMEMBER", "FALSE").equals("TRUE")) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (sharedPreferences.getString("REMEMBER", "FALSE").equals("FALSE")) {
            startActivity(new Intent(MemberDashboardActivity.this, LoginOptionsActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        /*startActivity(new Intent(MemberDashboardActivity.this, LoginOptionsActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
    }

    private void getNewUpdateFlash(String url) {
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        JSONObject jsonObject = response.getJSONObject(0);
                        if (jsonObject.getInt("Type") == 2 || jsonObject.getInt("Type") == 3) {
                            mTv_news.setText(jsonObject.getString("Message"));
                        }
                    } else {
                        //Toast.makeText(MemberDashboardActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*@Override
    public void onResume() {
        super.onResume();
        if (!inactivityTimeOut) {
            // TODO Show not cancellable dialog that user has been auto logged out
            showAutoLogoutDialog(this);
        }
    }*/

}
