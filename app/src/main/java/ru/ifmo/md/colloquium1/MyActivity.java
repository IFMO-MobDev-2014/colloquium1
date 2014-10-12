package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MyActivity extends Activity implements View.OnClickListener {

    Button left;
    Button right;
    ImageView field;
    TextView score;
    public TextView gameOver;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        left = (Button) findViewById(R.id.buttonLeft);
        right = (Button) findViewById(R.id.buttonRight);
        //field = (ImageView) findViewById(R.id.gameScreen);
        score = (TextView) findViewById(R.id.score);
        gameOver = (TextView) findViewById(R.id.gameOver);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        //gameView = new GameView(this);
        gameView = (GameView) findViewById(R.id.gameScreen);
        //gameView.pause();
        //gameView.resume();
        //field.setImageBitmap(gameView.bmap);
        //setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
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

    int curScore = 0;
    boolean isFirstClick = true;

    @Override
    public void onClick(View view) {
        if (isFirstClick) {
            curScore = 0;
            gameOver.setText("");
            score.setText(String.valueOf(curScore));
            isFirstClick = false;
            gameView.start();
        }
        switch (view.getId()) {
            case R.id.buttonLeft:
                gameView.left();
                break;
            case R.id.buttonRight:
                gameView.right();
                break;
        }
        curScore = gameView.score();
        if (gameView.gameOver()) {
            isFirstClick = true;
            gameOver.setText("GAME OVER. Press any key to restart");
        }
        score.setText(String.valueOf(curScore));

    }


}
