package ru.ifmo.md.colloquium1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author volhovm
 *         Created on 10/7/14
 */

public class SnakeEngine implements Runnable {
    public class Snake {
        int snakeColor = Color.BLACK;
        int fieldColor = Color.WHITE;
        int foodColor = Color.GREEN;
        private Random rand = new Random(12542152511241l);
        private ArrayList<SnakePoint> snakeArray;
        private int w, h;
        Dir direction;
        ArrayList<SnakePoint> food;
        Dir newDirection;
        boolean run = true;

        private void up() {snakeArray.set(0, new SnakePoint(snakeArray.get(0).x, snakeArray.get(0).y == 0 ? h - 1 : snakeArray.get(0).y - 1)); }
        private void left() {snakeArray.set(0, new SnakePoint(snakeArray.get(0).x == 0 ? w - 1 : snakeArray.get(0).x - 1, snakeArray.get(0).y));}
        private void down() {snakeArray.set(0, new SnakePoint(snakeArray.get(0).x, snakeArray.get(0).y == h - 1 ? 0 : snakeArray.get(0).y + 1));}
        private void right() {snakeArray.set(0, new SnakePoint(snakeArray.get(0).x == w ? 0 : snakeArray.get(0).x + 1, snakeArray.get(0).y));}

        //TODO
        public void update() {
            if (!run) return;
            for (int i = snakeArray.size() - 1; i > 0; i--) {
                snakeArray.set(i, snakeArray.get(i - 1));
            }
            if (newDirection == Dir.N) up();
            else if (newDirection == Dir.E) left();
            else if (newDirection == Dir.S) down();
            else if (newDirection == Dir.W) right();
            direction = newDirection;
            for (int i = 1; i < snakeArray.size(); i++) {
                if (snakeArray.get(i).equals(snakeArray.get(0))) die();
            }
            if (food.contains(snakeArray.get(0))) { food.remove(food.indexOf(snakeArray.get(0))); snakeArray.add(snakeArray.get(snakeArray.size() - 1)); }
        }



        //READY
        public void setNewDirection(Dir direction) {
            newDirection = direction;
        }

        // READY
        private int[] toArray() {
            int[] out = new int[w * h];
            for (int i = 0; i < out.length; i++) {
                out[i] = fieldColor;
            }
            for (SnakePoint aFood : food) {
                out[aFood.x + aFood.y * w] = foodColor;
            }
            for (SnakePoint point : snakeArray) {
                out[point.x + point.y * w] = snakeColor;
            }
            return out;
        }

        public void stop() {
            run = false;
        }

        // READY
        private class SnakePoint {
            int x, y;
            private SnakePoint(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                SnakePoint that = (SnakePoint) o;

                if (x != that.x) return false;
                if (y != that.y) return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = x;
                result = 31 * result + y;
                return result;
            }
        }


        // READY
        private Snake(int w, int h) {
            this.w = w;
            this.h = h;
            int x = w / 2;
            int y = h / 2;
            this.snakeArray = new ArrayList<SnakePoint>();
            snakeArray.add(new SnakePoint(x, y));
            snakeArray.add(new SnakePoint(x, y - 1));
            snakeArray.add(new SnakePoint(x, y - 2));
            direction = Dir.N;
            genFood();
            newDirection = direction;
        }

        private void genFood() {
            food = new ArrayList<SnakePoint>();
            for (int i = 0; i < 50; i++) {
                food.add(new SnakePoint(rand.nextInt(w - 1) + 1, rand.nextInt(h - 1) + 1));
            }
        }
    }
    private static enum Dir { N, E, S, W }

    private int w = 40;
    private int h = 60;
    private ImageView view;
    MyActivity context;
    private Thread mainThread;
    private boolean running = false;
    private Snake snake;
    public SnakeEngine(MyActivity act) {
        this.view = act.view;
        context = act;
        running = true;
        snake = new Snake(w / 2, h / 2);
        mainThread = new Thread(this);
        mainThread.start();
    }

    // DONE
    @Override
    public void run() {
        while (running) {
            snake.update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(Bitmap.createBitmap(snake.toArray(), snake.w, snake.h, Bitmap.Config.ARGB_4444));
                }
            });
        }
    }

    // TODO
    public void pause() {

    }

    // TODO
    public void resume() {

    }

    // DONE
    public void turnRight() {
        Dir orig = snake.direction;
        Dir newDir = null;
        if (orig == Dir.N) newDir = Dir.W;
        if (orig == Dir.W) newDir = Dir.S;
        if (orig == Dir.S) newDir = Dir.E;
        if (orig == Dir.E) newDir = Dir.N;
        snake.setNewDirection(newDir);
    }

    // DONE
    public void turnLeft() {
        Dir orig = snake.direction;
        Dir newDir = null;
        if (orig == Dir.N) newDir = Dir.E;
        if (orig == Dir.E) newDir = Dir.S;
        if (orig == Dir.S) newDir = Dir.W;
        if (orig == Dir.W) newDir = Dir.N;
        snake.setNewDirection(newDir);
    }


    private void die() {
        Toast a = Toast.makeText(context, "GAME OVER", Toast.LENGTH_LONG);
        a.show();
        snake.stop();
    }

    public void restart() {
        running = false;
        snake = new Snake(w, h);
        try {
            mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        running = true;
        mainThread = new Thread(this);
        mainThread.start();
    }

}
