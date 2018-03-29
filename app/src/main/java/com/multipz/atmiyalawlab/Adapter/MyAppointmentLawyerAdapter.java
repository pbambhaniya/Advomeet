package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class MyAppointmentLawyerAdapter extends RecyclerView.Adapter<MyAppointmentLawyerAdapter.MyViewHolder> {
    private List<MyAppointmentList> list;
    private ItemClickListener clickListener;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_name, txt_agenda, txtdate, txttime, txtPaidUnPaid, txtCancel, txtshowdetail, txtReject, txtApprove;
        private CircleImageView img_user;
        private LinearLayout rel_root, lnr_btn_Cancel_detail, lnr_rejected;
        private ImageView img_paid_unpaid;

        public MyViewHolder(View view) {
            super(view);
            img_user = (CircleImageView) view.findViewById(R.id.img_user);
            txt_agenda = (TextView) view.findViewById(R.id.txt_agenda);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txtdate = (TextView) view.findViewById(R.id.txtdate);
            txttime = (TextView) view.findViewById(R.id.txttime);
            txtCancel = (TextView) view.findViewById(R.id.txtCancel);
            txtshowdetail = (TextView) view.findViewById(R.id.txtshowdetail);

            txtApprove = (TextView) view.findViewById(R.id.txtApprove);
            lnr_btn_Cancel_detail = (LinearLayout) view.findViewById(R.id.lnr_btn_Cancel_detail);
            lnr_rejected = (LinearLayout) view.findViewById(R.id.lnr_rejected);
            txtReject = (TextView) view.findViewById(R.id.txtReject);
            img_paid_unpaid = (ImageView) view.findViewById(R.id.img_paid_unpaid);

            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
            txtshowdetail.setOnClickListener(this);
            txtCancel.setOnClickListener(this);
            txtReject.setOnClickListener(this);
            txtApprove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    public MyAppointmentLawyerAdapter(List<MyAppointmentList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyAppointmentLawyerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_appoinment_lawyer_side, parent, false);
        return new MyAppointmentLawyerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyAppointmentLawyerAdapter.MyViewHolder holder, int position) {
        MyAppointmentList model = list.get(position);
        holder.txt_name.setText(model.getFull_name());
        holder.txt_agenda.setText(model.getSubject());
        holder.txtdate.setText(model.getAppointment_date());
        holder.txttime.setText(model.getStart_time() + " To " + model.getEnd_time());

        int is_payment = model.getIs_payment();
        if (is_payment == 0) {
            holder.img_paid_unpaid.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.unpaid));
        } else if (is_payment == 1) {
            holder.img_paid_unpaid.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.paid));
        }

        Picasso.with(context).load(model.getProfile_img()).into(holder.img_user);
        if (model.getStatus_id() == 1) {
            holder.txtApprove.setVisibility(View.VISIBLE);
            holder.lnr_btn_Cancel_detail.setVisibility(View.VISIBLE);
            holder.img_paid_unpaid.setVisibility(View.GONE);
        } else if (model.getStatus_id() == 2) {
            // holder.txtApprove.setVisibility(View.VISIBLE);
            holder.lnr_rejected.setVisibility(View.GONE);
            holder.lnr_btn_Cancel_detail.setVisibility(View.VISIBLE);
        } else if (model.getStatus_id() == 3) {
            holder.txtApprove.setVisibility(View.GONE);
            holder.lnr_btn_Cancel_detail.setVisibility(View.GONE);
            holder.lnr_rejected.setVisibility(View.VISIBLE);
            holder.img_paid_unpaid.setVisibility(View.GONE);
        } else if (model.getStatus_id() == 4) {
            holder.lnr_btn_Cancel_detail.setVisibility(View.GONE);
            holder.lnr_rejected.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
