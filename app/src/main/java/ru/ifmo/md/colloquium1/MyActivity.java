package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MyActivity extends Activity {
    SnakeView snakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        snakeView = (SnakeView) findViewById(R.id.snakeView);
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

    public void changeDirection(View view) {
        int id = view.getId();
        int direction = snakeView.getDirection();
        switch(id) {
            case R.id.bnright:
                direction--;
                if (direction < 0) {
                    direction += 4;
                }
                break;
            case R.id.bnleft:
                direction++;
                if (direction >= 4) {
                    direction -= 4;
                }
                break;
        }
        snakeView.changeDirection(direction);
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
