package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Y on 07.10.14.
 */
class SnakeView extends SurfaceView implements Runnable {
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    SnakeEngine snakeEngine;
    public SnakeView(Context context){
        super(context);
        holder = getHolder();
        startNewGame();
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


    public void startNewGame(){
        snakeEngine = new SnakeEngine();
    }

    @Override
    public void run(){
        while(running) {
            if(holder.getSurface().isValid()){
                snakeEngine.snakeThink();
                Canvas canvas = holder.lockCanvas();
                drawSnake(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore){}
            }
        }
    }

    int scrW;
    int scrH;
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scrW = w;
        scrH = h;
    }

    //Палитра, определяющая зависимость окраски клетки от её типа
    int[] pallete = {0xFFFFFFFF, 0xFF000000, 0xFFFF0000};

    public void drawSnake(Canvas canvas){
        int w = snakeEngine.w;
        int h = snakeEngine.h;
        int[] img = new int[w * h];
            for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++){
                img[x + y * w] = pallete[snakeEngine.map[x][y]];
            }
        canvas.scale((float)scrW / w, (float)scrH / h);
        canvas.drawBitmap(img, 0, w, 0, 0, w, h, false, null);
    }
}
