package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminBankBookSetGet;

import java.util.ArrayList;

public class AdapterAdminBankBook extends RecyclerView.Adapter<AdapterAdminBankBook.AdminBankBookViewHolder> {

    private Context context;
    private ArrayList<AdminBankBookSetGet> arrayList;

    public AdapterAdminBankBook(Context context, ArrayList<AdminBankBookSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminBankBookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_admin_bank_book, viewGroup, false);
        return new AdminBankBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBankBookViewHolder holder, int i) {
        holder.mTv_ofcID.setText(arrayList.get(i).getIfcID());
        holder.mTv_bName.setText(arrayList.get(i).getbName());
        holder.mTv_tDate.setText(arrayList.get(i).gettDate());
        holder.mTv_bankCode.setText(arrayList.get(i).getBankCode());
        holder.mTv_purCode.setText(arrayList.get(i).getPurCode());
        holder.mTv_bankName.setText(arrayList.get(i).getBankName());
        holder.mtv_narration.setText(arrayList.get(i).getNarration());
        holder.mTv_debit.setText(arrayList.get(i).getDebit());
        holder.mTv_credit.setText(arrayList.get(i).getCredit());
        holder.mTv_rNo.setText(arrayList.get(i).getrNo());
        holder.mTv_serial.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdminBankBookViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_ofcID,mTv_bName,mTv_tDate,mTv_bankCode,mTv_purCode,mTv_bankName,mtv_narration,mTv_debit,mTv_credit,mTv_rNo;
        TextView mTv_serial;
        public AdminBankBookViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_ofcID=itemView.findViewById(R.id.tv_row_admin_bank_book_ofc_id);
            mTv_bName=itemView.findViewById(R.id.tv_row_admin_bank_book_b_name);
            mTv_tDate=itemView.findViewById(R.id.tv_row_admin_bank_book_t_date);
            mTv_bankCode=itemView.findViewById(R.id.tv_row_admin_bank_book_bank_code);
            mTv_purCode=itemView.findViewById(R.id.tv_row_admin_bank_book_pur_code);
            mTv_bankName=itemView.findViewById(R.id.tv_row_admin_bank_book_bank_name);
            mtv_narration=itemView.findViewById(R.id.tv_row_admin_bank_book_narration);
            mTv_debit=itemView.findViewById(R.id.tv_row_admin_bank_book_debit);
            mTv_credit=itemView.findViewById(R.id.tv_row_admin_bank_book_credit);
            mTv_rNo=itemView.findViewById(R.id.tv_row_admin_bank_book_r_no);

            mTv_serial=itemView.findViewById(R.id.tv_row_admin_bank_book_serial);
        }
    }
}
