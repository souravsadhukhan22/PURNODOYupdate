package com.geniustechnoindia.purnodaynidhi.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.geniustechnoindia.purnodaynidhi.others.SelectedAccount;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

public class MemberSBStatementPDFActivity extends AppCompatActivity implements View.OnClickListener {

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

    private Spinner mSp_sbList;
    private ArrayList<String> arrayList_sbCodes = new ArrayList<>();
    private ArrayList<String> arrayList_sbCodesName = new ArrayList<>();

    private DatePickerDialog datePickerDialogFromDate, datePickerDialogToDate;
    private Calendar calendar;
    private int curr_year, curr_month, curr_day;
    private int intFromDate = 0, intToDate = 0;
    private Button mBtn_fromDate, mBtn_toDate, mBtn_submitDate;
    private String mStr_selectedSBCode = "";

    private Spinner spSBAccList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_s_b_statement_p_d_f);

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

        //getSBCodes(APILinks.GET_SB_LIST_BY_ARR+ GlobalStore.GlobalValue.getUserName());

        mStr_selectedSBCode = SelectedAccount.savingsCode;

        mSp_sbList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mStr_selectedSBCode = arrayList_sbCodes.get(position);
                    //getSavingsAccountStatement(arrayList_sbCodes.get(position));
                    //getSavingsDetails(arrayList_sbCodes.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        mSp_sbList = findViewById(R.id.sp_activity_main_sb_acc_details_sb_list);

        mBtn_fromDate = findViewById(R.id.btn_activity_arr_sb_statement_from_date);
        mBtn_toDate = findViewById(R.id.btn_activity_arr_sb_statement_to_date);
        mBtn_submitDate = findViewById(R.id.btn_activity_arr_sb_statement_submit_date);
    }

    private void bindEventHandlers() {
        mBtn_search.setOnClickListener(this);
        mBtn_saveAsPdf.setOnClickListener(this);
        mBtn_fromDate.setOnClickListener(this);
        mBtn_toDate.setOnClickListener(this);
        mBtn_submitDate.setOnClickListener(this);
    }

    private void getSBCodes(String url) {
        arrayList_sbCodes.clear();
        arrayList_sbCodesName.clear();
        arrayList_sbCodes.add("");
        arrayList_sbCodesName.add("");

        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            arrayList_sbCodes.add(jsonObject.getString("AccountCode"));
                            arrayList_sbCodesName.add(jsonObject.getString("AccountCode") + "   " + jsonObject.getString("FirstApplicantName"));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MemberSBStatementPDFActivity.this, R.layout.spinner_hint_select, arrayList_sbCodesName);
                        mSp_sbList.setAdapter(arrayAdapter);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn_search) {
            if (mEt_enterAccountCode.getText().toString().trim().length() > 0) {
                totalDeposit = 0.0;
                totalWithdrawl = 0.0;
                final ProgressDialog progressDialog = DialogUtils.showProgressDialog(MemberSBStatementPDFActivity.this, "Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSavingsAccountStatement(mEt_enterAccountCode.getText().toString());
                        getSavingsDetails(mEt_enterAccountCode.getText().toString());
                        progressDialog.dismiss();
                    }
                }, 3000);
            } else {
                mEt_enterAccountCode.setError("Enter account no.");
                mEt_enterAccountCode.requestFocus();
            }
        } else if (v == mBtn_saveAsPdf) {
            if (arrayList != null) {
                /*IntentPrint(getStringForPrint());*/
                if(Build.VERSION.SDK_INT <= 28){
                    if (checkAndRequestPermissions()) {
                        saveDataAsPdf();
                    }
                } else {
                    saveDataAsPdf();
                }
            } else {
                Toast.makeText(this, "No data to print", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == mBtn_fromDate) {
            calendar = Calendar.getInstance();
            curr_year = calendar.get(Calendar.YEAR);
            curr_month = calendar.get(Calendar.MONTH);
            curr_day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialogFromDate = new DatePickerDialog(MemberSBStatementPDFActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mBtn_fromDate.setText(String.valueOf(dayOfMonth) + ":" + String.valueOf(month) + ":" + String.valueOf(year));
                    intFromDate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, curr_year, curr_month, curr_day);
            datePickerDialogFromDate.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialogFromDate.show();
        }

        if (v == mBtn_toDate) {
            calendar = Calendar.getInstance();
            curr_year = calendar.get(Calendar.YEAR);
            curr_month = calendar.get(Calendar.MONTH);
            curr_day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialogToDate = new DatePickerDialog(MemberSBStatementPDFActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    mBtn_toDate.setText(String.valueOf(dayOfMonth) + ":" + String.valueOf(month) + ":" + String.valueOf(year));
                    intToDate = Integer.parseInt(Integer.toString(year) + String.format("%02d", month) + String.format("%02d", dayOfMonth));
                }
            }, curr_year, curr_month, curr_day);
            datePickerDialogToDate.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialogToDate.show();
        }
        if (v == mBtn_submitDate) {
            if (mStr_selectedSBCode.length() > 0) {
                if (intToDate != 0 && intFromDate != 0) {
                    getSavingsAccountStatement(mStr_selectedSBCode);
                    getSavingsDetails(mStr_selectedSBCode);
                } else {
                    Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Select SB A/c", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void saveDataAsPdf() {
        Document document = new Document();

        long time = System.currentTimeMillis();
        String fileName = "SBMiniStatement_" + mStr_selectedSBCode + "_" + time;

        Uri uri = null;
        ContentResolver resolver = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }

        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dir = new File(uri.getPath());
        } else {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PdfWriter.getInstance(document, resolver.openOutputStream(uri));
            } else {
                PdfWriter.getInstance(document, new FileOutputStream(dir.getPath() + "/" + fileName + ".pdf"));
            }
            document.open();
            String tDate = "";
            String mode = "";
            String deposit = "";
            String withdraw = "";
            String row = "";


            /////////////////////////////////////////    logo

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pnl_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Image image = Image.getInstance(img);
            image.setAlignment(Element.ALIGN_CENTER);
            //image.setAbsolutePosition(100, 100);
            image.scalePercent(10f);
            document.add(image);

///////////////////////////////////////////////////////////////////


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
            Paragraph headingParaAccountNumber;
            // TODO this section is commented, because it depends on client whether he wants to show virtual account instead of savings account
            /*if(!SelectedAccount.selectedVirtualAccount.equals("")){
                headingParaAccountNumber = new Paragraph("Account no - " + SelectedAccount.selectedVirtualAccount, fontAccountNo);
            } else {*/
                headingParaAccountNumber = new Paragraph("Account no - " + mStr_selectedSBCode, fontAccountNo);
            //}

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
            documentSavedDialog("Document saved successfully");
            //Toast.makeText(this, "Document successfully saved", Toast.LENGTH_LONG).show();
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
                CallableStatement smt = cn.prepareCall("{call ADROID_GetSavingsAccountSummaryViewFromAgentNew(?)}");
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
                smt = cn.prepareCall("{call ADROID_GetSavingsAccountMiniStatementNew(?,?,?)}");
                smt.setString("@SBAccount", savingsAccountCode);
                smt.setString("@fromDate", String.valueOf(intFromDate));
                smt.setString("@toDate", String.valueOf(intToDate));
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


                    adapterSavingsAccountStatement = new AdapterSavingsAccountStatement(MemberSBStatementPDFActivity.this, arrayList);
                    mRv_savingsStement.setAdapter(adapterSavingsAccountStatement);

                    mTv_currentBalance.setText(currentBalance + "");

                    mLl_mainRoot.setVisibility(View.VISIBLE);
                } else {
                    mLl_mainRoot.setVisibility(View.GONE);
                    Toast.makeText(this, "No Statement Found", Toast.LENGTH_SHORT).show();
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
        int permissionStorage = ContextCompat.checkSelfPermission(MemberSBStatementPDFActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        java.util.List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MemberSBStatementPDFActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }


    private void documentSavedDialog(String message) {
        new AlertDialog.Builder(this).setTitle(message).setPositiveButton("OK", (dialog, which) -> dialog.cancel()).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_EXTERNAL_STORAGE: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        saveDataAsPdf();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MemberSBStatementPDFActivity.this,
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
                            Toast.makeText(MemberSBStatementPDFActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MemberSBStatementPDFActivity.this)
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
                Intent intent = new Intent(MemberSBStatementPDFActivity.this, MemberSavingsAccountActivity.class);
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
        startActivity(new Intent(MemberSBStatementPDFActivity.this, MemberSavingsAccountActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
