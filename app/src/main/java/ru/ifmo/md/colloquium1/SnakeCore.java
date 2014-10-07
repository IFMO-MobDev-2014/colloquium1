package ru.ifmo.md.colloquium1;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by pinguinson on 07.10.2014.
 */
public class SnakeCore {
    public static final int WIDTH = 40;
    public static final int HEIGHT = 60;
    public static final int BG_COLOR = Color.parseColor("black");

    private FoodObject food;
    private SnakeObject snake;

    public SnakeCore() {
        food = new FoodObject();
        snake = new SnakeObject();
    }

    public void draw(Canvas canvas) {
        float scaleX = (float) canvas.getWidth() / (float) WIDTH;
        float scaleY = (float) canvas.getHeight() / (float) HEIGHT;
        canvas.drawColor(BG_COLOR);
        food.draw(canvas, scaleX, scaleY);
        snake.draw(canvas, scaleX, scaleY);
    }

    public void step(Canvas canvas) {
        snake.move();
        if (!checkFood(canvas)) {
            snake.cut();
        }

    }

    public boolean checkFood(Canvas canvas) {
        if (snake.getPosition().x == food.getPosition().x && snake.getPosition().y == food.getPosition().y) {
            //delete food obj
            float scaleX = (float) canvas.getWidth() / (float) WIDTH;
            float scaleY = (float) canvas.getHeight() / (float) HEIGHT;
            food.delete(canvas, scaleX, scaleY);
            food = new FoodObject();
            return true;
        }
        return false;
    }

}
