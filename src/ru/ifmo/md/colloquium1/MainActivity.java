package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        snakeView = (SnakeView) findViewById(R.id.field);
        snakeView.setScoreView((TextView) findViewById(R.id.score));
        snakeView.setMainActivity(this);
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

    public void right(View v) {
        if (!snakeView.directionChanged)
            snakeView.direction = (snakeView.direction + 1) % 4;
    }

    public void left(View v) {
        if (!snakeView.directionChanged)
            snakeView.direction = (snakeView.direction + 3) % 4;
    }

    public void reset(View v) {
        snakeView.initField();
    }

    public void setScore(int score) {
//        ((TextView) findViewById(R.id.score)).setText("Score: " + score);
    }

    public void makeGOToast() {
//        Toast t = Toast.makeText(this, "Game over", Toast.LENGTH_SHORT);
    }
}
