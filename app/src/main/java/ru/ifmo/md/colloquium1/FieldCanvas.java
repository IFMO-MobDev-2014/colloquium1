package ru.ifmo.md.colloquium1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
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
    private Paint paint;

    public FieldCanvas(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        holder = surfaceView.getHolder();
        paint  = new Paint();
    }

    private Bitmap buffer;
    private int prevw, prevh;

    public void draw(int[][] field) {
        screenRect = new Rect(0, 0, surfaceView.getWidth(), surfaceView.getHeight());
        int w = field.length;
        int h = field[0].length;
        Bitmap buffer = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        int[] pixels = new int[w * h];
        for (int i = 0; i < w; ++i)
            for (int j = 0; j < h; ++j)
                pixels[j * w + i] = field[i][j];

        buffer.setPixels(pixels, 0, w, 0, 0, w, h);
        while (!holder.getSurface().isValid());
        Canvas canvas = holder.lockCanvas();
        canvas.drawBitmap(buffer, null, screenRect, null);
        holder.unlockCanvasAndPost(canvas);
    }

    public void drawText(String text) {
        Canvas canvas = holder.lockCanvas();
        paint.setTextSize(50);
        canvas.drawText(text, 100, 100, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    public boolean created() {
        return surfaceView.getWidth() != 0;
    }
}
