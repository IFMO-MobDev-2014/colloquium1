package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by anton on 07/10/14.
 */
public class SnakeGame {
    public class GameOverException extends Exception {
    }

    public static int FOOD_COUNT = 50;

    private static int SNAKE_COLOR = Color.GREEN;
    private static int BG_COLOR = Color.BLACK;
    private static int FOOD_COLOR = Color.RED;

    private static int W = SnakeView.W;
    private static int H = SnakeView.H;

    public static class Pixel {
        int x;
        int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private SnakeActivity activity;
    private int direction;
    private LinkedList<Pixel> body;
    private ArrayList<Pixel> foods = new ArrayList<Pixel>();

    private int[] pixels;

    public SnakeGame(SnakeActivity activity, int x, int y, int direction, int init_length) {
        this.activity = activity;
        this.direction = direction;
        this.body = new LinkedList<Pixel>();
        this.pixels = new int[W * H];
        for (int i = 0; i < W * H; i++) {
            this.pixels[i] = BG_COLOR;
        }
        Pixel px = new Pixel(x, y);
        for (int i = 0; i < init_length; i++) {
            this.body.addFirst(px);
            setPixel(px.x, px.y, SNAKE_COLOR);
            px = updatePixel(px);
        }

        // set score
        activity.setScore(body.size());
    }

    private void setPixel(int x, int y, int c) {
        pixels[y * W + x] = c;
    }

    private Pixel updatePixel(Pixel px) {
        int x = px.x;
        int y = px.y;
        if (direction == 0) {
            y--;
        } else if (direction == 1) {
            x++;
        } else if (direction == 2) {
            y++;
        } else /* if (direction == 3) */ {
            x--;
        }
        if (x < 0) {
            x += W;
        }
        if (x >= W) {
            x -= W;
        }
        if (y < 0) {
            y += H;
        }
        if (y >= H) {
            y -= H;
        }
        return new Pixel(x, y);
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void prolongateSnake() throws GameOverException {
        Pixel px = body.getFirst();
        int x = px.x;
        int y = px.y;
        Pixel newPx = updatePixel(px);
        // see if it came across food
        boolean doGrow = false;
        for (int i = 0; i < foods.size(); i++) {
            Pixel food = foods.get(i);
            if (food != null) {
                if (food.x == newPx.x && food.y == newPx.y) {
                    foods.set(i, null);
                    doGrow = true;
                }
            }
        }
        // see if it came across itself
        for (int i = 0; i < body.size(); i++) {
            Pixel part = body.get(i);
            if (part.x == newPx.x && part.y == newPx.y) {
                throw new GameOverException();
            }
        }
        // add head
        body.addFirst(newPx);
        setPixel(newPx.x, newPx.y, SNAKE_COLOR);
        // remove last piece
        if (!doGrow) {
            Pixel lastOne = body.removeLast();
            setPixel(lastOne.x, lastOne.y, BG_COLOR);
        }

        // set score
        activity.setScore(body.size());
    }

    public void addFood(Pixel food) {
        foods.add(food);
        setPixel(food.x, food.y, FOOD_COLOR);
    }

    public int[] getPixels() {
        return pixels;
    }
}
