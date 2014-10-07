package ru.ifmo.md.lesson1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

class SnakeView extends SurfaceView implements Runnable {
    int [][] field = null;
    Bitmap bitmap = Bitmap.createBitmap(40, 60, Bitmap.Config.RGB_565);
    int [] newField = null;
    int width = 40;
    int height = 60;
    int food = 0xffff0000;
    int colorSnake = 0xff00ff00;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    Rect src;
    Rect dst;
    String st = "Game Over";
    SnakesChecks snakeHead = new SnakesChecks(2, 0);
    SnakesChecks snakeEnd = new SnakesChecks(0, 0);
    Random rand = new Random();
    int timer = 0;
    int [] direction = {0, 1, 2, 3};
    int numb = 0;
    int [][] napr = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};


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
                if (timer == 4) {
                    up();
                    updateField();
                } else updateField();
                Drawing(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                timer = (timer + 1) % 5;
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    void up() {
        int m = rand.nextInt(2);
        if (m == 0) numb = ((4 + (numb + 1)) % 4);
        else numb = ((4 + numb - 1) % 4);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        src = new Rect(0, 0, width, height);
        dst = new Rect(0, 0, w, h);
        initField();
    }

    void initField() {
        field = new int[width][height];
        newField = new int [width*height];
            for (int j = 0; j < 50; j++) {
                int r1 = rand.nextInt(40);
                int r2 = rand.nextInt(60);
                field[r1][r2] = food;
            }
        field[0][0] = colorSnake;
        field[1][0] = colorSnake;
        field[2][0] = colorSnake;
        snakeHead.next = new SnakesChecks(1,0);
        snakeHead.next.previous = snakeHead;
        snakeHead.next.next = snakeEnd;
        snakeEnd.previous = snakeHead.next;

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (field[x][y] == 0) field[x][y] = Color.WHITE;
                newField[x + y*width] = field[x][y];
            }
        }
       // bitmap = Bitmap.createBitmap(newField, width, height, Bitmap.Config.RGB_565);

    }

    void updateField() {
        //int[][] field2 = new int[width][height];
        SnakesChecks newHead = new SnakesChecks(((40 + snakeHead.o + napr[numb][0])%40), ((60 + snakeHead.t + napr[numb][1])%60));
        newHead.next = snakeHead;
        snakeHead.previous = newHead;
        snakeHead = newHead;
        if (field[newHead.o][newHead.t] == colorSnake) {
            initField();
        }
        if (field[newHead.o][newHead.t] != food) {
            field[snakeEnd.o][snakeEnd.t] = Color.WHITE;
            snakeEnd = snakeEnd.previous;
            snakeEnd.next = null;
        }
        field[newHead.o][newHead.t] = colorSnake;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                newField[x + y*width] = field[x][y];
            }
        }
    }


    public void Drawing(Canvas canvas) {
        bitmap.setPixels(newField, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, src, dst, null);
    }
}
