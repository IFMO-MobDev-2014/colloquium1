package ru.ifmo.md.lesson1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/** * Created by thevery on 11/09/14.
*/
class Snake extends SurfaceView implements Runnable {
    class MyPair {
        int x, y;
        MyPair(int x2, int y2) {
            x = x2;
            y = y2;
        }

        MyPair(MyPair temp) {
            x = temp.x;
            y = temp.y;
        }
    }
    ArrayList<MyPair> snake = new ArrayList<MyPair>();

    Canvas canvas = null;
    Random rand = new Random();
    final static int fieldX = 40;
    final static int fieldY = 60;
    int score = 0;
    private int k;
    private final static int[] dx = new int[]{0, 0, -1, 1};
    private final static int[] dy = new int[]{1, -1, 0, 0};
    private int dir;
    boolean lose = false;

    private static final int CNT_FEED = 300;
    private static final int EAT = 1;
    private static final int SNAKE = 2;
    Handler h2;

    int[][] field = null;
    float scaleX;
    float scaleY;
    Bitmap bitmap = null;
    Paint paint = null;  SurfaceHolder holder; Thread thread = null;
    volatile boolean running = false;
    int[] palette = {Color.WHITE, Color.RED, Color.GREEN};

    public Snake(Context context, Handler h) {
        super(context);
        bitmap = Bitmap.createBitmap(fieldX, fieldY, Bitmap.Config.ARGB_4444);
        h2 = h;
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
        initField();
        while (running && !lose) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                if (k == 0) {
                    int temp = rand.nextInt(4);
                    if ((temp < 2 && dir < 2) || (temp > 1 && dir > 1)) {
                        temp = (temp + 2) % 4;
                    }
                    dir = temp;
                }

                canvas = holder.lockCanvas();

                updateField();

                setUpPixels();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);

                long finishTime = System.nanoTime();
                Log.i("TIME", "FPS: " + 1e9 / (finishTime - startTime));
                k = (k + 1) % 5;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignore) {}
            }
        }

        if (lose) {
            h2.sendEmptyMessage(10);
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = w * 1.f / fieldX;
        scaleY = h * 1.f / fieldY;
        initField();
    }

    void initField() {
        lose = false;
        field = new int[fieldX][fieldY];
        int temp = rand.nextInt(fieldX);
        snake.clear();
        for (int i = 5; i < 8; i++) {
            snake.add(new MyPair(temp, i));
            field[temp][i] = SNAKE;
        }

        for (int i = 0; i < CNT_FEED;) {
            int x = rand.nextInt(fieldX);
            int y = rand.nextInt(fieldY);
            if (field[x][y] == 0 && !(x == temp && y >4 && y < 8)) {
                field[x][y] = EAT;
                i++;
            }
        }
        paint = new Paint();
    }

    void updateField() {

       for (int i = 0; i < snake.size(); i++) {
           field[snake.get(i).x][snake.get(i).y] = 0;
       }

       int newX = (snake.get(0).x + dx[dir] + fieldX) % fieldX;
       int newY = (snake.get(0).y + dy[dir] + fieldY) % fieldY;
       MyPair temp = snake.get(snake.size() - 1);
       temp.x = newX;
       temp.y = newY;

       int good = 0;
       if (field[newX][newY] == EAT) {
            score++;
            snake.add(new MyPair(snake.get(snake.size() - 1)));
            good = 1;
        }

        for (int i = snake.size() - good - 1; i >= 1; i--) {
            snake.set(i, snake.get(i - 1));
        }

        snake.set(0, temp);
        for (int i = 0; i < snake.size(); i++) {
            field[snake.get(i).x][snake.get(i).y] = SNAKE;
        }

        if (good == 0 && snake.get(0).x == snake.get(snake.size() - 1).x && snake.get(0).y == snake.get(snake.size() - 1).y) {
            Log.i("LOSE", snake.get(0).x + "");
            lose = true;
        }
    }

    public void setUpPixels() {
        for (int x = 0; x < fieldX; x++) {
            for (int y = 0; y < fieldY; y++) {
                bitmap.setPixel(x, y, palette[field[x][y]]);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.scale(scaleX, scaleY);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
}

