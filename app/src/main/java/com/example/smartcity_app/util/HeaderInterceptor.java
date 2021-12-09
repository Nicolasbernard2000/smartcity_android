package com.example.smartcity_app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smartcity_app.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private Context Context;

    public HeaderInterceptor(Context Context) {
        this.Context = Context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = Context.getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Context.getString(R.string.token), null);

        Request newRequest = chain.request().newBuilder()
                .addHeader("authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
}
