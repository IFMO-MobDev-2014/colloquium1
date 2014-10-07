package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Freemahn on 07.10.2014.
 */
public class SnakeView extends SurfaceView implements Runnable {
    final int width = 40;
    final int height = 60;
    float scaleX = 0;
    float scaleY = 0;
    Thread thread = null;
    volatile boolean running = false;
    int[][] field;
    int headX, headY;
    int tailX, tailY;
    int[] palette = {Color.WHITE, Color.RED, Color.GREEN};
    int pixels[];
    SurfaceHolder holder;
    Canvas canvas;
    Random r;
    int dx = 0;
    int dy = 0;
    int score;
    ArrayList<Pair<Integer, Integer>> commands;
    boolean failed;

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();

    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleX = (float) w / width;
        scaleY = (float) h / height;
        initField();
    }


    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                canvas = holder.lockCanvas();
                update();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                //  Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                //time = (finishTime - startTime) / 1000000;
                // frames++;
                // fps = (float) (frames * 1000.0 / allTime);
                // allTime += time;
                //  Log.i("FPS", "FPS: " + fps + " " + "currentFPS: " + (1000.0 / time));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        }

    }

    public void initField() {
        r = new Random();
        dx = 1;
        dy = 0;
        headX = r.nextInt(width);
        headY = r.nextInt(height);
        tailX = headX - 2;
        tailY = headY;
        commands = new ArrayList<Pair<Integer, Integer>>();
        commands.add(new Pair<Integer, Integer>(1, 0));
        commands.add(new Pair<Integer, Integer>(1, 0));
        field = new int[width][height];
        field[headX][headY] = 2;//snake
        int a = headX - 1 > 0 ? headX - 1 : width - (headX - 1);
        int b = headX - 2 > 0 ? headX - 2 : width - (headX - 2);
        a %= width;
        b %= width;
        field[a][headY] = 2;
        field[b][headY] = 2;
        int countCreatedFood = 0;
        pixels = new int[width * height];
        while (countCreatedFood < 51) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            if (field[x][y] == 0) {
                field[x][y] = 1;//food init
                countCreatedFood++;

            }
        }

    }

    public void update() {
        int direction = new Random().nextInt(4) + 1;
        if (direction % 2 == 0) { //dont turn
            move();
        } else if (direction == 1) {//turn right
            int t = dx;
            dx = dy;
            dy = t;

            move();


        } else { //turl left
            int t = dx;
            dx = -dy;
            dy = t;
            move();
        }
    }

    void move() {
        commands.add(new Pair(dx, dy));
        headX += commands.get(commands.size() - 1).first;
        headY += commands.get(commands.size() - 1).second;
        if (headX < 0) headX += width;
        if (headY < 0) headX += height;
        headX %= width;
        headY %= height;


        if (field[headX][headY] == 2) {
            failed = true;
            onDraw(canvas);
            return;

        }
        if (field[headX][headY] == 1)
            score++;
        if (field[headX][headY] != 1) {
            field[tailX][tailY] = 0;
            tailX += commands.get(0).first;
            tailY += commands.get(0).second;
            tailX %= width;
            tailY %= height;
            if (tailX < 0) tailX += width;
            if (tailY < 0) tailY += height;
            commands.remove(0);
        }

        field[headX][headY] = 2;

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

    public void onDraw(Canvas canvas) {

        canvas.scale(scaleX, scaleY);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x + y * width] = palette[field[x][y]];
            }
        }
        canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, false, null);
        if (failed) {
            canvas.drawText("GAME OVER!", 0, 50, new Paint());
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        canvas.drawText("" + score, 0, 25, new Paint(Paint.FAKE_BOLD_TEXT_FLAG));


    }

}
