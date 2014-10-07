package ru.ifmo.md.colloquium1.myapplication;

/**
 * Created by Nikita Yaschenko on 07.10.14.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


class SnakeView extends SurfaceView implements Runnable {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;

    private SnakeGame snake;
    private boolean lose;

    private float scaleWidth = 1f;
    private float scaleHeight = 1f;
    private int width = WIDTH;
    private int height = HEIGHT;

    private int[] pixels = new int[WIDTH * HEIGHT];

    private Paint paint = null;
    private long lastFpsUpdate;
    private long lastGameUpdate;
    private long lastTurnUpdate;
    private final static long FPS_UPDATE_INTERVAL = 1000 * 1000 * 1000;
    private final static long GAME_UPDATE_INTERVAL = 200 * 1000 * 1000;
    private final static long TURN_UPDATE_INTERVAL = 5L * 1000 * 1000 * 1000;
    private float fps;
    private int framesDrawn = 0;

    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;

    public SnakeView(Context context) {
        super(context);
        snake = new SnakeGame(WIDTH, HEIGHT);
        lose = false;
        paint = new Paint(Color.YELLOW);
        paint.setTextSize(2);
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
            recalcTurn();
            recalcFps();
            recalcGame();
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        Log.i("TAG", "onSizeChanged: " + w + " " + h + " " + oldW + " " + oldH);
        scaleWidth = (float)w / WIDTH;
        scaleHeight = (float)h / HEIGHT;
        //scaleHeight *= 0.9; // 10% for score
    }

    private void recalcTurn() {
        long now = System.nanoTime();
        long elapsed = now - lastTurnUpdate;
        if (elapsed > TURN_UPDATE_INTERVAL) {
            lastTurnUpdate = now;
            snake.setRandomDirection();
        }
    }

    private void recalcGame() {
        if (lose) return;
        long now = System.nanoTime();
        long elapsed = now - lastGameUpdate;
        if (elapsed > GAME_UPDATE_INTERVAL) {
            lose = !snake.tick();
            lastGameUpdate = now;
        }
        for (int i = 0; i < WIDTH ; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                SnakeGame.Cell c = snake.field[i][j];
                switch (c) {
                    case SNAKE:
                        pixels[j * WIDTH + i] = Color.GREEN;
                        break;
                    case FOOD:
                        pixels[j * WIDTH + i] = Color.RED;
                        break;
                    case EMPTY:
                        pixels[j * WIDTH + i] = Color.WHITE;
                        break;
                }
            }
        }
    }


    void recalcFps() {
        framesDrawn++;
        long now = System.nanoTime();
        long elapsed = now - lastFpsUpdate;
        if (elapsed > FPS_UPDATE_INTERVAL) {
            fps = (float)framesDrawn * FPS_UPDATE_INTERVAL / elapsed;
            framesDrawn = 0;
            lastFpsUpdate = now;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(scaleWidth, scaleHeight);
        canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, false, null);
        canvas.drawText("Score: " + snake.getScore(), 2, 0, paint);
        canvas.restore();
    }
}