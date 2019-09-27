package com.example.flickrphotosearch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrphotosearch.FullscreenActivity;
import com.example.flickrphotosearch.R;
import com.example.flickrphotosearch.adapters.PhotoRecyclerViewAdapter;
import com.example.flickrphotosearch.interfaces.PhotoContract;
import com.example.flickrphotosearch.models.Photo;
import com.example.flickrphotosearch.models.Photos;
import com.example.flickrphotosearch.models.Size;
import com.google.android.material.snackbar.Snackbar;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPhotoInteractionListener}
 * interface.
 */
public class PhotoFragment extends Fragment implements PhotoContract.View {

    private static final String TAG = PhotoFragment.class.getSimpleName();

    private View rootView;
    private PhotoContract.Presenter mPresenter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoFragment() {
    }

    public static PhotoFragment newInstance(int columnCount) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        } else {
            mColumnCount = getContext().getResources().getInteger(R.integer.grid_span);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photo_list, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.searchPhotosByTag(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 3) {
                    mPresenter.searchPhotosByTag(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void populateList(final Photos photos) {
        // Set the adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), mColumnCount));
        }
        recyclerView.setAdapter(new PhotoRecyclerViewAdapter(photos, new OnPhotoInteractionListener() {
            @Override
            public void onPhotoBind(String id, String photoTitle, ImageView imageView) {
                mPresenter.getImageForView(id, photoTitle, imageView);
            }

            @Override
            public void onPhotoSelected(Photo photo) {
                mPresenter.getPhotoSizeList(photo.getId(), photo.getTitle());
            }
        }));
    }

    @Override
    public void showProgress(final boolean show) {
        rootView.findViewById(R.id.progress_main_view).setVisibility(show ? View.VISIBLE : View.GONE);
//        rootView.findViewById(R.id.list).setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        //TODO: Show empty view
        Log.d(TAG, "Show empty View in Fragment");
    }

    @Override
    public void showFullScreenImage(final Size image, final String photoTitle) {
        Intent i = new Intent(getContext(), FullscreenActivity.class);
        Bundle extras = getArguments();
        if (null == extras) {
            extras = new Bundle();
        }

        extras.putString(FullscreenActivity.EXTRA_IMAGE_URL, image.getSource());
        extras.putString(FullscreenActivity.EXTRA_IMAGE_TEXT, photoTitle);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void showError(final String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof OnPhotoInteractionListener) {
//            mListener = (OnPhotoInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnPhotoInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void setPresenter(PhotoContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface OnPhotoInteractionListener {
        void onPhotoBind(String id, String photoTitle, ImageView mContentView);

        void onPhotoSelected(Photo photo);
    }
}
