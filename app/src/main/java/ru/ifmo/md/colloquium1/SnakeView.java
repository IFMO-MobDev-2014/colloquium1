package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

    //Слудующий шаг для случайного поворота
    int nextStep = 1;
    public void randomMove(){
        nextStep--;
        if(nextStep <= 0){
            nextStep = (int)(2 + Math.random() * 5 + 1);
            if(Math.random() > 0.5){
                snakeEngine.snakeRight();
            } else {
                snakeEngine.snakeLeft();
            }
        }
    }
    @Override
    public void run(){
        while(running) {
            if(holder.getSurface().isValid()){
                randomMove();
                snakeEngine.snakeThink();

                Canvas canvas = holder.lockCanvas();

                drawSnake(canvas);
                drawScore(canvas);
                if(snakeEngine.isGameOver)
                    drawGameOver(canvas);

                holder.unlockCanvasAndPost(canvas);


                try {
                    Thread.sleep(100);
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
        canvas.scale(1f, 1f);
    }

    public void drawScore(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(0x99999999);
        paint.setTextSize(3f);
        canvas.drawText("SCORE: " + Integer.toString(snakeEngine.score), 10f, 4f, paint);
    }
    public void drawGameOver(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(0xFF990000);
        paint.setTextSize(3f);
        canvas.drawText("GAME OVER", 10f, 10f, paint);
    }
}
