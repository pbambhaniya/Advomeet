package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.multipz.atmiyalawlab.Model.ModelAddQualification;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class AddQualificationAdapter extends RecyclerView.Adapter<AddQualificationAdapter.PlanetViewHolder> {

    ArrayList<ModelAddQualification> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;
    private ArrayList<SpinnerModel> object_qualification, object_degree;
    private Shared shared;

    public AddQualificationAdapter(Context context, ArrayList<ModelAddQualification> planetList) {
        this.con = context;
        this.planetList = planetList;
        shared = new Shared(con);

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_qualification, parent, false);
        return new PlanetViewHolder(itemView);
    }

    ModelAddQualification data;

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        ModelAddQualification model = planetList.get(position);
        holder.txtDegree.setText("Degree : " + model.getDegree_id());
        holder.txtQulification.setText(model.getGraduate_id());
        holder.txt_passing_year.setText("Passing Year : " + model.getPassing_year());
        holder.txt_university.setText("Institute : " + model.getUniversity());
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDegree, txtQulification, txt_passing_year, txt_university, btn_remove;
        private LinearLayout rel_root;

        public PlanetViewHolder(View view) {
            super(view);
            txtDegree = (TextView) view.findViewById(R.id.txtDegree);
            txtQulification = (TextView) view.findViewById(R.id.txtQulification);
            txt_passing_year = (TextView) view.findViewById(R.id.txt_passing_year);
            txt_university = (TextView) view.findViewById(R.id.txt_university);
            btn_remove = (TextView) view.findViewById(R.id.btn_remove);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));

            itemView.setOnClickListener(this);
            btn_remove.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }

    public void removeItem(int pos) {
        this.planetList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, planetList.size());

        //  notifyData(planetList);
    }
}