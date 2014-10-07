package md.zoidberg.android.colloquium1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class AutomataView extends SurfaceView implements SurfaceHolder.Callback {
    int x, y;
    SnakeThread snakeThread;

    public AutomataView(Context ctx, AttributeSet set) {
        super(ctx, set);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        x = getWidth();
        y = getHeight();

        snakeThread = new SnakeThread(holder, x, y);
        snakeThread.setRunning(true);
        snakeThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            snakeThread.setState(SnakeThread.GameState.RUNNING);

            if (e.getY() < y / 3) snakeThread.setDirection(SnakeThread.Direction.UP);
            else if (e.getY() > (2 * y) / 3) snakeThread.setDirection(SnakeThread.Direction.DOWN);
            else {
                if (e.getX() < x / 2) snakeThread.setDirection(SnakeThread.Direction.LEFT);
                else snakeThread.setDirection(SnakeThread.Direction.RIGHT);
            }
        }

        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        snakeThread.setRunning(false);
        while (retry) {
            try {
                snakeThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // it should finish some time later, I guess
            }
        }
    }
}
