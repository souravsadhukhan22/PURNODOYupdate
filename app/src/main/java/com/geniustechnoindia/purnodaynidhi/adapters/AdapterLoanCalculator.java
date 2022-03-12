package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetEMIBrakupData;

import java.util.ArrayList;

public class AdapterLoanCalculator extends RecyclerView.Adapter<AdapterLoanCalculator.LoanCalculatorViewHolder> {


    private Context context;
    private ArrayList<SetGetEMIBrakupData> arrayList;

    public AdapterLoanCalculator(Context context, ArrayList<SetGetEMIBrakupData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public LoanCalculatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_loan_calculator_emi_data,parent,false);
        return new LoanCalculatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanCalculatorViewHolder holder, int position) {
        holder.mTv_emiNo.setText(arrayList.get(position).getPeriod());
        holder.mTv_payDate.setText(arrayList.get(position).getPayDate());
        holder.mTv_emiAmt.setText(arrayList.get(position).getPayment());
        holder.mTv_interest.setText(arrayList.get(position).getInterest());
        holder.mTv_principle.setText(arrayList.get(position).getPrincipal());
        holder.mTv_currentBal.setText(arrayList.get(position).getCurrentBal());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LoanCalculatorViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_emiNo,mTv_payDate,mTv_emiAmt,mTv_interest,mTv_principle,mTv_currentBal;
        public LoanCalculatorViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_emiNo=itemView.findViewById(R.id.tv_row_loan_calculator_emi_data_emi);
            mTv_payDate=itemView.findViewById(R.id.tv_row_loan_calculator_emi_data_pay_date);
            mTv_emiAmt=itemView.findViewById(R.id.tv_row_loan_calculator_emi_amount);
            mTv_interest=itemView.findViewById(R.id.tv_row_loan_calculator_emi_data_interest);
            mTv_principle=itemView.findViewById(R.id.tv_row_loan_calculator_emi_data_principle);
            mTv_currentBal=itemView.findViewById(R.id.tv_row_loan_calculator_emi_data_current_bal);
        }
    }
}
