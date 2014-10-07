package ru.ifmo.md.colloquium1;

import java.util.ArrayList;
import android.content.Context;
/**
 * Created by serge_000 on 07.10.2014.
 */
public class SnakeGame {
    public class position{
        int x;
        int y;
        position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    public static final int Vverh = 1;
    public static final int Vpravo = 2;
    public static final int Vniz = 3;
    public static final int Vlevo = 4;
    public static int mFieldX = 40;
    public static int mFieldY = 60;
    public int mScore=0;
    private int mField[][] = new int[mFieldX][mFieldY];
    private ArrayList<position> mSnake = new ArrayList<position>();
    int mNaprav = SnakeGame.Vlevo;
    int rastet = 0;
    SnakeGame(){
        for(int i = 0; i < mFieldX; ++i) {
            for (int j = 0; j < mFieldY; ++j) {
                mField[i][j] = 0;
            }
        }
    mSnake.add(new position(2, 2));
    mField[2][2] = -1;
    mSnake.add(new position(2, 3));
    mField[2][3] = -1;
    mSnake.add(new position(2, 4));
    mField[2][3] = -1;
    addFruite();
    }
    public void addFruite(){
        int scet = 0;
        while(scet < 50){
            int x = (int) (Math.random() * mFieldX);
            int y = (int) (Math.random() * mFieldY);
            if (mField[x][y] == 0) {
                mField[x][y] = 2;
                scet++;
            }
        }
    }

    public void addOneFruite(){
        int scet = 0;
        while(scet != 0){
            int x = (int) (Math.random() * mFieldX);
            int y = (int) (Math.random() * mFieldY);
            if (mField[x][y] == 0) {
                mField[x][y] = 2;
                scet++;
            }
        }
    }

    public boolean sledHod(){
        switch(this.mNaprav){
        case Vverh: {
            int nextX = mSnake.get(mSnake.size() - 1).x;
            int nextY = mSnake.get(mSnake.size() - 1).y - 1;
            if ((nextY >= 0) && mField[nextX][nextY] == 0) {
                if (rastet > 0)
                    rastet++;
                else {
                    mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                    mSnake.remove(0);
                }
                mSnake.add(new position(nextX, nextY));
                mField[nextX][nextY] = -1;
                return true;
            } else {
                if ((nextY >= 0) && mField[nextX][nextY] == 1)
                    return false;
                else {
                    if ((nextY >= 0) && mField[nextX][nextY] > 1) {
                        rastet += 2;
                        mScore++;
                        mField[nextX][nextY] = 0;
                        mSnake.add(new position(nextX, nextY));
                        mField[nextX][nextY] = -1;
                        //addOneFruite();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
            case Vpravo:{
                int nextX = mSnake.get(mSnake.size() - 1).x + 1;
                int nextY = mSnake.get(mSnake.size() - 1).y;
                if ((nextY >= 0) && mField[nextX][nextY] == 0) {
                    if (rastet > 0)
                        rastet++;
                    else {
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    mSnake.add(new position(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    return true;
                } else {
                    if ((nextY >= 0) && mField[nextX][nextY] == 1)
                        return false;
                    else {
                        if ((nextY >= 0) && mField[nextX][nextY] > 1) {
                            rastet += 2;
                            mScore++;
                            mField[nextX][nextY] = 0;
                            mSnake.add(new position(nextX, nextY));
                            mField[nextX][nextY] = -1;
                            //addOneFruite();
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            case Vniz:{
                int nextX = mSnake.get(mSnake.size() - 1).x;
                int nextY = mSnake.get(mSnake.size() - 1).y + 1;
                if ((nextY >= 0) && mField[nextX][nextY] == 0) {
                    if (rastet > 0)
                        rastet++;
                    else {
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    mSnake.add(new position(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    return true;
                } else {
                    if ((nextY >= 0) && mField[nextX][nextY] == 1)
                        return false;
                    else {
                        if ((nextY >= 0) && mField[nextX][nextY] > 1) {
                            rastet += 2;
                            mScore++;
                            mField[nextX][nextY] = 0;
                            mSnake.add(new position(nextX, nextY));
                            mField[nextX][nextY] = -1;
                            //addOneFruite();
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            case Vlevo:{
                int nextX = mSnake.get(mSnake.size() - 1).x - 1;
                int nextY = mSnake.get(mSnake.size() - 1).y;
                if ((nextY >= 0) && mField[nextX][nextY] == 0) {
                    if (rastet > 0)
                        rastet++;
                    else {
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    mSnake.add(new position(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    return true;
                } else {
                    if ((nextY >= 0) && mField[nextX][nextY] == 1)
                        return false;
                    else {
                        if ((nextY >= 0) && mField[nextX][nextY] > 1) {
                            rastet += 2;
                            mScore++;
                            mField[nextX][nextY] = 0;
                            mSnake.add(new position(nextX, nextY));
                            mField[nextX][nextY] = -1;
                            //addOneFruite();
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getDirectore() {
        return mNaprav;
    }

    public void clearScore(){
        this.mScore=0;
    }

    public void setDirection(int direction) {
        this.mNaprav = direction;
    }

    public int[][] getmField() {
        return mField;
    }

    public int getSnakeLength() {
        return  mSnake.size();
    }

    public ArrayList<position> getmSnake() {
        return mSnake;
    }
}
