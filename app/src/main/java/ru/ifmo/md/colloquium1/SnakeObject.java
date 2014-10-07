package ru.ifmo.md.colloquium1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Random;

/**
 * Created by pinguinson on 07.10.2014.
 */

public class SnakeObject extends GameObject {
    private ArrayDeque<Pair> snakePosition;
    public static final int COLOR = 0xFF00FF00;
    private Paint paint;

    public SnakeObject() {
        paint = new Paint();
        paint.setColor(COLOR);
        snakePosition = new ArrayDeque<Pair>();
        snakePosition.addLast(new Pair(12, 10));
        snakePosition.addLast(new Pair(11, 10));
        snakePosition.addLast(new Pair(10 ,10));
    }

    @Override
    void draw(Canvas canvas, float scaleX, float scaleY) {
        for (Pair cur : snakePosition) {
            canvas.drawRect(cur.x*scaleX, cur.y*scaleY, (cur.x+1)*scaleX, (cur.y+1)*scaleY, paint);
        }
    }

    void move() {
        snakePosition.addFirst(getRandom());
    }

    void cut() {
        snakePosition.pollLast();
    }

    Pair getRandom() {
        Pair current = snakePosition.getFirst();
        snakePosition.pollFirst();
        Pair prev = snakePosition.getFirst();
        snakePosition.addFirst(current); //no comments...

        int newX = prev.x;
        int newY = prev.y;
        Random random = new Random();
        while (newX == prev.x && newY == prev.y) {
            int rand = random.nextInt(4);
            switch (rand) {
                case 0:
                    newX = current.x - 1;
                    newY = current.y;
                    break;
                case 1:
                    newX = current.x;
                    newY = current.y + 1;
                    break;
                case 2:
                    newX = current.x + 1;
                    newY = current.y;
                    break;
                case 3:
                    newX = current.x;
                    newY = current.y - 1;
                    break;
            }
        }
        if (newX < 0) {
            newX = SnakeCore.WIDTH + newX;
        }

        if (newY < 0) {
            newY = SnakeCore.HEIGHT + newY;
        }
        newX = newX % SnakeCore.WIDTH;
        newY = newY % SnakeCore.HEIGHT;
        Log.i("POS", Integer.toString(newX) + " " + Integer.toString(newY));
        return new Pair(newX, newY);
    }

    Pair getPosition() {
        return snakePosition.getFirst();
    }
}
