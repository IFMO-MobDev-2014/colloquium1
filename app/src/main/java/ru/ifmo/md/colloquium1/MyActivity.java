package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MyActivity extends Activity {

    SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        snakeView = (SnakeView) findViewById(R.id.snakeview);
    }

    @Override
    public void onResume() {
        super.onResume();
        snakeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        snakeView.pause();
    }

    public void onButtonLeftClick(View view) {
        snakeView.turnLeft();
    }

    public void onButtonRightClick(View view) {
        snakeView.turnRight();
    }

    public void onNewGameClick(View view) {
        snakeView.pause();
        snakeView.newGame();
        snakeView.resume();
    }
}
