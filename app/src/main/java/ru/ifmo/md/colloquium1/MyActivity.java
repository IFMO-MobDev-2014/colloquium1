package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MyActivity extends Activity {
    Snake s;
    public static final int countFood = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snake);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button left = (Button) findViewById(R.id.button);
        Button right = (Button) findViewById(R.id.button2);
        Button restart = (Button) findViewById(R.id.button3);
        final TextView textView = (TextView) findViewById(R.id.textView);
        s = new Snake(imageView, countFood, textView);
        s.execute();
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s.left();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s.right();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //s.cancel(false);
                s = new Snake(imageView, countFood, textView);
                s.execute();
            }
        });
        Log.v("view", "" + (imageView == null));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
