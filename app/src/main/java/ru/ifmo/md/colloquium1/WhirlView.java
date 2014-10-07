package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

class WhirlView extends SurfaceView implements Runnable {
    int width = 40;
    int height = 60;
    int deviceWidth;
    int deviceHeight;
    int[][] field = null;
    int[][] updatedCell = null;
    int foodCount = 0;
    ArrayList<Point> snake = new ArrayList<Point>();
    int[] pixels = null;
    Random rand = new Random();
    int direction = 0;
    int[] dx = {0, 1, 0,  -1, 0, 0,  1, 1, 1};
    int[] dy = {-1,  0, 1, 0, 0, 1, -1, 0, 1};
    Bitmap place = null;
    Rect destRect = null;
    final int MAX_COLOR = 3;
    int[] palette = {0x00000, 0xFFFFFF, 0xEE0000,  0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080, 0xFF800080, 0xFFFFFFFF};
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;

    public WhirlView(Context context) {
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
                updateField();
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                reDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
//                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                Log.i("FPSe", String.valueOf(1000.0/((finishTime-startTime)/1000000.0)));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        deviceWidth = w;
        deviceHeight = h;
        destRect = new Rect(0, 0, w, h);
        initField();
    }

    private void initField() {
        field = new int[width][height];
        foodCount = 0;
        pixels = new int[width*height];
        place = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x=0; x<width; x++) {
            for (int y = 0; y<height; y++) {
                field[x][y] = 0;

                pixels[x + y*width] = palette[field[x][y]];
            }
        }

        snake = new ArrayList<Point>();
        Random rand = new Random();
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        snake.add(new Point(x, y));
        snake.add(updatedCell(x, y + 1));
        snake.add(updatedCell(x, y + 2));
        direction = 0;
        updateField();

        addDots(50);

    }

    private void addDots(int count) {

        int kx;
        int ky;
        while (count > 0) {
            kx = rand.nextInt(width);
            ky = rand.nextInt(height);
            if (field[kx][ky] == 0) {
                field[kx][ky] = 2;
                pixels[kx + ky*width] = palette[field[kx][ky]];
                count--;
            }
        }
    }

    public void rightMove() {
        direction = (direction + 1)%4;
    }

    public void leftMove() {
        direction = (direction - 1);
        if (direction < 0) {
            direction = 4 + direction;
        }
    }

    public void reset() {
        initField();
    }

    private boolean checkForFreeze(Point aim) {

        aim.x += dx[direction];
        aim.y += dy[direction];
        aim = updatedCell(aim.x, aim.y);

        for (int i = 0; i < snake.size(); i++) {
            if (snake.get(i).equals(aim.x, aim.y)) {
                return true;
            }
        }

        return false;
    }

    private Point updatedCell(int x, int y) {

        if (x < 0) { x = width - 1; }
        if (y < 0) { y = height - 1; }
        x = x % width;
        y = y % height;
        return new Point(x, y);
    }

    private void updateField() {


        for (int i = 0; i < snake.size(); i++) {
            field[snake.get(i).x][snake.get(i).y] = 0;
            pixels[snake.get(i).x + snake.get(i).y * width] = palette[0];
        }

        if (checkForFreeze(new Point(snake.get(0)))) {
            pause();
        }

        ArrayList<Point> oldSnake = new ArrayList<Point>(snake);
        snake = new ArrayList<Point>();
        snake.add(new Point(oldSnake.get(0)));
        snake.add(new Point(oldSnake.get(0)));
        Point head = updatedCell(snake.get(0).x + dx[direction], snake.get(0).y + dy[direction]);
        snake.set(0, new Point(head));
        for (int i = 1; i < oldSnake.size() - 1; i++) {
            snake.add(new Point(oldSnake.get(i)));
        }

        for (int i = snake.size() - 1; i > 0; i--) {
            if (snake.get(i).equals(snake.get(i-1))) {
                snake.set(i, new Point(snake.get(i-1)));
            }
        }

        if (field[head.x][head.y] == 2) {
            snake.add(1, new Point(head.x, head.y));
            foodCount++;
        }

        for (int i = 0; i < snake.size(); i++) {
            field[snake.get(i).x][snake.get(i).y] = 1;
            pixels[snake.get(i).x + snake.get(i).y * width] = palette[1];
        }
    }

    public void reDraw(Canvas canvas) {
        place.setPixels(pixels, 0, width, 0, 0, width, height);
        canvas.drawBitmap(place, null, destRect, null);
    }
}
