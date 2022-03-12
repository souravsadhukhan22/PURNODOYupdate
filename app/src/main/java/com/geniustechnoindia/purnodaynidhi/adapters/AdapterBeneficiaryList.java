package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.listeners.Listener;
import com.geniustechnoindia.purnodaynidhi.model.MemberBeneficiarySetGet;

import java.util.ArrayList;

public class AdapterBeneficiaryList extends RecyclerView.Adapter<AdapterBeneficiaryList.ViewHolderBeneList> {

    private Context context;
    private ArrayList<MemberBeneficiarySetGet> arrayList;
    private Listener listener;

    public AdapterBeneficiaryList(Context context, ArrayList<MemberBeneficiarySetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void setOnClick(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderBeneList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_bene_list,parent,false);
        return new ViewHolderBeneList(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBeneList holder, final int position) {
        holder.mTv_beneId.setText(arrayList.get(position).getBENEID());
        holder.mTv_beneName.setText(arrayList.get(position).getBENENAME());
        holder.mTv_beneStatus.setText(arrayList.get(position).getBENESTATUS());
        holder.mTv_bankName.setText(arrayList.get(position).getBANKNAME());
        holder.mTv_branchName.setText(arrayList.get(position).getBRANCHNAME());
        holder.mTv_ifscCode.setText(arrayList.get(position).getIFSCCODE());
        holder.mTv_accountNumber.setText(arrayList.get(position).getACCOUNTNO());
        holder.mTv_mobileNumber.setText(arrayList.get(position).getMOBILE());
        holder.mLl_beneRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolderBeneList extends RecyclerView.ViewHolder{

        private LinearLayout mLl_beneRoot;
        private TextView mTv_beneId;
        private TextView mTv_beneName;
        private TextView mTv_beneStatus;
        private TextView mTv_bankName;
        private TextView mTv_branchName;
        private TextView mTv_ifscCode;
        private TextView mTv_accountNumber;
        private TextView mTv_mobileNumber;


        public ViewHolderBeneList(View itemView) {
            super(itemView);

            mLl_beneRoot = itemView.findViewById(R.id.ll_row_bene_list_root);
            mTv_beneId = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_id);
            mTv_beneName = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_name);
            mTv_beneStatus = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_status);
            mTv_bankName = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_bank_name);
            mTv_branchName = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_branch_name);
            mTv_ifscCode = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_ifsc_code);
            mTv_accountNumber = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_account_code);
            mTv_mobileNumber = itemView.findViewById(R.id.tv_activity_row_bene_list_beneficiary_mobile_number);

        }
    }
}
