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
import com.geniustechnoindia.purnodaynidhi.adapters.AdminMemberSearchAdapter;
import com.geniustechnoindia.purnodaynidhi.model.AdminMemberSearchSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminMemberSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private Button mBtn_search;
    private EditText mEt_searchTerm;

    private RecyclerView mRv_memberDetails;
    private ArrayList<AdminMemberSearchSetGet> arrayList;
    private AdminMemberSearchSetGet memberResultSetGet;
    private AdminMemberSearchAdapter memberResultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_member_search);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();
        setupAdapter();
        setupLayoutManager();
    }

    private void setupAdapter() {
        arrayList = new ArrayList<>();
        memberResultAdapter = new AdminMemberSearchAdapter(AdminMemberSearchActivity.this, arrayList);
        mRv_memberDetails.setAdapter(memberResultAdapter);
    }

    private void setupLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminMemberSearchActivity.this);
        mRv_memberDetails.setLayoutManager(linearLayoutManager);
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mRv_memberDetails = findViewById(R.id.rv_activity_admin_search_member);
        mBtn_search = findViewById(R.id.btn_activity_admin_search_member);
        mEt_searchTerm = findViewById(R.id.et_activity_admin_search_member_enter_search_term);
    }

    private void bindEventHandlers() {
        mBtn_search.setOnClickListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mTv_toolbarTitle.setText("Search member");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_search) {
            if (mEt_searchTerm.getText().toString().trim().length() > 0) {
                arrayList.clear();
                // Check type
                if (mEt_searchTerm.getText().toString().matches("[0-9]+")) {                  // type phone
                    getSearchResult(mEt_searchTerm.getText().toString(), "phone");
                } else {                                                                            // type name
                    getSearchResult(mEt_searchTerm.getText().toString(), "name");
                }

            } else {
                mEt_searchTerm.setError("Enter name or ph no");
                mEt_searchTerm.requestFocus();
            }
        }
    }

    private void getSearchResult(String data, String type) {
        arrayList.clear();
        new GetDataParserArray(AdminMemberSearchActivity.this, APILinks.GET_MEMBER_DETAILS + "data=" + data + "&type=" + type, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    int j = 1;
                    if (response.length() > 0) {
                        for (int i = 0; i <= response.length(); i++) {
                            memberResultSetGet = new AdminMemberSearchSetGet();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                memberResultSetGet.setSlNo(j + "");
                                memberResultSetGet.setMemberCode(jsonObject.getString("MemberCode"));
                                memberResultSetGet.setMemberName(jsonObject.getString("MemberName"));
                                memberResultSetGet.setAddress(jsonObject.getString("Address"));
                                memberResultSetGet.setPhone(jsonObject.getString("Phone"));
                                arrayList.add(memberResultSetGet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            memberResultAdapter.notifyDataSetChanged();
                            j++;
                        }
                    } else {
                        Toast.makeText(AdminMemberSearchActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(AdminMemberSearchActivity.this, AdminDashboardActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminMemberSearchActivity.this, AdminDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
