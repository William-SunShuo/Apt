package com.example.network;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Okhttp {

    private OkHttpClient okHttpClient = new OkHttpClient();

    public void getData(Callback callback){
        Request request = new Request.Builder().url("http://www.wanandroid.com/wxarticle/chapters/json").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
