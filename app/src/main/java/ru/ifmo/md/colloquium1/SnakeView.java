package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SnakeView extends SurfaceView implements Runnable {
    Paint paint = new Paint();
    Canvas canvas;
    int width = 40;
    int height = 60;
    int [][] field = new int[width][height];
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    int[] colours = new int[width * height];
    float xScale;
    float yScale;
    int cnt = 1;
    int score = 0;
    ArrayList<Integer> snakeX = new ArrayList<Integer>();
    ArrayList<Integer> snakeY = new ArrayList<Integer>();
    int[] dx = new int[4];
    int[] dy = new int[4];
    int direction = 0;
    int gameOver = -1;

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
        paint.setTextSize(5);
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
        } catch (InterruptedException ignore) {}
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                canvas = holder.lockCanvas();
                render(canvas);
                if (gameOver == -1) updateField();
                //random turn
                /*if (cnt >= 5) {
                    Random rnd = new Random();
                    int tmp = rnd.nextInt(3) - 1;
                    direction = (direction + tmp);
                    if (direction >= 4) direction -= 4;
                    if (direction < 0) direction += 4;
                    cnt = 1;
                }
                cnt++;*/
                if (gameOver >= 0) {
                    gameOver--;
                    if (gameOver == -1) initField();
                }
                try {
                    thread.sleep(350);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        xScale = ((float) w) / width;
        yScale = ((float) h) / height;
        initField();
    }

    public void initField() {
        gameOver = -1;
        direction = 0;
        score = 0;
        dx[0] = 0; dx[1] = 1; dx[2] = 0; dx[3] = -1;
        dy[0] = 1; dy[1] = 0; dy[2] = -1; dy[3] = 0;
        Random rand = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field[x][y] = Color.WHITE;
            }
        }
        for (int i = 1; i <= 50; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            field[x][y] = Color.RED;
        }
        snakeX.clear();
        snakeY.clear();
        snakeX.add(20); snakeY.add(30);
        snakeX.add(20); snakeY.add(31);
        snakeX.add(20); snakeY.add(32);
    }

    void updateField() {
        int toX = snakeX.get(snakeX.size() - 1) + dx[direction];
        int toY = snakeY.get(snakeY.size() - 1) + dy[direction];
        if (toX < 0) toX += width;
        if (toY < 0) toY += height;
        if (toX >= width) toX -= width;
        if (toY >= height) toY -= height;
        snakeX.add(toX);
        snakeY.add(toY);
        if (field[toX][toY] == Color.RED)  {
            score += 5;
            field[toX][toY] = Color.WHITE;
        } else {
            snakeX.remove(0);
            snakeY.remove(0);
        }

        //collision
        for (int i = 0; i < snakeX.size() - 1; i++) {
            if (toX == snakeX.get(i) && toY == snakeY.get(i)) {
                gameOver = 10;
            }
        }
    }

    public void right() {
        direction--;
        if (direction < 0) direction += 4;
    }

    public void left() {
        direction++;
        if (direction >= 4) direction -= 4;
    }

    public void render(Canvas canvas) {
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    colours[x + y * width] = field[x][y];
            for (int i = 0; i < snakeX.size(); i++) {
                colours[snakeX.get(i) + snakeY.get(i) * width] = Color.GREEN;
            }
            canvas.scale(xScale, yScale);
            canvas.drawBitmap(colours, 0, width, 0, 0, width, height, false, null);
            canvas.drawText("SCORE = " + score, 3, 7, paint);
        if (gameOver >= 0) {
            canvas.drawText("Game over = " + score, 16, 20, paint);
            canvas.drawText("Restart after " + gameOver + " sec.", 5, 30, paint);
        }
    }

}
