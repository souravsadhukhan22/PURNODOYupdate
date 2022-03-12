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
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterMemberPolicyList;
import com.geniustechnoindia.purnodaynidhi.model.SetGetMemberPolicyList;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberMyPolicyActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private RecyclerView mRv_policyList;
    private AdapterMemberPolicyList adapterMemberPolicyList;
    private SetGetMemberPolicyList setGetMemberPolicyList;
    private ArrayList<SetGetMemberPolicyList> arrayList_memberPolicyList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_my_policy);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_policyList.setLayoutManager(linearLayoutManager);
        adapterMemberPolicyList=new AdapterMemberPolicyList(MemberMyPolicyActivity.this,arrayList_memberPolicyList);
        mRv_policyList.setAdapter(adapterMemberPolicyList);

        getMemberPolicyList(APILinks.GET_MEMBER_POLICY_LIST+ GlobalStore.GlobalValue.getMemberCode());

    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mRv_policyList=findViewById(R.id.rv_activity_member_my_policy_list);
    }

    private void bindEventHandlers(){}

    private void getMemberPolicyList(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            setGetMemberPolicyList=new SetGetMemberPolicyList();
                            setGetMemberPolicyList.setPolicyCode(jsonObject.getString("PolicyCode"));
                            setGetMemberPolicyList.setApplicantName(jsonObject.getString("ApplicantName"));
                            setGetMemberPolicyList.setDateOfCom(jsonObject.getString("DateOfCom"));
                            setGetMemberPolicyList.setMaturityDate(jsonObject.getString("MaturityDate"));
                            setGetMemberPolicyList.setApplicantPhone(jsonObject.getString("ApplicantPhone"));
                            setGetMemberPolicyList.setTerm(jsonObject.getInt("Term"));
                            setGetMemberPolicyList.setMode(jsonObject.getString("MODE"));
                            setGetMemberPolicyList.setAmount(jsonObject.getInt("Amount"));
                            setGetMemberPolicyList.setDepositAmt(jsonObject.getInt("DepositeAmount"));
                            setGetMemberPolicyList.setMaturityAmt(jsonObject.getInt("MaturityAmount"));
                            setGetMemberPolicyList.setIsmatured(jsonObject.getBoolean("IsMatured"));

                            arrayList_memberPolicyList.add(setGetMemberPolicyList);
                        }
                        adapterMemberPolicyList.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MemberMyPolicyActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
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
        mTv_toolbarTitle.setText("My Policy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(MemberMyPolicyActivity.this, MemberDashboardActivity.class);
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
        Intent intent = new Intent(MemberMyPolicyActivity.this, MemberDashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}