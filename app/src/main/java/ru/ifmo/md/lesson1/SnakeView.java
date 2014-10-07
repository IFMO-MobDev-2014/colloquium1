package ru.ifmo.md.lesson1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayDeque;
import java.util.Random;

class SnakeView extends SurfaceView implements Runnable {
    int[][] field = null;
    int state = 0;
    int direction = 0;
    int deviceWidth = 0;
    int deviceHeight = 0;
    int width = 40;
    int height = 60;
    Rect deviceRect;
    Bitmap b;
    int[] colors;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    int swaps = 0;
    int score = 0;

    ArrayDeque<Cell> snake = new ArrayDeque<Cell>();

    final int FIELD = 0xFFFFFFFF;
    final int FEED = 0xFFFF0000;
    final int SNAKE = 0xFF00FF00;

    final int TO_FORWARD = 0;
    final int TO_LEFT = 1;
    final int TO_RIGHT = -1;

    final int UP = 0;
    final int RIGHT = 1;
    final int DOWN = 2;
    final int LEFT = 3;

    public SnakeView(Context context) {
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

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                direction = TO_FORWARD;
                if (swaps == 0)
                    setDir();
                updateField();
                Canvas canvas = holder.lockCanvas();
                drawOnCan(canvas);
                holder.unlockCanvasAndPost(canvas);
                swaps = (swaps + 1) % 5;
                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        deviceWidth = w;
        deviceHeight = h;
        if (w < h && width > height || w > h && width < height) {
            int t = width;
            width = height;
            height = t;
        }
        initField();
    }

    class Cell {
        int x;
        int y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    Cell nextCell(Cell p) {
        int to = (4 + direction + state) % 4;
        state = to;
        int x = p.x;
        int y = p.y;
        switch (to) {
            case UP: return new Cell(x, (height + y - 1) % height);
            case RIGHT: return new Cell((x + 1) % width, y);
            case DOWN: return new Cell(x, (y + 1) % height);
            case LEFT: return new Cell((width + x - 1) % width, y);
            default: return new Cell(x, y);
        }
    }

    void setDir() {
        Random r = new Random();
        direction = r.nextInt(3) - 1;
    }

    void initField() {
        field = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field[x][y] = FIELD;
            }
        }

        Random rand = new Random();

        setDir();

        snake = new ArrayDeque<Cell>();

        snake.add(new Cell(rand.nextInt(width), rand.nextInt(height)));
        snake.addFirst(nextCell(snake.getFirst()));
        snake.addFirst(nextCell(snake.getFirst()));
        for (Cell c : snake) {
            field[c.x][c.y] = SNAKE;
        }

        int numOfFeed = 0;
        while (numOfFeed < 50) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (field[x][y] == FIELD) {
                field[x][y] = FEED;
                numOfFeed++;
            }
        }

        deviceRect = new Rect(0, 0, deviceWidth - 1, deviceHeight - 1);
        b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        colors = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colors[x + y * width] = field[x][y];
            }
        }
    }

    void set(int x, int y, int c) {
        field[x][y] = c;
        colors[x + y * width] = c;
    }

    void youLost() {
        initField();
    }

    void updateField() {
        Cell head = snake.getFirst();
        Cell next = nextCell(head);
        Cell tail = snake.getLast();

        switch (field[next.x][next.y]) {
            case FIELD: {
                snake.addFirst(next);
                snake.pollLast();
                set(next.x, next.y, SNAKE);
                set(tail.x, tail.y, FIELD);
                return;
            }
            case FEED: {
                snake.addFirst(next);
                set(next.x, next.y, SNAKE);
                score++;
                return;
            }
            case SNAKE: {
                youLost();
                return;
            }
        }
    }

    public void drawOnCan(Canvas canvas) {
        b.setPixels(colors, 0, width, 0, 0, width, height);
        canvas.drawBitmap(b, null, deviceRect, null);
    }
}
