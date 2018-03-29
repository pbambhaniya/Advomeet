package com.multipz.atmiyalawlab.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Model.CourtModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 22-01-2018.
 */

public class SettingCourtAdapter extends RecyclerView.Adapter<SettingCourtAdapter.MyViewHolder> {
    private List<CourtModel> list;
    private ItemClickListener clickListener;
    private Context context;
    private CircularProgressView progressBar1;
    Shared shared;
    RelativeLayout rel_progress;
    String ah_lawyer_court_id, ah_court_id, ah_users_id, param;
    ProgressDialog dialog;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox cb_court;

        public MyViewHolder(View view) {
            super(view);
            cb_court = (CheckBox) view.findViewById(R.id.cb_court_list);
            progressBar1 = (CircularProgressView) view.findViewById(R.id.progressBar1);
            rel_progress = (RelativeLayout) view.findViewById(R.id.rel_progress);
            progressBar1.startAnimation();
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


    public SettingCourtAdapter(Context context, List<CourtModel> list) {
        this.context = context;
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
    public SettingCourtAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_court_list, parent, false);

        return new SettingCourtAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CourtModel model = list.get(position);
        holder.cb_court.setText(model.getCourt_name());
        holder.cb_court.setButtonDrawable(R.drawable.check_box);
        // holder.cb_court.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.check_box));
//        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.check_box);
//        buttonDrawable.mutate();
//        holder.cb_court.setButtonDrawable(gerDrawable(context, R.drawable.check_box));
        // holder.cb_court.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.check_box));

        if (!model.isIs_selected()) {
            model.setIs_selected(false);
            holder.cb_court.setChecked(false);
        } else {
            model.setIs_selected(true);
            holder.cb_court.setChecked(true);
        }

        holder.cb_court.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ah_lawyer_court_id = model.getAh_lawyer_court_id();
                ah_court_id = model.getAh_court_id();
                ah_users_id = shared.getUserId();
                addCourt(ah_lawyer_court_id, ah_court_id, ah_users_id);
               /* if (!b) {

                } else {
                    addCourt(ah_lawyer_court_id, ah_court_id, ah_users_id);
                }*/
            }
        });

    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Drawable gerDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    private void addCourt(final String ah_lawyer_court_id, final String ah_court_id, final String ah_users_id) {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
//        dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(context, "" + msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.addLawyerCourt);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_court_id", ah_lawyer_court_id);
                    body.put("ah_users_id", ah_users_id);
                    body.put("ah_court_id", ah_court_id);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}