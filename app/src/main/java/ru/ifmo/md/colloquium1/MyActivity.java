package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MyActivity extends Activity {

    private Snake snake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snake = new Snake(this);
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
