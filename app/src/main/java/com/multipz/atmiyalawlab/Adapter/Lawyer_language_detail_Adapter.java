package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Model.LanguageModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import java.util.List;

/**
 * Created by Admin on 05-02-2018.
 */

public class Lawyer_language_detail_Adapter extends RecyclerView.Adapter<Lawyer_language_detail_Adapter.MyViewHolder> {
    private List<LanguageModel> list;
    private ItemClickListener clickListener;
    private Context context;
    Shared shared;
    private CircularProgressView progressBar1;
    RelativeLayout rel_progress;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_lan_type;

        public MyViewHolder(View view) {
            super(view);
            txt_lan_type = (TextView) view.findViewById(R.id.txt_lan_type);
            progressBar1 = (CircularProgressView) view.findViewById(R.id.progressBar1);
            rel_progress = (RelativeLayout) view.findViewById(R.id.rel_progress);
            progressBar1.startAnimation();
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    public Lawyer_language_detail_Adapter(Context mcontext, List<LanguageModel> list) {
        this.context = mcontext;
        this.list = list;
        this.shared = new Shared(context);
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
    public Lawyer_language_detail_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lawyer_detail_language_list, parent, false);

        return new Lawyer_language_detail_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Lawyer_language_detail_Adapter.MyViewHolder holder, int position) {
        final LanguageModel model = list.get(position);
        holder.txt_lan_type.setText(model.getLanguage_name());



    }
    @Override
    public int getItemCount() {
        return list.size();
    }



}
