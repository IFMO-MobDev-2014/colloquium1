package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by sugakandrey on 19.10.14.
 */
public class MainActivity extends Activity {
    private MyView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout game = new FrameLayout(this);
        gameView = new MyView(this);
        LinearLayout gameWidgets = new LinearLayout (this);
        Button restart = new Button(this);
        Button left = new Button(this);
        Button right = new Button(this);
        restart.setText("restart");
        left.setText("<-");
        right.setText("->");

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.changeDirection(gameView.RIGHT);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.changeDirection(gameView.LEFT);
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.restart();
            }
        });


        gameWidgets.addView(restart);
        gameWidgets.addView(left);
        gameWidgets.addView(right);
        game.addView(gameView);
        game.addView(gameWidgets);
        setContentView(game);

    }


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
