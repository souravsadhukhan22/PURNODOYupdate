package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetLoanEMI;

import java.util.ArrayList;

public class AdapterLoanEmiStatement extends RecyclerView.Adapter<AdapterLoanEmiStatement.EmiStatementViewHolder> {
    private Context context;
    private ArrayList<SetGetLoanEMI> arrayList;

    public AdapterLoanEmiStatement(Context context, ArrayList<SetGetLoanEMI> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EmiStatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_emi_statement,parent,false);
        return new EmiStatementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmiStatementViewHolder holder, int position) {
        if(arrayList.size() > 0){
            holder.mTv_emiNumber.setText(arrayList.get(position).getEmiNo());
            holder.mTv_currentBalance.setText(arrayList.get(position).getCurrentBalance());
            holder.mTv_emiAmount.setText(arrayList.get(position).getEmiAmount());
            holder.mTv_payMode.setText(arrayList.get(position).getPayMode());
            holder.mTv_remarks.setText(arrayList.get(position).getRemarks());
            holder.mTv_dueDate.setText(arrayList.get(position).getEmiDueDate());
            holder.mTv_payemntDate.setText(arrayList.get(position).getPaymentDate());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class EmiStatementViewHolder extends RecyclerView.ViewHolder{

        TextView mTv_emiNumber;
        TextView mTv_currentBalance;
        TextView mTv_emiAmount;
        TextView mTv_payMode;
        TextView mTv_remarks;
        TextView mTv_dueDate;
        TextView mTv_payemntDate;


        public EmiStatementViewHolder(View itemView) {
            super(itemView);

            mTv_emiNumber = itemView.findViewById(R.id.tv_row_loan_emi_statement_emi_no);
            mTv_currentBalance = itemView.findViewById(R.id.tv_row_loan_emi_statement_current_balance);
            mTv_emiAmount = itemView.findViewById(R.id.tv_row_loan_emi_statement_emi_amnt);
            mTv_payMode = itemView.findViewById(R.id.tv_row_loan_emi_statement_pay_mode);
            mTv_remarks = itemView.findViewById(R.id.tv_row_loan_emi_statement_remarks);
            mTv_dueDate = itemView.findViewById(R.id.tv_row_loan_emi_statement_due_date);
            mTv_payemntDate = itemView.findViewById(R.id.tv_row_loan_emi_statement_payment_date);
        }
    }
}
