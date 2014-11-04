package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.Random;

class SnakeView extends SurfaceView implements Runnable {
    final int WIDTH = 40, HEIGHT = 60;
    final Rect SRC = new Rect(0, 0, WIDTH, HEIGHT);
    Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
    boolean[][] hasFood;
    boolean[][] hasSnake;
    final int FIELD_COLOR = 0xFF000000;
    final int SNAKE_COLOR = 0xFF00FF00;
    final int FOOD_COLOR = 0xFFFF0000;
    final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    public boolean gameOver;
    public boolean turned;
    Rect dst;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    int direction;
    LinkedList<Point> snake;
    boolean initted;
    Random rand;


    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
        rand = new Random();
        init();
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

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        dst = new Rect(0, 0, w - 1, h - 1);
    }

    void init() {
        initted = false;
        // init vars
        gameOver = false;
        direction = RIGHT;
        hasFood = new boolean[WIDTH][HEIGHT];
        hasSnake = new boolean[WIDTH][HEIGHT];
        turned = false;

        // create food
        int toChoose = 50;
        int left = WIDTH * HEIGHT;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int r = rand.nextInt(left);
                if (r < toChoose) {
                    toChoose--;
                    hasFood[x][y] = true;
                    bitmap.setPixel(x, y, FOOD_COLOR);
                } else {
                    bitmap.setPixel(x, y, FIELD_COLOR);
                }
                left--;
            }
        }

        // create snake
        if (snake == null) {
            snake = new LinkedList<Point>();
        } else {
            snake.clear();
        }
        for (int i = 0; i < 3; i++) {
            snake.add(new Point(i, 0));
        }
        for (Point p : snake) {
            bitmap.setPixel(p.x, p.y, SNAKE_COLOR);
        }
        initted = true;
    }

    void step() {
        if (initted) {
            turned = false;

            Point head = snake.peekLast();
            Point tail = snake.peekFirst();
            int x2 = head.x;
            int y2 = head.y;
            if (direction == UP) y2 = y2 == 0 ? HEIGHT - 1 : y2 - 1;
            if (direction == RIGHT) x2 = x2 == WIDTH - 1 ? 0 : x2 + 1;
            if (direction == DOWN) y2 = y2 == HEIGHT - 1 ? 0 : y2 + 1;
            if (direction == LEFT) x2 = x2 == 0 ? WIDTH - 1 : x2 - 1;
            if (hasSnake[x2][y2]) {
                // game over
                restart();
            } else {
                if (hasFood[x2][y2]) {
                    hasFood[x2][y2] = false;
                } else {
                    snake.removeFirst();
                    hasSnake[tail.x][tail.y] = false;
                    bitmap.setPixel(tail.x, tail.y, FIELD_COLOR);
                }
                snake.addLast(new Point(x2, y2));
                bitmap.setPixel(x2, y2, SNAKE_COLOR);
                hasSnake[x2][y2] = true;
            }

            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void turnRight() {
        if (!turned) {
            direction = direction + 1;
            if (direction == 4) direction = 0;
            turned = true;
        }
    }

    public void turnLeft() {
        if (!turned) {
            direction = direction - 1;
            if (direction == -1) direction = 3;
            turned = true;
        }
    }

    public void restart() {
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, SRC, dst, null);
    }

    @Override
    public void run() {

    }
}