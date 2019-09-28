package com.example.flickrphotosearch.data;

import androidx.annotation.NonNull;

import com.example.flickrphotosearch.interfaces.PhotoDataSource;

public class PhotoRepository implements PhotoDataSource {

    private static PhotoRepository INSTANCE;
    private final PhotoDataSource mFlickrDataSource;

    // Prevent direct instantiation.
    private PhotoRepository(@NonNull PhotoDataSource flickrDataSource) {
        this.mFlickrDataSource = flickrDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param flickerDataSoure the backend data source
     * @return the {@link PhotoRepository} instance
     */
    public static PhotoRepository getInstance(PhotoDataSource flickerDataSoure) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoRepository(flickerDataSoure);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(PhotoDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getPhotoListByTag(String tag, int page, SearchResultCallback searchResultCallback) {
        mFlickrDataSource.getPhotoListByTag(tag, page, searchResultCallback);
    }

    @Override
    public void getPhotoSizes(String photoId, GetSizesCallback getSizesCallback) {
        mFlickrDataSource.getPhotoSizes(photoId, getSizesCallback);
    }
}
