package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class FilterDictionaryAdapter extends RecyclerView.Adapter<FilterDictionaryAdapter.PlanetViewHolder> {

    ArrayList<String> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;

    public FilterDictionaryAdapter(Context context, ArrayList<String> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_select_dictionary_text, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        String data = planetList.get(position);
        holder.txtFilterDictionary.setText(data);

        if (row_index == position) {
            holder.txtFilterDictionary.setTextColor(con.getResources().getColor(R.color.colorgray));
            holder.txtFilterDictionary.setBackground(con.getResources().getDrawable(R.drawable.small_circle));
        } else {
            holder.txtFilterDictionary.setTextColor(con.getResources().getColor(R.color.colorPrimary));
            holder.txtFilterDictionary.setBackground(con.getResources().getDrawable(R.drawable.small_circle_unselect));

        }

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtFilterDictionary;
        private LinearLayout rel_root;

        public PlanetViewHolder(View view) {
            super(view);
            txtFilterDictionary = (TextView) view.findViewById(R.id.txtFilterDictionary);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            txtFilterDictionary.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
                row_index = getAdapterPosition();
                notifyDataSetChanged();

            }
        }
    }
}