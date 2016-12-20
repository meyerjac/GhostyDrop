package com.example.guest.ghostydrop;

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
    @Bind(R.id.action_photo)
    Button mAction_photo;
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

    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private DatabaseReference mUserInputObjectReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserInputObjectReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_PHOTOS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSaveObject.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
//        if (v == mAction_photo) {
//            onLaunchCamera();
//        }
            if (v == mSaveObject) {
              String longitude = mLongitudeText.getText().toString();


                saveLongitudeToFirebase(longitude);

            }
    }

    public void saveLongitudeToFirebase(String longtitude) {
        mUserInputObjectReference.setValue(longtitude);
    }

//    public void onLaunchCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageLabel.setImageBitmap(imageBitmap);
//            encodeBitmapAndSaveToFirebase(imageBitmap);
//        }
//    }
//
//    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(mRestaurant.getPushId())
//                .child("imageUrl");
//        ref.setValue(imageEncoded);
//    }
}
