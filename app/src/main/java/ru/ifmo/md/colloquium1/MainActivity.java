package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnTouchListener {
    FieldSurface surface;
    MenuItem score;

    private void setupButtons() {
        Drawable arrow = getResources().getDrawable(R.drawable.arrow);
        float buttonSide = getResources().getDimension(R.dimen.button_side);
        float scale = buttonSide / arrow.getMinimumWidth();
        ImageView view = (ImageView) findViewById(R.id.buttonLeft);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        matrix.postRotate(180, buttonSide / 2, buttonSide / 2);
        view.setImageMatrix(matrix);
        view.setImageDrawable(arrow);
        view.setOnTouchListener(this);
        matrix.setScale(scale, scale);
        matrix.postRotate(90, buttonSide / 2, buttonSide / 2);
        view = (ImageView) findViewById(R.id.buttonDown);
        view.setImageMatrix(matrix);
        view.setImageDrawable(arrow);
        view.setOnTouchListener(this);
        matrix.setScale(scale, scale);
        matrix.postRotate(-90, buttonSide / 2, buttonSide / 2);
        view = (ImageView) findViewById(R.id.buttonUp);
        view.setImageDrawable(arrow);
        view.setImageMatrix(matrix);
        view.setOnTouchListener(this);
        view = (ImageView) findViewById(R.id.buttonRight);
        view.setImageDrawable(arrow);
        view.setOnTouchListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surface = (FieldSurface) findViewById(R.id.field);
        setupButtons();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            switch (v.getId()) {
                case R.id.buttonLeft:
                    surface.rotate(FieldSurface.MoveDirection.LEFT);
                    break;
                case R.id.buttonRight:
                    surface.rotate(FieldSurface.MoveDirection.RIGHT);
                    break;
                case R.id.buttonDown:
                    surface.rotate(FieldSurface.MoveDirection.DOWN);
                    break;
                case R.id.buttonUp:
                    surface.rotate(FieldSurface.MoveDirection.UP);
                    break;
            }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        surface.resume();
    }

    @Override
    protected void onPause() {
        surface.pause();
        super.onPause();
    }

    public void reportScore(int score) {
        if (this.score != null)
            this.score.setTitle(String.format(getString(R.string.score), score));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        score = menu.findItem(R.id.action_score);
        return true;
    }

    public void reset(boolean gameOver) {
        if (gameOver)
            Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_SHORT).show();
        surface.pause();
        surface.init();
        surface.resume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_restart) {
            reset(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
