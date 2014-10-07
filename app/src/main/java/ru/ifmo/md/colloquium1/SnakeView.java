package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author nqy
 */


public class SnakeView extends SurfaceView implements Runnable {
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    private int width = 40;
    private int height = 60;
    private Direction currentDirection = Direction.RIGHT;
    private Rect current;
    private Rect scaled;
    private int[][] field;
    private Bitmap bitmap = null;
    private ArrayList<Pair<Integer, Integer>> snake;
    private int score;
    private Paint paint = new Paint();
    private Handler handler;

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
        initField();
        paint.setTextSize(40);
        handler = new Handler();
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

    private void initField() {
        field = new int[width][height];
        current = new Rect(0, 0, width, height);
        scaled = current;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        score = 0;

        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[i].length; ++j) {
                field[i][j] = Color.WHITE;
            }
        }

        snake = new ArrayList<Pair<Integer, Integer>>();
        snake.add(new Pair<Integer, Integer>(20, 30));
        snake.add(new Pair<Integer, Integer>(20, 29));
        snake.add(new Pair<Integer, Integer>(20, 28));
        for (Pair<Integer, Integer> pair : snake) {
            field[pair.first][pair.second] = Color.GREEN;
        }

        int count = 0;
        Random random = new Random();
        while (count != 50) {
            int x = random.nextInt(40);
            int y = random.nextInt(60);
            if (field[x][y] == Color.WHITE) {
                field[x][y] = Color.RED;
                ++count;
            }
        }
    }

    private void updateField() {
        Pair<Integer, Integer> head = snake.get(0);
        int newX = head.first;
        int newY = head.second;
        int possible;
        switch (currentDirection) {
            case LEFT:
                possible = (newX - 1) % width;
                newX = possible < 0 ? width + 1 + possible : possible;
                break;
            case TOP:
                possible = (newY - 1) % height;
                newY = possible < 0 ? height + 1 + possible : possible;
                break;
            case RIGHT:
                newX = (newX + 1) % width;
                break;
            case BOTTOM:
                newY = (newY + 1) % height;
                break;
        }
        if (field[newX][newY] == Color.GREEN) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "You lose" , Toast.LENGTH_LONG)
                            .show();
                }
            });
        } else if (field[newX][newY] == Color.RED) {
            snake.add(0, new Pair<Integer, Integer>(newX, newY));
            field[newX][newY] = Color.GREEN;
            ++score;
            if (score == 50) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "You win" , Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        } else {
            Pair<Integer, Integer> old = snake.get(snake.size() - 1);
            snake.remove(snake.size() - 1);
            snake.add(0, new Pair<Integer, Integer>(newX, newY));
            field[newX][newY] = Color.GREEN;
            field[old.first][old.second] = Color.WHITE;
        }
    }

    @Override
    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                updateField();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                Random random = new Random();
                if (random.nextBoolean())
                try {
                    Thread.sleep(100);
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaled = new Rect(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (field[x][y] != bitmap.getPixel(x, y)) {
                    bitmap.setPixel(x, y, field[x][y]);
                }
            }
        }
        canvas.drawBitmap(bitmap, current, scaled, null);

        canvas.drawText("score: " + score, 40, 40, paint);
    }

    public void changeDirection(boolean where) {
        switch (currentDirection) {
            case LEFT:
                currentDirection = where ? Direction.TOP : Direction.BOTTOM;
                break;
            case TOP:
                currentDirection = where ? Direction.RIGHT : Direction.LEFT;
                break;
            case RIGHT:
                currentDirection = where ? Direction.BOTTOM : Direction.TOP;
                break;
            case BOTTOM:
                currentDirection = where ? Direction.LEFT : Direction.RIGHT;
                break;
        }
    }

    enum Direction {
        LEFT, TOP, RIGHT, BOTTOM
    }
}
