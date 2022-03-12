package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.fragments.FragmentPostpaidRechargeNew;
import com.geniustechnoindia.purnodaynidhi.fragments.FragmentPrepaidRechargeNew;
import com.google.android.material.tabs.TabLayout;

public class MemberMobileRechargeNewActivity extends AppCompatActivity implements View.OnClickListener {

    // Viewpager
    private ViewPagerAdapterMobileRecharge viewPagerAdapterMobileRecharge;
    private ViewPager viewpager_mobile_recharge;
    private TabLayout tabLayout;

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_mobile_recharge_new);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();


        viewPagerAdapterMobileRecharge = new ViewPagerAdapterMobileRecharge(getSupportFragmentManager());
        viewpager_mobile_recharge.setAdapter(viewPagerAdapterMobileRecharge);
        tabLayout.setupWithViewPager(viewpager_mobile_recharge);
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        mTv_toolbarTitle.setText("Mobile Recharge");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setViewReferences(){
        // Viewpager
        viewpager_mobile_recharge = findViewById(R.id.viewpager_mobile_recharge);
        tabLayout = findViewById(R.id.tablayout_mobile_recharge);

        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

    }

    private void bindEventHandlers(){}

    class ViewPagerAdapterMobileRecharge extends FragmentPagerAdapter {

        public ViewPagerAdapterMobileRecharge(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FragmentPrepaidRechargeNew();
                case 1:
                    return new FragmentPostpaidRechargeNew();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Prepaid Recharge";
            } else if(position == 1){
                return "Postpaid recharge";
            }
            return null;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MemberMobileRechargeNewActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberMobileRechargeNewActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
