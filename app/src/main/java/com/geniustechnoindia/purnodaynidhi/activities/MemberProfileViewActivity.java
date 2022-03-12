package com.geniustechnoindia.purnodaynidhi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.mssql.SqlManager;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.GetDataParserArray;
import com.geniustechnoindia.purnodaynidhi.store.GlobalStore;

import org.json.JSONArray;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemberProfileViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv_name;
    private TextView mTv_dateOfBirth;
    private TextView mTv_dateOfJoin;
    private TextView mTv_fatherName;
    private TextView mTv_address;
    private TextView mTv_phoneNumber;
    private TextView mTv_email;
    private TextView mTv_gender;
    private TextView mTv_identityProof;
    private TextView mTv_addressProof;

    private LinearLayout mLl_viewProfileRoot;

    private Toolbar mToolbar;
    private TextView mTv_tootlbarTitle;
    private ImageView mIv_pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile_view);

        setViewReferences();
        bindEventHandlers();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTv_tootlbarTitle.setText("My Profile");

        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String n = GlobalStore.GlobalValue.getMemberCode();
                        if (getmemberDetails(GlobalStore.GlobalValue.getMemberCode())) {
                            // Show visibility
                            mLl_viewProfileRoot.setVisibility(View.VISIBLE);
                        } else {
                            mLl_viewProfileRoot.setVisibility(View.GONE);
                            Toast.makeText(MemberProfileViewActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);

        getMemberImage(APILinks.GET_MEMBER_PIC_BY_MEMBER_CODE+GlobalStore.GlobalValue.getMemberCode());

    }

    private void getMemberImage(String url){
        new GetDataParserArray(this, url, true, new GetDataParserArray.OnGetResponseListener() {
            @Override
            public void onGetResponse(JSONArray response) {
                try{
                    if(response.length()>0){
                        mIv_pic.setVisibility(View.VISIBLE);
                        String pic=response.get(0).toString();
                        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        mIv_pic.setImageBitmap(decodedByte);
                    }else{
                        mIv_pic.setVisibility(View.INVISIBLE);
                        //Toast.makeText(ArrangerActiveMemberSearchActivity.this, "No Pic Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                startActivity(new Intent(MemberProfileViewActivity.this, MemberDashboardActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewReferences() {
        mTv_name = findViewById(R.id.tv_activity_member_profile_view_member_name);
        mTv_dateOfBirth = findViewById(R.id.tv_activity_member_profile_view_member_date_of_birth);
        mTv_dateOfJoin = findViewById(R.id.tv_activity_member_profile_view_member_date_of_join);
        mTv_fatherName = findViewById(R.id.tv_activity_member_profile_view_member_father_name);
        mTv_address = findViewById(R.id.tv_activity_member_profile_view_member_address);
        mTv_phoneNumber = findViewById(R.id.tv_activity_member_profile_view_member_phone_no);
        mTv_email = findViewById(R.id.tv_activity_member_profile_view_member_email);
        mTv_gender = findViewById(R.id.tv_activity_member_profile_view_member_gender);
        mTv_identityProof = findViewById(R.id.tv_activity_member_profile_view_member_identity_proof);
        mTv_addressProof = findViewById(R.id.tv_activity_member_profile_view_member_address_proof);

        mLl_viewProfileRoot = findViewById(R.id.ll_activity_member_profile_view_root);

        mToolbar = findViewById(R.id.custom_toolbar);
        mTv_tootlbarTitle = findViewById(R.id.toolbar_title);

        mIv_pic=findViewById(R.id.iv_mem_profile);
    }

    private void bindEventHandlers() {
    }

    @Override
    public void onClick(View v) {

    }

    public boolean getmemberDetails(String memberCode) {
        Connection cn = new SqlManager().getSQLConnection();
        CallableStatement smt = null;

        try {
            if (cn != null) {
                smt = cn.prepareCall("{call ADROID_GetMemberDetails(?)}");
                smt.setString("@MemberCode", memberCode);
                smt.execute();
                //ResultSet rs=smt.getResultSet();
                ResultSet rs = smt.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        rs.getString("MemberCode");
                        mTv_name.setText(rs.getString("MemberName"));

                        String dateOfJoin = String.valueOf(rs.getInt("DateOfJoin"));
                        Date dateOfJoinNew = null;
                        try {
                            dateOfJoinNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(dateOfJoin);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedDateOfJoin = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(dateOfJoinNew);
                        mTv_dateOfJoin.setText(formattedDateOfJoin);

                        String dateOfBirth = String.valueOf(rs.getInt("MemberDOB"));
                        Date dateOfBirthNew = null;
                        try {
                            dateOfBirthNew = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(dateOfBirth);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedDateOfBirth = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(dateOfBirthNew);
                        mTv_dateOfBirth.setText(formattedDateOfBirth);

                        mTv_fatherName.setText(rs.getString("Father"));
                        mTv_address.setText(rs.getString("Address"));
                        rs.getString("Pincode");
                        mTv_email.setText(rs.getString("Email"));
                        mTv_phoneNumber.setText(rs.getString("Phone"));
                        rs.getString("MemberType");
                        rs.getString("BloodGr");
                        mTv_gender.setText(rs.getString("Gender"));
                        rs.getString("Occupation");
                        rs.getString("Education");
                        mTv_identityProof.setText(rs.getString("IdentityProof"));
                        mTv_addressProof.setText(rs.getString("AddrProof"));
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.getMessage().toString();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MemberProfileViewActivity.this, MemberDashboardActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
