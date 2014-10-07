package ru.ifmo.md.lesson1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;


public class MyActivity extends Activity {

    private Snake snake;
    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        h = new Handler() {
                   public void  handleMessage(android.os.Message msg) {
                       Toast toast = Toast.makeText(getApplicationContext(), "lose. Your score: " + snake.score, Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();
        }; };
        snake = new Snake(this, h);
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
