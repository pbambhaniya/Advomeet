package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.DraftingTypeAdapter;
import com.multipz.atmiyalawlab.Adapter.SearchTextFilterDictionaryAdapter;
import com.multipz.atmiyalawlab.Model.DictionaryModel;
import com.multipz.atmiyalawlab.Model.ModelDraftingType;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDrafiting extends AppCompatActivity implements ItemClickListener {

    private RelativeLayout rel_root, rel_progress;
    private RecyclerView listDrafting;
    private ArrayList<ModelDraftingType> drafttypeList;
    private DraftingTypeAdapter adapter;
    private Context mContext;
    private Shared shared;
    private ProgressDialog dialog;
    private ImageView img_back;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafiting);
        mContext = this;
        shared = new Shared(mContext);

        drafttypeList = new ArrayList<>();

        reference();
        init();
    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        listDrafting = (RecyclerView) findViewById(R.id.listDrafting);
        img_back = (ImageView) findViewById(R.id.img_back);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Constant_method.checkConn(mContext)) {
            getDraftingType();
        } else {
            Toaster.getToast(mContext, ErrorMessage.NoInternet);
        }


    }

    private void getDraftingType() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //dialog.show();
        rel_progress.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                int status;
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONArray textarray = object.getJSONArray("data");
                        for (int i = 0; i < textarray.length(); i++) {
                            JSONObject text = textarray.getJSONObject(i);
                            ModelDraftingType model = new ModelDraftingType();
                            model.setAh_drafting_type_id(text.getInt("ah_drafting_type_id"));
                            model.setAh_drafting_type(text.getString("ah_drafting_type"));
                            model.setParentid(text.getInt("parentid"));
                            drafttypeList.add(model);

                        }
                        adapter = new DraftingTypeAdapter(mContext, drafttypeList);
                        listDrafting.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        listDrafting.setAdapter(adapter);

                        listDrafting.setNestedScrollingEnabled(false);
                        adapter.setClickListener(ActivityDrafiting.this);


                    } else {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(mContext, "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                rel_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getDraftingType);
                    params.put("json", main.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void itemClicked(View View, int position) {
        ModelDraftingType modelDraftingType = drafttypeList.get(position);
        int ah_drafting_type_id = modelDraftingType.getAh_drafting_type_id();

        Intent i = new Intent(ActivityDrafiting.this, ActivityDraftingDetail.class);
        i.putExtra("ah_drafting_type_id", ah_drafting_type_id);
        startActivity(i);
    }
}
