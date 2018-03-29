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
import com.multipz.atmiyalawlab.Adapter.FilterDictionaryAdapter;
import com.multipz.atmiyalawlab.Adapter.SearchTextFilterDictionaryAdapter;
import com.multipz.atmiyalawlab.Model.DictionaryModel;
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

public class ActivityDictionary extends AppCompatActivity implements ItemClickListener {

    private ImageView img_back;
    private RecyclerView listviewTextLetter, listviewfilterDictionary;
    private RelativeLayout rel_root, rel_progress;
    private ArrayList<String> listText;
    private ArrayList<DictionaryModel> listSearchText;
    private ArrayList<String> TemplistSearchText;
    private FilterDictionaryAdapter adapter;
    private SearchTextFilterDictionaryAdapter searchadapter;
    Context context;
    Shared shared;
    String name, param;
    ProgressDialog dialog;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        context = this;
        shared = new Shared(context);
        TemplistSearchText = new ArrayList<>();
        reference();
        init();

    }

    private void reference() {
        listSearchText = new ArrayList<DictionaryModel>();
        img_back = (ImageView) findViewById(R.id.img_back);
        listviewTextLetter = (RecyclerView) findViewById(R.id.listviewTextLetter);
        listviewfilterDictionary = (RecyclerView) findViewById(R.id.listviewfilterDictionary);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


    }


    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SelectDictionaryText();

        if (Constant_method.checkConn(context)) {
            getDictionary("A");
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }


    }

    private void SelectDictionaryText() {
        listText = new ArrayList<String>();
        listText.add("A");
        listText.add("B");
        listText.add("C");
        listText.add("D");
        listText.add("E");
        listText.add("F");
        listText.add("G");
        listText.add("H");
        listText.add("I");
        listText.add("J");
        listText.add("K");
        listText.add("L");
        listText.add("N");
        listText.add("M");
        listText.add("O");
        listText.add("P");
        listText.add("Q");
        listText.add("R");
        listText.add("S");
        listText.add("T");
        listText.add("U");
        listText.add("V");
        listText.add("W");
        listText.add("X");
        listText.add("Y");
        listText.add("Z");
        adapter = new FilterDictionaryAdapter(getApplicationContext(), listText);
        listviewTextLetter.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listviewTextLetter.setAdapter(adapter);
        adapter.setClickListener(this);


    }


    @Override
    public void itemClicked(View view, int position) {

        if (view.getId() == R.id.txtFilterDictionary) {
            name = listText.get(position);
            if (Constant_method.checkConn(context)) {
                getDictionary(name);
            } else {
                Toaster.getToast(context, ErrorMessage.NoInternet);
            }


        } else if (view.getId() == R.id.lnr_data) {
            Intent intent = new Intent(ActivityDictionary.this, DictionaryDetail.class);
            intent.putExtra("title", listSearchText.get(position).getTitle());
            intent.putExtra("des", listSearchText.get(position).getDescription());
            intent.putExtra("searchText", listSearchText);
            intent.putExtra("pos", position);
            startActivity(intent);
        }


    }

    private void getDictionary(final String name) {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String msg = "", status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONArray textarray = object.getJSONArray("data");
                        listSearchText.clear();
                        for (int i = 0; i < textarray.length(); i++) {
                            JSONObject text = textarray.getJSONObject(i);
                            DictionaryModel model = new DictionaryModel();
                            model.setAh_dictionary_id(text.getString("ah_dictionary_id"));
                            model.setTitle(text.getString("title"));
                            model.setDescription(text.getString("description"));
                            listSearchText.add(model);

                        }

                        searchadapter = new SearchTextFilterDictionaryAdapter(context, listSearchText);
                        listviewfilterDictionary.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        listviewfilterDictionary.setAdapter(searchadapter);
                        searchadapter.setClickListener(ActivityDictionary.this);
                        listviewfilterDictionary.setNestedScrollingEnabled(false);

                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);

                        listSearchText.clear();
                        searchadapter = new SearchTextFilterDictionaryAdapter(context, listSearchText);
                        listviewfilterDictionary.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        listviewfilterDictionary.setAdapter(searchadapter);
                        searchadapter.setClickListener(ActivityDictionary.this);
                        listviewfilterDictionary.setNestedScrollingEnabled(false);
                        Toaster.getToast(context, "" + msg);
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
                    main.put("action", Config.getDictionary);
                    JSONObject body = new JSONObject();
                    body.put("title", name);
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
