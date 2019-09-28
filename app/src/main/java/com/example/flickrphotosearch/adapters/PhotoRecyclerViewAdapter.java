package com.example.flickrphotosearch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrphotosearch.R;
import com.example.flickrphotosearch.fragments.PhotoFragment;
import com.example.flickrphotosearch.models.PhotoList;
import com.example.flickrphotosearch.models.Photos;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.example.flickrphotosearch.models.Photos} and makes a call to the
 * specified {@link PhotoFragment.OnPhotoInteractionListener}.
 */
public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private final Photos mPhotos;
    private final PhotoFragment.OnPhotoInteractionListener mListener;

    public PhotoRecyclerViewAdapter(Photos photos, PhotoFragment.OnPhotoInteractionListener listener) {
        mPhotos = photos;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_photo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mPhotoList = mPhotos.getPhotoList().get(position);
        mListener.onPhotoBind(holder.mPhotoList.getId(), holder.mPhotoList.getTitle(), holder.mContentView);
    }

    @Override
    public int getItemCount() {
        return mPhotos.getPhotoList().size();
    }

    public Photos getAllItems() {
        return mPhotos;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final ImageView mContentView;
        PhotoList mPhotoList;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContentView = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mPhotoList.getTitle() + "'";
        }

        @Override
        public void onClick(View v) {
            if (null != mListener) {
                mListener.onPhotoSelected(mPhotoList);
            }
        }
    }
}
