package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class MyActivity extends Activity {

    MySurfaceView mySurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        mySurfaceView = new MySurfaceView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mySurfaceView.setLayoutParams(layoutParams);
        layout.addView(mySurfaceView);
    }

    public void onLeftClicked(View view) {
        mySurfaceView.turnLeft();
    }

    public void onRestartClicked(View view) {
        mySurfaceView.restartEverything();
    }

    public void onRightClicked(View view) {
        mySurfaceView.turnRight();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.pause();
    }
}

