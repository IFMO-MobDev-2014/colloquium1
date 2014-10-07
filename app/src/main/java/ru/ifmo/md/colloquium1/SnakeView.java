package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;


class SnakeView extends SurfaceView implements Runnable {
    class pair {
        public int x;
        public int y;

        public pair(int a,int b) {
            x=a;
            y=b;
        }
    }

    boolean flag = false;
    Queue<Integer> moves = new ArrayDeque<Integer>();
    Random rand = new Random();
    pair head = new pair(20,20);
    pair tail = new pair(20,22);
    int field[][] = null;
    int[] bits = null;
    int width = 40;
    int height = 60;
    float scale_W = 1;
    float scale_H = 1;
    static final int MAX_COLOR = 3;
    int[] palette = {0xFFFFE4C4, 0xFFFFF0F5, 0xFF6495ED};
    SurfaceHolder holder;
    Thread thread = null;
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
        } catch (InterruptedException ignore) {
        }
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                updateField();
                Draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scale_H = (float) w / width;
        scale_W = (float) h / height;
        initField();
    }

    void initField() {
        field = new int [width][height];
        bits = new int[width * height];
        for (int i=0; i<width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = 1;
            }
        }

        for (int x = 1; x < 51; x++) {
            field[rand.nextInt(width)][rand.nextInt(height)]=2;
        }
        field[20][20]=0;
        field[20][21]=0;
        field[20][22]=0;
        moves.add(2);
        moves.add(2);
    }

    boolean isSnake(int a, int b){
        if (field[a][b]==0) {
            return true;
        } else {
            return false;
        }
    }

    void updateField() {
        flag = false;
        int move = rand.nextInt(4);
        if ((move==0) && !(isSnake(head.x+1,head.y))) {
            head.x++;
            moves.add(0);
            flag=true;
        }
        if ((move==1) && !(isSnake(head.x,head.y+1))) {
            head.y++;
            flag=true;
            moves.add(1);
        }
        if ((move==2) && !(isSnake(head.x-1,head.y))) {
            head.x--;
            flag=true;
            moves.add(2);
        }
        if ((move==3) && !(isSnake(head.x,head.y-1))) {
            head.y--;
            flag=true;
            moves.add(3);
        }

        if (field[head.x][head.y]==1) {
            Log.i("TIME", "Circle: " + field[tail.x][tail.y]);
            field[tail.x][tail.y]=1;
            if (moves.peek()==0) {
                tail.x++;
            }
            if (moves.peek()==1) {
                tail.y++;
            }
            if (moves.peek()==2) {
                tail.x--;
            }
            if (moves.peek()==3) {
                tail.y--;
            }
            Log.i("TIME", "Circle: " + field[tail.x][tail.y]);
            moves.remove();
        }
        if (flag) {
            field[head.x][head.y] = 0;
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bits[i+width*j]=palette[field[i][j]];
            }
        }
    }

    //@Override
    public void Draw(Canvas canvas) {
        canvas.scale(scale_H, scale_W);
        canvas.drawBitmap(bits, 0, width, 0, 0, width, height, false, null);
    }
}