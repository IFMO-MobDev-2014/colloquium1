package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SnakeActivity extends Activity {
    private SnakeView snakeView;
    private SnakeGame game;
    private TextView score;
    private int direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        snakeView = (SnakeView) findViewById(R.id.snake_view);
        score = (TextView) findViewById(R.id.score);

        Button left = (Button) findViewById(R.id.left);
        Button right = (Button) findViewById(R.id.right);
        Button restart = (Button) findViewById(R.id.restart);

        // set handlers
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeft();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRight();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
            }
        });

        restartGame();

        // run snake updater every 1000ms
        onTick();
    }

    public void restartGame() {
        direction = 0;
        // init game
        game = new SnakeGame(this, 5, 5, direction, 3);

        // randomly set food
        for (int i = 0; i < game.FOOD_COUNT; i++) {
            int x = (int)(Math.random() * snakeView.W);
            int y = (int)(Math.random() * snakeView.H);
            game.addFood(new SnakeGame.Pixel(x, y));
        }
    }

    public void setScore(int number) {
        score.setText("Score: " + number);
    }

    private void onTick() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        updateSnake();
                        onTick();
                    }
                }
        , 100);
    }

    private void updateSnake() {
        SurfaceHolder holder = snakeView.getHolder();
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            try {
                game.prolongateSnake();
                int[] pixels = game.getPixels();
                snakeView.setPixels(pixels);
                snakeView.redraw(canvas);
            } catch (SnakeGame.GameOverException e) {
                // show game over
                Toast.makeText(getApplicationContext(), "Game over", Toast.LENGTH_SHORT).show();
                // restart the game
                restartGame();
            } finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void onLeft() {
        direction--;
        if (direction < 0) {
            direction += 4;
        }
        game.setDirection(direction);
    }

    public void onRight() {
        direction++;
        if (direction >= 4) {
            direction -= 4;
        }
        game.setDirection(direction);
    }

    public void onRestart() {
        restartGame();
    }
}
