package com.example.vitalik.coloc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MyActivity extends Activity {
    MySurfaceView mySurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySurfaceView = new MySurfaceView(this);
        setContentView(mySurfaceView);
    }
    @Override
    public void onPause() {
        mySurfaceView.pause();
        super.onPause();
    }
    @Override
    public void onResume() {
        mySurfaceView.resume();
        super.onResume();
    }
}
