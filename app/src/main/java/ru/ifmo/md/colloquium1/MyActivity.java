package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.widget.ImageView;
import ru.ifmo.md.lesson1.R;


public class MyActivity extends Activity {
    SnakeEngine snake;
    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        view = (ImageView) findViewById(R.id.imageView);
        snake = new SnakeEngine(this);
        snake.restart();
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

    public void onLeftButtonPress(View view) {
        snake.turnLeft();
    }

    public void onResetButtonPress(View view) {
        snake.restart();
    }

    public void onRightButtonPress(View view) {
        snake.turnRight();
=======
import android.view.Menu;
import android.view.MenuItem;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
>>>>>>> c133cb608984e04ad7ddd4672fa8834f383536df
    }
}
