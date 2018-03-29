package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.NotificationModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 04-12-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<NotificationModel> list;
    private ClickListener clickListener;
    Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView img;
        public TextView txt_person_name, txt_time;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);

            img = (CircleImageView) view.findViewById(R.id.img);
            txt_person_name = (TextView) view.findViewById(R.id.txt_person_name);
            txt_time = (TextView) view.findViewById(R.id.txt_time);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public NotificationAdapter(Context context, List<NotificationModel> expertsList) {
        this.context = context;
        this.list = expertsList;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationModel model = list.get(position);
        holder.txt_person_name.setText(model.getActivity_message());
        holder.txt_time.setText(model.getCreate_date());
        String img = model.getAh_requested_profile_img();
        Picasso.with(context).load(img).into(holder.img);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
