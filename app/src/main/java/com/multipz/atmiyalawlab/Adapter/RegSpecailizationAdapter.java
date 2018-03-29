package com.multipz.atmiyalawlab.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.multipz.atmiyalawlab.Model.ModelCourt;
import com.multipz.atmiyalawlab.Model.ModelSpecialization;
import com.multipz.atmiyalawlab.Model.ModelSpecilazation;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 18-12-2017.
 */

public class RegSpecailizationAdapter extends RecyclerView.Adapter<RegSpecailizationAdapter.MyViewHolder> {
    private List<ModelSpecialization> list;
    private ItemClickListener clickListener;
    ProgressDialog dialog;
    Context context;
    String id, name, param;
    Shared shared;
    public ArrayList<ModelSpecialization> arrSpecialization;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox cb_court, txt_date;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            cb_court = (CheckBox) view.findViewById(R.id.cb_court);
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


    public RegSpecailizationAdapter(List<ModelSpecialization> expertsList, Context cox) {
        this.list = expertsList;
        this.context = cox;
        shared = new Shared(context);
        arrSpecialization = new Gson().fromJson(shared.getListSpecialization(), new TypeToken<List<ModelSpecialization>>() {
        }.getType());

        if (arrSpecialization == null) {
            arrSpecialization = new ArrayList<>();
        }
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_court, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ModelSpecialization model = list.get(position);
        holder.cb_court.setText(model.getLawyer_type());
          holder.cb_court.setButtonDrawable(R.drawable.check_box);
        if (arrSpecialization != null) {
            for (int l = 0; l < arrSpecialization.size(); l++) {
                if (list.get(position).getAh_lawyer_type_id().contentEquals(arrSpecialization.get(l).getAh_lawyer_type_id())) {
                    holder.cb_court.setChecked(true);
                    break;
                }
            }
        }

        holder.cb_court.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    arrSpecialization.add(list.get(position));

                } else {
                    for (int l = 0; l < arrSpecialization.size(); l++) {
                        if (list.get(position).getAh_lawyer_type_id().contentEquals(arrSpecialization.get(l).getAh_lawyer_type_id())) {
                            arrSpecialization.remove(l);
                            break;
                        }
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}