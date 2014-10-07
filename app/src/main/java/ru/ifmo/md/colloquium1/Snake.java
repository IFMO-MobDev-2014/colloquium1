package ru.ifmo.md.colloquium1;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Алексей on 07.10.2014.
 */
public class Snake extends AsyncTask<Void, Bitmap, Void> {
    ImageView imageView;
    TextView score;
    public static final int RED = 0xFFFF0000;
    public static final int GREEN = 0xFF00FF00;
    public static final int WHITE = 0x0F000000;
    public int[][] field;
    public static final int height = 60;
    public static final int width = 40;
    public Deque<Point> snake;
    public static final int[] dx = {1,0,-1,0};
    public static final int[] dy = {0,1,0,-1};
    public int direction;
    public int countFood = 0;
    public boolean changed;
    public boolean flag = true;

    public class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void left() {
        if (!changed) {
            direction = (direction+3)%4;
        }
        changed = true;
    }

    public void right() {
        if (!changed) {
            direction = (direction + 1)%4;
        }
        changed = true;
    }

    public Snake(ImageView imageView, int countFood, TextView textView) {
        this.imageView = imageView;
        this.countFood = countFood;
        this.score = textView;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        initField();
        Bitmap bmp;
        int[] color = new int[width*height];
        while (flag) {
            changed = false;
            moveSnake();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    color[i * width + j] = field[j][i];
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bmp = Bitmap.createBitmap(color, width, height, Bitmap.Config.ARGB_8888);
            bmp = Bitmap.createScaledBitmap(bmp, 500, 500, false);

            publishProgress(bmp);
        }
        return null;
    }

    public void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = next(head);
        if (field[newHead.x][newHead.y] != RED) {
            field[snake.getLast().x][snake.getLast().y] = WHITE;
            snake.removeLast();
        }
        if (check(newHead)) {
            field[newHead.x][newHead.y] = GREEN;
            snake.addFirst(newHead);
        } else {
            gameOver();
        }
    }

    public Point next(Point o) {
        return new Point((o.x + dx[direction] + width) % width, (o.y + dy[direction] + height) % height);
    }

    public boolean check(Point o) {
        for (Point x : snake) {
            if (x.x == o.x && x.y == o.y) {
                return false;
            }
        }
        return true;
    }

    public void gameOver() {
        flag = false;

    }

    public void initField() {
        field = new int[width][height];
        snake = new ArrayDeque<Point>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field[x][y] = WHITE;
            }
        }
        Random rnd = new Random();
        int startY = rnd.nextInt(height);
        field[0][startY] = GREEN;
        field[1][startY] = GREEN;
        field[2][startY] = GREEN;
        snake.addFirst(new Point(0, startY));
        snake.addFirst(new Point(1, startY));
        snake.addFirst(new Point(2, startY));

        for (int i = 0; i < countFood; i++) {
            int x,y;
            do {
                x = rnd.nextInt(width);
                y = rnd.nextInt(height);
            } while (!check(new Point(x,y)));

            field[x][y] = RED;

        }

        direction = 0;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        Log.v("bmp",""+(values[0] == null));
        Log.v("bmp",""+(imageView == null));
        imageView.setImageBitmap(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        score.setText("Game over!!!");

    }
}
