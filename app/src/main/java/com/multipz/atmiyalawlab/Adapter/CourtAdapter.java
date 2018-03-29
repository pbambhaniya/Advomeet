package com.multipz.atmiyalawlab.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.multipz.atmiyalawlab.Model.CourtModel;
import com.multipz.atmiyalawlab.Model.ModelCourt;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 18-12-2017.
 */

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.MyViewHolder> {
    private List<ModelCourt> list;
    private ItemClickListener clickListener;
    ProgressDialog dialog;
    Context context;
    String id, name, param;
    public static ArrayList<ModelCourt> arrCourt;
    private Shared shared;

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


    public CourtAdapter(List<ModelCourt> expertsList, Context cox) {
        this.list = expertsList;
        this.context = cox;
        shared = new Shared(context);
        arrCourt = new Gson().fromJson(shared.getListCourt(), new TypeToken<List<ModelCourt>>() {
        }.getType());

        if (arrCourt == null) {
            arrCourt = new ArrayList<>();
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ModelCourt model = list.get(position);
        holder.cb_court.setText(model.getCourt_name());
        holder.cb_court.setButtonDrawable(R.drawable.check_box);

        if (arrCourt != null) {
            for (int l = 0; l < arrCourt.size(); l++) {
                if (list.get(position).getAh_court_id() == arrCourt.get(l).getAh_court_id()) {
                    holder.cb_court.setChecked(true);
                    break;
                }
            }
        }

        holder.cb_court.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    arrCourt.add(list.get(position));

                } else {
                    for (int l = 0; l < arrCourt.size(); l++) {
                        if (list.get(position).getAh_court_id() == arrCourt.get(l).getAh_court_id()) {
                            arrCourt.remove(l);
                            break;
                        }
                    }
                }
            }
        });

    }

    public ArrayList<ModelCourt> getCourtList() {
        return arrCourt;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}