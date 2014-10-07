package ru.ifmo.md.lesson1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import java.util.logging.LogRecord;


public class MyActivity extends Activity {

    private WhirlView whirlView;

    Handler h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                Toast toast = Toast.makeText(getApplicationContext(), "game over", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        };
        whirlView = new WhirlView(this, h);
        setContentView(whirlView);
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

}
