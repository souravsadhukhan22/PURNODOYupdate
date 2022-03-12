package com.geniustechnoindia.purnodaynidhi.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
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

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.adapters.AdapterSavingsAccountStatement;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.model.SavingsStatementSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AgentSavingsStatementActivity extends AppCompatActivity implements View.OnClickListener {

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private EditText mEt_enterAccountCode;
    private Button mBtn_search;

    private TextView mTv_name;
    private TextView mTv_memberCode;

    private TextView mTv_currentBalance;

    private RecyclerView mRv_savingsStement;
    private ArrayList<SavingsStatementSetGet> arrayList;

    private Button mBtn_saveAsPdf;

    private LinearLayout mLl_mainRoot;

    private double totalDeposit = 0.0;
    private double totalWithdrawl = 0.0;
    private double currentBalance = 0.0;

    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";

    // RecyclerView
    private AdapterSavingsAccountStatement adapterSavingsAccountStatement;

    private static final int REQUEST_ID_EXTERNAL_STORAGE = 157;

    private EditText mEt_policyOrName;
    private Button mBtn_policyManualSearch;
    private Spinner mSp_policyNameCodeList;
    private ArrayList<String> arrayList_policyCode =new ArrayList<>();
    private ArrayList<String> arrayList_policyCodeName =new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_statement_and_print_activity);

        setViewReferences();
        bindEventHandlers();

        // toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_toolbarTitle.setText("Savings Statement");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        mRv_savingsStement.setLayoutManager(linearLayoutManager);

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
                        tag="SBCODE";
                    }
                    getPolicyCodeName(APILinks.GEt_SB_BY_SB_NAME+mEt_policyOrName.getText().toString()+"&tag="+tag);

                }else{
                    Toast.makeText(AgentSavingsStatementActivity.this, "enter search value", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSp_policyNameCodeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    mEt_enterAccountCode.setText(arrayList_policyCode.get(i));
                    mBtn_search.performClick();
                    mBtn_search.setClickable(true);
                    totalDeposit = 0.0;
                    totalWithdrawl = 0.0;
                    final ProgressDialog progressDialog = DialogUtils.showProgressDialog(AgentSavingsStatementActivity.this,"Please wait...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSavingsAccountStatement(arrayList_policyCode.get(i));
                            getSavingsDetails(arrayList_policyCode.get(i));
                            progressDialog.dismiss();
                        }
                    },3000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }




    private void getPolicyCodeName(String url){
        arrayList_policyCode.clear();arrayList_policyCodeName.clear();
        arrayList_policyCode.add("");arrayList_policyCodeName.add("");
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject=response.getJSONObject(i);
                            arrayList_policyCode.add(jsonObject.getString("AccountCode"));
                            arrayList_policyCodeName.add(jsonObject.getString("AccountCode")+"-"+jsonObject.getString("FirstApplicantName"));
                        }
                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(AgentSavingsStatementActivity.this,R.layout.spinner_hint, arrayList_policyCodeName);
                        mSp_policyNameCodeList.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(AgentSavingsStatementActivity.this, "NoData", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    String n = e.getMessage();
                }
            }
        });
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mEt_enterAccountCode = findViewById(R.id.et_activity_print_savings_statement_activity_enter_account_number);
        mBtn_search = findViewById(R.id.btn_activity_print_savings_statement_activity_search);

        mTv_currentBalance = findViewById(R.id.tv_activity_print_savings_statement_activity);

        mTv_name = findViewById(R.id.tv_activity_print_savings_statement_activity_name);
        mTv_memberCode = findViewById(R.id.tv_activity_print_savings_statement_activity_member_code);

        mBtn_saveAsPdf = findViewById(R.id.btn_activity_print_savings_statement_activity);

        mLl_mainRoot = findViewById(R.id.main_root);

        mRv_savingsStement = findViewById(R.id.rv_activity_print_savings_statement_activity);

        mSp_policyNameCodeList = findViewById(R.id.sp_activity_renewal_collection_policy_list);
        mEt_policyOrName=findViewById(R.id.et_activity_renewal_collection_polcode_name);
        mBtn_policyManualSearch=findViewById(R.id.btn_activity_renewal_collection_manual_search);
    }

    private void bindEventHandlers() {
        mBtn_search.setOnClickListener(this);
        mBtn_saveAsPdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_search) {
           /* if (mEt_enterAccountCode.getText().toString().trim().length() > 0) {
                totalDeposit = 0.0;
                totalWithdrawl = 0.0;
                final ProgressDialog progressDialog = DialogUtils.showProgressDialog(AgentSavingsStatementActivity.this,"Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSavingsAccountStatement(mEt_enterAccountCode.getText().toString());
                        getSavingsDetails(mEt_enterAccountCode.getText().toString());
                        progressDialog.dismiss();
                    }
                },3000);
            } else {
                mEt_enterAccountCode.setError("Enter account no.");
                mEt_enterAccountCode.requestFocus();
            }*/
        } else if (v == mBtn_saveAsPdf) {
            if (arrayList != null) {
                /*IntentPrint(getStringForPrint());*/
                if (checkAndRequestPermissions()) {
                    saveDataAsPdf();
                }
            } else {
                Toast.makeText(this, "No data to print", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDataAsPdf() {
        List list = new List();
        Document document = new Document();
        long time = System.currentTimeMillis();
        String fileName = "SavingsMiniStatement_" + mEt_enterAccountCode.getText().toString() + "_" + time;
        String path = Environment.getExternalStorageDirectory() + "/A/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path + fileName + ".pdf"));
            document.open();
            String tDate = "";
            String mode = "";
            String deposit = "";
            String withdraw = "";
            String row = "";

            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph headingPara = new Paragraph(getString(R.string.App_Full_Name), font);
            headingPara.setAlignment(Element.ALIGN_CENTER);
            headingPara.setSpacingAfter(20f);
            document.add(headingPara);

            Font fontMiniStatement = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph headingParaAccount = new Paragraph("Savings Account Mini Statement", fontMiniStatement);
            headingParaAccount.setAlignment(Element.ALIGN_CENTER);
            headingParaAccount.setSpacingAfter(10f);
            document.add(headingParaAccount);

            Font fontAccountNo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph headingParaAccountNumber = new Paragraph("Account no - " + mEt_enterAccountCode.getText().toString(), fontAccountNo);
            headingParaAccountNumber.setAlignment(Element.ALIGN_CENTER);
            headingParaAccountNumber.setSpacingAfter(30f);
            document.add(headingParaAccountNumber);

            PdfPTable tableHeadingFour = new PdfPTable(4);
            tableHeadingFour.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeadingFour.addCell("TDate");
            tableHeadingFour.addCell("Mode");
            tableHeadingFour.addCell("Deposit");
            tableHeadingFour.addCell("Withdraw");

            document.add(tableHeadingFour);

            for (SavingsStatementSetGet savingsStatementSetGet : arrayList) {
                tDate = savingsStatementSetGet.gettDate();
                mode = savingsStatementSetGet.getPaymode();
                deposit = savingsStatementSetGet.getDepositAmount();
                withdraw = savingsStatementSetGet.getWithdrawlAmount();
                row = tDate + "  |  " + mode + "  |  " + deposit + "  |  " + withdraw;


                PdfPTable tableRows = new PdfPTable(4);
                tableRows.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableRows.addCell(tDate);
                tableRows.addCell(mode);
                tableRows.addCell(deposit);
                tableRows.addCell(withdraw);
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

    private void getSavingsDetails(String accountCode) {
        mTv_name.setText("");
        mTv_memberCode.setText("");
        Connection cn = new SqlManager().getSQLConnection();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_GetSavingsAccountSummaryViewFromAgent(?)}");
                //smt.setString("@aCode", GlobalStore.GlobalValue.getUserName());
                smt.setString("@SBAccount", accountCode);
                smt.execute();
                ResultSet rs = smt.getResultSet();
                while (rs.next()) {
                    mTv_memberCode.setText(rs.getString("FirstApplicantMemberCode"));
                    mTv_name.setText(rs.getString("FirstApplicantName"));
                    rs.getString("AccountOpeningDate");
                    rs.getString("AccountAccessType");
                    rs.getString("CurrantBalance");
                }
            } else {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Network error.", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<SavingsStatementSetGet> getSavingsAccountStatement(String savingsAccountCode) {
        Connection cn = new SqlManager().getSQLConnection();
        arrayList = new ArrayList<SavingsStatementSetGet>();
        SavingsStatementSetGet savingsStatementSetGet = null;
        CallableStatement smt = null;

        try {
            if (cn != null) {
                smt = cn.prepareCall("{call ADROID_GetSavingsAccountMiniStatement(?)}");
                smt.setString("@SBAccount", savingsAccountCode);
                smt.execute();
                //ResultSet rs=smt.getResultSet();
                ResultSet rs = smt.executeQuery();

                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        savingsStatementSetGet = new SavingsStatementSetGet();
                        savingsStatementSetGet.settDate(rs.getString("TDate"));
                        savingsStatementSetGet.setDepositAmount(rs.getString("DepositAmount"));
                        totalDeposit += Double.parseDouble(rs.getString("DepositAmount"));
                        savingsStatementSetGet.setWithdrawlAmount(rs.getString("WithdrawlAmount"));
                        totalWithdrawl += Double.parseDouble(rs.getString("WithdrawlAmount"));
                        savingsStatementSetGet.setPaymode(rs.getString("PayMode"));
                        //savingsStatementSetGet.setBalance(rs.getString("Balance"));
                        arrayList.add(savingsStatementSetGet);
                    }

                    currentBalance = (totalDeposit - totalWithdrawl);


                    adapterSavingsAccountStatement = new AdapterSavingsAccountStatement(AgentSavingsStatementActivity.this, arrayList);
                    mRv_savingsStement.setAdapter(adapterSavingsAccountStatement);

                    mTv_currentBalance.setText(currentBalance + "");

                    mLl_mainRoot.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Statement not found", Toast.LENGTH_LONG).show();
                    mLl_mainRoot.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }

    private String getStringForPrint() {
        String resustForPrint = "";
        String companyName = getString(R.string.App_Full_Name);
        String memberCode = mTv_memberCode.getText().toString();
        String applicantName = mTv_name.getText().toString();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String dd = df.format(c);

        resustForPrint += " " + companyName + "\n\n";
        resustForPrint += "Member Code : " + memberCode + "\n";
        resustForPrint += "Applicant Name : " + applicantName + "\n\n";

        resustForPrint += "- Last 5 transaction -\n\n";

        resustForPrint += "TDate | Deposit | Withdraw\n\n";

        for (int i = 0; i < arrayList.size() - 5; i++) {        // Last 5 transaction
            resustForPrint += arrayList.get(i).gettDate() + "|" + arrayList.get(i).getDepositAmount() + "|" + arrayList.get(i).getWithdrawlAmount() + "\n\n";
        }
        return resustForPrint;
    }

    public void IntentPrint(String txtvalue) {
        byte[] buffer = txtvalue.getBytes();
        byte[] PrintHeader = {(byte) 0xAA, 0x55, 2, 0};
        PrintHeader[3] = (byte) buffer.length;
        InitPrinter();
        if (PrintHeader.length > 128) {
            value += "\nValue is more than 128 size\n";
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
        } else {
            try {

                outputStream.write(txtvalue.getBytes());
                outputStream.close();
                socket.close();
            } catch (Exception ex) {
                value += ex.toString() + "\n" + "Excep IntentPrint \n";
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void InitPrinter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            String s = "";
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    s = device.getName();
                    if (device.getName().equals("RPP02N")) //Note, you will need to change this to match the name of your device
                    {
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            } else {
                value += "No Devices found";
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception ex) {
            value += ex.toString() + "\n" + " InitPrinter \n";
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
        }
    }

    public void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("e", data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Check storage permission
     */
    private boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(AgentSavingsStatementActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        java.util.List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AgentSavingsStatementActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_EXTERNAL_STORAGE);
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(AgentSavingsStatementActivity.this,
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
                            Toast.makeText(AgentSavingsStatementActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AgentSavingsStatementActivity.this)
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
                Intent intent = new Intent(AgentSavingsStatementActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class);
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
        startActivity(new Intent(AgentSavingsStatementActivity.this, com.geniustechnoindia.purnodaynidhi.MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
