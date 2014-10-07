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

        MyPair() {}
    }
    private ArrayList<MyPair> snake = new ArrayList<MyPair>();

    private Canvas canvas = null;
    private Random rand = new Random();
    private final static int fieldX = 40;
    private final static int fieldY = 60;
    private final static int[] dx = new int[]{0, 1, 0, -1};
    private final static int[] dy = new int[]{-1, 0, 1, 0};
    private int dir;
    public int score;
    private boolean lose = true;

    private static final int CNT_FEED = 500;
    private static final int EAT = 1;
    private static final int SNAKE = 2;

    private int[][] field = null;
    private float scaleX;
    private float scaleY;
    private Bitmap bitmap = null;
    private Handler h2;
    private Paint paint = null;  SurfaceHolder holder;
    private Thread thread = null;
    volatile boolean running = false;
    private int[] palette = {Color.WHITE, Color.RED, Color.GREEN};

    public void goRight() {
        dir = (dir + 1 + 4) % 4;
    }

    public void goLeft() {
        dir = (dir - 1 + 4) % 4;
    }

    public void restart() {
        lose = true;
    }

    public Snake(Context context, Handler h) {
        super(context);
        bitmap = Bitmap.createBitmap(fieldX, fieldY, Bitmap.Config.ARGB_4444);
        paint = new Paint();
        field = new int[fieldX][fieldY];
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
        int cntCycle = 0;
        while (running) {
            if (lose) {
                h2.sendEmptyMessage(score);
                lose = false;
                initField();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (holder.getSurface().isValid()) {

                canvas = holder.lockCanvas();
                updateField();
                setUpPixels();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);

                cntCycle = (cntCycle + 1) % 5;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = w * 1.f / fieldX;
        scaleY = h * 1.f / fieldY;
        initField();
    }

    void initField() {
        for (int i = 0; i < fieldX; i++) {
            for (int j = 0; j < fieldY; j++) {
                field[i][j] = 0;
            }
        }
        lose = false;
        score = 0;
        snake.clear();

        int temp = rand.nextInt(fieldX);
        for (int i = 5; i < 8; i++) {
            snake.add(new MyPair(temp, i));
            field[temp][i] = SNAKE;
        }

        for (int i = 0; i < CNT_FEED;) {
            int x = rand.nextInt(fieldX);
            int y = rand.nextInt(fieldY);
            if (field[x][y] == 0) {
                field[x][y] = EAT;
                i++;
            }
        }
    }

    void clearField() {
        for (int i = 0; i < snake.size(); i++) {
            field[snake.get(i).x][snake.get(i).y] = 0;
        }
    }

    void updateField() {
         clearField();

        int newX = (snake.get(0).x + dx[dir] + fieldX) % fieldX;
        int newY = (snake.get(0).y + dy[dir] + fieldY) % fieldY;

        if (field[newX][newY] == EAT) {
            score++;
            snake.add(new MyPair());
        }
        MyPair temp = snake.get(snake.size() - 1);
        temp.x = newX;
        temp.y = newY;

        for (int i = snake.size() - 1; i > 0; i--) {
           snake.set(i, snake.get(i - 1));
        }

        snake.set(0, temp);

        for (int i = 0; i < snake.size(); i++) {
            if (field[snake.get(i).x][snake.get(i).y] == SNAKE) {
                lose = true;
                break;
            }
            field[snake.get(i).x][snake.get(i).y] = SNAKE;
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

