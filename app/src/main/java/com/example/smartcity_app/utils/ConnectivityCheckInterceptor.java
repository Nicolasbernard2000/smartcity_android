package com.example.smartcity_app.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.smartcity_app.utils.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityCheckInterceptor implements Interceptor {
    private Context context;

    public ConnectivityCheckInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if (!isOnline(context)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nw = connectivityManager.getActiveNetworkInfo();

        NetworkInfo netInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        Log.i("Debug", "Connexion : " + (netInfo != null));
        return netInfo != null && netInfo.isConnected();
    }
}
