package ru.ifmo.md.colloquium1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeTimer extends AsyncTask<Void, Bitmap, Bitmap> {
    public static final int FIELD_HEIGHT = 60;
    public static final int FIELD_WIDTH = 40;
    public static final int FOOD_COUNT = 50;
    public static final int SCORE_FOR_ONE = 50;
    public static final int ONE_TICK = 80;
    public static final int SNAKE_COLOR = Color.GREEN;
    public static final int FOOD_COLOR = Color.RED;
    public static final String END_GAME_TEXT = "GAME OVER";
    private final Button newGameButton;
    private final ImageView snakeScreen;
    private final TextView scoreText;
    private final int[] gameField = new int[FIELD_HEIGHT * FIELD_WIDTH];
    private final int finalHeight;
    private final int finalWidth;
    private List<FieldPoint> snake;
    private int speedX;
    private int speedY;
    private int score;
    public boolean gameContinues = true;
    public boolean gameStopped = false;

    private class FieldPoint {
        private int x;
        private int y;

        private FieldPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public SnakeTimer(ImageView snakeScreen, int finalHeight, int finalWidth, Button left, Button right, TextView scoreText, Button newGameButton) {
        this.snakeScreen = snakeScreen;
        this.newGameButton = newGameButton;
        snake = new ArrayList<FieldPoint>();
        snake.add(new FieldPoint(FIELD_WIDTH / 2, FIELD_HEIGHT / 2));
        snake.add(new FieldPoint(FIELD_WIDTH / 2 + 1, FIELD_HEIGHT / 2));
        snake.add(new FieldPoint(FIELD_WIDTH / 2 + 2, FIELD_HEIGHT / 2));
        speedX = 1;
        speedY = 0;
        this.finalHeight = finalHeight;
        this.finalWidth = finalWidth;
        this.scoreText = scoreText;
        Random rnd = new Random();
        for (int i = 0; i < FOOD_COUNT; i++) {
            int newX = rnd.nextInt(FIELD_WIDTH);
            int newY = rnd.nextInt(FIELD_HEIGHT);
            if (gameField[newY * FIELD_WIDTH + newX] != FOOD_COLOR) {
                gameField[newY * FIELD_WIDTH + newX] = FOOD_COLOR;
            } else {
                i--;
            }
        }

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speedX == 1) {
                    speedY = -1;
                    speedX = 0;
                    return;
                }
                if (speedX == -1) {
                    speedY = 1;
                    speedX = 0;
                    return;
                }
                if (speedY == 1) {
                    speedY = 0;
                    speedX = 1;
                    return;
                }
                if (speedY == -1) {
                    speedY = 0;
                    speedX = -1;
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speedX == 1) {
                    speedY = 1;
                    speedX = 0;
                    return;
                }
                if (speedX == -1) {
                    speedY = -1;
                    speedX = 0;
                    return;
                }
                if (speedY == 1) {
                    speedY = 0;
                    speedX = -1;
                    return;
                }
                if (speedY == -1) {
                    speedY = 0;
                    speedX = 1;
                }
            }
        });
    }

    private void moveSnake() {
        List<FieldPoint> newSnake = new ArrayList<FieldPoint>();
        int newX = (snake.get(0).getX() + speedX + FIELD_WIDTH) % FIELD_WIDTH;
        int newY = (snake.get(0).getY() + speedY + FIELD_HEIGHT) % FIELD_HEIGHT;
        if (gameField[newY * FIELD_WIDTH + newX] == SNAKE_COLOR) {
            gameContinues = false;
        }
        newSnake.add(new FieldPoint(newX, newY));
        for (int i = 0; i < snake.size() - 1; i++) {
            newSnake.add(snake.get(i));
        }
        if (gameField[newY * FIELD_WIDTH + newX] == FOOD_COLOR) {
            newSnake.add(snake.get(snake.size() - 1));
            score += SCORE_FOR_ONE;
        }
        for (FieldPoint point : snake) {
            gameField[point.getY() * FIELD_WIDTH + point.getX()] = 0;
        }
        for (FieldPoint point : newSnake) {
            gameField[point.getY() * FIELD_WIDTH + point.getX()] = SNAKE_COLOR;
        }
        snake = newSnake;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        while (gameContinues) {
            long startTime = System.nanoTime();
            moveSnake();
            bitmap = Bitmap.createBitmap(gameField, FIELD_WIDTH, FIELD_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, false);
            publishProgress(bitmap);
            long endTime = System.nanoTime();
            try {
                if (ONE_TICK > (endTime - startTime) / 1000000) {
                    Thread.sleep(ONE_TICK - (endTime - startTime) / 1000000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        Log.d("Wut?", "Publish bitmap");
        snakeScreen.setImageBitmap(values[0]);
        scoreText.setText("Score: " + score);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (gameStopped) {
            return;
        }
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(140);

        Rect bounds = new Rect();
        paint.getTextBounds(END_GAME_TEXT, 0, END_GAME_TEXT.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawText(END_GAME_TEXT, x, y, paint);
        newGameButton.setVisibility(View.VISIBLE);
    }
}
