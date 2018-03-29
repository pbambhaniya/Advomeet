package com.multipz.atmiyalawlab.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.multipz.atmiyalawlab.Adapter.DraftingDetailAdapter;
import com.multipz.atmiyalawlab.Adapter.DraftingTypeAdapter;
import com.multipz.atmiyalawlab.Model.ModelDraftingDetail;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.DownloadTask;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.multipz.atmiyalawlab.Util.UtilityPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDraftingDetail extends AppCompatActivity implements ItemClickListener {
    private RelativeLayout rel_root, rel_progress;
    private RecyclerView listDraftingDetail;
    private ArrayList<ModelDraftingDetail> draftDetailtypeList;
    private DraftingDetailAdapter adapter;
    private Context mContext;
    private Shared shared;
    private ProgressDialog dialog;
    private ImageView img_back;
    private String param = "";
    private int ah_drafting_type_id;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafting_detail);

        mContext = this;
        shared = new Shared(mContext);

        draftDetailtypeList = new ArrayList<>();

        reference();
        init();

    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        listDraftingDetail = (RecyclerView) findViewById(R.id.listDraftingDetail);
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

        ah_drafting_type_id = getIntent().getIntExtra("ah_drafting_type_id", 0);
        if (Constant_method.checkConn(mContext)) {
            getDraftingDetail();
        } else {
            Toaster.getToast(mContext, ErrorMessage.NoInternet);
        }

    }

    private void getDraftingDetail() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
//        dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status == 1) {
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        JSONArray textarray = object.getJSONArray("data");
                        for (int i = 0; i < textarray.length(); i++) {
                            JSONObject text = textarray.getJSONObject(i);
                            ModelDraftingDetail model = new ModelDraftingDetail();
                            model.setDrafting_id(text.getInt("drafting_id"));
                            model.setAh_drafting_subtype_id(text.getInt("ah_drafting_subtype_id"));
                            model.setAh_drafting_subtype(text.getString("ah_drafting_subtype"));
                            model.setDrafting_doc_url(text.getString("drafting_doc_url"));
                            draftDetailtypeList.add(model);
                        }

                        adapter = new DraftingDetailAdapter(mContext, draftDetailtypeList);
                        listDraftingDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        listDraftingDetail.setAdapter(adapter);
                        listDraftingDetail.setNestedScrollingEnabled(false);
                        adapter.setClickListener(ActivityDraftingDetail.this);


                    } else {
//                        dialog.dismiss();
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
//                dialog.hide();

                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getDraftingDetails);
                    JSONObject body = new JSONObject();
                    body.put("ah_drafting_type_id", ah_drafting_type_id);
                    main.put("body", body);
                    param = main.toString();
                    params.put("json", param);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void itemClicked(View view, int position) {
        ModelDraftingDetail model = draftDetailtypeList.get(position);
        if (checkPermission()) {
            new DownloadTask(mContext, model.getDrafting_doc_url());
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Snackbar.make(findViewById(R.id.rel_root), "Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}

