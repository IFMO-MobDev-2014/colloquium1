package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;


public class MyActivity extends Activity{

    public class SnakeView extends SurfaceView implements SurfaceHolder.Callback{
        //public SurfaceView surfaceView;
        //public SurfaceHolder surfaceHolder;
        public Canvas canvas;
        public DrawThread drawThread;
        SnakeView(Context ctx) {
            super(ctx);
            getHolder().addCallback(this);
            /*this.surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
            this.surfaceHolder = this.surfaceView.getHolder();
            this.canvas = this.surfaceHolder.lockCanvas();
            this.canvas.drawColor(Color.BLUE);*/
            //System.out.println("Canvas: w="+this.canvas.getWidth()+", h="+this.canvas.getHeight());
            /*SurfaceView view = new SurfaceView(this);
            setContentView(view);
            view.getHolder().addCallback(this);*/
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThread = new DrawThread(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) {
            //tryDrawing(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            // завершаем работу потока
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // если не получилось, то будем пытаться еще и еще
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        LinearLayout l = (LinearLayout)findViewById(R.id.layout);
        l.addView(new SnakeView(this));
        //l.addView();
    }
}
