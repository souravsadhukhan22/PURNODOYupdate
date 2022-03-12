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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterLoanEmiStatement;
import com.geniustechnoindia.purnodaynidhi.bean.LoanEMIBrakupData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.model.SetGetLoanEMI;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ArrangerLoanStatementAndPrint extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView toolbarTitle;

    // views
    private EditText mEt_enterLoanCode;
    private Button mBtn_show;

    private RecyclerView mRecyclerView;
    private AdapterLoanEmiStatement adapterLoanEmiStatement;

    private LinearLayout mLl_root;

    private Button mBtn_saveLoanStatementAsPdf;
    private static final int REQUEST_ID_EXTERNAL_STORAGE = 157;

    private ArrayList<SetGetLoanEMI> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arranger_loan_statement_and_print);

        setViewReferences();
        bindEventHandlers();
        setUpToolbar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public ArrayList<SetGetLoanEMI> getLoanEMIStatement(String loanCode) {
        Connection cn = new SqlManager().getSQLConnection();
        arrayList = new ArrayList<SetGetLoanEMI>();
        SetGetLoanEMI setGetLoanEmi;

        LoanEMIBrakupData bData = null;
        CallableStatement smt = null;
        double totalPaid = 0.0;
        try {
            if (cn != null) {
                smt = cn.prepareCall("{call ADROID_GetLoanEmiStatement(?)}");
                smt.setString("@LOANCODE", loanCode);
                smt.execute();
                //ResultSet rs=smt.getResultSet();
                ResultSet rs = smt.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        setGetLoanEmi = new SetGetLoanEMI();
                        setGetLoanEmi.setLoanCode(rs.getString("LoanCode"));
                        setGetLoanEmi.setEmiNo(rs.getString("EMINo"));

                        String emiDueDate = rs.getString("EMIDueDate");
                        Date emiDueDateNew = null;
                        try {
                            emiDueDateNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(emiDueDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedEmiDueDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(emiDueDateNew);
                        setGetLoanEmi.setEmiDueDate(formattedEmiDueDate);

                        String paymentDate = rs.getString("PaymentDate");
                        Date paymentDateNew = null;
                        try {
                            paymentDateNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(paymentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedPaymentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(paymentDateNew);
                        setGetLoanEmi.setPaymentDate(formattedPaymentDate);

                        setGetLoanEmi.setEmiAmount(rs.getString("EMIAmount"));
                        setGetLoanEmi.setCurrentBalance(rs.getString("CurrantBalance"));
                        setGetLoanEmi.setPayMode(rs.getString("PayMode"));
                        setGetLoanEmi.setRemarks(rs.getString("Remarks"));
                        arrayList.add(setGetLoanEmi);
                        //noOfPaidTerm ++;
                        //totalPaid = totalPaid + TempLoanData.emiAmount;
                        //mTv_totalEmiPaid.setText("Rs. " + totalPaid);
                    }
                    //int dueT = TempLoanData.loanTerm - noOfPaidTerm;
                    //mTv_dueTerm.setText(dueT + "");

                    mLl_root.setVisibility(View.VISIBLE);
                } else {
                    mLl_root.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ArrangerLoanStatementAndPrint.this);
                    builder.setMessage("Statement not found for this account");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ArrangerLoanStatementAndPrint.this, ArrangerStatementActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }).show();
                }

                adapterLoanEmiStatement = new AdapterLoanEmiStatement(ArrangerLoanStatementAndPrint.this, arrayList);
                mRecyclerView.setAdapter(adapterLoanEmiStatement);
            } else {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }

    private void setViewReferences() {
        mToolbar = findViewById(R.id.custom_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        mRecyclerView = findViewById(R.id.rv_activity_arranger_loan_statement_and_print);

        mEt_enterLoanCode = findViewById(R.id.et_activity_arranger_loan_statement_loan_code);
        mBtn_show = findViewById(R.id.btn_activity_arranger_loan_statement_and_print_show);
        mLl_root = findViewById(R.id.ll_activity_arranger_loan_statement_root);

        mBtn_saveLoanStatementAsPdf = findViewById(R.id.btn_activity_arranger_loan_statement_and_print_save_loan_statement_pdf);
    }

    private void bindEventHandlers() {
        mBtn_show.setOnClickListener(this);
        mBtn_saveLoanStatementAsPdf.setOnClickListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbarTitle.setText("Loan EMI Statement");
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_show) {
            if (mEt_enterLoanCode.getText().toString().trim().length() > 0) {
                final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrangerLoanStatementAndPrint.this, "Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLoanEMIStatement(mEt_enterLoanCode.getText().toString());          // TODO Get loan code
                        dialog.dismiss();
                    }
                }, 3000);
            } else {
                mEt_enterLoanCode.setError("Enter loan code");
                mEt_enterLoanCode.requestFocus();
            }
        } else if (v == mBtn_saveLoanStatementAsPdf) {
            if (checkAndRequestPermissions()) {
                saveDataAsPdf();
            }
        }
    }

    /**
     * Check storage permission
     */
    private boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(ArrangerLoanStatementAndPrint.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        java.util.List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ArrangerLoanStatementAndPrint.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_EXTERNAL_STORAGE);
            return false;
        }
        return true;
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ArrangerLoanStatementAndPrint.this,
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
                            Toast.makeText(ArrangerLoanStatementAndPrint.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }

    private void saveDataAsPdf() {
        List list = new List();
        Document document = new Document();
        long time = System.currentTimeMillis();

        String loanCode = mEt_enterLoanCode.getText().toString();
        String[] loanCodeArray = loanCode.split("/");

        String fileName = "LoanEmiMiniStatement_" + loanCodeArray[0] + "_" + time;
        String path = Environment.getExternalStorageDirectory() + "/A/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path + fileName + ".pdf"));
            document.open();
            String emiNo = "";
            String emiAmount = "";
            String paymentDate = "";
            String emiDueDate = "";
            String currentBalance = "";
            String row = "";

            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph headingPara = new Paragraph(getString(R.string.App_Full_Name), font);
            headingPara.setAlignment(Element.ALIGN_CENTER);
            headingPara.setSpacingAfter(20f);
            document.add(headingPara);

            Font fontMiniStatement = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph headingParaAccount = new Paragraph("Loan EMI Mini Statement", fontMiniStatement);
            headingParaAccount.setAlignment(Element.ALIGN_CENTER);
            headingParaAccount.setSpacingAfter(10f);
            document.add(headingParaAccount);

            Font fontAccountNo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph headingParaAccountNumber = new Paragraph("Account no - " + mEt_enterLoanCode.getText().toString(), fontAccountNo);
            headingParaAccountNumber.setAlignment(Element.ALIGN_CENTER);
            headingParaAccountNumber.setSpacingAfter(30f);
            document.add(headingParaAccountNumber);

            PdfPTable tableHeadingFour = new PdfPTable(5);
            tableHeadingFour.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeadingFour.addCell("EMINo");
            tableHeadingFour.addCell("EMI Amnt.");
            tableHeadingFour.addCell("Payment Date");
            tableHeadingFour.addCell("Due Date");
            tableHeadingFour.addCell("Due Amnt.");

            document.add(tableHeadingFour);

            for (SetGetLoanEMI setGetLoanEMI : arrayList) {
                emiNo = setGetLoanEMI.getEmiNo();
                emiAmount = setGetLoanEMI.getEmiAmount();
                paymentDate = setGetLoanEMI.getPaymentDate();
                emiDueDate = setGetLoanEMI.getEmiDueDate();
                currentBalance = setGetLoanEMI.getCurrentBalance();

                PdfPTable tableRows = new PdfPTable(5);
                tableRows.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableRows.addCell(emiNo);
                tableRows.addCell(emiAmount);
                tableRows.addCell(paymentDate);
                tableRows.addCell(emiDueDate);
                tableRows.addCell(currentBalance);
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

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ArrangerLoanStatementAndPrint.this)
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
                Intent intent = new Intent(ArrangerLoanStatementAndPrint.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        super.onBackPressed();
        Intent intent = new Intent(ArrangerLoanStatementAndPrint.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
