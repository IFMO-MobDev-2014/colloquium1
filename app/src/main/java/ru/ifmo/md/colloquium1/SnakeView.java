package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class SnakeView extends SurfaceView implements Runnable {
    TextView tw;
    int [][] field = null;
    int [][] field2 = null;
    int width = 60;
    int height = 40;
    ArrayList<Pair<Integer, Integer>> food;
    LinkedList<Pair<Integer, Integer>> snake;
    float scaleWidth = 1, scaleHeight = 1;
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Rect rect;
    boolean gameOver = false;
   // final int MAX_COLOR = 10;
    int[] palette = {Color.WHITE, Color.GREEN, Color.RED};
    SurfaceHolder holder;
    Thread thread = null;
    int direction = 0;
    Random rand = new Random();
    volatile boolean running = false;

    public SnakeView(Context context) {
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
                long startTime = System.nanoTime();

                Canvas canvas = holder.lockCanvas();
                updateField();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);

                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(1600);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        rect = new Rect(0, 0, width, height);
        scaleWidth = (float) w / width;
        scaleHeight = (float) h / height;
        initField();
    }

    void initField() {
        field = new int[width][height];
        snake = new LinkedList<Pair<Integer, Integer>>();
        food = new ArrayList<Pair<Integer, Integer>>();

        snake.add(new Pair<Integer, Integer>(rand.nextInt(width), rand.nextInt(height)));
        snake.add(new Pair<Integer, Integer>(snake.get(0).first + 1, snake.get(0).second));
        snake.add(new Pair<Integer, Integer>(snake.get(1).first + 1, snake.get(1).second));

        for (int i = 0; i < 50; i++) {
            food.add(new Pair<Integer, Integer>(rand.nextInt(width), rand.nextInt(height)));
        }

    }

    void right() {
        if (snake.get(0).first + 1 != width) {
            snake.addFirst(new Pair<Integer, Integer>(snake.get(0).first + 1, snake.get(0).second));
            snake.remove(snake.size() - 1);
        } else {
            snake.addFirst(new Pair<Integer, Integer>(0, snake.get(0).second));
            snake.remove(snake.size() - 1);
        }
    }

    void left() {
        if (snake.get(0).first - 1 != -1) {
            snake.addFirst(new Pair<Integer, Integer>(snake.get(0).first - 1, snake.get(0).second));
            snake.remove(snake.size()-1);
        } else {
            snake.addFirst(new Pair<Integer, Integer>(width - 1, snake.get(0).second));
            snake.remove(snake.size() - 1);
        }
    }

    void up() {
        if (snake.get(0).second + 1 != height) {
            snake.addFirst(new Pair<Integer, Integer>(snake.get(0).first, snake.get(0).second + 1));
            snake.remove(snake.size() - 1);
        } else {
            snake.addFirst(new Pair<Integer, Integer>(snake.get(0).first, 0));
            snake.remove(snake.size() - 1);
        }
    }

    void down() {
        if (snake.get(0).second - 1 != -1) {
            snake.addFirst(new Pair<Integer, Integer>(snake.get(0).first, snake.get(0).second - 1));
            snake.remove(snake.size()-1);
        } else {
            snake.addLast(new Pair<Integer, Integer>(snake.get(0).first, height));
            snake.remove(snake.size() - 1);
        }
    }

    void updateField() {
        int newDirection = rand.nextInt(4);

        while (Math.abs(newDirection - direction) == 1) {
            newDirection = rand.nextInt(4);
        }
        direction = newDirection;

        switch (direction) {
            case 0:
                right();
                break;
            case 1:
                left();
                break;
            case 2:
                down();
                break;
            case 3:
                up();
                break;
            default:
                break;
        }
        for (int i = 0; i < snake.size(); i++) {
            for (int j = 0; j < snake.size(); j++) {
                if (i == j) continue;
                if (snake.get(i) == snake.get(j)) {
                    gameOver = true;
                    pause();
                }
            }
        }

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                field[x][y] = 0;

        for (int i = 0; i < snake.size(); i++)
            field[snake.get(i).first][snake.get(i).second] = 1;

        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).first.equals(snake.get(0).first) && food.get(i).second.equals(snake.get(0).second)) {
                snake.add(new Pair<Integer, Integer>(food.get(i).first, food.get(i).second));
                food.remove(i);
            } else {
                field[food.get(i).first][food.get(i).second] = 2;
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.scale(scaleWidth, scaleHeight);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, palette[field[x][y]]);
            }
        }
        if (gameOver) {
            canvas.drawText("GAME OVER" + Integer.toString(snake.size()),10, 25, new Paint());
        }
        canvas.drawBitmap(bitmap, null, rect, null);
        canvas.drawText(Integer.toString(snake.size()),10, 10, new Paint());
    }
}