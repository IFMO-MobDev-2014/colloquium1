package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import ru.ifmo.md.lesson1.R;


public class MyActivity extends Activity {
    SnakeEngine snake;
    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        view = (ImageView) findViewById(R.id.imageView);
        snake = new SnakeEngine(this);
        snake.restart();
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

    public void onLeftButtonPress(View view) {
        snake.turnLeft();
    }

    public void onResetButtonPress(View view) {
        snake.restart();
    }

    public void onRightButtonPress(View view) {
        snake.turnRight();
    }
}
