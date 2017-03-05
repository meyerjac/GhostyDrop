package com.example.guest.ghostydrop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        ButterKnife.bind(this);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.loginButton);
        final TextView GhostDropText = (TextView) findViewById(R.id.welcomeTextView);
        final TextView GhostDropText2 = (TextView) findViewById(R.id.welcomeTextView2);
        final TextView GhostDropText3 = (TextView) findViewById(R.id.welcomeTextView3);
        final TextView GhostDropText4 = (TextView) findViewById(R.id.welcomeTextView4);
        final RelativeLayout LearnBox = (RelativeLayout) findViewById(R.id.exploreRelativeLayoutBox);


        GhostDropText.setVisibility(View.INVISIBLE);
        GhostDropText2.setVisibility(View.INVISIBLE);
        GhostDropText3.setVisibility(View.INVISIBLE);
        GhostDropText4.setVisibility(View.INVISIBLE);
        LearnBox.setVisibility(View.INVISIBLE);

        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fadein);
                GhostDropText.startAnimation(fadeIn);
            }
        }, 100);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fadeout);
                GhostDropText.startAnimation(fadeAnimation);

            }
        }, 700);

        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fadein);
                GhostDropText2.startAnimation(fadeIn);

            }
        }, 500);
        final Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation staythenslide = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fadeout);
                GhostDropText2.startAnimation(staythenslide);

            }
        }, 1100);

        final Handler handler5 = new Handler();
        handler5.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fadein);
                GhostDropText3.startAnimation(fadeIn);

            }
        }, 900);
        final Handler handler6 = new Handler();
        handler6.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation staythenslide = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fadeout);
                GhostDropText3.startAnimation(staythenslide);

            }
        }, 1500);
        final Handler handler7 = new Handler();
        handler7.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation slowfadein = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slowwwfadein);
                GhostDropText4.startAnimation(slowfadein);
                GhostDropText4.setVisibility(View.VISIBLE);
            }
        }, 1500);

        final Handler handler8 = new Handler();
        handler8.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation slideUp = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.move_up_slow);
                LearnBox.startAnimation(slideUp);
                LearnBox.setVisibility(View.VISIBLE);

            }
        }, 1000);
    }


    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
