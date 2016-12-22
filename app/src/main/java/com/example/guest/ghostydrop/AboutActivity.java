package com.example.guest.ghostydrop;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.bullet1)
    TextView m1;
    @Bind(R.id.bullet2)
    TextView m2;
    @Bind(R.id.bullet3)
    TextView m3;
    @Bind(R.id.body_text3)
    TextView m4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        String fontPath = "fonts/Roboto-Regular.ttf";
        Typeface RobotoFont = Typeface.createFromAsset(getAssets(), fontPath);
        m1.setTypeface(RobotoFont);
        m2.setTypeface(RobotoFont);
        m3.setTypeface(RobotoFont);
        m4.setTypeface(RobotoFont);
    }
}
