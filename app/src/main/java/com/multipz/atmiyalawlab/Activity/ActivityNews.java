package com.multipz.atmiyalawlab.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.multipz.atmiyalawlab.Adapter.JudgementAdapter;
import com.multipz.atmiyalawlab.Adapter.NewsAdapter;
import com.multipz.atmiyalawlab.Adapter.SeeallNewsAdapter;
import com.multipz.atmiyalawlab.Fragment.Dashboard;
import com.multipz.atmiyalawlab.Model.ModelJudgement;
import com.multipz.atmiyalawlab.Model.NewsModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityNews extends AppCompatActivity implements ItemClickListener {
    private RelativeLayout rel_root;
    private ImageView img_back;
    private RecyclerView rv_news;
    private ArrayList<ModelJudgement> jlist;
    Context context;
    Shared shared;
    private ArrayList<NewsModel> newsarray;
    SeeallNewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        context = this;
        shared = new Shared(context);
        newsarray = new ArrayList<>();
        reference();
        init();

    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_back = (ImageView) findViewById(R.id.img_back);
        rv_news = (RecyclerView) findViewById(R.id.rv_news);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


    }

    private void init() {
        getNews();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getNews() {
        try {
            JSONArray array = new JSONArray(shared.getString(Config.News, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONObject sourceobject = object.getJSONObject("source");
                NewsModel newsModel = new NewsModel();
                newsModel.setId(sourceobject.getString("id"));
                newsModel.setName(sourceobject.getString("name"));
                newsModel.setAuthor(object.getString("author"));
                newsModel.setTitle(object.getString("title"));
                newsModel.setDescription(object.getString("description"));
                newsModel.setUrl(object.getString("url"));
                newsModel.setUrlToImage(object.getString("urlToImage"));
                newsModel.setPublishedAt(object.getString("publishedAt"));
                newsarray.add(newsModel);
            }

            adapter = new SeeallNewsAdapter(newsarray, context);
            RecyclerView.LayoutManager mLayoutManagers = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rv_news.setLayoutManager(mLayoutManagers);
            rv_news.setItemAnimator(new DefaultItemAnimator());
            rv_news.setAdapter(adapter);
            rv_news.setNestedScrollingEnabled(false);
            adapter.setClickListener(this);
        } catch (JSONException e) {

        }
    }

    @Override
    public void itemClicked(View View, int position) {
        Intent intent = new Intent(ActivityNews.this, ActivityNewsDetail.class);
        intent.putExtra("url", newsarray.get(position).getUrl());
        startActivity(intent);
    }
}
