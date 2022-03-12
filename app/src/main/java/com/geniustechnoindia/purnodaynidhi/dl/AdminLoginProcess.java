package com.geniustechnoindia.purnodaynidhi.dl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.geniustechnoindia.purnodaynidhi.activities.AdminLoginActivity;
import com.geniustechnoindia.purnodaynidhi.activities.LoginOptionsActivity;
import com.geniustechnoindia.purnodaynidhi.bean.AdminData;
import com.geniustechnoindia.purnodaynidhi.bean.AdminInfoStaticData;
import com.geniustechnoindia.purnodaynidhi.others.APILinks;
import com.geniustechnoindia.purnodaynidhi.parsers.PostDataParserArrayResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminLoginProcess {

    private String str_uaername,str_password;
    private Context context;
    private boolean flag=false;

    public AdminLoginProcess(Context context, String str_uaername, String str_password) {
        this.str_uaername = str_uaername;
        this.str_password = str_password;
        this.context = context;
    }

    public void adminLoginProcessJSON(){

        HashMap hashMap=new HashMap();
        hashMap.put("username", str_uaername);
        hashMap.put("pass", str_password);

        new PostDataParserArrayResponse(context, APILinks.ADMIN_LOGIN, hashMap, true, new PostDataParserArrayResponse.OnGetResponseListner() {

            @Override
            public void onGetResponse(JSONArray response) {
                try {
                    if (response != null) {
                        if (response.length() > 0) {
                            JSONObject jsonObject = response.getJSONObject(0);
                            if (jsonObject.getString("UserName").equals(str_uaername) || jsonObject.getString("Password").equals(str_password)) {
                                AdminInfoStaticData.setAdminUserName(jsonObject.getString("UserName"));
                                AdminInfoStaticData.setAdminPassword(jsonObject.getString("Password"));
                                AdminInfoStaticData.setAdminUserTypeId(jsonObject.getString("UserTypeID"));
                                AdminInfoStaticData.setAdminOfcId(jsonObject.getString("OfficeID"));
                                AdminInfoStaticData.setAdminIsActivate(jsonObject.getString("IsActive"));
                                AdminInfoStaticData.setAdminId(jsonObject.getString("id"));
                                AdminData.adminOfcID=jsonObject.getString("OfficeID");
                                //AdminInfoStaticData.setLoginStatus(true);
                                if(context.getClass() == AdminLoginActivity.class){
                                    ((AdminLoginActivity)context).callThis(true);
                                }
                                if(context.getClass()== LoginOptionsActivity.class){
                                    ((LoginOptionsActivity)context).callThis(true);
                                }
                            } else {
                                Toast.makeText(context, "Invalid Username Or Password", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //AdminInfoStaticData.setLoginStatus(false);
                            if(context.getClass() == AdminLoginActivity.class){
                                ((AdminLoginActivity)context).callThis(false);
                            }
                            if(context.getClass()== LoginOptionsActivity.class){
                                ((AdminLoginActivity)context).callThis(false);
                            }
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("ADMIN",e.getMessage());
                    e.printStackTrace();
                }
            }


        });
        //return flag;
    }

}
