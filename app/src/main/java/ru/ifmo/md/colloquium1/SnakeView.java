package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

//psvm + tab = publac static void main(String[] args) {}

class SnakeView extends SurfaceView implements Runnable {
    int [][] field = null;
    int [] colors = null;
    public static final int width = 60;
    public static final int height = 40;
    float scalew = 0;
    float scaleh = 0;
    int numberOfIterations = 0;
    int white = 0xFFFFFFFF;
    int green = 0xFF00FF00;
    int red = 0xFFFF0000;
    int headX = 0;
    int headY = 0;
    int snakeLength = 3;
    int[][] snakeIn = new int[2500][2];
    int direction = 0;
    public int score = 0;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    boolean gameIsCorrect = true;
    Random rand;
    Canvas canvas;
    Paint paint = new Paint();

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
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                canvas = holder.lockCanvas();
                updateField();
                draw(canvas);
                if (!gameIsCorrect) {
                    canvas.drawText("Game over, Your score is " + Integer.toString(score), 10, 10, paint);
                    gameIsCorrect = true;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ignore) {}
                    initField();
                }
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(1000);
                    numberOfIterations++;
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scalew = (float) w/width;
        scaleh = (float) h/height;
        initField();
    }

    void initField() {
        field = new int[height][width];
        colors = new int[width * height];
        paint.setColor(0xFF000000);
        paint.setTextSize(3);
        rand = new Random();
        int numberOfFood = 0;
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                field[y][x] = white;
            }
        }
        int y = rand.nextInt(height - 3);
        int x = rand.nextInt(width - 3);
        field[y][x] = green;
        field[y][x + 1] = green;
        field[y][x + 2] = green;
        headX = x + 2;
        headY = y;
        direction = 1;
        snakeIn[0][0] = x + 2;
        snakeIn[0][1] = y;
        snakeIn[1][0] = x + 1;
        snakeIn[1][1] = y;
        snakeIn[2][0] = x;
        snakeIn[2][1] = y;
        while (numberOfFood < 50) {
            y = rand.nextInt(height - 1);
            x = rand.nextInt(width - 1);
            if (field[y][x] == white) {
                field[y][x] = red;
                numberOfFood++;
            }
        }
    }

    void updateField() {
        int xHead = snakeIn[0][0];
        int yHead = snakeIn[0][1];
        int xTail = snakeIn[snakeLength - 1][0];
        int yTail = snakeIn[snakeLength - 1][1];
        for (int i = snakeLength - 1; i >= 1; i--) {
            snakeIn[i][0] = snakeIn[i - 1][0];
            snakeIn[i][1] = snakeIn[i - 1][1];
        }
        if (numberOfIterations % 5 == 0 && numberOfIterations != 0) {
            int change = rand.nextInt(2);
            if (change == 2) {
                change = 3;
            }
            direction += change;
            if (direction > 4) {
                direction -= 4;
            }
        }
        if (direction == 1) {
            snakeIn[0][0] = xHead + 1;
            snakeIn[0][1] = yHead;
            if (snakeIn[0][0] >= width) {
                snakeIn[0][0] -= width;
            }
            if (field[snakeIn[0][1]][snakeIn[0][0]] == green) {
                gameIsCorrect = false;
            }
            field[yTail][xTail] = white;
            if (field[snakeIn[0][1]][snakeIn[0][0]] == red) {
                snakeIn[snakeLength][0] = xTail;
                snakeIn[snakeLength][1] = yTail;
                field[yTail][xTail] = green;
                snakeLength++;
                score++;
            }
            field[snakeIn[0][1]][snakeIn[0][0]] = green;
        } else if (direction == 2) {
            snakeIn[0][0] = xHead;
            snakeIn[0][1] = yHead + 1;
            if (snakeIn[0][1] >= height) {
                snakeIn[0][1] -= height;
            }
            if (field[snakeIn[0][1]][snakeIn[0][0]] == green) {
                gameIsCorrect = false;
            }
            field[yTail][xTail] = white;
            if (field[snakeIn[0][1]][snakeIn[0][0]] == red) {
                snakeIn[snakeLength][0] = xTail;
                snakeIn[snakeLength][1] = yTail;
                field[yTail][xTail] = green;
                snakeLength++;
                score++;
            }
            field[snakeIn[0][1]][snakeIn[0][0]] = green;
        } else if (direction == 3) {
            snakeIn[0][0] = xHead - 1;
            snakeIn[0][1] = yHead;
            if (snakeIn[0][0] < 0) {
                snakeIn[0][0] += width;
            }
            if (field[snakeIn[0][1]][snakeIn[0][0]] == green) {
                gameIsCorrect = false;
            }
            field[yTail][xTail] = white;
            if (field[snakeIn[0][1]][snakeIn[0][0]] == red) {
                snakeIn[snakeLength][0] = xTail;
                snakeIn[snakeLength][1] = yTail;
                field[yTail][xTail] = green;
                snakeLength++;
                score++;
            }
            field[snakeIn[0][1]][snakeIn[0][0]] = green;
        } else if (direction == 4) {
            snakeIn[0][0] = xHead;
            snakeIn[0][1] = yHead - 1;
            if (snakeIn[0][1] < 0) {
                snakeIn[0][1] += height;
            }
            if (field[snakeIn[0][1]][snakeIn[0][0]] == green) {
                gameIsCorrect = false;
            }
            field[yTail][xTail] = white;
            if (field[snakeIn[0][1]][snakeIn[0][0]] == red) {
                snakeIn[snakeLength][0] = xTail;
                snakeIn[snakeLength][1] = yTail;
                field[yTail][xTail] = green;
                snakeLength++;
                score++;
            }
            field[snakeIn[0][1]][snakeIn[0][0]] = green;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.scale(scalew, scaleh);
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                colors[x + y * width] = field[y][x];
            }
        }
        canvas.drawBitmap(colors, 0, width, 0, 0, width, height, false, null);
    }
}
