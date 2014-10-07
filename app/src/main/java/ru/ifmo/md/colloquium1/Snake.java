package ru.ifmo.md.colloquium1;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class Snake {
    public static final int WIDTH = 40;
    public static final int HEIGHT = 60;
    public static final int START_LENGTH = 3;
    private static final Direction[] directions = {Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN};
    public static final int STEPS_DELAY = 200;
    private final Food food;
    private final Deque<Cell> snake;
    private Direction direction;
    private int length = START_LENGTH;
    private int score = 0;
    private Thread updaterThread = null;
    private volatile boolean running = true;

    private ScoreChangedListener listener;
    private OnCollisionListener onCollisionListener;

    static interface ScoreChangedListener {
        void onScoreChanged(int score);
    }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            return x == cell.x && y == cell.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    public static enum Direction {
        LEFT(0),
        UP(1),
        RIGHT(2),
        DOWN(3);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

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

    public Snake(Food food) {
        snake = new LinkedList<Cell>();
        this.food = food;
        for (int i = 0; i < START_LENGTH; i++) {
            snake.addFirst(new Cell(i, 0));
        }
        direction = Direction.RIGHT;
    }

    public Deque<Cell> getSnake() {
        return snake;
    }

    public void rotate(Direction to) {
        this.direction = directions[
                (this.direction.getValue() + to.dx() + directions.length) % directions.length];
    }

    public void setListener(ScoreChangedListener listener) {
        this.listener = listener;
    }

    public void setOnCollisionListener(OnCollisionListener onCollisionListener) {
        this.onCollisionListener = onCollisionListener;
    }

    public ScoreChangedListener getListener() {
        return listener;
    }

    public OnCollisionListener getOnCollisionListener() {
        return onCollisionListener;
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
                        Thread.sleep(STEPS_DELAY);
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

    private void checkCollisions() {
        for (Cell a : snake) {
            for (Cell b : snake) {
                if (a != b && a.equals(b)) {
                    onCollisionListener.onCollision();
                }
            }
        }
    }

    private void update() {
        if (snake.size() >= length) {
            snake.pollLast();
        }
        snake.addFirst(goTo(snake.peekFirst(), direction));
        checkEaten();
    }

    private void checkEaten() {
        for (Food.Fruit fruit : food.getFruits()) {
            Cell head = snake.peekFirst();
            if (fruit.getX() == head.getX() && fruit.getY() == head.getY()) {
                food.eaten(fruit);
                eaten();
            }
        }
    }

    private void eaten() {
        length++;
        score++;
        listener.onScoreChanged(score);
    }

    interface OnCollisionListener {
        public void onCollision();
    }
}
