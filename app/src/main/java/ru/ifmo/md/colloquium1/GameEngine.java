package ru.ifmo.md.colloquium1;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by antonk on 07.10.14.
 */
public class GameEngine {

    public GameEngine() {
        snake.add(new Coordinate(0, 1));
        snake.add(new Coordinate(0, 2));
        snake.add(new Coordinate(0, 3));
        score = 0;
    }

    class Coordinate {
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;

        public boolean equals(Coordinate t) {
            return t.x == x && t.y == y;
        }
    }

    public final static int WIDTH = 40;
    public final static int HEIGHT = 60;

    /*
    0 - UP
    1 - RIGHT
    2 - DOWN
    3 - LEFT
     */

    volatile public int currentDirection = 0;

    volatile public int score = 0;

    public int[] vx = {-1, 0, 1, 0};
    public int[] vy = {0, 1, 0, -1};

    public Coordinate food = new Coordinate(20, 20);
    public ArrayList<Coordinate> snake = new ArrayList<Coordinate>();

    public boolean pushes(Coordinate t) {
        for (int i = 0; i < snake.size(); i++)
            if (snake.get(i).equals(t))
                return true;
        return false;
    }

    public void reset() {
        snake = new ArrayList<Coordinate>();
        snake.add(new Coordinate(0, 1));
        snake.add(new Coordinate(0, 2));
        snake.add(new Coordinate(0, 3));
        food = new Coordinate(20, 20);
        score = 0;
    }
    public boolean makeStep() {
        Coordinate head = snake.get(snake.size() - 1);
        int nx = head.x + vx[currentDirection];
        int ny = head.y + vy[currentDirection];
        nx = (nx + HEIGHT) % HEIGHT;
        ny = (ny + WIDTH) % WIDTH;

        Coordinate newCoordinate = new Coordinate(nx, ny);
        if (pushes(newCoordinate))
            return false;
        snake.add(newCoordinate);
        Random random = new Random();
        if (newCoordinate.equals(food)) {
            score += 1;
            for (food = new Coordinate(Math.abs(random.nextInt()) % HEIGHT, Math.abs(random.nextInt()) % WIDTH);
                    pushes(food); ) {}
        } else {
            snake.remove(0);
        }
        return true;
    }


}
