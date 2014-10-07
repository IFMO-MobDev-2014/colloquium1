package ru.ifmo.md.colloquium1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
class SnakeView extends SurfaceView {
    private final SurfaceHolder holder;
    private final Snake snake;
    private final Paint paint;

    private float scaleX;
    private float scaleY;

    private Thread drawThread = null;
    private volatile boolean running = false;

    public SnakeView(Context context) {
        super(context);

        holder = getHolder();
        snake = new Snake();
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public void resume() {
        snake.resume();
        running = true;
        drawThread = new Thread(new Runnable() {
            @SuppressLint("WrongCall")
            @Override
            public void run() {
                while (running) {
                    if (holder.getSurface().isValid()) {
                        Canvas canvas = holder.lockCanvas();
                        onDraw(canvas);
                        holder.unlockCanvasAndPost(canvas);

                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException ignore) {

                        }
                    }
                }
            }
        });
        drawThread.start();
    }

    public void pause() {
        snake.pause();
        running = false;
        try {
            drawThread.join();
        } catch (InterruptedException ignore) {
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = w / Snake.WIDTH;
        scaleY = h / Snake.HEIGHT;
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Clear screen
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);



        paint.setColor(Color.GREEN);
        for (Snake.Cell cell : snake.getSnake()) {
            canvas.drawRect(cell.getX() * scaleX, cell.getY() * scaleY,
                    (cell.getX() + 1) * scaleX, (cell.getY() + 1) * scaleY, paint);
        }
    }
}
