package com.example.flickrphotosearch.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.flickrphotosearch.BuildConfig;
import com.example.flickrphotosearch.interfaces.PhotoDataSource;
import com.example.flickrphotosearch.models.PhotoDetails;
import com.example.flickrphotosearch.models.Photos;
import com.example.flickrphotosearch.models.SearchResultPage;
import com.example.flickrphotosearch.models.Size;
import com.example.flickrphotosearch.network.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrDataSource implements PhotoDataSource {

    private static final String TAG = FlickrDataSource.class.getSimpleName();

    private static volatile FlickrDataSource INSTANCE;

    private FlickrDataSource() {
    }

    public static FlickrDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (FlickrDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlickrDataSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getPhotoListByTag(final String tag, final SearchResultCallback searchResultCallback) {
        Call<SearchResultPage> call = RestClient.get().getSearchResults(BuildConfig.FLICKR_API_KEY, tag);
        call.enqueue(new Callback<SearchResultPage>() {
            @Override
            public void onResponse(@NonNull Call<SearchResultPage> call, @NonNull Response<SearchResultPage> response) {
                if (response.isSuccessful()) {
                    SearchResultPage results = response.body();
                    searchResultCallback.onSearchResultSuccess(results.getPhotos());
                } else {
                    Log.e(TAG, "Result failed searching photo for tag: " + tag + " -> " + response.body());
                    searchResultCallback.onSearchFail(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResultPage> call, @NonNull Throwable t) {
                Log.e(TAG, "Error searching photo for tag: " + tag, t);
                searchResultCallback.onError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getPhotoSizes(final String photoId, final GetSizesCallback getSizesCallback) {
        Call<PhotoDetails> call = RestClient.get().getImageSizes(BuildConfig.FLICKR_API_KEY, photoId);
        call.enqueue(new Callback<PhotoDetails>() {
            @Override
            public void onResponse(@NonNull Call<PhotoDetails> call, @NonNull Response<PhotoDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Size> sizeMap = buildSizeMap(response.body());
                    getSizesCallback.onRequestSuccess(sizeMap);
                } else {
                    Log.e(TAG, "Result failed searching photo ID: " + photoId + " -> " + response.body());
                    getSizesCallback.onSearchFail(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoDetails> call, @NonNull Throwable t) {
                Log.e(TAG, "Error searching photo ID: " + photoId, t);
                getSizesCallback.onError(t.getLocalizedMessage());
            }
        });
    }

    /**
     * Builds a size map with the size label as the key.
     *
     * @param photoDetails The details of all the sizes availabe for a photo.
     * @return A map of all the sizes, with the size label as the key.
     */
    private Map<String, Size> buildSizeMap(@NonNull PhotoDetails photoDetails) {
        Map<String, Size> sizeMap = new HashMap<>();
        List<Size> sizeList = photoDetails.getSizes().getSize();
        for (Size size : sizeList) {
            sizeMap.put(size.getLabel(), size);
        }

        return sizeMap;
    }
}
