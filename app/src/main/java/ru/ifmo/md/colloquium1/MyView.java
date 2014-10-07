package ru.ifmo.md.colloquium1;

/**
 * Created by lightning95 on 10/7/14.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

class MyView extends SurfaceView implements Runnable {
    public static final Bitmap.Config CONFIG = Bitmap.Config.RGB_565;
    public static final int WIDTH = 40;
    private static final int HEIGHT = 60;
    private static final int FOOD_NUMBER = 50;
    private static final int START_SIZE = 3;

    private Pair<Integer, Integer>[] food;
    private boolean[] available;

    ArrayDeque<Pair<Integer, Integer>> snake;
    int direction;

    public static final int[] PALETTE = {Color.BLACK, Color.GREEN, Color.RED};

    int[][] field;

    Bitmap bitmap;
    RectF rectF;
    Canvas canvas;
    Random random;
    Paint paint;
    SurfaceHolder holder;
    Thread thread;
    volatile boolean running;

    public MyView(Context context) {
        super(context);
        holder = getHolder();

        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        random = new Random();
        initField();
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
        int cnt = 0;
        while (running) {
            if (holder.getSurface().isValid()) {
                ++cnt;
                if (cnt == 5) {
                    direction = random.nextInt(4);
                }
                cnt %= 5;
                canvas = holder.lockCanvas();

                updateField();
                drawField(canvas);
                holder.unlockCanvasAndPost(canvas);

                SystemClock.sleep(70);
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        rectF = new RectF(0, 0, w, h);
        initField();
    }

    void initField() {
        field = new int[WIDTH][HEIGHT];
        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, CONFIG);

        locateSnake();

        food = new Pair[FOOD_NUMBER];
        available = new boolean[FOOD_NUMBER];
        Arrays.fill(available, true);

        locateFood();
    }

    void updateField() {

        Pair<Integer, Integer> tail = snake.removeLast();

        bitmap.setPixel(tail.first, tail.second, PALETTE[0]);

        Pair<Integer, Integer> head = snake.getFirst();
        if (direction == 0) {
            snake.addFirst(new Pair<Integer, Integer>(head.first,
                    (head.second - 1 + HEIGHT) % HEIGHT));
        } else if (direction == 1) {
            snake.addFirst(new Pair<Integer, Integer>((head.first + 1) % WIDTH,
                    head.second));
        } else if (direction == 2) {
            snake.addFirst(new Pair<Integer, Integer>(head.first,
                    (head.second + 1) % HEIGHT));
        } else {
            snake.addFirst(new Pair<Integer, Integer>((head.first - 1 + WIDTH) % WIDTH,
                    head.second));
        }

        Pair<Integer, Integer> newHead = snake.getFirst();


        for (int i = 0; i < FOOD_NUMBER; ++i) {
            if (food[i].first == newHead.first && food[i].second == newHead.second && available[i]) {
                snake.addLast(tail);
                available[i] = false;
                break;
            }
        }

        int res = checkEnd();
        if (res == 1) {
            mes = "GAME OVER - DEAD\nSCORE: " + (snake.size() - START_SIZE);
            initField();
        } else if (res == 2) {
            mes = "GAME OVER - END OF FOOD\nSCORE: " + (snake.size() - START_SIZE);
            initField();
        }
        for (Pair<Integer, Integer> p : snake) {
            bitmap.setPixel(p.first, p.second, PALETTE[1]);
        }
        for (int i = 0; i < FOOD_NUMBER; ++i) {
            if (available[i]) {
                bitmap.setPixel(food[i].first, food[i].second, PALETTE[2]);
            }
        }
    }

    int checkEnd() {
        HashSet<Pair<Integer, Integer>> set = new HashSet<Pair<Integer, Integer>>();
        boolean dead = false;
        for (Pair<Integer, Integer> p : snake) {
            if (set.contains(p)) {
                dead = true;
                break;
            } else {
                set.add(p);
            }
        }

        if (dead) {
            return 1;
        }

        int cnt = 0;
        for (int i = 0; i < FOOD_NUMBER; ++i) {
            if (available[i]) {
                ++cnt;
            }
        }
        if (cnt == 0) {
            return 2;
        }
        return 0;
    }

    String mes = "";

    public void drawField(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rectF, null);
        if (!mes.equals(null) && !mes.equals("")) {
            canvas.drawText(mes, WIDTH / 2, HEIGHT / 2, paint);
            mes = "";
        }
    }

    void locateSnake() {
        snake = new ArrayDeque<Pair<Integer, Integer>>();
        int x = WIDTH / 2;
        int y = HEIGHT / 2;
        for (int i = 0; i < START_SIZE; ++i) {
            snake.addLast(new Pair(x, y));
            field[x][y] = 1;
            ++x;
            ++y;
            x %= WIDTH;
            y %= HEIGHT;

        }
        direction = 0;
    }

    void locateFood() {
        for (int i = 0; i < FOOD_NUMBER; ++i) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            while (field[x][y] != 0) {
                x = random.nextInt(WIDTH);
                y = random.nextInt(HEIGHT);
            }

            field[x][y] = 2;
            food[i] = new Pair(x, y);
        }
    }

//    void myClickListener(View view){
//        Button button = (Button) view;
//
//        int id = view.getId();
//        if (){
//
//        }
//    }
}