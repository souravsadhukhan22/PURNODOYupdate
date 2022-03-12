package com.geniustechnoindia.purnodaynidhi.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.activities.MemberMobileRechargeNewActivity;
import com.geniustechnoindia.purnodaynidhi.model.ProviderNewSetGet;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.others.SearchableSpinner;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserObjectResponse;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_API_PASSWORD;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_SENDER_ID;
import static com.geniustechnoindia.purnodaynidhi.others.MyConfig.SMS_USER_NAME;

public class FragmentPrepaidRechargeNew extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    // Views
    private LinearLayout mLl_prepaidRechargeSelectContact;
    private TextInputEditText mTie_mobileNumber;
    private TextInputEditText mTie_rechargeAmount;
    private Button mBtn_proceedToRecharge;
    private SearchableSpinner mSs_searchableSpinner;
    private TextView mTv_currentBalance;
    private TextInputEditText mTie_remarks;

    // Static code
    private static final int PICK_CONTACT_REQUEST = 22;

    // vars
    private ProviderNewSetGet providerNewSetGet;
    private ArrayList<ProviderNewSetGet> mArrayListProviderSetGet;

    private PopupMenu popupMenuSelectSavingsAccount;
    private TextView mTv_selectSavingsAccount;


    private static String sbAccountCode = "";
    private static String memberCode = "";
    private static String providerId = "";                 // Operator code
    private static String providerName = "";
    private static String amount = "";
    private static String reqId = "";                      // membercoichasathinidhibilenumber
    private static String fieldOne = "";

    private static double sbCurrentBalance = 0.0;
    private Button mBtn_planDetails;
    private String reqTypeDesc = "";

    private String STV = "";
    private RadioGroup mRg_rcType;
    private RadioButton mRb_rcTypeTopUp, mRb_rcTypeSpecial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepaid_recharge_new, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewReferences();
        bindEventHandlers();

        mArrayListProviderSetGet = new ArrayList<>();

        // Searchable spinner
        mSs_searchableSpinner.setTitle("Select Provider");

        loadSpinnerData(APILinks.GET_OPERATOR_CODES_NEW + "Mobile%20Recharge");

        // Popup menu select account
        popupMenuSelectSavingsAccount = new PopupMenu(getActivity(), mTv_selectSavingsAccount);     // TODO Change menu options

        // TODO Get menu options first
        getMenuOptions(APILinks.GET_SAVINGS_ACCOUNT_CODE + GlobalStore.GlobalValue.getMemberCode());

        popupMenuSelectSavingsAccount.setOnMenuItemClickListener(item -> {
            mTv_selectSavingsAccount.setText(item.getTitle());
            sbAccountCode = (String) item.getTitle();
            getCurrentBalance(sbAccountCode);
            return true;
        });

    }

    private void getCurrentBalance(String accCode) {
        new GetDataParserArray(getActivity(), APILinks.GET_SB_CURRENT_BALANCE + accCode, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            sbCurrentBalance = response.getDouble(0);
                            //mTv_currentBalance.setText("Rs. " + String.valueOf(sbCurrentBalance));
                            mTv_currentBalance.setText("Rs. " + String.format("%.2f", sbCurrentBalance));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getMenuOptions(String url) {
        new GetDataParserArray(getActivity(), url, true, response -> {
            if (response != null) {
                /*popupMenuAccountNumber.getMenu().add("19020100001");
                popupMenuAccountNumber.getMenu().add("19020100002");*/
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            popupMenuSelectSavingsAccount.getMenu().add(response.getString(i));
                        }
                    } else {
                        Toast.makeText(getActivity(), "No savings account found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Load spinner data
     */
    private void loadSpinnerData(String url) {
        new GetDataParserArray(getActivity(), url, true, response -> {
            if (response != null) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        //providerNewSetGet = new ProviderNewSetGet();
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            providerNewSetGet = new Gson().fromJson(jsonObject.toString(), ProviderNewSetGet.class);
                            mArrayListProviderSetGet.add(providerNewSetGet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mSs_searchableSpinner.setAdapter(new ArrayAdapter<ProviderNewSetGet>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mArrayListProviderSetGet));
                }
            }
        });
    }

    private void setViewReferences() {
        mLl_prepaidRechargeSelectContact = getActivity().findViewById(R.id.ll_prepaid_recharge_select_contact);
        mTie_mobileNumber = getActivity().findViewById(R.id.tie_fragment_prepaid_recharge_mobile_number);
        mTie_rechargeAmount = getActivity().findViewById(R.id.tie_fragment_prepaid_recharge_amount);
        mBtn_proceedToRecharge = getActivity().findViewById(R.id.btn_fragment_prepaid_recharge_proceed_to_recharge);
        mSs_searchableSpinner = getActivity().findViewById(R.id.ss_fragment_prepaid_recharge_activity);
        mTv_currentBalance = getActivity().findViewById(R.id.tv_activity_wallet_recharge_current_balance);
        mTie_remarks = getActivity().findViewById(R.id.tie_fragment_prepaid_recharge_new_remarks);

        mTv_selectSavingsAccount = getActivity().findViewById(R.id.tv_fragment_prepaid_recharge_select_savings_account);
        mBtn_planDetails = getActivity().findViewById(R.id.btn_fragment_prepaid_recharge_plan_details);

        mRg_rcType = getActivity().findViewById(R.id.rg_fragment_prepaid_recharge_new_recharge_type);
        mRb_rcTypeTopUp = getActivity().findViewById(R.id.rb_fragment_prepaid_recharge_new_recharge_type_top_up);
        mRb_rcTypeSpecial = getActivity().findViewById(R.id.rb_fragment_prepaid_recharge_new_recharge_type_special);
    }

    private void bindEventHandlers() {
        mLl_prepaidRechargeSelectContact.setOnClickListener(this);
        mBtn_proceedToRecharge.setOnClickListener(this);
        mSs_searchableSpinner.setOnItemSelectedListener(this);
        mTv_selectSavingsAccount.setOnClickListener(this);
        mBtn_planDetails.setOnClickListener(this);
        mRg_rcType.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLl_prepaidRechargeSelectContact) {
            pickContact();
        } else if (v == mBtn_proceedToRecharge) {
            if (mTv_selectSavingsAccount.getText().toString().trim().length() > 0) {
                if (mTie_mobileNumber.getText().toString().trim().length() > 0) {
                    if (!providerId.equals("")) {
                        if (!STV.equals("")) {
                            if (mTie_rechargeAmount.getText().toString().trim().length() > 0) {
                                if (!mTie_rechargeAmount.getText().toString().contains(".")) {
                                    memberCode = GlobalStore.GlobalValue.getMemberCode();
                                    amount = mTie_rechargeAmount.getText().toString();
                                    reqId = memberCode + mTie_mobileNumber.getText().toString();
                                    String rechargeFor = "MobileRecharge," + providerName;

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("AccountCode", sbAccountCode);
                                    if(mTie_remarks.getText().toString().trim().length() > 0){
                                        hashMap.put("Reamrks", mTie_remarks.getText().toString());
                                    } else {
                                        hashMap.put("Reamrks", "Recharge from SB");
                                    }
                                    hashMap.put("UserName", GlobalStore.GlobalValue.getMemberCode());
                                    hashMap.put("ProviderName", providerName);
                                    hashMap.put("ProviderCode", providerId);
                                    hashMap.put("ReqTypeDesc", reqTypeDesc);
                                    hashMap.put("RefNo", GlobalStore.GlobalValue.getMemberCode() + System.currentTimeMillis());
                                    hashMap.put("CustNo", mTie_mobileNumber.getText().toString());
                                    //hashMap.put("RefMobNo",GlobalStore.GlobalValue.getPhone());
                                    hashMap.put("RefMobNo", mTie_mobileNumber.getText().toString());
                                    hashMap.put("AMT", amount);
                                    hashMap.put("STV", STV);
                                    hashMap.put("PCode", "741121");
                                    hashMap.put("LAT", "23.3017");
                                    hashMap.put("LONG", "88.5302");
                                    hashMap.put("APIREQTYPE", "RECH");

                                    payBill(getActivity(), APILinks.PAY_BILL, hashMap);


                                    //sendRechargeDataToApi(sbAccountCode, mTie_mobileNumber.getText().toString(), memberCode, providerId, providerName, amount, reqId, "", rechargeFor,"","",providerId,providerName,"Prepaid");
                                } else {
                                    mTie_rechargeAmount.setError("Recharge amount not valid ");
                                    mTie_rechargeAmount.requestFocus();
                                }
                            } else {
                                mTie_rechargeAmount.setError("Enter recharge amount");
                                mTie_rechargeAmount.requestFocus();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Select recharge type", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        mSs_searchableSpinner.performClick();
                        Toast.makeText(getActivity(), "Select provider", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mTie_mobileNumber.setError("Enter mobile number");
                    mTie_mobileNumber.requestFocus();
                }
            } else {
                mTv_selectSavingsAccount.performClick();
                Toast.makeText(getActivity().getApplicationContext(), "Select savings account.", Toast.LENGTH_SHORT).show();
            }

        } else if (v == mTv_selectSavingsAccount) {
            popupMenuSelectSavingsAccount.show();
        } else if (v == mBtn_planDetails) {
            Uri uri = Uri.parse("https://www.plansinfo.com"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * Send recharge amount data to server
     */
    private void payBill(Context context, String URL, HashMap<String, String> hashMap) {
        new PostDataParserObjectResponse(context, URL, hashMap, true, response -> {
            if (response != null) {
                /*{
                    "APIStatus": true,
                        "IsBalanceLow": false,
                        "IsRechargeRecorded": false
                }*/
                try {
                    if (response.getBoolean("APIStatus")) {
                        if (!response.getBoolean("IsBalanceLow")) {
                            if (response.getBoolean("IsRechargeRecorded")) {
                                showConfirmationDialog("Success", "Your request for Prepaid recharge has been recorded successfully.", true);
                            } else {
                                showConfirmationDialog("Failed", "Failed to record your Prepaid recharge request.", false);
                            }
                        } else {
                            showConfirmationDialog("Failed", "You don't have enough balance in your savings account.", false);
                        }
                    } else {
                        showConfirmationDialog("Failed", "Something went wrong. Please try again later.", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void showConfirmationDialog(String title, String message, boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", ((dialog, which) -> {
            dialog.dismiss();
            if (status) {
                sendSmsConfirmation(mTie_rechargeAmount.getText().toString(), GlobalStore.GlobalValue.getPhone());
            }
        })).show();
    }


    private static String msgBody = "";

    private void sendSmsConfirmation(String rechargeAmt, String phoneNumber) {
        msgBody = "Dear Customer, Prepaid recharge of Rs- " + rechargeAmt + " is successful and debited from your savings account, "+ R.string.App_Full_Name +".";
        String smsUrl = "http://bulksms.geniustechnoindia.com/api/api_http.php?username=" + SMS_USER_NAME + "&password=" + SMS_API_PASSWORD + "&to=" + phoneNumber + "&senderid=" + SMS_SENDER_ID + "&text=" + msgBody + "&route=Informative&type=text&datetime=2017-03-24%2014%3A03%3A14";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(smsUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                startActivity(new Intent(getActivity(), MemberMobileRechargeNewActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                startActivity(new Intent(getActivity(), MemberMobileRechargeNewActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
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
                Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String selectedNumber = cursor.getString(column).replaceAll("\\s+", "");     // Remove any spaces
                String selectedNumberNew = selectedNumber.replaceAll("-", "");               // Remove any dashes
                mTie_mobileNumber.setText(selectedNumberNew.substring(selectedNumberNew.length() - 10));      // Max length should not be more than 10
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    // SearchableSpinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        providerId = mArrayListProviderSetGet.get(position).getCODE();
        providerName = mArrayListProviderSetGet.get(position).getNAME();
        reqTypeDesc = mArrayListProviderSetGet.get(position).getTYPE();
        if (!mArrayListProviderSetGet.get(position).getREMARKS().equals("")) {
            //mTv_inputHint.setText(mArrayListProviderSetGet.get(position).getREMARKS());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_fragment_prepaid_recharge_new_recharge_type_top_up: {
                STV = "0";
                break;
            }
            case R.id.rb_fragment_prepaid_recharge_new_recharge_type_special: {
                STV = "1";
                break;
            }
        }
    }
}