package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.CallListModel;
import com.multipz.atmiyalawlab.Model.SubscriptionPlanModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Admin on 16-02-2018.
 */

public class GetSubsriptionPlanAdapter extends RecyclerView.Adapter<GetSubsriptionPlanAdapter.MyViewHolder> {
    private List<SubscriptionPlanModel> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout rel_root;
        TextView txt_select_plan;

        public MyViewHolder(View view) {
            super(view);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            txt_select_plan = (TextView) view.findViewById(R.id.txt_select_plan);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }

        }
    }

    public GetSubsriptionPlanAdapter(Context context, List<SubscriptionPlanModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public GetSubsriptionPlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        try {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subscription_plan, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new GetSubsriptionPlanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GetSubsriptionPlanAdapter.MyViewHolder holder, int position) {
        SubscriptionPlanModel model = list.get(position);
            holder.txt_select_plan.setText(model.getSubscription_plan() + " for ₹" + model.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
