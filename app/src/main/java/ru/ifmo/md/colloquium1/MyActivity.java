package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
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

        final Context context = this;

        snakeView.setOnCollisionListener(new Snake.OnCollisionListener() {
            @Override
            public void onCollision() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Game over")
                        .setMessage("Game over")
                .setCancelable(false)
                .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        snakeView.restart();
                    }
                });
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
        snakeView.restart();
    }
}
