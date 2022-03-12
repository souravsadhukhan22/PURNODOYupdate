package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetMemberDetails;

import java.util.ArrayList;

public class AdapterArrMemberDetails extends RecyclerView.Adapter<AdapterArrMemberDetails.ArrMemberDetails>{

    private Context context;
    private ArrayList<SetGetMemberDetails> arrayList;

    public AdapterArrMemberDetails(Context context, ArrayList<SetGetMemberDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ArrMemberDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_member_details,parent,false);
        return new ArrMemberDetails(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ArrMemberDetails holder, int position) {
        holder.mTv_memberCode.setText(arrayList.get(position).getMemberCode());
        holder.mTv_memberName.setText(arrayList.get(position).getName());
        holder.mTv_ofcID.setText(arrayList.get(position).getMemberCode());
        holder.mTv_dateOfJoin.setText(arrayList.get(position).getJoinDate());
        holder.mTv_dob.setText(arrayList.get(position).getDob());
        holder.mTv_father.setText(arrayList.get(position).getFather());
        holder.mTv_addr.setText(arrayList.get(position).getAddr());
        holder.mTv_state.setText(arrayList.get(position).getState());
        holder.mTv_email.setText(arrayList.get(position).getEmail());
        holder.mTv_phone.setText(arrayList.get(position).getPhone());
        holder.mTv_gender.setText(arrayList.get(position).getGender());
        holder.mTv_idProof.setText(arrayList.get(position).getIdProof());
        holder.mTv_idProofNo.setText(arrayList.get(position).getIdProofNo());
        holder.mTv_panNo.setText(arrayList.get(position).getPanNo());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ArrMemberDetails extends RecyclerView.ViewHolder{
        TextView mTv_memberCode,mTv_memberName,mTv_ofcID,mTv_dateOfJoin,mTv_dob,mTv_father,mTv_addr,mTv_state,mTv_email,mTv_phone,mTv_gender;
        TextView mTv_idProof,mTv_idProofNo,mTv_panNo;
        public ArrMemberDetails(@NonNull View itemView) {
            super(itemView);

            mTv_memberCode = itemView.findViewById(R.id.tv_row_member_details_member_code);
            mTv_memberName = itemView.findViewById(R.id.tv_row_member_details_member_name);
            mTv_ofcID = itemView.findViewById(R.id.tv_row_member_details_ofc_id);
            mTv_dateOfJoin = itemView.findViewById(R.id.tv_row_member_details_joined);
            mTv_dob = itemView.findViewById(R.id.tv_row_member_details_dob);
            mTv_father = itemView.findViewById(R.id.tv_row_member_details_father);
            mTv_addr = itemView.findViewById(R.id.tv_row_member_details_addr);
            mTv_state = itemView.findViewById(R.id.tv_row_member_details_state);
            mTv_email = itemView.findViewById(R.id.tv_row_member_details_email);
            mTv_phone = itemView.findViewById(R.id.tv_row_member_details_phone);
            mTv_gender = itemView.findViewById(R.id.tv_row_member_details_gender);
            mTv_idProof = itemView.findViewById(R.id.tv_row_member_details_idproof);
            mTv_idProofNo = itemView.findViewById(R.id.tv_row_member_details_idproof_no);
            mTv_panNo = itemView.findViewById(R.id.tv_row_member_details_pan_no);
        }
    }
}
