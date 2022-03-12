package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminExecutiveCollecReportSetGet;

import java.util.ArrayList;

public class AdapterAdminExecutiveCollecReport extends RecyclerView.Adapter<AdapterAdminExecutiveCollecReport.AdapterAdminExecutiveCollecReportViewHolder> {

    private Context context;
    private ArrayList<AdminExecutiveCollecReportSetGet> arrayList;

    public AdapterAdminExecutiveCollecReport(Context context, ArrayList<AdminExecutiveCollecReportSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterAdminExecutiveCollecReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_admin_executive_collection, viewGroup, false);
        return new AdapterAdminExecutiveCollecReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAdminExecutiveCollecReportViewHolder holder, int i) {
        holder.mTv_policyCode.setText(arrayList.get(i).getPolicyCode());
        holder.mTv_instNo.setText(arrayList.get(i).getInstNo());
        holder.mTv_amount.setText(arrayList.get(i).getAmount());
        holder.mTv_dateOfRenewal.setText(arrayList.get(i).getDateOfRenewal());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdapterAdminExecutiveCollecReportViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_policyCode, mTv_instNo, mTv_amount, mTv_dateOfRenewal;

        public AdapterAdminExecutiveCollecReportViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_policyCode = itemView.findViewById(R.id.tv_row_admin_executive_collection_policy_code);
            mTv_instNo = itemView.findViewById(R.id.tv_row_admin_executive_collection_inst_no);
            mTv_amount = itemView.findViewById(R.id.tv_row_admin_executive_collection_amount);
            mTv_dateOfRenewal = itemView.findViewById(R.id.tv_row_admin_executive_collection_date_of_renewal);
        }
    }

}
