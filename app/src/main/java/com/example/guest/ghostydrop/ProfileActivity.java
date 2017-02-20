package com.example.guest.ghostydrop;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guest.ghostydrop.util.Android_Gesture_Detector;
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

import static com.example.guest.ghostydrop.R.id.bioTextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mCurrentUserRef;
    private ProgressDialog ProfileProgressDialog;
    private GestureDetector mGestureDetector;
    private static final String TAG = "Debug";

    @Bind(R.id.profilePictureImageView)
    ImageView ProfilePictureImageView;
    @Bind(R.id.displayNameTextView)
    TextView DisplayNameTextView;
    @Bind(R.id.ageTextView)
    TextView AgeTextView;
    @Bind(bioTextView)
    TextView BioTextView;
    @Bind(R.id.logoutButton)
    Button Logout;
    @Bind(R.id.editProfileButton)
    Button EditProfileButton;
    @Bind(R.id.searchLogo)
    ImageView SearchImageView;
    @Bind(R.id.profileImageView)
    ImageView ProfileImageView;
    @Bind(R.id.headerText)
    TextView HeaderTextView;
    @Bind(R.id.socialChunk)
    RelativeLayout SocialChunk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        createProfileProgressDialog();
        ProfileProgressDialog.show();
        delayDialog();
        fillProfileData();
        EditProfileButton.setOnClickListener(this);
        HeaderTextView.setOnClickListener(this);
        SearchImageView.setOnClickListener(this);

        //Gesture handler, this is where every action is handled
        Android_Gesture_Detector custom_gesture_detector = new Android_Gesture_Detector() {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
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

    private void createProfileProgressDialog() {
        ProfileProgressDialog = new ProgressDialog(this);
        ProfileProgressDialog.setTitle("Loading...");
        ProfileProgressDialog.setMessage("Generating your profile...");
        ProfileProgressDialog.setCancelable(false);
    }

    private void delayDialog() {
        //3 second delay for the progress dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ProfileProgressDialog.dismiss();
            }
        }, 1500);
    }


    private void fillProfileData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);


        // Get a reference to our posts
        mCurrentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DisplayNameTextView.setText(dataSnapshot.child("displayName").getValue().toString());
                BioTextView.setText(dataSnapshot.child("bio").getValue().toString());
                AgeTextView.setText(dataSnapshot.child("birthday").getValue().toString() + "years old");
                String profilePictureURL = dataSnapshot.child("picture").getValue().toString();
                new DownloadImageTask((ImageView) findViewById(R.id.profilePictureImageView))
                        .execute(profilePictureURL);
                SocialChunk.setVisibility(View.VISIBLE);

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
    }

    @Override
    public void onClick(View v) {
        if (v == EditProfileButton) {
            FragmentManager fm = getFragmentManager();
            EditProfileDialogBoxFragment editProfDialogBox = new EditProfileDialogBoxFragment();
                editProfDialogBox.show(fm, "dialog is shown");
        } else if (v == SearchImageView) {
            Intent intent = new Intent(ProfileActivity.this, FindPictureListActivity.class);
            startActivity(intent);
        } else if (v == HeaderTextView) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
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
