package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterLoanCalculator;
import com.geniustechnoindia.purnodaynidhi.model.SetGetEMIBrakupData;
import com.geniustechnoindia.purnodaynidhi.model.SetGetLoanTableMaster;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LoanEMICalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;
    private ArrayList<SetGetLoanTableMaster> arrayList_loanTableMaster = new ArrayList<>();
    private ArrayList<String> arrayList_loanTableName = new ArrayList<>();
    private SetGetLoanTableMaster setGetLoanTableMaster;
    private Spinner mSp_schemeNamel,mSp_term;
    private int mInt_minTerm = 0, mInt_maxTerm = 0;
    private int mInt_selectedTerm=0;

    private TextView mTv_roi, mTv_emiMode, mTv_loanMinAmt, mTv_loanMaxAmt;
    private ArrayList<String> arrayList_term=new ArrayList<>();
    private Button mBtn_submit;
    private SetGetEMIBrakupData setGetEMIBrakupData;
    private AdapterLoanCalculator adapterLoanCalculator;
    private ArrayList<SetGetEMIBrakupData> arrayList_emiBrakupData=new ArrayList<>();
    private RecyclerView mRv_emiDetails;
    private EditText mEt_loanAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_e_m_i_calculator);


        setViewReferences();
        bindEventHandlers();
        setUpToolBar();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRv_emiDetails.setLayoutManager(linearLayoutManager);
        adapterLoanCalculator=new AdapterLoanCalculator(LoanEMICalculatorActivity.this,arrayList_emiBrakupData);
        mRv_emiDetails.setAdapter(adapterLoanCalculator);

        getValueFromLoanTableMaster(APILinks.GET_VALUE_LOAN_TABLE_MASTER);

        mSp_schemeNamel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    mInt_minTerm = Integer.parseInt(arrayList_loanTableMaster.get(position-1).getLoanMinTerm());
                    mInt_maxTerm = Integer.parseInt(arrayList_loanTableMaster.get(position-1).getLoanMaxTerm());
                    mTv_roi.setText(arrayList_loanTableMaster.get(position-1).getEmiPercent());
                    mTv_emiMode.setText(arrayList_loanTableMaster.get(position-1).getEmiMode());
                    mTv_loanMinAmt.setText(arrayList_loanTableMaster.get(position-1).getLoanMinAmt());
                    mTv_loanMaxAmt.setText(arrayList_loanTableMaster.get(position-1).getLoanMaxAmt());
                    setTerm(mInt_minTerm,mInt_maxTerm);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSp_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mInt_selectedTerm= Integer.parseInt(arrayList_term.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);
        mSp_schemeNamel = findViewById(R.id.sp_activity_loan_emi_calculator_scheme);
        mTv_roi = findViewById(R.id.tv_activity_loan_emi_calculator_roi);
        mTv_emiMode = findViewById(R.id.tv_activity_loan_emi_calculator_emi_mode);
        mTv_loanMinAmt = findViewById(R.id.tv_activity_loan_emi_calculator_min_amt);
        mTv_loanMaxAmt = findViewById(R.id.tv_activity_loan_emi_calculator_max_amt);
        mSp_term = findViewById(R.id.sp_activity_loan_emi_calculator_term);
        mBtn_submit = findViewById(R.id.btn_activity_loan_emi_calculator_submit);
        mRv_emiDetails = findViewById(R.id.rv_activity_loan_emi_calculator_emi_details);
        mEt_loanAmt = findViewById(R.id.et_activity_loan_emi_calculator_loan_amt);
    }


    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtn_submit){
            int loanAmt= Integer.parseInt(mEt_loanAmt.getText().toString());
            int roi= Integer.parseInt(mTv_roi.getText().toString());

            getLoanEMIBrakupForEMICalculator(loanAmt,roi,mInt_selectedTerm,mTv_emiMode.getText().toString());
        }
    }

    private void getLoanEMIBrakupForEMICalculator(int loanAmt, int roi, int period, String mode){
        arrayList_emiBrakupData.clear();
        Connection cn;
        cn= new SqlManager().getSQLConnection();
        try{
            if(cn!=null){
                CallableStatement smt = cn.prepareCall("{call ADROID_GetLoanEMIBrakupForEMICalculator_NEW(?,?,?,?)}");
                smt.setInt("@LoanAmount",loanAmt);
                smt.setInt("@InterestRate",roi);
                smt.setInt("@LoanPeriod",period);
                smt.setString("@Mode",mode);
                smt.execute();

                ResultSet rs=smt.executeQuery();
                while(rs.next()){
                    setGetEMIBrakupData=new SetGetEMIBrakupData();
                    setGetEMIBrakupData.setPeriod(String.valueOf(rs.getInt("PERIOD")));
                    setGetEMIBrakupData.setPayDate(rs.getString("PAYDATE"));
                    setGetEMIBrakupData.setPayment(String.valueOf(rs.getInt("PAYMENT")));
                    setGetEMIBrakupData.setInterest(String.valueOf(rs.getInt("INTEREST")));
                    setGetEMIBrakupData.setPrincipal(String.valueOf(rs.getInt("PRINCIPAL")));
                    setGetEMIBrakupData.setCurrentBal(String.valueOf(rs.getInt("CURRENT_BALANCE")));
                    arrayList_emiBrakupData.add(setGetEMIBrakupData);
                }
                adapterLoanCalculator.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setTerm(int minTerm, int maxTerm){
        arrayList_term.add(String.valueOf(minTerm));
        for(int i=minTerm+1;i<=maxTerm;i++){
            arrayList_term.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_hint, arrayList_term);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_hint_one);
        mSp_term.setAdapter(arrayAdapter);
    }

    private void setLoanValues() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_hint, arrayList_loanTableName);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_hint_one);
        mSp_schemeNamel.setAdapter(arrayAdapter);
    }

    private void getValueFromLoanTableMaster(String url) {
        arrayList_loanTableName.clear();
        arrayList_loanTableMaster.clear();
        arrayList_loanTableName.add("");
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            setGetLoanTableMaster = new SetGetLoanTableMaster();
                            setGetLoanTableMaster.setLoanTableName(jsonObject.getString("LoanTableName"));
                            setGetLoanTableMaster.setLoanMinTerm(String.valueOf(jsonObject.getInt("LoanMinTerm")));
                            setGetLoanTableMaster.setLoanMaxTerm(String.valueOf(jsonObject.getInt("LoanMaxTerm")));
                            setGetLoanTableMaster.setLoanMinAmt(String.valueOf(jsonObject.getInt("LoanMinAmount")));
                            setGetLoanTableMaster.setLoanMaxAmt(String.valueOf(jsonObject.getInt("LoanMaxAmount")));
                            setGetLoanTableMaster.setEmiPercent(String.valueOf(jsonObject.getInt("EMIPercentage")));
                            setGetLoanTableMaster.setEmiMode(jsonObject.getString("EMIMode"));

                            arrayList_loanTableMaster.add(setGetLoanTableMaster);
                            arrayList_loanTableName.add(arrayList_loanTableMaster.get(i).getLoanTableName());
                        }
                        setLoanValues();
                    } else {
                        Toast.makeText(LoanEMICalculatorActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        arrayList_loanTableName.clear();
                        arrayList_loanTableMaster.clear();
                        arrayList_loanTableName.add("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Edit Member Details");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(LoanEMICalculatorActivity.this, MemberLoanActivity.class);
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
        Intent intent = new Intent(LoanEMICalculatorActivity.this, MemberLoanActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
