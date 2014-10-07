package ru.ifmo.md.colloquium1;

import android.graphics.Color;

import java.util.Arrays;
import java.util.Random;

public class Field {
    int score = 0;
    int[] field;
    Snake snake;
    int WIDTH = 40;
    int HEIGHT = 60;
    int SNAKE = Color.GREEN;
    int GROUND = Color.BLACK;
    int FOOD = Color.RED;

    Field(Snake s) {
        field = new int[WIDTH * HEIGHT];
        Arrays.fill(field, GROUND);
        snake = s;
        placeSnake();
        placeFood();
    }

    public void deleteSnake() {
        for(int i = 0; i < snake.snake.size(); i++) {
            Part p = snake.snake.get(i);
            field[p.x + WIDTH * p.y] = GROUND;
        }
    }

    public void placeSnake() {
        for(int i = 0; i < snake.snake.size(); i++) {
            Part p = snake.snake.get(i);
            if(field[p.x + WIDTH * p.y] == FOOD) score++;
            field[p.x + WIDTH * p.y] = SNAKE;
        }
    }

    private void placeFood() {
        Random r = new Random();
        for(int i = 0; i < 50; i++) {
            boolean success = true;
            while(success) {
                int x = r.nextInt(40);
                int y = r.nextInt(60);
                if(field[x + y * WIDTH] == GROUND) {
                    field[x + y * WIDTH] = FOOD;
                    success = false;
                }
            }
        }
    }
}