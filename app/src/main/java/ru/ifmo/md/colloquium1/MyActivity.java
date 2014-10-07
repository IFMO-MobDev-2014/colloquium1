package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MyActivity extends Activity {
    private SnakeView snakeView;
    MyButton1 myButton1;
    MyButton2 myButton2;
    MyButton3 myButton3;

    class MyButton1 extends Button {
        public MyButton1(Context context) {
            super(context);
            this.setText("turn Left");
            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    snakeView.left();
                }
            });
        }
    }

    class MyButton2 extends Button {
        public MyButton2(Context context) {
            super(context);
            this.setText("turn Right");
            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    snakeView.right();
                }
            });
        }
    }

    class MyButton3 extends Button {
        public MyButton3(Context context) {
            super(context);
            this.setText("Restart");
            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    snakeView.initField();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snakeView = new SnakeView(this);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        myButton1 = new MyButton1(this);
        myButton2 = new MyButton2(this);
        myButton3 = new MyButton3(this);
        ll.addView(myButton1);
        ll.addView(myButton2);
        ll.addView(myButton3);
        ll.addView(snakeView);
        setContentView(ll);

        //setContentView(snakeView);
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
