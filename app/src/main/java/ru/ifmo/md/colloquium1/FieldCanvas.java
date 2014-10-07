package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by pva701 on 07.10.14.
 */
public class FieldCanvas {
    private SurfaceHolder holder;
    private SurfaceView surfaceView;
    private Rect screenRect;
    public FieldCanvas(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        holder = surfaceView.getHolder();
        screenRect = new Rect(0, 0, surfaceView.getWidth(), surfaceView.getHeight());
    }

    private Bitmap buffer;
    private int prevw, prevh;

    public void draw(int[][] field) {
        int w = field[0].length;
        int h = field.length;
        Bitmap buffer = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int[] pixels = new int[w * h];
        buffer.setPixels(pixels, 0, w, 0, 0, w, h);
        Canvas canvas = holder.lockCanvas();
        canvas.drawBitmap(buffer, null, screenRect, null);
    }
}
