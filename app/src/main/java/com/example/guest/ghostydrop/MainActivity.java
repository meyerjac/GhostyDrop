package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabaseRef;
    private Picture pictures;
    @Bind(R.id.createGhostButton)
    ImageView mCreateGhostButton;
    @Bind(R.id.aboutButton)
    ImageView mAboutButton;
    @Bind(R.id.discoverButton)
    ImageView mDiscoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_PHOTOS);
        Intent intent = getIntent();
        String bitmap = intent.getStringExtra("bitmap");
        String longi = intent.getStringExtra("longi");
        String lat = intent.getStringExtra("lati");
        String com = intent.getStringExtra("com");

        String comment = com;
        String pictureURL = bitmap;
        String latitude = lat;
        String longitude = longi;

        pictures = new Picture(comment, pictureURL, latitude, longitude);

        mDatabaseRef.push().setValue(pictures);
        mAboutButton.setOnClickListener(this);
        mDiscoverButton.setOnClickListener(this);
        mCreateGhostButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mCreateGhostButton) {
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
            startActivity(intent);
        } if (v == mAboutButton) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } if (v == mDiscoverButton) {
            Intent intent = new Intent(MainActivity.this,   FindPictureListActivity.class);
          startActivity(intent);
        }
    }
}





