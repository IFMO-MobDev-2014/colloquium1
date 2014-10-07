package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by thevery on 07.10.14.
 */
public class SnakeView extends SurfaceView implements Runnable {
    class Pair {
        int x, y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    protected final String TAG = "Snake";
    protected final int initialWidth = 40;
    protected final int initialHeight = 60;
    protected final int MAX_COLOR = 3;
    protected final int MAX_FOOD = 150;

    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    Paint paint = new Paint();

    int width = initialWidth;
    int height = initialHeight;
    float scaleW = 1, scaleH = 1;
    int score = 0;

    ArrayList<Pair> snake = new ArrayList<Pair>();
    int[][] field = new int[width][height];
    int[] colors = new int[width * height];

    int direction = 0;
    int dx[] = {0, 1, 0, -1};
    int dy[] = {1, 0, -1, 0};

    Random rand = new Random();

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
        paint.setTextSize(5);
        initField();
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
        int count = 1;
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                updateField(count);
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                try {
                    Thread.sleep(100);
                    count++;
                    if (count == 5)
                        count = 0;
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleW = (float)w / width;
        scaleH = (float)h / height;
    }

    void initField() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                field[i][j] = Color.WHITE;
        snake.add(new Pair(width / 2, height / 2));
        snake.add(new Pair(width / 2 + 1, height / 2));
        snake.add(new Pair(width / 2 + 2, height / 2));
        for (int it = 0; it < MAX_FOOD; it++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            while (field[x][y] != Color.WHITE) {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            }
            field[x][y] = Color.RED;
        }
    }

    void printSnake() {
        for (int i = 0; i < snake.size(); i++)
            Log.i(TAG, String.valueOf(snake.get(i).x) + " " + String.valueOf(snake.get(i).y));
        Log.i(TAG, "");
    }

    void updateField(int count) {
        if (snake.size() == 0)
            return;
        int newX = ((snake.get(snake.size() - 1).x + dx[direction]) % width + width) % width;
        int newY = ((snake.get(snake.size() - 1).y + dy[direction]) % height + height) % height;
        if (count == 0)
            direction = (direction + (rand.nextInt(2) == 1 ? 1 : 3)) % 4;
        snake.add(new Pair(newX, newY));
        if (field[snake.get(snake.size() - 1).x][snake.get(snake.size() - 1).y] == Color.RED) {
            score++;
            field[snake.get(snake.size() - 1).x][snake.get(snake.size() - 1).y] = Color.WHITE;
        } else {
            snake.remove(0);
        }
        Log.i(TAG, String.valueOf(snake.size()));
        printSnake();
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                colors[j * width + i] = field[i][j];
        for (int i = 0; i < snake.size(); i++)
            colors[snake.get(i).y * width + snake.get(i).x] = Color.GREEN;
        canvas.scale(scaleW, scaleH);
        canvas.drawBitmap(colors, 0, width, 0, 0, width, height, false, null);
        canvas.drawText("Score: " + String.valueOf(score), 3, 7, paint);
    }
}
