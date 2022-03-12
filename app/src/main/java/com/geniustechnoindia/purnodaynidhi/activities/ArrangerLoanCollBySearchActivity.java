package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
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

import androidx.appcompat.app.AppCompatActivity;
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
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ArrangerLoanCollBySearchActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtSearchLoan, txtDepositAmount;
    TextView txtLoanHolderName, txtLoanCode, txtLoanDate, txtLoanAmount, txtLoanTerm,
            txtLoanEMIAmount, txtLoanMode, txtLoanEMIFromInstNo, txtLoanEMIToInstNo, devBygen;
    Button btnSearchLoan, btnEMIView, btnLoanEMISave;
    LoanData lData;

    private ImageView mIv_holderImage;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_loan_coll_by_search);

        // initialize widget
        setViewReferences();
        bindEventHandlers();
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

    }

    // fetch loan by loan codetxtSearchLoan
    private void fetchValue(final String loanCode) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrangerLoanCollBySearchActivity.this, "Loading...");
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


                                if(lData.getHolderPhoto() != null && !lData.getHolderPhoto().equals("")) {
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
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrangerLoanCollBySearchActivity.this, "Wait...");
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
                                Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Please approve any previous pending approval", Toast.LENGTH_SHORT).show();
                            } else {
                                clearText();
                                arrayListBreakUp = null;
                                lData = null;
                                mLl_lateFineRoot.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ArrangerLoanCollBySearchActivity.this,ArrangerLoanCollBySearchActivity.class));
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
                        Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
                        fillEMIData(txtLoanCode.getText().toString(), Integer.parseInt(txtLoanEMIFromInstNo.getText().toString()),
                                Integer.parseInt(txtLoanEMIToInstNo.getText().toString()), arrayList);
                    }
                }
            }
        });
    }

    private void fillEMIData(final String loanCode, final Integer f, final Integer t, final ArrayList<RenewalLatefineSetGet> arrayList) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrangerLoanCollBySearchActivity.this, "Loading...");
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

            if (mStr_type.length() > 0) {
                if (txtSearchLoan.getText().toString().length() > 0) {
                    getLoanCodeDetails(APILinks.ARRANGER_GET_LOAN_CODE + mStr_type + "&value=" + txtSearchLoan.getText().toString());
                } else {
                    Toast.makeText(this, "Enter Name or Loan Code", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Select Type", Toast.LENGTH_SHORT).show();
            }


        } else if (v == btnEMIView) {
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
                    checkIfAnyEmiPending(APILinks.CHECK_IF_ANY_EMI_PENDING + mStr_selectedLoanCode);
                } else {
                    Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Loan Code Required", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void getLoanCodeDetails(String url) {
        arrayList_loanCode.clear();
        arrayList_loanName.clear();
        arrayList_loanCodeName.clear();

        arrayList_loanCode.add("");
        arrayList_loanName.add("");
        arrayList_loanCodeName.add("");
        new GetDataParserArray(this, url, false, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject=response.getJSONObject(i);

                            arrayList_loanCode.add(jsonObject.getString("LoanCode"));
                            arrayList_loanName.add(jsonObject.getString("HolderName"));
                            arrayList_loanCodeName.add(jsonObject.getString("HolderName") + " - "+jsonObject.getString("LoanCode"));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(ArrangerLoanCollBySearchActivity.this, R.layout.row_savings_acc_hint, arrayList_loanCodeName);
                        arrayAdapter.setDropDownViewResource(R.layout.row_savings_acc_hint);
                        mSp_loanCode.setAdapter(arrayAdapter);
                    } else {
                        Toast.makeText(ArrangerLoanCollBySearchActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        mIv_holderImage = findViewById(R.id.ivLoanHolderImage);
    }


    private void checkIfAnyEmiPending(String url) {
        new GetDataParserArray(ArrangerLoanCollBySearchActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            if (response.getInt(0) == 1) {            // There is a pending approval
                                Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Please check for any pending approval", Toast.LENGTH_LONG).show();
                            } else if (response.getInt(0) == 0) {
                                fetchValue(mStr_selectedLoanCode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ArrangerLoanCollBySearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void bindEventHandlers() {
        btnSearchLoan.setOnClickListener(this);
        btnEMIView.setOnClickListener(this);
        btnLoanEMISave.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ArrangerLoanCollBySearchActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}

