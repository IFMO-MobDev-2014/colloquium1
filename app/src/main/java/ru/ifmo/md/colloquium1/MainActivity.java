package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vlad107.snake.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;


public class MainActivity extends ActionBarActivity {

    private SnakeView snakeView;
    protected TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snakeView = new SnakeView(this);
        setContentView(R.layout.activity_main);
        LinearLayout lnr = (LinearLayout) findViewById(R.id.linear);
        lnr.addView(snakeView);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void onResume() {
        super.onResume();
        snakeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        snakeView.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void clickLeft(View view) {
        snakeView.goLeft();
    }

    public void clickRight(View view) {
        snakeView.goRight();
    }

    public void incScore() {
//        String text = textView.getText().toString();
//        if (text.isEmpty()) {
//            text = "0";
//        }
//        int score = Integer.valueOf(text);
//        score++;
//        textView.setText(String.valueOf(score));
    }

    class SnakeView extends SurfaceView implements Runnable {
        final int WIDTH = 40;
        final int HEIGHT = 60;
        final int[] PALETTE = {0xFFF0F8FF, 0x00FF00, 0xFF0000};
        final int[] DX = {+1, 0, -1, 0};
        final int[] DY = {0, +1, 0, -1};
        final Random RAND = new Random();
        int score = 0;
        int dir = 0;
        SurfaceHolder holder;
        Thread thread;
        Canvas canvas;
        float scaleX, scaleY;
        int[] colors = new int[WIDTH * HEIGHT];
        int[][] field = new int [WIDTH][HEIGHT];
        volatile boolean running = false;
        ArrayList<Pair<Integer, Integer>> snake = new ArrayList<Pair<Integer, Integer>>();
        ArrayList<Pair<Integer, Integer>> food = new ArrayList<Pair<Integer, Integer>>();
        Context context;
        Handler h2;

        SnakeView(Context context) {
            super(context);
            this.h2 = h2;
            this.context = context;
            holder = getHolder();
            resume();
        }

        public void pause() {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException ignore) {
            }
        }

        public void resume() {
            running = true;
            thread = new Thread(this);
            thread.start();
        }

        @Override
        public void onSizeChanged(int w, int h, int oldW, int oldH) {
            scaleX = ((float) w) / WIDTH;
            scaleY = ((float) h) / HEIGHT;
            initField();
        }

        void initField() {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    field[x][y] = 0;
                }
            }
            snake.clear();
            snake.add(Pair.create(WIDTH / 2, HEIGHT / 2));
            snake.add(Pair.create(WIDTH / 2 + 1, HEIGHT / 2));
            snake.add(Pair.create(WIDTH / 2 + 2, HEIGHT / 2));
            for (Pair<Integer, Integer> cur : snake) {
                field[cur.first][cur.second] = 1;
            }
            food.clear();
            while (food.size() < 50) {
                int x = RAND.nextInt(WIDTH);
                int y = RAND.nextInt(HEIGHT);
                if (field[x][y] == 0) {
                    food.add(Pair.create(x, y));
                    field[x][y] = 2;
                }
            }
        }

        @Override
        public void run() {
            int cnt = 0;
            while (running) {
                if (holder.getSurface().isValid()) {
                    canvas = holder.lockCanvas();
                    updateField();
                    drawIt();
                    Log.i("run: ", String.valueOf(cnt++));
                    holder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }

        public void drawIt() {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    colors[y * WIDTH + x] = PALETTE[field[x][y]];
                }
            }
            canvas.scale(scaleX, scaleY);
            canvas.drawBitmap(colors, 0, WIDTH, 0, 0, WIDTH, HEIGHT, false, null);
        }

        void onLose() {
        }

        void updateField() {
            int curX = snake.get(snake.size() - 1).first;
            int curY = snake.get(snake.size() - 1).second;
            curX += DX[dir];
            curY += DY[dir];
            if (curX >= WIDTH) {
                curX -= WIDTH;
            }
            if (curX < 0) {
                curX += WIDTH;
            }
            if (curY >= HEIGHT) {
                curY -= HEIGHT;
            }
            if (curY < 0) {
                curY += HEIGHT;
            }
            if (field[curX][curY] == 1) {
                onLose();
            } else {
                snake.add(Pair.create(curX, curY));
                if (field[curX][curY] != 2) {
                    field[snake.get(0).first][snake.get(0).second] = 0;
                    snake.remove(0);
                } else {
                }
                field[curX][curY] = 1;
            }
        }

        public void goLeft() {
            dir = dir + 3;
            if (dir >= 4) {
                dir -= 4;
            }
        }

        public void goRight() {
            dir = dir + 1;
            if (dir >= 4) {
                dir -= 4;
            }
        }
    }
}

