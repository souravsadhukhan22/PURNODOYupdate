package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.fragments.MemberLoginUnamePassFragments;
import com.google.android.material.tabs.TabLayout;

public class MemberLoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Viewpager
    private ViewPager viewPagerTwoOptionsLogin;
    private TabLayout tabLayout;

    private ViewPagerAdapterMemberLoginActivity viewPagerAdapterMemberLoginActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        setViewReferences();
        bindEventHandlers();

        // ViewPager
        viewPagerAdapterMemberLoginActivity = new ViewPagerAdapterMemberLoginActivity(getSupportFragmentManager());
        viewPagerTwoOptionsLogin.setAdapter(viewPagerAdapterMemberLoginActivity);
        tabLayout.setupWithViewPager(viewPagerTwoOptionsLogin);
    }

    class ViewPagerAdapterMemberLoginActivity extends FragmentPagerAdapter {

        public ViewPagerAdapterMemberLoginActivity(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MemberLoginUnamePassFragments();
                //case 1:
                    //return new MemberLoginEasyPINFragments();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Welcome";
            } else if(position == 1){
                return "Easy PIN";
            }
            return null;
        }
    }

    private void setViewReferences() {
        viewPagerTwoOptionsLogin = findViewById(R.id.vp_activity_member_login_two_login_options);
        tabLayout = findViewById(R.id.tabs_activity_member_login);
    }

    private void bindEventHandlers() {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberLoginActivity.this, LoginOptionsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
