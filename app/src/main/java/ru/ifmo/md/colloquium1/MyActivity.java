package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MyActivity extends Activity {


    private GameView game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GameView(this);
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
