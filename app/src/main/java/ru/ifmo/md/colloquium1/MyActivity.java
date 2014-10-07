package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MyActivity extends Activity {

    private WhirlView whirlView = null;
    private LinearLayout linearLayout = null;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout2);
        toast = new Toast(this);
        whirlView = new WhirlView(this, toast);
        linearLayout.addView(whirlView);
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

    public void leftMove(View v) {
        whirlView.leftMove();
    }

    public void rightMove(View v) {
        whirlView.rightMove();
    }

    public void reset(View v) {
        whirlView.reset();
    }

}
