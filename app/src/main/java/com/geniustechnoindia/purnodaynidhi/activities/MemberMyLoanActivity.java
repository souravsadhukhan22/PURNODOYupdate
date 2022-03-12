package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterMemberLoanList;
import com.geniustechnoindia.purnodaynidhi.model.MemberLoanListSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberMyLoanActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private RecyclerView mRv_loanList;

    private AdapterMemberLoanList adapterMemberLoanList;
    private MemberLoanListSetGet memberLoanListSetGet;
    private ArrayList<MemberLoanListSetGet> arrayList_memberLoanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_my_loan);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_loanList.setLayoutManager(linearLayoutManager);
        adapterMemberLoanList=new AdapterMemberLoanList(MemberMyLoanActivity.this,arrayList_memberLoanList);
        mRv_loanList.setAdapter(adapterMemberLoanList);

        getLoanList(APILinks.GET_MEMBER_LOAN_LIST+ GlobalStore.GlobalValue.getMemberCode());
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mRv_loanList=findViewById(R.id.rv_activity_member_my_loan_list);
    }

    private void bindEventHandlers(){}

    private void getLoanList(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            memberLoanListSetGet=new MemberLoanListSetGet();
                            memberLoanListSetGet.setLoanCode(jsonObject.getString("LoanCode"));
                            memberLoanListSetGet.setEmiMode(jsonObject.getString("EMIMode"));
                            memberLoanListSetGet.setEmiAmt(jsonObject.getInt("EMIAmount"));
                            memberLoanListSetGet.setPhone(jsonObject.getString("HolderPhoneNo"));
                            memberLoanListSetGet.setHolderName(jsonObject.getString("HolderName"));
                            memberLoanListSetGet.setClose(jsonObject.getBoolean("IsClose"));
                            arrayList_memberLoanList.add(memberLoanListSetGet);
                        }
                        adapterMemberLoanList.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MemberMyLoanActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Member Loan");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberMyLoanActivity.this, MemberLoanActivity.class);
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
        super.onBackPressed();
        Intent intent = new Intent(MemberMyLoanActivity.this, MemberLoanActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}