package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.MainActivity;
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
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ArrLoanCollectionManualActivity extends AppCompatActivity implements View.OnClickListener {


    EditText txtSearchLoan, txtDepositAmount;
    TextView txtLoanHolderName, txtLoanCode, txtLoanDate, txtLoanAmount, txtLoanTerm,
            txtLoanEMIAmount, txtLoanMode, txtLoanEMIFromInstNo, txtLoanEMIToInstNo, devBygen,mTv_phone;
    Button btnSearchLoan, btnEMIView, btnLoanEMISave;
    LoanData lData;

    int LateFineSummary;
    private boolean isLateFinePaid = false;
    private CheckBox mCb_lateFinePay;

    ArrayList<LoanEMIBrakupData> arrayListBreakUp;
    LoanEMIBrakupData bData;
    String value = "";
    String cDate = "";


    private RadioGroup mRadioGr;
    private RadioButton mRadio_name, mRadio_acc;
    private Spinner mSp_loanCode;
    private Button mBtn_submit;

    private ArrayList<String> arrayList_loanCode = new ArrayList<>();
    private ArrayList<String> arrayList_loanName = new ArrayList<>();
    private ArrayList<String> arrayList_loanCodeName = new ArrayList<>();
    private String mStr_selectedLoanCode = "";
    private String mStr_type = "";


    private static int serverDate;      // Server current date
    private static int payDate;         // EMI pay date

    private static int dateDifference;
    private static float lateFineParc;
    private static String loanMode;
    private static float emiAmount;
    private static float finalLateFine;

    private TextView mTv_totalLatefine;
    private RecyclerView mRv_latefine;


    private void clearText() {
        txtSearchLoan.setText("");
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

    private static final int REQUEST_CHECK_SETTINGS = 101;
    private static final int REQUEST_ID_ACCESS_LOCATION_PERMISSIONS = 103;
    private String mStr_loanCode;

    private EditText mEt_noOfEmi;

    private CheckBox mCb_approval;
    private boolean mBool_approval=false;
    private TextView mTv_walletBal;
    private int mInt_walletBal=0;
    private Button mBtn_saveLoanEMI;

    private EditText mEt_policyOrName;
    private Button mBtn_policyManualSearch;
    private Spinner mSp_policyNameCodeList;
    private ArrayList<String> arrayList_policyCode =new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName =new ArrayList<>();

    private ImageView mIv_holderImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_loan_collection_manual);

        // initialize widget
        setViewReferences();
        bindEventHandlers();
        adapters();
        layoutManagers();

     /*   if (checkAndRequestPermissions()) {
            turnOnLocIfNotOn();
        }*/

        txtDepositAmount.setEnabled(true);


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


        mRadioGr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mRadio_name.isChecked()) {
                    mStr_type = "name";
                }
                if (mRadio_acc.isChecked()) {
                    mStr_type = "acc";
                }
            }
        });

        mSp_loanCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mStr_selectedLoanCode = arrayList_loanCode.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCb_approval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(mInt_walletBal>Integer.parseInt(txtDepositAmount.getText().toString())){
                        mBool_approval=true;
                    }else{
                        mBool_approval=false;
                        Toast.makeText(ArrLoanCollectionManualActivity.this, "Not enough wallet balance", Toast.LENGTH_LONG).show();
                        mCb_approval.setChecked(false);
                    }

                }else{
                    mBool_approval=false;
                }
            }
        });

        //getWalletbalance(APILinks.GET_EMPLOYEE_WALLET_BAL+ GlobalStore.GlobalValue.getUserName());

        mBtn_policyManualSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEt_policyOrName.getText().toString().length()>0){
                    arrayList_policyCode.clear();
                    arrayList_policyCodeName.clear();
                    arrayList_policyCode.add("");
                    arrayList_policyCodeName.add("");
                    String tag;
                    if (mEt_policyOrName.getText().toString().matches("[a-zA-Z]+")){
                        tag="NAME";
                    } else {
                        tag="loanCode";
                    }
                    getPolicyCodeName(APILinks.GEt_LOAN_BY_PCODE_NAME+mEt_policyOrName.getText().toString()+"&tag="+tag+"&arrCode="+ GlobalStore.GlobalValue.getUserName());

                }else{
                    Toast.makeText(ArrLoanCollectionManualActivity.this, "enter search value", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSp_policyNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    txtSearchLoan.setText(arrayList_policyCode.get(i));
                    fetchValue(arrayList_policyCode.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void getPolicyCodeName(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject=response.getJSONObject(i);
                            arrayList_policyCode.add(jsonObject.getString("LCode"));
                            arrayList_policyCodeName.add(jsonObject.getString("LCode")+"-"+jsonObject.getString("Name"));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(ArrLoanCollectionManualActivity.this,R.layout.spinner_hint, arrayList_policyCodeName);
                        mSp_policyNameCodeList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(ArrLoanCollectionManualActivity.this, "Loan code not associated with this Executive or No data found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    // fetch loan by loan codetxtSearchLoan
    private void fetchValue(final String loanCode) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrLoanCollectionManualActivity.this, "Loading...");
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
                                mStr_loanCode=lData.getLoanCode();
                                serverDate = lData.getServerDate();
                                //String loanDate = lData.getLoanDate();

                                txtLoanAmount.setText(String.valueOf(lData.getLoanApproveAmount()));
                                txtLoanTerm.setText(String.valueOf(lData.getLoanTerm()));
                                txtLoanEMIAmount.setText(String.valueOf(lData.getEMIAmount()));
                                emiAmount = lData.getEMIAmount();
                                txtLoanMode.setText(lData.getEMIMode());
                                loanMode = lData.getEMIMode();
                                mTv_phone.setText(lData.getPhoneNo());

                                if(lData.getHolderPhoto() != null && !lData.getHolderPhoto().equals("")){
                                    byte[] decodedString = Base64.decode(lData.getHolderPhoto().toString(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    mIv_holderImage.setImageBitmap(decodedByte);
                                }

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


    private void getWalletbalance(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        JSONObject jsonObject=response.getJSONObject(0);
                        mTv_walletBal.setText("Wallet balance : - "+String.valueOf(jsonObject.getInt("WalletBalance")));
                        mInt_walletBal=jsonObject.getInt("WalletBalance");
                    }else{
                        mTv_walletBal.setText("No Wallet balance found");
                        mInt_walletBal=0;
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
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
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrLoanCollectionManualActivity.this, "Wait...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        LoanManagement lm = new LoanManagement();
                        try {
                            ErrorData err = lm.insertLoanEMI(lpd);
                            if (err.getErrorCode() > 0 && err.getErrorCode() != 50005) {
                                // Error
                                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            } else if (err.getErrorCode() == 50005) {
                                Toast.makeText(ArrLoanCollectionManualActivity.this, "Please approve any previous pending approval", Toast.LENGTH_SHORT).show();
                            } else {
                                clearText();
                                arrayListBreakUp = null;
                                lData = null;
                                mLl_lateFineRoot.setVisibility(View.GONE);
                                //getCurrentLocation();
                                //sendLocToServer(APILinks.SEND_COLL_LOC_TO_SERVER);
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ArrLoanCollectionManualActivity.this,ArrLoanCollectionManualActivity.class));
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                finish();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }


    /*private void sendLocToServer(String url){
        getCurrentLocation();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("loc",TempLocation.currentAddress);
        hashMap.put("isRenewal","False");
        hashMap.put("isLoan","True");
        hashMap.put("loanCode",mStr_loanCode);
        hashMap.put("policyCode","");
        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if(response.getString("returnStatus").equals("Success")){
                        startActivity(new Intent(ArrangerLoanCollBySearchActivity.this,ArrangerLoanCollBySearchActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();
                    }else{
                        Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Location save failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }*/


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
                                jsonObject.getString("PolicyCode");
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
                        Toast.makeText(ArrLoanCollectionManualActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                        fillEMIData(txtLoanCode.getText().toString(), Integer.parseInt(txtLoanEMIFromInstNo.getText().toString()),
                                Integer.parseInt(txtLoanEMIToInstNo.getText().toString()), arrayList);
                    }
                }
            }
        });
    }

    private void fillEMIData(final String loanCode, final Integer f, final Integer t, final ArrayList<RenewalLatefineSetGet> arrayList) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrLoanCollectionManualActivity.this, "Loading...");
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
        if (v == btnSearchLoan) {

            //if (mStr_type.length() > 0) {
            if (txtSearchLoan.getText().toString().length() > 0) {
                fetchValue(txtSearchLoan.getText().toString());
                //getLoanCodeDetails(APILinks.ARRANGER_GET_LOAN_CODE + mStr_type + "&value=" + txtSearchLoan.getText().toString());
            } else {
                Toast.makeText(this, "Enter Name or Loan Code", Toast.LENGTH_SHORT).show();
            }
            //}// else {
            //Toast.makeText(this, "Select Type", Toast.LENGTH_SHORT).show();
            // }

        } else if (v == btnEMIView) {
            // enter deposit amount and then view
            try {
                //int noOfInst=Integer.parseInt(mEt_noOfEmi.getText().toString());
                //int amt=(int)(Math.round(emiAmount))*noOfInst;
                //txtDepositAmount.setText(String.valueOf(amt));
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
            lpd.setLoanCode(txtLoanCode.getText().toString());
            lpd.setEMIPercentage(lData.getEMIPercentage());
            lpd.setReducingEMI(lData.getReducingEMI());
            lpd.setOfficeID(GlobalStore.GlobalValue.getOfficeID());
            lpd.setPaymentDate(lData.getServerDate());
            lpd.setTotalPaymentAmount(Integer.parseInt(txtDepositAmount.getEditableText().toString()));
            lpd.setActualEMIAmount(Math.round(lData.getEMIAmount()));
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

        if (v == mBtn_submit) {
            if (mStr_selectedLoanCode.length() > 0) {
                if (txtSearchLoan.getText().toString().length() > 0) {
                    String s=APILinks.CHECK_IF_ANY_EMI_PENDING + mStr_selectedLoanCode;
                    fetchValue(txtSearchLoan.getText().toString());
                    //checkIfAnyEmiPending(APILinks.CHECK_IF_ANY_EMI_PENDING + mStr_selectedLoanCode);
                } else {
                    Toast.makeText(ArrLoanCollectionManualActivity.this, "Loan Code Required", Toast.LENGTH_LONG).show();
                }
            }
        }

        if(v==mBtn_saveLoanEMI){
            if(mStr_loanCode.length()>0 && txtDepositAmount.getText().toString().length()>0){
                insertManualLoanEMIColl(APILinks.INSERT_MANUAL_LOANEMI_COLL);
            }else{
                Toast.makeText(this, "Fill Data", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void insertManualLoanEMIColl(String url){
        int depositAmt=Integer.parseInt(txtDepositAmount.getText().toString());
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("loanCode",mStr_loanCode);
        hashMap.put("amount",String.valueOf(depositAmt));
        hashMap.put("userName",GlobalStore.GlobalValue.getUserName());
        /*if(mBool_approval){
            hashMap.put("walletApprove","1");
        }else{
            hashMap.put("walletApprove","0");
        }*/

        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if(response.getString("status").equals("1")){
                        Toast.makeText(ArrLoanCollectionManualActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        if(mBool_approval){
                            //deductFromWallet(APILinks.DEDUCT_FROM_WALLET,depositAmt);
                        }else{
                            startActivity(new Intent(ArrLoanCollectionManualActivity.this,ArrLoanCollectionManualActivity.class));
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            finish();
                        }
                    }else{
                        Toast.makeText(ArrLoanCollectionManualActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();}
            }
        });

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
        txtSearchLoan = findViewById(R.id.txtSearchLoan);
        txtDepositAmount = findViewById(R.id.txtDepositAmount);

        txtLoanHolderName = findViewById(R.id.txtLoanHolderName);
        txtLoanCode = findViewById(R.id.txtLoanCode);
        txtLoanDate = findViewById(R.id.txtLoanDate);
        txtLoanAmount = findViewById(R.id.txtLoanAmount);
        txtLoanTerm = findViewById(R.id.txtLoanTerm);
        txtLoanEMIAmount = findViewById(R.id.txtLoanEMIAmount);
        txtLoanMode = findViewById(R.id.txtLoanMode);
        txtLoanEMIFromInstNo = findViewById(R.id.txtLoanEMIFromInstNo);
        txtLoanEMIToInstNo = findViewById(R.id.txtLoanEMIToInstNo);
        //devBygen = findViewById(R.id.tv_activity_loan_collection_developed_by);

        btnSearchLoan = findViewById(R.id.btnSearchLoan);
        btnEMIView = findViewById(R.id.btnEMIView);
        btnLoanEMISave = findViewById(R.id.btnLoanEMISave);

        mTv_totalLatefine = findViewById(R.id.tv_activity_renewal_collection_total_late_fine);

        mRv_latefine = findViewById(R.id.rv_activity_loan_collection_loan_emi_late_fine);

        mLl_lateFineRoot = findViewById(R.id.ll_activity_renewal_collection_late_fine_root);

        mCb_lateFinePay = findViewById(R.id.cb_activity_arranger_loan_collection);

        mRadioGr = findViewById(R.id.radio_group_activity_loan_collection);
        mRadio_name = findViewById(R.id.radio_activity_loan_collection_name);
        mRadio_acc = findViewById(R.id.radio_activity_loan_collection_acc_no);
        mSp_loanCode = findViewById(R.id.sp_activity_loan_collection_acc_code);
        mBtn_submit = findViewById(R.id.btn_activity_loan_collection_submit);
        mEt_noOfEmi= findViewById(R.id.et_activity_arr_loan_coll_by_search_no_of_emi);

        mTv_walletBal = findViewById(R.id.tv_activity_loan_collection_wallet_bal);
        mCb_approval = findViewById(R.id.cb_activity_loan_collection_check_for_approval);
        mBtn_saveLoanEMI = findViewById(R.id.btn_Loan_EMI_Save);

        mTv_phone = findViewById(R.id.tvPhoneNumber);

        mSp_policyNameCodeList = findViewById(R.id.sp_activity_renewal_collection_policy_list);
        mEt_policyOrName=findViewById(R.id.et_activity_renewal_collection_polcode_name);
        mBtn_policyManualSearch=findViewById(R.id.btn_activity_renewal_collection_manual_search);
        mIv_holderImage = findViewById(R.id.ivLoanHolderImage);
    }

    private void bindEventHandlers() {
        btnSearchLoan.setOnClickListener(this);
        btnEMIView.setOnClickListener(this);
        btnLoanEMISave.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mBtn_saveLoanEMI.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ArrLoanCollectionManualActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

   /* public void scanQR(View view) {
        IntentIntegrator integrator = new IntentIntegrator(ArrangerLoanCollBySearchActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }*/

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
            } else {
                Log.e("Scan", "Scanned");

                txtSearchLoan.setText(result.getContents());
                mStr_selectedLoanCode = txtSearchLoan.getText().toString();
                if (!TextUtils.isEmpty(txtSearchLoan.getText().toString().trim())) {
                    //getLoanCodeDetails(APILinks.ARRANGER_GET_LOAN_CODE + mStr_type + "&value=" + txtSearchLoan.getText().toString());
                    getLoanCodeDetails(APILinks.ARRANGER_GET_LOAN_CODE + "acc" + "&value=" + txtSearchLoan.getText().toString());
                } else {
                    Toast.makeText(this, "Enter Name or Loan Code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    ///////////////////////////////// location/////////////////////////////

    /*private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(ArrangerLoanCollBySearchActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        *//*int permissionStorage = ContextCompat.checkSelfPermission(AppDashboardActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);*//*
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        *//*if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }*//*
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ArrangerLoanCollBySearchActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_ACCESS_LOCATION_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void turnOnLocIfNotOn() {
        *//** Turn on location service if not on *//*
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        *//*locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);*//*

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        //locationStatus = true;   //TODO
                        //mFsv_search.setFocusable(true);   // TODO
                        getCurrentLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        //locationStatus = false;     // TODO
                        //mFsv_search.setFocusable(false);  // TODO
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ArrangerLoanCollBySearchActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void getCurrentLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        getAddress(ArrangerLoanCollBySearchActivity.this, latitude, longitude);
    }

    public static void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {

                TempLocation.currentAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                TempLocation.latitude=LATITUDE;
                TempLocation.longitude=LONGITUDE;
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                *//*Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);*//*
                //Toast.makeText(context, state + "", Toast.LENGTH_SHORT).show();
                *//*Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);*//*

                // Get city or locality name

                //Toast.makeText(context, TempLocation.currentAddress, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }*/

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_ACCESS_LOCATION_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        *//*&& perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED*//*) {
                        // TODO Get current location
                        getCurrentLocation();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ArrangerLoanCollBySearchActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) *//*|| ActivityCompat.shouldShowRequestPermissionRationale(AppDashboardActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)*//*) {
                            showDialogOK("This permission is required to get your current location",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }*/

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ArrLoanCollectionManualActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

}

