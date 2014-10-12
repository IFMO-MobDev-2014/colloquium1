package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by pokrasko on 07.10.14.
 */
class ChangeableView extends SurfaceView implements Runnable {
    final int width = 40;
    final int height = 60;
    final int size = width * height;
    int[] pixels = null;

    LinkedList<IntPair> snake;
    int direction;
    int food;
    Random random;
    Bitmap bitmap;
    Rect rect;
    int w;
    int h;
    volatile boolean running = false;
    Context context;
    SurfaceHolder holder;
    Thread thread;
    
    public ChangeableView(Context context) {
        super(context);
        this.context = context;
        holder = getHolder();
        init();
    }

    public ChangeableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        holder = getHolder();
        init();
    }

    public ChangeableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        holder = getHolder();
        init();
    }

    private void init() {
        pixels = new int[size];
        Arrays.fill(pixels, Color.BLUE);
        //for (int i = 0; i < 2350; i++) {
            //pixels[i] = Color.BLUE;
        //} //can't set more than 2350 pixels out of 2400
        random = new Random();
        snake = new LinkedList<IntPair>();
        rect = new Rect(0, 0, w, h);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
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
            pixels[x + width * y] = Color.GREEN;
        }

        food = 50;
        for (int i = 0; i < food; i++) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            while (pixels[x + width * y] != 0) {
                x = random.nextInt(width);
                y = random.nextInt(height);
            }
            pixels[x + width * y] = Color.RED;
        }
    }

    private void update() {

        IntPair direct = getDirection(direction);
        Log.i("vars", "former head = " + snake.getLast().first() + "; " + snake.getLast().second());
        IntPair head = IntPair.add(snake.getLast(), direct, width, height);
        Log.i("vars", "head = " + head.first() + "; " + head.second());
        Log.i("vars", "tail = " + snake.getFirst().first() + "; " + snake.getFirst().second());
        if (pixels[getCoords(head)] == Color.GREEN && !head.equals(snake.getFirst())) {
            ((MyActivity) context).outToast("Collision happened");
            running = false;
        }
        if (pixels[getCoords(head)] != Color.RED) {
            pixels[getCoords(snake.removeFirst())] = Color.BLUE;
        } else if (--food == 0) {
            ((MyActivity) context).outToast("You won");
            running = false;
        }
        snake.add(head);
        pixels[getCoords(head)] = Color.GREEN;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int direction() {
        return direction;
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, null, rect, null);
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        start();
        thread.start();
    }

    public void pause() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ignored) {}
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                Canvas canvas = holder.lockCanvas();
                update();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

}
