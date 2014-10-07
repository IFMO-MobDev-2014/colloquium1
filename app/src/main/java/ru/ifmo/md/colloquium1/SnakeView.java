package ru.ifmo.md.colloquium1;


        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.graphics.Bitmap;

        import java.util.Deque;
        import java.util.LinkedList;
        import java.util.Random;

/**
 * Created by Aganov on 07/10/14.
 */
class SnakeView extends SurfaceView implements Runnable {
    int [][] field = null;
    Paint paint = new Paint();
    int width = 40;
    int height = 60;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    final int meal_number = 50;
    int direction;
    Random rand;
    Deque<Integer> sx;
    Deque<Integer> sy;
    int score;
    int realWidth;
    int realHeight;
    int[] colors;
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
        } catch (InterruptedException ignore) {}
    }

    public void run() {
        Canvas canvas;
        long t1, t2, t3, t4;
        long lastUpdate = 0;
        long lastRotation = 0;
        long rotateTime = 5000;
        long stepTime = 1000;
        boolean gameover = false;
        while (running && !gameover) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                if (-lastUpdate + startTime > 1000000L * stepTime) {
                    canvas = holder.lockCanvas();
                    if (-lastRotation + startTime > 1000000L * rotateTime) {
                        rotate();
                        lastRotation = startTime;
                    }
                    if (updateField() == 1) {
                        gameover = true;
                    }
                    onDraw(canvas);
                    if (gameover) {
                        canvas.drawText("game over", 10, 30, paint);
                    }
                    holder.unlockCanvasAndPost(canvas);
                    lastUpdate = System.nanoTime();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = 40;
        height = 60;

        realHeight = h;
        realWidth = w;
        initField();
    }

    void initField() {
        field = new int[width][height];
        colors = new int[width * height];
        rand = new Random();
        field[20][30] = 2;
        field[20][31] = 2;
        field[20][32] = 2;
        sx = new LinkedList<Integer>();
        sy = new LinkedList<Integer>();
        sx.addFirst(20);
        sx.addFirst(20);
        sx.addFirst(20);
        sy.addFirst(30);
        sy.addFirst(31);
        sy.addFirst(32);
        score = 0;
        for (int i = 0; i < meal_number; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            while (field[x][y] != 0) {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            }
            field[x][y] = 1;
        }
        direction = 0;
    }

    int getX(int dir) {
        if (dir == 0 || dir == 2) {
            return 0;
        } else if (dir == 1) {
            return 1;
        } else {
            return -1;
        }
    }

    int getY(int dir) {
        if (dir == 0) {
            return 1;
        } else if (dir == 1 || dir == 3) {
            return 0;
        } else {
            return -1;
        }
    }

    void rotate() {
        int t = rand.nextInt(3);
        if (t == 2) {
            t = 3;
        }
        direction = (direction + t) % 4;
    }

    int updateField() {
        int x = (width + sx.getFirst() + getX(direction)) % width;
        int y = (height + sy.getFirst() + getY(direction)) % height;
        if (field[x][y] == 2) {
            return 1;
        } else if (field[x][y] == 1) {
            score++;
            field[x][y] = 2;
            sx.addFirst(x);
            sy.addFirst(y);
        } else {
            field[x][y] = 2;
            sx.addFirst(x);
            sy.addFirst(y);
            field[sx.getLast()][sy.getLast()] = 0;
            sx.removeLast();
            sy.removeLast();
        }
        return 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (field[x][y] == 1) {
                    colors[y * width + x] = 0xFFFF0000;
                } else if (field[x][y] == 2) {
                    colors[y * width + x] = 0xFF00FF00;
                } else {
                    colors[y * width + x] = 0xFFFFFFFF;
                }
            }
        }
        canvas.scale((float)realWidth / width, (float)realHeight / height);
        canvas.drawBitmap(colors, 0, width, 0, 0, width, height, false, paint);
        paint.setTextSize(3);
        canvas.drawText("score " + String.format("%d", score), 0, 3, paint);
    }
}

//fuck you gradle