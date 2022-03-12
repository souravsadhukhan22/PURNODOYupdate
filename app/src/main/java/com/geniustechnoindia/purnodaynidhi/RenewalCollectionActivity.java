package com.geniustechnoindia.purnodaynidhi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.adapters.LatefineAdapter;
import com.geniustechnoindia.purnodaynidhi.bean.RenewalData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.dl.PolicyManagement;
import com.geniustechnoindia.purnodaynidhi.model.RenewalLatefineSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.TempData;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserObject;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RenewalCollectionActivity extends AppCompatActivity {

    EditText txtSearchPolicy, txtRenewalInstNo;
    TextView txtPolicyHolderView, txtPolicyNo, txtDateOfCom,
            txtPlanCode, txtTerm, txtAmount, txtMode, txtNetAmount,
            txtFromInstNo, txtToInstNo, mTv_lateFine,tvWalletBalance,tvPreviousDepositeDate,tvPolicyAmount;
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

    private double walletBalance = 0.0;

    private TextView tvPhoneNumber;
    private ImageView mIv_holderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewal_collection);

        setViewReferences();
        adapters();
        layoutManagers();

        getWalletBalance(APILinks.GET_ARRANGER_WALLET_BALANCE + GlobalStore.GlobalValue.getUserName());

        this.clearText();

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

        btnSearchPolicy.setOnClickListener(v -> {
            if (txtSearchPolicy.getEditableText().toString().trim().isEmpty()) {
                Toast.makeText(RenewalCollectionActivity.this, "Policy Code Required.", Toast.LENGTH_LONG).show();
            } else {
                fetchValue(txtSearchPolicy.getEditableText().toString());
                // checkIfAnyRenewalPending(APILinks.CHECK_IF_ANY_RENEWAL_PENDING + txtSearchPolicy.getEditableText().toString() );
                // clearText();
            }
        });

        btnView.setOnClickListener(v -> {
            actualLateFine = 0f;
            getInstChange();
        });

        txtRenewalInstNo.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                getInstChange();
            }
        });

        btnSave.setOnClickListener(v -> {
            if(walletBalance > Double.parseDouble(txtNetAmount.getText().toString())){
                String msg = "";
                if (!isValidate(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else {
                    // TODO Get user entered late fine amount and due late fine
                    insertRenew(txtPolicyNo.getText().toString(), String.valueOf(txtFromInstNo.getText()),
                            String.valueOf(txtToInstNo.getText()), actualLateFine, isLateFine, isLateFinePaid);
                }
            } else {
                Toast.makeText(RenewalCollectionActivity.this, "Not enough balance in wallet", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getWalletBalance(String walletURL) {
        new GetDataParserObject(RenewalCollectionActivity.this, walletURL, true, response -> {
            try {
                if (response != null) {
                    walletBalance = response.getDouble("bal");
                    tvWalletBalance.setText("Rs. " + walletBalance);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
        tvPolicyAmount=findViewById(R.id.tv_activity_renewal_collection_total_amount);
        tvPreviousDepositeDate=findViewById(R.id.tv_activity_renewal_collection_previous_deposite_date);
        btnSearchPolicy = findViewById(R.id.btnSearchPolicy);
        btnView = findViewById(R.id.btnRenView);
        btnSave = findViewById(R.id.btnRenewalSave);
        mLl_lateFineRoot = findViewById(R.id.ll_activity_renewal_collection_late_fine_root);
        mTv_totalLatefine = findViewById(R.id.tv_activity_renewal_collection_total_late_fine);
        mCb_lateFinePay = findViewById(R.id.cb_activity_arranger_renewal_collection);

        mRv_latefine = findViewById(R.id.rv_activity_renewal_collection_loan_emi_late_fine);

        mTv_lateFineApplicable = findViewById(R.id.tv_activity_renewal_collection_late_fine_applicable);
        mTv_lateFinePercentage = findViewById(R.id.tv_activity_renewal_collection_late_fine_percentage);
        mTv_lateFine = findViewById(R.id.tv_activity_renewal_collection_late_fine);
        tvWalletBalance = findViewById(R.id.tvWalletBalance);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
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
            txtNetAmount.setText(String.valueOf(rData.getAmount() * (Integer.parseInt(txtRenewalInstNo.getEditableText().toString()))));
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
        final ProgressDialog dialog = DialogUtils.showProgressDialog(RenewalCollectionActivity.this, "Loading...");
        rData = new RenewalData();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PolicyManagement md = new PolicyManagement();
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
                            tvPhoneNumber.setText(TempData.phoneNumber);
                            if(rData.getHolderPhoto() != null && !rData.getHolderPhoto().equals("")){
                                byte[] decodedString = Base64.decode(rData.getHolderPhoto().toString(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                mIv_holderImage.setImageBitmap(decodedByte);
                            }
                            txtMode.setText(rData.getMode());
                            cDate = rData.getMaturityDate();
                            tvPreviousDepositeDate.setText(rData.getLAST_DEP_DATE());
                            tvPolicyAmount.setText(rData.getPolicyAmount());
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Policy code not associated with this arranger", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }

    private void insertRenew(final String PolicyCode, final String InstNoFrom, final String InstNoTo, final float lateFine, boolean IsLateFine, final boolean isPaid) {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(RenewalCollectionActivity.this, "Loading...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        PolicyManagement md = new PolicyManagement();
                        try {
                            insertErrorCode = md.insertRenewal(PolicyCode, InstNoFrom, InstNoTo, lateFine, isLateFine, isPaid);
                            if (insertErrorCode == 0) {
                                isSuccess = false;
                                clearText();
                                actualLateFine = 0f;
                                Toast.makeText(getApplicationContext(), "Successfully Saved.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RenewalCollectionActivity.this, RenewalCollectionActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else if (insertErrorCode == 50005) {
                                Toast.makeText(RenewalCollectionActivity.this, "Not enough balance in wallet", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RenewalCollectionActivity.this, RenewalCollectionActivity.class));
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
        new GetDataParserArray(this, url, true, response -> {
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
                    Toast.makeText(RenewalCollectionActivity.this, "Late fine not applicable", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(RenewalCollectionActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
