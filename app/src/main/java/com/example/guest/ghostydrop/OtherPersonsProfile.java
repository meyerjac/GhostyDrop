package com.example.guest.ghostydrop;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.ghostydrop.Constructors.Picture;
import com.example.guest.ghostydrop.adapters.SavedPicturesInProfileViewHolder;
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

public class OtherPersonsProfile extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mCurrentUserRef;
    private DatabaseReference mTheirUserRef;
    private DatabaseReference TheirSavedPhotoDatabaseRef;
    private ProgressDialog ProfileProgressDialog;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private ImageView mProfileImageView;

    @Bind(R.id.profilePictureImageView) ImageView ProfilePictureImageView;
    @Bind(R.id.displayNameTextView) TextView DisplayNameTextView;
    @Bind(R.id.ageTextView) TextView AgeTextView;
    @Bind(R.id.bioTextView) TextView BioTextView;
    @Bind(R.id.textView2) TextView TextView2;
    @Bind(R.id.searchLogo) ImageView SearchImageView;
    @Bind(R.id.profileLogoImageView) ImageView ProfileLogo;
    @Bind(R.id.headerText) TextView HeaderTextView;
    @Bind(savedPictureRecyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.socialChunk) RelativeLayout SocialChunk;
    @Bind(R.id.were) RelativeLayout Were;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_persons_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String newProfileUid = intent.getStringExtra(Constants.USER_UID);
        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        HeaderTextView.setTypeface(ostrichFont);

        //current user ref
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);

        //onProfilesPersonRef
        mTheirUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(newProfileUid);


        TheirSavedPhotoDatabaseRef = mTheirUserRef.child("collectedPhotos");
        setUpProfileCollectedPhotosFirebaseAdapter();

        fillProfileData();
        HeaderTextView.setOnClickListener(this);
        ProfileLogo.setOnClickListener(this);
        SearchImageView.setOnClickListener(this);
        SocialChunk.setVisibility(View.INVISIBLE);
        Were.setVisibility(View.INVISIBLE);


        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation moveUp = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.move_up_slow);
                SocialChunk.startAnimation(moveUp);
                SocialChunk.setVisibility(View.VISIBLE);

            }
        }, 100);
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation moveUp = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.move_up_slow);
                Were.startAnimation(moveUp);
                Were.setVisibility(View.VISIBLE);
            }
        }, 300);
    }

    //responsible for touch events, handles them


    private void setUpProfileCollectedPhotosFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Picture, SavedPicturesInProfileViewHolder>
                (Picture.class, R.layout.photo_list_item, SavedPicturesInProfileViewHolder.class,
                        TheirSavedPhotoDatabaseRef) {

            @Override
            protected void populateViewHolder(SavedPicturesInProfileViewHolder viewHolder,
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
        ProfileProgressDialog.setTitle("Loading ...");
        ProfileProgressDialog.setMessage("Generating their profile...");
        ProfileProgressDialog.setCancelable(false);
    }

    private void delayDialog() {
        //3 second delay for the progress dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ProfileProgressDialog.dismiss();
            }
        }, 3000);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {

                //checking to see if this person hasn't collected photos yet
                DatabaseReference rootRef = mTheirUserRef;
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!(snapshot.hasChild("collectedPhotos"))) {
                            Context context = getApplicationContext();
                            CharSequence text = "They haven't collected Any Photos yet, check back later!!!";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, 3000);
    }


    private void fillProfileData() {
        createProfileProgressDialog();
        ProfileProgressDialog.show();
        delayDialog();
        SocialChunk.setVisibility(View.VISIBLE);
        Were.setVisibility(View.VISIBLE);


        // Get a reference to Their profile
        mTheirUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String theirName = dataSnapshot.child("displayName").getValue().toString();
                DisplayNameTextView.setText(theirName);
                BioTextView.setText(dataSnapshot.child("bio").getValue().toString());
                AgeTextView.setText(dataSnapshot.child("birthday").getValue().toString() + " years old");
                String profilePictureURL = dataSnapshot.child("picture").getValue().toString();
                new OtherPersonsProfile.DownloadImageTask((ImageView) findViewById(R.id.profilePictureImageView))
                        .execute(profilePictureURL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == HeaderTextView) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.viewslideleft);
                    SocialChunk.startAnimation(slideLeft);
                    SocialChunk.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.viewslideleft);
                    Were.startAnimation(slideLeft);
                    Were.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(OtherPersonsProfile.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                }
            }, 500);

        } else if (v == SearchImageView) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.viewslideleft);
                    SocialChunk.startAnimation(slideLeft);
                    SocialChunk.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation slideLeft = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.viewslideleft);
                    Were.startAnimation(slideLeft);
                    Were.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(OtherPersonsProfile.this, FindPictureListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                }
            }, 500);
        }
        else if (v == ProfileLogo) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation Slide = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.viewsslideright);
                    SocialChunk.startAnimation(Slide);
                    SocialChunk.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation Slide = AnimationUtils.loadAnimation(OtherPersonsProfile.this, R.anim.viewsslideright);
                    Were.startAnimation(Slide);
                    Were.setVisibility(View.INVISIBLE);
                }
            }, 200);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(OtherPersonsProfile.this, ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);

                }
            }, 400);

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
