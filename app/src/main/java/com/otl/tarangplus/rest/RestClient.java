package com.otl.tarangplus.rest;

import android.content.Context;
import android.text.TextUtils;

import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.PreferenceHandler;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by VPotadar on 28/08/18.
 */

public class RestClient {
    private ApiService apiService;
    private Context mContext;


    public static String getBaseUrlBasedOnBuildType() {
        String base = null;

        base = BuildConfig.BASE_URL;

        return base;
    }


    public RestClient(final Context context) {
        mContext = context;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addNetworkInterceptor(loggingInterceptor).build();
        } else
            httpClient.build();


        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                String contryCode = PreferenceHandler.getRegion(context);

                Request original = chain.request();

                HttpUrl originalHttpUrl = original.url();

                HttpUrl.Builder httpbuilderObj = originalHttpUrl.newBuilder();

                httpbuilderObj.addQueryParameter("auth_token", Constants.API_KEY);
                httpbuilderObj.addQueryParameter("region", contryCode);

                String lang = PreferenceHandler.getLang(context);

                if (!TextUtils.isEmpty(lang) && lang.equals(Constants.ODIA)) { // other language
                    httpbuilderObj.addQueryParameter("item_language", Constants.ODIA);
                } else { // English
                    httpbuilderObj.addQueryParameter("item_language", Constants.ENGLISH);
                }

                HttpUrl url = httpbuilderObj.build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(getBaseUrlBasedOnBuildType())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        apiService = restAdapter.create(ApiService.class);
    }

    public static String getBaseUrl() {
        return getBaseUrlBasedOnBuildType();
    }

    public ApiService getApiService() {
        return apiService;
    }

}
