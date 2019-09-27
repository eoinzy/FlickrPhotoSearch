package com.example.flickrphotosearch.network;

import com.example.flickrphotosearch.models.PhotoDetails;
import com.example.flickrphotosearch.models.Photos;
import com.example.flickrphotosearch.models.SearchResultPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestInterface {
    @GET("/services/rest/?method=flickr.photos.search&page=1&format=json&nojsoncallback=1")
    Call<SearchResultPage> getSearchResults(@Query("api_key") String apiKey, @Query("tags") String tag);

    @GET("/services/rest/?method=flickr.photos.getSizes&format=json&nojsoncallback=1")
    Call<PhotoDetails> getImageSizes(@Query("api_key") String apiKey, @Query("photo_id") String photoId);
}
