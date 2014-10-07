package ru.ifmo.md.colloquium1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
class SnakeView extends SurfaceView {
    private final SurfaceHolder holder;
    private final Snake snake;
    private final Food food;
    private final Paint paint;

    private float scaleX;
    private float scaleY;

    private Thread drawThread = null;
    private volatile boolean running = false;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        food = new Food();
        snake = new Snake(food);
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

    public void rotate(Snake.Direction direction) {
        snake.rotate(direction);
    }

    public void setOnScoreChangedListener(Snake.ScoreChangedListener listener) {
        snake.setListener(listener);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = w / Snake.WIDTH;
        scaleY = h / Snake.HEIGHT;
    }

    @Override
    public void onDraw(Canvas canvas) {
        clearScreen(canvas);

        drawSnake(canvas);
        drawFood(canvas);
    }

    private void clearScreen(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private void drawFood(Canvas canvas) {
        paint.setColor(Color.RED);
        for (Food.Fruit fruit : food.getFruits()) {
            canvas.drawRect(fruit.getX() * scaleX, fruit.getY() * scaleY,
                    (fruit.getX() + 1) * scaleX, (fruit.getY() + 1) * scaleY, paint);
        }
    }

    private void drawSnake(Canvas canvas) {
        paint.setColor(Color.GREEN);
        for (Snake.Cell cell : snake.getSnake()) {
            canvas.drawRect(cell.getX() * scaleX, cell.getY() * scaleY,
                    (cell.getX() + 1) * scaleX, (cell.getY() + 1) * scaleY, paint);
        }
    }
}
