package ru.ifmo.md.colloquium1;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.os.SystemClock;
        import android.util.Log;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;

        import java.util.Random;

/**
 * Created by thevery on 11/09/14.
 */

class Zmeika extends SurfaceView implements Runnable {
    int width = 40;
    int height = 60;
    final int red = Color.rgb(255,0,0);
    final int green = Color.rgb(0,255,0);
    final int blue = Color.rgb(0,0,255);
    int zmeikaLength=3;
    int xHead=2;
    int yHead=0;
    boolean direction = false;
    boolean gameOver = false;
    int step = 1;
    int foodNumber=50;
    int seconds=0;
    int[][] field;
    float scaleWidth = 0;
    float scaleHeight = 0;
   // final int MAX_COLOR = 10;
   int[] palette = {0xFFFF0000, 0xFF800000, 0xFF808000, 0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080, 0xFF800080, 0xFFFFFFFF};
    int[] colors = new int[width * height];
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    Paint paint = new Paint();
    Random r  = new Random();
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    //Rect rect;

    public Zmeika(Context context) {
        super(context);
        holder = getHolder();
        initField();
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ignore) {
        }
    }

    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                if (seconds==5){
                   step = r.nextInt(1)==0? -1:1;
                   direction = r.nextInt(1)==0;
                   seconds=0;
                }
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                updateField();
                onDraw(canvas);
                if (gameOver)
                    running =false;
                holder.unlockCanvasAndPost(canvas);
               // SystemClock.sleep(1000);
              //  long finishTime = System.nanoTime();
                //Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ignore) {
                }
                seconds++;
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scaleWidth = ((float) w / width);
        scaleHeight = ((float) h / height);
        if (BuildConfig.DEBUG)
            Log.d("size", w + " x " + h);
        //rect = new Rect(0, 0, (int) (width * scaleWidth), (int) (height * scaleHeight));
    }

    void initField() {
        field = new int[height][width];
        Random rand = new Random();
        int countRedFields =0;
        int x; int y;
        while (countRedFields!=50) {
            do {
                x = rand.nextInt(width - 1);
                y = rand.nextInt(height - 1);
            }while (field[y][x]!=0 && y==0 && x<3);
            field[y][x] =  red;
            colors[y*width+x-1]=palette[field[y][x]];
            countRedFields++;
        }
        for ( x = 0; x < width ; x++) {
            for ( y = 0; y < height; y++) {
                if (field[y][x]!=red) {
                    field[y][x] = blue;
                    colors[y*width+x-1]=palette[field[y][x]];
                }
            }
        }
        for (int i=0; i<3;i++) {
            field[0][i] = green;
            colors[i]=palette[field[0][i]];
        }
    }

    boolean[][] change = new boolean[height][width];

    void updateField() {
       // colors[(y - 1) * width + x - 1] = palette[field[y][x]];
        if (!direction)
        for (int i=0; i<zmeikaLength;i++)
            if (xHead+i<width) {
                if (field[yHead][xHead + i] == red)
                    foodNumber--;
                field[yHead][xHead + i] = green;
                xHead+=zmeikaLength*step;
                field[yHead][xHead-zmeikaLength]=blue;
                colors[yHead*width+xHead-zmeikaLength-1]=palette[field[yHead][xHead-zmeikaLength]];
            }
                else
                    gameOver=true;
        else
            for (int i=0; i<zmeikaLength;i++)
                if (yHead+i<height) {
                    if (field[yHead+i][xHead] == red)
                        foodNumber--;
                    field[yHead+i][xHead] = green;
                    yHead+=zmeikaLength*step;
                    field[yHead-zmeikaLength][xHead]=blue;
                    colors[(yHead-zmeikaLength)*width+xHead-1]=palette[field[yHead-zmeikaLength][xHead]];
                }
                else
                    gameOver=true;
        if (foodNumber ==0)
            gameOver = true;
    }


    @Override
    public void onDraw (Canvas canvas){
        //bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        // canvas.save();
        canvas.scale(scaleWidth,scaleHeight);
        canvas.drawBitmap(colors,0,width,0,0,width,height,true,null);
        // canvas.restore();

    }
}