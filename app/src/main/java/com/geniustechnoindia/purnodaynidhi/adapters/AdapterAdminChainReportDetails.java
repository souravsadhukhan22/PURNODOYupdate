package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.ChainReportDetailsSetGet;

import java.util.ArrayList;

public class AdapterAdminChainReportDetails extends RecyclerView.Adapter<AdapterAdminChainReportDetails.CReportViewHolder> {

    Context context;
    ArrayList<ChainReportDetailsSetGet> arrayList;

    public AdapterAdminChainReportDetails(Context context, ArrayList<ChainReportDetailsSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_chain_report_details,viewGroup,false);
        return new CReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CReportViewHolder cReportViewHolder, int i) {
        cReportViewHolder.mTv_arrCode.setText(arrayList.get(i).getArrangerCode());
        cReportViewHolder.mTv_arrName.setText(arrayList.get(i).getArrangerName());
        cReportViewHolder.mTv_renewalAmount.setText(arrayList.get(i).getRenewalAmount());
        cReportViewHolder.mTv_slNo.setText(arrayList.get(i).getSlNo());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CReportViewHolder extends RecyclerView.ViewHolder{
        TextView mTv_arrCode,mTv_arrName,mTv_renewalAmount,mTv_slNo;
        public CReportViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_arrCode=itemView.findViewById(R.id.row_chain_report_details_arranger_code);
            mTv_arrName=itemView.findViewById(R.id.row_chain_report_details_arranger_name);
            mTv_renewalAmount=itemView.findViewById(R.id.row_chain_report_details_renewal_amount);
            mTv_slNo=itemView.findViewById(R.id.tv_row_chain_report_details_serial_no);
        }
    }

}
