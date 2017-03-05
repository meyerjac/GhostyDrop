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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import static com.example.guest.ghostydrop.R.id.recyclerView;

public class FindPictureListActivity extends AppCompatActivity implements View.OnClickListener{
    public String Latitude;
    public String Longitude;
    private DatabaseReference mDatabaseRef;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private ProgressDialog loadPictureProgressDialog;
    private GestureDetector mGestureDetector;

    @Bind(recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.profileImageView) ImageView ProfileImageView;
    @Bind(R.id.headerText) TextView HeaderText;
    @Bind(R.id.cameraLogo) ImageView CameraLogo;
    @Bind(R.id.horizLine1) View HorizLine1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_picture_list);
        ButterKnife.bind(this);
        HeaderText.setOnClickListener(this);
        ProfileImageView.setOnClickListener(this);
        CameraLogo.setOnClickListener(this);
        CameraLogo.setVisibility(View.INVISIBLE);
        HorizLine1.setVisibility(View.INVISIBLE);

        createLoadPictureProgressDialog();
        loadPictureProgressDialog.show();
        delayDialog();
        getLocationExtras();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PHOTOS);
        setUpFirebaseAdapter();

        Android_Gesture_Detector custom_gesture_detector = new Android_Gesture_Detector() {
            @Override
            public void onSwipeRight() {
                Intent intent = new Intent(FindPictureListActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
            }
        };
        mGestureDetector = new GestureDetector(this, custom_gesture_detector);
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation moveUp = AnimationUtils.loadAnimation(FindPictureListActivity.this, R.anim.move_up_slow);
                CameraLogo.startAnimation(moveUp);
                CameraLogo.setVisibility(View.VISIBLE);
                HorizLine1.startAnimation(moveUp);
                HorizLine1.setVisibility(View.VISIBLE);

            }
        }, 300);
    }

    private void getLocationExtras() {
        Intent intent = getIntent();
        Longitude = intent.getStringExtra("long");
        Latitude = intent.getStringExtra("la");
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
        //2 second delay for the progress dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                loadPictureProgressDialog.dismiss();
            }
        }, 2000);
    }

    //sets dialog box that shows on load of this activity
    private void createLoadPictureProgressDialog() {
        loadPictureProgressDialog = new ProgressDialog(this);
        loadPictureProgressDialog.setTitle("Loading...");
        loadPictureProgressDialog.setMessage("Finding all the awesome pictures near you...");
        loadPictureProgressDialog.setCancelable(false);
    }

    //sets firebase adapter to proper data
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
    public void onClick(View v) {
        if (v  == ProfileImageView) {

            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideRight = AnimationUtils.loadAnimation(FindPictureListActivity.this, R.anim.viewsslideright);
                    mRecyclerView.startAnimation(slideRight);
                    mRecyclerView.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideRight2 = AnimationUtils.loadAnimation(FindPictureListActivity.this, R.anim.viewsslideright);
                    CameraLogo.startAnimation(slideRight2);
                    CameraLogo.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FindPictureListActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
                }
            }, 500);
        } else if (v == HeaderText) {
            Intent intent = new Intent(FindPictureListActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
        } else if (v == CameraLogo) {
            Intent intent = new Intent(FindPictureListActivity.this, PictureActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    //onActivity end, this is called
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
