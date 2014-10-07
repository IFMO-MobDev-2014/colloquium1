package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Random;


public class MyActivity extends Activity {
    int[] pixels;
    ArrayList<Pair<Integer, Integer>> snake;
    int direction;
    int width;
    int height;
    Bitmap bitmap;
    Bitmap scaled;
    Rect rect;
    Canvas canvas;
    ChangeableView imageView;
    int w;
    int h;
    boolean collision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        imageView = (ChangeableView) findViewById(R.id.image);
    }

    public void leftClick() {
        imageView.setDirection((imageView.direction() - 1) % 4);
    }

    public void rightClick() {
        imageView.setDirection((imageView.direction() + 1) % 4);
    }

    public void restart() {
        imageView.restart();
    }
}
