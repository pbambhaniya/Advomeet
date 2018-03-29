package com.multipz.atmiyalawlab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;

import java.util.ArrayList;

/**
 * Created by Admin on 31-07-2017.
 */

public class SpinnerAdapter extends BaseAdapter {

    Context c;

    ArrayList<SpinnerModel> objects;

    public SpinnerAdapter(Context context, ArrayList<SpinnerModel> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
       /* int count = getCount();
        return count > 0 ? count - 1 : count;*/
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        SpinnerModel cur_obj = objects.get(position);
        LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View row = mInflater.inflate(R.layout.spinner_item, null);

        // View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.company);

//        label.setTypeface(Application.fontOxygenRegular);
        label.setText(cur_obj.getName());

        return row;
    }
}
