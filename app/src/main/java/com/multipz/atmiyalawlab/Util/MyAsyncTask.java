package com.multipz.atmiyalawlab.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyAsyncTask extends AsyncTask<String, Void, String> {

    private String url;
    ProgressDialog pd;
    private Context context;
    private AsyncInterface asyncReference;
    private int flag;
    private String param;
    private boolean isLoadMore = false;

    public interface AsyncInterface {
        //void onResponseService(String response, int flag);
        void onResponseService(String response, int flag, ProgressDialog pd);
    }

    public MyAsyncTask(String url, Activity activity, String param, int flag) {
        this.url = url;
        context = (Context) activity;
        asyncReference = (AsyncInterface) activity;
        this.param = param;
        this.flag = flag;
//        this.isLoadMore=false;
    }

    public MyAsyncTask(String url, Activity activity, AsyncInterface asyncInterface, int flag) {
        this.url = url;
        context = (Context) activity;
        asyncReference = asyncInterface;
        this.flag = flag;
//        this.isLoadMore=false;
    }

    public MyAsyncTask(String url, Activity activity, String param, int flag, boolean isLoadMore) {
        this.url = url;
        context = (Context) activity;
        asyncReference = (AsyncInterface) activity;
        this.param = param;
        this.flag = flag;
        this.isLoadMore = isLoadMore;
    }

    public MyAsyncTask(String url, Context activity, String param, int flag, boolean isLoadMore) {
        this.url = url;
        context = (Context) activity;
        asyncReference = (AsyncInterface) activity;
        this.param = param;
        this.flag = flag;
        this.isLoadMore = isLoadMore;
    }

    //
    public MyAsyncTask(String url, Activity activity, AsyncInterface asyncInterface, int flag, boolean isLoadMore) {
        this.url = url;
        context = (Context) activity;
        asyncReference = asyncInterface;
        this.flag = flag;
        this.isLoadMore = isLoadMore;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isLoadMore)
            pd = ProgressDialog.show(context, "Wait", "Loading");
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder().add("json", param);
        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            //pd.dismiss();
            asyncReference.onResponseService(s, flag, pd);
        } catch (Exception e) {
        }
        //asyncReference.onResponseService(s, flag);
    }

}
