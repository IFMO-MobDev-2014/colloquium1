package ru.ifmo.md.lesson1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
* Created by thevery on 11/09/14.
*/
class WhirlView extends SurfaceView implements Runnable {
    ArrayDeque < Integer > snakeX = new ArrayDeque<Integer>();
    ArrayDeque < Integer > snakeY = new ArrayDeque<Integer>();
    final static private int[] dx = {0, -1, 0, 1};
    final static private int[] dy = {-1, 0, 1, 0};
    int direction = 0;

 final int MAX_COLOR = 10;
    final int P = (int)1e9 + 9;
    int [][] field = null;
    int [][] field2 = null;
    int W, H;
    float scaleX;
    float scaleY;
    boolean flag;
    int[] palette =  {0xFF000000, 0xFFFF0000, 0xFF00FF00};
    //, 0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080, 0xFF800080, 0xFFFFFFFF};

    Paint [] paint = new Paint[MAX_COLOR];
    int width = 40;
    int height = 60;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    Random random;
    Context _context;
    Handler h;


    public WhirlView(MyActivity context, Handler h2) {
        super(context);
        h = h2;
        _context = context;
        holder = getHolder();
     }

    //public WhirlView(MyActivity context, Handler h) {
        //super();
    //}

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
        for (int tmr = 0; running; tmr++) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                makeStep();
                Bitmap bitmap = onDraw2();

                Rect dst = new Rect(0, 0, W, H);
                canvas.drawBitmap(bitmap, null, dst, null);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (tmr % 5 == 0) {
                    int d = Math.abs(random.nextInt()) % 3;
                    if (d == 0)
                        direction = (direction + 3) % 4;
                    if (d == 1)
                        direction = (direction + 1) % 4;
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        W = w;
        H = h;
        initField();
    }

    void initField() {
        random = new Random();
        field = new int[width][height];
        int T = 50;
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                field[x][y] = 0;
        for (int i = 0; i < T; i++) {
            int x = Math.abs(random.nextInt()) % width;
            int y = Math.abs(random.nextInt()) % height;
            field[x][y] = 1;
        }

        snakeX.clear();
        snakeY.clear();
        for (int i = 0; i < 3; i++) {
            int x = width / 2;
            int y = 2 * height / 3 + i;
            snakeX.addLast(x);
            snakeY.addLast(y);
            field[x][y] = 2;
        }
    }

    public void makeStep() {
        int newX = snakeX.getFirst() + dx[direction];
        int newY = snakeY.getFirst() + dy[direction];
        newX = (newX + width) % width;
        newY = (newY + height) % height;
        snakeX.addFirst(newX);
        snakeY.addFirst(newY);
        if (field[newX][newY] == 2) {
            initField();
            //Message message = new Message();
            //message.setData("game over");
            //h.sendEmptyMessage(0);
            //Toast toast = Toast.makeText(getContext(), "game over", Toast.LENGTH_SHORT);
            return;
        }
        if (field[newX][newY] != 1) {
            int x = snakeX.getLast();
            int y = snakeY.getLast();
            field[x][y] = 0;
            snakeX.removeLast();
            snakeY.removeLast();
        }
        field[newX][newY] = 2;
    }

    public Bitmap onDraw2() {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                bitmap.setPixel(i, j, palette[field[i][j]]);
        return bitmap;
    }


}


