package com.example.guest.ghostydrop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.ghostydrop.Constructors.Picture;
import com.example.guest.ghostydrop.adapters.FirebaseAllPhotosViewHolder;
import com.example.guest.ghostydrop.util.Android_Gesture_Detector;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindPictureListActivity extends AppCompatActivity implements View.OnClickListener{
    public String Latitude;
    public String Longitude;
    private DatabaseReference mDatabaseRef;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private ProgressDialog loadPictureProgressDialog;
    private GestureDetector mGestureDetector;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.profileImageView)
    ImageView ProfileImageView;
    @Bind(R.id.headerText)
    TextView HeaderText;
    @Bind(R.id.cameraLogo)
    ImageView CameraLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_picture_list);
        ButterKnife.bind(this);
        createLoadPictureProgressDialog();
        loadPictureProgressDialog.show();
        delayDialog();


        Intent intent = getIntent();
        Longitude = intent.getStringExtra("long");
        Latitude = intent.getStringExtra("la");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PHOTOS);
        setUpFirebaseAdapter();

        HeaderText.setOnClickListener((View.OnClickListener) this);
        ProfileImageView.setOnClickListener((View.OnClickListener) this);
        CameraLogo.setOnClickListener((View.OnClickListener) this);

        Android_Gesture_Detector custom_gesture_detector = new Android_Gesture_Detector() {
            @Override
            public void onSwipeRight() {
                Intent intent = new Intent(FindPictureListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        mGestureDetector = new GestureDetector(this, custom_gesture_detector);
    }

    //responsible for touch events, handles them
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (0 == 0) {
            mGestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    private void delayDialog() {
        //3 second delay for the progress dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                loadPictureProgressDialog.dismiss();
            }
        }, 2000);
    }

    private void createLoadPictureProgressDialog() {
        loadPictureProgressDialog = new ProgressDialog(this);
        loadPictureProgressDialog.setTitle("Loading...");
        loadPictureProgressDialog.setMessage("Finding all the awesome pictures near you...");
        loadPictureProgressDialog.setCancelable(false);
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Picture, FirebaseAllPhotosViewHolder>
                (Picture.class, R.layout.photo_list_item, FirebaseAllPhotosViewHolder.class,
                        mDatabaseRef) {

            @Override
            protected void populateViewHolder(FirebaseAllPhotosViewHolder viewHolder,
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

    @Override
    public void onClick(View v) {
        if (v  == ProfileImageView) {
            Intent intent = new Intent(FindPictureListActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (v == HeaderText) {
            Intent intent = new Intent(FindPictureListActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (v == CameraLogo) {
            Intent intent = new Intent(FindPictureListActivity.this, PictureActivity.class);
            startActivity(intent);
        }
    }
}
