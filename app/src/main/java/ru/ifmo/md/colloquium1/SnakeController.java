package ru.ifmo.md.colloquium1;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by creed on 07.10.14.
 */

public class SnakeController {
    public int[] field, capture;
    public boolean valid;
    public long prevTime;
    public ArrayList<Point> snake;
    public int offset;
    public final int W = 40;
    public final int H = 60;
    public int dest;
    final Point[] ds;
    SnakeController() {
        prevTime = System.currentTimeMillis();
        field = new int[W*H];
        capture = new int[W*H];
        valid = true;
        for(int i = 0; i < W*H; i++) {
            field[i] = Color.BLACK;
            capture[i] = Color.BLACK;
        }
        for(int i = 0; i < 50; i++) {
            int x = (int)(Math.random()*W*H);
            field[x] = Color.RED;
        }

        snake = new ArrayList<Point>();
        snake.add(new Point(W/2, H/2));
        snake.add(new Point(W/2-1, H/2));
        snake.add(new Point(W/2+1, H/2));
        offset = 0;
        ds = new Point[4];
        ds[0] = new Point(0, -1);
        ds[1] = new Point(1, 0);
        ds[2] = new Point(0, 1);
        ds[3] = new Point(-1, 0);
        dest = 0;
    }
    public int getCell(Point t) {
        return field[t.y*W+t.x];
    }
    public int setCell(Point t, int val) {
        return field[t.y*W+t.x] = val;
    }
    public Point getNext(Point head) {
        long now = System.currentTimeMillis();
        long elapsedTime = now - prevTime;
        if (elapsedTime > 5000) {
            prevTime = now;

            int rnd = (int)(Math.random()*3);
            if (rnd == 0) {
                dest = (dest + 1) % 4;
            } else if (rnd == 1){
                dest = (dest - 1 + 4) % 4;
            }
        }

        return new Point((head.x + ds[dest].x + W)%W, (head.y + ds[dest].y + H)%H);
    }

    public void go() {
        Point head = snake.get(snake.size() - 1);
        Point newHead = getNext(head);
        if (getCell(newHead) == Color.RED) {
            //eat cell
            setCell(newHead, Color.BLACK);
            snake.add(newHead);
        } else if (capture[newHead.y*W+newHead.x] == Color.GREEN) {
            valid = false;
        } else {
            offset++;
            snake.add(newHead);
        }

    }
    public int getSize() {
        return snake.size() - offset;
    }
    public void iterate() {
        go();
        for(int i = 0; i < field.length; i++) {
            capture[i] = field[i];
        }
        for(int i = offset; i < snake.size(); i++) {
            int pos = snake.get(i).y*W+snake.get(i).x;
            capture[pos] = Color.GREEN;
        }
    }
}
