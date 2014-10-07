package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by german on 07.10.14.
 */
public class SnakeRun extends SurfaceView implements Runnable {
    private Thread t = null;
    private SurfaceHolder holder;
    private boolean running;

    private int score;

    private long totalTime;

    private class GameCell {
        int x, y;
        GameCell(int x, int y) {
            if (x < 0) x += fieldWidth;
            if (y < 0) y += fieldHeight;
            if (x >= fieldWidth) x -= fieldWidth;
            if (y >= fieldHeight) y -= fieldHeight;
            this.x = x;
            this.y = y;
        }
        public int getId() {
            return y * fieldWidth + x;
        }
//        void move(int dx, int dy) {
//            x += dx;
//            y += dy;
//            x = (x + fieldWidth) % fieldWidth;
//            y = (y + fieldHeight) % fieldHeight;
//        }
    }

    final private int fieldWidth = 40;
    final private int fieldHeight = 60;

    private Paint paint;

    // 0 - empty, 1 - snake, 2 - apple
    private int field[][] = new int[fieldWidth][fieldHeight];
    private int pixels[] = new int[fieldHeight * fieldWidth];

    private Bitmap bitmap, scaledBitmap;
    private Random random;

    private ArrayList<GameCell> snake = new ArrayList<GameCell>();
    private ArrayList<GameCell> apples = new ArrayList<GameCell>();

    private int dx, dy;

    private void addCellToBody(int x, int y) {
        snake.add(new GameCell(x, y));
        field[x][y] = 1;
        pixels[y * fieldWidth + x] = 0xff00ff00;
    }

    private void removeCellFromBody(int i) {
        if (i < 0 || i >= snake.size()) return;
        GameCell del = snake.get(i);
        field[del.x][del.y] = 0;
        pixels[del.getId()] = 0xff000000;
        snake.remove(i);
    }

    private void addApple() {
        int emptyCnt = (fieldHeight * fieldWidth - apples.size() - snake.size());
        int randCell = random.nextInt(emptyCnt);
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                if (field[x][y] == 0) {
                    if (randCell == 0) {
                        field[x][y] = 2;
                        pixels[y * fieldWidth + x] = 0xffff0000;
                        apples.add(new GameCell(x, y));
                        return;
                    }
                    randCell--;
                }
            }
        }
    }

    private void changeDirection() {
        int orientation = random.nextInt(3);
        int ndx, ndy;
        switch (orientation) {
            case 0:
                ndx = dx;
                ndy = dy;
                break;
            case 1:
                ndx = dy;
                ndy = -dx;
                break;
            default:
                ndx = -dy;
                ndy = dx;
                break;
        }
        dx = ndx;
        dy = ndy;
    }

    private void update() {
//        System.out.println(field[fieldWidth/2][fieldHeight/2]);
        GameCell head = snake.get(0);
        GameCell newHead = new GameCell(head.x + dx, head.y + dy);

        boolean found = false;

        for (ListIterator<GameCell> it = apples.listIterator(); it.hasNext();) {
            GameCell cur = it.next();
            if (newHead.getId() == cur.getId()) {
                found = true;
                break;
            }
        }
        if (!found) {
            removeCellFromBody(snake.size() - 1);
        } else {
            score++;
        }
        snake.add(0, newHead);
        field[newHead.x][newHead.y] = 1;
        pixels[newHead.getId()] = 0xff00ff00;
    }


    public SnakeRun(Context context) {
        super(context);
        holder = getHolder();
        random = new Random(System.currentTimeMillis());
        bitmap = Bitmap.createBitmap(fieldWidth, fieldHeight, Bitmap.Config.RGB_565);
        for(int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                field[x][y] = 0;
                pixels[y * fieldWidth + x] = 0xff000000;
            }
        }
        addCellToBody(fieldWidth / 2 + 2, fieldHeight / 2);
        addCellToBody(fieldWidth / 2 + 1, fieldHeight / 2);
        addCellToBody(fieldWidth / 2, fieldHeight / 2);
        for (int i = 0; i < 50; i++) {
            addApple();
        }
        dx = 1;
        dy = 0;
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    public void onResume() {
        running = true;
        t = new Thread(this);
        t.start();
    }

    public void onPause() {
        running = false;
        try {
            t.join();
        } catch (InterruptedException ignore) {

        }
    }

    @Override
    public void run() {
        totalTime = 0;
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.currentTimeMillis();
                Canvas canvas = holder.lockCanvas();
                draw(canvas);
                update();
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.currentTimeMillis();
                totalTime += (finishTime - startTime);
                if (totalTime >= 5000) {
                    changeDirection();
                    totalTime = 0;
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        bitmap.setPixels(pixels, 0, fieldWidth, 0, 0, fieldWidth, fieldHeight);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), false);
        canvas.drawBitmap(scaledBitmap, 0, 0, null);
        canvas.drawText(Integer.toString(score), 50, 50, paint);
    }
}
