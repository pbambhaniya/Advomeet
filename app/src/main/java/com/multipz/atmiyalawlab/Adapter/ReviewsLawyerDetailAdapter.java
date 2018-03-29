package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.ModelReviewsLawyerDetail;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Admin on 18-11-2017.
 */

public class ReviewsLawyerDetailAdapter extends RecyclerView.Adapter<ReviewsLawyerDetailAdapter.PlanetViewHolder> {

    ArrayList<ModelReviewsLawyerDetail> planetList;
    private Context con;
    private int row_index = 0;
    private ItemClickListener clickListener;

    public ReviewsLawyerDetailAdapter(Context context, ArrayList<ModelReviewsLawyerDetail> planetList) {
        this.con = context;
        this.planetList = planetList;

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review_lawyer_detail, parent, false);
        return new PlanetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, final int position) {
        ModelReviewsLawyerDetail data = planetList.get(position);
        holder.txtusername.setText(data.getFull_name());
        holder.txtdate.setText(data.getFeedback_date());
        holder.txtShowComment.setText(data.getMessage());
        holder.reviewrating.setRating(Float.parseFloat(data.getRate()));
    }


    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtusername, txtdate, txtShowComment;
        private LinearLayout rel_root;
        private RatingBar reviewrating;

        public PlanetViewHolder(View view) {
            super(view);

            reviewrating = (RatingBar) view.findViewById(R.id.reviewrating);
            txtusername = (TextView) view.findViewById(R.id.txtusername);
            txtdate = (TextView) view.findViewById(R.id.txtdate);
            txtShowComment = (TextView) view.findViewById(R.id.txtShowComment);
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