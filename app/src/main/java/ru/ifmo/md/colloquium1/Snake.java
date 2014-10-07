package ru.ifmo.md.colloquium1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by nagibator2005 on 2014-10-07.
 */
public class Snake {
    private static final int BACKGROUND = 0xFF000000;
    private static final int SNAKE = 0xFF00FF00;
    private static final int FOOD = 0xFFFF0000;
    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
    public static final int DOWN = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int LEFT = 3;

    private int width, height;
    private int[][] field;
    private int direction;
    ArrayDeque<Point> snake;
    boolean lost;
    int foodCount;
    int score;


    Snake(int width, int height) {
        this.width = width;
        this.height = height;
        snake = new ArrayDeque<Point>();
        field = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = BACKGROUND;
            }
        }
        for (int i = -1; i <= 1; i++) {
            int x = width / 2 + i;
            int y = height / 2;
            field[x][y] = SNAKE;
            snake.addFirst(new Point(x, y));
        }

        foodCount = 0;
        direction = RIGHT;
        lost = false;
        score = 0;
        addRandomFood();
    }

    public void step() {
        Point next = new Point(snake.getFirst());
        Point last = null;
        next.x += dx[direction];
        next.y += dy[direction];
        if (next.x < 0) {
            next.x = width - 1;
        }
        if (next.y < 0) {
            next.y = height - 1;
        }
        if (next.x >= width) {
            next.x = 0;
        }
        if (next.y >= height) {
            next.y = 0;
        }
        switch (field[next.x][next.y]) {
            case BACKGROUND:
                snake.addFirst(next);
                last = snake.removeLast();
                break;
            case FOOD:
                foodCount--;
                snake.addFirst(next);
                score++;
                break;
            case SNAKE:
                lost = true;
                break;
        }
        field[next.x][next.y] = SNAKE;
        if (last != null) {
            field[last.x][last.y] = BACKGROUND;
        }
        if (foodCount < 1) {
            addRandomFood();
        }
    }

    public int getScore() {
        return score;
    }

    public void toBitmapData(int[] data) {
        int cPos = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[cPos] = field[x][y];
                cPos++;
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public void changeDirection(int newDirection) {
        if (((direction ^ newDirection) & 1) == 1) {
            direction = newDirection;
        }
    }

    public boolean isLooser() {
        return lost;
    }

    private void addRandomFood() {
        int x, y;
        do {
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
        } while (field[x][y] != BACKGROUND);
        field[x][y] = FOOD;
        foodCount++;
    }

    private static class Point {
        public int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        Point(Point other) {
            this.x = other.x;
            this.y = other.y;
        }
    }
}
