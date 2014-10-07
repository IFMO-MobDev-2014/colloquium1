package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;


public class MyActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new Game(this);
        setContentView(game);
    }

    @Override
    public void onResume() {
        super.onResume();
        game.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        game.pause();
    }
}