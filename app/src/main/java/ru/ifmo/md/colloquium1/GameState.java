package ru.ifmo.md.colloquium1;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pva701 on 07.10.14.
 */
public class GameState {
    private int DX[] = new int[]{-1, 0, 1, 0};
    private int DY[] = new int[]{0, -1, 0, 1};

    private int direction = 0;
    public final int WIDTH;
    public final int HEIGHT;
    public final  int FOODS = 50;
    private boolean gameOver = false;

    private ArrayList <Integer> foodX, foodY;
    private ArrayList <Integer> snakeX, snakeY;
    private int[][] ret;

    public GameState(int wi, int he) {
        WIDTH = wi;
        HEIGHT = he;
        Random rnd = new Random(System.currentTimeMillis());
        int x = rnd.nextInt(WIDTH);
        int y = rnd.nextInt(HEIGHT);
        snakeX = new ArrayList<Integer>();
        snakeY = new ArrayList<Integer>();
        foodX = new ArrayList<Integer>();
        foodY = new ArrayList<Integer>();
        snakeX.add(x);
        snakeY.add(y);

        snakeX.add(x);
        snakeY.add((y + 1) % HEIGHT);

        snakeX.add(x);
        snakeY.add((y + 2) % HEIGHT);

        for (int i = 0; i < FOODS; ++i) {
            int fx = rnd.nextInt(WIDTH);
            int fy = rnd.nextInt(HEIGHT);
            foodX.add(fx);
            foodY.add(fy);
        }

        ret = new int[WIDTH][HEIGHT];

        Log.i("SIZE ", "Sn = " + snakeX.size());
        Log.i("SIZE ", "Sn = " + snakeY.size());

        Log.i("SIZE ", "Sn = " + foodX.size());
        Log.i("SIZE ", "Sn = " + foodY.size());
    }

    public int getScore() {
        return snakeX.size();
    }

    public int[][] getField() {
        for (int i = 0; i < WIDTH; ++i)
            for (int j = 0; j < HEIGHT; ++j)
                ret[i][j] = Color.WHITE;
        for (int i = 0; i < foodX.size(); ++i)
            ret[foodX.get(i)][foodY.get(i)] = Color.RED;
        for (int i = 0; i < snakeX.size(); ++i)
            ret[snakeX.get(i)][snakeY.get(i)] = Color.GREEN;
        return ret;
    }

    public void turnLeft() {
        if (gameOver)
            return;
        direction = (direction + 3) % 4;
    }

    public void turnRight() {
        if (gameOver)
            return;
        direction = (direction + 1) % 4;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void move() {
        if (gameOver)
            return;
        int nx = snakeX.get(0) + DX[direction];
        int ny = snakeY.get(0) + DY[direction];
        if (nx < 0) nx = WIDTH - 1;
        if (ny < 0) ny = HEIGHT - 1;
        if (nx >= WIDTH) nx = 0;
        if (ny >= HEIGHT) ny = 0;
        boolean found = false;
        for (int i = 0; i < foodX.size(); ++i)
            if (foodX.get(i) == nx && ny == foodY.get(i)) {
                snakeX.add(0, nx);
                snakeY.add(0, ny);
                foodX.remove(i);
                foodY.remove(i);
                found = true;
            }
        if (!found) {
            for (int i = 0; i < snakeX.size(); ++i)
                if (nx == snakeX.get(i) && ny == snakeY.get(i)) {
                    gameOver = true;
                    return;
                }
        }

        if (!found) {
            snakeX.add(0, nx);
            snakeY.add(0, ny);
            snakeX.remove(snakeX.size() - 1);
            snakeY.remove(snakeY.size() - 1);
        }
    }
}