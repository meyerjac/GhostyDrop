package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
//    @Bind(R.id.currentPic)
//    TextView mCurrentPictureImageView;


    private DatabaseReference mDatabaseRef;
    private Pictures pictures;

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
        Toast.makeText(MainActivity.this, lat, Toast.LENGTH_LONG).show();

        String comment = com;
        String pictureURL = bitmap;
        String latitude = lat;
        String longitude = longi;

        pictures = new Pictures(comment, pictureURL, latitude, longitude);

        mDatabaseRef.push().setValue(pictures);
    }
}

//        if (pictures.getImageUrl().contains("http")) {
//            try {
//                Bitmap imageBitmap = decodeFromFirebaseBase64(pictures.getImageUrl());
//                mCurrentPictureImageView.setImageBitmap(imageBitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static Bitmap decodeFromFirebaseBase64(String image) throws  IOException {
//        byte[] decodeByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
//    }





