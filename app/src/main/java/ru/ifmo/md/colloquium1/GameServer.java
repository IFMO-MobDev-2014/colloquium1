package ru.ifmo.md.colloquium1;

import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pva701 on 07.10.14.
 */
public class GameServer {
    private Timer timerGame;
    private int PERIOD = 1000;
    private FieldCanvas painter;
    private GameServer server;
    private Button left;
    private Button right;
    private Button restart;
    private GameState state;

    public GameServer(FieldCanvas painter, Button left, Button right, Button restart) {
        this.left = left;
        this.right = right;
        this.restart = restart;
        this.painter = painter;
        state = new GameState(40, 60);

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

    }

    private void setTimer() {
        timerGame.schedule(new TimerTask() {
            @Override
            public void run() {
                state.move();
                painter.draw(state.getField());

            }
        }, PERIOD, PERIOD);

    }
}
