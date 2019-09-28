package com.example.flickrphotosearch;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.flickrphotosearch.data.PhotoRepository;
import com.example.flickrphotosearch.interfaces.PhotoContract;
import com.example.flickrphotosearch.interfaces.PhotoDataSource;
import com.example.flickrphotosearch.models.Photos;
import com.example.flickrphotosearch.models.Size;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class PhotoPresenter implements PhotoContract.Presenter {

    private final PhotoRepository mPhotoRepository;

    private final PhotoContract.View mPhotosView;

    private static final String DEFAULT_IMAGE_SIZE = "Large Square";

    public PhotoPresenter(@NonNull PhotoRepository photoRepository, @NonNull PhotoContract.View photosView) {
        mPhotoRepository = photoRepository;

        mPhotosView = photosView;
        mPhotosView.setPresenter(this);
    }

    @Override
    public void searchPhotosByTag(String tag) {
        searchPhotosByTag(tag, 1);
    }

    @Override
    public void searchPhotosByTag(final String tag, final int page) {
        showProgress(true);
        mPhotoRepository.getPhotoListByTag(tag, page, new PhotoDataSource.SearchResultCallback() {
            @Override
            public void onSearchResultSuccess(final Photos photos) {
                showProgress(false);
                if (page == 1) {
                    mPhotosView.showEmptyView(false);
                    mPhotosView.populateList(photos);
                } else {
                    mPhotosView.appendList(photos);
                }
            }

            @Override
            public void onSearchFail(final String errorMessage) {
                showProgress(false);
                mPhotosView.showError(errorMessage);
            }

            @Override
            public void onError(final String errorMessage) {
                showProgress(false);
                mPhotosView.showError(errorMessage);
            }
        });
    }

    @Override
    public void getPhotoSizeList(final String photoId, final String photoTitle) {
        showProgress(true);
        getImage(photoId, photoTitle, null);
    }

    @Override
    public void getImageForView(String photoId, String photoTitle, ImageView imageView) {
        getImage(photoId, photoTitle, imageView);
    }

    private void getImage(final String photoId, final String photoTitle, @Nullable final ImageView imageView) {
        mPhotoRepository.getPhotoSizes(photoId, new PhotoDataSource.GetSizesCallback() {
            @Override
            public void onRequestSuccess(final Map<String, Size> photoSizes) {
                showProgress(false);
                if (null == imageView) {
                    Size size = photoSizes.get(DEFAULT_IMAGE_SIZE);
                    mPhotosView.showFullScreenImage(size, photoTitle);
                } else {
                    Picasso.get().load(photoSizes.get(DEFAULT_IMAGE_SIZE).getSource()).into(imageView);
                }
            }

            @Override
            public void onSearchFail(final String errorMessage) {
                showProgress(false);
                mPhotosView.showError(errorMessage);
            }

            @Override
            public void onError(final String errorMessage) {
                showProgress(false);
                mPhotosView.showError(errorMessage);
            }
        });
    }

    @Override
    public void showProgress(final boolean show) {
        mPhotosView.showProgress(show);
    }

    @Override
    public void start() {
        //Do any initial setup
        mPhotosView.showEmptyView(true);
    }
}
