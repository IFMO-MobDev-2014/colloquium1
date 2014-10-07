package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements Runnable {

    enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NOTHING
    }

    class SnakeCell {
        int x, y;
        Direction direction;

        SnakeCell(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }
    ArrayList<SnakeCell> snake;

    private Random RNG = new Random(31L);

    private static final int SNAKE = Color.GREEN;
    private static final int FOOD = Color.RED;
    private static final int FREE = Color.BLACK;

    private static float scaleX = 1f;
    private static float scaleY = 1f;

    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;

    private SurfaceHolder holder;
    private Thread thread = null;
    volatile boolean running = false;

    private int field1[][] = new int[WIDTH][HEIGHT];
    private int field2[][] = new int[WIDTH][HEIGHT];
    private int pixels[] = new int[WIDTH * HEIGHT];

    public MySurfaceView(Context context) {
        super(context);
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
        } catch (InterruptedException ignore) {
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = (float) w / WIDTH;
        scaleY = (float) h / HEIGHT;
        initField();
    }

    void initField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                field1[x][y] = field2[x][y] = FREE;
            }
        }
        snake = new ArrayList<SnakeCell>();
        snake.add(new SnakeCell(WIDTH / 2, HEIGHT / 2, Direction.UP));
        snake.add(new SnakeCell(WIDTH / 2, HEIGHT / 2 + 1, Direction.UP));
        snake.add(new SnakeCell(WIDTH / 2, HEIGHT / 2 + 2, Direction.UP));
        for (int i = 0; i < snake.size(); i++) {
            int x = snake.get(i).x;
            int y = snake.get(i).y;
            field1[x][y] = SNAKE;
            field2[x][y] = SNAKE;
        }
        addFood(50);
    }

    public void addFood(int numberOfPoints) {
        int took = 0;
        for (int i = 0; i < numberOfPoints; i++) {
            int x = RNG.nextInt(WIDTH);
            int y = RNG.nextInt(HEIGHT);
            if (field1[x][y] == FREE) {
                field1[x][y] = FOOD;
                if (++took == numberOfPoints)
                    return;
            }
        }
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                updateField();
                drawOnCanvas(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
//                Log.i("FPS", "current: " + (1e9 / (finishTime - startTime)));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    void updateField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (field1[x][y] != SNAKE)
                    field2[x][y] = field1[x][y];
                else
                    field2[x][y] = FREE;
            }
        }

        for (int i = 0; i < snake.size(); i++) {
            int x = snake.get(i).x;
            int y = snake.get(i).y;
            Direction direction = snake.get(i).direction;
            int nx = x;
            int ny = y;
            switch (direction) {
                case LEFT: nx = x - 1; break;
                case RIGHT: nx = x + 1; break;
                case UP: ny = y - 1; break;
                case DOWN: ny = y + 1; break;
            }
            if (nx < 0) nx += WIDTH;
            if (nx >= WIDTH) nx -= WIDTH;
            if (ny < 0) ny += HEIGHT;
            if (ny >= HEIGHT) ny -= HEIGHT;
            snake.get(i).x = nx;
            snake.get(i).y = ny;
            field2[nx][ny] = SNAKE;
        }

        for (int i = snake.size() - 1; i > 0; i--)
            snake.get(i).direction = snake.get(i - 1).direction;

        int temp[][] = field1;
        field1 = field2;
        field2 = temp;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                pixels[x + y * WIDTH] = field1[x][y];
            }
        }
    }

    public void drawOnCanvas(Canvas canvas) {
        canvas.scale(scaleX, scaleY);
        canvas.drawBitmap(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
//        canvas.drawText("FPS: " + String.format("%.1f", fps), 10, 25, paint);
    }

    public void turnLeft() {
        Direction direction = snake.get(0).direction;
        Direction newDirection = Direction.NOTHING;
        switch (direction) {
            case LEFT:
                newDirection = Direction.DOWN;
                break;
            case DOWN:
                newDirection = Direction.RIGHT;
                break;
            case RIGHT:
                newDirection = Direction.UP;
                break;
            case UP:
                newDirection = Direction.LEFT;
                break;
        }
        snake.get(0).direction = newDirection;
    }

    public void turnRight() {
        Direction direction = snake.get(0).direction;
        Direction newDirection = Direction.NOTHING;
        switch (direction) {
            case LEFT:
                newDirection = Direction.UP;
                break;
            case UP:
                newDirection = Direction.RIGHT;
                break;
            case RIGHT:
                newDirection = Direction.DOWN;
                break;
            case DOWN:
                newDirection = Direction.LEFT;
                break;
        }
        snake.get(0).direction = newDirection;
    }

    public void gameOver() {

    }

    public void restartEverything() {
        initField();
    }

}
