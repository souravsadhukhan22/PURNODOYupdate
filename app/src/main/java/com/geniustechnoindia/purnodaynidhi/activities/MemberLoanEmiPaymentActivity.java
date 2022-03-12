package com.geniustechnoindia.purnodaynidhi.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/*import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;*/

public class MemberLoanEmiPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    /*
    private EditText txtSearchLoan;
    private TextView txtLoanHolderName, txtLoanCode, txtLoanDate, txtLoanAmount, txtLoanTerm,
            txtLoanEMIAmount, txtLoanMode, txtLoanEMIFromInstNo, txtLoanEMIToInstNo, devBygen,txtDepositAmount;
    private Button btnEMIView, btnLoanEMISave;
    private LoanData lData;

    private int LateFineSummary;
    private boolean isLateFinePaid=false;
    private CheckBox mCb_lateFinePay;

    private ArrayList<LoanEMIBrakupData> arrayListBreakUp;
    private LoanEMIBrakupData bData;
    private String value = "";
    private String cDate = "";

    private static int serverDate;      // Server current date
    private static int payDate;         // EMI pay date

    private static int dateDifference;
    private static float lateFineParc;
    private static String loanMode;
    private static float emiAmount;
    private static float finalLateFine;

    private TextView mTv_totalLatefine;
    private RecyclerView mRv_latefine;
    private static String payUTransID = "";


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

    private Spinner mSp_loanCodes;
    private ArrayList<String> mArrayListLoanCodes;

    private PayUmoneySdkInitializer.PaymentParam paymentParam;
    private EditText mEt_enterNoOfEMI;
    private double totalDepositAmount = 0.0;

    private static double amountForPayUMoneyTwoPerc = 0.0;

    private static double amountForPayUMoneyGST = 0.0;

    private static double amountForPayUMoneyFinal = 0.0;

    private static double gstPlusTwoPerc = 0.0;

    private TextView mTv_totalTransFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_loan_emi_payment);


        // initialize widget
        setViewReferences();
        bindEventHandlers();
        adapters();
        layoutManagers();

        fetchValue(MemberDataForLoanPay.loanCode);

        //getLoanCodes(APILinks.GET_LOAN_CODES_BY_MEMBER_CODE + GlobalStore.GlobalValue.getMemberCode());

        mCb_lateFinePay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {                                                                    // Arranger is paying late fine
                    isLateFinePaid = true;
                    calculateTransFee(totalDepositAmount + totalLatefine);
                    *//*for (int i = 0; i < arrayList.size(); i++) {
                        lateFinePaidCommaSep = lateFinePaidCommaSep + arrayList.get(i).getLatefine() + ",";
                    }*//*
                } else {                                                                            // // Arranger not paying late fine
                    isLateFinePaid = false;
                    calculateTransFee(totalDepositAmount);
                    *//*for (int i = 0; i < arrayList.size(); i++) {
                        lateFineNotPaid = lateFineNotPaid + arrayList.get(i).getLatefine() + ",";
                    }*//*
                }
            }
        });


        // Get loan codes
        mSp_loanCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    if(mArrayListLoanCodes != null){
                        fetchValue(mArrayListLoanCodes.get(position));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mEt_enterNoOfEMI.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if(count > 0){
                    if(!s.equals("")){
                        double noOfEmi = Double.parseDouble(s.toString());
                        totalDepositAmount = (emiAmount * noOfEmi);
                        txtDepositAmount.setText(String.valueOf(totalDepositAmount));
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


    }

    private void getLoanCodes(String URL){
        mArrayListLoanCodes = new ArrayList<>();
        new GetDataParserArray(MemberLoanEmiPaymentActivity.this, URL, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            mArrayListLoanCodes.add("");
                            for (int i = 0; i < response.length(); i++) {
                                mArrayListLoanCodes.add(response.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MemberLoanEmiPaymentActivity.this,R.layout.spinner_select,mArrayListLoanCodes);
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_select);
                        mSp_loanCodes.setAdapter(arrayAdapter);
                    } else {
                        Toast.makeText(MemberLoanEmiPaymentActivity.this, "No policy codes found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // fetch loan by loan code
    private void fetchValue(final String loanCode) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberLoanEmiPaymentActivity.this, "Loading...");
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

                                serverDate = lData.getServerDate();
                                //String loanDate = lData.getLoanDate();

                                txtLoanAmount.setText(String.valueOf(lData.getLoanApproveAmount()));
                                txtLoanTerm.setText(String.valueOf(lData.getLoanTerm()));
                                txtLoanEMIAmount.setText(String.valueOf(lData.getEMIAmount()));
                                emiAmount = lData.getEMIAmount();
                                txtLoanMode.setText(lData.getEMIMode());
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
        for(int i = 0; i <arrayListLateFine.size(); i++){
            hashMap.put(arrayListLateFine.get(i).getInstNo(),arrayListLateFine.get(i).getLatefine());
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

                        if(hashMap.get(rs.getInt("PERIOD")) == null){
                            bData.setLateFineAmt(0);
                        } else
                        {
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
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberLoanEmiPaymentActivity.this, "Wait...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        LoanManagementMember lm = new LoanManagementMember();
                        try {
                            ErrorData err = lm.insertLoanEMI(lpd);
                            if (err.getErrorCode() > 0 && err.getErrorCode() != 50005) {
                                // Error
                                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            } else {
                                clearText();
                                arrayListBreakUp = null;
                                lData = null;
                                mLl_lateFineRoot.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
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
                                jsonObject.getString("PolicyCode");
                                arrayList.add(renewalLatefineSetGet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        latefineAdapter.notifyDataSetChanged();
                        mTv_totalLatefine.setText(totalLatefine + "");
                        fillEMIData(txtLoanCode.getText().toString(), Integer.parseInt(txtLoanEMIFromInstNo.getText().toString()),
                                Integer.parseInt(txtLoanEMIToInstNo.getText().toString()),arrayList);
                    } else {
                        isLateFine = false;
                        mLl_lateFineRoot.setVisibility(View.GONE);
                        Toast.makeText(MemberLoanEmiPaymentActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                        fillEMIData(txtLoanCode.getText().toString(), Integer.parseInt(txtLoanEMIFromInstNo.getText().toString()),
                                Integer.parseInt(txtLoanEMIToInstNo.getText().toString()),arrayList);
                    }
                }
            }
        });
    }

    private void fillEMIData(final String loanCode, final Integer f, final Integer t, final ArrayList<RenewalLatefineSetGet> arrayList) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(MemberLoanEmiPaymentActivity.this, "Loading...");
        bData = new LoanEMIBrakupData();
        arrayListBreakUp = new ArrayList<LoanEMIBrakupData>();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            arrayListBreakUp = getLoanEMIBrakupData(loanCode, lData.getReducingEMI(), f, t,arrayList);
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
            calculateTransFee(totalDepositAmount);
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
            launchPayUMoney(amountForPayUMoneyFinal);
        }
    }

    private void calculateTransFee(double totalDepositAmount){
        amountForPayUMoneyTwoPerc = ((totalDepositAmount * 2) / 100);
        amountForPayUMoneyGST = ((amountForPayUMoneyTwoPerc * 18) / 100);
        gstPlusTwoPerc = (amountForPayUMoneyTwoPerc + amountForPayUMoneyGST);

        amountForPayUMoneyFinal = (gstPlusTwoPerc + totalDepositAmount);

        mTv_totalTransFee.setText(String.format("%.2f", gstPlusTwoPerc));
    }

    *//** PayUMoney Code *//*
    private void launchPayUMoney(double amt) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Done");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Pay renewal");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        String txnId = System.currentTimeMillis() + "";
        //String phone = GlobalStore.GlobalValue.getMemberPhone();
        String phone = MemberDataForLoanPay.phoneNo;
        String productName = "EMI Payment";
        *//*if (GlobalStore.customerName.contains(" ")) {             // TODO Uncomment if need
            firstNameSubString = GlobalStore.customerName.substring(0, GlobalStore.customerName.indexOf(" "));
        }*//*
        String firstName = GlobalStore.GlobalValue.getMemberName();
        //String email = GlobalStore.GlobalValue.getMemberEmail();           // GlobalStore.GlobalValue.getMemberEmail()
        String email = MemberDataForLoanPay.email;          // GlobalStore.GlobalValue.getMemberEmail()
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
                    // TODO Work with database (Insert save DB work here) ##########################
                    updateDB();
                } else {
                    //Failure Transaction
                    Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateDB(){
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
        lpd.setTotalPaymentAmount((int) Double.parseDouble(txtDepositAmount.getEditableText().toString()));
        lpd.setActualEMIAmount(Math.round(lData.getEMIAmount()));
        lpd.setPayMode("");
        lpd.setDepACCLCode("");
        lpd.setSavingsAC("");
        lpd.setFromCode(lData.getLCode());
        lpd.setToCode("");
        lpd.setChequeNo("");
        lpd.setChequeDate(lData.getServerDate());
        lpd.setEMIDetails(EMIDetails);
        lpd.setRemarks("");
        lpd.setBounceCharge(Float.parseFloat("0"));
        lpd.setRecoveryCharge(Float.parseFloat("0"));
        lpd.setDueLateFine(false);
        lpd.setDueLateFine(isLateFinePaid);
        lpd.setPayUTransID(String.valueOf(payUTransID));
        saveLoanEMI(lpd);
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

    @SuppressLint("StaticFieldLeak")
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MemberLoanEmiPaymentActivity.this);
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
                Toast.makeText(MemberLoanEmiPaymentActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                paymentParam.setMerchantHash(merchantHash);
                PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, MemberLoanEmiPaymentActivity.this, R.style.AppTheme_default, false);
            }
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
        txtSearchLoan = findViewById(R.id.txtSearchLoan);
        txtDepositAmount = findViewById(R.id.tv_activity_member_loan_emi_payment_deposit_amount);

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

        //btnSearchLoan = findViewById(R.id.btnSearchLoan);
        btnEMIView = findViewById(R.id.btnEMIView);
        btnLoanEMISave = findViewById(R.id.btnLoanEMISave);

        mTv_totalLatefine = findViewById(R.id.tv_activity_renewal_collection_total_late_fine);

        mRv_latefine = findViewById(R.id.rv_activity_loan_collection_loan_emi_late_fine);

        mLl_lateFineRoot = findViewById(R.id.ll_activity_renewal_collection_late_fine_root);

        mCb_lateFinePay=findViewById(R.id.cb_activity_arranger_loan_collection);

        mSp_loanCodes = findViewById(R.id.sp_activity_member_loan_emi_payment_loan_codes);

        mEt_enterNoOfEMI = findViewById(R.id.et_activity_loan_collection_enter_no_of_emi);

        mTv_totalTransFee = findViewById(R.id.tv_activity_member_loan_emi_payment_trans_fee);
    }

    private void bindEventHandlers() {
        //btnSearchLoan.setOnClickListener(this);
        btnEMIView.setOnClickListener(this);
        btnLoanEMISave.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberLoanEmiPaymentActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }*/
}