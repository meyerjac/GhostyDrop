package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.ghostydrop.Constructors.Profile;
import com.example.guest.ghostydrop.util.Android_Gesture_Detector;
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
    public static final String TAG = "debug";
    private DatabaseReference mUserRef;
    private DatabaseReference msnapRef;
    private GestureDetector mGestureDetector;
    private String facebookData;

    @Bind(cameraLogo) ImageView CameraLogo;
    @Bind(R.id.searchLogo) ImageView SearchLogo;
    @Bind(R.id.profileImageView) ImageView ProfileLogo;
    @Bind(R.id.projectPurpose) TextView ProjectPurpose;
    @Bind(R.id.projectPurpose2) TextView ProjectPurpose2;
    @Bind(R.id.projectTechnologies) TextView ProjectTechnologies;
    @Bind(R.id.projectTechnologies2) TextView ProjectTechnologies2;
    @Bind(R.id.whyMe) TextView WhyMe;
    @Bind(R.id.whyMe2) TextView WhyMe2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SearchLogo.setOnClickListener(this);
        CameraLogo.setOnClickListener(this);
        ProfileLogo.setOnClickListener(this);

        //references
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

        //Gesture handler, this is where every action is handled
        Android_Gesture_Detector custom_gesture_detector = new Android_Gesture_Detector() {
            @Override
            public void onSwipeRight() {
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewsslideright);
                        ProjectPurpose.startAnimation(moveUp);
                        ProjectPurpose2.startAnimation(moveUp);
                        ProjectPurpose.setVisibility(View.INVISIBLE);
                        ProjectPurpose2.setVisibility(View.INVISIBLE);

                    }
                }, 100);
                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewsslideright);
                        ProjectTechnologies.startAnimation(moveUp);
                        ProjectTechnologies2.startAnimation(moveUp);
                        ProjectTechnologies.setVisibility(View.INVISIBLE);
                        ProjectTechnologies2.setVisibility(View.INVISIBLE);
                    }
                }, 200);
                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewsslideright);
                        WhyMe.startAnimation(moveUp);
                        WhyMe2.startAnimation(moveUp);
                        WhyMe.setVisibility(View.INVISIBLE);
                        WhyMe2.setVisibility(View.INVISIBLE);
                    }
                }, 300);
                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this,   ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);

                    }
                }, 400);
            }

            public void onSwipeLeft() {
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewslideleft);
                        ProjectPurpose.startAnimation(slideRight);
                        ProjectPurpose2.startAnimation(slideRight);
                        ProjectPurpose.setVisibility(View.INVISIBLE);
                        ProjectPurpose2.setVisibility(View.INVISIBLE);

                    }
                }, 100);
                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewslideleft);
                        ProjectTechnologies.startAnimation(slideRight);
                        ProjectTechnologies2.startAnimation(slideRight);
                        ProjectTechnologies.setVisibility(View.INVISIBLE);
                        ProjectTechnologies2.setVisibility(View.INVISIBLE);
                    }
                }, 200);
                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewslideleft);
                        WhyMe.startAnimation(slideRight);
                        WhyMe2.startAnimation(slideRight);
                        WhyMe.setVisibility(View.INVISIBLE);
                        WhyMe2.setVisibility(View.INVISIBLE);
                    }
                }, 300);
                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this,   FindPictureListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                    }
                }, 400);
            }
        };
        mGestureDetector = new GestureDetector(this, custom_gesture_detector);
    }

    //responsible for touch events, handles them
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (true) {
            mGestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    //click listeners for all our view elements
    @Override
    public void onClick(View v) {
        if (v == CameraLogo) {
        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if (v == SearchLogo) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewslideleft);
                    ProjectPurpose.startAnimation(moveUp);
                    ProjectPurpose2.startAnimation(moveUp);
                    ProjectPurpose.setVisibility(View.INVISIBLE);
                    ProjectPurpose2.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewslideleft);
                    ProjectTechnologies.startAnimation(moveUp);
                    ProjectTechnologies2.startAnimation(moveUp);
                    ProjectTechnologies.setVisibility(View.INVISIBLE);
                    ProjectTechnologies2.setVisibility(View.INVISIBLE);
                }
            }, 200);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewslideleft);
                    WhyMe.startAnimation(moveUp);
                    WhyMe2.startAnimation(moveUp);
                    WhyMe.setVisibility(View.INVISIBLE);
                    WhyMe2.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,   FindPictureListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                }
            }, 400);
        } else if (v ==ProfileLogo) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewsslideright);
                    ProjectPurpose.startAnimation(moveUp);
                    ProjectPurpose2.startAnimation(moveUp);
                    ProjectPurpose.setVisibility(View.INVISIBLE);
                    ProjectPurpose2.setVisibility(View.INVISIBLE);

                }
            }, 100);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewsslideright);
                    ProjectTechnologies.startAnimation(moveUp);
                    ProjectTechnologies2.startAnimation(moveUp);
                    ProjectTechnologies.setVisibility(View.INVISIBLE);
                    ProjectTechnologies2.setVisibility(View.INVISIBLE);
                }
            }, 200);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation moveUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.viewsslideright);
                    WhyMe.startAnimation(moveUp);
                    WhyMe2.startAnimation(moveUp);
                    WhyMe.setVisibility(View.INVISIBLE);
                    WhyMe2.setVisibility(View.INVISIBLE);
                }
            }, 300);
            final Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,   ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);

                }
            }, 400);
        }
    }

//making Facebook API call only if the person doesn't have a profile set up, if they do it
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
            String bio = "Insert awesome bio for your everyone to see, also include some fun facts about you!";
            String birthday = "25";
            String email = "";
            String facebookId = "";
            String picture = "";
            String latitude = "";
            String longitude = "";
            Boolean firstLogin = true;

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
            Profile profile = new Profile(firstName, lastName, displayName, bio, birthday, email, facebookId, longitude, latitude, firstLogin, picture);
            mUserRef.setValue(profile);

        } catch (JSONException e) {
            Log.d("hello", "caught exception");
        }
    }
}





