package com.example.guest.ghostydrop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.guest.ghostydrop.R.id.imageView;

public class PictureActivity extends AppCompatActivity implements  View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private LocationManager locationManager;
    private LocationListener listener;
    private String Latitude;
    private String Longitude;
    private String imageEncoded;
    private String CommentLine;

    @Bind(R.id.action_photo)
    Button mAction_Photo;
    @Bind(imageView)
    ImageView mImageView;
    @Bind(R.id.commentText)
    TextView mCommentText;
    @Bind(R.id.ghostDrop)
    Button mGhostDrop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Latitude = "";
        Longitude = "";

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Latitude = String.valueOf(location.getLatitude());
                Longitude = String.valueOf(location.getLongitude());
                Log.d("lat", Latitude);
                Log.d("long", Longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
        mGhostDrop.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.INTERNET}

                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        mAction_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //noinspection MissingPermission
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
                onLaunchCamera();
            }
        });
    }

    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            encodeBitmap(imageBitmap);
            View image = findViewById(R.id.imageView);
            View commentLine = findViewById(R.id.commentText);
            View save = findViewById(R.id.ghostDrop);
            image.setVisibility(View.VISIBLE);
            commentLine.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
        }
    }

    private void encodeBitmap(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void onClick(View v) {
        if (v == mGhostDrop) {
            Toast.makeText(PictureActivity.this, CommentLine, Toast.LENGTH_LONG).show();
            CommentLine = mCommentText.getText().toString();
            Log.d("comment", CommentLine);
            if ((Longitude == "") || (Latitude == "")) {
                Toast.makeText(PictureActivity.this, "GPS not activated, please wait 3 seconds before trying again", Toast.LENGTH_LONG).show();
                return;
            }

            if (CommentLine == null) {
                Toast.makeText(PictureActivity.this, "Please add a comment to this picture, Thanks Ghoster", Toast.LENGTH_LONG).show();
                return;
            }
            if ((Longitude != "") || (Latitude != "")) {
                Toast.makeText(PictureActivity.this, "Successfully dropped Photo!", Toast.LENGTH_LONG).show();
                CommentLine = mCommentText.getText().toString();

                Intent intent = new Intent(PictureActivity.this, MainActivity.class);
                intent.putExtra("bitmap", imageEncoded);
                intent.putExtra("longi", Longitude);
                intent.putExtra("lati", Latitude);
                intent.putExtra("com", CommentLine);

                startActivity(intent);
            }
        }
    }
}
