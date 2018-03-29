package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.NewsModel;
import com.multipz.atmiyalawlab.Model.TopLawyerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 10-01-2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Context context;
    private List<NewsModel> list;
    private ItemClickListener clickListener;


    public NewsAdapter(List<NewsModel> expertsList, Context context) {
        this.list = expertsList;
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtlawyername;
        public ImageView imguser;
        public RelativeLayout rel_root, rel_news;

        public MyViewHolder(View view) {
            super(view);
            imguser = (ImageView) view.findViewById(R.id.imguser);
            txtlawyername = (TextView) view.findViewById(R.id.txtlawyername);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            rel_news = (RelativeLayout) view.findViewById(R.id.rel_news);

            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
            rel_news.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_news_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsModel model = list.get(position);
        if (model.getAuthor().contentEquals("null")) {
            holder.txtlawyername.setText("");
        } else {
            holder.txtlawyername.setText(model.getAuthor());
        }
        if (model.getUrlToImage().contentEquals("")) {
            Picasso.with(context).load(R.drawable.user).into(holder.imguser);
        } else {
            Picasso.with(context).load(list.get(position).getUrlToImage()).into(holder.imguser);
        }
//        Glide.with(context).load(list.get(position).getUserImage()).into(holder.img_profile);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
