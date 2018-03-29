package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.multipz.atmiyalawlab.Model.ChatListModelClass;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<ChatListModelClass> list = new ArrayList<>();
    Context mcontext;
    private ItemClickListener clickListener;
    private int pos = 0;

    public ChatListAdapter(Context context, ArrayList<ChatListModelClass> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_list, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        ChatListModelClass m = list.get(position);
        holder.txtUserName.setText(m.getFull_name());
        holder.txtlastmsgShow.setText(m.getLast_msg());
        if (!m.getCreate_date().contentEquals("")) {
            holder.txtshowtime.setText(Constant_method.getTime(m.getCreate_date()));
        }

        Glide.with(mcontext).load(Config.chatProfile + "" + m.getProfile_img()).error(R.drawable.user)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userimg);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtUserName, txtdate, txtlastmsgShow, txtCountMsg, txtshowtime;
        ImageView userimg;

        public MyViewHolder(View view) {
            super(view);
            txtUserName = (TextView) view.findViewById(R.id.txtUserName);
            txtdate = (TextView) view.findViewById(R.id.txtdate);
            txtlastmsgShow = (TextView) view.findViewById(R.id.txtlastmsgShow);
            txtCountMsg = (TextView) view.findViewById(R.id.txtCountMsg);
            txtshowtime = (TextView) view.findViewById(R.id.txtshowtime);
            userimg = (ImageView) view.findViewById(R.id.userimg);
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
