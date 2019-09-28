package com.example.flickrphotosearch.interfaces;

import android.widget.ImageView;

import com.example.flickrphotosearch.models.Photos;
import com.example.flickrphotosearch.models.Size;

public interface PhotoContract {

    interface View extends BaseView<Presenter> {

        void showProgress(final boolean show);

        void showEmptyView(final boolean show);

        void showFullScreenImage(final Size image, final String photoTitle);

        void showError(final String message);

        void populateList(final Photos photos);

        void appendList(final Photos photos);
    }

    interface Presenter extends BasePresenter {

        void searchPhotosByTag(final String tag);

        void searchPhotosByTag(final String tag, final int page);

        void getPhotoSizeList(final String photoId, final String photoTitle);

        void getImageForView(String photoId, String photoTitle, ImageView imageView);

        void showProgress(final boolean show);
    }
}
