package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.MyAppointmentList;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 11-12-2017.
 */

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.MyViewHolder> {
    private List<MyAppointmentList> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_name, txt_agenda, txtdate, txttime, txtPaidUnPaid, txtCancel, txtshowdetail, txtReject;
        private CircleImageView img_user;
        private LinearLayout rel_root, lnr_btn_Cancel_detail, lnr_rejected;

        public MyViewHolder(View view) {
            super(view);
            img_user = (CircleImageView) view.findViewById(R.id.img_user);
            txt_agenda = (TextView) view.findViewById(R.id.txt_agenda);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txtdate = (TextView) view.findViewById(R.id.txtdate);
            txttime = (TextView) view.findViewById(R.id.txttime);
            txtCancel = (TextView) view.findViewById(R.id.txtCancel);
            txtshowdetail = (TextView) view.findViewById(R.id.txtshowdetail);
            lnr_btn_Cancel_detail = (LinearLayout) view.findViewById(R.id.lnr_btn_Cancel_detail);
            lnr_rejected = (LinearLayout) view.findViewById(R.id.lnr_rejected);
            txtReject = (TextView) view.findViewById(R.id.txtReject);

            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
            txtshowdetail.setOnClickListener(this);
            txtCancel.setOnClickListener(this);
            txtReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    public MyAppointmentAdapter(List<MyAppointmentList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyAppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_appoinment, parent, false);
        return new MyAppointmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyAppointmentAdapter.MyViewHolder holder, int position) {
        MyAppointmentList model = list.get(position);
        holder.txt_name.setText(model.getFull_name());
        holder.txt_agenda.setText(model.getSubject());
        holder.txtdate.setText(model.getAppointment_date());
        holder.txttime.setText(model.getStart_time() + " To " + model.getEnd_time());
        Picasso.with(context).load(model.getProfile_img()).into(holder.img_user);
        if (model.getStatus_id() == 3) {
            holder.lnr_rejected.setVisibility(View.VISIBLE);
            holder.lnr_btn_Cancel_detail.setVisibility(View.GONE);
        } else {
            holder.lnr_btn_Cancel_detail.setVisibility(View.VISIBLE);
            holder.lnr_rejected.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
