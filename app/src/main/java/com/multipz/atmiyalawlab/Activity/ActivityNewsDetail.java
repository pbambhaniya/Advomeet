package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.R;

public class ActivityNewsDetail extends AppCompatActivity {
    WebView wv_news;
    ProgressDialog pd;
    private ImageView img_back;
    RelativeLayout rel_progress;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        wv_news = (WebView) findViewById(R.id.wv_news);
        img_back = (ImageView) findViewById(R.id.img_back);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        pd = new ProgressDialog(ActivityNewsDetail.this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();
//        rel_progress.setVisibility(View.VISIBLE);

        wv_news.setWebViewClient(new MyBrowser());
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        String url = getIntent().getStringExtra("url");
        wv_news.loadUrl(url);
        wv_news.getSettings().setLoadsImagesAutomatically(true);
        wv_news.getSettings().setJavaScriptEnabled(true);
        wv_news.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            rel_progress.setVisibility(View.GONE);
            pd.dismiss();
        }
    }
}
