package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterSearchPolicyByName;
import com.geniustechnoindia.purnodaynidhi.model.SetGetSearchPolicyByName;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrSearchPolicyByNameActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private EditText mEt_name;
    private Button mBtn_Search;
    private RecyclerView mRv_policyList;
    private AdapterSearchPolicyByName adapterSearchPolicyByName;
    private SetGetSearchPolicyByName setGetSearchPolicyByName;
    private ArrayList<SetGetSearchPolicyByName> arrayList_searchPolicyByName=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_search_policy_by_name);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_policyList.setLayoutManager(linearLayoutManager);
        adapterSearchPolicyByName=new AdapterSearchPolicyByName(ArrSearchPolicyByNameActivity.this,arrayList_searchPolicyByName);
        mRv_policyList.setAdapter(adapterSearchPolicyByName);
    }

    private void setViewReferences(){
        mToolbar=findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle=findViewById(R.id.toolbar_title);
        mEt_name=findViewById(R.id.et_activity_arr_search_policy_by_name);
        mBtn_Search=findViewById(R.id.btn_activity_arr_search_policy_by_name_saerch);
        mRv_policyList=findViewById(R.id.rv_activity_arr_search_policy_by_name);
    }

    private void bindEventHandlers(){
        mBtn_Search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtn_Search){
            if(mEt_name.getText().toString().length()>0){
                getPolicyList(APILinks.GET_POLICY_LIST_BY_NAME+mEt_name.getText().toString());
            }
        }
    }

    private void getPolicyList(String url){
        arrayList_searchPolicyByName.clear();
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            setGetSearchPolicyByName=new SetGetSearchPolicyByName();
                            setGetSearchPolicyByName.setPolicyCode(jsonObject.getString("policycode"));
                            setGetSearchPolicyByName.setName(jsonObject.getString("ApplicantName"));
                            setGetSearchPolicyByName.setGuardian(jsonObject.getString("ApplicantGuardian"));

                            arrayList_searchPolicyByName.add(setGetSearchPolicyByName);
                        }
                        adapterSearchPolicyByName.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ArrSearchPolicyByNameActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }


    private void setUpTollBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Policy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrSearchPolicyByNameActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        //super.onBackPressed();
        Intent intent = new Intent(ArrSearchPolicyByNameActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}