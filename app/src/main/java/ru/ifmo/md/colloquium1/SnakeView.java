package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.jar.Attributes;

/**
 * Created by nagibator2005 on 2014-10-07.
 */
public class SnakeView extends SurfaceView implements Runnable {
    Snake snake;
    Bitmap bitmap;
    Rect imgRect;
    int[] bitmapData;
    int width = 40, height = 60;
    SurfaceHolder holder;
    Paint scorePaint;
    Thread thread = null;
    volatile boolean running = false;

    public SnakeView(Context context) {
        super(context);
        initField();
    }

    public SnakeView(Context context, AttributeSet attributes) {
        super(context, attributes);
        initField();
    }

    private void initField() {
        holder = getHolder();
        snake = new Snake(width, height);
        imgRect = new Rect(0, 0, 0, 0);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapData = new int[width * height];
        scorePaint = new Paint();
        scorePaint.setColor(0x80FFFFFF);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextSize(30f);

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

    public void changeDirection(int direction) {
        snake.changeDirection(direction);
    }

    public int getDirection() {
        return snake.getDirection();
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                if (snake.isLooser()) {
                    snake = new Snake(width, height);
                }
                snake.step();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {}//*/
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        imgRect.bottom = h;
        imgRect.right = w;
    }

    @Override
    public void draw(Canvas canvas) {
        snake.toBitmapData(bitmapData);
        bitmap.setPixels(bitmapData, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, null, imgRect, null);
        String scoreStr = "Score: " + snake.getScore();
        canvas.drawText(scoreStr, 00f, 30f, scorePaint);
    }
}
