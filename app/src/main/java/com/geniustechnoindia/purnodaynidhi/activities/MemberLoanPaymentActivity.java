package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.LatefineAdapter;
import com.geniustechnoindia.purnodaynidhi.bean.ErrorData;
import com.geniustechnoindia.purnodaynidhi.bean.LoanData;
import com.geniustechnoindia.purnodaynidhi.bean.LoanEMIBrakupData;
import com.geniustechnoindia.purnodaynidhi.bean.LoanEMIPaymentData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.dl.LoanManagement;
import com.geniustechnoindia.purnodaynidhi.model.RenewalLatefineSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.MyConfig;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class MemberLoanPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    private TextView mTv_savingsBalance;

    TextView txtLoanHolderName, txtLoanCode, txtLoanDate, txtLoanAmount, txtLoanTerm, txtDepositAmount,
            txtLoanEMIAmount, txtLoanMode, txtLoanEMIFromInstNo, txtLoanEMIToInstNo, devBygen;
    Button btnSearchLoan, btnEMIView, btnLoanEMISave;
    LoanData lData;
    private TextView mTv_actualEmiAmt;


    private Spinner mSp_savingsAcc, mSp_loanAcc;
    private ArrayList<String> arrayList_SavingsAcc = new ArrayList<>();
    private ArrayList<String> arrayList_SavingsBalance = new ArrayList<>();

    int LateFineSummary;
    private boolean isLateFinePaid = false;
    private CheckBox mCb_lateFinePay;

    ArrayList<LoanEMIBrakupData> arrayListBreakUp;
    LoanEMIBrakupData bData;
    String value = "";
    String cDate = "";
String LoanAmount;
    private static int serverDate;      // Server current date
    private static int payDate;         // EMI pay date

    private static int dateDifference;
    private static float lateFineParc;
    private static String loanMode;
    private static double emiAmount;
    private static float finalLateFine;

    private TextView mTv_totalLatefine;
    private RecyclerView mRv_latefine;

    private EditText mEt_enterNoOfEMI;

    private void clearText() {
        txtDepositAmount.setText("");

        txtLoanHolderName.setText("");
        txtLoanCode.setText("");
        txtLoanDate.setText("");
        txtLoanAmount.setText("");
        txtLoanTerm.setText("");
        txtLoanEMIAmount.setText("");
        txtLoanMode.setText("");
        txtLoanEMIFromInstNo.setText("");
        txtLoanEMIToInstNo.setText("");
    }

    private LatefineAdapter latefineAdapter;
    private ArrayList<RenewalLatefineSetGet> arrayList;
    private RenewalLatefineSetGet renewalLatefineSetGet;
    private double totalLatefine = 0.0;

    private LinearLayout mLl_lateFineRoot;
    private boolean isLateFine = false;


    // SMS
    private static String msgBody = "";

    private double totalDepositAmount = 0.0;

    private String phoneNo = "";

    private String mStr_SBAccCode = "", mStr_loanAccCode = "", mStr_savingsBalance = "";
    private ArrayList<String> arrayList_loanAccCode=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_loan_payment);


        // initialize widget
        setViewReferences();
        bindEventHandlers();
        setUpToolbar();
        adapters();
        layoutManagers();

        mCb_lateFinePay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {                                                                    // Arranger is paying late fine
                    isLateFinePaid = true;
                    /*for (int i = 0; i < arrayList.size(); i++) {
                        lateFinePaidCommaSep = lateFinePaidCommaSep + arrayList.get(i).getLatefine() + ",";
                    }*/
                } else {                                                                            // // Arranger not paying late fine
                    isLateFinePaid = false;
                    /*for (int i = 0; i < arrayList.size(); i++) {
                        lateFineNotPaid = lateFineNotPaid + arrayList.get(i).getLatefine() + ",";
                    }*/
                }
            }
        });


        mEt_enterNoOfEMI.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (count > 0) {
                    if (!s.equals("")) {
                        double noOfEmi = Double.parseDouble(s.toString());
                        //totalDepositAmount = (emiAmount * noOfEmi);
                        //txtDepositAmount.setText(totalDepositAmount + "");
                        emiAmount= Double.parseDouble(mTv_actualEmiAmt.getText().toString());
                        totalDepositAmount = ( emiAmount * noOfEmi);
                        txtDepositAmount.setText(totalDepositAmount + "");
                    }
                } else {
                    txtDepositAmount.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });


        mSp_savingsAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    //Toast.makeText(CustomerLoanPaymentActivity.this, mSp_savingsAcc.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    mStr_SBAccCode = arrayList_SavingsAcc.get(position).toString();
                    mStr_savingsBalance = arrayList_SavingsBalance.get(position).toString();
                    mTv_savingsBalance.setText(mStr_savingsBalance);
                    if (mStr_SBAccCode.length() > 0) {
                        getLoanAccCodes(APILinks.GET_LOAN_POLICY_ACC_CODE+"l"+"&mcode="+GlobalStore.GlobalValue.getMemberCode());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSp_loanAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    //Toast.makeText(CustomerLoanPaymentActivity.this, mSp_loanAcc.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    mStr_loanAccCode=arrayList_loanAccCode.get(position).toString();
                    if (mStr_SBAccCode.length() > 0 && mStr_loanAccCode.length() > 0) {
                        fetchValue(mStr_loanAccCode);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getSavingsBalance(APILinks.GET_SB_BALANCE + GlobalStore.GlobalValue.getMemberCode());

    }

    private void getLoanAccCodes(String url) {
        mStr_loanAccCode = "";
        arrayList_loanAccCode.add("");
        new GetDataParserArray(MemberLoanPaymentActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++){
                            arrayList_loanAccCode.add(response.getString(i));
                        }
                        ArrayAdapter<String> arrayAdapter_loanAccCode=new ArrayAdapter(MemberLoanPaymentActivity.this,R.layout.spinner_hint_select,arrayList_loanAccCode);
                        arrayAdapter_loanAccCode.setDropDownViewResource(R.layout.spinner_hint_select);
                        mSp_loanAcc.setAdapter(arrayAdapter_loanAccCode);
                    }else{
                        Toast.makeText(MemberLoanPaymentActivity.this, "No Loan Code found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getSavingsBalance(String url) {
        mStr_SBAccCode = "";
        mStr_savingsBalance = "";
        arrayList_SavingsAcc.add("");
        arrayList_SavingsBalance.add("");
        new GetDataParserArray(MemberLoanPaymentActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            arrayList_SavingsAcc.add(jsonObject.getString("AccountCode"));
                            arrayList_SavingsBalance.add(String.valueOf(jsonObject.getInt("Balance")));
                        }
                        ArrayAdapter<String> arrayAdapter_SavingsAcc = new ArrayAdapter(MemberLoanPaymentActivity.this, R.layout.spinner_hint_select, arrayList_SavingsAcc);
                        arrayAdapter_SavingsAcc.setDropDownViewResource(R.layout.spinner_hint_select);
                        mSp_savingsAcc.setAdapter(arrayAdapter_SavingsAcc);

                    } else {
                        Toast.makeText(MemberLoanPaymentActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // fetch loan by loan code
    private void fetchValue(final String loanCode) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberLoanPaymentActivity.this, "Loading...");
        lData = new LoanData();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        LoanManagement lm = new LoanManagement();
                        try {
                            lData = lm.getLoanData(loanCode);
                            if (lData.getErrorCode() == 0) {

                                txtLoanHolderName.setText(lData.getHolderName());
                                txtLoanCode.setText(lData.getLoanCode());
                                txtLoanDate.setText(lData.getLoanDate());
                                phoneNo = lData.getPhoneNo();
                                serverDate = lData.getServerDate();
                                //String loanDate = lData.getLoanDate();

                                txtLoanAmount.setText(String.valueOf(lData.getLoanApproveAmount()));
                                LoanAmount=String.valueOf(lData.getLoanApproveAmount());
                                txtLoanTerm.setText(String.valueOf(lData.getLoanTerm()));
                                txtLoanEMIAmount.setText(String.valueOf(lData.getEMIAmount()));
                                emiAmount = lData.getEMIAmount();
                                txtLoanMode.setText(lData.getEMIMode());
                                mTv_actualEmiAmt.setText(String.valueOf(lData.getEMIAmount()+lData.getProcessingFee()));
                                loanMode = lData.getEMIMode();
                            } else {
                                Toast.makeText(getApplicationContext(), lData.getErrorString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }

    Connection cn = new SqlManager().getSQLConnection();

    public ArrayList<LoanEMIBrakupData> getLoanEMIBrakupData(String loanCode, Boolean isReducing, Integer f, Integer t, ArrayList<RenewalLatefineSetGet> arrayListLateFine) {
        ArrayList<LoanEMIBrakupData> arrayList = new ArrayList<LoanEMIBrakupData>();
        LoanEMIBrakupData bData = null;
        CallableStatement smt = null;

        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < arrayListLateFine.size(); i++) {
            hashMap.put(arrayListLateFine.get(i).getInstNo(), arrayListLateFine.get(i).getLatefine());
        }

        try {
            if (cn != null) {
                if (isReducing) {
                    smt = cn.prepareCall("{call USP_GetLoanEMIBrakup(?)}");
                } else {
                    smt = cn.prepareCall("{call ADROID_GetLoanEMIBrakupFlat(?)}");
                }
                smt.setString("@LOANCODE", loanCode);
                smt.execute();
                ResultSet rs = smt.executeQuery();
                int i = 0;
                while (rs.next()) {
                    if ((rs.getInt("PERIOD") >= f) && (rs.getInt("PERIOD") <= t)) {
                        bData = new LoanEMIBrakupData();
                        bData.setPeriod(rs.getInt("PERIOD"));
                        bData.setPayDate(rs.getString("PAYDATE"));
                        bData.setPayment(rs.getFloat("PAYMENT"));
                        bData.setInterest(rs.getFloat("INTEREST"));
                        bData.setPrinciple(rs.getFloat("PRINCIPAL"));
                        bData.setCurrentBalance(rs.getFloat("CURRENT_BALANCE"));

                        if (hashMap.get(rs.getInt("PERIOD")) == null) {
                            bData.setLateFineAmt(0);
                        } else {
                            bData.setLateFineAmt(Integer.valueOf(hashMap.get(rs.getInt("PERIOD"))));
                        }
                        // todo

                        bData.setErrorCode(0);
                        arrayList.add(bData);
                        i++;
                    }
                }
            } else {
                bData.setErrorCode(2);
                bData.setErrorString("Network related problem.");
                arrayList.add(bData);
            }
        } catch (Exception ex) {
            bData.setErrorCode(1);
            bData.setErrorString(ex.getMessage().toString());
            arrayList.add(bData);
        }
        return arrayList;
    }

    private void saveLoanEMI(final LoanEMIPaymentData lpd) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberLoanPaymentActivity.this, "Wait...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        LoanManagement lm = new LoanManagement();
                        try {
                            ErrorData err = lm.insertLoanEMIfromMember(lpd,"");
                            if (err.getErrorCode() > 0 && err.getErrorCode() != 50005) {
                                // Error
                                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            }else if(err.getErrorCode() == 50002){
                                Toast.makeText(getApplicationContext(), "Low Balance", Toast.LENGTH_SHORT).show();
                            } else {
                                sendSmsOnSuccessfulDeposit(txtDepositAmount.getText().toString(), mStr_loanAccCode, phoneNo);
                                clearText();
                                arrayListBreakUp = null;
                                lData = null;
                                mLl_lateFineRoot.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                /*startActivity(new Intent(MemberLoanPaymentActivity.this,MemberLoanPaymentActivity.class));
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                finish();*/
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }

    // test comment

    private void sendSmsOnSuccessfulDeposit(String depositAmnt, String loanAccount, String phoneNo) {

        msgBody = "Dear Customer, we have received EMI Amount of Rs. "+depositAmnt+" against Loan No. "+loanAccount+". Loan Amount "+LoanAmount+" . Purnoday Nidhi Limited.";

       // msgBody = "Dear Customer, We have received EMI Amount of Rs. " + depositAmnt + " against Loan No. " + loanAccount + " , " + R.string.App_Full_Name +".";
        //msgBody = "Dear Customer, Your savings account " + mEt_accountCode.getText().toString() + "has been credited with " + mEt_depositAmount.getText().toString() + ", Nidhi Limited.";
        String smsUrl = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + MyConfig.SMS_USER_NAME + "&password=" + MyConfig.SMS_API_PASSWORD + "&to=" + phoneNo + "&senderid=" + MyConfig.SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text&datetime=2017-03-24%2014%3A03%3A14";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(smsUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MemberLoanPaymentActivity.this, "A confirmation message has been sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MemberLoanPaymentActivity.this, MemberLoanPaymentActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                startActivity(new Intent(MemberLoanPaymentActivity.this, MemberLoanPaymentActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }


    private void getLoanEmiLatefine(String url) {
        arrayList.clear();
        totalLatefine = 0.0;
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        isLateFine = true;
                        mLl_lateFineRoot.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++) {
                            renewalLatefineSetGet = new RenewalLatefineSetGet();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                renewalLatefineSetGet.setInstNo(jsonObject.getInt("EmiNo"));
                                renewalLatefineSetGet.setLatefine(jsonObject.getString("LateFine"));
                                totalLatefine = totalLatefine + Double.parseDouble(jsonObject.getString("LateFine"));
                                jsonObject.getString("LoanCode");
                                arrayList.add(renewalLatefineSetGet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        latefineAdapter.notifyDataSetChanged();
                        mTv_totalLatefine.setText(totalLatefine + "");
                        fillEMIData(txtLoanCode.getText().toString(), Integer.parseInt(txtLoanEMIFromInstNo.getText().toString()),
                                Integer.parseInt(txtLoanEMIToInstNo.getText().toString()), arrayList);
                    } else {
                        isLateFine = false;
                        mLl_lateFineRoot.setVisibility(View.GONE);
                        Toast.makeText(MemberLoanPaymentActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                        fillEMIData(txtLoanCode.getText().toString(), Integer.parseInt(txtLoanEMIFromInstNo.getText().toString()),
                                Integer.parseInt(txtLoanEMIToInstNo.getText().toString()), arrayList);
                    }
                }
            }
        });
    }

    private void fillEMIData(final String loanCode, final Integer f, final Integer t, final ArrayList<RenewalLatefineSetGet> arrayList) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberLoanPaymentActivity.this, "Loading...");
        bData = new LoanEMIBrakupData();
        arrayListBreakUp = new ArrayList<LoanEMIBrakupData>();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            arrayListBreakUp = getLoanEMIBrakupData(loanCode, lData.getReducingEMI(), f, t, arrayList);
                            payDate = Integer.parseInt(arrayListBreakUp.get(0).getPayDate());
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onClick(View v) {
        if (v == btnEMIView) {
            // enter deposit amount and then view
            try {
                int NoOfInstNo;
                NoOfInstNo = (int) Math.floor(Float.parseFloat(txtDepositAmount.getEditableText().toString()) / lData.getEMIAmount());
                // NoOfInstNo +=1;
                txtLoanEMIFromInstNo.setText(String.valueOf(lData.getLastEMINo() + 1));
                txtLoanEMIToInstNo.setText(String.valueOf(lData.getLastEMINo() + NoOfInstNo));
                getLoanEmiLatefine(APILinks.GET_LOAN_EMI_LATE_FINE + "loanCode=" + txtLoanCode.getText().toString() + "&fromInstNo=" + txtLoanEMIFromInstNo.getText().toString() + "&toInstNo=" + txtLoanEMIToInstNo.getText().toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (v == btnLoanEMISave) {
            //Create EMIDetails
            String EMIDetails = "";
            for (LoanEMIBrakupData l : arrayListBreakUp) {
                EMIDetails += l.getPeriod() + "," + l.getPayDate() + "," +
                        l.getPrinciple() + "," + l.getInterest() + "," +
                        l.getCurrentBalance() + "," + l.getLateFineAmt() + ";";
                LateFineSummary += l.getLateFineAmt();
            }
            LoanEMIPaymentData lpd = new LoanEMIPaymentData();
            lpd.setSbCode(mStr_SBAccCode);
            lpd.setLoanCode(txtLoanCode.getText().toString());
            lpd.setEMIPercentage(lData.getEMIPercentage());
            lpd.setReducingEMI(lData.getReducingEMI());
            lpd.setOfficeID(GlobalStore.GlobalValue.getOfficeID());
            lpd.setPaymentDate(lData.getServerDate());
            //lpd.setTotalPaymentAmount(Integer.parseInt(txtDepositAmount.getEditableText().toString()));
            lpd.setActualEMIAmount((int)(Math.round(lData.getEMIAmount())));
            lpd.setPayMode("Cash");
            lpd.setDepACCLCode("1001");
            lpd.setSavingsAC("");
            lpd.setFromCode(lData.getLCode());
            lpd.setToCode("1001");
            lpd.setChequeNo("");
            lpd.setChequeDate(lData.getServerDate());
            lpd.setEMIDetails(EMIDetails);
            lpd.setRemarks("");
            lpd.setBounceCharge(Float.parseFloat("0"));
            lpd.setRecoveryCharge(Float.parseFloat("0"));
            lpd.setDueLateFine(false);
            lpd.setDueLateFine(isLateFinePaid);
            saveLoanEMI(lpd);
        }
    }

    private void layoutManagers() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_latefine.setLayoutManager(linearLayoutManager);
    }

    private void adapters() {
        arrayList = new ArrayList<>();
        latefineAdapter = new LatefineAdapter(this, arrayList);
        mRv_latefine.setAdapter(latefineAdapter);
    }

    private void setViewReferences() {
        //txtSearchLoan = findViewById(R.id.txtSearchLoan);
        txtDepositAmount = findViewById(R.id.tv_activity_loan_pay_txtDepositAmount);

        txtLoanHolderName = findViewById(R.id.tv_activity_loan_pay_txtLoanHolderName);
        txtLoanCode = findViewById(R.id.tv_activity_loan_pay_txtLoanCode);
        txtLoanDate = findViewById(R.id.tv_activity_customer_loan_pay_txtLoanDate);
        txtLoanAmount = findViewById(R.id.tv_activity_loan_pay_txtLoanAmount);
        txtLoanTerm = findViewById(R.id.tv_activity_loan_pay_txtLoanTerm);
        txtLoanEMIAmount = findViewById(R.id.tv_activity_loan_pay_txtLoanEMIAmount);
        txtLoanMode = findViewById(R.id.tv_acitivity_cus_loan_payment_txtLoanMode);
        txtLoanEMIFromInstNo = findViewById(R.id.tv_activity_loan_pay_txtLoanEMIFromInstNo);
        txtLoanEMIToInstNo = findViewById(R.id.tv_activity_loan_pay_txtLoanEMIToInstNo);
        //devBygen = findViewById(R.id.tv_activity_loan_collection_developed_by);

        //btnSearchLoan = findViewById(R.id.btnSearchLoan);
        btnEMIView = findViewById(R.id.btn_activity_loan_pay_btnEMIView);
        btnLoanEMISave = findViewById(R.id.btn_activity_loan_pay_btnLoanEMISave);

        mEt_enterNoOfEMI = findViewById(R.id.et_activity_loan_pay_enter_no_of_emi);

        mTv_totalLatefine = findViewById(R.id.tv_activity_renewal_pay_total_late_fine);

        mRv_latefine = findViewById(R.id.rv_activity_loan_pay_loan_emi_late_fine);

        mLl_lateFineRoot = findViewById(R.id.ll_activity_renewal_pay_late_fine_root);

        mCb_lateFinePay = findViewById(R.id.cb_activity_arranger_loan_pay);

        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mSp_savingsAcc = findViewById(R.id.sp_activity_customer_loan_payment_savings_acc);
        mSp_loanAcc = findViewById(R.id.sp_activity_customer_loan_payment_loan_acc);

        mTv_savingsBalance = findViewById(R.id.tv_activity_customer_loan_pay_view_savings_balance);
        mTv_actualEmiAmt=findViewById(R.id.tv_activity_member_loan_pay_payble_amount);
    }

    private void setUpToolbar() {
        // Toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mTv_toolbarTitle.setText("Loan Payment");
    }

    private void bindEventHandlers() {
        //btnSearchLoan.setOnClickListener(this);
        btnEMIView.setOnClickListener(this);
        btnLoanEMISave.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(MemberLoanPaymentActivity.this, MemberDashboardActivity.class));
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
        startActivity(new Intent(MemberLoanPaymentActivity.this, MemberDashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}

