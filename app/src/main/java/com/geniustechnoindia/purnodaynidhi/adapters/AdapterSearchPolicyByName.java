package com.geniustechnoindia.purnodaynidhi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geniustechnoindia.purnodaynidhi.R;
import com.geniustechnoindia.purnodaynidhi.activities.ArrangerPolicyRenewViewActivity;
import com.geniustechnoindia.purnodaynidhi.bean.PolicySearchByNameData;
import com.geniustechnoindia.purnodaynidhi.model.SetGetSearchPolicyByName;

import java.util.ArrayList;

public class AdapterSearchPolicyByName extends RecyclerView.Adapter<AdapterSearchPolicyByName.SearchPolicyByNameViewHolder> {
    //SetGetSearchPolicyByName


    private Context context;
    private ArrayList<SetGetSearchPolicyByName> arrayList;

    public AdapterSearchPolicyByName(Context context, ArrayList<SetGetSearchPolicyByName> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SearchPolicyByNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_policy_list_by_name, parent, false);
        return new SearchPolicyByNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPolicyByNameViewHolder holder, int position) {
        holder.mTv_policyCode.setText(arrayList.get(position).getPolicyCode());
        holder.mTv_name.setText(arrayList.get(position).getName());
        holder.mTv_guardian.setText(arrayList.get(position).getGuardian());
        holder.mLl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PolicySearchByNameData.policyCode=arrayList.get(position).getPolicyCode();
                context.startActivity(new Intent(context, ArrangerPolicyRenewViewActivity.class));
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SearchPolicyByNameViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_policyCode, mTv_name, mTv_guardian;
        LinearLayout mLl_root;

        public SearchPolicyByNameViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv_policyCode = itemView.findViewById(R.id.tv_row_policy_list_by_name_policy_code);
            mTv_name = itemView.findViewById(R.id.tv_row_policy_list_by_name_name);
            mTv_guardian = itemView.findViewById(R.id.tv_row_policy_list_by_name_guardian);
            mLl_root = itemView.findViewById(R.id.ll_row_policy_list_by_name_root);
        }
    }

}
