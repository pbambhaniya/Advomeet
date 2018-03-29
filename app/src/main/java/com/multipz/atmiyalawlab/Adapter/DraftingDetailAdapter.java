package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.ModelDraftingDetail;
import com.multipz.atmiyalawlab.Model.ModelDraftingType;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class DraftingDetailAdapter extends RecyclerView.Adapter<DraftingDetailAdapter.PlanetViewHolder> {

    ArrayList<ModelDraftingDetail> planetList;
    private Context con;
    private int row_index = 0;
    public ItemClickListener clickListener;

    public DraftingDetailAdapter(Context context, ArrayList<ModelDraftingDetail> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_drafting_detail, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        ModelDraftingDetail data = planetList.get(position);
        holder.txtDraftingDetail.setText(data.getAh_drafting_subtype());

    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDraftingDetail;
        private ImageView doc_download;
        private LinearLayout rel_root;

        public PlanetViewHolder(View view) {
            super(view);
            txtDraftingDetail = (TextView) view.findViewById(R.id.txtDraftingDetail);
            doc_download = (ImageView) view.findViewById(R.id.doc_download);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            doc_download.setOnClickListener(this);
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