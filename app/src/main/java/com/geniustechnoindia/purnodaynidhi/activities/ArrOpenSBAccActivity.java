package com.geniustechnoindia.purnodaynidhi.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.geniustechnoindia.purnodaynidhi.MainActivity;
import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.bean.ErrorData;
import com.geniustechnoindia.purnodaynidhi.bl.DialogUtils;
import com.geniustechnoindia.purnodaynidhi.dl.GeneralFnc;
import com.geniustechnoindia.purnodaynidhi.model.SavingsSchemeMasterSetGet;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArrOpenSBAccActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ArrOpenSBAccActivity";

    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private TextView mTv_firstApplicantDateOfBirth;
    private TextView mTv_secondApplicantDateOfBirth;
    private TextView mTv_openAccessType;
    private TextInputEditText mTie_firstApplicantMemberCode;
    private TextInputEditText mTie_firstApplicantName;
    private TextInputEditText mTie_firstApplicantRelationWithGuardian;
    private TextInputEditText mTie_firstApplicantGuardianName;
    private TextInputEditText mTie_firstApplicantRelation;
    private TextInputEditText mTie_firstApplicantMobileNumber;
    private TextInputEditText mTie_firstApplicantPan;
    private TextInputEditText mTie_firstApplicantAddress;
    private TextInputEditText mTie_firstApplicantPin;
    private LinearLayout mLl_firstApplicantRoot;
    private LinearLayout mLl_secondApplicantRoot;
    private TextView mTvSecondApplicantState;

    private TextView mTv_selectImageApplicantOne;
    private TextView mTv_selectImageApplicantTwo;

    private Button mBtnShowSecondApplicant;

    private TextView mTv_state;

    private CircleImageView mCiv_firstSelectedImage;
    private CircleImageView mCiv_secondSelectedImage;

    private EditText mEt_savingsReferenceNumber;
    private CircleImageView mCiv_savingsAccountReferenceImage;

    private TextView mTv_addNominee;

    private Spinner mSp_firstApplicantState;

    private String mStr_formatedMemberDob = "";


    private TextInputEditText mTie_secondApplicantMemberCode;
    private TextInputEditText mTie_secondApplicantName;
    private TextInputEditText mTie_secondApplicantGuardianName;
    private TextInputEditText mTie_secondApplicantGuardianRelation;
    private TextView mTv_dateOfBirth;
    private TextInputEditText mTie_secondApplicantPhoneNumber;
    private TextInputEditText mTie_secondApplicantPan;
    private TextInputEditText mTie_secondApplicantAddress;
    private Spinner mSp_secondApplicantState;
    private TextInputEditText mTie_secondApplicantPin;

    private LinearLayout mLl_bottomRoot;
    private Button mBtn_submit;

    private EditText mEt_introducerAccountCode;

    // Datepicker
    private Calendar mCalendar;
    private int currentDay, currentMonth, currentYear;

    // popup menu
    private PopupMenu mPopupMenuAccessType;

    private Animation slideDown;

    // vars
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;

    protected boolean nomineeAdded = false;

    private static String applicant = "";

    protected String selectedState = "";                            // **
    protected String selectedNomineeIDProofName = "";

    private String accountAccessType = "";                   // **
    private final String ACCOUNT_TYPE = "Savings";           // **
    private final String OFFICE_ID = "00001";                // **
    private String accountOpeningDate = "";                  // **
    private String dateOfEntry = "";                         // **

    private String firstApplicantDob = "";                   // **
    private String firstApplicantState = "";                 // **
    private String firstApplicantAge = "1";                  // **


    private String secondApplicantDob = "";                  // **
    private String secondApplicantState = "";                // **
    private String secondApplicantAge = "1";                 // **


    private String nomineeName = "";                         // **
    private String nomineeDateOfBirth = "";                  // **
    private String nomineeGuardian = "";                     // **
    private String nomineeGuardianRelation = "";             // **
    private String nomineeAddress = "";                      // **
    private String nomineeState = "";                        // **
    private String nomineePin = "";                          // **
    private String nomineeIdNo = "";                          // **
    private String nomineePhone = "";                        // **

    private final String REG_AMNT = "1000";                  // **
    private final String PAY_MODE = "1001";                  // **

    private String encodedImageStringReferenceImage = "";    // **

    private String chequeDate = "";

    private String nomineeMemberCode = "";

    private CheckBox mCb_smsSub;
    private TextView mTv_smsSubMsg;
    private Boolean mBool_smsSubStatus = false;
    private Button mBtn_submitMCode;

    //private ArrayList<SavingsSchemeMasterSetGet> arrSavingsScheme;
    private Spinner ssSbScheme;
    private ArrayAdapter<SavingsSchemeMasterSetGet> arrayAdapterScheme;
    private String selectedTypeID = "";


    // Image selection
    private LinearLayout mLlSelectNomineeImage, mLlSelectNomineeSignature, llNomineeImageRoot;
    private ImageView mIvNomineeImage, mIvNomineeSignature;
    private String imageType = "";
    private String encodedNomineeImageString = "";
    private String encodedNomineeSignatureString = "";

    private byte[] dataNomineeImage = null;
    private byte[] dataNomineeSignature = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arr_open_s_b_acc);


        setViewReferences();
        bindEventHandlers();

        Log.d(TAG, "onCreate: ");

        // Toolbar
        setSupportActionBar(mToolbar);
        mTv_toolbarTitle.setText("Open Savings Account");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*arrSavingsScheme = new ArrayList<>();

        arrayAdapterScheme = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrSavingsScheme);
        ssSbScheme.setAdapter(arrayAdapterScheme);*/

        /*getSavingsSchemeMaster();

        ssSbScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrSavingsScheme.size() > 0) {
                    selectedTypeID = arrSavingsScheme.get(position).getTypeName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        mCb_smsSub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBool_smsSubStatus = true;
                    mTv_smsSubMsg.setVisibility(View.VISIBLE);
                } else {
                    mBool_smsSubStatus = false;
                    mTv_smsSubMsg.setVisibility(View.GONE);
                }
            }
        });

        // Calendar
        mCalendar = Calendar.getInstance();
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = mCalendar.get(Calendar.MONTH);
        currentYear = mCalendar.get(Calendar.YEAR);

        slideDown = AnimationUtils.loadAnimation(ArrOpenSBAccActivity.this, R.anim.slide_down);

        // *** Popup menu Account access type ***
        mPopupMenuAccessType = new PopupMenu(this, mTv_openAccessType);                               // The second parameter - The view on which the pop up will show
        mPopupMenuAccessType.getMenu().add("Single");
        mPopupMenuAccessType.getMenu().add("Joint");

        mPopupMenuAccessType.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mTv_openAccessType.setText(item.getTitle());
                // TODO Show 2nd applicant details if access type is joint
                if (item.getTitle().equals("Single")) {
                    // TODO Hide 2nd applicant form
                    mLl_firstApplicantRoot.startAnimation(slideDown);
                    mLl_firstApplicantRoot.setVisibility(View.VISIBLE);
                    mLl_secondApplicantRoot.setVisibility(View.GONE);
                    accountAccessType = "Single";
                    mLl_bottomRoot.setVisibility(View.VISIBLE);
                } else if (item.getTitle().equals("Joint")) {
                    // TODO Show 2nd applicant form
                    mLl_secondApplicantRoot.startAnimation(slideDown);
                    mLl_firstApplicantRoot.setVisibility(View.VISIBLE);
                    mLl_secondApplicantRoot.setVisibility(View.VISIBLE);
                    accountAccessType = "Joint";
                    mLl_bottomRoot.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });


        /** Current date */
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        dateOfEntry = fDate.replaceAll("\\D", "");
        accountOpeningDate = dateOfEntry;
        chequeDate = dateOfEntry;

        List<String> stateList = new ArrayList<String>();
        stateList.add("Select state");
        stateList.add("Andhra Pradesh");
        stateList.add("Arunachal Pradesh");
        stateList.add("Assam");
        stateList.add("Bihar");
        stateList.add("Chhattisgarh");
        stateList.add("Goa");
        stateList.add("Gujarat");
        stateList.add("Haryana");
        stateList.add("Himachal Pradesh");
        stateList.add("Jammu and Kashmir");
        stateList.add("Jharkhand");
        stateList.add("Karnataka");
        stateList.add("Kerala");
        stateList.add("Madhya Pradesh");
        stateList.add("Maharashtra");
        stateList.add("Manipur");
        stateList.add("Meghalaya");
        stateList.add("Mizoram");
        stateList.add("Nagaland");
        stateList.add("Odisha");
        stateList.add("Punjab");
        stateList.add("Rajasthan");
        stateList.add("Sikkim");
        stateList.add("Tamil Nadu");
        stateList.add("Telangana");
        stateList.add("Tripura");
        stateList.add("Uttar Pradesh");
        stateList.add("Uttarakhand");
        stateList.add("West Bengal");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_firstApplicantState.setAdapter(dataAdapter);
        mSp_secondApplicantState.setAdapter(dataAdapter);

        // First applicant state
        mSp_firstApplicantState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    firstApplicantState = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Second applicant state
        mSp_secondApplicantState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    secondApplicantState = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*private void getSavingsSchemeMaster() {
        new GetDataParserArray(this, APILinks.GET_SAVINGS_SCHEME_MASTER, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response.getJSONObject(i);
                            SavingsSchemeMasterSetGet smSetGet = new SavingsSchemeMasterSetGet();
                            smSetGet.setTypeCode(jsonObject.getString("TypeCode"));
                            smSetGet.setTypeName(jsonObject.getString("TypeName"));
                            smSetGet.setMinimumOpeningBalance(jsonObject.getDouble("MinimumOpeningBalance"));
                            smSetGet.setRoi(jsonObject.getDouble("ROI"));
                            smSetGet.setIsActive(jsonObject.getBoolean("isActive"));
                            smSetGet.setPrefixName(jsonObject.getString("PrefixName"));
                            smSetGet.setFromLCode(jsonObject.getString("FromLCode"));
                            smSetGet.setIntLCode(jsonObject.getString("IntLCode"));
                            smSetGet.setSetMinimumBalance(jsonObject.getBoolean("SetMinimumBalance"));
                            smSetGet.setAccountClosingDuration(jsonObject.getInt("AccountClosingDuration"));
                            arrSavingsScheme.add(smSetGet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    arrayAdapterScheme.notifyDataSetChanged();
                } else {
                    Toast.makeText(ArrOpenSBAccActivity.this, "Savings scheme not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public void openBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ArrOpenSBAccActivity.this);
        bottomSheetDialog.setContentView(R.layout.custom_bottom_sheet_dialog1);

        ImageView mIv_camera = bottomSheetDialog.findViewById(R.id.profile_bottom_sheet_select_image);
        mIv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    //TODO: Permission granted
                    takeImage();
                }
            }
        });
        bottomSheetDialog.show();
    }

    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(ArrOpenSBAccActivity.this,
                Manifest.permission.CAMERA);
        int permissionStorage = 0;
        if (Build.VERSION.SDK_INT <= 28) {
            permissionStorage = ContextCompat.checkSelfPermission(ArrOpenSBAccActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (Build.VERSION.SDK_INT <= 28) {
            if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ArrOpenSBAccActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    /**
     * Trigger image selection for photo
     **/
    public void takeImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (Build.VERSION.SDK_INT <= 28 && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        takeImage();
                    } else if (Build.VERSION.SDK_INT > 28 && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        takeImage();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ArrOpenSBAccActivity.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(ArrOpenSBAccActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Both Camera and Storage Permissions are required for this app",
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
                            Toast.makeText(ArrOpenSBAccActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ArrOpenSBAccActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //if(applicant.equals("first")){
                Uri resultUri = result.getUriContent();
                mTv_selectImageApplicantOne.setText("Image selected");
                InputStream imageStream = null;
                try {
                    imageStream = this.getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                if (imageType.equals("NOMINEE_IMAGE")) {
                    mIvNomineeImage.setImageBitmap(yourSelectedImage);
                    encodedNomineeImageString = encodeToBase64(compressImage(yourSelectedImage));
                    dataNomineeImage = Base64.decode(encodedNomineeImageString, Base64.DEFAULT);
                    Log.d("Test", "Encoded image: " + encodedNomineeImageString);
                } else if (imageType.equals("NOMINEE_SIGNATURE")) {
                    mIvNomineeSignature.setImageBitmap(yourSelectedImage);
                    encodedNomineeSignatureString = encodeToBase64(compressImage(yourSelectedImage));
                    dataNomineeSignature = Base64.decode(encodedNomineeSignatureString, Base64.DEFAULT);
                    Log.d("Test", "Encoded signature: " + encodedNomineeSignatureString);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Failed to take image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap compressImage(Bitmap image) {
        Bitmap decoded = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 55, out);
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            return decoded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decoded;
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(ArrOpenSBAccActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // Popup menu
        mTv_openAccessType = findViewById(R.id.tv_activity_open_savings_account_access_type);

        mLl_firstApplicantRoot = findViewById(R.id.ll_activity_open_savings_account_first_applicant_root);
        mTie_firstApplicantMemberCode = findViewById(R.id.tie_activity_open_savings_account_first_applicant_membership_code);
        mTie_firstApplicantName = findViewById(R.id.tie_activity_open_savings_account_first_applicant_name);
        mTie_firstApplicantRelation = findViewById(R.id.tie_activity_open_savings_account_first_applicant_relation);
        mTv_firstApplicantDateOfBirth = findViewById(R.id.tv_activity_open_savings_account_first_applicant_date_of_birth);
        mSp_firstApplicantState = findViewById(R.id.sp_activity_open_savings_account_state);
        mTie_firstApplicantGuardianName = findViewById(R.id.tie_activity_open_savings_account_first_applicant_guardian_name);
        mTie_firstApplicantRelationWithGuardian = findViewById(R.id.tie_activity_open_savings_account_first_applicant_relation);
        mTie_firstApplicantMobileNumber = findViewById(R.id.tie_activity_open_savings_account_first_account_phone_number);
        mTie_firstApplicantPan = findViewById(R.id.tie_activity_open_savings_account_first_applicant_pan);
        mTie_firstApplicantAddress = findViewById(R.id.tie_activity_open_savings_account_first_applicant_address);
        mTie_firstApplicantPin = findViewById(R.id.tie_activity_open_savings_account_first_applicant_pin);


        mLl_secondApplicantRoot = findViewById(R.id.ll_activity_open_savings_account_second_applicant_root);
        mTie_secondApplicantMemberCode = findViewById(R.id.tie_activity_open_savings_account_second_applicant_membership_code);
        mTie_secondApplicantName = findViewById(R.id.tie_activity_login_second_applicant_user_name);
        mTie_secondApplicantGuardianName = findViewById(R.id.tie_activity_open_savings_account_second_applicant_guardian_name);
        mTie_secondApplicantGuardianRelation = findViewById(R.id.tie_activity_open_savings_account_second_applicant_relation);
        mTv_secondApplicantDateOfBirth = findViewById(R.id.tv_activity_open_savings_account_second_applicant_date_of_birth);
        mTie_secondApplicantPhoneNumber = findViewById(R.id.tie_activity_open_savings_account_second_applicant_phone_number);
        mTie_secondApplicantPan = findViewById(R.id.tie_activity_open_savings_account_second_applicant_pan);
        mTie_secondApplicantAddress = findViewById(R.id.tie_activity_open_savings_account_second_applicant_address);
        mTie_secondApplicantPin = findViewById(R.id.tie_activity_open_savings_account_second_applicant_pin);
        mSp_secondApplicantState = findViewById(R.id.sp_activity_open_savings_account_state_second_applicant);


        mEt_savingsReferenceNumber = findViewById(R.id.et_activity_open_savings_account_reference_number);
        mCiv_savingsAccountReferenceImage = findViewById(R.id.civ_activity_open_savings_account_reference_image);
        mTv_addNominee = findViewById(R.id.tv_activity_open_savings_account_add_nominee);

        mTv_selectImageApplicantOne = findViewById(R.id.tv_activity_open_savings_account_select_image_first_applicant);         // Not needed, view hidden in xml
        mTv_selectImageApplicantTwo = findViewById(R.id.tv_activity_open_savings_account_select_image_second_applicant);

        mEt_introducerAccountCode = findViewById(R.id.et_activity_open_savings_account_introducer_account_code);

        mBtn_submit = findViewById(R.id.btn_activity_open_savings_account_submit);
        mLl_bottomRoot = findViewById(R.id.ll_activity_open_savings_account_bottom_root);

        mCb_smsSub = findViewById(R.id.cb_activity_employee_open_sb_acc_sms);
        mTv_smsSubMsg = findViewById(R.id.tv_activity_employee_open_sb_acc_msg);
        mBtn_submitMCode = findViewById(R.id.btn_activity_emloyee_open_sb_acc_submit_memebr_code);
        mTv_state = findViewById(R.id.tv_activity_employee_open_savings_state);
        ssSbScheme = findViewById(R.id.llTypeId);
        mBtnShowSecondApplicant = findViewById(R.id.btn_activity_emloyee_open_sb_acc_submit_second_memebr_code);
        mTvSecondApplicantState = findViewById(R.id.tv_activity_employee_open_savings_second_applicant_state);

        mLlSelectNomineeImage = findViewById(R.id.ll_select_nominee_image);
        mLlSelectNomineeSignature = findViewById(R.id.ll_select_nominee_signature);

        mIvNomineeImage = findViewById(R.id.iv_nominee_image);
        mIvNomineeSignature = findViewById(R.id.iv_nominee_signature);
        llNomineeImageRoot = findViewById(R.id.llNomineeImageRoot);

    }

    private void bindEventHandlers() {
        mTv_firstApplicantDateOfBirth.setOnClickListener(this);
        mTv_openAccessType.setOnClickListener(this);
        //mTv_secondApplicantDateOfBirth.setOnClickListener(this);
        mTv_selectImageApplicantOne.setOnClickListener(this);
        mTv_selectImageApplicantTwo.setOnClickListener(this);
        mTv_addNominee.setOnClickListener(this);
        mBtn_submit.setOnClickListener(this);
        mBtn_submitMCode.setOnClickListener(this);

        mCiv_savingsAccountReferenceImage.setOnClickListener(this);
        mBtnShowSecondApplicant.setOnClickListener(this);

        mLlSelectNomineeImage.setOnClickListener(this);
        mLlSelectNomineeSignature.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv_firstApplicantDateOfBirth) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ArrOpenSBAccActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month += 1;
                    mTv_firstApplicantDateOfBirth.setText(dayOfMonth + "-" + month + "-" + year);
                    firstApplicantDob = Integer.toString(year) + String.format("%02d", (month)) + String.format("%02d", dayOfMonth);
                }
            }, currentYear, currentMonth, currentDay);
            mCalendar.set(currentYear, currentMonth, currentDay);

            datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
            datePickerDialog.show();
        } else if (v == mTv_openAccessType) {
            // TODO Open account access type
            mPopupMenuAccessType.show();
        } else if (v == mTv_secondApplicantDateOfBirth) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ArrOpenSBAccActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month += 1;
                    mTv_secondApplicantDateOfBirth.setText(dayOfMonth + "-" + month + "-" + year);
                    secondApplicantDob = Integer.toString(year) + String.format("%02d", (month)) + String.format("%02d", dayOfMonth);
                }
            }, currentYear, currentMonth, currentDay);
            mCalendar.set(currentYear, currentMonth, currentDay);

            datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
            datePickerDialog.show();
        } /*else if (v == mTv_selectImageApplicantOne) {
            // TODO Select image first applicant
            openBottomSheetDialog();
            applicant = "first";
        } else if(v == mTv_selectImageApplicantTwo){

            applicant = "second";
        }*/ else if (v == mCiv_savingsAccountReferenceImage) {
            //openBottomSheetDialog();
        } else if (v == mTv_addNominee) {
            openAddNomineeDialog();
        } else if (v == mBtn_submit) {
            if (mTie_firstApplicantMemberCode.getText().toString().trim().length() > 0) {
                if (mTie_firstApplicantName.getText().toString().trim().length() > 0) {
                    if (mTv_firstApplicantDateOfBirth.getText().toString().trim().length() > 0) {
                        if (mTie_firstApplicantMobileNumber.getText().toString().trim().length() > 0) {
                            if (mTie_firstApplicantAddress.getText().toString().trim().length() > 0) {
                                if (!firstApplicantState.equals("")) {
                                    if (mTie_firstApplicantPin.getText().toString().trim().length() > 0) {
                                        if (accountAccessType.equals("Joint")) {
                                            if (mTie_secondApplicantMemberCode.getText().toString().trim().length() > 0) {
                                                if (mTie_secondApplicantName.getText().toString().trim().length() > 0) {
                                                    if (mTv_secondApplicantDateOfBirth.getText().toString().trim().length() > 0) {
                                                        if (mTie_secondApplicantPhoneNumber.getText().toString().trim().length() > 0) {
                                                            if (mTie_secondApplicantAddress.getText().toString().trim().length() > 0) {
                                                                //if (secondApplicantState.equals("")) {
                                                                if (mTie_secondApplicantPin.getText().toString().trim().length() > 0) {
                                                                    if (mEt_introducerAccountCode.getText().toString().trim().length() > 0) {
                                                                        //if (mEt_savingsReferenceNumber.getText().toString().trim().length() > 0) {
                                                                        // TODO Continue with first and second applicant
                                                                        openSavingsAcc();
                                                                            /*} else {
                                                                                mEt_savingsReferenceNumber.setError("Enter reference number");
                                                                                mEt_savingsReferenceNumber.requestFocus();
                                                                            }*/
                                                                    } else {
                                                                        mEt_introducerAccountCode.setError("Enter introducer code");
                                                                        mEt_introducerAccountCode.requestFocus();
                                                                    }
                                                                } else {
                                                                    mTie_secondApplicantPin.setError("Enter pin code");
                                                                    mTie_secondApplicantPin.requestFocus();
                                                                }
                                                                /*} else {
                                                                    mSp_secondApplicantState.performClick();
                                                                    Toast.makeText(this, "Select state", Toast.LENGTH_SHORT).show();
                                                                }*/
                                                            } else {
                                                                mTie_secondApplicantAddress.setError("Enter address");
                                                                mTie_secondApplicantAddress.requestFocus();
                                                            }
                                                        } else {
                                                            mTie_secondApplicantPhoneNumber.setError("Enter phone number");
                                                            mTie_secondApplicantPhoneNumber.requestFocus();
                                                        }
                                                    } else {
                                                        mTv_secondApplicantDateOfBirth.performClick();
                                                        Toast.makeText(this, "Select Date of Birth", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    mTie_secondApplicantName.setError("Enter name");
                                                    mTie_secondApplicantName.requestFocus();
                                                }
                                            } else {
                                                mTie_secondApplicantMemberCode.setError("Enter member code");
                                                mTie_secondApplicantMemberCode.requestFocus();
                                            }
                                        } else
                                        if (mEt_introducerAccountCode.getText().toString().trim().length() > 0) {
                                            //if (mEt_savingsReferenceNumber.getText().toString().trim().length() > 0) {
                                            // TODO HERE continue with the first applicant only
                                            openSavingsAcc();
                                            /*} else {
                                                mEt_savingsReferenceNumber.setError("Enter reference number");
                                                mEt_savingsReferenceNumber.requestFocus();
                                            }*/
                                        } else {
                                            mEt_introducerAccountCode.setError("Enter introducer code");
                                            mEt_introducerAccountCode.requestFocus();
                                        }
                                    } else {
                                        mTie_firstApplicantPin.setError("Enter pin code");
                                        mTie_firstApplicantPin.requestFocus();
                                    }
                                } else {
                                    //mSp_firstApplicantState.performClick();
                                    Toast.makeText(this, "Select state", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mTie_firstApplicantAddress.setError("Enter address");
                                mTie_firstApplicantAddress.requestFocus();
                            }
                        } else {
                            mTie_firstApplicantMobileNumber.setError("Enter phone number");
                            mTie_firstApplicantMobileNumber.requestFocus();
                        }
                    } else {
                        mTv_firstApplicantDateOfBirth.performClick();
                        Toast.makeText(this, "Select Date of Birth", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mTie_firstApplicantName.setError("Enter name");
                    mTie_firstApplicantName.requestFocus();
                }
            } else {
                mTie_firstApplicantMemberCode.setError("Enter member code");
                mTie_firstApplicantMemberCode.requestFocus();
            }
        }

        if (v == mBtn_submitMCode) {
            new GetDataParserArray(this, APILinks.GET_MEMBER_DETAILS_BY_MCODE + mTie_firstApplicantMemberCode.getText().toString().trim(), true, new GetDataParserArray.OnGetResponseListener() {
                @Override
                public void onGetResponse(JSONArray response) {
                    try {
                        if (response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(0);
                            mTie_firstApplicantName.setText(jsonObject.getString("MemberName"));
                            mTie_firstApplicantGuardianName.setText(jsonObject.getString("Father"));
                            //mTv_firstApplicantDateOfBirth.setText(String.valueOf(jsonObject.getInt("MemberDOB")));
                            //SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                            /*SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String formatedDate = newFormat.format(String.valueOf(jsonObject.getInt("MemberDOB")));*/
                            //firstApplicantDob=String.valueOf(jsonObject.getInt("MemberDOBFormat"));
                            firstApplicantDob = String.valueOf(jsonObject.getInt("MemberDOB"));
                            mTv_firstApplicantDateOfBirth.setText(jsonObject.getString("FormattedDate"));
                            mTie_firstApplicantMobileNumber.setText(jsonObject.getString("Phone"));
                            mTie_firstApplicantPan.setText(jsonObject.getString("PANNo"));
                            mTie_firstApplicantAddress.setText(jsonObject.getString("Address"));
                            mTie_firstApplicantPin.setText(jsonObject.getString("Pincode"));
                            mTv_state.setText(jsonObject.getString("State"));
                            firstApplicantState = jsonObject.getString("State");
                        } else {
                            Toast.makeText(ArrOpenSBAccActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v == mBtnShowSecondApplicant) {
            new GetDataParserArray(this, APILinks.GET_MEMBER_DETAILS_BY_MCODE + mTie_secondApplicantMemberCode.getText().toString().trim(), true, new GetDataParserArray.OnGetResponseListener() {
                @Override
                public void onGetResponse(JSONArray response) {
                    try {
                        if (response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(0);
                            mTie_secondApplicantName.setText(jsonObject.getString("MemberName"));
                            mTie_secondApplicantGuardianName.setText(jsonObject.getString("Father"));
                            //mTv_secondApplicantDateOfBirth.setText(String.valueOf(jsonObject.getInt("MemberDOB")));
                            //SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                            /*SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String formatedDate = newFormat.format(jsonObject.getInt("MemberDOB"));*/
                            //firstApplicantDob=String.valueOf(jsonObject.getInt("MemberDOBFormat"));
                            secondApplicantDob = String.valueOf(jsonObject.getInt("MemberDOB"));
                            mTv_secondApplicantDateOfBirth.setText(jsonObject.getString("FormattedDate"));
                            mTie_secondApplicantPhoneNumber.setText(jsonObject.getString("Phone"));
                            mTie_secondApplicantPan.setText(jsonObject.getString("PANNo"));
                            mTie_secondApplicantAddress.setText(jsonObject.getString("Address"));
                            mTie_secondApplicantPin.setText(jsonObject.getString("Pincode"));
                            mTvSecondApplicantState.setText(jsonObject.getString("State"));
                            secondApplicantState = jsonObject.getString("State");
                        } else {
                            Toast.makeText(ArrOpenSBAccActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v == mLlSelectNomineeImage) {
            imageType = "NOMINEE_IMAGE";
            openBottomSheetDialog();
        } else if (v == mLlSelectNomineeSignature) {
            imageType = "NOMINEE_SIGNATURE";
            openBottomSheetDialog();
        }
    }


    private void openSavingsAcc() {
        final ProgressDialog dialog = DialogUtils.showProgressDialog(ArrOpenSBAccActivity.this, "Please wait...");
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            ErrorData err = submitSavingsAccount();
                            if (err.getErrorCode() > 0) {
                                // Error
                                Toast.makeText(ArrOpenSBAccActivity.this, "Error code - " + err.getErrorCode(), Toast.LENGTH_SHORT).show();
                            } else {
                                encodedNomineeImageString = "";
                                encodedNomineeSignatureString = "";
                                dataNomineeImage = null;
                                dataNomineeSignature = null;
                                nomineeAdded = false;
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ArrOpenSBAccActivity.this, ArrOpenSBAccActivity.class));
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                        // onLoginFailed();
                        dialog.dismiss();
                    }
                }, 3000);
    }


    // Open savings account
    private ErrorData submitSavingsAccount() {
        Connection cn = new SqlManager().getSQLConnection();
        ErrorData errData = new ErrorData();
        try {
            if (cn != null) {
                CallableStatement smt = cn.prepareCall("{call ADROID_InsertUpdateSavingsAccountTemp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                smt.setString("@Action", "Insert");
                smt.setString("@AccountCode", "");
                smt.setString("@AccountType", ACCOUNT_TYPE);
                smt.setString("@AccountAccessType", accountAccessType);
                smt.setString("@TypeID", "");
                smt.setString("@FormNo", GlobalStore.GlobalValue.getUserName());
                smt.setString("@OfficeID", GlobalStore.GlobalValue.getOfficeID());
                smt.setString("@AccountOpeningDate", accountOpeningDate);
                smt.setString("@DateOfEntry", dateOfEntry);
                smt.setString("@FirstApplicantMemberCode", mTie_firstApplicantMemberCode.getText().toString());
                smt.setString("@FirstApplicantName", mTie_firstApplicantName.getText().toString());
                smt.setString("@FirstApplicantDateOfBirth", firstApplicantDob);
                smt.setString("@FirstApplicantGuardian", mTie_firstApplicantGuardianName.getText().toString());
                smt.setString("@FirstApplicantGuardianRelation", mTie_firstApplicantRelationWithGuardian.getText().toString());
                smt.setString("@FirstApplicantAddress", mTie_firstApplicantAddress.getText().toString());
                smt.setString("@FirstApplicantState", mTv_state.getText().toString());
                smt.setString("@FirstApplicantAge", "1");
                smt.setString("@FirstApplicantPin", mTie_firstApplicantPin.getText().toString());
                smt.setString("@FirstApplicantPan", mTie_firstApplicantPan.getText().toString());
                smt.setString("@FirstApplicantPhone", mTie_firstApplicantMobileNumber.getText().toString());
                smt.setString("@NomineeMemberCode", nomineeMemberCode);
                smt.setString("@NomineeName", nomineeName);
                smt.setString("@NomineeDateOfBirth", nomineeDateOfBirth);
                smt.setString("@NomineeGuardian", nomineeGuardian);
                smt.setString("@NomineeGuardianRelation", nomineeGuardianRelation);
                smt.setString("@NomineeAddress", nomineeAddress);
                smt.setString("@NomineeState", nomineeState);
                smt.setString("@NomineeAge", nomineeDateOfBirth);
                smt.setString("@NomineePin", nomineePin);
                smt.setString("@NomineePan", "");
                smt.setString("@NomineePhone", nomineePhone);
                smt.setString("@NomineeDocID", nomineeIdNo);
                smt.setString("@NomineeDocType", selectedNomineeIDProofName);

                smt.setString("@IntroducerAccountCode", mEt_introducerAccountCode.getText().toString());
                smt.setString("@ArrangerCode", "");
                smt.setString("@RegAmount", "0");
                smt.setString("@PayMode", "1001");
                smt.setString("@ChequeNo", "");
                smt.setString("@ChequeDate", "");
                smt.setString("@REMARKS", "");
                smt.setString("@SecondApplicantMemberCode", mTie_secondApplicantMemberCode.getText().toString());
                if (mTie_secondApplicantMemberCode.getText().toString().length() > 0) {
                    smt.setBoolean("@HasApplicantTwo", true);
                } else {
                    smt.setBoolean("@HasApplicantTwo", false);
                }
                smt.setString("@SecondApplicantName", mTie_secondApplicantName.getText().toString());
                smt.setString("@SecondApplicantDateOfBirth", secondApplicantDob);
                smt.setString("@SecondApplicantGuardian", mTie_secondApplicantGuardianName.getText().toString());
                smt.setString("@SecondApplicantGuardianRelation", mTie_secondApplicantGuardianRelation.getText().toString());
                smt.setString("@SecondApplicantAddress", mTie_secondApplicantAddress.getText().toString());
                smt.setString("@SecondApplicantState", secondApplicantState);
                smt.setString("@SecondApplicantAge", "1");
                smt.setString("@SecondApplicantPin", mTie_secondApplicantPin.getText().toString());
                smt.setString("@SecondApplicantPan", mTie_secondApplicantPan.getText().toString());
                smt.setString("@SecondApplicantPhone", mTie_secondApplicantPhoneNumber.getText().toString());
                smt.setString("@ThirdApplicantMemberCode", "");
                smt.setString("@ThirdApplicantName", "");
                smt.setString("@ThirdApplicantDateOfBirth", "");
                smt.setString("@ThirdApplicantGuardian", "");
                smt.setString("@ThirdApplicantGuardianRelation", "");
                smt.setString("@ThirdApplicantAddress", "");
                smt.setString("@ThirdApplicantState", "");
                smt.setString("@ThirdApplicantAge", "");
                smt.setString("@ThirdApplicantPin", "");
                smt.setString("@ThirdApplicantPan", "");
                smt.setString("@ThirdApplicantPhone", "");
                smt.setString("@UserName", GlobalStore.GlobalValue.getUserName());
                smt.setBoolean("@IsSMSApplicable", mBool_smsSubStatus);


                smt.setObject("@NomineeImage", dataNomineeImage);


                smt.setObject("@NomineeSignature", dataNomineeSignature);


                smt.registerOutParameter("@ReturnVoucherNo", Types.VARCHAR);
                smt.registerOutParameter("@ACode", Types.VARCHAR);
                smt.registerOutParameter("@ErrorCode", Types.INTEGER);


                smt.executeUpdate();
                errData.setErrorCode(smt.getInt("@ErrorCode"));
                errData.setErrorString(smt.getString("@ReturnVoucherNo"));
                errData.setErrorString(smt.getString("@ACode"));
            } else {
                errData.setErrorCode(2);
                errData.setErrorString("Connection failed");
            }
        } catch (Exception ex) {
            errData.setErrorCode(1);
            errData.setErrorString(ex.getMessage().toString());
        }
        return errData;
    }


    /**
     * Open add nominee dialog
     */
    private void openAddNomineeDialog() {
        final Dialog dialog = new Dialog(ArrOpenSBAccActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_add_nominee);
        dialog.show();

        final TextInputEditText mTie_addNomineeName = dialog.findViewById(R.id.tie_custom_dialog_add_nominee_name);
        final TextInputEditText mTie_nomineeMemberCode = dialog.findViewById(R.id.tie_custom_dialog_add_nominee_member_code);
        final TextView mTv_nomineeDateOfBirth = dialog.findViewById(R.id.tv_custom_dialog_add_nominee_date_of_birth);
        final TextInputEditText mTie_nomineeGuardianName = dialog.findViewById(R.id.tie_custom_dialog_add_nominee_guardian_name);
        final EditText mEt_nomineeAddress = dialog.findViewById(R.id.et_custom_dialog_add_nominee_nominee_address);
        final Spinner mSp_state = dialog.findViewById(R.id.sp_custom_dialog_add_nominee_state);
        final TextInputEditText mTie_nomineePin = dialog.findViewById(R.id.tie_custom_dialog_add_nominee_pin);
        final TextInputEditText mTie_nomineePan = dialog.findViewById(R.id.tie_custom_dialog_add_nominee_pan);
        final TextInputEditText mTie_nomineePhone = dialog.findViewById(R.id.tie_custom_dialog_add_nominee_phone);
        final Spinner mSp_nomineeIdProof = dialog.findViewById(R.id.sp_custom_dialog_add_nominee_id_proof);
        final Spinner mSp_nomineeGuardianRelation = dialog.findViewById(R.id.sp_custom_dialog_add_nominee_guardian_relation);

        Button mBtn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button mBtn_ok = dialog.findViewById(R.id.btn_add);

        List<String> stateList = new ArrayList<String>();
        stateList.add("Select state");
        stateList.add("Andhra Pradesh");
        stateList.add("Arunachal Pradesh");
        stateList.add("Assam");
        stateList.add("Bihar");
        stateList.add("Chhattisgarh");
        stateList.add("Goa");
        stateList.add("Gujarat");
        stateList.add("Haryana");
        stateList.add("Himachal Pradesh");
        stateList.add("Jammu and Kashmir");
        stateList.add("Jharkhand");
        stateList.add("Karnataka");
        stateList.add("Kerala");
        stateList.add("Madhya Pradesh");
        stateList.add("Maharashtra");
        stateList.add("Manipur");
        stateList.add("Meghalaya");
        stateList.add("Mizoram");
        stateList.add("Nagaland");
        stateList.add("Odisha");
        stateList.add("Punjab");
        stateList.add("Rajasthan");
        stateList.add("Sikkim");
        stateList.add("Tamil Nadu");
        stateList.add("Telangana");
        stateList.add("Tripura");
        stateList.add("Uttar Pradesh");
        stateList.add("Uttarakhand");
        stateList.add("West Bengal");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, stateList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSp_state.setAdapter(dataAdapter);

        mSp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectedState = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> idProofList = new ArrayList<String>();
        idProofList.add("Select ID proof");
        idProofList.add("Aadhar Card");
        idProofList.add("Voter Card");
        idProofList.add("PAN Card");
        idProofList.add("Ration Card");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, R.layout.spinner_item, idProofList);

        adapter2.setDropDownViewResource(R.layout.spinner_item);
        mSp_nomineeIdProof.setAdapter(adapter2);

        mSp_nomineeIdProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectedNomineeIDProofName = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> guardianRelationList = new GeneralFnc().getRelation();
        guardianRelationList.remove(0);
        guardianRelationList.add(0, "Select Guardian relation");
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, R.layout.spinner_item, guardianRelationList);

        adapter3.setDropDownViewResource(R.layout.spinner_item);
        mSp_nomineeGuardianRelation.setAdapter(adapter3);

        mSp_nomineeGuardianRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    nomineeGuardianRelation = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /** Date of Birth */
        mTv_nomineeDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ArrOpenSBAccActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        mTv_nomineeDateOfBirth.setText(dayOfMonth + "-" + month + "-" + year);
                        nomineeDateOfBirth = Integer.toString(year) + String.format("%02d", (month)) + String.format("%02d", dayOfMonth);
                        String n;
                    }
                }, currentYear, currentMonth, currentDay);
                mCalendar.set(currentYear, currentMonth, currentDay);

                datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // context.startActivity(new Intent(MemberHomeScreen.this, KycActivity.class));
                // finish();
                //if (mTie_nomineeMemberCode.getText().toString().trim().length() > 0) {
                if (mTie_addNomineeName.getText().toString().trim().length() > 0) {
                    if (mTv_nomineeDateOfBirth.getText().toString().trim().length() > 0) {
                        if (!selectedState.equals("")) {
                            if (mTie_nomineePin.getText().toString().trim().length() > 0) {
                                nomineeMemberCode = mTie_nomineeMemberCode.getText().toString();
                                nomineeName = mTie_addNomineeName.getText().toString();
                                //nomineeDateOfBirth = mTv_nomineeDateOfBirth.getText().toString();
                                if (mTie_nomineeGuardianName.getText().toString().trim().length() > 0)
                                    nomineeGuardian = mTie_nomineeGuardianName.getText().toString();

                                if (mEt_nomineeAddress.getText().toString().trim().length() > 0)
                                    nomineeAddress = mEt_nomineeAddress.getText().toString();
                                nomineeState = selectedState;
                                nomineePin = mTie_nomineePin.getText().toString();
                                if (mTie_nomineePan.getText().toString().trim().length() > 0)
                                    nomineeIdNo = mTie_nomineePan.getText().toString();
                                if (mTie_nomineePhone.getText().toString().trim().length() > 0)
                                    nomineePhone = mTie_nomineePhone.getText().toString();
                                nomineeAdded = true;
                                nomineeAdded();
                                dialog.cancel();
                            } else {
                                mTie_nomineePin.setError("Enter Pin");
                                mTie_nomineePin.requestFocus();
                            }
                        } else {
                            mSp_state.performClick();
                            Toast.makeText(ArrOpenSBAccActivity.this, "Select state", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mTv_nomineeDateOfBirth.setError("Select DOB");
                        mTv_nomineeDateOfBirth.performClick();
                    }
                } else {
                    mTie_addNomineeName.setError("Enter name");
                    mTie_addNomineeName.requestFocus();
                }
                /*} else {
                    mTie_nomineeMemberCode.setError("Enter member code");
                    mTie_nomineeMemberCode.requestFocus();
                }*/
            }
        });
    }

    private void nomineeAdded() {
        if (nomineeAdded) {
            llNomineeImageRoot.setVisibility(View.VISIBLE);
        } else {
            llNomineeImageRoot.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ArrOpenSBAccActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
