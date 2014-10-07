package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by Женя on 07.10.2014.
 */
public class SnakeView extends SurfaceView implements Runnable {
    private volatile boolean running = false;
    private SurfaceHolder holder;
    private Thread thread;
    private State state;

    public int getScore() {
        return state.getScore();
    }

    public SnakeView(Context context) {
        super(context);
        holder = this.getHolder();
        initField();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        holder = getHolder();
        initField();
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        initField();
    }

    public void resume() {
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void pause() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initField() {
        state = new State();
    }

    @Override
    public void run() {
        double start;
        while (running) {
            start = System.nanoTime();
            synchronized (holder) {
                synchronized (state) {
                    if (holder.getSurface().isValid()) {
                        Canvas canvas = holder.lockCanvas();
                        draw(canvas);
                        holder.unlockCanvasAndPost(canvas);
                        running = state.tick();
                        ((TextView)findViewById(R.id.textView)).setText("SCORE: " + getScore());
                    }
                }
            }
            try {
                Thread.sleep(Math.max(0, 100 - (int) (System.nanoTime() - start) / 1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            draw(canvas);
            Paint pt = new Paint();
            pt.setColor(Color.BLUE);
            pt.setTextSize(5);
            canvas.drawText("GAME OVER!!!", 0, 10, pt);
            canvas.drawText("score: " + state.getScore(), 5, 20, pt);
            holder.unlockCanvasAndPost(canvas);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        float scaleX = ((float) getWidth()) / state.WIDTH;
        float scaleY = ((float) getHeight()) / state.HEIGHT;
        canvas.scale(scaleX, scaleY);
        int[] colors = state.getColors();
        canvas.drawBitmap(colors, 0, state.WIDTH, 0, 0, state.WIDTH, state.HEIGHT, false, null);
    }
    public void leftBtn() {
        state.moveLeft();
    }
    public void rightBtn() {
        state.moveRight();
    }
    public void replay() {
        initField();
        thread = new Thread(this);
        running = true;
        thread.start();
    }
}
