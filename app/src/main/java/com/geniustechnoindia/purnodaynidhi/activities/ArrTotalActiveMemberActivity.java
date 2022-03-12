package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterTotalActiveMember;
import com.geniustechnoindia.purnodaynidhi.model.SetGetTotalActiveMember;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrTotalActiveMemberActivity extends AppCompatActivity {
    String url="http://purnodayapp.geniustechnoindia.com//GetTotalActiveMember";
    ArrayList<SetGetTotalActiveMember> activeMemberalist=new ArrayList<>();
    RecyclerView rv_activity_arr_loan_details;
    TextView txtactivemember;
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    TextView tv_row_loan_list_loan_code,tv_row_loan_list_holder_name,tv_row_loan_list_loan_amount,tv_row_loan_list_date_of_entry,tvadress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_total_active_member);
        txtactivemember=findViewById(R.id.txtactivemember);

        rv_activity_arr_loan_details=findViewById(R.id.activememberrecycle);

        tv_row_loan_list_loan_code=findViewById(R.id.tv_row_loan_list_loan_code);
       tv_row_loan_list_holder_name=findViewById(R.id.tv_row_loan_list_holder_name);
       tv_row_loan_list_loan_amount=findViewById(R.id.tv_row_loan_list_loan_amount);
       tv_row_loan_list_date_of_entry=findViewById(R.id.tv_row_loan_list_date_of_entry);
      tvadress=findViewById(R.id.tvaddress);


        GetAllmMemberData(url);

    }



    private void GetAllmMemberData(String url){
        RequestQueue queue= Volley.newRequestQueue(ArrTotalActiveMemberActivity.this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String ItemNo=jsonObject.getString("ItemNo");
                        String MemberCode=jsonObject.getString("MemberCode");
                        String MemberName=jsonObject.getString("MemberName");
                        String Address=jsonObject.getString("Address");
                        String Phone=jsonObject.getString("Phone");


                        SetGetTotalActiveMember sg=new SetGetTotalActiveMember( ItemNo,MemberCode,MemberName,Address,Phone);
                        activeMemberalist.add(sg);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RecyclerView.LayoutManager LM=new LinearLayoutManager(ArrTotalActiveMemberActivity.this,LinearLayoutManager.VERTICAL,false);
                    rv_activity_arr_loan_details.setLayoutManager(LM);
                    rv_activity_arr_loan_details.setHasFixedSize(true);
                   AdapterTotalActiveMember Adapter=new AdapterTotalActiveMember(activeMemberalist,ArrTotalActiveMemberActivity.this);
                    rv_activity_arr_loan_details.setAdapter(Adapter);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonArrayRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ArrTotalActiveMemberActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    }


