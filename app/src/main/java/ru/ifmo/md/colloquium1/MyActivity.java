package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyActivity extends Activity {
    private SnakeView snakeView;
    Button restartButton, leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = new FrameLayout(this);
        snakeView = new SnakeView(this);
        layout.addView(snakeView);

        LinearLayout linearLayout = new LinearLayout(this);
        restartButton = new Button(this);
        restartButton.setText("restart");
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snakeView = new SnakeView(MyActivity.this);
                setContentView(snakeView);
            }
        });

        leftButton = new Button(this);
        leftButton.setText("<~~");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snakeView.changeDirection(false);
            }
        });

        rightButton = new Button(this);
        rightButton.setText("~~>");
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snakeView.changeDirection(true);
            }
        });

        linearLayout.addView(restartButton);
        linearLayout.addView(leftButton);
        linearLayout.addView(rightButton);
        layout.addView(linearLayout);
        setContentView(layout);
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
    }
