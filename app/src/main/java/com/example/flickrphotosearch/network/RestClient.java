package com.example.flickrphotosearch.network;

import android.util.Log;

import com.example.flickrphotosearch.App;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String TAG = RestClient.class.getSimpleName();

    private static RestInterface REST_CLIENT;
    private static final String API_BASE_URL = "https://api.flickr.com/";

    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";

    private static Cache mCache;

    private static final int CACHE_SIZE = 10 * 1024 * 1024; //10MB
    private static OkHttpClient mCachedOkHttpClient;

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static RestInterface get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        if (REST_CLIENT == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(60, TimeUnit.SECONDS);
            httpClient.connectTimeout(60, TimeUnit.SECONDS);

            //Add Cache
            httpClient.addInterceptor(provideCacheInterceptor()).cache(provideCache());
            httpClient.networkInterceptors().add(provideCacheInterceptor());

            mCachedOkHttpClient = httpClient.build();

            Retrofit builder = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(mCachedOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();

            REST_CLIENT = builder.create(RestInterface.class);
        }
    }

    private static Cache provideCache() {
        if (mCache == null) {
            mCache = new Cache(new File(App.getAppContext().getCacheDir(), "http-cache"), CACHE_SIZE);
        }
        return mCache;
    }

    private static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                CacheControl cacheControl;

                if (NetworkManager.isConnected(App.getAppContext())) {
                    cacheControl = new CacheControl.Builder()
                            .maxStale(0, TimeUnit.SECONDS)
                            .build();
                } else {
                    cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();
                }

                return response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public static void clearCache() {
        if (mCachedOkHttpClient != null) {
            // Cancel Pending Cached Request
            mCachedOkHttpClient.dispatcher().cancelAll();
        }

        REST_CLIENT = null;

        if (mCache != null) {
            try {
                mCache.evictAll();
            } catch (IOException e) {
                Log.e(TAG, "Error cleaning http cache");
            }
        }

        mCache = null;
    }
}
