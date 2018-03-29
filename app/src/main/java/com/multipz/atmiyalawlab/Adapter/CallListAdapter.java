package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.CallListModel;
import com.multipz.atmiyalawlab.Model.CasesModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Admin on 16-02-2018.
 */

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.MyViewHolder> {
    private List<CallListModel> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_call;
        TextView txt_name, txt_call_type;

        public MyViewHolder(View view) {
            super(view);
            image_call = (ImageView) view.findViewById(R.id.image_call);
            txt_name = (TextView) view.findViewById(R.id.txt_call);
            txt_call_type = (TextView) view.findViewById(R.id.txt_call_type);
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

    public CallListAdapter(Context context, List<CallListModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public CallListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        try {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_call_list, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CallListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallListAdapter.MyViewHolder holder, int position) {
        CallListModel model = list.get(position);

        holder.txt_name.setText(model.getLawyer_name());
        holder.txt_call_type.setText(model.getCommunication_type());
        Picasso.with(context).load(list.get(position).getProfile_img()).into(holder.image_call);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
