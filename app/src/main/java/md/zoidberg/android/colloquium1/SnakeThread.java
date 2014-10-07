package md.zoidberg.android.colloquium1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class
        SnakeThread extends Thread {
    public enum GameState {
        NOT_YET,
        RUNNING,
        DONE
    }

    public enum Direction {
        NO_DIRECTION,
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private int field[][];

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    private static final int fieldWidth = 40;
    private static final int fieldHeight = 60;

    private volatile GameState state = GameState.NOT_YET;
    private volatile Direction direction = Direction.NO_DIRECTION;
    private ArrayList<Point> snakePoints;


    private Paint textPaint = new Paint();
    {
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(20);
        textPaint.setFakeBoldText(true);
    }

    private Paint snakePaint = new Paint();
    {
        textPaint.setColor(Color.GREEN);
        textPaint.setStyle(Paint.Style.FILL);
    }

    private Paint foodPaint = new Paint();
    {
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
    }

    private Paint emptyPaint = new Paint();
    {
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
    }

    private int score;
    private DecimalFormat scoreFormat = new DecimalFormat("Score ###");

    private static final int SNAKE = 1;
    private static final int FOOD = -1;
    private static final int EMPTY = 0;

    private int headX;
    private int headY;

    private final SurfaceHolder holder;

    private boolean isRunning;

    public SnakeThread(SurfaceHolder holder, int w, int h) {
        this.holder = holder;
        isRunning = false;
        initField();
    }

    Random rand;

    public void setRunning(boolean newRunning) {
        isRunning = newRunning;
    }

    private void initField() {
        field = new int[fieldHeight][fieldWidth];
        rand = new Random();
        score = 0;

        snakePoints = new ArrayList<Point>();
        snakePoints.add(new Point(0, 0));
        snakePoints.add(new Point(1, 0));
        snakePoints.add(new Point(2, 0));

        field[0][0] = SNAKE;
        field[0][1] = SNAKE;
        field[0][2] = SNAKE;

        headX = 2;
        headY = 0;
        generateFood();

        Log.d("game", "initField");
    }

    private void generateFood() {
        if (score == 50) return;
        int gX = rand.nextInt(fieldWidth);
        int gY = rand.nextInt(fieldHeight);
        while (field[gY][gX] != EMPTY) {
            gX = rand.nextInt(fieldWidth);
            gY = rand.nextInt(fieldHeight);
        }

        field[gY][gX] = FOOD;
    }

    public void recalculateField() {
        int cX = headX;
        int cY = headY;

        if (direction == Direction.NO_DIRECTION) return;
        if (direction == Direction.UP) { cX = headX; cY = headY - 1; }
        if (direction == Direction.DOWN) { cX = headX; cY = headY + 1; }
        if (direction == Direction.LEFT) { cX = headX - 1; cY = headY; }
        if (direction == Direction.RIGHT) { cX = headX + 1; cY = headY; }

        if (cX < 0 || cY < 0 || cX >= fieldWidth || cY >= fieldHeight || field[cY][cX] == SNAKE) {
            state = GameState.DONE;
        } else if (field[cY][cX] == FOOD) {
            snakePoints.add(new Point(cX, cY));
            field[cY][cX] = SNAKE;

            headX = cX;
            headY = cY;

            score += 1;
            generateFood();
        } else {
            Point tail = snakePoints.get(0);
            field[tail.y][tail.x] = EMPTY;
            field[cY][cX] = SNAKE;

            snakePoints.add(new Point(cX, cY));
            snakePoints.remove(0);

            headX = cX;
            headY = cY;
            Log.d("snake", Integer.toString(cX) + " " + Integer.toString(cY));
        }

    }

    private void redrawScreen(Canvas screen) {
        if (screen == null) return;
        if (state != GameState.DONE) {
            float segWidth = 30; //screen.getWidth() / fieldWidth;
            float segHeight = 30; // /screen.getHeight() / fieldHeight;
            for (int y = 0; y < fieldHeight; y++) {
                for (int x = 0; x < fieldWidth; x++) {
                    Paint paint = emptyPaint;
                    boolean draw = false;
                    if (field[y][x] == SNAKE) {
                        paint = snakePaint;
                        draw = true;
                    } else if (field[y][x] == FOOD) {
                        paint = foodPaint;
                        draw = true;
                    }

                    if (draw) screen.drawRect(y * segWidth, x * segHeight, segWidth, segHeight, paint);
                }
            }
            Log.d("game", scoreFormat.format(score));
            screen.drawText(scoreFormat.format(score), 20.0f, 40.0f, textPaint);
        } else {
            screen.drawARGB(0, 0, 0, 0);
            screen.drawText(scoreFormat.format(score), 20.0f, 40.0f, textPaint);
        }

        screen.drawCircle(40.0f, 40.0f, 20.0f, textPaint);
    }

    public void run() {
        Canvas canvas = null;
        while (isRunning) {
            while (state != GameState.DONE) {
                recalculateField();
                try {
                    canvas = holder.lockCanvas();
                    synchronized (holder) {
                        redrawScreen(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

                SystemClock.sleep(100);
            }

            initField();
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    redrawScreen(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
