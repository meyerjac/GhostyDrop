package com.example.guest.ghostydrop;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileDialogBoxFragment extends DialogFragment {
    private DatabaseReference mDatabaseRef;
    private String OwnerUid;
    private DatabaseReference mUserRef;
    private DatabaseReference msnapRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_edit_profile_dialog_box_fragment, container, false);

        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        Button submitButton = (Button) rootView.findViewById(R.id.postButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        mUserRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText Bio = (EditText) rootView.findViewById(R.id.bioEditTextView);
                String bio = Bio.getText().toString();
                EditText Birthday = (EditText) rootView.findViewById(R.id.ageEditTextView);
                String birthday = Birthday.getText().toString();
                EditText Email = (EditText) rootView.findViewById(R.id.emailEditTextView);
                String email = Email.getText().toString();

                Log.d("Edit", (bio + birthday + email));

                updateInfoInDataBase();
                dismiss();
            }
        });

        return rootView;
    }
    public void updateInfoInDataBase() {
//        Post newPost = new Post(postOwnersUid, postMessagePost, numberOfTimesFlagged, numberOfSidekicks, potentialSidekicks);
//        mDatabaseRef.push().setValue(newPost);
    }
}

