package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.AdminMemberSearchSetGet;

import java.util.ArrayList;

public class AdminMemberSearchAdapter extends RecyclerView.Adapter<AdminMemberSearchAdapter.MemberSearchViewHolder> {


    private Context context;
    private ArrayList<AdminMemberSearchSetGet> arrayList;

    public AdminMemberSearchAdapter(Context context, ArrayList<AdminMemberSearchSetGet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MemberSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_member_details_admin,viewGroup,false);
        return new MemberSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberSearchViewHolder memberResultViewHolder, int i) {
        memberResultViewHolder.mTv_slNo.setText(arrayList.get(i).getSlNo());
        memberResultViewHolder.mTv_memberName.setText(arrayList.get(i).getMemberName());
        memberResultViewHolder.mTv_memberCode.setText(arrayList.get(i).getMemberCode());
        memberResultViewHolder.mTv_memberPhone.setText(arrayList.get(i).getPhone());
        memberResultViewHolder.mTv_memberAddress.setText(arrayList.get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class MemberSearchViewHolder extends RecyclerView.ViewHolder{

        TextView mTv_slNo;
        TextView mTv_memberName;
        TextView mTv_memberCode;
        TextView mTv_memberPhone;
        TextView mTv_memberAddress;
        public MemberSearchViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_slNo = itemView.findViewById(R.id.tv_row_member_details_sl_no);
            mTv_memberName = itemView.findViewById(R.id.tv_row_member_details_member_name);
            mTv_memberCode = itemView.findViewById(R.id.tv_row_member_details_member_code);
            mTv_memberPhone = itemView.findViewById(R.id.tv_row_member_details_member_phone);
            mTv_memberAddress = itemView.findViewById(R.id.tv_row_member_details_address);
        }
    }
}
