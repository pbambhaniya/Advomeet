package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.atmiyalawlab.Adapter.AskQueViewAdapter;
import com.multipz.atmiyalawlab.Model.AskQuestionViewModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityAskQuestionView extends AppCompatActivity implements ItemClickListener {

    RecyclerView rv_ask_que;
    Context context;
    ProgressDialog dialog;
    String param;
    ArrayList<AskQuestionViewModel> list;
    ImageView img_back;
    RelativeLayout rel_plus;
    AskQueViewAdapter adapter;
    private RelativeLayout rel_root;
    private Shared shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question_view);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rel_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAskQuestionView.this, ActivityAskLegalAdvise.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = new ArrayList<>();
        getAsqViewApi();


    }


    private void reference() {
        rv_ask_que = (RecyclerView) findViewById(R.id.rv_ask_que);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_plus = (RelativeLayout) findViewById(R.id.rel_plus);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void getAsqViewApi() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        String tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                String Message = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        Message = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            AskQuestionViewModel model = new AskQuestionViewModel();
                                model.setQuestion_id(c.getString("question_id"));
                            model.setPosted_on(c.getString( "posted_on"));
                            model.setAnswer_id(c.getString("answer_id"));
                            model.setTitle(c.getString("title"));
                            model.setDescription(c.getString("description"));
                            model.setAnswer(c.getString("answer"));

                            list.add(model);
                        }

                        adapter = new AskQueViewAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_ask_que.setLayoutManager(mLayoutManager);
                        rv_ask_que.setItemAnimator(new DefaultItemAnimator());
                        rv_ask_que.setAdapter(adapter);
                        rv_ask_que.setNestedScrollingEnabled(false);
                    } else if (status == 0) {
//                        NodataPopup();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.d("error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.questionList);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    object.put("body", body);
                    param = object.toString();
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
    public void itemClicked(View View, int position) {

    }

}

