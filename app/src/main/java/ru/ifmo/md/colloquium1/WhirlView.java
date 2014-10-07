package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
* Created by thevery on 11/09/14.
*/
class WhirlView extends SurfaceView implements Runnable {
    Field field;
    Snake snake;
    int width = 0;
    int height = 0;
    int scaleX = 0;
    int scaleY = 0;

    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    Bitmap basic = null, scaled = null;

    public WhirlView(Context context) {
        super(context);
        holder = getHolder();
        snake = new Snake();
        field = new Field(snake);
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
                field.deleteSnake();
                Random r = new Random();
                int dir = r.nextInt(10) % 4;
                field.snake.changeDirection(dir);
                field.snake.move();
                field.placeSnake();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                long a = (finishTime - startTime) / 1000000;
                Log.i("TIME", "Circle: " + a);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;
        scaleX = w / field.WIDTH;
        scaleY = h / field.HEIGHT;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Bitmap base = Bitmap.createBitmap(40, 60, Bitmap.Config.RGB_565);
        base.setPixels(field.field, 0, field.WIDTH, 0, 0, field.WIDTH, field.HEIGHT);
        Bitmap scaled = Bitmap.createScaledBitmap(base, width, height, false);
        canvas.drawBitmap(scaled, 0, 0, null);
        Paint p = new Paint();
        p.setTextSize(30);
        p.setColor(Color.YELLOW);
        canvas.drawText("SCORE " + field.score, 10, 40, p);
    }
}
