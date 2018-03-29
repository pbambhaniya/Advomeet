package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.multipz.atmiyalawlab.Model.CasesModel;
import com.multipz.atmiyalawlab.Model.PostCaseModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 11-12-2017.
 */

public class PostCaseAdapter extends RecyclerView.Adapter<PostCaseAdapter.MyViewHolder> {
    private List<CasesModel> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_title, txt_criminal_type, txt_city, txt_posted_date;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_criminal_type = (TextView) view.findViewById(R.id.txt_criminal_type);
            txt_city = (TextView) view.findViewById(R.id.txt_city);
            txt_posted_date = (TextView) view.findViewById(R.id.txt_posted_date);
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


    public PostCaseAdapter(List<CasesModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public PostCaseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_case, parent, false);
        return new PostCaseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostCaseAdapter.MyViewHolder holder, int position) {
        CasesModel model = list.get(position);
        holder.txt_title.setText(model.getTitle());
        holder.txt_criminal_type.setText(model.getCase_type());
        holder.txt_city.setText(model.getCity_name());
        holder.txt_posted_date.setText("Posted On " + model.getCreated_on());

        //Picasso.with(context).load(Config.ProfileImage + model.getProfile_img()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
