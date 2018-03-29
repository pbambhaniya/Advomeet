package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.atmiyalawlab.Model.DiaryModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ItemClickListener;

import java.util.List;

/**
 * Created by Admin on 10-01-2018.
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {
    private List<DiaryModel> expertsList;
    private ItemClickListener clickListener;
    private Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_type, txt_name, txt_time;
        private LinearLayout rel_root;
        private ImageView viewDairyDetail;

        public MyViewHolder(View view) {
            super(view);
            txt_type = (TextView) view.findViewById(R.id.txt_type_diary);
            txt_name = (TextView) view.findViewById(R.id.txt_name_diary);
            txt_time = (TextView) view.findViewById(R.id.txt_time_diary);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            viewDairyDetail = (ImageView) view.findViewById(R.id.viewDairyDetail);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
            viewDairyDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    public DiaryAdapter(List<DiaryModel> expertsList, Context context) {
        this.context = context;
        this.expertsList = expertsList;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary_date, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DiaryModel model = expertsList.get(position);
        holder.txt_name.setText(model.getName());
        holder.txt_type.setText(model.getDescription());
        holder.txt_time.setText(Constant_method.current_time(model.getTime()));
    }

    @Override
    public int getItemCount() {
        return expertsList.size();
    }
}
