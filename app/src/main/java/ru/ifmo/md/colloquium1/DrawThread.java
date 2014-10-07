package ru.ifmo.md.colloquium1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

/**
 * Created by creed on 07.10.14.
 */
class DrawThread extends Thread{
    public boolean runFlag = false;
    public SurfaceHolder surfaceHolder;
    public Bitmap state;
    public long prevTime;
    public SnakeController snake;
    public Paint p;

    public DrawThread(SurfaceHolder surfaceHolder){
        p = new Paint();
        this.surfaceHolder = surfaceHolder;
        this.snake = new SnakeController();
        this.state = Bitmap.createBitmap(snake.W, snake.H, Bitmap.Config.RGB_565);
        // сохраняем текущее время
        prevTime = System.currentTimeMillis();
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            if (elapsedTime > 50){
                prevTime = now;
                snake.iterate();
            }
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    //canvas.drawColor(Color.BLUE);
                    //System.err.println("Snake = " + snake.field.length);
                    if (snake.valid) {
                        Point size = new Point(canvas.getWidth(), canvas.getHeight());
                        state.setPixels(snake.capture, 0, snake.W, 0, 0, snake.W, snake.H);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(state, size.x, size.y, false), 0, 0, null);
                        p.setColor(Color.YELLOW);
                        p.setTextSize(100);
                        canvas.drawText("Score: "+((Integer)(snake.getSize()-3)).toString(), 0, 100, p);
                    } else {
                        setRunning(false);
                        p.setColor(Color.YELLOW);
                        p.setTextSize(100);
                        canvas.drawColor(Color.BLACK);
                        canvas.drawText("GAME OVER", 0, 100, p);
                    }
                }
            }
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}

