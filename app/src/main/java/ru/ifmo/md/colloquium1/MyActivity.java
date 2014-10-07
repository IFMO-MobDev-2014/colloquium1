package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MyActivity extends Activity {

    private SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        snakeView = (SnakeView) findViewById(R.id.snakeView);
        snakeView.setOnScoreChangedListener(new Snake.ScoreChangedListener() {
            @Override
            public void onScoreChanged(int score) {
                Log.d("SCORE", score + "");
            }
        });
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

    public void onLeftClick(View view) {
        snakeView.rotate(Snake.Direction.LEFT);
    }

    public void onRightClick(View view) {
        snakeView.rotate(Snake.Direction.RIGHT);
    }

    public void onRestartClick(View view) {

    }
}
