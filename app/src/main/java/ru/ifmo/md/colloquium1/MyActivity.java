package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class MyActivity extends Activity {

    private SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        snakeView = (SnakeView) findViewById(R.id.surface);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                snakeView.turnLeft();
                break;
            case R.id.right:
                snakeView.turnRight();
                break;
            case R.id.reset:
                snakeView.resetGame();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeView.pause();
    }
}
