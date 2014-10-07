package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by thevery on 11/09/14.
 */
class SnakeView extends SurfaceView implements Runnable {

    class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class SnakePoint extends Point {
        int dir = 0;

        SnakePoint(int x, int y, int dir) {
            super(x, y);
            this.dir = dir;
        }

        void followDir() {
            switch(dir) {
                case 0:
                    y--;
                    break;
                case 1:
                    x++;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    x--;
                    break;
            }
            if(x >= width) x = 0;
            else if(x < 0) x = width - 1;
            if(y >= height) y = 0;
            else if(y < 0) y = height - 1;
        }

        void turn(int d) {
            dir += d;
            if(dir < 0) dir += 4;
            else if(dir > 3) dir -= 4;
        }
    }

    MyActivity activity;
    static final int width = 40;
    static final int height = 60;
    int [][] field = new int[width][height];
    int [] colors = new int[width * height];
    int [] palette = {0xFFFFFFFF, 0xFFFF0000, 0xFFCCBBAA};
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Rect area;
    Random rand = new Random();
    SurfaceHolder holder;
    Thread thread = null;
    ArrayList<SnakePoint> snake;
    ArrayList<Point> freeFields = new ArrayList<Point>(width * height);
    int score = 0;
    int stepCounter = 0;
    Point foodPosition;
    volatile boolean running = false;
    volatile boolean gameOvered = false;
    volatile boolean restarting = false;
    volatile int turning = 0;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
    }

    public void setActivity(MyActivity a) {
        activity = a;
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
            if(restarting) {
                initField();
                restarting = false;
            }
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                Log.i("STEP", "started");
                updateField();
                drawIt(canvas);
                holder.unlockCanvasAndPost(canvas);
                Log.i("STEP", "finished");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        area = new Rect(0, 0, w, h);
        initField();
    }

    int reverseDir(int dir) {
        int newDir = dir + 2;
        if(newDir > 3) newDir -= 4;
        return newDir;
    }

    SnakePoint moveToDir(SnakePoint p, int dir) {
        int x = p.x;
        int y = p.y;
        switch(dir) {
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
        }
        if(x >= width) x = 0;
        else if(x < 0) x = width - 1;
        if(y >= height) y = 0;
        else if(y < 0) y = height - 1;
        return new SnakePoint(x, y, p.dir);
    }

    void addFood() {
        freeFields.clear();
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                if(field[x][y] == 0) {
                    freeFields.add(new Point(x, y));
                }
            }
        }
        int i = rand.nextInt(freeFields.size());
        foodPosition = freeFields.get(i);
    }

    void initField() {
        snake = new ArrayList<SnakePoint>(3);
        foodPosition = new Point(0, 0);
        int snakeX = rand.nextInt(width);
        int snakeY = rand.nextInt(height);
        int snakeDir = rand.nextInt(4);
        SnakePoint head = new SnakePoint(snakeX, snakeY, snakeDir);
        snake.add(head);
        int tailDir = reverseDir(snakeDir);
        SnakePoint middle = moveToDir(head, tailDir);
        snake.add(middle);
        SnakePoint end = moveToDir(middle, tailDir);
        snake.add(end);
        clearField();
        applySnake();
        addFood();
        applyFood();
    }

    void clearField() {
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                field[x][y] = 0;
            }
        }
    }

    boolean applySnake() {
        for(SnakePoint sp: snake) {
            if(field[sp.x][sp.y] == 1) return false;
            field[sp.x][sp.y] = 1;
        }
        return true;
    }

    void applyFood() {
        field[foodPosition.x][foodPosition.y] = 2;
    }

    void updateField() {
        clearField();
        for(SnakePoint sp: snake) {
            sp.followDir();
        }
        for(int i = snake.size() - 1; i >= 1; i--) {
            SnakePoint cur = snake.get(i);
            cur.dir = snake.get(i - 1).dir;
        }
        if(turning != 0) {
            SnakePoint head = snake.get(0);
            head.turn(turning);
            turning = 0;
        }
        boolean noCollisions = applySnake();
        if(noCollisions) {
            if(field[foodPosition.x][foodPosition.y] == 1) {
                score++;
                activity.updateScore(score);
                SnakePoint tail = snake.get(snake.size() - 1);
                int rdir = reverseDir(tail.dir);
                snake.add(moveToDir(tail, rdir));
                addFood();
            }
            applyFood();
        } else {
            gameOver();
        }
    }

    void gameOver() {
        gameOvered = true;
        pause();
    }

    void tryToResume() {
        if(!gameOvered) return;
        gameOvered = false;
        initField();
        resume();
    }

    public void turnLeft() {
        turning = -1;
    }

    public void turnRight() {
        turning = 1;
    }

    public void restart() {
        restarting = true;
    }

    public void drawIt(Canvas canvas) {
        int cnum = 0;
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                colors[cnum] = palette[field[x][y]];
                cnum++;
            }
        }
        bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, null, area, null);
    }
}

