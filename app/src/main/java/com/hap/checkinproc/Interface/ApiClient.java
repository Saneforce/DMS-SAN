package com.hap.checkinproc.Interface;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //public static String BASE_URL = "http://ff.hap.in/server/";
    public static String BASE_URL = "http://fmcg.sanfmcg.com/dmsserver/";//field
    // public static String BASE_URL = "http://primary.hap.in/server/";//distri

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Log.d("BaseURL", BASE_URL);
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
