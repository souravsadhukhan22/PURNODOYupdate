package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminNewRenewBusinessSetGet;

import java.util.ArrayList;

public class AdapterAdminNewRenewAdapter extends RecyclerView.Adapter<AdapterAdminNewRenewAdapter.NewRenewViewHolder> {


    Context context;
    ArrayList<AdminNewRenewBusinessSetGet> arrayList;

    public AdapterAdminNewRenewAdapter(Context context, ArrayList<AdminNewRenewBusinessSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NewRenewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_new_renew_business_report,viewGroup,false);
        return new NewRenewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewRenewViewHolder newRenewViewHolder, int i) {
        newRenewViewHolder.mTv_policyCode.setText(arrayList.get(i).getPolicyCode());
        newRenewViewHolder.mTv_name.setText(arrayList.get(i).getName());
        newRenewViewHolder.mTv_term.setText(arrayList.get(i).getTerm());
        newRenewViewHolder.mTv_amount.setText(arrayList.get(i).getAmount());
        newRenewViewHolder.mTv_deposit.setText(arrayList.get(i).getDepositAmt());
        newRenewViewHolder.mTv_maturity.setText(arrayList.get(i).getMaturityAmt());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class NewRenewViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_policyCode,mTv_name,mTv_term,mTv_amount,mTv_deposit,mTv_maturity;
        public NewRenewViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_policyCode=itemView.findViewById(R.id.tv_row_new_renew_busi_policy_code);
            mTv_name=itemView.findViewById(R.id.tv_row_new_renew_busi_name);
            mTv_term=itemView.findViewById(R.id.tv_row_new_renew_busi_term);
            mTv_amount=itemView.findViewById(R.id.tv_row_new_renew_busi_amount);
            mTv_deposit=itemView.findViewById(R.id.tv_row_new_renew_busi_deposit_amount);
            mTv_maturity=itemView.findViewById(R.id.tv_row_new_renew_busi_maturity_amount);
        }
    }

}
