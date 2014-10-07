package ru.ifmo.md.colloquium1;

import java.util.ArrayList;
import java.util.Random;

public class Snake {
    class Pos {
        int x, y;

        public Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    private static enum direction{NORTH, SOUTH, WEST, EAST};
    private ArrayList<Pos> snake = new ArrayList<Pos>();

    int Score = 0;
    direction SnakeDirection = direction.EAST;
    int isGrowing = 0;
    int field[][];
    int width = 40;
    int height = 60;

    Random rnd = new Random();

    Snake(){
        field = new int[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                field[i][j] = 0;
            }
        }
        int x = width/2, y = height/2;
        snake.add(new Pos(x, y));
        field[x--][y--] = 1;
        snake.add(new Pos(x, y));
        field[x--][y--] = 1;
        snake.add(new Pos(x, y));
        field[x][y] = 1;
        addFruit();
    }

    private void addFruit() {
        int count = 0;
        while(count < 50){
            int x = rnd.nextInt(width);
            int y = rnd.nextInt(height);
            if(field[x][y] == 0){
                field[x][y] = 2;
                count++;
            }
        }
    }

    public boolean move(){
        switch (SnakeDirection) {
            case NORTH:{
                int nextX = snake.get(snake.size() - 1).x;
                int nextY = snake.get(snake.size() - 1).y - 1;
                if(nextY == -1 && field[nextX][height - 1] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(nextX, height - 1));
                    field[nextX][height - 1] = 1;
                } else if(nextY == -1 && field[nextX][height - 1] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(nextX, height - 1));
                    field[nextX][height - 1] = 1;
                } else if(nextY >= 0&& field[nextX][nextY] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else if(nextY >= 0 && field[nextX][nextY] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else if(nextY >= 0 && field[nextX][nextY] == 1){
                    return false;
                }  else
                    return false;
                return true;
            }
            case SOUTH:{
                int nextX = snake.get(snake.size() - 1).x;
                int nextY = snake.get(snake.size() - 1).y + 1;
                if(nextY == height && field[nextX][0] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(nextX, 0));
                    field[nextX][0] = 1;
                } else if(nextY == height && field[nextX][0] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(nextX, 0));
                    field[nextX][0] = 1;
                } else if(nextY < height && field[nextX][nextY] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else if(nextY < height && field[nextX][nextY] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                //} else if(nextY < height && field[nextX][nextY] == 1){ return false;
                } else
                    return false;
                return true;
            }
            case WEST:{

                int nextX = snake.get(snake.size() - 1).x - 1;
                int nextY = snake.get(snake.size() - 1).y;
                if(nextX == -1&& field[width - 1][nextY] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(width - 1, nextY));
                    field[width - 1][nextY] = 1;
                } else if(nextX == -1 && field[width - 1][nextY] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(width - 1, nextY));
                    field[width - 1][nextY] = 1;
                } else if(nextX >= 0 && field[nextX][nextY] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else if(nextX >= 0 && field[nextX][nextY] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else
                    return false;
                return true;

            }
            case EAST:{
                int nextX = snake.get(snake.size() - 1).x + 1;
                int nextY = snake.get(snake.size() - 1).y;
                if(nextX == width && field[0][nextY] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(0, nextY));
                    field[0][nextY] = 1;
                } else if(nextX == width && field[0][nextY] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(0, nextY));
                    field[0][nextY] = 1;
                } else if(nextX < width && field[nextX][nextY] == 0){
                    if(isGrowing > 0){
                        isGrowing--;
                    } else{
                        field[snake.get(0).x][snake.get(0).y] = 0;
                        snake.remove(0);
                    }
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else if(nextX < width && field[nextX][nextY] == 2){
                    isGrowing+=2;
                    Score++;
                    snake.add(new Pos(nextX, nextY));
                    field[nextX][nextY] = 1;
                } else
                    return false;
                return true;
            }

        }
        return false;
    }

    public direction getSnakeDirection(){
        return SnakeDirection;
    }

    public void setSnakeDirection(direction d){
        SnakeDirection = d;
    }

    public int[][] getField(){
        return field;
    }

    public int getSnakeSize(){
        return snake.size();
    }

    public ArrayList<Pos> getSnake(){
        return snake;
    }
}
