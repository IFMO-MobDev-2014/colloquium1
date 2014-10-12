package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gshark on 07.10.14.
 */
public class GameView extends SurfaceView implements Runnable {

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        holder = getHolder();
    }

    int [][] field = null;
    int [][] field2 = null;
    Bitmap bmap;
    Rect rect1;
    Rect rect2;
    int dw;
    int dh;
    int width = 40;
    int height = 60;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;


    public GameView(Context context) {
        super(context);
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
        } catch (InterruptedException ignore) {}
    }

    boolean isStarted = false;
    boolean isFirstInit = true;

    public void start() {
        isStarted = true;
        if (!isFirstInit) {
            initField();
        } else {
            isFirstInit = false;
        }
    }

    int dir = 3;
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};
    int score = 0;
    ArrayList<Pair<Integer, Integer>> snake;
    boolean canClick;
    boolean isGameOver = false;

    public int score() {
        return score;
    }
    public void right() {
        if (canClick) {
            dir--;
            if (dir < 0) {
                dir = 3;
            }
            canClick = false;
        }
    }

    public boolean gameOver() {
        return isGameOver;
    }

    public void left() {
        if (canClick) {
            dir++;
            if (dir > 3) {
                dir = 0;
            }
            canClick = false;
        }
    }

    private boolean isPointInSnake(Pair<Integer, Integer> point) {
        for (int i = 0; i < snake.size(); i++) {
            if (point.equals(snake.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                canClick = true;
                if(isStarted && !isGameOver) {
                    updateField();
                }
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        dw = w;
        dh = h;
        rect2 = new Rect(0, 0, w, h);
        initField();
    }

    void initField() {
        field = new int[width][height];
        field2 = new int[width][height];
        Random rand = new Random();
        isGameOver = false;
        score = 0;
        dir = 3;
        snake = new ArrayList<Pair<Integer, Integer>>();
        snake.add(Pair.create(width / 2, height / 2));
        snake.add(Pair.create(width / 2, height / 2 + 1));
        snake.add(Pair.create(width / 2, height / 2 + 2));
        for (int x=0; x< width; x++) {
            for (int y=0; y<height; y++) {
                field[x][y] = Color.YELLOW;
            }
        }
        for (int i = 0; i < snake.size(); i++) {
            int x = snake.get(i).first;
            int y = snake.get(i).second;
            field[x][y] = Color.RED;
        }
        for (int i = 0; i < 50; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            while (isPointInSnake(Pair.create(x, y)) || field[x][y] == Color.BLUE) {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            }
            field[x][y] = Color.BLUE;
        }
        bmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }

    private void updateField() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field2[x][y] = field[x][y];
            }
        }
        int x = snake.get(0).first;
        int y = snake.get(0).second;
        int new_x = x + dx[dir];
        int new_y = y + dy[dir];
        if (new_x >= width) {
            new_x = 0;
        }
        if (new_x < 0) {
            new_x = width - 1;
        }
        if (new_y >= height) {
            new_y = 0;
        }
        if (new_y < 0) {
            new_y = height - 1;
        }
        if (field[new_x][new_y] == Color.BLUE) {
            score++;
        } else {
            if (isPointInSnake(Pair.create(new_x, new_y))) {
                isGameOver = true;
            } else {
                int k = snake.size() - 1;
                field2[snake.get(k).first][snake.get(k).second] = Color.YELLOW;
                snake.remove(k);
            }
        }
        if (!gameOver()) {
            snake.add(0, Pair.create(new_x, new_y));
            field2[new_x][new_y] = Color.RED;
        }
        field = field2;
    }

    @Override
    public void draw(Canvas canvas) {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                bmap.setPixel(x, y, field[x][y]);
            }
        }
        canvas.drawBitmap(bmap, rect1, rect2, null);
    }
}
