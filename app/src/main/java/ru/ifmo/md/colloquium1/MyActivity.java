package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MyActivity extends Activity {
    private SnakeView snakeView;
    private FrameLayout frameLayout;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        snakeView = new SnakeView(this);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
        frameLayout.addView(snakeView, layoutParams);

        timer = new Timer();
        timer.schedule(new UpdateTask(), 0, 1000);
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

    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
            snakeView.step();
            /*if (snakeView.gameOver) {
                gameOver(snakeView.score);
                snakeView.restart();
            }*/
        }
    }
}
