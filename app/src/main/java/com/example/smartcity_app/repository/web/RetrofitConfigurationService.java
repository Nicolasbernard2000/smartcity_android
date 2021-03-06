package com.example.smartcity_app.repository.web;

import android.content.Context;

import com.example.smartcity_app.util.ConnectivityCheckInterceptor;
import com.example.smartcity_app.util.Constants;
import com.example.smartcity_app.util.HeaderInterceptor;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import java.sql.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitConfigurationService {
    private Retrofit retrofitClient;

    private static WalloniaFixedWebService webService;

    private RetrofitConfigurationService(Context context) {
        initializeRetrofit(context);
    }

    private void initializeRetrofit(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityCheckInterceptor(context))
                .addInterceptor(new HeaderInterceptor(context))
                .build();

        Moshi moshiConverter = new Moshi.Builder()
                .add(new KotlinJsonAdapterFactory())
                .add(Date.class, new Rfc3339DateJsonAdapter())
                .add(java.util.Date.class, new Rfc3339DateJsonAdapter())
                .build();

        this.retrofitClient = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
                .build();
    }

    public static RetrofitConfigurationService getInstance(Context context) {
        return new RetrofitConfigurationService(context);
    }

    public WalloniaFixedWebService webService() {
        if (webService == null) {
            webService = retrofitClient.create(WalloniaFixedWebService.class);
        }

        return webService;
    }
}
