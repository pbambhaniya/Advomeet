package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.DictionaryModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class SearchTextFilterDictionaryAdapter extends RecyclerView.Adapter<SearchTextFilterDictionaryAdapter.PlanetViewHolder> {

    ArrayList<DictionaryModel> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;

    public SearchTextFilterDictionaryAdapter(Context context, ArrayList<DictionaryModel> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dictionary_search_list, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        DictionaryModel data = planetList.get(position);
        holder.txt_dictionary_search.setText(position + 1 + ". " + data.getTitle());

       /* if (row_index == position) {
            holder.txtFilterDictionary.setTextColor(Color.parseColor("#d15241"));
        } else {
            holder.txtFilterDictionary.setTextColor(Color.parseColor("#ffffff"));
        }*/

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_dictionary_search;
        private LinearLayout rel_root, lnr_data;

        public PlanetViewHolder(View view) {
            super(view);
            txt_dictionary_search = (TextView) view.findViewById(R.id.txt_dictionary_search);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            lnr_data = (LinearLayout) view.findViewById(R.id.lnr_data);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            lnr_data.setOnClickListener(this);
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