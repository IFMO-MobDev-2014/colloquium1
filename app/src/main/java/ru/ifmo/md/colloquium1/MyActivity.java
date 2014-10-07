package ru.ifmo.md.colloquium1.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MyActivity extends Activity {

    private SnakeView snake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snake = new SnakeView(this);
        setContentView(snake);
    }

    @Override
    public void onResume() {
        super.onResume();
        snake.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        snake.pause();
    }

}
