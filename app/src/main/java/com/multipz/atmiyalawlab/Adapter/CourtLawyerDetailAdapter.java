package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.CourtModel;
import com.multipz.atmiyalawlab.Model.ModelJudgement;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class CourtLawyerDetailAdapter extends RecyclerView.Adapter<CourtLawyerDetailAdapter.PlanetViewHolder> {

    ArrayList<CourtModel> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;

    public CourtLawyerDetailAdapter(Context context, ArrayList<CourtModel> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_courts_lawyers_detail, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        CourtModel data = planetList.get(position);
        holder.txt_court_type.setText(data.getCourt_name());

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_court_type;
        private LinearLayout rel_root;

        public PlanetViewHolder(View view) {
            super(view);

            txt_court_type = (TextView) view.findViewById(R.id.txt_court_type);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
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
}