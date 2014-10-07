package ru.ifmo.md.colloquium1;



import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


public class GameView extends SurfaceView implements Runnable {


    class Point {
        public int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    static final int COLOR_BLACK = 0x0;
    static final int COLOR_YELLOW = 0xffff00;

    static final int COLOR_GREEN = 0x00ff00;

    final int height = 60, width = 40;


    private int[] field;
    int score;
    public Direction prevDir = Direction.RIGHT;

    LinkedList<Point> snake;


    Random rand;

    SurfaceHolder holder;
    Thread thread;

    boolean running = true;

    public GameView(Context ctx) {
        super(ctx);
        holder = getHolder();
        rand = new Random();
    }



    Point randomPoint() {
        return new Point(rand.nextInt(width), rand.nextInt(height));
    }

    Point move(Point from, Direction dir) {
        Point r = new Point(from.x, from.y);
        switch(dir) {
            case UP:
                r.y--;
                break;
            case DOWN:
                r.y++;
                break;
            case LEFT:
                r.x--;
                break;
            case RIGHT:
                r.x++;
                break;

        }

        if(r.x >= width) {
            r.x -= width;
        } else if(r.x < 0) {
            r.x += width;
        } else if(r.y >= height) {
            r.y -= height;
        } else if(r.y < 0) {
            r.y += height;
        }
        return r;
    }

    Direction clockwise(Direction dir) {
        switch(dir) {
            case UP:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.DOWN;
            case DOWN:
                return Direction.LEFT;
            case LEFT:
                return Direction.UP;

        }
        return null;
    }
    Direction counterClockwise(Direction dir) {
        switch(dir) {
            case UP:
                return Direction.LEFT;
            case LEFT:
                return Direction.DOWN;
            case DOWN:
            return Direction.RIGHT;
            case RIGHT:
                return Direction.UP;
        }
        return null;
    }

    int idx(Point p) {
        return p.x + p.y * width;
    }



    void newGame() {
        snake = new LinkedList<Point>();
        field = new int[height * width];
        for(int i = 0; i < height * width; i++) {
            field[i] = COLOR_BLACK;
        }
        snake.add(randomPoint());
        field[idx(snake.getLast())] = COLOR_GREEN;
        snake.add(move(snake.getLast(), Direction.RIGHT));
        field[idx(snake.getLast())] = COLOR_GREEN;
        snake.add(move(snake.getLast(), Direction.RIGHT));
        field[idx(snake.getLast())] = COLOR_GREEN;

        // add corn
        for(int i = 0; i < 50; i++)
        {
            Point c = randomPoint();
            if(field[idx(c)] != COLOR_BLACK) {
                continue;
            }
            field[idx(c)] = COLOR_YELLOW;
        }
    }


    void gameStep(Direction dir) {
        prevDir = dir;
        Point newPos = move(snake.getLast(), dir);
        switch(field[idx(newPos)]) {
            case COLOR_GREEN: // eaten by itself
                running = false;
                break;
            case COLOR_BLACK: // rm front
                field[idx(snake.getFirst())] = COLOR_BLACK;
                snake.removeFirst();
                --score;
                // no break
            case COLOR_YELLOW:
                snake.add(newPos);
                field[idx(snake.getLast())] = COLOR_GREEN;
                ++score;
                break;
        }
    }


    void drawField(Canvas canvas) {
        canvas.scale((float) canvas.getWidth() / width, (float) canvas.getHeight() / height);
        canvas.drawBitmap(field, 0, width, 0, 0, width, height, false, null);
    }


    public void resume() {
        running = true;
        newGame();
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
                switch(rand.nextInt(3)) {
                    case 0: gameStep(prevDir); break;
                    case 1: gameStep(clockwise(prevDir)); break;
                    case 2: gameStep(counterClockwise(prevDir)); break;
                }


                Canvas canvas = holder.lockCanvas();
                drawField(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {}
            }
        }
    }
}
