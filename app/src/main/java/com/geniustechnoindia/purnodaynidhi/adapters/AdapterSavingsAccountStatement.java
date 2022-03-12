package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SavingsStatementSetGet;

import java.util.ArrayList;

public class AdapterSavingsAccountStatement extends RecyclerView.Adapter<AdapterSavingsAccountStatement.ViewHolderSavingsStatement> {

    private Context mContext;
    private ArrayList<SavingsStatementSetGet> mArrayList;

    public AdapterSavingsAccountStatement(Context mContext, ArrayList<SavingsStatementSetGet> mArrayList){
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public ViewHolderSavingsStatement onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_savings_account_statement,parent,false);
        return new ViewHolderSavingsStatement(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSavingsStatement holder, int position) {
        if(mArrayList.size() > 0){
            holder.mTv_slNo.setText(String.valueOf(position+1));
            holder.mTv_tDate.setText(mArrayList.get(position).gettDate());
            holder.mTv_mode.setText(mArrayList.get(position).getPaymode());
            holder.mTv_deposit.setText(mArrayList.get(position).getDepositAmount());
            holder.mTv_withdraw.setText(mArrayList.get(position).getWithdrawlAmount());
            holder.mTv_balance.setText(mArrayList.get(position).getBalance());
            holder.mTv_particular.setText(mArrayList.get(position).getParticular());
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class ViewHolderSavingsStatement extends RecyclerView.ViewHolder{

        private TextView mTv_slNo;
        private TextView mTv_tDate;
        private TextView mTv_mode;
        private TextView mTv_deposit;
        private TextView mTv_withdraw;
        private TextView mTv_balance;
        private TextView mTv_particular;

        public ViewHolderSavingsStatement(View itemView) {
            super(itemView);

            mTv_slNo = itemView.findViewById(R.id.tv_row_savings_account_statement_slno);
            mTv_tDate = itemView.findViewById(R.id.tv_row_savings_account_statement_tdate);
            mTv_mode = itemView.findViewById(R.id.tv_row_savings_account_statement_mode);
            mTv_deposit = itemView.findViewById(R.id.tv_row_savings_account_statement_deposit);
            mTv_withdraw = itemView.findViewById(R.id.tv_row_savings_account_statement_withdraw);
            mTv_balance = itemView.findViewById(R.id.tv_row_savings_account_statement_balance);
            mTv_particular = itemView.findViewById(R.id.tv_row_savings_account_statement_particular);

        }
    }

}
