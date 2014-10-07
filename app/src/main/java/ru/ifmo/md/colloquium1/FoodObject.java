package ru.ifmo.md.colloquium1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by pinguinson on 07.10.2014.
 */

public class FoodObject extends GameObject {
    public static final int COLOR = Color.parseColor("red");
    private Paint paint;
    private Pair position;

    public FoodObject(Pair position) {
        this.position = position;
        paint = new Paint();
        paint.setColor(COLOR);
    }

    @Override
    void draw(Canvas canvas, float scaleX, float scaleY) {
        canvas.drawRect(position.x * scaleX, position.y * scaleY, (position.x + 1) * scaleX, (position.y + 1) * scaleY, paint);
    }

    public FoodObject () {
        Random random = new Random();
        int x = random.nextInt(SnakeCore.WIDTH);
        int y = random.nextInt(SnakeCore.HEIGHT);
        this.position = new Pair(x, y);
        paint = new Paint();
        paint.setColor(COLOR);
    }

    public void delete(Canvas canvas, float scaleX, float scaleY) {
        paint.setColor(SnakeCore.BG_COLOR);
        canvas.drawRect(position.x * scaleX, position.y * scaleY, (position.x + 1) * scaleX, (position.y + 1) * scaleY, paint);
        paint.setColor(COLOR);
    }

    public Pair getPosition() {
        return position;
    }
}
