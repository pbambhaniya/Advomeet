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
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityAdvocateDiaryDetail;
import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.Activity.ActivityDictionary;
import com.multipz.atmiyalawlab.Activity.ActivityDrafiting;
import com.multipz.atmiyalawlab.Activity.ActivityNews;
import com.multipz.atmiyalawlab.Activity.ActivityNewsDetail;
import com.multipz.atmiyalawlab.Activity.ActivitySubscriptionPlan;
import com.multipz.atmiyalawlab.Activity.JudgementDetailActivity;
import com.multipz.atmiyalawlab.Activity.JudjmentListActivity;
import com.multipz.atmiyalawlab.Adapter.BannerAdapter;
import com.multipz.atmiyalawlab.Adapter.JudgementAdapter;
import com.multipz.atmiyalawlab.Adapter.LawyerTopAdapter;
import com.multipz.atmiyalawlab.Adapter.NewsAdapter;
import com.multipz.atmiyalawlab.Model.BannerModel;
import com.multipz.atmiyalawlab.Model.JudjmentModel;
import com.multipz.atmiyalawlab.Model.ModelLawyerTop;
import com.multipz.atmiyalawlab.Model.NewsModel;
import com.multipz.atmiyalawlab.Model.TopLawyerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityBookmarkList;
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
public class Dashboard extends Fragment implements ItemClickListener {

    private ViewPager pagerBanner;
    private CircleIndicator indicator;
    private RecyclerView recyclerview_topLawyer, recyclerview_topLawyer_news;
    private List<ModelLawyerTop> list = new ArrayList<>();
    private LawyerTopAdapter adapter;
    private JudgementAdapter judgementAdapter;
    private NewsAdapter newsadapter;
    private BannerAdapter bannerAdapter;
    private Context context;
    private RelativeLayout rel_root, rel_progress;
    private ProgressDialog dialog;
    String param;
    ArrayList<TopLawyerModel> toplawyerarray;
    ArrayList<NewsModel> newsarray;
    ArrayList<BannerModel> bannerlist;
    private RecyclerView recyclerview_judgement;
    private LinearLayout txtSeeAll, lnr_SeeAll_judgement, txtSeeAll_news;
    private LinearLayout lnr_Dictionary, lnr_Drafting, lnr_lagislation, lnr_diary;
    Shared shared;
    private int currentPage = 0;
    private ArrayList<JudjmentModel> mlistJudgement;
    public static int remainig_days = 0;
    public static boolean is_expired = false;
    private CircularProgressView progressBar1;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
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
        mlistJudgement = new ArrayList<>();

        pagerBanner = (ViewPager) view.findViewById(R.id.pagerBanner);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        txtSeeAll = (LinearLayout) view.findViewById(R.id.txtSeeAll);
        lnr_SeeAll_judgement = (LinearLayout) view.findViewById(R.id.lnr_SeeAll_judgement);
        txtSeeAll_news = (LinearLayout) view.findViewById(R.id.txtSeeAll_news);

        recyclerview_topLawyer = (RecyclerView) view.findViewById(R.id.recyclerview_topLawyer);
        recyclerview_topLawyer_news = (RecyclerView) view.findViewById(R.id.recyclerview_topLawyer_news);
        lnr_Dictionary = (LinearLayout) view.findViewById(R.id.lnr_Dictionary);
        lnr_Drafting = (LinearLayout) view.findViewById(R.id.lnr_Drafting);
        lnr_lagislation = (LinearLayout) view.findViewById(R.id.lnr_lagislation);
        lnr_diary = (LinearLayout) view.findViewById(R.id.lnr_diary);
        recyclerview_judgement = (RecyclerView) view.findViewById(R.id.recyclerview_judgement);
        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);

        progressBar1 = (CircularProgressView) view.findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) view.findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

    }


    private void init() {
//        mCustomPagerAdapter = new BannerAdapter(getActivity(),);
//        pagerBanner.setAdapter(mCustomPagerAdapter);
        ActivityDashboard.txt_name_title_lawyer.setText(getResources().getString(R.string.dashboard));

        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActivityBookmarkList.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        lnr_Dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActivityDictionary.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });

        lnr_Drafting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActivityDrafiting.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        lnr_lagislation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        txtSeeAll_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNews.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        lnr_SeeAll_judgement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JudjmentListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        lnr_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActivityAdvocateDiaryDetail.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        getDashboard();
        setTimeBanner();


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

    private void getDashboard() {

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
                dialog.dismiss();
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
                        newsadapter.setClickListener(Dashboard.this);


                        JSONArray jujment = data.getJSONArray("judgement");

                        for (int i = 0; i < jujment.length(); i++) {
                            JSONObject j = jujment.getJSONObject(i);
                            JudjmentModel model = new JudjmentModel();
                            model.setJudgement_id(j.getString("judgement_id"));
                            model.setNumcites(j.getString("numcites"));
                            model.setTitle(j.getString("title"));
                            model.setDoctype(j.getString("doctype"));
                            model.setFragment(j.getString("fragment"));
                            model.setDocsource(j.getString("docsource"));
                            model.setDocsize(j.getString("docsize"));
                            model.setPublishdate(j.getString("publishdate"));
                            model.setTid(j.getString("tid"));
                            mlistJudgement.add(model);

                        }
                        judgementAdapter = new JudgementAdapter(mlistJudgement, context);
                        //  recyclerview_judgement.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
                            @Override
                            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                                    private static final float SPEED = 2000f;

                                    @Override
                                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                        return SPEED / displayMetrics.densityDpi;
                                    }
                                };
                                smoothScroller.setTargetPosition(position);
                                startSmoothScroll(smoothScroller);
                            }

                        };

                        recyclerview_judgement.setLayoutManager(layoutManager);
                        recyclerview_judgement.setAdapter(judgementAdapter);
                        autoScroll();
                        judgementAdapter.setClickListener(Dashboard.this);

                        JSONArray banner = data.getJSONArray("banner");
                        for (int b = 0; b < 4; b++) {
                            JSONObject bannerObj = banner.getJSONObject(b);
                            BannerModel bannerModel = new BannerModel();
                            bannerModel.setId(bannerObj.getString("id"));
                            bannerModel.setImageurl(bannerObj.getString("imageurl"));
                            bannerlist.add(bannerModel);
                        }
                        bannerAdapter = new BannerAdapter(context, bannerlist);
                        pagerBanner.setAdapter(bannerAdapter);
                        indicator.setViewPager(pagerBanner);

                        JSONArray subscription = data.getJSONArray("subscription");
                        for (int s = 0; s <= subscription.length(); s++) {
                            JSONObject subObj = subscription.getJSONObject(s);
                            int ah_user_subscription_id = subObj.getInt("ah_user_subscription_id");
                            boolean is_subscribe = subObj.getBoolean("is_subscribe");
                           /* if (!is_subscribe) {
                                Intent i = new Intent(getActivity(), ActivitySubscriptionPlan.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }*/

                            boolean is_verified = subObj.getBoolean("is_verified");
                            remainig_days = subObj.getInt("remainig_days");
                            is_expired = subObj.getBoolean("is_expired");
                            if (remainig_days <= 15) {
                                ActivityDashboard.txtExpire.setText("Subscription expires in " + Dashboard.remainig_days + " days");
                            } else if (remainig_days == 1) {
                                ActivityDashboard.txtExpire.setText("Subscription expires in " + Dashboard.remainig_days + " day");
                            } else if (remainig_days == 0) {
                                ActivityDashboard.txtExpire.setText("Plan has Expire");
                            } else {
                                ActivityDashboard.txtExpire.setVisibility(View.GONE);
                            }
                            boolean is_paid = subObj.getBoolean("is_paid");
                        }


                    } else if (status.contentEquals("0")) {
                        Toaster.getToast(getActivity(), msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject object = new JSONObject();
                    JSONObject body = new JSONObject();
                    object.put("action", Config.dashboard);
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
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


    public void autoScroll() {
        final int speedScroll = 6000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 1;

            @Override
            public void run() {
                if (count == judgementAdapter.getItemCount()) {
                    count = count + 2;
                    recyclerview_judgement.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                } else if (count < judgementAdapter.getItemCount()) {
                    count = count + 2;
                    recyclerview_judgement.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                } else {
                    //recyclerview_judgement.smoothScrollToPosition(count);
                    recyclerview_judgement.scrollToPosition(0);
                    count = 1;
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    @Override
    public void itemClicked(View view, int position) {

        if (view.getId() == R.id.rel_news) {
            Intent intent = new Intent(getActivity(), ActivityNewsDetail.class);
            intent.putExtra("url", newsarray.get(position).getUrl());
            startActivity(intent);
        } else if (view.getId() == R.id.lnr_judgement) {
            Intent intent = new Intent(getActivity(), JudgementDetailActivity.class);
            intent.putExtra("tid", mlistJudgement.get(position).getTid());
            startActivity(intent);
        }

    }

}
