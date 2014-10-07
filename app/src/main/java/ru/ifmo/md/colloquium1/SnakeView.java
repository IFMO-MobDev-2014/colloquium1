package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeView extends SurfaceView implements Runnable {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;
    private static final int EMPTY_CELL = 0;
    private static final int SNAKE_CELL = 1;
    private static final int FOOD_CELL = 2;
    private static final int INITIAL_FOOD = 50;
    private static final int LENGTH_INCREMENT = 1;
    private static final int SCORE_INCREMENT = 10;
    private static final int[][] MOVEMENT = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private static final Paint[] PAINTS = {new Paint(), new Paint(), new Paint()};
    static {
        PAINTS[0].setColor(Color.WHITE);
        PAINTS[1].setColor(Color.GREEN);
        PAINTS[2].setColor(Color.RED);
    }
    private static final float TEXT_SIZE = 2f;
    private static final Paint TEXT_PAINT = new Paint();
    static {
        TEXT_PAINT.setTextSize(TEXT_SIZE);
        TEXT_PAINT.setColor(Color.BLACK);
    }

    private final SurfaceHolder holder;
    private volatile boolean running = false;
    private Matrix scaleMatrix = new Matrix();
    private Thread thread;
    private int[][] field;
    private List<Integer> snakeX;
    private List<Integer> snakeY;
    private int snakeLength;
    private int snakeHead;
    private int score;
    private int turnCounter = 0;
    private final Handler handler = new Handler();

    public SnakeView(Context context) {
        super(context);
        this.holder = getHolder();
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.holder = getHolder();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.holder = getHolder();
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
        } catch (InterruptedException ignored) {}
    }

    public void resetGame() {
        initField();
    }

    public void turnLeft() {
        snakeHead = (snakeHead + 3) % 4;
    }

    public void turnRight() {
        snakeHead = (snakeHead + 1) % 4;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (running) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                turnCounter = (turnCounter + 1) % 50;
                if (turnCounter == 0) {
                    switch (random.nextInt(3)) {
                        case 0:
                            turnLeft();
                            break;
                        case 1:
                            turnRight();
                            break;
                    }
                }
                updateField();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        scaleMatrix.setScale((float) w / WIDTH, (float) h / HEIGHT);
        initField();
    }

    private void initField() {
        field = new int[HEIGHT][WIDTH];
        snakeX = new ArrayList<Integer>();
        snakeY = new ArrayList<Integer>();
        snakeX.add(WIDTH / 2);
        snakeX.add(WIDTH / 2);
        snakeX.add(WIDTH / 2);
        snakeY.add(HEIGHT / 2);
        snakeY.add(HEIGHT / 2 + 1);
        snakeY.add(HEIGHT / 2 + 1);
        snakeLength = 3;
        snakeHead = 0;
        score = 0;

        for (int i = 0; i < snakeLength; ++i) {
            field[snakeY.get(i)][snakeX.get(i)] = SNAKE_CELL;
        }

        Random random = new Random();
        for (int f = 0; f < INITIAL_FOOD; ++f) {
            int y, x;
            do {
                y = random.nextInt(HEIGHT);
                x = random.nextInt(WIDTH);
            } while (field[y][x] != EMPTY_CELL);
            field[y][x] = FOOD_CELL;
        }
    }

    private void updateField() {
        int headY = (snakeY.get(0) + MOVEMENT[snakeHead][0] + HEIGHT) % HEIGHT;
        int headX = (snakeX.get(0) + MOVEMENT[snakeHead][1] + WIDTH) % WIDTH;
        int headCell = field[headY][headX];
        if (headCell == SNAKE_CELL) {
            final int endGameScore = score;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), getContext().getString(R.string.game_end, endGameScore), Toast.LENGTH_LONG).show();
                }
            });
            resetGame();
            return;
        }
        if (headCell == FOOD_CELL) {
            snakeLength += LENGTH_INCREMENT;
            score += SCORE_INCREMENT;
            for (int i = 0; i < LENGTH_INCREMENT; ++i) {
                snakeY.add(snakeY.get(snakeY.size() - 1));
                snakeX.add(snakeX.get(snakeX.size() - 1));
            }
        }
        field[snakeY.get(snakeLength - 1)][snakeX.get(snakeLength - 1)] = EMPTY_CELL;
        for (int i = snakeLength - 1; i > 0; i--) {
            Integer y = snakeY.get(i - 1);
            Integer x = snakeX.get(i - 1);
            snakeY.set(i, y);
            snakeX.set(i, x);
            field[y][x] = SNAKE_CELL;
        }
        snakeY.set(0, headY);
        snakeX.set(0, headX);
        field[headY][headX] = SNAKE_CELL;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setMatrix(scaleMatrix);
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                canvas.drawRect(x, y, x + 1, y + 1, PAINTS[field[y][x]]);
            }
        }
        canvas.drawText(getContext().getString(R.string.score, score), 0, TEXT_SIZE, TEXT_PAINT);
    }
}
