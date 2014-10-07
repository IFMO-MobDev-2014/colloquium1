package ru.ifmo.md.colloquium1;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class Snake {
    public static final int WIDTH = 40;
    public static final int HEIGHT = 60;

    static public final class Cell {
        private final int x;
        private final int y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    static private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN;

        public int dx() {
            switch (this) {
                case LEFT:
                    return -1;
                case RIGHT:
                    return 1;
                default:
                    return 0;
            }
        }

        public int dy() {
            switch (this) {
                case UP:
                    return -1;
                case DOWN:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    private final Deque<Cell> snake;
    private final Direction direction;
    private Thread updaterThread = null;
    private volatile boolean running = true;

    public Deque<Cell> getSnake() {
        return snake;
    }

    public Snake() {
        snake = new LinkedList<Cell>();
        for (int i = 0; i < 3; i++) {
            snake.addFirst(new Cell(i, 0));
        }
        direction = Direction.RIGHT;
    }

    public void pause() {
        running = false;
        try {
            updaterThread.join();
        } catch (InterruptedException ignore) {
        }
    }

    public void resume() {
        running = true;
        updaterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {
                    }

                    update();
                }
            }
        });

        updaterThread.start();
    }

    private static Cell goTo(Cell c, Direction d) {
        return new Cell((c.getX() + d.dx()) % WIDTH, (c.getY() + d.dy()) % HEIGHT);
    }

    private void update() {
        snake.pollLast();
        snake.addFirst(goTo(snake.peekFirst(), direction));
    }
}
