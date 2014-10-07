package ru.ifmo.md.lesson1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by thevery on 11/09/14.
 */
class WhirlView extends SurfaceView implements Runnable {
    enum directions {
        LEFT, RIGHT, UP, DOWN
    }

    TextView scores;
    private int width = 40;
    private int height = 60;
    int[][] field = new int[height][width];
    Random rnd = new Random(328943294l);
    float scaleX = 0;
    float scaleY = 0;
    private int score;
    private int food;
    volatile boolean ready;
    int[] bitmapArray = new int[width * height];
    int[] palette = {0xFFFF0000, 0xFF800000, 0xFF808000, 0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080, 0xFF800080, 0xFFFFFFFF};
    //    final int MAX_COLOR = 21;
    //  int[] palette = {0xFFFF0000, 0xFF800000, 0xFF808000, 0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080, 0xFF800080, 0xFFFFFFFF, 0xFF86E500, 0xFF40E100, 0xFF00DD03, 0xFF00D944, 0xFF00D583, 0xFF00D2BF, 0xFF00A2CE, 0xFF0062CA, 0xFF0024C6, 0xFF1600C2, 0xFF4E00BF};
    ArrayList<Point> snake;
    directions direction;
    final int MAX_COLOR = palette.length;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    Boolean game;
    Context cont;
    public WhirlView(Context context) {
        super(context);
        cont = context;
        holder = getHolder();
    }


    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ignore) {
        }
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                updateField();
                render(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = (float) w / width;
        scaleY = (float) h / height;
        initField();
    }

    void initField() {
        score = 0;
        food = 10;
        scores.post(new Runnable() {
            public void run() {
                scores.setText("" + score);
            }
        });
        game = true;
        int snakeX = rnd.nextInt(width);
        int snakeY = rnd.nextInt(height);
        snake = new ArrayList<Point>();
        snake.add(new Point(snakeX, snakeY));
        snake.add(new Point((snakeX + 1) % width, snakeY));
        snake.add(new Point((snakeX + 2) % width, snakeY));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[y][x] = Color.BLACK;
            }
        }
        field[snakeY][snakeX] = Color.GREEN;
        field[snakeY][(snakeX + 1) % width] = Color.GREEN;
        field[snakeY][(snakeX + 2) % width] = Color.GREEN;
        int foodX;
        int foodY;
        for (int i = 0; i < food; i++) {
            foodX = rnd.nextInt(width);
            foodY = rnd.nextInt(height);
            while (field[foodY][foodX] != Color.BLACK) {
                foodX = rnd.nextInt(width);
                foodY = rnd.nextInt(height);
            }
            field[foodY][foodX] = Color.RED;
        }
        direction = directions.RIGHT;
    }

    private void win() {
        game = false;
        new ToastMsg().execute("You are the WINNER!");
    }

    private void lose() {
        game = false;
        new ToastMsg().execute("GameOver...");
    }

    private class ToastMsg extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    private void updateSnake() {
        Point nextStep = snake.get(snake.size() - 1);
        int nextX = nextStep.x;
        int nextY = nextStep.y;
        if (food == score) {
            win();
            return;
        }
        if (direction == directions.RIGHT) {
            nextX = (nextX + 1) % width;
        } else if (direction == directions.LEFT) {
            nextX = (nextX - 1);
            if (nextX < 0) {
                nextX += width;
            }
        } else if (direction == directions.UP) {
            nextY = (nextY + 1) % height;
        } else if (direction == directions.DOWN) {
            nextY = (nextY - 1);
            if (nextY < 0) {
                nextY += height;
            }
        }
        int cell = field[nextY][nextX];
        if (cell == Color.BLACK || (cell == Color.GREEN
                && (nextX == snake.get(0).x && nextY == snake.get(0).y))) {
            field[snake.get(0).y][snake.get(0).x] = Color.BLACK;
            for (int i = 1; i < snake.size(); i++) {
                snake.set(i - 1, snake.get(i));
            }
            snake.set(snake.size() - 1, new Point(nextX, nextY));
            field[nextY][nextX] = Color.GREEN;
        } else if (cell == Color.RED) {
            snake.add(new Point(nextX, nextY));
            score++;
            scores.post(new Runnable() {
                public void run() {
                    scores.setText("" + score);
                }
            });
            field[nextY][nextX] = Color.GREEN;
        } else if (cell == Color.GREEN && (nextX != snake.get(0).x || nextY != snake.get(0).y)) {
            game = false;
            lose();
        }
    }

    void updateField() {
        if (game)
            updateSnake();
    }

    public WhirlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
    }

    public WhirlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        holder = getHolder();
    }

    public void render(Canvas canvas) {
        long startTime = System.nanoTime();
        for (int y = 0; y < height; y++) {
            System.arraycopy(field[y], 0, bitmapArray, y * width, width);
        }
        canvas.scale(scaleX, scaleY);
        canvas.drawBitmap(bitmapArray, 0, width, 0, 0, width, height, false, null);
        long finishTime = System.nanoTime();
        Log.i("onDrawCycle", "" + (finishTime - startTime) / 1000000);
    }
}
