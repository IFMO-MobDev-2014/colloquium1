package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by anton on 07/10/14.
 */
public class SnakeView extends SurfaceView {
    public static int W = 60;
    public static int H = 40;

    private Bitmap bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.RGB_565);
    private Rect dst;

    public SnakeView(Context context) {
        super(context);
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        dst = new Rect(0, 0, w, h);
    }

    public void setPixels(int[] pixels) {
        bitmap.setPixels(pixels, 0, W, 0, 0, W, H);
    }

    public void redraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, null, dst, null);
    }
}
