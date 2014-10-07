package ru.ifmo.md.lesson1;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MyActivity extends Activity {

    private MainThreadWrapper mainThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        final WhirlView whirlView = (WhirlView) findViewById(R.id.surfaceView);
        whirlView.setTextView((TextView) findViewById(R.id.textView));
        whirlView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mainThread = new MainThreadWrapper(whirlView);

        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whirlView.dir -= 1;
                if (whirlView.dir < 1)
                    whirlView.dir += 4;
                whirlView.dir_x = whirlView.get_x(whirlView.dir);
                whirlView.dir_y = whirlView.get_y(whirlView.dir);
            }
        });

        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whirlView.initField();
            }
        });

        ((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whirlView.dir += 1;
                if (whirlView.dir > 4)
                    whirlView.dir -= 4;
                whirlView.dir_x = whirlView.get_x(whirlView.dir);
                whirlView.dir_y = whirlView.get_y(whirlView.dir);
            }
        });


    }

    //40x60, змейка 3, корм 1, зеленая змейка, красный корм, 50 штук корма,раз в секунду двигается
    //раз в 5 секунд рандомно поворачивает, доп баллы: +2 балла за управление (3 кнопки, влево и вправо и перезапуск)
    //+ 2 балла за score на экран, + 2 балла первым 5 классным

    @Override
    public void onResume() {
        super.onResume();
        mainThread.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainThread.pause();
    }
}
