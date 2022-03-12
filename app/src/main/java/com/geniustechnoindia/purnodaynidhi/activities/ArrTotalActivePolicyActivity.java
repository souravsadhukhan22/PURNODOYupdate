package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterAllTotalActivePolicyList;
import com.geniustechnoindia.purnodaynidhi.model.SetGetAllTotalActivePolicyList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrTotalActivePolicyActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private RecyclerView activepolicyrecycle;
    TextView txtactivepolicy;
    private AdapterAllTotalActivePolicyList adapterAllTotalActivePolicyList;
    private SetGetAllTotalActivePolicyList setGetAllTotalActivePolicyList;
    private ArrayList<SetGetAllTotalActivePolicyList> arrayList_allActivePolicyList = new ArrayList<>();
    String url = "http://purnodayapp.geniustechnoindia.com/GetTotalActivePolicy";


    TextView tv1policy, tv2policy, tv3policy, tv4policy, tv5policy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_total_active_policy);





            activepolicyrecycle=findViewById(R.id.activepolicyrecycle);

txtactivepolicy=findViewById(R.id.txtactivepolicy);
        tv1policy = findViewById(R.id.tv1policy);
        tv2policy = findViewById(R.id.tv2policy);
        tv3policy = findViewById(R.id.tv3policy);
        tv4policy = findViewById(R.id.tv4policy);
        tv5policy = findViewById(R.id.tv5policy);


        GetActivePolicy(url);

    }






    private void GetActivePolicy(String url) {

        RequestQueue pqueue = Volley.newRequestQueue(ArrTotalActivePolicyActivity.this);
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jobj = response.getJSONObject(i);
                        String Itemno = jobj.getString("ItemNo");
                        String PolicyCode = jobj.getString("PolicyCode");
                        String ApplicantName = jobj.getString("ApplicantName");
                        String ApplicantPhone = jobj.getString("ApplicantPhone");
                        String DepositeAmount = jobj.getString("DepositeAmount");

                        SetGetAllTotalActivePolicyList setget = new SetGetAllTotalActivePolicyList(Itemno, PolicyCode, ApplicantName, ApplicantPhone, DepositeAmount);
                        arrayList_allActivePolicyList.add(setget);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    RecyclerView.LayoutManager LM = new LinearLayoutManager(ArrTotalActivePolicyActivity.this, LinearLayoutManager.VERTICAL, false);
                    activepolicyrecycle.setLayoutManager(LM);
                    activepolicyrecycle.setHasFixedSize(true);
                    AdapterAllTotalActivePolicyList policyAdapter = new AdapterAllTotalActivePolicyList(arrayList_allActivePolicyList, ArrTotalActivePolicyActivity.this);
                    activepolicyrecycle.setAdapter(policyAdapter);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        pqueue.add(jsonArrayRequest2);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ArrTotalActivePolicyActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

