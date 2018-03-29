package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.NewsModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 10-01-2018.
 */

public class SeeallNewsAdapter extends RecyclerView.Adapter<SeeallNewsAdapter.MyViewHolder> {
    private Context context;
    private List<NewsModel> list;
    private ItemClickListener clickListener;


    public SeeallNewsAdapter(List<NewsModel> expertsList, Context context) {
        this.list = expertsList;
        this.context = context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_news_title, txt_news_desc;
        public ImageView imguser;
        public LinearLayout rel_root;
        LinearLayout lnr_news;

        public MyViewHolder(View view) {
            super(view);
            imguser = (ImageView) view.findViewById(R.id.imguser);
            txt_news_title = (TextView) view.findViewById(R.id.txt_news_title);
            txt_news_desc = (TextView) view.findViewById(R.id.txt_news_desc);
            lnr_news = (LinearLayout) view.findViewById(R.id.lnr_news);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            lnr_news.setOnClickListener(this);
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
                .inflate(R.layout.list_item_news, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsModel model = list.get(position);
        holder.txt_news_title.setText(model.getTitle());
        holder.txt_news_desc.setText(model.getDescription());
        Picasso.with(context).load(list.get(position).getUrlToImage()).into(holder.imguser);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

