package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.multipz.atmiyalawlab.Model.CasesModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 08-12-2017.
 */

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.MyViewHolder> {
    private List<CasesModel> list;
    private CasesAdapter.ClickListener clickListener;
    private Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_case_Title, txt_lawyer_type, txt_case_name, txt_city, txtposted_date;
        private RelativeLayout rel_root;
        private CircleImageView img_lawyer;

        public MyViewHolder(View view) {
            super(view);
            txt_case_Title = (TextView) view.findViewById(R.id.txt_case_Title);
            txt_lawyer_type = (TextView) view.findViewById(R.id.txt_lawyer_type);
            txt_case_name = (TextView) view.findViewById(R.id.txt_case_name);
            txt_city = (TextView) view.findViewById(R.id.txt_city);
            txtposted_date = (TextView) view.findViewById(R.id.txtposted_date);
            img_lawyer = (CircleImageView) view.findViewById(R.id.img_lawyer);
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

    public CasesAdapter(Context context, List<CasesModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(CasesAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void remove(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, list.size());
    }

    @Override
    public CasesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cases, parent, false);

        return new CasesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CasesAdapter.MyViewHolder holder, int position) {
        CasesModel model = list.get(position);
        int idstatus = model.getStatus_id();
        holder.txt_case_Title.setText(model.getTitle());
        holder.txt_lawyer_type.setText(model.getCase_type());
        holder.txt_case_name.setText("Posted By " + model.getCased_by_name());
        holder.txt_city.setText(model.getCity_name());
        holder.txtposted_date.setText("Posted On " + model.getCreated_on());
        Picasso.with(context).load(list.get(position).getProfile_img()).into(holder.img_lawyer);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
