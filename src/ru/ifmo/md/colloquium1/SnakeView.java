package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.*;

class SnakeView extends SurfaceView implements Runnable {
    Context context;
    MainActivity ma;
    int[] field;
    final int fieldW = 40;
    final int fieldH = 60;
    final int greenN = 50;
    int cnt;
    TextView score;
    final int diffX[] = {0, 1, 0, -1};
    final int diffY[] = {1, 0, -1, 0};
    public int direction = 0;
    ArrayList<Integer> snakeX = new ArrayList<Integer>();
    ArrayList<Integer> snakeY = new ArrayList<Integer>();
    final int green = 0xFF00FF00;
    final int white = 0xFFFFFFFF;
    final int black = 0xFF000000;
    int width;
    int height;
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    public boolean directionChanged = false;
    Paint paint = new Paint();

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
        width = getWidth();
        height = getHeight();
        this.context = context;
    }

    public SnakeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        holder = getHolder();
        width = getWidth();
        height = getHeight();
        this.context = context;
    }

    public SnakeView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        holder = getHolder();
        width = getWidth();
        height = getHeight();
        this.context = context;
    }

    public void setScoreView(TextView s) {
        score = s;
    }

    public void setMainActivity(MainActivity m) {
        ma = m;
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ignore) {}
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                try {
                    updateField();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        initField();
    }

    void initField() {
        cnt = 0;
        ma.setScore(cnt);

        Random rand = new Random(System.nanoTime());
        field = new int[fieldH * fieldW];
        Arrays.fill(field, black);
        int tmpX = Math.abs(rand.nextInt()) % fieldW;
        int tmpY = Math.abs(rand.nextInt()) % fieldH;
        for (int i = 0; i < greenN; i++) {
            while (field[tmpY * fieldW + tmpX] == green) {
                tmpX = Math.abs(rand.nextInt()) % fieldW;
                tmpY = Math.abs(rand.nextInt()) % fieldH;
            }
            field[tmpY * fieldW + tmpX] = green;
        }

        snakeY = new ArrayList<Integer>();
        snakeX = new ArrayList<Integer>();
        snakeX.add(0);
        snakeX.add(0);
        snakeX.add(0);
        snakeY.add(0);
        snakeY.add(1);
        snakeY.add(2);
    }

    private void gameOver() {
        initField();
        ma.makeGOToast();
    }

    public static boolean contains(final ArrayList<Integer> array, final int v) {
        for (int i = 0; i < array.size(); i++)
            if (array.get(i).equals((Integer)v))
                return true;

        return false;
    }

    void updateField() throws InterruptedException {
        directionChanged = false;

        int currX = snakeX.get(snakeX.size() - 1);
        int currY = snakeY.get(snakeY.size() - 1);
        if (contains(snakeX, ((currX + diffX[direction]) % fieldW)) &&
                contains(snakeY, ((currY + diffY[direction]) % fieldH)))
            gameOver();
        if (field[((currX + diffX[direction]) % fieldW) + ((currY + diffY[direction]) % fieldH )* fieldW] == green) {
            field[((currX + diffX[direction]) % fieldW) + ((currY + diffY[direction]) % fieldH )* fieldW] = 0;
            ma.setScore(++cnt);
        }
        else {
            snakeX.remove(0);
            snakeY.remove(0);
        }
        snakeX.add(currX + diffX[direction]);
        snakeY.add(currY + diffY[direction]);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int tmpfield[] = new int[fieldH * fieldW];
        System.arraycopy(field, 0, tmpfield, 0, fieldH * fieldW);
        for (int i = 0; i < snakeY.size(); i++) {
            tmpfield[(snakeX.get(i) % fieldW) + (snakeY.get(i) % fieldH) * fieldW] = white;
        }

        canvas.scale((float)getWidth() / fieldW, (float)getHeight() / fieldH);
        canvas.drawBitmap(tmpfield, 0, fieldW, 0, 0, fieldW, fieldH, false, paint);
    }
}