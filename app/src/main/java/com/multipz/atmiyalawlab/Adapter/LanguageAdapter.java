package com.multipz.atmiyalawlab.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.multipz.atmiyalawlab.Model.LanguageModel;
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
 * Created by Admin on 18-12-2017.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    private List<LanguageModel> list;
    private ItemClickListener clickListener;
    private Context context;
    private CircularProgressView progressBar1;
    String ah_lawyer_language_id, ah_language_id, uid, param;
    Shared shared;
    ProgressDialog dialog;
    RelativeLayout rel_progress;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox cb_language;

        public MyViewHolder(View view) {
            super(view);
            cb_language = (CheckBox) view.findViewById(R.id.cb_language);
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


    public LanguageAdapter(Context mcontext, List<LanguageModel> list) {
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LanguageModel model = list.get(position);
        holder.cb_language.setText(model.getLanguage_name());
        holder.cb_language.setButtonDrawable(R.drawable.check_box);
        if (!model.isIs_selected()) {
            model.setIs_selected(false);
            holder.cb_language.setChecked(false);
        } else {
            model.setIs_selected(true);
            holder.cb_language.setChecked(true);
        }


        holder.cb_language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ah_lawyer_language_id = model.getAh_lawyer_language_id();
                ah_language_id = model.getAh_language_id();
                uid = shared.getUserId();
                addLanguage(ah_lawyer_language_id, ah_language_id, uid);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void addLanguage(final String ah_lawyer_language_id, final String ah_language_id, final String uid) {


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
                    main.put("action", Config.addLawyerLaguage);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_language_id", ah_lawyer_language_id);
                    body.put("ah_language_id", ah_language_id);
                    body.put("ah_users_id", uid);
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


}
