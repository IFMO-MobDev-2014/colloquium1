package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snake_layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y - findViewById(R.id.leftArrowButton).getHeight();
        Button leftArrowButton = (Button) findViewById(R.id.leftArrowButton);
        Button rightArrowButton = (Button) findViewById(R.id.rightArrowButton);
        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        ImageView snakeScreen = (ImageView) findViewById(R.id.snakeScreenView);
        new SnakeTimer(snakeScreen, height, width, leftArrowButton, rightArrowButton, scoreText).execute();
    }

}
