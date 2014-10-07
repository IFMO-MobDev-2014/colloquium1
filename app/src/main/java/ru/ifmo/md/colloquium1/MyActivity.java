package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MyActivity extends Activity {
    GameView gameView = null;
    GameEngine engine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        gameView = (GameView) findViewById(R.id.gameView);
        engine = new GameEngine();
        gameView.engine = engine;
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

    @Override
    public void onResume() {
        super.onResume();
        gameView.resume();
        Log.i("Dds", "ds");
    }

    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    public void buttonPressed(View button) {
        if (R.id.button3 == button.getId())
            engine.currentDirection = (engine.currentDirection + 3) % 4;
        else
            engine.currentDirection = (engine.currentDirection + 1) % 4;
//        TextView textView = (TextView) findViewById(R.id.score);
//        textView.setText(engine.score);
    }
}
