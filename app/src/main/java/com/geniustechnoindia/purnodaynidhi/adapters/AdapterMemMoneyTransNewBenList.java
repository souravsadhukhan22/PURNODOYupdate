package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetMemMoneyTransNewBenList;

import java.util.ArrayList;

public class AdapterMemMoneyTransNewBenList extends RecyclerView.Adapter<com.geniustechnoindia.purnodaynidhi.adapters.AdapterMemMoneyTransNewBenList.MemMoneyTransNewBenLisViewHolder> {

    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position,boolean isPay,boolean isDel);
    }

    private Context context;
    private ArrayList<SetGetMemMoneyTransNewBenList> arrayList;

    public AdapterMemMoneyTransNewBenList(Context context, ArrayList<SetGetMemMoneyTransNewBenList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MemMoneyTransNewBenLisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_member_money_trans_new_bene_list, parent, false);
        return new MemMoneyTransNewBenLisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemMoneyTransNewBenLisViewHolder holder, int position) {
        holder.mTv_beneName.setText(arrayList.get(position).getBeneName());
        holder.mTv_beneMobile.setText("Ph - " + arrayList.get(position).getBeneMobile());
        holder.mTv_beneBankName.setText(arrayList.get(position).getBeneBankName());
        holder.mTv_beneAccNo.setText("Acc No : \n" + arrayList.get(position).getBeneAccNo());
        holder.mTv_beneIFSC.setText("IFSC : \n" + arrayList.get(position).getBeneIFSC());
        if (arrayList.get(position).getBeneStatus().equals("1")) {
            holder.mTv_beneStatus.setText("Status : Active");
        } else {
            holder.mTv_beneStatus.setText("Status : Not Active");
        }

        holder.mIv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position,true,false);
            }
        });
        holder.mIv_beneDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position,false,true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MemMoneyTransNewBenLisViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_beneID, mTv_beneName, mTv_beneMobile, mTv_beneBankID, mTv_beneBankName, mTv_beneAccNo, mTv_beneIFSC, mTv_beneStatus;
        ImageView mIv_payment, mIv_beneDelete;

        public MemMoneyTransNewBenLisViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_beneName = itemView.findViewById(R.id.tv_row_member_money_trans_new_bene_list_name);
            mTv_beneMobile = itemView.findViewById(R.id.tv_row_member_money_trans_new_bene_list_phone);
            mTv_beneBankName = itemView.findViewById(R.id.tv_row_member_money_trans_new_bene_list_bank_name);
            mTv_beneAccNo = itemView.findViewById(R.id.tv_row_member_money_trans_new_bene_list_acc_no);
            mTv_beneIFSC = itemView.findViewById(R.id.tv_row_member_money_trans_new_bene_list_ifsc);
            mTv_beneStatus = itemView.findViewById(R.id.tv_row_member_money_trans_new_bene_list_status);

            mIv_payment = itemView.findViewById(R.id.iv_row_mem_money_trans_new_bene_pay);
            mIv_beneDelete = itemView.findViewById(R.id.iv_row_mem_money_trans_new_bene_delete);

        }
    }

    public void setOnClick(com.geniustechnoindia.purnodaynidhi.adapters.AdapterMemMoneyTransNewBenList.OnItemClicked onClick) {
        this.onClick = onClick;
    }

}
