package com.example.guest.ghostydrop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.ghostydrop.Constants;
import com.example.guest.ghostydrop.Constructors.Picture;
import com.example.guest.ghostydrop.OtherPersonsProfile;
import com.example.guest.ghostydrop.ProfileActivity;
import com.example.guest.ghostydrop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static com.example.guest.ghostydrop.R.id.numberOfLikes;

public class MySavedPicturesInProfileViewHolder extends RecyclerView.ViewHolder {
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 400;
    private SharedPreferences mSharedPreferences;
    private DatabaseReference PhotoOwnerRef;
    private DatabaseReference UserRef;
    private DatabaseReference mPhotosRef;
    private String mLat;
    private String mLong;
    View mView;
    Context mContext;

    public MySavedPicturesInProfileViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindPicture(final Picture picture) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mLat = mSharedPreferences.getString(Constants.LATITUDE, null);
        mLong = mSharedPreferences.getString(Constants.LONGITUDE, null);


        final TextView PhotoComment = (TextView) mView.findViewById(R.id.photoCommentTextView);
        TextView DistanceText = (TextView) mView.findViewById(R.id.distanceTextView);
        final TextView NumberOfLikes = (TextView) mView.findViewById(numberOfLikes);
        final TextView OwnerName = (TextView) mView.findViewById(R.id.postOwnerNameTextView);
        ImageView Image = (ImageView) mView.findViewById(R.id.photoImageView);
        final ImageButton WhiteStar = (ImageButton) mView.findViewById(R.id.whiteStarImageButton);
        final ImageButton YellowStar = (ImageButton) mView.findViewById(R.id.yellowStarImageButton);
        final ImageButton WhiteHeart = (ImageButton) mView.findViewById(R.id.whiteHeartImageButton);
        final ImageButton RedHeart = (ImageButton) mView.findViewById(R.id.redHeartImageButton);
        final ImageButton WhiteFlag = (ImageButton) mView.findViewById(R.id.whiteFlagImageButton);
        final ImageButton RedFlag = (ImageButton) mView.findViewById(R.id.redFlagImageButton);
        WhiteHeart.setVisibility(View.INVISIBLE);
        RedHeart.setVisibility(View.INVISIBLE);
        WhiteFlag.setVisibility(View.INVISIBLE);
        RedFlag.setVisibility(View.INVISIBLE);

        //user credentials and databaseReferences
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        UserRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USER)
                .child(uid);
        mPhotosRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_PHOTOS);
        PhotoComment.setText(picture.getCaption());
        final String postUid = picture.getOwnerUid();

        OwnerName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (postUid.equals(uid)) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    mContext.startActivity(intent);
                } else {
                    String PersonsProfile = postUid;
                    Intent intent = new Intent(mContext, OtherPersonsProfile.class);
                    intent.putExtra(Constants.USER_UID, picture.getOwnerUid());
                    mContext.startActivity(intent);
                }
            }
        });

        //setting fonts
        Typeface typeface = Typeface.createFromAsset(PhotoComment.getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface2 = Typeface.createFromAsset(OwnerName.getContext().getAssets(), "fonts/Rubik-MediumItalic.ttf");
        PhotoComment.setTypeface(typeface);
        OwnerName.setTypeface(typeface);
        DistanceText.setTypeface(typeface2);


        //checking how to load picture from database
        if (!picture.getImageUrl().contains("http")) {
            try {
                Bitmap imageBitmap = decodeFromFirebaseBase64(picture.getImageUrl());
                Image.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(mContext)
                    .load(picture.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(Image);
        }

        //retreiving photo owner uid
        PhotoOwnerRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(postUid);
        PhotoOwnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OwnerName.setText(dataSnapshot.child("displayName").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //gets the correct miles from those photos you are currently located

        double lat1 = Double.parseDouble(picture.getLatitude());
        double lat2 = Double.parseDouble(mLat);
        double lon1 = Double.parseDouble(picture.getLongitude());
        double lon2 = Double.parseDouble(mLong);
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);
        float distanceInMeters = loc1.distanceTo(loc2);
        double distanceInMiles = distanceInMeters * 0.000621371;
        double roundedMiles = (double) Math.round(distanceInMiles * 10d) / 10d;
        DistanceText.setText(roundedMiles + " miles away");


        UserRef.child("collectedPhotos").orderByChild("caption").equalTo(picture.getCaption()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.exists()); {
                    YellowStar.setVisibility(View.VISIBLE);
                    WhiteStar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });

        YellowStar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference exactPictureClickedonViaQuery = UserRef.child("collectedPhotos");
                final Query commentQuery = exactPictureClickedonViaQuery.orderByChild("caption").equalTo(picture.getCaption());
                commentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot DSnapshot : dataSnapshot.getChildren()) {
                            DSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                YellowStar.setVisibility(View.INVISIBLE);
                WhiteStar.setVisibility(View.INVISIBLE);
            }
        });
    };

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }
}
