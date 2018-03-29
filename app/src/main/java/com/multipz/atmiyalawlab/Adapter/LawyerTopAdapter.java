package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.TopLawyerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 30-11-2017.
 */

public class LawyerTopAdapter extends RecyclerView.Adapter<LawyerTopAdapter.MyViewHolder> {
    private Context context;
    private List<TopLawyerModel> list;
    private ItemClickListener clickListener;


    public LawyerTopAdapter(List<TopLawyerModel> expertsList, Context context) {
        this.list = expertsList;
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtlawyername;
        public ImageView imguser;
        public RelativeLayout rel_root;
        public RelativeLayout tol_lawyer;
        public MyViewHolder(View view) {
            super(view);
            imguser = (ImageView) view.findViewById(R.id.imguser);
            txtlawyername = (TextView) view.findViewById(R.id.txtlawyername);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            tol_lawyer = (RelativeLayout) view.findViewById(R.id.top_lawyer);
            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
            tol_lawyer.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    @Override
    public LawyerTopAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_tem_top_lawyer, parent, false);

        return new LawyerTopAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LawyerTopAdapter.MyViewHolder holder, int position) {
        TopLawyerModel model = list.get(position);
        holder.txtlawyername.setText(model.getFull_name());
        String img = model.getProfile_img();
        Picasso.with(context).load(img).into(holder.imguser);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
