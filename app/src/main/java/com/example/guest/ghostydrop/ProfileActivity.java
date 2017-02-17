package com.example.guest.ghostydrop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mCurrentUserRef;
    private static final String TAG = "Debug";
    @Bind(R.id.profilePictureImageView)
    ImageView ProfilePictureImageView;
    @Bind(R.id.displayNameTextView)
    TextView DisplayNameTextView;
    @Bind(R.id.ageTextView)
    TextView AgeTextView;
    @Bind(R.id.bioTextView)
    TextView BioTextView;
    @Bind(R.id.logoutButton)
    Button Logout;
    @Bind(R.id.editProfileButton)
    Button EditProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        fillProfileData();
        EditProfileButton.setOnClickListener((View.OnClickListener) this);
    }

    private void fillProfileData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);


        // Get a reference to our posts
        mCurrentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.child("displayName").getValue().toString());
                Log.d(TAG, dataSnapshot.child("bio").getValue().toString());
                Log.d(TAG, dataSnapshot.child("birthday").getValue().toString());
                Log.d(TAG, dataSnapshot.child("picture").getValue().toString());
                Log.d(TAG, dataSnapshot.child("lastName").getValue().toString());

                DisplayNameTextView.setText(dataSnapshot.child("displayName").getValue().toString());
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
    }

    @Override
    public void onClick(View v) {
        if (v == EditProfileButton) {
            //have dialog box poplate and 
            Log.d(TAG, "onClick: edit tedt");
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
