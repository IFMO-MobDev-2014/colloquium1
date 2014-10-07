package ru.ifmo.mobdev.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by sugakandrey on 19.10.14.
 */
public class MyView extends SurfaceView implements Runnable {

    private final Handler handler;
    private Paint p = new Paint();
    private MainActivity mainActivity;

    public void restart() {
        snakeCoordinates.clear();
        score = 0;
        initField();
    }

    public final int DOWN = 0;
    public final int UP = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;

    private static final int height = 40;
    private static final int width = 60;
    private int score = 0;
    private int[] colors;
    private SurfaceHolder holder;
    private Thread thread;
    private volatile boolean running = false;
    private Random rng = new Random();
    private ArrayList<Pair<Integer, Integer>> snakeCoordinates;
    private int currentDirection;
    private int[] dx = new int[4];
    private int[] dy = new int[4];

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

    public MyView(MainActivity mainActivity) {
        super(mainActivity);
        holder = getHolder();
        currentDirection = 1;
        snakeCoordinates = new ArrayList<Pair<Integer, Integer>>();
        p.setTextSize(5);
         handler = new Handler();
        initField();
    }


    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                updateField();
                Canvas canvas = holder.lockCanvas();
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    void initField() {
        colors = new int[40 * 60];
        for (int i = 0; i < width * height; i++) {
            colors[i] = Color.WHITE;
        }
        colors[width / 2 + (height / 2) * width] = Color.GREEN;
        colors[width / 2 + (height / 2 - 1) * width] = Color.GREEN;
        colors[width / 2 + (height / 2 - 2) * width] = Color.GREEN;
        snakeCoordinates.add(new Pair<Integer, Integer>(width / 2, height / 2));
        snakeCoordinates.add(new Pair<Integer, Integer>(width / 2, height / 2 - 1));
        snakeCoordinates.add(new Pair<Integer, Integer>(width / 2, height / 2 - 2));
        for (int i = 0; i < 50; i++) {
            int nextDot = rng.nextInt(width * height);
            while (colors[nextDot] != Color.WHITE) {
                nextDot = rng.nextInt(width * height);
            }
            colors[nextDot] = Color.RED;
        }
        dx[0] = 0; dx[1] = 0; dx[2] = -1; dx[3] = 1;
        dy[0] = 1; dy[1] = -1; dy[2] = 0; dy[3] = 0;
    }

    public void move(){
        int newX = snakeCoordinates.get(snakeCoordinates.size() - 1).first + dx[currentDirection];
        int newY = snakeCoordinates.get(snakeCoordinates.size() - 1).second + dy[currentDirection];
        newX = (newX >= 0)? newX : width - newX;
        newY = (newY >= 0)? newY : height - newY;
        if (colors[(newX + newY * width) % (width * height)] == Color.GREEN){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "GAME OVER", Toast.LENGTH_SHORT).show();
                }
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
            restart();
        }
        snakeCoordinates.add(new Pair<Integer, Integer>(newX, newY));
        if (colors[(newX + newY * width) % (width * height)] == Color.RED){
            colors[(newX + newY * width) % (width * height)] = Color.GREEN;
            score++;
        } else {
            colors[(newX + newY * width) % (width * height)] = Color.GREEN;
            Pair<Integer, Integer> coordinate = snakeCoordinates.get(0);
            colors[(coordinate.first + coordinate.second * width) % (width * height)] = Color.WHITE;
            snakeCoordinates.remove(0);
        }
    }


    public void changeDirection(int turn) {
        if (turn == LEFT) {
            switch (currentDirection) {
                case UP:
                    currentDirection = LEFT;
                    return;
                case DOWN:
                    currentDirection = LEFT;
                    return;
                case LEFT:
                    currentDirection = DOWN;
                    return;
                case RIGHT:
                    currentDirection = UP;
            }
        }
        if (turn == RIGHT) {
            switch (currentDirection) {
                case UP:
                    currentDirection = RIGHT;
                    return;
                case DOWN:
                    currentDirection = RIGHT;
                    return;
                case LEFT:
                    currentDirection = UP;
                    return;
                case RIGHT:
                    currentDirection = DOWN;
            }
        }
    }

    private void updateField() {
        move();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.scale(canvas.getWidth() * 1f / width, canvas.getHeight() * 1f / height);
        canvas.drawBitmap(colors, 0, width, 0f, 0f, width, height, false, null);
        canvas.drawText("Score: " + score, 3, 8, p);
    }
}
