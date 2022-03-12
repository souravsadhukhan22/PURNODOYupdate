package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetAllTotalActivePolicyList;

import java.util.ArrayList;

public class AdapterAllTotalActivePolicyList extends RecyclerView.Adapter<AdapterAllTotalActivePolicyList.ActiveMemeberPolicyViewHolder> {
    ArrayList<SetGetAllTotalActivePolicyList> activeMemberpolicyalist;
    Context context;

    public AdapterAllTotalActivePolicyList(ArrayList<SetGetAllTotalActivePolicyList> activeMemberpolicyalist, Context context) {
        this.activeMemberpolicyalist = activeMemberpolicyalist;
        this.context = context;
    }

    @NonNull
    @Override
    public ActiveMemeberPolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterAllTotalActivePolicyList.ActiveMemeberPolicyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activememberpolicyshow,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveMemeberPolicyViewHolder holder, int position) {
        holder.policyItemno.setText(activeMemberpolicyalist.get(position).getItemNo());
        holder.policyPolicycode.setText(activeMemberpolicyalist.get(position).getPolicyCode());
        holder.policyApplicantname.setText(activeMemberpolicyalist.get(position).getApplicantName());
        holder.policyApplicantphone.setText(activeMemberpolicyalist.get(position).getApplicantPhone());
        holder.policyDepositamount.setText(activeMemberpolicyalist.get(position).getDepositeAmount());

    }

    @Override
    public int getItemCount() {
        return activeMemberpolicyalist.size();
    }


    public   class ActiveMemeberPolicyViewHolder extends RecyclerView.ViewHolder{
        TextView policyItemno,policyPolicycode,policyApplicantname,policyApplicantphone,policyDepositamount;

           public ActiveMemeberPolicyViewHolder(@NonNull View itemView) {
               super(itemView);
               policyItemno=itemView.findViewById(R.id.tv1policyshow);
               policyPolicycode=itemView.findViewById(R.id.tv2policyshow);
               policyApplicantname=itemView.findViewById(R.id.tv3policyshow);
               policyApplicantphone=itemView.findViewById(R.id.tv4policyshow);
               policyDepositamount=itemView.findViewById(R.id.tv5policyshow);
           }
       }
    }

