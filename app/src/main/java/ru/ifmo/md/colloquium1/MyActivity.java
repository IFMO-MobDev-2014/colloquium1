package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class MyActivity extends Activity {

    private SnakeView snakeView;
    private TextView scoreIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        snakeView = (SnakeView) findViewById(R.id.snake_view);
        snakeView.setActivity(this);
        snakeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snakeView.tryToResume();
            }
        });
        scoreIndicator = (TextView) findViewById(R.id.score_indicator);
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

    public void turnLeft(View view) {
        snakeView.turnLeft();
    }

    public void turnRight(View view) {
        snakeView.turnRight();
    }

    public void restart(View view) {
        snakeView.restart();
    }

    public void gameOver() {
        snakeView.gameOvered = true;
        snakeView.pause();
    }

    public void updateScore(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreIndicator.setText(Integer.toString(score));
            }
        });
    }
}
