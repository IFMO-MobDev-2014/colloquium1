package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pokrasko on 07.10.14.
 */
public class ChangeableView extends ImageView implements Runnable {
    int[] pixels;
    ArrayList<Pair<Integer, Integer>> snake;
    int direction;
    int width;
    int height;
    int size;
    int food;
    Random random;
    Bitmap bitmap;
    Rect rect;
    int w;
    int h;
    boolean collision;
    UpdateTask updateTask;
    Context context;
    Toast toast;
    
    public ChangeableView(Context context) {
        super(context);
        this.context = context;
    }

    private void initTask() {
        width = 40;
        height = 60;
        size = width * height;
        pixels = new int[size];
        snake = new ArrayList<Pair<Integer, Integer>>(3);
        random = new Random();
        rect = new Rect(0, 0, w, h);
    }

    private Pair<Integer, Integer> getDirection(int direction) {
        int dirX;
        int dirY;
        if (direction == 1) {
            dirX = 1;
        } else if (direction == 3) {
            dirX = -1;
        } else {
            dirX = 0;
        }
        if (direction == 2) {
            dirY = 1;
        } else if (direction == 0) {
            dirY = -1;
        } else {
            dirY = 0;
        }
        return new Pair<Integer, Integer>(dirX, dirY);
    }

    public void restart() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        snake.add(new Pair<Integer, Integer>(x, y));
        direction = random.nextInt(4);
        Pair<Integer, Integer> directPair = getDirection(direction);
        pixels[x + width * y] = 0xFF00FF00;
        for (int i = 1; i < 3; i++) {
            x = (x + directPair.first()) % width;
            y = (y + directPair.second()) % height;
            snake.add(new Pair<Integer, Integer>(x, y));
            pixels[x + width * y] = 0xFF00FF00;
        }

        food = 50;
        for (int i = 0; i < food; i++) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            while (pixels[x + width * y] != 0) {
                x = random.nextInt(width);
                y = random.nextInt(height);
            }
            pixels[x + width * y] = 0xFFFF0000;
        }

        invalidate();
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        int collisionX;
        int collisionY;

        @Override
        protected Void doInBackground(Void... ignored) {

            Pair<Integer, Integer> directPair = getDirection(direction);
            int headX = (snake.get(snake.size() - 1).first() + directPair.first()) % width;
            int headY = (snake.get(snake.size() - 1).second() + directPair.second()) % height;
            for (int i = 1; i < snake.size(); i++) {
                if (headX == snake.get(i).first() && headY == snake.get(i).second()) {
                    collision = true;
                    collisionX = headX;
                    collisionY = headY;
                    for (int j = 0; j < snake.size() - 1; j++) {
                        snake.set(j, snake.get(j + 1));
                    }
                    snake.set(snake.size() - 1, new Pair<Integer, Integer>(headX, headY));
                    pixels[headX + width * headY] = 0xFF00FF00;
                    pixels[collisionX + width * collisionY] = 0xFF0000FF;
                    return null;
                }
            }
            if (pixels[headX + width * headY] == 0xFFFF0000) {
                snake.add(new Pair<Integer, Integer>(headX, headY));
            } else {
                pixels[snake.get(0).first() + width * snake.get(0).second()] = 0xFF000000;
                for (int i = 0; i < snake.size() - 1; i++) {
                    snake.set(i, snake.get(i + 1));
                }
                snake.set(snake.size() - 1, new Pair<Integer, Integer>(headX, headY));
            }
            pixels[headX + width * headY] = 0xFF00FF00;
            return null;
        }

        @Override
        protected void onPostExecute(Void ignored) {
            invalidate();
            if (collision) {
                outToast("Collision happened");
            }
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int direction() {
        return direction;
    }

    private void outToast(String message) {
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.setPixels(pixels, width, 0, 0, 0, width, height);
        canvas.drawBitmap(bitmap, null, rect, null);
    }

    public void run() {
        try {
            initTask();
            while (!collision) {
                Thread.sleep(100);
                updateTask = new UpdateTask();
                updateTask.execute();
            }
        } catch (InterruptedException e) {
        }
    }

}
