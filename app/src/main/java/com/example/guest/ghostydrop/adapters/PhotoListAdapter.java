package com.example.guest.ghostydrop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.ghostydrop.Picture;
import com.example.guest.ghostydrop.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {
    private ArrayList<Picture> mPictures = new ArrayList<>();
    private Context mContext;

    public PhotoListAdapter(Context context, ArrayList<Picture> pictures) {
        mContext = context;
        mPictures = pictures;
    }

    @Override
    public PhotoListAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoListAdapter.PhotoViewHolder holder, int position) {
        holder.bindPicture(mPictures.get(position));
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.photoImageView) ImageView mPhotoImageView;
        @Bind(R.id.photoCommentTextView) TextView mPhotoCommentView;
        @Bind(R.id.distanceTextView) TextView mDistanceTextView;
        private Context mContext;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindPicture(Picture picture) {
            mPhotoCommentView.setText(picture.getComment());
            mDistanceTextView.setText(picture.getLatitude());
//            mPhotoImageView.setText(picture.getImageUrl());
        }

    }
}