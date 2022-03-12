package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.InvestmentAccountStatementAdapter;
import com.geniustechnoindia.purnodaynidhi.bean.AppData;
import com.geniustechnoindia.purnodaynidhi.bean.LoanData;
import com.geniustechnoindia.purnodaynidhi.model.SetGetInvestmentAccountStatement;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class InvestmentAccountMemberActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextView mTv_investmentAccountPopUp;

    // RecyclerView statement
    private RecyclerView mRv_investmentAccountReport;
    private InvestmentAccountStatementAdapter mInvestmentAccountStatementAdapter;
    private SetGetInvestmentAccountStatement setGetInvestmentAccountStatement;
    private ArrayList<SetGetInvestmentAccountStatement> mArrayList;

    // Popup
    private PopupMenu mPopUpMenuInvestmentAccount;

    // variables
    private static int totalAmount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_investment_account);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Investment Account");

        mPopUpMenuInvestmentAccount = new PopupMenu(InvestmentAccountMemberActivity.this, mTv_investmentAccountPopUp);

        getMenuOptions(GlobalStore.GlobalValue.getMemberCode());

        mArrayList = new ArrayList<>();
        mPopUpMenuInvestmentAccount.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mTv_investmentAccountPopUp.setText(item.getTitle());
                mArrayList.clear();
                totalAmount = 0;
                getInvestmentAccountStatement((String) item.getTitle(), GlobalStore.GlobalValue.getMemberCode(), "");
                return true;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRv_investmentAccountReport.setLayoutManager(linearLayoutManager);
    }

    private boolean getMenuOptions(String memberCode) {
        Connection cn = new SqlManager().getSQLConnection();
        boolean rValue = false;
        String ut = "";
        AppData ad;
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetMemberPolicyCode(?)}");
                smt.setString("@MemberCode", memberCode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                /*String accountCode = smt.getString("AccountCode");
                popupMenusSavingsAccount.getMenu().add(accountCode);*/
                while (rs.next()) {
                    mPopUpMenuInvestmentAccount.getMenu().add(rs.getString("POLICYCODE"));
                    rValue = true;
                }
            }
        } catch (Exception ex) {
            rValue = false;
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (Exception e) {
                    //
                }
            }
        }
        return rValue;
    }

    /**
     * Get savings account statement
     */
    private void getInvestmentAccountStatement(String policyCode, String memberCode, String userType) {
        Connection cn = new SqlManager().getSQLConnection();
        LoanData loanData = new LoanData();

        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetRenewalReportByPolicyCode(?,?,?)}");
                smt.setString("@PolicyCode", policyCode);
                smt.setString("@MemberCode", memberCode);
                smt.setString("@UserType", userType);
                smt.execute();
                ResultSet rs = smt.getResultSet();

                int count = 0;

                while (rs.next()) {
                    //if (count < 10) {
                        setGetInvestmentAccountStatement = new SetGetInvestmentAccountStatement();
                        setGetInvestmentAccountStatement.setPolicyCode("Policy Code - " + rs.getString("PolicyCode"));
                        setGetInvestmentAccountStatement.setInstNo("Inst. No. - " + String.valueOf(rs.getInt("InstNo")));
                        setGetInvestmentAccountStatement.setRenewDate("Renew Date - " + rs.getString("RenewDate"));
                        setGetInvestmentAccountStatement.setAmount("Rs. " + String.valueOf(rs.getInt("Amount")));
                        setGetInvestmentAccountStatement.setLateFine("Late fine - Rs. " + String.valueOf(rs.getInt("LateFine")));
                        mArrayList.add(setGetInvestmentAccountStatement);
                        loanData.setErrorCode(0);
                    //}
                    //count += 1;
                }
                mInvestmentAccountStatementAdapter = new InvestmentAccountStatementAdapter(InvestmentAccountMemberActivity.this, mArrayList);
                mRv_investmentAccountReport.setAdapter(mInvestmentAccountStatementAdapter);
            } else {
                loanData.setErrorCode(2);
                loanData.setErrorString("Network related problem.");
            }
        } catch (Exception ex) {
            loanData.setErrorCode(1);
            loanData.setErrorString(ex.getMessage().toString());
        }
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_investmentAccountPopUp = findViewById(R.id.tv_activity_member_investment_account_select_account_dropdown);
        mRv_investmentAccountReport = findViewById(R.id.rv_investment_account_customer_report);
    }

    private void bindEventHandlers() {
        mTv_investmentAccountPopUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_investmentAccountPopUp) {
            mPopUpMenuInvestmentAccount.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(InvestmentAccountMemberActivity.this, MemberDashboardActivity.class));
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
        startActivity(new Intent(InvestmentAccountMemberActivity.this, MemberDashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
