package com.example.guest.ghostydrop;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabaseRef;
    private Picture pictures;
    private String Latitude;
    private String Longitude;
    @Bind(R.id.createGhostButton)
    ImageView mCreateGhostButton;
    @Bind(R.id.aboutButton)
    ImageView mAboutButton;
    @Bind(R.id.discoverButton)
    ImageView mDiscoverButton;
    @Bind(R.id.welcomeText)
    TextView mWelcomeText;
    @Bind(R.id.profileImageView)
    ImageView ProfileImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        String fontPath = "fonts/Roboto-Regular.ttf";
        Typeface RobotoFont = Typeface.createFromAsset(getAssets(), fontPath);
        mWelcomeText.setTypeface(RobotoFont);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_PHOTOS);
        Intent intent = getIntent();
        String bitmap = intent.getStringExtra("bitmap");
        Longitude = intent.getStringExtra("longi");
        Latitude = intent.getStringExtra("lati");
        String com = intent.getStringExtra("com");

        String comment = com;
        String pictureURL = bitmap;
        String latitude = Latitude;
        String longitude = Longitude;

        pictures = new Picture(comment, pictureURL, latitude, longitude);

        mDatabaseRef.push().setValue(pictures);
        mAboutButton.setOnClickListener(this);
        mDiscoverButton.setOnClickListener(this);
        mCreateGhostButton.setOnClickListener(this);
        ProfileImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mCreateGhostButton) {
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
            startActivity(intent);
        } else if (v == mAboutButton) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (v == mDiscoverButton) {
            Intent intent = new Intent(MainActivity.this,   FindPictureListActivity.class);

            intent.putExtra("long", Longitude);
            intent.putExtra("lat", Latitude);
          startActivity(intent);
        } else if (v ==ProfileImageView) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    }
}





