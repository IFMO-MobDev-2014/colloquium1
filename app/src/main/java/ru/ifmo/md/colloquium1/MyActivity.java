package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;


public class MyActivity extends Activity {
    SnakeRun snakeRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);
        //Display display = getWindowManager().getDefaultDisplay();
        snakeRun = new SnakeRun(this);
        setContentView(snakeRun);
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeRun.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeRun.onPause();
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
