package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminLoanPaySetGet;

import java.util.ArrayList;

public class AdapterAdminLoanPay extends RecyclerView.Adapter<AdapterAdminLoanPay.AdminLoanPayViewHolder> {

    private Context context;
    private ArrayList<AdminLoanPaySetGet> arrayList;

    public AdapterAdminLoanPay(Context context, ArrayList<AdminLoanPaySetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminLoanPayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_admin_loan_payment_details, viewGroup, false);
        return new AdminLoanPayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminLoanPayViewHolder holder, int i) {
        holder.mTv_loanCode.setText(arrayList.get(i).getLoanCode());
        holder.mTv_name.setText(arrayList.get(i).getHolderName());
        holder.mTv_loanDate.setText(arrayList.get(i).getLoanDate());
        holder.mTv_loanTerm.setText(arrayList.get(i).getLoanTerm());
        holder.mTv_emiMode.setText(arrayList.get(i).getEmiMode());
        holder.mTv_loanApprAmt.setText(arrayList.get(i).getLoanApproveAmt());
        holder.mTv_emiAmt.setText(arrayList.get(i).getEmiAmt());
        holder.mTv_emiNo.setText(arrayList.get(i).getEmiNo());
        holder.mTv_paidAmt.setText(arrayList.get(i).getPaidAmt());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdminLoanPayViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_loanCode,mTv_name,mTv_loanDate,mTv_loanTerm,mTv_emiMode,mTv_loanApprAmt,mTv_emiAmt,mTv_emiNo,mTv_paidAmt;
        public AdminLoanPayViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_loanCode=itemView.findViewById(R.id.tv_row_loan_payment_details_loan_code);
            mTv_name=itemView.findViewById(R.id.tv_row_loan_payment_details_holder_name);
            mTv_loanDate=itemView.findViewById(R.id.tv_row_loan_payment_details_loan_date);
            mTv_loanTerm=itemView.findViewById(R.id.tv_row_loan_payment_details_loan_term);
            mTv_emiMode=itemView.findViewById(R.id.tv_row_loan_payment_details_emi_mode);
            mTv_loanApprAmt=itemView.findViewById(R.id.tv_row_loan_payment_details_loan_appr_amt);
            mTv_emiAmt=itemView.findViewById(R.id.tv_row_loan_payment_details_emi_amt);
            mTv_emiNo=itemView.findViewById(R.id.tv_row_loan_payment_details_emi_no);
            mTv_paidAmt=itemView.findViewById(R.id.tv_row_loan_payment_details_paid_amt);
        }
    }
}
