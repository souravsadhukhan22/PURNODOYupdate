package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterArrMemberDetails;
import com.geniustechnoindia.purnodaynidhi.model.SetGetMemberDetails;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONObject;

import java.util.ArrayList;

public class ArrangerMemberDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private RadioGroup radioGroup;
    private RadioButton mRb_all, mRb_name, mRb_code;
    private Button mBtn_search;
    private RecyclerView mRv_memberDetails;
    private String mStr_type="";
    private EditText mEt_value;

    private SetGetMemberDetails setGetMemberDetails;
    private ArrayList<SetGetMemberDetails> arrayList_setGetMemberDetails;
    private AdapterArrMemberDetails adapterArrMemberDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_member_details);

        setViewReferences();
        bindEventHandlers();
        setUpTollBar();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_memberDetails.setLayoutManager(linearLayoutManager);
        arrayList_setGetMemberDetails=new ArrayList<>();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mRb_all.isChecked()){
                    mStr_type="All";
                    mEt_value.setText("");
                }
                if(mRb_name.isChecked()){
                    mStr_type="name";
                    mEt_value.setVisibility(View.VISIBLE);
                    mEt_value.setText("");
                    mEt_value.setHint("Enter Member Name");
                }
                if(mRb_code.isChecked()){
                    mStr_type="code";
                    mEt_value.setVisibility(View.VISIBLE);
                    mEt_value.setText("");
                    mEt_value.setHint("Enter Member Code");
                }
            }
        });
    }


    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        radioGroup = findViewById(R.id.rg_activity_arr_member_details);
        mRb_all = findViewById(R.id.rb_activity_arr_member_details_all);
        mRb_name = findViewById(R.id.rb_activity_arr_member_details_name);
        mRb_code = findViewById(R.id.rb_activity_arr_member_details_code);
        mBtn_search = findViewById(R.id.btn_activity_arr_member_details_search);
        mRv_memberDetails=findViewById(R.id.rv_activity_arr_member_details);
        mEt_value=findViewById(R.id.et_activity_arr_member_details_value);
    }

    private void bindEventHandlers() {
        mBtn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtn_search){
            if(mStr_type.length()>0){
                getMemberList(APILinks.ARRANGER_GET_MEMBER_LIST+mStr_type+"&value="+mEt_value.getText().toString());
            }else{
                Toast.makeText(this, "Choose search parameter All/Name/Code", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void getMemberList(String url){
        arrayList_setGetMemberDetails.clear();
        new GetDataParserArray(this, url, true, response -> {
            try{
                if(response.length()>0){
                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject=response.getJSONObject(i);
                        setGetMemberDetails=new SetGetMemberDetails();
                        setGetMemberDetails.setMemberCode(jsonObject.getString("MemberCode"));
                        setGetMemberDetails.setName(jsonObject.getString("MemberName"));
                        setGetMemberDetails.setOfcId(jsonObject.getString("OfcID"));
                        setGetMemberDetails.setJoinDate(jsonObject.getString("DateOfJoin"));
                        setGetMemberDetails.setDob(jsonObject.getString("MemberDOB"));
                        setGetMemberDetails.setFather(jsonObject.getString("Father"));
                        setGetMemberDetails.setAddr(jsonObject.getString("Addr"));
                        setGetMemberDetails.setState(jsonObject.getString("StateofMem"));
                        setGetMemberDetails.setEmail(jsonObject.getString("Email"));
                        setGetMemberDetails.setPhone(jsonObject.getString("Phone"));
                        setGetMemberDetails.setGender(jsonObject.getString("Gender"));
                        setGetMemberDetails.setIdProof(jsonObject.getString("IDProof"));
                        setGetMemberDetails.setIdProofNo(jsonObject.getString("IDProofNo"));
                        setGetMemberDetails.setPanNo(jsonObject.getString("PanNo"));

                        arrayList_setGetMemberDetails.add(setGetMemberDetails);
                    }

                    //adapterArrMemberDetails.notifyDataSetChanged();
                    adapterArrMemberDetails=new AdapterArrMemberDetails(ArrangerMemberDetailsActivity.this,arrayList_setGetMemberDetails);
                    mRv_memberDetails.setAdapter(adapterArrMemberDetails);
                }else{
                    Toast.makeText(ArrangerMemberDetailsActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
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
        mTv_toolbarTitle.setText("Search Member Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrangerMemberDetailsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        Intent intent = new Intent(ArrangerMemberDetailsActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
