package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.multipz.atmiyalawlab.Model.LawyerListModel;
import com.multipz.atmiyalawlab.Model.SearchAdvocateModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 08-12-2017.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.MyViewHolder> {
    private List<LawyerListModel> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_lawyer_name, txt_lawyer_type, txt_city, txt_experiense, txt_availabilty, txt_ratting, txt_total_case, txttotalrating;
        ImageView img_favourite, imgrating_star, img_favourite_bookmark;
        private RelativeLayout rel_root;
        CircleImageView img_lawyer;

        public MyViewHolder(View view) {
            super(view);
            txt_lawyer_name = (TextView) view.findViewById(R.id.txt_lawyer_name);
            txt_city = (TextView) view.findViewById(R.id.txt_city);
            txt_experiense = (TextView) view.findViewById(R.id.txt_experiense);
            img_lawyer = (CircleImageView) view.findViewById(R.id.img_lawyer);
            txt_availabilty = (TextView) view.findViewById(R.id.txt_availabilty);
            txt_ratting = (TextView) view.findViewById(R.id.txt_ratting);
            imgrating_star = (ImageView) view.findViewById(R.id.imgrating_star);
            txt_total_case = (TextView) view.findViewById(R.id.txt_total_case);
            txttotalrating = (TextView) view.findViewById(R.id.txttotalrating);
        //    img_favourite_bookmark = (ImageView) view.findViewById(R.id.img_favourite_bookmark);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

          //  img_favourite_bookmark.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    public BookMarkAdapter(Context context, List<LawyerListModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void remove(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, list.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookmark_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LawyerListModel model = list.get(position);
        holder.txt_lawyer_name.setText(model.getFull_name());
        holder.txt_city.setText(model.getCity_name());
        holder.txt_experiense.setText("Experience " + model.getExperienceinyear() + "." + model.getExperienceinmonth() + " Years");
        String txt_availabilty = model.getIs_available();
        if (txt_availabilty.contentEquals("Not Available Today")) {
            holder.txt_availabilty.setText("Not Available Today");
            holder.txt_availabilty.setTextColor(context.getResources().getColor(R.color.not_availabilty_today));
        } else if (txt_availabilty.contentEquals("Available Today")) {
            holder.txt_availabilty.setText("Available Today");
            holder.txt_availabilty.setTextColor(context.getResources().getColor(R.color.availabilty_today));
        }

        holder.txt_ratting.setText(model.getAvg_rating());
        holder.txt_total_case.setText("Total case - " + model.getTotal_case_assign());
        holder.txttotalrating.setText("Total rating - " + model.getTotal_rating_count());


        Picasso.with(context).load(list.get(position).getProfile_img()).into(holder.img_lawyer);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
