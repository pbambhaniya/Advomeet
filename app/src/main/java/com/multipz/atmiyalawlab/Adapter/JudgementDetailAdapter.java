package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.JudjmentModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.List;

/**
 * Created by Admin on 18-11-2017.
 */

public class JudgementDetailAdapter extends RecyclerView.Adapter<JudgementDetailAdapter.MyViewHolder> {
    private Context context;
    private List<JudjmentModel> list;
    private ItemClickListener clickListener;


    public JudgementDetailAdapter(List<JudjmentModel> expertsList, Context context) {
        this.list = expertsList;
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtJudgementTitle,txt_date;
        private LinearLayout rel_root,lnr_judgement;

        public MyViewHolder(View view) {
            super(view);
            txtJudgementTitle = (TextView) view.findViewById(R.id.txtJudgementTitle);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            lnr_judgement = (LinearLayout) view.findViewById(R.id.lnr_judgement);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            lnr_judgement.setOnClickListener(this);
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
    public JudgementDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_judgement, parent, false);

        return new JudgementDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JudgementDetailAdapter.MyViewHolder holder, int position) {
        JudjmentModel model = list.get(position);
        holder.txtJudgementTitle.setText(model.getTitle());
        holder.txt_date.setText(model.getPublishdate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
