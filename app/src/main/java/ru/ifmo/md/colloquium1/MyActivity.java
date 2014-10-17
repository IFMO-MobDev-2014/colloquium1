package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MyActivity extends Activity {
    private ChangeableView imageView;
    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        imageView = (ChangeableView) findViewById(R.id.image);
        scoreView = (TextView) findViewById(R.id.score);
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        imageView.pause();
    }

    public void leftClick(View view) {
        if (imageView.direction() != 1) {
            imageView.setDirection(3);
        }
    }

    public void rightClick(View view) {
        if (imageView.direction() != 3) {
            imageView.setDirection(1);
        }
    }

    public void upClick(View view) {
        if (imageView.direction() != 2) {
            imageView.setDirection(0);
        }
    }

    public void downClick(View view) {
        if (imageView.direction() != 0) {
            imageView.setDirection(2);
        }
    }

    public void restartClick(View view) {
        imageView.pause();
        imageView.resume();
    }
}
