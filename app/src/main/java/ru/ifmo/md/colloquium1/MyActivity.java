package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyActivity extends Activity {
    private MyView view;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_my);

//        textView = (TextView) findViewById(R.id.textView);
//        textView.setText("");

        view = new MyView(this);
        setContentView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        view.pause();
    }
}
