package ru.ifmo.md.colloquium1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.Integer;
import java.lang.InterruptedException;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.System;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.transform.URIResolver;

// Loskutov Ignat (2538)
/**
* Created by thevery on 11/09/14.
*/
class SnakeView extends SurfaceView implements Runnable {
    final int width = 40;
    final int height = 60;
    int food = 50;
    int [] field;
    ArrayList<Cell> snake;
    int wScale;
    int hScale;
    SurfaceHolder holder;
    Thread thread = null;
    final Paint paint = new Paint();
    volatile boolean running = false;
    Handler h;

    public SnakeView(Context context) {
        super(context);
        h = new Handler();
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
        Canvas canvas;
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                canvas = holder.lockCanvas();
                updateField();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        //width = w/scale;
        //height = h/scale;
        wScale = w/width;
        hScale = h/height;
        Log.i("w", w + " " + width + " " + wScale);
        Log.i("h", h + " " + height + " " + hScale);
        initField();
    }

    Pair<Integer, Integer> nextInd(Cell c) {
        int x = c.x;
        int y = c.y;
        Cell.Direction d = c.d;
        switch (d) {
            case UP:
                if(y == 0)
                    y = height - 1;
                else
                    y--;
                break;
            case RIGHT:
                if(x == width - 1)
                    x = 0;
                else
                    x++;
                break;
            case DOWN:
                if(y == height - 1)
                    y = 0;
                else
                    y++;
                break;
            case LEFT:
                if (x == 0)
                    x = width - 1;
                else
                    x--;
                break;
        }
        Log.i("next is", "(" + x + ", " + y + ")");
        return Pair.create(x, y);
    }

    void addFood(int i) {
        int ind = new Random().nextInt(width * height - snake.size() - i);
        for(Cell c : snake) {
            if(c.x + c.y * width == ind)
                ind++;
        }
        field[ind] = Color.RED;
    }

    void initField() {
        field = new int[width * height];
        snake = new ArrayList<Cell>();
        snake.add(new Cell(width / 2 - 1, height / 2, Cell.Direction.RIGHT));
        snake.add(new Cell(width/2, height/2, Cell.Direction.RIGHT));
        snake.add(new Cell(width / 2 + 1, height / 2, Cell.Direction.RIGHT));
        Random rand = new Random();
        for(int i = 0; i < food; i++) {
            addFood(i);
        }
    }

    void gameOver() {
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Game over", Toast.LENGTH_LONG).show();
            }
        });
    }


    void updateField() {
        Pair<Integer, Integer> p = nextInd(snake.get(snake.size() - 1));
        if(field[p.first + p.second * width] == Color.GREEN) {
            gameOver();
            return;
        }
        if(field[p.first + p.second * width] == Color.RED) {
            food--;
            field[p.first + p.second * width] = Color.GREEN;
            if(food == 0) {
                gameOver();
                return;
            }
            snake.add(new Cell(p.first, p.second, snake.get(snake.size() - 1).d));
        } else {
            Cell tail = snake.get(0);
            field[tail.x + tail.y * width] = Color.BLACK;
            for (int i = 0; i < snake.size(); i++) {
                Cell c = snake.get(i);
                if(i > 0) {
                    Cell prev = snake.get(i - 1);
                    prev.d = c.d;
                    snake.set(i - 1, prev);
                }
                p = nextInd(c);
                c.x = p.first;
                c.y = p.second;
                field[c.x + c.y * width] = Color.GREEN;
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.scale(wScale, hScale);
        canvas.drawBitmap(field, 0, width, 0, 0, width, height, false, paint);
    }
}
