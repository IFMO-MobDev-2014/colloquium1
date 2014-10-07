package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

class Snake extends SurfaceView implements Runnable {
    int [][] field = null;
    Bitmap bitmap;
    Rect src, dst;
    public static int count = 0;
    public static int width = 0;
    public static int height = 0;
    public static int snake_length = 3;
    public static int direction = 1;
    public static int dx = -1;
    public static int dy = 0;
    public static int headx = 0;
    public static int heady = 0;
    public static int snake_end = 3;
    public static int snake_begin = 0;
    int [] snake_placex = new int[400 * 600];
    int [] snake_placey = new int[400 * 600];
    public static int snake_color = Color.GREEN;
    public static int food_color = Color.RED;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;

    public Snake(Context context) {
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

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                if (count % 5 == 0)
                    updateField(1);
                else
                    updateField(0);
                onDraw(canvas);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignore) {}
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        height = 60;
        width = 40;
        if (w > h) {
            int temp = height;
            width = height;
            height = temp;
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        initField(w, h);
    }

    void initField(int w, int h) {
        Random rand = new Random();
        field = new int [width][height];
        int i = 0;
        for (int x = 0; x < 40; x++)
            for (int y = 0; y < 60; y++)
                field[x][y] = Color.WHITE;
        while (i < 50) {
            int x = rand.nextInt(35);
            int y = rand.nextInt(58);
            if(field[x][y] == Color.WHITE) {
                field[x][y] = food_color;
                i++;
            }
        }
        snake_placex[0] = 39;
        snake_placex[1] = 38;
        snake_placex[2] = 37;
        snake_placey[0] = 59;
        snake_placey[1] = 59;
        snake_placey[2] = 59;
        heady = 59;
        headx = 37;
        for (int x = 0; x < snake_length; x++)
            field[snake_placex[x]][snake_placey[x]] = snake_color;
        src = new Rect(0, 0, width, height);
        dst = new Rect(0, 0, w, h);
    }

    void updateField(int n) {
        if (n == 1) {
            if (direction == 1) {
                direction = 2;
                dx = -1;
                dy = 0;
            }
            if (direction == 2) {
                direction = 3;
                dx = 0;
                dy = -1;
            }
            if (direction == 3) {
                direction = 4;
                dx = 1;
                dy = 0;
            }
            if (direction == 4) {
                direction = 1;
                dx = 0;
                dy = 1;
            }
        }
        headx += dx;
        heady += dy;
        if (headx == 40)
            headx = 0;
        if (heady == 60)
            heady = 0;
        if (heady == -1)
            heady = 59;
        if (headx == -1)
            headx = 39;
        count++;
        if (field[headx][heady] == Color.GREEN) {
            initField(width, height);
        }
        if (field[headx][heady] == Color.WHITE) {
            snake_placex[snake_end] = headx;
            snake_placey[snake_end] = heady;
            field[snake_placex[snake_begin]][snake_placey[snake_begin]] = Color.WHITE;
            field[snake_placex[snake_end]][snake_placey[snake_end]] = Color.GREEN;
            snake_end++;
            snake_length++;
            snake_begin++;
        }
        if (field[headx][heady] == Color.RED) {
            snake_placex[snake_end] = headx;
            snake_placey[snake_end] = heady;
            field[snake_placex[snake_end]][snake_placey[snake_end]] = Color.GREEN;
            snake_end++;
            snake_length++;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        int count = 0;
        int [] color = new int [width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                color[count++] = field[x][y];
            }
        }
        bitmap.setPixels(color, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

}