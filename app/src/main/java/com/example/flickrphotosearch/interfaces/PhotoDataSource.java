package com.example.flickrphotosearch.interfaces;

import com.example.flickrphotosearch.models.Photos;
import com.example.flickrphotosearch.models.Size;

import java.util.Map;

public interface PhotoDataSource {

    void getPhotoListByTag(String tag, SearchResultCallback searchResultCallback);

    void getPhotoSizes(String photoId, GetSizesCallback getSizesCallback);

    interface SearchResultCallback {

        void onSearchResultSuccess(Photos photos);

        void onSearchFail(String errorMessage);

        void onError(String errorMessage);
    }

    interface GetSizesCallback {

        void onRequestSuccess(Map<String, Size> photoSizes);

        void onSearchFail(String errorMessage);

        void onError(String errorMessage);
    }
}
