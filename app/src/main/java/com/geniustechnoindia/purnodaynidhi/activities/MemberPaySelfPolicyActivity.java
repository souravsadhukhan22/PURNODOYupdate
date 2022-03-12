package com.geniustechnoindia.purnodaynidhi.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/*import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;*/

public class MemberPaySelfPolicyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

/*
    EditText txtSearchPolicy, txtRenewalInstNo;
    TextView txtPolicyHolderView, txtPolicyNo, txtDateOfCom,
            txtPlanCode, txtTerm, txtAmount, txtMode, txtNetAmount,
            txtFromInstNo, txtToInstNo, mTv_lateFine;
    Button btnSearchPolicy, btnView, btnSave;
    TextView mTv_devByGen;
    TextView mTv_totalLatefine;
    EditText mEt_lateFineAmountPaid;
    TextView mTv_transFee;

    private Spinner mSp_policyCodes;

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

    // PolicyCodes
    private ArrayList<String> mArrayListPolicyCodes;
    private String selectedPolicyCodes = "";

    private PayUmoneySdkInitializer.PaymentParam paymentParam;

    private double price = 0.0;

    private static String firstNameSubString = "";

    private static double netAmount = 0.0;

    private static double amountForPayUMoneyTwoPerc = 0.0;

    private static double amountForPayUMoneyGST = 0.0;

    private static double amountForPayUMoneyFinal = 0.0;

    private static double gstPlusTwoPerc = 0.0;

    private static String payUTransID = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_pay_self_policy);


        setViewReferences();
        adapters();
        layoutManagers();
        bindEventHandlers();

        this.clearText();

        mCb_lateFinePay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {                                                                    // Arranger is paying late fine
                    //isLateFinePaid = true;
                    *//*for (int i = 0; i < arrayList.size(); i++) {
                        lateFinePaidCommaSep = lateFinePaidCommaSep + arrayList.get(i).getLatefine() + ",";
                    }*//*
                } else {                                                                            // // Arranger not paying late fine
                    //isLateFinePaid = false;
                    *//*for (int i = 0; i < arrayList.size(); i++) {
                        lateFineNotPaid = lateFineNotPaid + arrayList.get(i).getLatefine() + ",";
                    }*//*
                }
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

        fetchValue(MemberDataForRenewalPay.policyCode);
        //getpolicyValue();


        //TODO ####### My Policy codes * * * * * * *
        //getPolicyCodes(APILinks.GET_MEMBER_POLICY_CODES + GlobalStore.GlobalValue.getMemberCode());
        mSp_policyCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPolicyCodes = mArrayListPolicyCodes.get(position);
                if(position > 0){
                    if (!selectedPolicyCodes.equals("")) {
                        //fetchValue(selectedPolicyCodes);
                    } else {
                        Toast.makeText(MemberPaySelfPolicyActivity.this, "Policy Code Required.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    selectedPolicyCodes = "";
                    clearText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void getpolicyValue(){
        txtPolicyHolderView.setText(MemberDataForRenewalPay.applicantName);
        txtPolicyNo.setText(MemberDataForRenewalPay.policyCode);
        txtDateOfCom.setText(MemberDataForRenewalPay.dateOfCom);
        //txtPlanCode.setText(MemberDataForRenewalPay.);
        txtTerm.setText(String.valueOf(MemberDataForRenewalPay.term));
        txtAmount.setText(String.valueOf(MemberDataForRenewalPay.amount));
        txtMode.setText(MemberDataForRenewalPay.mode);


    }



    private void getPolicyCodes(String URL) {
        mArrayListPolicyCodes = new ArrayList<>();
        new GetDataParserArray(MemberPaySelfPolicyActivity.this, URL, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            mArrayListPolicyCodes.add("");
                            for (int i = 0; i < response.length(); i++) {
                                mArrayListPolicyCodes.add(response.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MemberPaySelfPolicyActivity.this,R.layout.spinner_select,mArrayListPolicyCodes);
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_select);
                        mSp_policyCodes.setAdapter(arrayAdapter);
                    } else {
                        Toast.makeText(MemberPaySelfPolicyActivity.this, "No policy codes found", Toast.LENGTH_SHORT).show();
                    }
                }
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
        //txtSearchPolicy = findViewById(R.id.txtSearchPolicy);
        txtRenewalInstNo = findViewById(R.id.txtRenInstNo);

        txtPolicyHolderView = findViewById(R.id.txt_Ren_Policy_Holder_Name);
        txtPolicyNo = findViewById(R.id.txt_Ren_Policy_Number);
        txtDateOfCom = findViewById(R.id.txt_Ren_DOC);
        txtPlanCode = findViewById(R.id.txt_Ren_Plan_Code);
        txtTerm = findViewById(R.id.txt_Ren_Term);
        txtAmount = findViewById(R.id.txt_Ren_Amount);
        txtMode = findViewById(R.id.txt_Ren_Mode);
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

        mSp_policyCodes = findViewById(R.id.sp_activity_member_policy_renewal_select_policy);

        mTv_transFee = findViewById(R.id.tv_activity_member_policy_renewal_trans_fee);
    }

    private void getInstChange() {
        try {
            txtNetAmount.setText(String.valueOf(rData.getAmount() * (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
            netAmount = (rData.getAmount() * (Integer.parseInt(txtRenewalInstNo.getText().toString())));

            amountForPayUMoneyTwoPerc = ((netAmount * 2) / 100);
            amountForPayUMoneyGST = ((amountForPayUMoneyTwoPerc * 18) / 100);
            gstPlusTwoPerc = (amountForPayUMoneyTwoPerc + amountForPayUMoneyGST);

            amountForPayUMoneyFinal = (gstPlusTwoPerc + netAmount);

            mTv_transFee.setText(String.valueOf(gstPlusTwoPerc));

            txtFromInstNo.setText(String.valueOf(rData.getLastInstNo() + 1));
            txtToInstNo.setText(String.valueOf(rData.getLastInstNo() + (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
            // TODO Call method to calculate late fine
            //calculateLateFine(txtSearchPolicy.getText().toString(), String.valueOf(rData.getLastInstNo() + 1), String.valueOf(rData.getLastInstNo() + (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
            getRenewalLatefine(APILinks.GET_RENEWAL_LATE_FINE + "policyCode=" + txtPolicyNo.getText().toString() + "&fromInstNo=" + txtFromInstNo.getText().toString() + "&toInstNo=" + txtToInstNo.getText().toString());

        } catch (Exception ex) {
            //
        }
    }

    private void fetchValue(final String PolicyCode) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberPaySelfPolicyActivity.this, "Loading...");
        rData = new RenewalData();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PolicyManagementMember md = new PolicyManagementMember();
                        try {
                            rData = md.getPolicyDataForRenwal(PolicyCode);
                            txtPolicyHolderView.setText(rData.getApplicantName());
                            txtPolicyNo.setText(rData.getPolicyCode());
                            txtDateOfCom.setText(rData.getDateOfCom());
                            txtPlanCode.setText(rData.getPlanCode());
                            txtTerm.setText(String.valueOf(rData.getTerm()));
                            txtAmount.setText(String.valueOf(rData.getAmount()));
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
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }

    private void insertRenew(final String PolicyCode, final String InstNoFrom, final String InstNoTo, final float lateFine, boolean IsLateFine, final boolean isPaid, final String payUTransID) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberPaySelfPolicyActivity.this, "Loading...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PolicyManagementMember md = new PolicyManagementMember();
                        try {
                            isSuccess = md.insertRenewal(PolicyCode, InstNoFrom, InstNoTo, lateFine, isLateFine, isPaid,payUTransID);
                            if (isSuccess) {
                                isSuccess = false;
                                clearText();
                                actualLateFine = 0f;
                                Toast.makeText(getApplicationContext(), "Renewal paid successfully.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MemberPaySelfPolicyActivity.this, MemberPaySelfPolicyActivity.class));
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
    }

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
                        Toast.makeText(MemberPaySelfPolicyActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            if(netAmount > 0){
                price = amountForPayUMoneyFinal;
                launchPayUMoney(price);
            }
            *//*String msg = "";
            if (!isValidate(msg)) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            } else {
                // TODO Get user entered late fine amount and due late fine
                insertRenew(txtPolicyNo.getText().toString(), String.valueOf(txtFromInstNo.getText()),
                        String.valueOf(txtToInstNo.getText()), actualLateFine, isLateFine, isLateFinePaid);
            }*//*
        } else if (v == btnView) {
            actualLateFine = 0f;
            getInstChange();
        } else if (v == btnSearchPolicy) {
            if (!selectedPolicyCodes.equals("")) {
                fetchValue(selectedPolicyCodes);
            } else {
                Toast.makeText(MemberPaySelfPolicyActivity.this, "Policy Code Required.", Toast.LENGTH_LONG).show();
            }
        }
    }


    *//** ########### PayUMoney Integration *//*
    *//**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     *//*
    private void launchPayUMoney(double amt) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Done");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Pay renewal");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        String txnId = System.currentTimeMillis() + "";
        //String phone = GlobalStore.GlobalValue.getMemberPhone();
        String phone = MemberDataForRenewalPay.applicantPhone;
        String productName = "Policy payment";
        *//*if (GlobalStore.customerName.contains(" ")) {             // TODO Uncomment if need
            firstNameSubString = GlobalStore.customerName.substring(0, GlobalStore.customerName.indexOf(" "));
        }*//*
        String firstName = GlobalStore.GlobalValue.getMemberName();
        //String email = GlobalStore.GlobalValue.getMemberEmail();            // GlobalStore.GlobalValue.getMemberEmail()
        String email = MemberDataForRenewalPay.emailID;            // GlobalStore.GlobalValue.getMemberEmail()
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";

        Environment appEnvironment = ((Application) getApplication()).getEnvironment();
        builder.setAmount(String.format("%.2f", amt))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                //.setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            paymentParam = builder.build();
            generateHashFromServer(paymentParam);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    // TODO Uncomment this section
                    String response = transactionResponse.getPayuResponse();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                        payUTransID = String.valueOf(jsonObjectResult.getInt("paymentId"));
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    insertRenew(txtPolicyNo.getText().toString(), String.valueOf(txtFromInstNo.getText()),
                            String.valueOf(txtToInstNo.getText()), actualLateFine, isLateFine, isLateFinePaid,payUTransID);             // TODO Work with database
                    //Toast.makeText(this, "Your transaction was successful", Toast.LENGTH_SHORT).show();
                } else {
                    //Failure Transaction
                    *//*insertRenew(txtPolicyNo.getText().toString(), String.valueOf(txtFromInstNo.getText()),
                            String.valueOf(txtToInstNo.getText()), actualLateFine, isLateFine, isLateFinePaid,payUTransID);*//*
                    Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
            }  *//*else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*//*
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MemberPaySelfPolicyActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL(APILinks.GET_PAY_U_HASH);

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                //System.out.println("Response "+responseStringBuffer.toString());
                String resp = "Response " + responseStringBuffer.toString();
                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        *//**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *//*
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(MemberPaySelfPolicyActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                paymentParam.setMerchantHash(merchantHash);
                PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, MemberPaySelfPolicyActivity.this, R.style.AppTheme_default, false);
            }
        }
    }

    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {


        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));


        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    *//**
     * This AsyncTask generates hash from server.
     *//*

    private void clearText() {
        //txtPolicyHolderView.setText("");
        txtPolicyHolderView.setText("");
        txtPolicyNo.setText("");
        txtDateOfCom.setText("");
        txtPlanCode.setText("");
        txtTerm.setText("");
        txtAmount.setText("");
        txtMode.setText("");
        txtNetAmount.setText("");
        txtFromInstNo.setText("");
        txtToInstNo.setText("");
        txtRenewalInstNo.setText("");
        mTv_transFee.setText("");
        actualLateFine = 0.f;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberPaySelfPolicyActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void bindEventHandlers() {
        btnSave.setOnClickListener(this);
        btnView.setOnClickListener(this);
    }*/
}
