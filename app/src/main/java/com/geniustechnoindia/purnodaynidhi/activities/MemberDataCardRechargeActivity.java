package com.geniustechnoindia.purnodaynidhi.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.ProviderSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.SearchableSpinner;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberDataCardRechargeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    // toolbar
    private Toolbar mToolbar;
    private TextView mTv_toolbarTitle;

    // views
    private Button mBtn_submit;
    private TextInputEditText mTie_customerNumber;
    private TextInputEditText mTie_customerMobileNumber;
    private SearchableSpinner mSs_providerSearchableSpinner;
    private TextInputEditText mTie_rechargeAmount;
    private LinearLayout mLl_selectContact;
    private TextView mTv_selectSavingsAccount;

    // Spinner
    private ProviderSetGet providerSetGet;
    private ArrayList<ProviderSetGet> mArrayListProviderSetGet;

    // Static code
    private static final int PICK_CONTACT_REQUEST = 23;

    // variables
    private String providerId = "";
    private String providerName = "";
    private String mobileNumber = "";

    private PopupMenu popupMenuSelectSavingsAccount;
    private static String accountCode = "";
    private static String memberCode = "";
    private static String amount = "";
    private static String reqId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data_card_recharge);


        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mTv_toolbarTitle.setText("Datacard Recharge");

        mArrayListProviderSetGet = new ArrayList<>();

        // Searchable spinner
        mSs_providerSearchableSpinner.setTitle("Select Provider");

        // Popup menu select account
        popupMenuSelectSavingsAccount = new PopupMenu(this, mTv_selectSavingsAccount);     // TODO Change menu options

        // TODO Get menu options first
        getMenuOptions(APILinks.GET_SAVINGS_ACCOUNT_CODE + GlobalStore.GlobalValue.getMemberCode());

        popupMenuSelectSavingsAccount.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mTv_selectSavingsAccount.setText(item.getTitle());
                accountCode = (String) item.getTitle();
                return true;
            }
        });

        loadSpinnerData(APILinks.GET_OPERATOR_CODES);
    }

    private void getMenuOptions(String url) {
        new GetDataParserArray(MemberDataCardRechargeActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    /*popupMenuAccountNumber.getMenu().add("19020100001");
                    popupMenuAccountNumber.getMenu().add("19020100002");*/
                    try {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                popupMenuSelectSavingsAccount.getMenu().add(response.getString(i));
                            }
                        } else {
                            Toast.makeText(MemberDataCardRechargeActivity.this, "No savings account found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadSpinnerData(String url) {
        new GetDataParserArray(MemberDataCardRechargeActivity.this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            providerSetGet = new ProviderSetGet();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (jsonObject.getString("type").equals("datacard")) {
                                    providerSetGet.setProviderId(jsonObject.getString("opcode"));
                                    providerSetGet.setProviderName(jsonObject.getString("providername"));
                                    mArrayListProviderSetGet.add(providerSetGet);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mSs_providerSearchableSpinner.setAdapter(new ArrayAdapter<ProviderSetGet>(MemberDataCardRechargeActivity.this, android.R.layout.simple_spinner_dropdown_item, mArrayListProviderSetGet));
                    }
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        if (v == mBtn_submit) {
            if (mTie_customerMobileNumber.getText().toString().trim().length() > 0) {
                if (!providerId.equals("")) {
                    if (mTie_rechargeAmount.getText().toString().trim().length() > 0) {
                        memberCode = GlobalStore.GlobalValue.getMemberCode();
                        amount = mTie_rechargeAmount.getText().toString();
                        reqId = memberCode + mTie_customerMobileNumber.getText().toString();
                        String rechargeFor = "DatacardRecharge," + providerName;
                        sendRechargeDataToApi(accountCode, mTie_customerMobileNumber.getText().toString(), memberCode, providerId, providerName, amount, reqId, "",rechargeFor,"","",providerId,providerName,"Datacard");
                    } else {
                        mTie_rechargeAmount.setError("Enter recharge amount");
                        mTie_rechargeAmount.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "Select provider", Toast.LENGTH_SHORT).show();
                    mSs_providerSearchableSpinner.performClick();
                }
            } else {
                mTie_customerMobileNumber.setError("Enter mobile number");
                mTie_customerMobileNumber.requestFocus();
            }
        } else if (v == mLl_selectContact) {
            pickContact();
        } else if (v == mTv_selectSavingsAccount) {
            popupMenuSelectSavingsAccount.show();
        }
    }

    /**
     * Send recharge amount data to server
     */
    private void sendRechargeDataToApi(String accountCode, String mobileNumber, String memberCode, String providerId, String providerName, String amount, String reqId, String fieldOne, String rechargeFor, String fieldTwo, String fieldThree, String opCode, String opName, String rechargeType) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("membercode", memberCode);
        hashMap.put("mobilenumber", mobileNumber);
        hashMap.put("operatorcode", providerId);
        hashMap.put("amount", amount);
        hashMap.put("reqid", reqId);
        hashMap.put("field1", fieldOne);
        hashMap.put("field2", fieldTwo);
        hashMap.put("field3", fieldThree);
        hashMap.put("accountNumber", mobileNumber);                                                 // This is the account number eg, electricity bill acc no
        hashMap.put("opCode", opCode);
        hashMap.put("opName", opName);
        hashMap.put("rechargeType", rechargeType);
        hashMap.put("accountcode", accountCode);
        hashMap.put("rechargeFor", rechargeFor);
        new PostDataParserObjectResponse(MemberDataCardRechargeActivity.this, APILinks.RECHARGE_MOBILE, hashMap, true, new PostDataParserObjectResponse.OnGetResponseListner() {
            @Override
            public void onGetResponse(JSONObject response) {
                if (response != null) {
                    mTie_customerMobileNumber.setText("");
                    mTie_rechargeAmount.setText("");
                    try {
                        Toast.makeText(MemberDataCardRechargeActivity.this, response.getString("returnStatus"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MemberDataCardRechargeActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);             // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String selectedNumber = cursor.getString(column).replaceAll("\\s+", "");             // Remove any spaces
                String selectedNumberNew = selectedNumber.replaceAll("-", "");                       // Remove any dashes
                mTie_customerMobileNumber.setText(selectedNumberNew.substring(selectedNumberNew.length() - 10));      // Max length should not be more than 10
            }
        }
    }

    private void setViewReferences() {
        // toolbar
        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_toolbarTitle = findViewById(R.id.toolbar_title);

        // views
        mTv_selectSavingsAccount = findViewById(R.id.tv_activity_datacard_recharge_select_savings_account);
        mTie_customerMobileNumber = findViewById(R.id.tie_activity_datacard_recharge_customer_mobile_number);
        mLl_selectContact = findViewById(R.id.ll_activity_datacard_recharge_customer_select_mobile_number);
        mSs_providerSearchableSpinner = findViewById(R.id.ss_activity_datacard_recharge_select_provider);
        mTie_rechargeAmount = findViewById(R.id.tie_activity_datacard_recharge_amount);
        mBtn_submit = findViewById(R.id.btn_activity_datacard_recharge_proceed_to_pay_bill);
    }

    private void bindEventHandlers() {
        mBtn_submit.setOnClickListener(this);
        mLl_selectContact.setOnClickListener(this);
        mSs_providerSearchableSpinner.setOnItemSelectedListener(this);
        mTv_selectSavingsAccount.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberDataCardRechargeActivity.this, MemberRechargeOption.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberDataCardRechargeActivity.this, MemberRechargeOption.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        providerId = mArrayListProviderSetGet.get(position).getProviderId();
        providerName = mArrayListProviderSetGet.get(position).getProviderName();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
