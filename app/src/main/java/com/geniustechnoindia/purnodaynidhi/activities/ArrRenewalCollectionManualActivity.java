package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.geniustechnoindia.purnodaynidhi.bean.RenewalData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.dl.PolicyManagement;
import com.geniustechnoindia.purnodaynidhi.model.RenewalLatefineSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
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

public class ArrRenewalCollectionManualActivity extends AppCompatActivity {
    EditText txtSearchPolicy, txtRenewalInstNo;
    TextView txtPolicyHolderView, txtPolicyNo, txtDateOfCom,
            txtPlanCode, txtTerm, txtAmount, txtMode, txtNetAmount,
            txtFromInstNo, txtToInstNo, mTv_lateFine;
    Button btnSearchPolicy, btnView, btnSave;
    TextView mTv_devByGen;
    TextView mTv_totalLatefine;
    EditText mEt_lateFineAmountPaid;

    private CheckBox mCb_lateFinePay;

    private RecyclerView mRv_latefine;
    private TextView mTv_lateFineApplicable;
    private TextView mTv_lateFinePercentage;

    private LatefineAdapter latefineAdapter;
    private ArrayList<RenewalLatefineSetGet> arrayList;
    private RenewalLatefineSetGet renewalLatefineSetGet;

    RenewalData rData = new RenewalData();
    boolean isSuccess = false;

    String value = "";
    String cDate = "";

    double totalLatefine = 0.0;

    private String lateFinePaidCommaSep = "";
    private String lateFineNotPaid = "";
    private boolean isLateFinePaid = true;

    private LinearLayout mLl_lateFineRoot;

    private static float actualLateFine = 0f;

    private boolean isLateFine = false;

    private int insertErrorCode = 1;
    private static final int REQUEST_CHECK_SETTINGS = 101;
    private static final int REQUEST_ID_ACCESS_LOCATION_PERMISSIONS = 103;
    private String mStr_policyCode="";

    private CheckBox mCb_approval;
    private boolean mBool_approval=false;
    private TextView mTv_walletBal;
    private int mInt_walletBal=0;

    private TextView tvPhoneNumber,tv_totalAmount,tv_LastDipositDate;

    private EditText mEt_policyOrName;
    private Button mBtn_policyManualSearch;
    private Spinner mSp_policyNameCodeList;
    private ArrayList<String> arrayList_policyCode =new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName =new ArrayList<>();

    private ImageView mIv_holderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_renewal_collection_manual);

        setViewReferences();
        adapters();
        layoutManagers();

        this.clearText();

       /* if (checkAndRequestPermissions()) {
            turnOnLocIfNotOn();
        }*/

        mCb_approval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    if(mInt_walletBal>Integer.parseInt(txtNetAmount.getText().toString())){
                        mBool_approval=true;
                    }else{
                        mBool_approval=false;
                        Toast.makeText(ArrRenewalCollectionManualActivity.this, "Not enough wallet balance", Toast.LENGTH_LONG).show();
                        mCb_approval.setChecked(false);
                    }

                }else{
                    mBool_approval=false;
                }
            }
        });

        mCb_lateFinePay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {                                                                    // Arranger is paying late fine
                //isLateFinePaid = true;
                /*for (int i = 0; i < arrayList.size(); i++) {
                    lateFinePaidCommaSep = lateFinePaidCommaSep + arrayList.get(i).getLatefine() + ",";
                }*/
            } else {                                                                            // // Arranger not paying late fine
                //isLateFinePaid = false;
                /*for (int i = 0; i < arrayList.size(); i++) {
                    lateFineNotPaid = lateFineNotPaid + arrayList.get(i).getLatefine() + ",";
                }*/
            }
        });

        btnSearchPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtSearchPolicy.getEditableText().toString().trim().isEmpty()) {
                    Toast.makeText(ArrRenewalCollectionManualActivity.this, "Policy Code Required.", Toast.LENGTH_LONG).show();
                } else {
                    fetchValue(txtSearchPolicy.getEditableText().toString());
                    //checkIfAnyRenewalPending(APILinks.CHECK_IF_ANY_RENEWAL_PENDING + txtSearchPolicy.getEditableText().toString() );
                    //clearText();
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualLateFine = 0f;
                getInstChange();
            }
        });

        txtRenewalInstNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getInstChange();
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNetAmount.getText().toString().length()>0 && mStr_policyCode.length()>0){
                    insertManualPolicyColl(APILinks.INSERT_MANUAL_POLICY_COLL);
                }else{
                    Toast.makeText(ArrRenewalCollectionManualActivity.this, "Please Fill Data", Toast.LENGTH_SHORT).show();
                }


                /*String msg = "";
                if (!isValidate(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else {
                    // TODO Get user entered late fine amount and due late fine
                    insertRenew(txtPolicyNo.getText().toString(), String.valueOf(txtFromInstNo.getText()),
                            String.valueOf(txtToInstNo.getText()), actualLateFine, isLateFine, isLateFinePaid);
                }*/
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
                    }else{
                        tag="PolCODE";
                    }
                    getPolicyCodeName(APILinks.GEt_POLICY_BY_PCODE_NAME+mEt_policyOrName.getText().toString()+"&tag="+tag+"&arrCode="+ GlobalStore.GlobalValue.getUserName());

                }else{
                    Toast.makeText(ArrRenewalCollectionManualActivity.this, "enter search value", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSp_policyNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
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
                            arrayList_policyCode.add(jsonObject.getString("PolicyCode"));
                            arrayList_policyCodeName.add(jsonObject.getString("PolicyCode")+"-"+jsonObject.getString("ApplicantName"));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(ArrRenewalCollectionManualActivity.this,R.layout.spinner_hint, arrayList_policyCodeName);
                        mSp_policyNameCodeList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(ArrRenewalCollectionManualActivity.this, "NoData", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }



    private void insertManualPolicyColl(String url){
        int depositAmt=Integer.parseInt(txtNetAmount.getText().toString());
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("policyCode",mStr_policyCode);
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
                    if(response.getString("returnStatus").equals("Success")){
                        Toast.makeText(ArrRenewalCollectionManualActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        if(mBool_approval){
                            //deductFromWallet(APILinks.DEDUCT_FROM_WALLET,depositAmt);
                        }else{
                            startActivity(new Intent(ArrRenewalCollectionManualActivity.this,ArrRenewalCollectionManualActivity.class));
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            finish();
                        }
                    }else{
                        Toast.makeText(ArrRenewalCollectionManualActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
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
        tv_totalAmount=findViewById(R.id.tv_totalAmount);
        tv_LastDipositDate=findViewById(R.id.tv_LastDipositDate);
        txtSearchPolicy = findViewById(R.id.txtSearchPolicy);
        txtRenewalInstNo = findViewById(R.id.txtRenInstNo);

        txtPolicyHolderView = findViewById(R.id.txtRenPolicyHolderName);
        txtPolicyNo = findViewById(R.id.txtRenPolicyNumber);
        txtDateOfCom = findViewById(R.id.txtRenDOC);
        txtPlanCode = findViewById(R.id.txtRenPlanCode);
        txtTerm = findViewById(R.id.txtRenTerm);
        txtAmount = findViewById(R.id.txtRenAmount);
        txtMode = findViewById(R.id.txtRenMode);
        txtNetAmount = findViewById(R.id.txtRenTotalAmount);
        txtFromInstNo = findViewById(R.id.txtRenFormInstNo);
        txtToInstNo = findViewById(R.id.txtRenToInstNo);
        btnSearchPolicy = findViewById(R.id.btnSearchPolicy);
        btnView = findViewById(R.id.btnRenView);
        btnSave = findViewById(R.id.btnRenewalSave);
        mLl_lateFineRoot = findViewById(R.id.ll_activity_renewal_collection_late_fine_root);
        //mEt_lateFineAmountPaid = findViewById(R.id.et_renewal_collection_late_fine_amount_paid);
        mTv_totalLatefine = findViewById(R.id.tv_activity_renewal_collection_total_late_fine);
        mCb_lateFinePay = findViewById(R.id.cb_activity_arranger_renewal_collection);

        mRv_latefine = findViewById(R.id.rv_activity_renewal_collection_loan_emi_late_fine);

        mTv_lateFineApplicable = findViewById(R.id.tv_activity_renewal_collection_late_fine_applicable);
        mTv_lateFinePercentage = findViewById(R.id.tv_activity_renewal_collection_late_fine_percentage);
        mTv_lateFine = findViewById(R.id.tv_activity_renewal_collection_late_fine);
        mTv_walletBal = findViewById(R.id.tv_activity_renewal_collection_wallet_bal);
        mCb_approval = findViewById(R.id.cb_activity_renewal_collection_check_for_approval);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);

        mBtn_policyManualSearch=findViewById(R.id.btn_activity_renewal_collection_manual_search);
        mEt_policyOrName=findViewById(R.id.et_activity_renewal_collection_polcode_name);
        mSp_policyNameCodeList = findViewById(R.id.sp_activity_renewal_collection_policy_list);
        mEt_policyOrName=findViewById(R.id.et_activity_renewal_collection_polcode_name);

        mIv_holderImage = findViewById(R.id.holderImage);
    }

    private boolean isValidate(String msg) {
        boolean rValue = false;
        if (String.valueOf(txtNetAmount.getText()).toString().isEmpty()) {
            msg = "Installment No Required";
            rValue = false;
        } else if (txtPolicyNo.getText().toString().isEmpty()) {
            msg = "Policy Code Required.";
            rValue = false;
        } else {
            rValue = true;
        }
        return rValue;
    }

    private void getInstChange() {
        try {
            //txtNetAmount.setText(String.valueOf(rData.getAmount() * (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
            txtFromInstNo.setText(String.valueOf(rData.getLastInstNo() + 1));
            //txtToInstNo.setText(String.valueOf(rData.getLastInstNo() + (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
            int totalDeposit=Integer.parseInt(txtNetAmount.getText().toString());
            int toInst = totalDeposit/((int)(rData.getAmount()));
            txtToInstNo.setText(String.valueOf(toInst+rData.getLastInstNo()));
            // TODO Call method to calculate late fine
            //calculateLateFine(txtSearchPolicy.getText().toString(), String.valueOf(rData.getLastInstNo() + 1), String.valueOf(rData.getLastInstNo() + (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
            getRenewalLatefine(APILinks.GET_RENEWAL_LATE_FINE + "policyCode=" + txtPolicyNo.getText().toString() + "&fromInstNo=" + txtFromInstNo.getText().toString() + "&toInstNo=" + txtToInstNo.getText().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchValue(final String PolicyCode) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrRenewalCollectionManualActivity.this, "Loading...");
        rData = new RenewalData();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PolicyManagement md = new PolicyManagement();
                        try {
                            rData = md.getPolicyDataForRenwal(PolicyCode);
                            /*if(!rData.getPlanCode().equals("DRD")){
                                Toast.makeText(ArrRenewalCollectionManualActivity.this, "Can not deposit this policy", Toast.LENGTH_LONG).show();
                                btnSave.setEnabled(false);
                            }*/
                            txtPolicyHolderView.setText(rData.getApplicantName());
                            txtPolicyNo.setText(rData.getPolicyCode());
                            mStr_policyCode=rData.getPolicyCode();
                            txtDateOfCom.setText(rData.getDateOfCom());
                            txtPlanCode.setText(rData.getPlanCode());
                            txtTerm.setText(String.valueOf(rData.getTerm()));
                            txtAmount.setText(String.valueOf(rData.getAmount()));
                            tv_totalAmount.setText(rData.getAmountUptoPrevMonth());
                            tv_LastDipositDate.setText(rData.getLAST_DEP_DATE());
                            if (rData.getIsLateFineApplicable().equals("1")) {
                                mTv_lateFineApplicable.setText("Yes");
                                mTv_lateFinePercentage.setText(rData.getLatePercentage());
                                //mLl_lateFineRoot.setVisibility(View.VISIBLE);
                            } else {
                                //mLl_lateFineRoot.setVisibility(View.GONE);
                                mTv_lateFinePercentage.setText(0 + "");
                                mTv_lateFineApplicable.setText("No");
                            }
                            txtMode.setText(rData.getMode());
                            cDate = rData.getMaturityDate();
                            tvPhoneNumber.setText(TempData.phoneNumber);
                            if(rData.getHolderPhoto() != null && !rData.getHolderPhoto().equals("")){
                                byte[] decodedString = Base64.decode(rData.getHolderPhoto().toString(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                mIv_holderImage.setImageBitmap(decodedByte);
                            }
                        } catch (Exception ex) {
                            Log.d("sunita", "run: "+ex);
                            Toast.makeText(getApplicationContext(), "Policy code not associated with this arranger", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }

    /*private void insertRenew(final String PolicyCode, final String InstNoFrom, final String InstNoTo, final float lateFine, boolean IsLateFine, final boolean isPaid) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrRenewalCollectionManualActivity.this, "Loading...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PolicyManagement md = new PolicyManagement();
                        try {
                            insertErrorCode = md.insertRenewal(PolicyCode, InstNoFrom, InstNoTo, lateFine, isLateFine, isPaid,mBool_approval);
                            if (insertErrorCode == 0) {
                                isSuccess = false;
                                clearText();
                                actualLateFine = 0f;
                                Toast.makeText(getApplicationContext(), "Successfully Saved.", Toast.LENGTH_LONG).show();
                                //getCurrentLocation();
                                //sendLocToServer(APILinks.SEND_COLL_LOC_TO_SERVER);
                                *//*startActivity(new Intent(RenewalCollectionActivity.this, RenewalCollectionActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();*//*
                            } else if (insertErrorCode == 50005) {
                                Toast.makeText(ArrRenewalCollectionManualActivity.this, "Please approve the previous pending approval", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ArrRenewalCollectionManualActivity.this, RenewalCollectionActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }, 3000);
    }*/

    private boolean calculateLateFine(String policyCode, String fromInst, String toInst) {
        Connection cn = new SqlManager().getSQLConnection();
        RenewalData rData = new RenewalData();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetCalculatedRenewalLateFine(?,?,?)}");
                smt.setString("@PolicyCode", policyCode);
                smt.setString("@FromInstNo", fromInst);
                smt.setString("@ToInstNo", toInst);
                smt.executeQuery();
                ResultSet rs = smt.getResultSet();
                while (rs.next()) {
                    //rs.getInt("InstNo");
                    if (rs.getString("LateFine") != null) {
                        mTv_lateFine.setText(rs.getString("LateFine"));
                        actualLateFine = Float.parseFloat(rs.getString("LateFine"));
                    } else {
                        mTv_lateFine.setText("0");
                    }

                }
                return true;
            } else {
                rData.setErrorCode(2);
                rData.setErrorString("Network related problem.");
                return false;
            }
        } catch (Exception ex) {
            rData.setErrorCode(1);
            rData.setErrorString(ex.getMessage());
            return false;
        }
    }

    private void getRenewalLatefine(String url) {
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
                                renewalLatefineSetGet.setInstNo(jsonObject.getInt("InstNo"));
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
                    } else {
                        isLateFine = false;
                        mLl_lateFineRoot.setVisibility(View.GONE);
                        Toast.makeText(ArrRenewalCollectionManualActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void clearText() {
        //txtPolicyHolderView.setText("");
        txtPolicyNo.setText("");
        txtDateOfCom.setText("");
        txtPlanCode.setText("");
        txtTerm.setText("");
        txtAmount.setText("");
        txtMode.setText("");
        txtNetAmount.setText("");
        txtFromInstNo.setText("");
        txtToInstNo.setText("");
        actualLateFine = 0.f;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ArrRenewalCollectionManualActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }




    /*private void sendLocToServer(String url){
        getCurrentLocation();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("loc",TempLocation.currentAddress);
        hashMap.put("isRenewal","True");
        hashMap.put("isLoan","False");
        hashMap.put("loanCode","");
        hashMap.put("policyCode",mStr_policyCode);
        new PostDataParserObjectResponse(this, url, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                try{
                    if(response.getString("returnStatus").equals("Success")){
                        //dialogSelection();
                        if(mBool_approval){
                            walletApproval();
                        }else{
                            startActivity(new Intent(RenewalCollectionActivity.this, RenewalCollectionActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }else{
                        Toast.makeText(RenewalCollectionActivity.this, "Location save failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }*/

    private void walletApproval(){

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


    /*private void dialogSelection(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.renewal_custom_selection, null);
        //builder.setView(customLayout);
        builder.setTitle("Policy Saved successfully")
                .setMessage("Please Select -- ")
                .setCancelable(false)
                .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(ArrRenewalCollectionManualActivity.this, RenewalCollectionActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/

    ///////////////////////////////// location/////////////////////////////

    /*private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(ArrRenewalCollectionManualActivity.this,
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
            ActivityCompat.requestPermissions(ArrRenewalCollectionManualActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_ACCESS_LOCATION_PERMISSIONS);
            return false;
        }
        return true;
    }*/

    /*    private void turnOnLocIfNotOn() {
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
                            status.startResolutionForResult(RenewalCollectionActivity.this, REQUEST_CHECK_SETTINGS);
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
    }*/

/*    private void getCurrentLocation() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        getAddress(RenewalCollectionActivity.this, latitude, longitude);
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
    }
    @Override
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(RenewalCollectionActivity.this,
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
                            Toast.makeText(RenewalCollectionActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }*/

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ArrRenewalCollectionManualActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
