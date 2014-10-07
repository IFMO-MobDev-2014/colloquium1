package ru.ifmo.md.lesson1;

import android.app.Activity;
import android.os.Bundle;


public class MyActivity extends Activity {

    private SnakeGame SnakeGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SnakeGame = new SnakeGame(this);
        setContentView(SnakeGame);
    }

    @Override
    public void onResume() {
        super.onResume();
        SnakeGame.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        SnakeGame.pause();
    }
}
