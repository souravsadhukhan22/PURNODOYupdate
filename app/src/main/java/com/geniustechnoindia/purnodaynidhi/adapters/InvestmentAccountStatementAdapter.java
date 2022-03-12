package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetInvestmentAccountStatement;

import java.util.ArrayList;

public class InvestmentAccountStatementAdapter extends RecyclerView.Adapter<InvestmentAccountStatementAdapter.ViewHolderInvestmentAccSt> {

    private Context mContext;
    private ArrayList<SetGetInvestmentAccountStatement> mArrayList;

    public InvestmentAccountStatementAdapter(Context mContext, ArrayList<SetGetInvestmentAccountStatement> mArrayList){
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public ViewHolderInvestmentAccSt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_investment_account,parent,false);
        return new ViewHolderInvestmentAccSt(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInvestmentAccSt holder, int position) {
        if(mArrayList.size() > 0){
            holder.mInstNo.setText(mArrayList.get(position).getInstNo());
            holder.mPolicyCode.setText(mArrayList.get(position).getPolicyCode());
            holder.mAmount.setText(mArrayList.get(position).getAmount());
            holder.mRenewDate.setText(mArrayList.get(position).getRenewDate());
            holder.mLateFine.setText(mArrayList.get(position).getLateFine());
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class ViewHolderInvestmentAccSt extends RecyclerView.ViewHolder{

        private TextView mInstNo;
        private TextView mPolicyCode;
        private TextView mAmount;
        private TextView mRenewDate;
        private TextView mLateFine;

        public ViewHolderInvestmentAccSt(View itemView) {
            super(itemView);

            mInstNo = itemView.findViewById(R.id.tv_row_investment_account_inst_no);
            mPolicyCode = itemView.findViewById(R.id.tv_row_investment_account_policy_code);
            mAmount = itemView.findViewById(R.id.tv_row_investment_account_amount);
            mRenewDate = itemView.findViewById(R.id.tv_row_investment_account_renew_date);
            mLateFine = itemView.findViewById(R.id.tv_row_investment_account_late_fine);
        }
    }
}
