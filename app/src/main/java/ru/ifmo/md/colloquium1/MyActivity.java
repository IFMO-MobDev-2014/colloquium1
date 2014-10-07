package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;


public class MyActivity extends Activity {
    private SnakeView snakeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        snakeView = (SnakeView)findViewById(R.id.surfaceView);
    }
    @Override
    public void onPause() {
        super.onPause();
        snakeView.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        snakeView.resume();
    }
    public void goClick(View view) {
        switch (view.getId()) {
            case R.id.leftBtn:
                snakeView.leftBtn();
                break;
            case R.id.rightBtn:
                snakeView.rightBtn();
                break;
            case R.id.repBtn:
                snakeView.replay();
                break;
        }
    }
}
