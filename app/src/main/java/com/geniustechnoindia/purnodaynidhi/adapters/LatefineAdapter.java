package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.RenewalLatefineSetGet;

import java.util.ArrayList;

public class LatefineAdapter extends RecyclerView.Adapter<LatefineAdapter.LatefineViewHolder> {

    private Context context;
    private ArrayList<RenewalLatefineSetGet> arrayList;

    public LatefineAdapter(Context context, ArrayList<RenewalLatefineSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public LatefineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_renewal_late_fine, viewGroup, false);
        return new LatefineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LatefineViewHolder latefineViewHolder, int i) {
        latefineViewHolder.mTv_instNo.setText(arrayList.get(i).getInstNo()+"");
        latefineViewHolder.mTv_instAmount.setText(arrayList.get(i).getLatefine());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class LatefineViewHolder extends RecyclerView.ViewHolder {

        TextView mTv_instNo;
        TextView mTv_instAmount;

        public LatefineViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_instNo = itemView.findViewById(R.id.tv_row_renewal_late_fine_inst_no);
            mTv_instAmount = itemView.findViewById(R.id.tv_row_renewal_late_fine_amnt);

        }
    }
}
