package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dimatomp on 07.10.14.
 */
public class FieldSurface extends View {
    public static final int INIT_LENGTH = 4;
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    public static final int DELAY = 100;
    private static final Random random = new Random();
    ArrayDeque<Point> snake = new ArrayDeque<>();
    Point candy;
    Timer stepTimer;
    int score;
    MoveDirection direction = MoveDirection.RIGHT;
    Paint paint = new Paint();

    public FieldSurface(Context context) {
        super(context);
        init();
    }

    public FieldSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setScore(int score) {
        this.score = score;
        ((MainActivity) getContext()).reportScore(score);
    }

    private Point getWrapped(int x, int y) {
        return new Point((x % WIDTH + WIDTH) % WIDTH, (y % HEIGHT + HEIGHT) % HEIGHT);
    }

    public void init() {
        setScore(0);
        stepTimer = new Timer();
        random.setSeed(System.currentTimeMillis());
        int x = random.nextInt(WIDTH), y = random.nextInt(HEIGHT);
        snake = new ArrayDeque<>();
        for (int i = 0; i < INIT_LENGTH; i++)
            snake.addLast(getWrapped(x - i, y));
        placeNewCandy();
        paint.setStrokeWidth(getResources().getDimension(R.dimen.field_stroke_width));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        invalidate();
    }

    public void resume() {
        stepTimer.schedule(new GameTask(), DELAY, DELAY);
    }

    public void pause() {
        stepTimer.cancel();
    }

    private void placeNewCandy() {
        do {
            candy = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        } while (snake.contains(candy));
        post(new Invalidator(getCell(candy)));
    }

    public Rect getCell(Point p) {
        return new Rect(getWidth() * p.x / WIDTH, getHeight() * p.y / HEIGHT, getWidth() * (p.x + 1) / WIDTH, getHeight() * (p.y + 1) / HEIGHT);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(android.R.color.holo_green_light));
        for (Point p : snake)
            canvas.drawRect(getCell(p), paint);
        paint.setColor(getResources().getColor(android.R.color.holo_orange_light));
        canvas.drawRect(getCell(candy), paint);
        paint.setColor(0xffcccccc);
        for (int i = 0; i <= WIDTH; i++)
            canvas.drawLine(getWidth() * i / WIDTH, 0, getWidth() * i / WIDTH, getHeight(), paint);
        for (int i = 0; i <= HEIGHT; i++)
            canvas.drawLine(0, getHeight() * i / HEIGHT, getWidth(), getHeight() * i / HEIGHT, paint);
    }

    public synchronized void rotate(MoveDirection direction) {
        if (Math.abs(direction.ordinal() - this.direction.ordinal()) % 2 == 1)
            this.direction = direction;
    }

    public enum MoveDirection {
        UP(0, -1), LEFT(-1, 0), DOWN(0, 1), RIGHT(1, 0);

        public final int x, y;

        MoveDirection(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class GameTask extends TimerTask {
        @Override
        public void run() {
            synchronized (FieldSurface.this) {
                Point newP = getWrapped(snake.getFirst().x + direction.x, snake.getFirst().y + direction.y);
                if (snake.contains(newP)) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getContext()).reset(true);
                        }
                    });
                }
                if (!newP.equals(candy)) {
                    Rect toInv = getCell(snake.getLast());
                    snake.removeLast();
                    post(new Invalidator(toInv));
                } else {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            setScore(score + 5);
                        }
                    });
                    placeNewCandy();
                }
                snake.addFirst(newP);
                post(new Invalidator(getCell(newP)));
            }
        }
    }

    class Invalidator implements Runnable {
        private final Rect which;

        Invalidator(Rect which) {
            this.which = which;
        }

        @Override
        public void run() {
            invalidate(which);
        }
    }
}
