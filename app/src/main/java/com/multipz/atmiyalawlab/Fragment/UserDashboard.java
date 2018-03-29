package com.multipz.atmiyalawlab.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityNews;
import com.multipz.atmiyalawlab.Activity.ActivityNewsDetail;
import com.multipz.atmiyalawlab.Adapter.BannerAdapter;
import com.multipz.atmiyalawlab.Adapter.LawyerTopAdapter;
import com.multipz.atmiyalawlab.Adapter.NewsAdapter;
import com.multipz.atmiyalawlab.Model.BannerModel;
import com.multipz.atmiyalawlab.Model.ModelLawyerTop;
import com.multipz.atmiyalawlab.Model.NewsModel;
import com.multipz.atmiyalawlab.Model.TopLawyerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityAskLegalAdvise;
import com.multipz.atmiyalawlab.User.ActivityAskQuestionView;
import com.multipz.atmiyalawlab.User.ActivityBookmarkList;
import com.multipz.atmiyalawlab.User.ActivityLawyerDetail;
import com.multipz.atmiyalawlab.User.ActivityPostYourCase;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDashboard extends Fragment implements ItemClickListener {

    private ViewPager pagerBanner;
    private CircleIndicator indicator;
    private BannerAdapter mCustomPagerAdapter;
    private RecyclerView recyclerview_topLawyer, recyclerview_topLawyer_news;
    private List<ModelLawyerTop> list = new ArrayList<>();
    private Context context;
    private RelativeLayout rel_root, rel_progress;
    private LinearLayout lnr_ask_legal_advise, lnr_post_your_case;
    private Button btn_search_more;
    private ProgressDialog dialog;
    private LawyerTopAdapter adapter;
    private NewsAdapter newsadapter;
    String param;
    ArrayList<TopLawyerModel> toplawyerarray;
    ArrayList<NewsModel> newsarray;
    Shared shared;
    TextView txtSeeAll_news, txtSeeAll;
    ArrayList<BannerModel> bannerlist;
    private BannerAdapter bannerAdapter;
    private int currentPage = 0;
    RelativeLayout rel_post_case;
    private CircularProgressView progressBar1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);

        context = this.getActivity();
        shared = new Shared(context);
        reference(view);
        init();


        return view;
    }


    private void reference(View view) {
        toplawyerarray = new ArrayList<>();
        newsarray = new ArrayList<>();
        bannerlist = new ArrayList<>();

        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        recyclerview_topLawyer = (RecyclerView) view.findViewById(R.id.recyclerview_topLawyer);
        recyclerview_topLawyer_news = (RecyclerView) view.findViewById(R.id.recyclerview_topLawyer_news);
        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        lnr_ask_legal_advise = (LinearLayout) view.findViewById(R.id.lnr_ask_legal_advise);
        lnr_post_your_case = (LinearLayout) view.findViewById(R.id.lnr_post_your_case);
//        btn_search_more = (Button) view.findViewById(R.id.btn_search_more);
        txtSeeAll_news = (TextView) view.findViewById(R.id.txtSeeAll_news);
        txtSeeAll = (TextView) view.findViewById(R.id.txtSeeAll);
        rel_post_case = (RelativeLayout) view.findViewById(R.id.rel_post_case);
        pagerBanner = (ViewPager) view.findViewById(R.id.pagerBanner);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        rel_progress = (RelativeLayout) view.findViewById(R.id.rel_progress);
        progressBar1 = (CircularProgressView) view.findViewById(R.id.progressBar1);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
        getUserDashboard();
        setTimeBanner();
    }

    private void init() {
       /* mCustomPagerAdapter = new BannerAdapter(context);
        pagerBannerUser.setAdapter(mCustomPagerAdapter);*/
        indicator.setViewPager(pagerBanner);
        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityBookmarkList.class);
                startActivity(intent);
            }
        });
        lnr_ask_legal_advise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAskLegalAdvise.class);
                startActivity(intent);
            }
        });
        txtSeeAll_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNews.class);
                startActivity(intent);
            }
        });

        rel_post_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityPostYourCase.class);
                startActivity(intent);

            }
        });

    }

    private void setTimeBanner() {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == bannerlist.size()) {
                    currentPage = 0;
                }
                pagerBanner.setCurrentItem(currentPage++, true);
            }
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 2000);
    }


    private void getUserDashboard() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        //  dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);

                String msg, status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray toplawyer = data.getJSONArray("top_lawyers");
                        for (int i = 0; i < toplawyer.length(); i++) {
                            JSONObject c = toplawyer.getJSONObject(i);
                            TopLawyerModel model = new TopLawyerModel();
                            model.setAh_users_id(c.getString("ah_lawyer_id"));
                            model.setFull_name(c.getString("full_name"));
                            model.setProfile_img(c.getString("profile_img"));
                            model.setTotal_rating(c.getString("total_rating"));
                            toplawyerarray.add(model);
                        }
                        adapter = new LawyerTopAdapter(toplawyerarray, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        recyclerview_topLawyer.setLayoutManager(mLayoutManager);
                        recyclerview_topLawyer.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_topLawyer.setAdapter(adapter);
                        recyclerview_topLawyer.setNestedScrollingEnabled(false);
                        adapter.setClickListener(UserDashboard.this);


                        JSONArray news = data.getJSONArray("news");
                        shared.putString(Config.News, news.toString());
                        for (int i = 0; i < 5; i++) {
                            JSONObject mainobject = news.getJSONObject(i);
                            JSONObject sourceobjects = mainobject.getJSONObject("source");
                            NewsModel newsModel = new NewsModel();
                            newsModel.setId(sourceobjects.getString("id"));
                            newsModel.setName(sourceobjects.getString("name"));
                            newsModel.setAuthor(mainobject.getString("author"));
                            newsModel.setTitle(mainobject.getString("title"));
                            newsModel.setDescription(mainobject.getString("description"));
                            newsModel.setUrl(mainobject.getString("url"));
                            newsModel.setUrlToImage(mainobject.getString("urlToImage"));
                            newsModel.setPublishedAt(mainobject.getString("publishedAt"));
                            newsarray.add(newsModel);
                        }
                        newsadapter = new NewsAdapter(newsarray, context);
                        RecyclerView.LayoutManager mLayoutManagers = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        recyclerview_topLawyer_news.setLayoutManager(mLayoutManagers);
                        recyclerview_topLawyer_news.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_topLawyer_news.setAdapter(newsadapter);
                        recyclerview_topLawyer_news.setNestedScrollingEnabled(false);
                        newsadapter.setClickListener(UserDashboard.this);


                        JSONArray banner = data.getJSONArray("banner");
                        for (int b = 0; b < banner.length(); b++) {
                            JSONObject bannerObj = banner.getJSONObject(b);
                            BannerModel bannerModel = new BannerModel();
                            bannerModel.setId(bannerObj.getString("id"));
                            bannerModel.setImageurl(bannerObj.getString("imageurl"));
                            bannerlist.add(bannerModel);
                        }
                        bannerAdapter = new BannerAdapter(context, bannerlist);
                        pagerBanner.setAdapter(bannerAdapter);
                        indicator.setViewPager(pagerBanner);

                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(context, msg);
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
                    JSONObject object = new JSONObject();
                    object.put("action", Config.dashboard);
                    object.put("ah_users_id", shared.getUserId());
                    object.put("type", shared.getUsertype());
                    JSONObject body = new JSONObject();
                    object.put("body", body);
                    param = object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"" + Config.getcountry + "\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void itemClicked(View view, int position) {
        if (view.getId() == R.id.rel_news) {
            Intent intent = new Intent(getActivity(), ActivityNewsDetail.class);
            intent.putExtra("url", newsarray.get(position).getUrl());
            startActivity(intent);
        } else if (view.getId() == R.id.top_lawyer) {
            if (shared.getUsertype().contentEquals("U")) {
                TopLawyerModel model = toplawyerarray.get(position);
                Intent i = new Intent(context, ActivityLawyerDetail.class);
                i.putExtra("lawyer_id", model.getAh_users_id());
                startActivity(i);
            }

        }
    }


}
