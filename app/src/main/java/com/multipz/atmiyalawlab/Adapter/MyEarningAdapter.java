package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.ModelJudgement;
import com.multipz.atmiyalawlab.Model.ModelMyEarnData;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 18-11-2017.
 */

public class MyEarningAdapter extends RecyclerView.Adapter<MyEarningAdapter.PlanetViewHolder> {

    ArrayList<ModelMyEarnData> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;

    public MyEarningAdapter(Context context, ArrayList<ModelMyEarnData> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_earning, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        ModelMyEarnData data = planetList.get(position);
        holder.txt_case_Title.setText(data.getFull_name());
        holder.txt_communication_type.setText(data.getCommunication_type());
        holder.txt_ruppes.setText("₹ " + data.getAmount());
        holder.txt_date_earn.setText(data.getEnd_time());
        holder.txt_setTime.setText(data.getStart_time() + " To " + data.getEnd_time());
        Picasso.with(con).load(data.getProfile_img()).into(holder.img_lawyer);

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_case_Title, txt_communication_type, txt_city, txt_ruppes, txt_date_earn, txt_setTime;
        private CircleImageView img_lawyer;
        private RelativeLayout rel_root;

        public PlanetViewHolder(View view) {
            super(view);
            txt_case_Title = (TextView) view.findViewById(R.id.txt_case_Title);
            txt_communication_type = (TextView) view.findViewById(R.id.txt_communication_type);
            txt_city = (TextView) view.findViewById(R.id.txt_city);
            txt_ruppes = (TextView) view.findViewById(R.id.txt_ruppes);
            txt_date_earn = (TextView) view.findViewById(R.id.txt_date_earn);
            img_lawyer = (CircleImageView) view.findViewById(R.id.img_lawyer);
            txt_setTime = (TextView) view.findViewById(R.id.txt_setTime);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
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
}