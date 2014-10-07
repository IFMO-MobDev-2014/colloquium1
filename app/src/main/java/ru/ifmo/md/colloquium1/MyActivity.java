package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MyActivity extends Activity {
    final int WIDTH = 40;
    final int HEIGHT = 60;
    int [] field = new int[WIDTH*HEIGHT];
    FrameLayout layout;
    private SnakeView snakeView;
//    ImageView im = (ImageView) findViewById(R.id.imageView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        snakeView = new SnakeView(this);
        layout = (FrameLayout) findViewById(R.id.layout);
        layout.addView(snakeView);
    }

    @Override
    public void onResume() {
        super.onResume();
        snakeView.resume();
    }

    public void up(View view) {
        Cell c = snakeView.snake.get(snakeView.snake.size() - 1);
        if(c.d != Cell.Direction.DOWN) {
            c.d = Cell.Direction.UP;
            snakeView.snake.set(snakeView.snake.size() - 1, c);
        }
    }

    public void right(View view) {
        Cell c = snakeView.snake.get(snakeView.snake.size() - 1);
        if(c.d != Cell.Direction.LEFT) {
            c.d = Cell.Direction.RIGHT;
            snakeView.snake.set(snakeView.snake.size() - 1, c);
        }
    }
    public void down(View view) {
        Cell c = snakeView.snake.get(snakeView.snake.size() - 1);
        if(c.d != Cell.Direction.UP) {
            c.d = Cell.Direction.DOWN;
            snakeView.snake.set(snakeView.snake.size() - 1, c);
        }
    }
    public void left(View view) {
        Cell c = snakeView.snake.get(snakeView.snake.size() - 1);
        if(c.d != Cell.Direction.RIGHT) {
            c.d = Cell.Direction.LEFT;
            snakeView.snake.set(snakeView.snake.size() - 1, c);
        }
    }

    public void restart(View view) {
        snakeView.initField();
    }

    public void gameOver() {
        new AlertDialog.Builder(MyActivity.this)
                .setTitle("Game Over")
                .setMessage("Возникли проблемы с поиском изображений")
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        snakeView.pause();
    }

}
