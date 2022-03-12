package com.geniustechnoindia.purnodaynidhi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.geniustechnoindia.purnodaynidhi.activities.AgentLoanDueReport;
import com.geniustechnoindia.purnodaynidhi.activities.AgentSavingsStatementActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrAboutUsActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrLoanCollectionManualActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrMemberUpdateInfoActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrOpenSBAccActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrProfileDetailsActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrRenewalCollectionManualActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrTotalActiveMemberActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrTotalActivePolicyActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerAccountOpeningActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerActiveMemberSearchActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerChangePassActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerLoanStatementAndPrint;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerMemberDetailsActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerPolicyRenewViewActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerSBCollectionActivity;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerStatementActivity;
import com.geniustechnoindia.purnodaynidhi.activities.LoanPlanDetailsActivity;
import com.geniustechnoindia.purnodaynidhi.activities.PlanDetailsActivity;
import com.geniustechnoindia.purnodaynidhi.bean.AppIntentData;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.geniustechnoindia.purnodaynidhi.util.OpenLinks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GridView _mnuGrid;
    private TextView mTv_devByGen,tvWalletBalance;
    private Button mBtn_logout;
    // views
    private LinearLayout mLl_newMember;
    private LinearLayout mLl_policyRenewal;
    private LinearLayout mLl_loanEmi;
    private LinearLayout mLl_loanDueReport;
    private LinearLayout mLl_planDetails;
    private LinearLayout mLl_loanPlanDetails;
    private LinearLayout mLl_totalActiveMember;
    private LinearLayout mLl_searActiveMember;
    private LinearLayout mLl_memberUpdateInfo;
    private LinearLayout mLl_memberDetails;
    private LinearLayout mLl_policyInvestment;
    private LinearLayout mLl_sbDeposit;
    private LinearLayout mLlOpenPolicy;
    private LinearLayout mLl_openSbAcc;
    private LinearLayout mLl_loanEmiFlexible;
    private LinearLayout mLl_renewalCollectionFlexible;
    private LinearLayout mlL_totalActivePolicy;
    private TextView mTv_news;

    private LinearLayout mLl_savingsAcc, mLl_loanDetails, mLl_policyDetails;

    private TextView mTv_wlcomeMessage;
    private LinearLayout mLl_statementPrint,mLl_myProfile;
    private SharedPreferences sharedPreferencesArranger;
    boolean doubleBackToExitPressedOnce = false;

    private Button mBtn_changePass;
    private TextView mTv_memberCode,mTv_empCode;
    private LinearLayout mLl_aboutUS;

    private long walletBalance = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewReferences();
        bindEventHandlers();
        //getNewUpdateFlash(APILinks.GET_NEWS_FLASH);

        sharedPreferencesArranger = getSharedPreferences("ARRANGERLOGIN", Context.MODE_PRIVATE);
        mTv_devByGen = findViewById(R.id.tv_activity_main_developed_by);
        mTv_devByGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OpenLinks(com.geniustechnoindia.purnodaynidhi.MainActivity.this).openGeniusTechnology();
            }
        });

        mTv_wlcomeMessage.setText("Welcome " + GlobalStore.GlobalValue.getUserOriginalName());
        mTv_memberCode.setText(GlobalStore.GlobalValue.getArrangerMemberCode());
        mTv_empCode.setText(GlobalStore.GlobalValue.getUserName());

        getWalletBalance(APILinks.GET_ARRANGER_WALLET_BALANCE + GlobalStore.GlobalValue.getUserName());

    }

    private void getWalletBalance(String walletURL) {
        new GetDataParserObject(MainActivity.this, walletURL, true, response -> {
            try {
                if (response != null) {
                    walletBalance = response.getLong("bal");
                    tvWalletBalance.setText("Rs. " + walletBalance);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void bindEventHandlers() {
        mLl_newMember.setOnClickListener(this);
        mLl_policyRenewal.setOnClickListener(this);
        mLl_loanEmi.setOnClickListener(this);
        mLl_statementPrint.setOnClickListener(this);
        mBtn_logout.setOnClickListener(this);
        mBtn_changePass.setOnClickListener(this);
mLl_totalActiveMember.setOnClickListener(this);
mlL_totalActivePolicy.setOnClickListener(this);
        mLl_savingsAcc.setOnClickListener(this);
        mLl_loanDetails.setOnClickListener(this);
        mLl_policyDetails.setOnClickListener(this);
        mLl_loanDueReport.setOnClickListener(this);
        mLl_planDetails.setOnClickListener(this);
        mLl_loanPlanDetails.setOnClickListener(this);
        mLl_searActiveMember.setOnClickListener(this);
        mLl_memberUpdateInfo.setOnClickListener(this);
        mLl_memberDetails.setOnClickListener(this);
        mLl_myProfile.setOnClickListener(this);
        mLl_policyInvestment.setOnClickListener(this);
        mLl_aboutUS.setOnClickListener(this);
        mLl_sbDeposit.setOnClickListener(this);
        mLlOpenPolicy.setOnClickListener(this);
        mLl_openSbAcc.setOnClickListener(this);
        mLl_loanEmiFlexible.setOnClickListener(this);
        mLl_renewalCollectionFlexible.setOnClickListener(this);
    }

    private void setViewReferences() {
        mLl_newMember = findViewById(R.id.ll_activity_main_new_member);
        mLl_policyRenewal = findViewById(R.id.ll_activity_main_policy_renewal);
        mLl_loanEmi = findViewById(R.id.ll_activity_main_loan_emi);
        mLl_loanEmiFlexible=findViewById(R.id.ll_activity_main_loan_emi_flexible);
        mLl_renewalCollectionFlexible=findViewById(R.id.ll_activity_main_policy_investment_flexible);
        mTv_wlcomeMessage = findViewById(R.id.tv_activity_main_welcome_message);
        mLl_statementPrint = findViewById(R.id.ll_activity_main_statement_print);
        mBtn_logout = findViewById(R.id.btn_activity_main_agent_logout);
        mBtn_changePass = findViewById(R.id.btn_activity_main_change_password);

        mLl_savingsAcc = findViewById(R.id.ll_activity_main_savings_statement);
        mLl_loanDetails = findViewById(R.id.ll_activity_main_savings_loan_details);
        mLl_policyDetails = findViewById(R.id.ll_activity_main_savings_policy_details);
        mLl_loanDueReport = findViewById(R.id.ll_activity_main_savings_loan_due_report);
        mLl_planDetails = findViewById(R.id.ll_activity_main_plan_details);
        mLl_loanPlanDetails = findViewById(R.id.ll_activity_main_loan_plan_details);
        mLl_totalActiveMember=findViewById(R.id.ll_activity_main_total_active_member);
        mlL_totalActivePolicy=findViewById(R.id.ll_activity_main_total_active_policy);
        mLl_searActiveMember=findViewById(R.id.ll_activity_main_active_search_member);
        mTv_memberCode =findViewById(R.id.tv_activity_main_member_code);
        mTv_empCode=findViewById(R.id.tv_activity_main_employee_code);
        mLl_memberUpdateInfo=findViewById(R.id.ll_activity_main_update_member_info);
        mLl_memberDetails=findViewById(R.id.ll_activity_main_active_member_details);

        mTv_news=findViewById(R.id.tv_activity_main_news);
        mLl_myProfile = findViewById(R.id.ll_activity_main_profile);
        mLl_policyInvestment = findViewById(R.id.ll_activity_main_policy_investment);
        mLl_aboutUS = findViewById(R.id.ll_activity_main_about_us);
        mLl_sbDeposit = findViewById(R.id.ll_activity_main_savings_deposit);

        mLlOpenPolicy = findViewById(R.id.ll_activity_policy_create);

        mLl_openSbAcc = findViewById(R.id.ll_activity_open_savings_account);
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
    }

    @Override
    public void onClick(View v) {
        if (v == mLl_newMember) {
            Intent intent = new Intent(getApplicationContext(), com.geniustechnoindia.purnodaynidhi.NewMemberActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_loanEmi) {
            Intent intent = new Intent(getApplicationContext(), LoanCollectionActivity.class);
            //Intent intent = new Intent(getApplicationContext(), ArrLoanCollectionManualActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_policyRenewal) {
            Intent intent = new Intent(getApplicationContext(), com.geniustechnoindia.purnodaynidhi.RenewalCollectionActivity.class);
            //Intent intent = new Intent(getApplicationContext(), ArrRenewalCollectionManualActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_statementPrint) {
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrangerStatementActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mBtn_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Want to Logout ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferencesArranger.edit();
                            editor.putString("REMEMBER", "FALSE");
                            editor.commit();
                            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, com.geniustechnoindia.purnodaynidhi.AssociateLoginActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (v == mBtn_changePass) {
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrangerChangePassActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_savingsAcc) {
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, AgentSavingsStatementActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_loanDetails) {
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrangerLoanStatementAndPrint.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_policyDetails) {
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrangerPolicyRenewViewActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_loanDueReport) {
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, AgentLoanDueReport.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_planDetails) {
            AppIntentData.select_context = "ARRANGER";
            Intent intent = new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, PlanDetailsActivity.class);
            //intent.putExtra("FLAGCONTEXT","ARRANGER");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v == mLl_loanPlanDetails) {
            AppIntentData.select_context_loan_plan = "ARRANGER";
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, LoanPlanDetailsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mLl_searActiveMember){
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrangerActiveMemberSearchActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(v==mLl_memberUpdateInfo){
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrMemberUpdateInfoActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else if(v==mLl_memberDetails){
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrangerMemberDetailsActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else if(v==mLl_myProfile){
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrProfileDetailsActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else if(v==mLl_policyInvestment){
            startActivity(new Intent(MainActivity.this,RenewalCollectionActivity.class));
            //startActivity(new Intent(com.geniustechnoindia.ichasathinidhi.MainActivity.this, ArrPolicyInvestmentActivity.class));
            //startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrSearchPolicyByNameActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else if(v==mLl_aboutUS){
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrAboutUsActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else if(v==mLl_renewalCollectionFlexible){
            startActivity(new Intent(com.geniustechnoindia.purnodaynidhi.MainActivity.this, ArrRenewalCollectionManualActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        else if(v == mLl_sbDeposit){
            startActivity(new Intent(MainActivity.this, ArrangerSBCollectionActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        } else if(v == mLlOpenPolicy){
            //startActivity(new Intent(MainActivity.this, ArrStartNewPolicyActivity.class));
            startActivity(new Intent(MainActivity.this, ArrangerAccountOpeningActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        else if(v == mLl_openSbAcc){
            startActivity(new Intent(MainActivity.this, ArrOpenSBAccActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        else if(v == mLl_loanEmiFlexible){
            startActivity(new Intent(MainActivity.this, ArrLoanCollectionManualActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        else if(v == mLl_totalActiveMember){
            startActivity(new Intent(MainActivity.this, ArrTotalActiveMemberActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        else if(v == mlL_totalActivePolicy){
            startActivity(new Intent(MainActivity.this, ArrTotalActivePolicyActivity.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
    }

    private void getNewUpdateFlash(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        JSONObject jsonObject=response.getJSONObject(0);
                        if(jsonObject.getInt("Type")==2 || jsonObject.getInt("Type")==3){
                            mTv_news.setText(jsonObject.getString("Message"));
                        }
                    }else{
                        //Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    @Override
    public void onBackPressed() {
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
        /*startActivity(new Intent(MemberDashboardActivity.this, LoginOptionsActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
    }
}
