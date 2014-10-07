package ru.ifmo.md.lesson1;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyActivity extends Activity {

    private Snake snake;
    private Handler h;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        h = new Handler() {
           public void  handleMessage(Message msg) {
               Toast toast = Toast.makeText(getApplicationContext(), "Game over.\n Your score: " + msg.what, Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.CENTER, 0, 0);
               toast.show();
        }; };
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);

        snake = new Snake(this, h);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        snake.setLayoutParams(layoutParams);
        layout.addView(snake);
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

    public void left(View view) {
        snake.goLeft();
    }

    public void restart(View view) {
        snake.restart();
    }

    public void right(View view) {
        snake.goRight();
    }
}
