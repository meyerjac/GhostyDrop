package com.example.guest.ghostydrop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.guest.ghostydrop.Constructors.Picture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.guest.ghostydrop.R.id.iconImage;
import static com.example.guest.ghostydrop.R.id.imageView;

public class PictureActivity extends AppCompatActivity implements  View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private LocationManager locationManager;
    private LocationListener listener;
    private String Latitude;
    private String TAG = "Picture Activity";
    private String Longitude;
    private String imageEncoded;
    private String CommentLine;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private DatabaseReference mPhotosRef;

    @Bind(imageView)
    ImageView mImageView;
    @Bind(iconImage)
    ImageView mIcon;
    @Bind(R.id.commentText)
    TextView mCommentText;
    @Bind(R.id.ghostDrop)
    Button mGhostDrop;
    @Bind(R.id.action_photo)
    Button mAction_Photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mPhotosRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_PHOTOS);

        Latitude = "";
        Longitude = "";
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Latitude = String.valueOf(location.getLatitude());
                Longitude = String.valueOf(location.getLongitude());
                addToSharedPreferences(Latitude, Longitude);

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

    private void addToSharedPreferences(String Latitude, String Longitude) {
        mEditor.putString(Constants.LONGITUDE, Longitude).apply();
        mEditor.putString(Constants.LATITUDE, Latitude).apply();
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
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
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
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
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
            View icon = findViewById(R.id.iconImage);
            image.setVisibility(View.VISIBLE);
            commentLine.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            icon.setVisibility(View.INVISIBLE);
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
            CommentLine = mCommentText.getText().toString();
            if ((Longitude == "") || (Latitude == "")) {
                Toast.makeText(PictureActivity.this, "GPS not activated, please wait 3 seconds before trying again", Toast.LENGTH_LONG).show();
                return;
            }

            if (CommentLine.equals("")) {
                Log.d(TAG, "onClick:  in commentLine!");
                Toast.makeText(PictureActivity.this, "Please add a caption to this picture, Thanks Ghoster", Toast.LENGTH_LONG).show();
                return;
            }
            if ((Longitude != "") || (Latitude != "")) {
                CommentLine = mCommentText.getText().toString();
                Log.d("up", CommentLine);
                String caption = CommentLine;
                String pictureURL = imageEncoded;
                String latitude = Latitude;
                String longitude = Longitude;
                ArrayList<String> comments = new ArrayList<String>();
//                {{
//                    comments.add("0");
//                }}

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String ownerUid = user.getUid();

                Picture pictures = new Picture(caption, pictureURL, latitude, longitude, ownerUid, comments);
                mPhotosRef.push().setValue(pictures);
                Intent intent = new Intent(PictureActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
