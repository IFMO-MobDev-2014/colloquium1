package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ActivitySnake extends SurfaceView implements Runnable{
    public SurfaceHolder holder;

    public ActivitySnake(Context context) {
        super(context);
        holder = getHolder();
    }

    @Override
    public void run() {

    }
}
