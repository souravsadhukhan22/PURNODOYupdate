package com.geniustechnoindia.purnodaynidhi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.activities.MemberPaySelfPolicyActivity;
import com.geniustechnoindia.purnodaynidhi.bean.MemberDataForRenewalPay;
import com.geniustechnoindia.purnodaynidhi.model.SetGetMemberPolicyList;

import java.util.ArrayList;

public class AdapterMemberPolicyList extends RecyclerView.Adapter<AdapterMemberPolicyList.MemberPolicyListViewHolder> {


    private Context context;
    private ArrayList<SetGetMemberPolicyList> arrayList;

    public AdapterMemberPolicyList(Context context, ArrayList<SetGetMemberPolicyList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MemberPolicyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_member_policy_list,parent,false);
        return new MemberPolicyListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberPolicyListViewHolder holder, int position) {
        holder.mTv_policyCode.setText(arrayList.get(position).getPolicyCode());
        holder.mTv_mode.setText(arrayList.get(position).getMode());
        holder.mTv_amount.setText(String.valueOf(arrayList.get(position).getAmount()));
        if(arrayList.get(position).isIsmatured()){
            holder.mTv_matured.setText("Matured");
        }else{
            holder.mTv_matured.setText("Not-Matured");
        }
        holder.mLl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDataForRenewalPay.policyCode=arrayList.get(position).getPolicyCode();
                MemberDataForRenewalPay.applicantName=arrayList.get(position).getApplicantName();
                MemberDataForRenewalPay.dateOfCom=arrayList.get(position).getDateOfCom();
                MemberDataForRenewalPay.maturityDate=arrayList.get(position).getMaturityDate();
                MemberDataForRenewalPay.applicantPhone=arrayList.get(position).getApplicantPhone();
                MemberDataForRenewalPay.mode=arrayList.get(position).getMode();
                MemberDataForRenewalPay.emailID=arrayList.get(position).getEmailID();
                MemberDataForRenewalPay.term=arrayList.get(position).getTerm();
                MemberDataForRenewalPay.amount=arrayList.get(position).getAmount();
                MemberDataForRenewalPay.depositAmt=arrayList.get(position).getDepositAmt();
                MemberDataForRenewalPay.maturityAmt=arrayList.get(position).getMaturityAmt();


                Intent intent=new Intent(context, MemberPaySelfPolicyActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
                Toast.makeText(context,arrayList.get(position).getPolicyCode() , Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MemberPolicyListViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_policyCode,mTv_mode,mTv_amount,mTv_matured;
        LinearLayout mLl_root;
        public MemberPolicyListViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_policyCode = itemView.findViewById(R.id.tv_row_member_policy_list_policy_code);
            mTv_mode = itemView.findViewById(R.id.tv_row_member_policy_list_mode);
            mTv_amount = itemView.findViewById(R.id.tv_row_member_policy_list_amount);
            mTv_matured = itemView.findViewById(R.id.tv_row_member_policy_list_matured);
            mLl_root = itemView.findViewById(R.id.ll_row_member_policy_list);
        }
    }

}
