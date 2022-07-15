package org.cryptonews.main.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;
    private static API api = null;
    private static int REQUEST_TIMEOUT = 30;
    private static String BASE_URL = "https://pro-api.coinmarketcap.com";

    public static Retrofit getRetrofitInstance() {
        if(retrofit!=null) return retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
        return retrofit;
    }

    public static OkHttpClient getClient() {
        if(client!=null) return client;
        client = new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        builder.addHeader("Accept", "application/json")
                                .addHeader("Content-Type", "application/json")
                                .addHeader("X-CMC_PRO_API_KEY","1aa29c68-9c98-4243-95e4-184118053c51");
                        return chain.proceed(builder.build());
                    }
                })
                .build();
        return client;
    }
    public static OkHttpClient getClient1() {
       OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS)
               .addInterceptor(new Interceptor() {
                   @Override
                   public Response intercept(Chain chain) throws IOException {
                       Request request = chain.request();
                       Request.Builder builder = request.newBuilder();
                       builder.addHeader("accept", "application/json");
                       return chain.proceed(builder.build());
                   }
               })
                .build();
        return client;
    }

    public static API getApi() {
        if(api==null) api = getRetrofitInstance().create(API.class);
        return api;
    }
}
