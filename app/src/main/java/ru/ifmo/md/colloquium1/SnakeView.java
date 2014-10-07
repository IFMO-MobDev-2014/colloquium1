package ru.ifmo.md.colloquium1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by vi34 on 07.10.14.
 */



class SnakeView extends SurfaceView implements Runnable {
    int [] field = null;
    final int WIDTH = 40;
    final int HEIGHT = 60;
    int screenW;
    int screenH;
    int score = 0;
    int time = 0;
    int [][] snake;
    int dx = -1;
    int dy = 0;
    int snakeLength = 3;
    SurfaceHolder holder;
    Thread thread = null;
    Canvas canvas;
    Rect screen;
    Paint paint = new Paint();
    Bitmap bmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565);
    Random rand = new Random();

    volatile boolean running = false;

    public SnakeView(Context context) {
        super(context);
        holder = getHolder();
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
                canvas = holder.lockCanvas();
                try {
                    updateField();
                    canvas.drawBitmap(bmap, null, screen, paint);
                    canvas.drawText("SCORE:" + String.valueOf(score), 60, 60, paint);
                } catch (SnakeException e) {
                    paint.setTextSize(70f);
                    canvas.drawText("GAME OVER! SCORE:" + String.valueOf(score), 30, screenH / 2, paint);
                    canvas.drawText("Touch to restart" , 70, screenH / 2 + 80, paint);

                    running = false;
                    //pause();
                }
                holder.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!running) {
            clear();
            initField();
            resume();
        }
        return true;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        screenW = w;
        screenH = h;
        screen = new Rect(0,0,w,h);
        initField();
    }

    void initField() {
        paint.setTextSize(50f);
        paint.setColor(0xFFFFFFFF);
        field = new int[WIDTH * HEIGHT];
        snake = new int[2][1000];
        int dir = rand.nextInt(4);
        switch (dir) {
            case 0:
                dx = -1;
                dy = 0;
                break;
            case 1:
                dx = 1;
                dy = 0;
                break;
            case 2:
                dx = 0;
                dy = 1;
                break;
            case 3:
                dx = 0;
                dy = -1;
                break;
        }

        int xx = rand.nextInt(WIDTH);
        int yy = rand.nextInt(HEIGHT);
        for(int i = 0; i < snakeLength; ++i) {
            snake[0][i] = xx;
            snake[1][i] = yy;

            field[xx + yy * WIDTH] = Color.GREEN;
            xx = (xx - dx + WIDTH) % WIDTH;
            yy = (yy - dy + HEIGHT) % HEIGHT;
        }
        for(int food = 0; food < 50;){
            int x = rand.nextInt(40);
            int y = rand.nextInt(60);
            if( field[x + y * WIDTH] != Color.GREEN && field[x + y * WIDTH] != Color.RED)
            {
                food++;
                field[x + y * WIDTH] = Color.RED;
            }
        }

    }

    void clear() {
        for(int i = 0; i < WIDTH; i++)
        {
            for(int j = 0; j < HEIGHT; j++){
                field[i + j * WIDTH] = Color.BLACK;
            }
        }
        for(int i = 0; i < snakeLength; ++i)
        {
            snake[0][i] = 0;
            snake[1][i] = 0;
        }
        snakeLength = 3;
        dx = -1;
        dy = 0;
        time = 0;
        score = 0;
    }


    void updateField() throws SnakeException{
        time++;
        int move = 2;
        if(time == 4) {
            move = rand.nextInt(3);
            time = 0;
        }
        if(move == 0)
        {
            // left
            if(dx == 0) {

                if(dy == -1) {
                    dx = -1;
                } else {
                    dx = 1;
                }
                dy = 0;
            } else {
                if(dx == -1) {
                    dy = 1;
                } else {
                    dy = -1;
                }
                dx = 0;
            }
        } else if(move == 1)
        {
            // right
            if(dx == 0) {
                if(dy == -1) {
                    dx = 1;
                } else {
                    dx = -1;
                }
                dy = 0;
            } else {
                if(dx == -1) {
                    dy = -1;
                } else {
                    dy = 1;
                }
                dx = 0;
            }

        }

        int nx = (snake[0][0] + dx + WIDTH) % WIDTH;
        int ny = (snake[1][0] + dy + HEIGHT) % HEIGHT;
        int x = snake[0][snakeLength - 1];
        int y = snake[1][snakeLength - 1];
        boolean add = false;
        boolean exc = false;
        if (field[nx + ny * WIDTH] == Color.RED){
            add = true;

        } else if(field[nx + ny * WIDTH] == Color.GREEN){
            exc = true;
        } else {
            field[x + y * WIDTH] = Color.BLACK;
        }

        for(int i = snakeLength - 1; i > 0; --i)
        {
            snake[0][i] = snake[0][i - 1];
            snake[1][i] = snake[1][i - 1];
        }
        if(add) {
            snakeLength++;
            score++;
            snake[0][snakeLength - 1] = x;
            snake[1][snakeLength - 1] = y;
        }
        snake[0][0] = (snake[0][0] + dx + WIDTH) % WIDTH;
        snake[1][0] = (snake[1][0] + dy + HEIGHT) % HEIGHT;
        x = snake[0][0];
        y = snake[1][0];
        field[x + y * WIDTH] = Color.GREEN;
        if(exc)
        {
            throw new SnakeException("");
        }


        bmap.setPixels(field, 0, WIDTH,0,0, WIDTH, HEIGHT);

    }
}
