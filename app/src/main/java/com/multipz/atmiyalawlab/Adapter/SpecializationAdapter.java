package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.SpecializationModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.List;

/**
 * Created by Admin on 05-02-2018.
 */

public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.MyViewHolder> {
    private List<SpecializationModel> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_language;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txt_language = (TextView) view.findViewById(R.id.txt_language);

            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    public SpecializationAdapter(List<SpecializationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public SpecializationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specilization_item, parent, false);
        return new SpecializationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecializationAdapter.MyViewHolder holder, int position) {
        SpecializationModel model = list.get(position);
        holder.txt_language.setText(model.getLawyer_type());


        //Picasso.with(context).load(Config.ProfileImage + model.getProfile_img()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
