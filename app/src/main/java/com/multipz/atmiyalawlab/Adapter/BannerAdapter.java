package com.multipz.atmiyalawlab.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.multipz.atmiyalawlab.Model.BannerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 05-01-2018.
 */

public class BannerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<BannerModel> list;

    public BannerAdapter(Context context, ArrayList<BannerModel> mlist) {
        this.mContext = context;
        this.list = mlist;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.banner_item, container, false);
        BannerModel model = list.get(position);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        String img = model.getImageurl();
        Picasso.with(mContext).load(list.get(position).getImageurl()).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
