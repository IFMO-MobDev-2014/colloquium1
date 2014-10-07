package ru.ifmo.md.lesson1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MyActivity extends Activity {

    private WhirlView whirlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //whirlView = new WhirlView(this);
        setContentView(R.layout.myfield);
        whirlView = (WhirlView) findViewById(R.id.snake);
        whirlView.scores = (TextView) findViewById(R.id.score);
    }

    @Override
    public void onResume() {
        super.onResume();
        whirlView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        whirlView.pause();
    }

    public void myTrueOnclick(View x) {
        int id = x.getId();
        WhirlView.directions dir = whirlView.direction;
        if (id == R.id.right) {
            if (dir == WhirlView.directions.DOWN) {
                whirlView.direction = WhirlView.directions.RIGHT;
            } else if (dir == WhirlView.directions.RIGHT) {
                whirlView.direction = WhirlView.directions.UP;
            } else if (dir == WhirlView.directions.UP) {
                whirlView.direction = WhirlView.directions.LEFT;
            } else if (dir == WhirlView.directions.LEFT) {
                whirlView.direction = WhirlView.directions.DOWN;
            }
        } else if (id == R.id.left) {
            if (dir == WhirlView.directions.DOWN) {
                whirlView.direction = WhirlView.directions.LEFT;
            } else if (dir == WhirlView.directions.RIGHT) {
                whirlView.direction = WhirlView.directions.DOWN;
            } else if (dir == WhirlView.directions.UP) {
                whirlView.direction = WhirlView.directions.RIGHT;
            } else if (dir == WhirlView.directions.LEFT) {
                whirlView.direction = WhirlView.directions.UP;
            }
        } else if (id == R.id.reset) {
            whirlView.initField();
        }
    }
}
