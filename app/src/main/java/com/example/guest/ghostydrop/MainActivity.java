package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.saveObject)
    Button mSaveObject;
    @Bind(R.id.commentText)
    TextView mCommentText;
    @Bind(R.id.imageUrlText)
    TextView mImageUrlText;
    @Bind(R.id.latitudeText)
    TextView mLatitudeText;
    @Bind(R.id.longitudeText)
    TextView mLongitudeText;

    private DatabaseReference mDatabaseRef;
    private Pictures pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSaveObject.setOnClickListener(this);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_PHOTOS);
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveObject) {
            Intent intent = getIntent();
            String bitmap = intent.getStringExtra("bitmap");
            String longi = intent.getStringExtra("longi");
            String lat = intent.getStringExtra("lati");


            String comment = mCommentText.getText().toString();
            String pictureURL = bitmap;
            String latitude = lat;
            String longitude = longi;



            pictures = new Pictures(comment, pictureURL, latitude, longitude);

            mDatabaseRef.push().setValue(pictures);


        }
    }
}




