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
import com.geniustechnoindia.purnodaynidhi.activities.MemberLoanEmiPaymentActivity;
import com.geniustechnoindia.purnodaynidhi.bean.MemberDataForLoanPay;
import com.geniustechnoindia.purnodaynidhi.model.MemberLoanListSetGet;

import java.util.ArrayList;

public class AdapterMemberLoanList extends RecyclerView.Adapter<AdapterMemberLoanList.MemberLoanListViewHolder>{


    private Context context;
    private ArrayList<MemberLoanListSetGet> arrayList;

    public AdapterMemberLoanList(Context context, ArrayList<MemberLoanListSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MemberLoanListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_member_loan_list,parent,false);
        return new MemberLoanListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberLoanListViewHolder holder, int position) {
        holder.mTv_loanCode.setText(arrayList.get(position).getLoanCode());
        holder.mTv_emiAmt.setText(String.valueOf(arrayList.get(position).getEmiAmt()));
        holder.mTv_emiMode.setText(arrayList.get(position).getEmiMode());
        if(arrayList.get(position).isClose()){
            holder.mTv_status.setText("L Closed");
        }else{
            holder.mTv_status.setText("L Open");
        }
        holder.mLl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDataForLoanPay.loanCode=arrayList.get(position).getLoanCode();
                MemberDataForLoanPay.emiAmt=arrayList.get(position).getEmiAmt();
                MemberDataForLoanPay.phoneNo=arrayList.get(position).getPhone();

                if(arrayList.get(position).isClose()){
                    Toast.makeText(context, "Loan closed", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(context, MemberLoanEmiPaymentActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    Toast.makeText(context,arrayList.get(position).getLoanCode() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MemberLoanListViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_loanCode,mTv_emiAmt,mTv_emiMode,mTv_status;
        private LinearLayout mLl_root;
        public MemberLoanListViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_loanCode=itemView.findViewById(R.id.tv_row_member_loan_list_loan_code);
            mTv_emiAmt=itemView.findViewById(R.id.tv_row_member_loan_list_emi_amt);
            mTv_emiMode=itemView.findViewById(R.id.tv_row_member_loan_list_emi_mode);
            mTv_status=itemView.findViewById(R.id.tv_row_member_loan_list_loan_status);
            mLl_root=itemView.findViewById(R.id.ll_row_member_loan_list_root);
        }
    }
}
