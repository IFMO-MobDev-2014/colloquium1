package com.example.myapplication;

/**
 * Created by Амир on 07.10.2014.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayDeque;
import java.util.Random;

public class MyView extends SurfaceView implements Runnable {
    int width = 0;
    int height = 0;
    Bitmap bitmap;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;

    Paint paint;
    RectF rectf;


    public enum Direction {
        TOP, RIGHT, DOWN, LEFT;
    }

    public static final int NUM_OF_MEAL = 50;
    public static final int WIDTH = 40;
    public static final int HEIGHT = 60;

    public class Pair {
        int x;
        int y;

        public Pair(int a, int b) {
            x = a;
            y = b;
        }

        public Pair(Pair a) {
            x = a.x;
            y = a.y;
        }
    }

    int[][] field;
    ArrayDeque<Pair> snake;
    Direction dir;
    int time;
    int score;
    boolean lost;
    Random r;
    //0 - free
    //1 - snake
    //2 - meal


    public MyView(Context context) {
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
        } catch (InterruptedException ignore) {
        }
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                updateField();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;
        rectf = new RectF(0, 0, width, height);
        initField();
    }

    void initField() {
        lost = false;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        time = 0;
        score = 0;
        paint = new Paint();
        paint.setColor(0xFF000000);
        paint.setTextSize(40);
        dir = Direction.DOWN;
        field = new int[WIDTH][HEIGHT];
        r = new Random();
        snake = new ArrayDeque<Pair>();
        snake.addFirst(new Pair(20, 30));
        snake.addFirst(new Pair(20, 31));
        snake.addFirst(new Pair(20, 32));
        field[20][30] = 1;
        field[20][31] = 1;
        field[20][32] = 1;
        for (int i = 0; i < NUM_OF_MEAL; i++) {
            int x = r.nextInt(WIDTH);
            int y = r.nextInt(HEIGHT);
            while (field[x][y] != 0) {
                x = r.nextInt(WIDTH);
                y = r.nextInt(HEIGHT);
            }
            field[x][y] = 2;
        }

    }

    Direction opposite(Direction x) {
        if (x == Direction.DOWN) {
            return Direction.TOP;
        } else if (x == Direction.TOP) {
            return Direction.DOWN;
        } else if (x == Direction.LEFT) {
            return Direction.RIGHT;
        } else return Direction.LEFT;
    }

    void updateField() {
        time++;
        Pair head = new Pair(snake.peekFirst());

        if (dir == Direction.TOP) {
            head.y = (head.y - 1 + HEIGHT) % HEIGHT;
        } else if (dir == Direction.DOWN) {
            head.y = (head.y + 1 + HEIGHT) % HEIGHT;
        } else if (dir == Direction.LEFT) {
            head.x = (head.x - 1 + WIDTH) % WIDTH;
        } else if (dir == Direction.RIGHT) {
            head.x = (head.x + 1 + WIDTH) % WIDTH;
        }
        int x = r.nextInt(3);

        if (time % 5 == 0) {
            while (Direction.values()[x] == opposite(dir)) {
                x = r.nextInt(3);
            }
            dir = Direction.values()[x];
        }

        if (field[head.x][head.y] == 2) {
            field[head.x][head.y] = 1;
            snake.addFirst(head);
            score++;
        } else if (field[head.x][head.y] == 0) {
            snake.addFirst(head);
            field[head.x][head.y] = 1;
            Pair tail = new Pair(snake.getLast());
            snake.pollLast();
            field[tail.x][tail.y] = 0;
        } else if (field[head.x][head.y] == 1) {
            running = false;
            lost = true;
        }


    }

    @Override
    public void draw(Canvas canvas) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int x2 = (int) (((double) x) / width * WIDTH);
                int y2 = (int) (((double) y) / height * HEIGHT);
                if (field[x2][y2] == 0) {
                    bitmap.setPixel(x, y, 0xFFFFFFFF);
                }
                if (field[x2][y2] == 1) {
                    bitmap.setPixel(x, y, 0xFF00FF00);
                }
                if (field[x2][y2] == 2) {
                    bitmap.setPixel(x, y, 0xFFFF0000);
                }
            }
        }
        canvas.drawBitmap(bitmap, null, rectf, paint);

        canvas.drawText("Your score: " + score, 0, 30, paint);
        if (lost) {
            paint.setTextSize(85);
            canvas.drawText("GAME OVER", 50, height/2, paint);
        }
    }
}
