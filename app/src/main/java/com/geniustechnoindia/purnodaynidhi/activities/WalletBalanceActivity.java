package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.AppData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.dl.BalanceManagement;
import com.geniustechnoindia.purnodaynidhi.model.BalanceSetGet;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

public class WalletBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextView mTv_walletBalance;
    private TextView mTv_memberCode;

    // Model class
    private BalanceSetGet balanceSetGet;

    private AppData appData = new AppData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallet);

        setViewReference();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        mTv_toolbarTitle.setText("Wallet Balance");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Toast.makeText(this, GlobalStore.GlobalValue.getUserName(), Toast.LENGTH_SHORT).show();

        if(GlobalStore.GlobalValue.getUserName() != null)
        mTv_memberCode.setText("~ Member Code ~\n\n" + GlobalStore.GlobalValue.getUserName());

        getWalletBalance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(WalletBalanceActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.popeout, R.anim.popeup);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getWalletBalance(){
        final ProgressDialog dialog= DialogUtils.showProgressDialog(WalletBalanceActivity.this,"Loading...");
        balanceSetGet = new BalanceSetGet();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        BalanceManagement balanceManagement= new BalanceManagement();
                        try{
                            balanceSetGet = balanceManagement.getBalance(GlobalStore.GlobalValue.getUserName());
                            mTv_walletBalance.setText("Rs. " + balanceSetGet.getBalance());
                        }catch(Exception ex){
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }, 3000);
    }

    private void setViewReference(){
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mTv_walletBalance = findViewById(R.id.tv_activity_view_wallet_balance);
        mTv_memberCode = findViewById(R.id.tv_activity_view_wallet_member_code);
    }

    private void bindEventHandlers(){}

    @Override
    public void onClick(View v) {

    }
}
