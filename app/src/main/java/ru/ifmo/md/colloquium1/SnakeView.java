package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
* Created by thevery on 11/09/14.
*/
class SnakeView extends SurfaceView {

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (running) {
                if (holder.getSurface().isValid()) {
                    long ct = System.nanoTime();

                    Canvas canvas = holder.lockCanvas();

                    if(ct - lastFrame > moveDelay) {
                        updateField();
                        lastFrame = ct;
                    }
                    doDraw(canvas);

                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private enum SnakeDir {
        XPOS, YPOS, XNEG, YNEG
    }

    private volatile CyclicBarrier barrier = null;
    private int [] field;
    private ArrayList<Integer> snakeSegments = new ArrayList<Integer>(10);
    private int snakeHead;
    private SnakeDir snakeDir;
    private long lastFrame = 0;
    private long moveDelay;

    private float scaleX = 0.0f;
    private float scaleY = 0.0f;
    private final static int SIZE_LONG = 60;
    private final static int SIZE_SHORT = 40;
    private final static int STARTING_FOOD = 50;
    private final static long STARTING_DELAY = 500000000L;
    private final static int COLOR_FOOD = 0xffff0000;
    private final static int COLOR_SNAKE = 0xff00ff00;
    private final static Paint paintWhiteSmall = new Paint();
    static {
        paintWhiteSmall.setTextSize(24.0f);
        paintWhiteSmall.setStyle(Paint.Style.FILL);
        paintWhiteSmall.setColor(0xffffffff);
    }

    private SurfaceHolder holder;
    private WorkerThread worker;
    private volatile boolean running = false;
    private boolean gameOver = false;
    private int score = 0;

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
    }

    public SnakeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        holder = getHolder();
    }

    public void resume() {
        running = true;
        worker = new WorkerThread();
        worker.start();
    }

    public void pause() {
        running = false;
        try {
            worker.join();
        } catch (InterruptedException ignore) {}
        barrier = null;
    }

    public void newGame() {
        initField();
        score = 0;
        gameOver = false;
        moveDelay = STARTING_DELAY;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        boolean wasRunning = running;
        if (running)
            pause();

        scaleX = w / (float) SIZE_SHORT;
        scaleY = h / (float) SIZE_LONG;
        newGame();

        if (wasRunning)
            resume();
    }

    private void initField() {
        field = new int[SIZE_SHORT * SIZE_LONG];

        Random r = new Random();
        for(int i = 0; i < STARTING_FOOD; i++) {
            int randomX = r.nextInt(SIZE_SHORT);
            int randomY = r.nextInt(SIZE_LONG);
            field[randomX + randomY * SIZE_SHORT] = COLOR_FOOD;
        }

        snakeSegments.clear();
        snakeHead = 0;
        for(int i = 0; i < 3; i++) {
            snakeSegments.add(SIZE_SHORT / 2 + i + SIZE_LONG / 2 * SIZE_SHORT);
            field[SIZE_SHORT / 2 + i + SIZE_LONG / 2 * SIZE_SHORT] = COLOR_SNAKE;
        }

        snakeDir = SnakeDir.XNEG;
    }

    private void updateField() {
        if(gameOver)
            return;

        int headpos = snakeSegments.get(snakeHead);
        int tailIdx = snakeHead - 1;
        if(tailIdx < 0)
            tailIdx = snakeSegments.size() - 1;

        int newX = headpos % SIZE_SHORT;
        int newY = headpos / SIZE_SHORT;

        switch(snakeDir) {
            case XPOS:
                newX++;
                break;
            case XNEG:
                newX--;
                break;
            case YPOS:
                newY++;
                break;
            case YNEG:
                newY--;
                break;
        }

        if(newX < 0)
            newX += SIZE_SHORT;
        if(newX >= SIZE_SHORT)
            newX -= SIZE_SHORT;
        if(newY < 0)
            newY += SIZE_LONG;
        if(newY >= SIZE_LONG)
            newY -= SIZE_LONG;

        boolean addSegment = false;
        int newIdx = newX + newY * SIZE_SHORT;
        if(field[newIdx] == COLOR_SNAKE) {
            gameOver = true;
            post(new Runnable() {
                @Override
            public void run() {
                    Toast.makeText(getContext(), R.string.game_over, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        } else if(field[newIdx] == COLOR_FOOD) {
            score++;
            moveDelay = moveDelay * 9L / 10L;
            addSegment = true;
        }

        field[snakeSegments.get(tailIdx)] = 0;
        snakeSegments.set(tailIdx, newIdx);
        field[newIdx] = COLOR_SNAKE;
        snakeHead--;
        if(snakeHead < 0)
            snakeHead += snakeSegments.size();

        if(addSegment) {
            snakeSegments.add(snakeHead, snakeSegments.get(snakeHead));
        }
    }

    public void turnRight() {
        int newDirIdx = (snakeDir.ordinal() + 1) % 4;
        snakeDir = SnakeDir.values()[newDirIdx];
    }

    public void turnLeft() {
        int newDirIdx = snakeDir.ordinal() - 1;
        if(newDirIdx < 0)
            newDirIdx = 3;
        snakeDir = SnakeDir.values()[newDirIdx];
    }

    private void doDraw(Canvas canvas) {
        canvas.scale(scaleX, scaleY);
        canvas.drawBitmap(field, 0, SIZE_SHORT, 0.0f, 0.0f, SIZE_SHORT, SIZE_LONG, false, null);

        String scoreStr = getResources().getString(R.string.score) + score;

        canvas.scale(1.0f / scaleX, 1.0f / scaleY);
        canvas.drawText(scoreStr, 5, SIZE_LONG * scaleY, paintWhiteSmall);

    }

}
