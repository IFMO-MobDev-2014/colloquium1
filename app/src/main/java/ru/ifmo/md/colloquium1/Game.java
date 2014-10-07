package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Random;


class Game extends SurfaceView implements Runnable {
    Bitmap screen = Bitmap.createBitmap(40, 60, Bitmap.Config.RGB_565);
    Random gen = new Random();
    Field field = new Field();
    Snake snake = new Snake();
    Paint paint = new Paint();
    int [] colors = new int[3];
    int step;
    boolean notOver = true;


    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;

    public Game(Context context) {
        super(context);
        holder = getHolder();
        step = 0;
        colors[0] = 0x000000;
        colors[1] = 0x00FF00;
        colors[2] = 0xFF0000;
        paint.setColor(Color.BLUE);
        paint.setTextSize(80);
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
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                if (notOver) {
                    snake.move();
                }
                if (++step % 4 == 0) {
                    int dir = snake.direction;
                    int newdir = gen.nextInt(4);
                    if (dir != newdir && (dir + newdir) % 2 == 0) {
                        snake.direction = dir;
                    }
                    else {
                        snake.direction = newdir;
                    }
                    step = 0;
                };
                draw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                double fps = 1000000000.0 / (finishTime - startTime);
                Log.i("TIME", "FPS: "  + fps);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 60; j++) {
                screen.setPixel(i, j, colors[field.cells[i][j]]);
            }
        }
        canvas.drawBitmap(Bitmap.createScaledBitmap(screen, canvas.getWidth(), canvas.getHeight(), false), 0, 0, null);
        canvas.drawText("Score:" + (snake.length - 3), 20, 90, paint);
        if (!notOver) {
            canvas.drawText("Game over!", 300, 800, paint);
        }
    }

    private class Snake {
        int length;
        int direction;
        int headX;
        int headY;
        ArrayList<Integer> cellsX;
        ArrayList<Integer> cellsY;

        public Snake() {
            this.length = 3;
            this.direction = 0;
            this.cellsX = new ArrayList<Integer>();
            this.cellsY = new ArrayList<Integer>();
            this.headX = 5 + gen.nextInt(30);
            this.headY = 5 + gen.nextInt(50);
            for (int i = 0; i < 3; i++) {
                cellsX.add(headX + i);
                cellsY.add(headY);
                field.cells[cellsX.get(cellsX.size() - 1)][cellsY.get(cellsY.size() - 1)] = 1;
            }
        }

        private void move() {
            switch (direction) {
                case 0:
                    int aux = cellsX.get(cellsX.size() - 1) + 1;
                    if (aux >= 40)
                        aux -= 40;
                    cellsX.add(aux);
                    cellsY.add(cellsY.get(cellsY.size() - 1));
                    break;
                case 1:
                    cellsX.add(cellsX.get(cellsX.size() - 1));
                    aux = cellsY.get(cellsY.size() - 1) + 1;
                    if (aux >= 60)
                        aux -= 60;
                    cellsY.add((cellsY.get(cellsY.size() - 1) + 1) % 60);
                    break;
                case 2:
                    aux = cellsX.get(cellsX.size() - 1) - 1;
                    if (aux < 0)
                        aux += 40;
                    cellsX.add(aux);
                    cellsY.add(cellsY.get(cellsY.size() - 1));
                    break;
                case 3:
                    aux = cellsY.get(cellsY.size() - 1) - 1;
                    if (aux < 0)
                        aux += 60;
                    cellsX.add(cellsX.get(cellsX.size() - 1));
                    cellsY.add(aux);
                    break;
            }
            switch (field.cells[cellsX.get(cellsX.size() - 1)][cellsY.get(cellsY.size() - 1)]) {
                case 0:
                    field.cells[cellsX.get(0)][cellsY.get(0)] = 0;
                    cellsX.remove(0);
                    cellsY.remove(0);
                    break;
                case 1:
                    //GAME OVER
                    notOver = false;
                    break;
                case 2:
                    length++;
                    break;
            }
            field.cells[cellsX.get(cellsX.size() - 1)][cellsY.get(cellsY.size() - 1)] = 1;
        }
    }

    public class Field {
        int [][] cells;

        public Field() {
            this.cells = new int[40][60];
            fillFood();
        }

        public void fillFood() {
            ArrayList<Pair> coords = new ArrayList<Pair>();
            while (coords.size() < 50) {
                coords.add(new Pair(gen.nextInt(40), gen.nextInt(60)));
            }
            for (Pair i : coords) {
                cells[(Integer)i.first][(Integer)i.second] = 2;
            }
        }

    }
}
