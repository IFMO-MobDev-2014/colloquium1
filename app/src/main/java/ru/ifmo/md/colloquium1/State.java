package ru.ifmo.md.colloquium1;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Женя on 07.10.2014.
 */
public class State {
    private class Point {
        public int x;
        public int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public boolean equals(Point other) {
            return other.x == x && other.y == y;
        }
        public void set(Point other) {
            x = other.x;
            y = other.y;
        }
    }
    private final int FOOD_CNT = 50;
    private Random rnd;
    private ArrayList<Point> foodList;
    private ArrayList<Point> snake;
    public final int WIDTH = 40;
    public final int HEIGHT = 60;
    private int direction = 0;
    private int[] dx = {0, 1, 0, -1};
    private int[] dy = {-1, 0, 1, 0};
    private int[] colors;
    private int score = 0;

    private int randFromTo(int l, int r) {
        return l + rnd.nextInt(r - l + 1);
    }
    private boolean snakeOnFood(Point p) {
        for (int i = 0; i < snake.size(); i++)
            if (p.equals(snake.get(i)))
                return true;
        return false;
    }
    public State() {
        rnd = new Random(0);
        int snakeX = randFromTo(10, WIDTH - 10);
        int snakeY = randFromTo(10, HEIGHT - 10);
        foodList = new ArrayList<Point>();
        snake = new ArrayList<Point>();
        for (int i = 0; i < 3; i++)
            snake.add(new Point(snakeX + i, snakeY));
        for (int i = 0; i < FOOD_CNT; i++) {
            Point point = new Point(randFromTo(0, WIDTH), randFromTo(0, HEIGHT));
            while (snakeOnFood(point)) {
                point.x = randFromTo(0, WIDTH);
                point.y = randFromTo(0, HEIGHT);
            }
            foodList.add(point);
        }
        colors = new int[WIDTH * HEIGHT];
    }
    public boolean tick() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).set(snake.get(i-1));
        }
        snake.get(0).x = (snake.get(0).x + dx[direction] + WIDTH) % WIDTH;
        snake.get(0).y = (snake.get(0).y + dy[direction] + HEIGHT) % HEIGHT;
        for (int i = 0; i < foodList.size(); i++)
            if (snakeOnFood(foodList.get(i))) {
                Point pt = new Point(0, 0);
                int dx = snake.get(snake.size() - 2).x - snake.get(snake.size() - 1).x;
                int dy = snake.get(snake.size() - 2).y - snake.get(snake.size() - 1).y;
                pt.x = snake.get(snake.size() - 1).x - dx;
                pt.y = snake.get(snake.size() - 1).y - dy;
                pt.x = (pt.x + WIDTH) % WIDTH;
                pt.y = (pt.y + HEIGHT) % HEIGHT;
                snake.add(pt);
                foodList.remove(i);
                ++score;
            }
        return !intersect();
    }
    private boolean intersect() {
        for (int i = 1; i < snake.size(); i++)
            if (snake.get(i).equals(snake.get(0))) {
                return true;
            }
        return false;
    }
    public int[] getColors() {
        for (int i = 0; i < WIDTH * HEIGHT; i++)
            colors[i] = Color.WHITE;
        for (int i = 0; i < foodList.size(); i++) {
            colors[foodList.get(i).y * WIDTH + foodList.get(i).x] = Color.RED;
        }
        for (int i = 0; i < snake.size(); i++) {
            colors[snake.get(i).y * WIDTH + snake.get(i).x] = Color.GREEN;
        }
        return colors;
    }
    public int getScore() {
        return score;
    }
    public void moveLeft() {
        direction = (direction - 1 + 4) % 4;
    }
    public void moveRight() {
        direction = (direction + 1) % 4;
    }
}

