package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.guest.ghostydrop.Constructors.Profile;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.guest.ghostydrop.R.id.cameraLogo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main";
    private DatabaseReference mUserRef;
    private DatabaseReference msnapRef;

    private String Latitude;
    private String Longitude;
    private String facebookData;

    @Bind(cameraLogo)
    ImageView CameraLogo;
    @Bind(R.id.searchLogo)
    ImageView SearchLogo;
    @Bind(R.id.profileImageView)
    ImageView ProfileLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        mUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);
        msnapRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER);



        // Check to see if this is a first time user
        msnapRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    Log.d(TAG, "user profile already exists!");
                } else {
                    getResponses();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        SearchLogo.setOnClickListener(this);
        CameraLogo.setOnClickListener(this);
        ProfileLogo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == CameraLogo) {
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
            startActivity(intent);
        } else if (v == SearchLogo) {
            Intent intent = new Intent(MainActivity.this,   FindPictureListActivity.class);
            intent.putExtra("long", Longitude);
            intent.putExtra("lat", Latitude);
          startActivity(intent);
        } else if (v ==ProfileLogo) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
    }
//getting facebook data through API call only if the persom doesn't have a profile set up, if they do it
    public void getResponses() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me?fields=id,about,birthday,first_name,last_name,email,picture",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handling the result */
                        facebookData = String.valueOf(response.getJSONObject());
                        getFacebookProfileData();

                    }
                }
        ).executeAsync();
    }

//parsing data if method called, instantiating properties
    private void getFacebookProfileData() {
        try {
            String firstName = "";
            String lastName = "";
            String displayName = "";
            String bio = "Insert awesome bio for your friends to see!";
            String birthday = "10000";
            String email = "";
            String facebookId = "";
            String picture = "";


            String jsonData = facebookData;
            if (jsonData == null) {
                Toast.makeText(MainActivity.this, "it didnt work", Toast.LENGTH_LONG).show();
            }
                System.out.println("Response:" + facebookData);
                JSONObject facebookJSON = new JSONObject(jsonData);

            firstName = facebookJSON.getString("first_name");
            lastName= facebookJSON.getString("last_name");
            displayName = (firstName + " " + lastName.charAt(0));
            facebookId = facebookJSON.getString("id");
                JSONObject picturePicture = facebookJSON.getJSONObject("picture");
                JSONObject pictureData = picturePicture.getJSONObject("data");
            picture = pictureData.getString("url");

            Profile profile = new Profile(firstName, lastName, displayName, bio, birthday, email, facebookId, picture);
            mUserRef.setValue(profile);

        } catch (JSONException e) {
            Log.d("hello", "caught exception");
        }
    }
}





