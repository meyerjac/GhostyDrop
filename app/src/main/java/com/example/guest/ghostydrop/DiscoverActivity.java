package com.example.guest.ghostydrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.guest.ghostydrop.adapters.PhotoListAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DiscoverActivity extends AppCompatActivity {
    public static final String TAG = DiscoverActivity.class.getSimpleName();

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private PhotoListAdapter mAdapter;
    public ArrayList<Picture> mPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ButterKnife.bind(this);

        String location = "hello";
    }

    private void getPictures(String location) {

        DiscoverActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mAdapter = new PhotoListAdapter(getApplicationContext(), mPictures);
                mRecyclerView.setAdapter(mAdapter);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(DiscoverActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);
            }
        });
    }
}