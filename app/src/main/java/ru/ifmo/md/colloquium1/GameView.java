package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.logging.Logger;

/**
 * Created by antonk on 07.10.14.
 */
public class GameView extends SurfaceView implements Runnable {
    volatile boolean running = false;

    Paint paint = new Paint();
    Thread thread = null;
    SurfaceHolder holder;
    GameEngine engine = null;

    public GameView(Context context) {
        super(context);
        holder = getHolder();

    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        holder = getHolder();
    }

    public GameView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
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
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                if (!engine.makeStep())
                    engine.reset();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }


    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
        for (int i = 0; i < engine.snake.size(); i++) {
            GameEngine.Coordinate element = engine.snake.get(i);
            paint.setColor(Color.GREEN);
            Rect r = new Rect(element.y * this.getWidth() / GameEngine.WIDTH,
                    element.x * this.getHeight() / GameEngine.HEIGHT,
                    (element.y + 1) * this.getWidth() / GameEngine.WIDTH,
                    (element.x + 1) * this.getHeight() / GameEngine.HEIGHT);
            canvas.drawRect(r, paint);
        }
        GameEngine.Coordinate element = engine.food;
        paint.setColor(Color.RED);
        Rect r = new Rect(element.y * this.getWidth() / GameEngine.WIDTH,
                element.x * this.getHeight() / GameEngine.HEIGHT,
                (element.y + 1) * this.getWidth() / GameEngine.WIDTH,
                (element.x + 1) * this.getHeight() / GameEngine.HEIGHT);
        canvas.drawRect(r, paint);
    }
}
