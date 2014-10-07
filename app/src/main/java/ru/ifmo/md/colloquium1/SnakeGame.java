package ru.ifmo.md.lesson1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

enum Direction {
    FRONT, LEFT, RIGHT, BACK
}

class Snake {
    Snake() {
        nodes = new ArrayList<SnakeNode>();
    }
    ArrayList<SnakeNode> nodes;
    Direction curDir;
}

class SnakeNode {
    SnakeNode(int tempx, int tempy) {
        x = tempx;
        y = tempy;
    }

    int x;
    int y;
}

class SnakeGame extends SurfaceView implements Runnable {
    int Score = 0;
    Snake snake = new Snake();
    int [][] field = null;
    int counter = 0;
    int width = 40;
    int height = 60;
    int scaleH;
    int scaleW;
    boolean ok = true;
    Paint paint = new Paint();
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;

    public SnakeGame(Context context) {
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
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                onDraw(canvas);
                if(ok) {
                    updateField();
                }
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleW = w / width;
        scaleH = h / height;
        initField();
    }

    void initField() {
        field = new int[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                field[i][j] = Color.BLACK;
            }
        }

        int tempx;
        int tempy;

        while (counter < 50){
            Random rand = new Random();
            tempx = rand.nextInt(100000) % 40;
            tempy = rand.nextInt(100000) % 60;
            if(field[tempx][tempy] != Color.RED) {
                field[tempx][tempy] = Color.RED;
                counter++;
            }
        }

        boolean head = false;

        while(!head) {
            Random rand = new Random();
            tempx = rand.nextInt(100000) % 40;
            tempy = rand.nextInt(100000) % 60;
            if(field[tempx][tempx] == Color.BLACK) {
                field[tempx][tempy] = Color.GREEN;
                field[tempx][(tempy + 1) % height] = Color.GREEN;
                field[tempx][(tempy + 1) % height] = Color.GREEN;

                SnakeNode node1 = new SnakeNode(tempx, tempy);
                SnakeNode node2 = new SnakeNode(tempx, (tempy + 1) % height);
                SnakeNode node3 = new SnakeNode(tempx, (tempy + 2) % height);
                snake.nodes.add(node1);
                snake.nodes.add(node2);
                snake.nodes.add(node3);

                head = true;
            }
        }
    }

    void updateField() {
        Random rand = new Random();
        int dir = rand.nextInt(10) % 3;
        int x  = snake.nodes.get(0).x;
        int y = snake.nodes.get(0).y;

        if(dir  != 2) {
            if (snake.curDir == Direction.FRONT) {
                if (dir == 0) {
                    x = Math.abs((x - 1) % width);
                    snake.curDir = Direction.LEFT;
                } else if (dir == 1) {
                    x = Math.abs((x + 1) % width);
                    snake.curDir = Direction.RIGHT;
                }
            } else if (snake.curDir == Direction.LEFT) {
                if (dir == 0) {
                    y = Math.abs((y + 1) % height);
                    snake.curDir = Direction.BACK;
                } else if (dir == 1) {
                    y = Math.abs((y - 1) % height);
                    snake.curDir = Direction.FRONT;
                }
            } else if (snake.curDir == Direction.RIGHT) {
                if (dir == 0) {
                    y = Math.abs((y - 1) % height);
                    snake.curDir = Direction.FRONT;
                } else if (dir == 1) {
                    y = Math.abs((y + 1) % height);
                    snake.curDir = Direction.BACK;
                }
            } else {
                if (dir == 0) {
                    x = Math.abs((x + 1) % width);
                    snake.curDir = Direction.RIGHT;
                } else if (dir == 1) {
                    x = Math.abs((x - 1) % width);
                    snake.curDir = Direction.LEFT;
                }
            }
        } else {
            if(snake.curDir == Direction.FRONT) {
                y = Math.abs((y - 1) % height);
            } else if(snake.curDir == Direction.LEFT) {
                x = Math.abs((x - 1) % width);
            } else if(snake.curDir == Direction.RIGHT) {
                x = Math.abs((x + 1) % width);
            } else {
                y = Math.abs((y + 1) % height);
            }
        }

        if(field[x][y] == Color.RED) {
            SnakeNode node = new SnakeNode(x, y);
            Score++;
            field[x][y] = Color.GREEN;
            snake.nodes.add(0, node);
        } else if(field[x][y] == Color.BLACK) {
            field[x][y] = Color.GREEN;
            int tx = snake.nodes.get(snake.nodes.size() - 1).x;
            int ty = snake.nodes.get(snake.nodes.size() - 1).y;
            field[tx][ty] = Color.BLACK;
            SnakeNode node = snake.nodes.get(snake.nodes.size() - 1);
            node.x = x;
            node.y = y;
            snake.nodes.add(0, node);
            snake.nodes.remove(snake.nodes.size() - 1);
        } else {
            ok = false;
            for(int i = 0; i < 40; i++) {
                for(int j = 0; j < 60; j++) {
                    field[i][j] = Color.WHITE;
                }
           }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                paint.setColor(field[x][y]);
                canvas.drawRect(x * scaleW, y * scaleH, (x+1) * scaleW, (y+1) * scaleH, paint);
            }
        }

        if(!ok) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(50);
            canvas.drawText("Game over score = " + Integer.toString(Score), 200, 600, paint);
        }
    }
}
