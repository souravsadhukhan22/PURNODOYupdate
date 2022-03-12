package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetLoanDueReport;

import java.util.ArrayList;

public class AdapterLoanDueReport extends RecyclerView.Adapter<AdapterLoanDueReport.DueReportViewHolder> {

    private Context context;
    private ArrayList<SetGetLoanDueReport> arrayList;

    public AdapterLoanDueReport(Context context, ArrayList<SetGetLoanDueReport> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DueReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_loan_due_report, parent, false);
        return new DueReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DueReportViewHolder holder, int position) {
        holder.mTv_loanCode.setText(arrayList.get(position).getLoanCode());
        holder.mTv_applicantName.setText(arrayList.get(position).getApplicantName());
        holder.mTv_phNo.setText(arrayList.get(position).getPhNo());
        holder.mTv_totalDueEmiNo.setText(arrayList.get(position).getTotalDueEmiNo());
        holder.mTv_totalDueEmiAmt.setText(arrayList.get(position).getTotalDueEmiAmnt());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DueReportViewHolder extends RecyclerView.ViewHolder {

        TextView mTv_loanCode;
        TextView mTv_applicantName;
        TextView mTv_phNo;
        TextView mTv_totalDueEmiNo;
        TextView mTv_totalDueEmiAmt;

        public DueReportViewHolder(View itemView) {
            super(itemView);

            mTv_loanCode = itemView.findViewById(R.id.tv_row_loan_due_report_lcode);
            mTv_applicantName = itemView.findViewById(R.id.tv_row_loan_due_report_name);
            mTv_phNo = itemView.findViewById(R.id.tv_row_loan_due_report_ph_no);
            mTv_totalDueEmiNo = itemView.findViewById(R.id.tv_row_loan_due_report_due_emi);
            mTv_totalDueEmiAmt = itemView.findViewById(R.id.tv_row_loan_due_report_due_emi_amnt);

        }
    }
}
