package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.guest.ghostydrop.adapters.FirebasePhotoViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindPictureListActivity extends AppCompatActivity {
    public String Latitude;
    public String Longitude;
    private DatabaseReference mDatabaseRef;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Longitude = intent.getStringExtra("long");
        Latitude = intent.getStringExtra("la");




        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PHOTOS);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Picture, FirebasePhotoViewHolder>
                (Picture.class, R.layout.photo_list_item, FirebasePhotoViewHolder.class,
                        mDatabaseRef) {

            @Override
            protected void populateViewHolder(FirebasePhotoViewHolder viewHolder,
                                              Picture model, int position) {

                viewHolder.bindPicture(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
