package com.example.flickrphotosearch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.flickrphotosearch.data.FlickrDataSource;
import com.example.flickrphotosearch.data.PhotoRepository;
import com.example.flickrphotosearch.fragments.PhotoFragment;

public class MainActivity extends AppCompatActivity {

    private PhotoPresenter mPhotoPresenter;
    private static final int COLUMN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PhotoFragment photoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (photoFragment == null) {
            // Create the fragment
            photoFragment = PhotoFragment.newInstance(COLUMN_COUNT);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment, photoFragment);
            transaction.commit();
        }

        //Initialise Presenter and associate it with fragment
        mPhotoPresenter = new PhotoPresenter(getDataSource(), photoFragment);
    }

    private PhotoRepository getDataSource() {
        return PhotoRepository.getInstance(FlickrDataSource.getInstance());
    }
}
