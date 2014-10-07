package ru.ifmo.md.colloquium1;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Svet on 07.10.2014.
 */
public class Snake {
    ArrayList<Part> snake;
    int defDir = 3;
    public Snake() {
        Random r = new Random();
        snake = new ArrayList<Part>();
        int xHead = 10 + r.nextInt(30);
        int yHead = 10 + r.nextInt(50);
        snake.add(new Part(xHead, yHead, defDir));
        snake.add(new Part(xHead, yHead + 1, defDir));
        snake.add(new Part(xHead, yHead + 2, defDir));
    }

    public void add() {
        Part p = snake.get(snake.size());
        Part another = new Part(0, 0, 0);
        if(p.dir == 0) {
            another.dir = 2;
            another.x = p.x;
            another.y = (p.y + 1) % 60;
        }
        if(p.dir == 1) {
            another.dir = 3;
            another.x = p.x - 1;
            if(another.x < 0) another.x = 39;
            another.y = p.y;
        }
        if(p.dir == 2){
            another.dir = 0;
            another.x = p.x;
            another.y = p.y - 1;
            if(another.y < 0) another.y = 59;
        }
        if(p.dir == 3) {
            another.dir = 1;
            another.x = (p.x + 1) % 40;
            another.y = p.y;
        }
        snake.add(another);
                    // 0 -> 2
            // 1 -> 3
            // 2 -> 0
            // 3 -> 1
    }
// 0 -> 2 1 -> 3
    public void changeDirection(int d) {
        if(!(snake.get(0).dir == (d + 2) % 4))
            snake.get(0).dir = d;
    }

    public void move() {
        Part head = snake.get(0);
        int dx = 0, dy = 0;
        switch (head.dir) {
            case 0 : {
                dy = head.y - 1;
                if(dy < 0) dy = 59;
                dx = head.x;
                replaceTail(dx, dy);
                break;
            }
            case 1 : {
                dy = head.y;
                dx = (head.x + 1)% 40;
                replaceTail(dx, dy);
                break;
            }
            case 2 : {
                dy = (head.y + 1) % 60;
                dx = head.x;
                replaceTail(dx, dy);
                break;
            }
            case 3 : {
                dx = head.x - 1;
                if(dx < 0) dx = 39;
                replaceTail(dx, dy);
                break;
            }
        }
    }

    private void replaceTail(int dx, int dy) {
        for(int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).dir = snake.get(i - 1).dir;
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        snake.get(0).x = dx;
        snake.get(0).y = dy;
    }
}
