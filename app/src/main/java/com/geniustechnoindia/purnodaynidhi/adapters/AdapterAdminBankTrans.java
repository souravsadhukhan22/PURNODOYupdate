package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminBankTransSetGet;

import java.util.ArrayList;

public class AdapterAdminBankTrans extends RecyclerView.Adapter<AdapterAdminBankTrans.AdminBankTransViewHolder> {

    private Context context;
    private ArrayList<AdminBankTransSetGet> arrayList;

    public AdapterAdminBankTrans(Context context, ArrayList<AdminBankTransSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminBankTransViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_admin_bank_trans_details, viewGroup, false);
        return new AdminBankTransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBankTransViewHolder holder, int i) {
        holder.mTv_date.setText(arrayList.get(i).getDate());
        holder.mTv_debit.setText(arrayList.get(i).getDebit());
        holder.mTv_credit.setText(arrayList.get(i).getCredit());
        holder.mTv_narration.setText(arrayList.get(i).getNarration());
        holder.mTv_serial.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdminBankTransViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_date,mTv_debit,mTv_credit,mTv_narration,mTv_serial;

        public AdminBankTransViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_date=itemView.findViewById(R.id.tv_row_admin_bank_trans_details_date);
            mTv_debit=itemView.findViewById(R.id.tv_row_admin_bank_trans_details_debit);
            mTv_credit=itemView.findViewById(R.id.tv_row_admin_bank_trans_details_credit);
            mTv_narration=itemView.findViewById(R.id.tv_row_admin_bank_trans_details_narration);
            mTv_serial=itemView.findViewById(R.id.row_admin_bank_trans_details_serial);
        }
    }
}
