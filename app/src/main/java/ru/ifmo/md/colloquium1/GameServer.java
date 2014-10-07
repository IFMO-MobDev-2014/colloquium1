package ru.ifmo.md.colloquium1;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pva701 on 07.10.14.
 */
public class GameServer implements Runnable {
    private Timer timerGame;
    private int PERIOD = 500;
    private FieldCanvas painter;
    private GameServer server;
    private Button left;
    private Button right;
    private Button restart;
    private GameState state;
    private Thread thread;
    private GameState stsv;
    public GameServer(FieldCanvas painter, Button left, Button right, Button restart) {
        this.left = left;
        this.right = right;
        this.restart = restart;
        this.painter = painter;
        state = new GameState(40, 60);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!painter.created());
        timerGame = new Timer();
        setTimer();
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state.turnLeft();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state.turnRight();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           state = new GameState(40, 60);
                                           painter.draw(state.getField());
                                       }
                                   });
                painter.draw(state.getField());
    }

    private void setTimer() {
        timerGame.schedule(new TimerTask() {
            @Override
            public void run() {
                state.move();
                if (!state.isGameOver())
                    painter.draw(state.getField());
                else {
                    painter.drawText("Game Over!\nScore: " + Integer.toString(state.getScore()));
                    timerGame.cancel();
                }
            }
        }, PERIOD, PERIOD);

    }
}
