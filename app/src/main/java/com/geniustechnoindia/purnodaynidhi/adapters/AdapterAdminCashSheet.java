package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminCashSheetSetGet;

import java.util.ArrayList;

public class AdapterAdminCashSheet extends RecyclerView.Adapter<AdapterAdminCashSheet.AdminCashSheetViewHolder> {

    private Context context;
    private ArrayList<AdminCashSheetSetGet> arrayList;

    public AdapterAdminCashSheet(Context context, ArrayList<AdminCashSheetSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminCashSheetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_admin_cash_sheet_details, viewGroup, false);
        return new AdminCashSheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCashSheetViewHolder holder, int i) {
        holder.mTv_receivedName.setText(arrayList.get(i).getReceivedName());
        holder.mTv_receivedAmt.setText(String.valueOf(arrayList.get(i).getReceivedAmt()));
        holder.mTv_paymentName.setText(arrayList.get(i).getPaymentName());
        holder.mTv_paymentAmt.setText(String.valueOf(arrayList.get(i).getPaymentAmt()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdminCashSheetViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_receivedName,mTv_receivedAmt,mTv_paymentName,mTv_paymentAmt;
        public AdminCashSheetViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_receivedName=itemView.findViewById(R.id.row_admin_cash_sheet_received_name);
            mTv_receivedAmt=itemView.findViewById(R.id.row_admin_cash_sheet_received_amt);
            mTv_paymentName=itemView.findViewById(R.id.row_admin_cash_sheet_payment_name);
            mTv_paymentAmt=itemView.findViewById(R.id.row_admin_cash_sheet_payment_amt);
        }
    }
}
