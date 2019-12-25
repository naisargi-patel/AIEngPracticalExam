package com.example.aiengpracticalexam.network;

import com.example.aiengpracticalexam.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static RetrofitMethods retrofitMethods = createRetrofitBuilder().create(RetrofitMethods.class);

    public static RetrofitMethods getApi() {
        return retrofitMethods;
    }

    private static Retrofit createRetrofitBuilder() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getHttpClient())
                .build();
    }

    private static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }
}
