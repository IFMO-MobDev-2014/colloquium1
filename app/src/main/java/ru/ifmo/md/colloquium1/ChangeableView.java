package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class ChangeableView extends SurfaceView implements Runnable {
    final int width = 40;
    final int height = 60;
    final int size = width * height;
    int[] pixels = null;

    LinkedList<IntPair> snake;
    int direction;
    Random random;
    int food;
    int score;

    Bitmap bitmap;
    Rect rect;
    volatile boolean running = false;
    Context context;
    SurfaceHolder holder;
    Thread thread;
    Handler toastHandler;
    Handler scoreHandler;
    
    public ChangeableView(Context context) {
        super(context);
        this.context = context;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        init();
    }

    public ChangeableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        init();
    }

    public ChangeableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        init();
    }

    private void init() {
        holder = getHolder();
        scoreHandler = new Handler();
        toastHandler = new Handler();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        rect = new Rect(0, 0, w, h);
    }

    private IntPair getDirection(int direction) {
        int dirX;
        int dirY;
        if (direction == 1) {
            dirX = 1;
        } else if (direction == 3) {
            dirX = -1;
        } else {
            dirX = 0;
        }
        if (direction == 2) {
            dirY = 1;
        } else if (direction == 0) {
            dirY = -1;
        } else {
            dirY = 0;
        }
        return new IntPair(dirX, dirY);
    }

    private int getCoords(IntPair coords) {
        return coords.first() + width * coords.second();
    }

    public void start() {
        pixels = new int[size];
        Arrays.fill(pixels, Color.BLACK);
        random = new Random();
        snake = new LinkedList<IntPair>();

        int x = random.nextInt(width);
        int y = random.nextInt(height);
        IntPair cur = new IntPair(x, y);
        snake.add(new IntPair(cur));
        direction = random.nextInt(4);
        IntPair direct = getDirection(direction);
        pixels[x + width * y] = Color.GREEN;
        for (int i = 1; i < 3; i++) {
            cur = IntPair.add(cur, direct, width, height);
            snake.add(new IntPair(cur));
            pixels[getCoords(cur)] = Color.GREEN;
        }

        food = 50;
        for (int i = 0; i < food; i++) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            while (pixels[x + width * y] != Color.BLACK) {
                x = random.nextInt(width);
                y = random.nextInt(height);
            }
            pixels[x + width * y] = Color.RED;
        }
        score = 0;
        updateScore(score);
    }

    private void updateField() {

        IntPair direct = getDirection(direction);
        IntPair head = IntPair.add(snake.getLast(), direct, width, height);
        if (pixels[getCoords(head)] == Color.GREEN && !head.equals(snake.getFirst())) {
            outToast("Collision happened\n" + "Score: " + score);
            running = false;
        }
        if (pixels[getCoords(head)] != Color.RED) {
            pixels[getCoords(snake.removeFirst())] = Color.BLACK;
        } else {
            updateScore(++score);
            if (--food == 0) {
                outToast("You won\n" + "Score: " + score);
                running = false;
            }
        }
        snake.add(head);
        pixels[getCoords(head)] = Color.GREEN;
    }

    private void updateScore(final int score) {
        scoreHandler.post(new Runnable() {
            @Override
            public void run() {
                ((MyActivity) context).scoreView.setText("Score: " + score);
            }
        });
    }

    private void outToast(final String message) {
        toastHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int direction() {
        return direction;
    }

    public void updateScreen(Canvas canvas) {
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, null, rect, null);
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
        } catch (InterruptedException ignored) {}
    }

    public void run() {
        start();
        while (running) {
            if (holder.getSurface().isValid()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {}
                Canvas canvas = holder.lockCanvas();
                updateField();
                updateScreen(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
