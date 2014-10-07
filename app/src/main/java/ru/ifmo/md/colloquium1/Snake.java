package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Яна on 07.10.2014.
 */
public class Snake extends SurfaceView implements Runnable{
    int x;
    int y;
    int i;
    int width = 40;
    int height = 60;
    int cnt = 0;
    int[][] field = new int[width][height];
    int[] linear_field = new int[width * height];
    List<int[]> snake;

    int dx = 0;
    int dy = 0;
    float widthScale;
    float heightScale;

    int randx;
    int randy;
    SurfaceHolder holder;
    Thread thread1 = null;
    Thread thread2 = null;
    volatile boolean running = false;

    public Snake(Context context) {
        super(context);
        holder = getHolder();
    }

    public void resume() {
        running = true;
        thread1 = new Thread(this);
        thread1.start();
    }

    public void pause() {
        running = false;
        try {
            thread1.join();
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
                long finishTime = System.nanoTime();
            }
        }
    }


    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        widthScale = (float)w / width;
        heightScale = (float)h / height;
        initField();
    }

    void initField() {
        snake = new ArrayList<int[]>();
        Random rand = new Random();
        for (i = 0; i < 50; i++) {
            randx = rand.nextInt(40);
            randy = rand.nextInt(60);
            field[randx][randy] = Color.RED;
        }
        int[] tmp;
        for (i = 0; i < 3; i++) {
            tmp = new int[2];
            tmp[0] = 20;
            tmp[1] = 30 + i;
            snake.add(tmp);
        }
        dx = 1;
        dy = 0;
    }

    void updateField() {
        for (i = 0; i < snake.size(); i++) {
            field[snake.get(i)[0]][snake.get(i)[1]] = Color.BLACK;
        }
        int[] tmp = new int[2];
        tmp[0] = snake.get(snake.size() - 1)[0] + dx;
        tmp[1] = snake.get(snake.size() - 1)[1] + dy;
        snake.remove(0);
        if (tmp[0] >= width)
            tmp[0] = 0;
        else if (tmp[0] < 0) {
            tmp[0] = width - 1;
        } else if (tmp[1] >= height) {
            tmp[1] = 0;
        } else if (tmp[1] < 0) {
            tmp[1] = height - 1;
        }
        if (field[tmp[0]][tmp[1]] == Color.RED) {
            int[] tmp1 = new int[2];
            tmp1[0] = tmp[0] + dx;
            tmp1[1] = tmp[1] + dy;
            if (tmp1[0] >= width)
                tmp1[0] = 0;
            else if (tmp1[0] < 0) {
                tmp1[0] = width - 1;
            } else if (tmp1[1] >= height) {
                tmp1[1] = 0;
            } else if (tmp1[1] < 0) {
                tmp1[1] = height - 1;
            }
            snake.add(tmp1);
        }
        snake.add(tmp);
        for (i = 0; i < snake.size(); i++) {
            field[snake.get(i)[0]][snake.get(i)[1]] = Color.GREEN;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        cnt++;
        Random rand = new Random();
        if (cnt == 5) {
            cnt = 0;
            dx = rand.nextInt(2) - 1;
            if (dx == 0)
                dy = rand.nextInt(2) - 1;
            else
                dy = 0;
        }
        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                linear_field[y * width + x] = field[x][y];
            }
        }

        canvas.scale(widthScale, heightScale);
        canvas.drawBitmap(linear_field, 0, width, 0, 0, width, height, false, null);
    }

}