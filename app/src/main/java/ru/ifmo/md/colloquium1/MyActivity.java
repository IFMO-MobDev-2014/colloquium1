package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {
    private SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snakeView = new SnakeView(this);
        setContentView(snakeView);
    }


    @Override
    public void onResume() {
        super.onResume();
        snakeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        snakeView.pause();
    }
}
