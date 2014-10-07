package ru.ifmo.md.lesson1;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Objects;

/**
 * Created by Home on 14.09.2014.
 */
public class MainThreadWrapper {

    volatile boolean running = false;

    private final WhirlView whirlView;
    private Thread drawThread = null;

    class DrawWrapper implements Runnable {

        public void run() {
            Canvas canvas;
            while (running) {
                if (whirlView.holder.getSurface().isValid()) {

                    canvas = whirlView.holder.lockCanvas();
                    whirlView.updateField();
                    whirlView.render(canvas);
                    whirlView.holder.unlockCanvasAndPost(canvas);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public MainThreadWrapper(WhirlView view) {
        whirlView = view;
    }

    public void resume() {
        running = true;
        drawThread = new Thread(new DrawWrapper());
        drawThread.start();
    }

    public void pause() {
        running = false;
        try {
            drawThread.join();
        } catch (InterruptedException ignore) {}
    }

}
