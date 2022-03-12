package com.geniustechnoindia.purnodaynidhi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.model.SetGetTotalActiveMember;

import java.util.ArrayList;

public class AdapterTotalActiveMember extends RecyclerView.Adapter<AdapterTotalActiveMember.ActiveMemberViewHolder> {
    ArrayList<SetGetTotalActiveMember> activeMemberalist;
    Context context;

    public AdapterTotalActiveMember(ArrayList<SetGetTotalActiveMember> activeMemberalist, Context context) {
        this.activeMemberalist = activeMemberalist;
        this.context = context;
    }

    @NonNull
    @Override
    public ActiveMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActiveMemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.totalactivemembershow,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveMemberViewHolder holder, int position) {
        holder.Itemno.setText(activeMemberalist.get(position).getItemNo());
        holder.Membercode.setText(activeMemberalist.get(position).getMemberCode());
        holder.Membername.setText(activeMemberalist.get(position).getMemberName());
        holder.Address.setText(activeMemberalist.get(position).getAddress());
        holder.Phone.setText(activeMemberalist.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return activeMemberalist.size();
    }

    public class ActiveMemberViewHolder extends RecyclerView.ViewHolder {
        TextView Itemno,Membercode,Membername,Address,Phone;
        public ActiveMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            Itemno=itemView.findViewById(R.id.tv1show);
            Membercode=itemView.findViewById(R.id.tv2show);
            Membername=itemView.findViewById(R.id.tv3show);
          Address=itemView.findViewById(R.id.tv5show);
            Phone=itemView.findViewById(R.id.tv4show);
        }
    }
}
