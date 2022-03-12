package com.geniustechnoindia.purnodaynidhi.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.InvestmentAccountStatementAdapter;
import com.geniustechnoindia.purnodaynidhi.bean.LoanData;
import com.geniustechnoindia.purnodaynidhi.bean.PolicySearchByNameData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.model.SetGetInvestmentAccountStatement;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrangerPolicyRenewViewActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private EditText mEt_memberCode;
    private EditText mEt_policyCode;
    private Button mBtn_show;

    private Button mBtn_saveAsPdfPolicy;
    private static final int REQUEST_ID_EXTERNAL_STORAGE = 128;

    private LinearLayout mLl_root;

    // RecyclerView
    private SetGetInvestmentAccountStatement setGetInvestmentAccountStatement;
    private ArrayList<SetGetInvestmentAccountStatement> mArrayList;
    private InvestmentAccountStatementAdapter mInvestmentAccountStatementAdapter;
    private RecyclerView mRv_policyView;

    private EditText mEt_policyOrName;
    private Button mBtn_policyManualSearch;
    private Spinner mSp_policyNameCodeList;
    private ArrayList<String> arrayList_policyCode =new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName =new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_policy_renew_view_activity);

        setViewReferences();
        bindEventHandlers();
        setupToolbar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRv_policyView.setLayoutManager(linearLayoutManager);

        if(PolicySearchByNameData.policyCode.length()>0){
            mEt_policyCode.setText(PolicySearchByNameData.policyCode);
            mBtn_show.performClick();
            mBtn_show.setPressed(true);
        }


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
                    Toast.makeText(ArrangerPolicyRenewViewActivity.this, "enter search value", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSp_policyNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mEt_policyCode.setText(arrayList_policyCode.get(i));
                    mBtn_show.performClick();
                    mBtn_show.setClickable(true);
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
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(ArrangerPolicyRenewViewActivity.this,R.layout.spinner_hint, arrayList_policyCodeName);
                        mSp_policyNameCodeList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(ArrangerPolicyRenewViewActivity.this, "NoData", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void getRenewalReportByPolicyCode(String policyCode, String memberCode, String type) {
        Connection cn = new SqlManager().getSQLConnection();
        LoanData loanData = new LoanData();
        mArrayList = new ArrayList<SetGetInvestmentAccountStatement>();

        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetRenewalReportByPolicyCodeOnly(?,?)}");
                smt.setString("@PolicyCode", policyCode);
                //smt.setString("@MemberCode", memberCode);
                smt.setString("@UserType", type);
                smt.execute();
                ResultSet rs = smt.getResultSet();

                int count = 0;

                if(rs.isBeforeFirst()){
                    while (rs.next()) {
                        //if (count < 10) {
                            setGetInvestmentAccountStatement = new SetGetInvestmentAccountStatement();
                            setGetInvestmentAccountStatement.setPolicyCode("Policy Code - " + rs.getString("PolicyCode"));
                            setGetInvestmentAccountStatement.setInstNo("Inst. No. " + String.valueOf(rs.getInt("InstNo")));
                            setGetInvestmentAccountStatement.setRenewDate("Renew Date - " + rs.getString("RenewDate"));
                            setGetInvestmentAccountStatement.setAmount("Rs. " + String.valueOf(rs.getInt("Amount")));
                            setGetInvestmentAccountStatement.setLateFine("Late fine - Rs. " + String.valueOf(rs.getInt("LateFine")));
                            mArrayList.add(setGetInvestmentAccountStatement);
                            loanData.setErrorCode(0);
                        //}
                        //count += 1;
                    }
                    mLl_root.setVisibility(View.VISIBLE);
                } else {
                    mLl_root.setVisibility(View.GONE);
                }
                mInvestmentAccountStatementAdapter = new InvestmentAccountStatementAdapter(ArrangerPolicyRenewViewActivity.this, mArrayList);
                mRv_policyView.setAdapter(mInvestmentAccountStatementAdapter);
            } else {
                loanData.setErrorCode(2);
                loanData.setErrorString("Network related problem.");
            }
        } catch (Exception ex) {
            loanData.setErrorCode(1);
            loanData.setErrorString(ex.getMessage().toString());
        }

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Policy Report");
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        mEt_memberCode = findViewById(R.id.et_activity_arranger_policy_renew_view_activity_member_code);
        mEt_policyCode = findViewById(R.id.et_activity_arranger_policy_renew_view_activity_policy_code);
        mBtn_show = findViewById(R.id.btn_activity_arranger_policy_renew_view_activity_show);
        mLl_root = findViewById(R.id.ll_activity_arranger_policy_renew_view_activity_root);

        mBtn_saveAsPdfPolicy = findViewById(R.id.btn_activity_arranger_policy_renew_view);

        mRv_policyView = findViewById(R.id.rv_arranger_policy_renew_view_activity);

        mSp_policyNameCodeList = findViewById(R.id.sp_activity_renewal_collection_policy_list);
        mEt_policyOrName=findViewById(R.id.et_activity_renewal_collection_polcode_name);
        mBtn_policyManualSearch=findViewById(R.id.btn_activity_renewal_collection_manual_search);
    }

    private void bindEventHandlers() {
        mBtn_show.setOnClickListener(this);
        mBtn_saveAsPdfPolicy.setOnClickListener(this);
        mBtn_policyManualSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_show) {
            //if (mEt_memberCode.getText().toString().trim().length() > 0) {
                if (mEt_policyCode.getText().toString().trim().length() > 0) {

                    final ProgressDialog dialog= DialogUtils.showProgressDialog(ArrangerPolicyRenewViewActivity.this,"Please wait...");
                    new Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    getRenewalReportByPolicyCode(mEt_policyCode.getText().toString(), mEt_memberCode.getText().toString(), "");
                                    dialog.dismiss();
                                }
                            }, 3000);
                } else {
                    mEt_policyCode.setError("Enter policy code");
                    mEt_policyCode.requestFocus();
                }
            /*} else {
                mEt_memberCode.setError("Enter member code");
                mEt_memberCode.requestFocus();
            }*/

        } else if(v == mBtn_saveAsPdfPolicy){
            if (checkAndRequestPermissions()) {
                saveDataAsPdf();
            }
        }
    }

    /**
     * Check storage permission
     */
    private boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(ArrangerPolicyRenewViewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        java.util.List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ArrangerPolicyRenewViewActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }


    private void saveDataAsPdf() {
        List list = new List();
        Document document = new Document();
        long time = System.currentTimeMillis();
        String fileName = "PolicyRenewalMiniStatement_" + mEt_policyCode.getText().toString() + "_" + time;
        String path = Environment.getExternalStorageDirectory() + "/A/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path + fileName + ".pdf"));
            document.open();
            String insNo = "";
            String renewDate = "";
            String amount = "";

            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph headingPara = new Paragraph(getString(R.string.App_Full_Name), font);
            headingPara.setAlignment(Element.ALIGN_CENTER);
            headingPara.setSpacingAfter(20f);
            document.add(headingPara);

            Font fontMiniStatement = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph headingParaAccount = new Paragraph("Policy Renewal Mini statement", fontMiniStatement);
            headingParaAccount.setAlignment(Element.ALIGN_CENTER);
            headingParaAccount.setSpacingAfter(10f);
            document.add(headingParaAccount);

            Font fontAccountNo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph headingParaAccountNumber = new Paragraph("Account no - " + mEt_policyCode.getText().toString(), fontAccountNo);
            headingParaAccountNumber.setAlignment(Element.ALIGN_CENTER);
            headingParaAccountNumber.setSpacingAfter(30f);
            document.add(headingParaAccountNumber);

            PdfPTable tableHeadingFour = new PdfPTable(3);
            tableHeadingFour.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeadingFour.addCell("InstNo.");
            tableHeadingFour.addCell("Amount");
            tableHeadingFour.addCell("Renew Date");

            document.add(tableHeadingFour);

            for (SetGetInvestmentAccountStatement setGetInvestmentAccountStatement : mArrayList) {
                insNo = setGetInvestmentAccountStatement.getInstNo();
                renewDate = setGetInvestmentAccountStatement.getRenewDate();
                amount = setGetInvestmentAccountStatement.getAmount();
                //withdraw = savingsStatementSetGet.getWithdrawlAmount();
                //row = tDate + "  |  " + mode + "  |  " + deposit + "  |  " + withdraw;


                PdfPTable tableRows = new PdfPTable(3);
                tableRows.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableRows.addCell(insNo);
                tableRows.addCell(amount);
                tableRows.addCell(renewDate);
                //tableRows.addCell(withdraw);
                document.add(tableRows);


                //document.add(new Paragraph(row));
                document.add(new Paragraph());
            }
            document.close();
            Toast.makeText(this, "Document successfully saved in A folder", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_EXTERNAL_STORAGE: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // TODO Get current location
                        saveDataAsPdf();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ArrangerPolicyRenewViewActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("This permission is required to get your current location",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(ArrangerPolicyRenewViewActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ArrangerPolicyRenewViewActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(ArrangerPolicyRenewViewActivity.this, MainActivity.class);
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
        Intent intent = new Intent(ArrangerPolicyRenewViewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}