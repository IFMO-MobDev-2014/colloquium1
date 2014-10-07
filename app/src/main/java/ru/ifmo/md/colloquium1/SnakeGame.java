package ru.ifmo.md.colloquium1;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Nikita Yaschenko on 07.10.14.
 */
public class SnakeGame {
    private static final int FOOD_AMOUNT = 50;
    private static final int SCORE_PER_FOOD = 10;

    private int width;
    private int height;
    private boolean lose;

    private LinkedList<Coord> snake;
    private LinkedList<Coord> food;
    private Direction direction;
    private Random random;

    public Cell[][] field;
    private int score;

    public SnakeGame(int width, int height) {
        this.width = width;
        this.height = height;
        lose = false;
        score = 0;

        setupField();
    }

    public int getScore() {
        return score;
    }

    public Cell tick() {
        if (lose) {
            return Cell.SNAKE;
        }
        Coord head = snake.get(0);
        Coord next = next(head, direction);
        Cell nextCell = checkCell(next);
        boolean ateFood = false;
        switch (nextCell) {
            case SNAKE:
                lose = true;
                break;
            case FOOD:
                ateFood = true;
                score += SCORE_PER_FOOD;
                for (Coord f : food) {
                    if (f.x == next.x && f.y == next.y) {
                        food.remove(f);
                        break;
                    }
                }
                break;
            case EMPTY:
                break;
        }
        snake.addFirst(next);
        if (!ateFood) {
            snake.removeLast();
        }
        updateField();
        return Cell.EMPTY;
    }

    private void updateField() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }
        for (Coord c : food) {
            field[c.x][c.y] = Cell.FOOD;
        }
        for (Coord c : snake) {
            field[c.x][c.y] = Cell.SNAKE;
        }
    }

    private void setupField() {
        field = new Cell[width][height];
        random = new Random(213);
        snake = new LinkedList<Coord>();
        food = new LinkedList<Coord>();
        field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }

        int sx = width / 2;
        int sy = height / 2;
        for (int i = 0; i < 3; i++) {
            snake.add(new Coord(sx, sy + i));
        }
        direction = Direction.LEFT;

        for (int i = 0; i < FOOD_AMOUNT; i++) {
            boolean found = false;
            int x = 0, y = 0;
            while (!found) {
                x = random.nextInt(width);
                y = random.nextInt(height);
                found = true;
                for (Coord p : snake) {
                    if (p.x == x && p.y == y) {
                        found = false;
                    }
                }
                for (Coord p : food) {
                    if (p.x == x && p.y == y) {
                        found = false;
                    }
                }
            }
            food.add(new Coord(x, y));
        }
    }

    private Cell checkCell(Coord coord) {
        for (Coord c : food) {
            if (c.x == coord.x && c.y == coord.y) {
                return Cell.FOOD;
            }
        }
        for (Coord c : snake) {
            if (c.x == coord.x && c.y == coord.y) {
                return Cell.SNAKE;
            }
        }
        return Cell.EMPTY;
    }

    private Coord next(Coord c, Direction direction) {
        int x = c.x;
        int y = c.y;
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        if (x < 0) x += width;
        if (x == width) x = 0;
        if (y < 0) y += height;
        if (y == height) y = 0;
        return new Coord(x, y);
    }

    public void setDirection(Direction d) {
        switch (direction) {
            case UP: if (d == Direction.DOWN) return; break;
            case DOWN: if (d == Direction.UP) return; break;
            case LEFT: if (d == Direction.RIGHT) return; break;
            case RIGHT: if (d == Direction.LEFT) return; break;
        }
        direction = d;
    }

    public void setRandomDirection() {
        int r = random.nextInt(4);
        switch (r) {
            case 0:
                if (direction != Direction.UP) {
                    direction = Direction.DOWN;
                }
                break;
            case 1:
                if (direction != Direction.DOWN) {
                    direction = Direction.UP;
                }
                break;
            case 2:
                if (direction != Direction.LEFT) {
                    direction = Direction.RIGHT;
                }
                break;
            case 3:
                if (direction != Direction.RIGHT) {
                    direction = Direction.LEFT;
                }
                break;
        }
    }

    public enum Cell {
        EMPTY, SNAKE, FOOD,
    }

    public enum Direction {
        UP, RIGHT, DOWN, LEFT,
    }

    private class Coord {
        public final int x, y;

        Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


}
