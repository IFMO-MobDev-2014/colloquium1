package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class FullscreenActivity extends SurfaceView implements SurfaceHolder.Callback {

    private SnakeThread thread;
    private SnakeCore core;


    public FullscreenActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new SnakeThread(holder);
        setFocusable(true);
        core = new SnakeCore();

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread.interrupt();
    }

    public class SnakeThread extends Thread {

        private SurfaceHolder holder;
        private Canvas canvas;

        public SnakeThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            while(!interrupted()) {
                try {
                    Thread.sleep(1000);
                    canvas = holder.lockCanvas();
                    core.step(canvas);
                    core.draw(canvas);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }


}