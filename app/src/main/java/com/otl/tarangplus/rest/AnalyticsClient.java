package com.otl.tarangplus.rest;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by srishesh on 10/5/18.
 */

public class AnalyticsClient {

    private ApiService apiService;
    private Context mContext;


    public static String getBaseUrlBasedOnBuildType() {
        String base = null;
//        base = "http://18.210.75.7:3000/";
//        base = "https://shemaroo-web.innocraft.cloud/";
        base = "https://matomo.apisaranyu.in/";
        return base;
    }


    public AnalyticsClient(final Context context) {
        mContext = context;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addNetworkInterceptor(loggingInterceptor).build();


       /* httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
                String contryCode = mPref.getString(Constants.REGION, "IN");

                Request original = chain.request();

                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("auth_token", Constants.API_KEY)
                        .addQueryParameter("region", contryCode)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });*/

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
