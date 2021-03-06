package com.example.guest.ghostydrop;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.ghostydrop.Constructors.Picture;
import com.example.guest.ghostydrop.adapters.MySavedPicturesInProfileViewHolder;
import com.example.guest.ghostydrop.util.Android_Gesture_Detector;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.guest.ghostydrop.R.id.savedPictureRecyclerView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mCurrentUserRef;
    private DatabaseReference SavedPhotoDatabaseRef;
    private ProgressDialog ProfileProgressDialog;
    private GestureDetector mGestureDetector;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private static final String TAG = "Debug";

    @Bind(R.id.profilePictureImageView) ImageView ProfilePictureImageView;
    @Bind(R.id.displayNameTextView) TextView DisplayNameTextView;
    @Bind(R.id.ageTextView) TextView AgeTextView;
    @Bind(R.id.bioTextView) TextView BioTextView;
    @Bind(R.id.textView2) TextView TextView2;
    @Bind(R.id.logoutButton) Button Logout;
    @Bind(R.id.editProfileButton) Button EditProfileButton;
    @Bind(R.id.searchLogo) ImageView SearchImageView;
    @Bind(R.id.profileImageView) ImageView ProfileImageView;
    @Bind(R.id.headerText) TextView HeaderTextView;
    @Bind(savedPictureRecyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.socialChunk) RelativeLayout SocialChunk;
    @Bind(R.id.editLogoutRelativeLayout) RelativeLayout EditLogoutRelativeLayout;
    @Bind(R.id.were) RelativeLayout Were;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        HeaderTextView.setTypeface(ostrichFont);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);


        //checking to see if this person hasn't collected photos yet
            DatabaseReference rootRef = mCurrentUserRef;
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!(snapshot.hasChild("collectedPhotos"))) {
                        Context context = getApplicationContext();
                        CharSequence text = "No Photos collected yet, Get Collecting!!!";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        SavedPhotoDatabaseRef = mCurrentUserRef.child("collectedPhotos");
        setUpProfileCollectedPhotosFirebaseAdapter();

        fillProfileData();
        EditProfileButton.setOnClickListener(this);
        HeaderTextView.setOnClickListener(this);
        SearchImageView.setOnClickListener(this);
        SocialChunk.setVisibility(View.INVISIBLE);
        Were.setVisibility(View.INVISIBLE);
        EditLogoutRelativeLayout.setVisibility(View.INVISIBLE);

        //Gesture handler, this is where every action is handled
        Android_Gesture_Detector custom_gesture_detector = new Android_Gesture_Detector() {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
            }
        };
        mGestureDetector = new GestureDetector(this, custom_gesture_detector);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation moveUp = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.move_up_slow);
                SocialChunk.startAnimation(moveUp);
                SocialChunk.setVisibility(View.VISIBLE);

            }
        }, 100);
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation moveUp = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.move_up_slow);
                Were.startAnimation(moveUp);
                Were.setVisibility(View.VISIBLE);
            }
        }, 300);
        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation moveUp = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.move_up_slow);
                EditLogoutRelativeLayout.startAnimation(moveUp);
                EditLogoutRelativeLayout.setVisibility(View.VISIBLE);
            }
        }, 600);

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


    private void setUpProfileCollectedPhotosFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Picture, MySavedPicturesInProfileViewHolder>
                (Picture.class, R.layout.photo_list_item, MySavedPicturesInProfileViewHolder.class,
                        SavedPhotoDatabaseRef) {

            @Override
            protected void populateViewHolder(MySavedPicturesInProfileViewHolder viewHolder,
                                              Picture model, int position) {

                viewHolder.bindPicture(model);
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);


    }

    private void createProfileProgressDialog() {
        ProfileProgressDialog = new ProgressDialog(this);
        ProfileProgressDialog.setTitle("Loading...");
        ProfileProgressDialog.setMessage("Generating profile...");
        ProfileProgressDialog.setCancelable(false);
    }

    private void delayDialog() {
        //3 second delay for the progress dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ProfileProgressDialog.dismiss();
            }
        }, 2500);
    }

    private void fillProfileData() {
        createProfileProgressDialog();
        ProfileProgressDialog.show();
        delayDialog();
        SocialChunk.setVisibility(View.VISIBLE);
        Were.setVisibility(View.VISIBLE);
        EditLogoutRelativeLayout.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);


        // Get a reference to our profile
        mCurrentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DisplayNameTextView.setText(dataSnapshot.child("displayName").getValue().toString());
                BioTextView.setText(dataSnapshot.child("bio").getValue().toString());
                AgeTextView.setText(dataSnapshot.child("birthday").getValue().toString() + " years old");
                String profilePictureURL = dataSnapshot.child("picture").getValue().toString();
                new DownloadImageTask((ImageView) findViewById(R.id.profilePictureImageView))
                        .execute(profilePictureURL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onClick(View v) {
        if (v == EditProfileButton) {
            FragmentManager fm = getFragmentManager();
            EditProfileDialogBoxFragment editProfDialogBox = new EditProfileDialogBoxFragment();
                editProfDialogBox.show(fm, "dialog is shown");

        } else if (v == HeaderTextView) {

            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.viewslideleft);
                    SocialChunk.startAnimation(slideLeft);
                    SocialChunk.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.viewslideleft);
                    Were.startAnimation(slideLeft);
                    Were.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.viewslideleft);
                    EditLogoutRelativeLayout.startAnimation(slideLeft);
                    EditLogoutRelativeLayout.setVisibility(View.INVISIBLE);
                }
            }, 500);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                }
            }, 800);


        } else if (v == SearchImageView) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.viewslideleft);
                    SocialChunk.startAnimation(slideLeft);
                    SocialChunk.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.viewslideleft);
                    Were.startAnimation(slideLeft);
                    Were.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.viewslideleft);
                    EditLogoutRelativeLayout.startAnimation(slideLeft);
                    EditLogoutRelativeLayout.setVisibility(View.INVISIBLE);

                }
            }, 500);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ProfileActivity.this, FindPictureListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                }
            }, 800);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
