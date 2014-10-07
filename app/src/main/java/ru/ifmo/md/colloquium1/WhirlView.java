package snake.serbud.ru.snake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by thevery on 11/09/14.
 */
class WhirlView extends SurfaceView implements Runnable {
    int [][] field_now = new int[40][60];
    int [][] field_new = new int[40][60];
    int [] snake_dir = new int[2400];
    final int width = 40;
    final int height = 60;
    int eaten = 0;
    int snake_size =3;
    int snake_xy[][] = new int[2400][2];
    int [] field_colors = null;
    Bitmap picture;
   // DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
  //  int m1 = displaymetrics.widthPixels;
   // int m2 = displaymetrics.heightPixels;
    final int MAX_COLOR = 3;
   // int[] palette = {0xFFFF0000, 0xFF800000, 0xFF808000, 0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080, 0xFF800080, 0xFFFFFFFF};
    SurfaceHolder holder;
    Thread thread = null;
    volatile boolean running = false;
    float width_to_draw = 1;
    float height_to_draw = 1;
    int scale = 16;

    public WhirlView(Context context) {
        super(context);
        holder = getHolder();
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
        } catch (InterruptedException ignore) {}
    }

    int cl = 0;
    @SuppressLint("WrongCall")
    public void run() {
        while (running) {
            if (holder.getSurface().isValid()) {
                long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                cl++;
                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveSnake();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                long finishTime = System.nanoTime();
                Log.i("TIME", "Circle: " + (finishTime - startTime) / 1000000);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        width_to_draw = (float) w/width;
        height_to_draw = (float) h/height;

        initField();
    }

    void initField() {
        for(int i = 0; i < width;i++)
        {
            for(int  j = 0 ; j < height;j++)
            {
                field_now[i][j] = 0;
            }
        }
       for(int i = 0;i < 50;i++){
           Random r = new Random();
           int x = r.nextInt(40);
           int y = r.nextInt(60);
           field_now[x][y] = 2;
       }
        //right = 1,left = 2,up = 3,down = 4;
        field_now[20][30] = 1;
        snake_dir[0] = 1;
        field_now[21][30] = 1;
        snake_dir[1] = 1;
        field_now[22][30] = 1;
        snake_dir[2] = 1;
        snake_xy[0][0] = 22;
        snake_xy[0][1] = 30;
        snake_xy[1][0] = 21;
        snake_xy[1][1] = 30;
        snake_xy[2][0] = 20;
        snake_xy[2][1] = 30;
        field_colors = new int[width*height];
    }

    int ch=0;
    void moveSnake() {
        ch++;
        field_new = field_now;
        //snake_move
        for(int i = 0; i <snake_size;i++)
        {
            //right
            if(snake_dir[i] == 1){
                int next = 0;
                if(snake_xy[i][0]+1 >= width){
                    next = 1;
                } else {
                    next = snake_xy[i][0]+1;
                }
                if(field_new[next][snake_xy[i][1]] == 2)
                    eaten++;
if(field_new[next][snake_xy[i][1]] == 1)
                    Toast.makeText(this, "Game over",
                    Toast.LENGTH_LONG).show();
                field_new[next][snake_xy[i][1]] = 1;
                field_new[snake_xy[i][0]][snake_xy[i][1]] = 0;
                snake_xy[i][0] = next;
            }
            //left
            if(snake_dir[i] == 2){
                int next = 0;
                if(snake_xy[i][0]-1 < 0){
                    next = width -1;
                } else {
                    next = snake_xy[i][0]-1;
                }
                field_new[next][snake_xy[i][1]] = 1;
                if(field_new[next][snake_xy[i][1]] == 2)
                    eaten++;
if(field_new[next][snake_xy[i][1]] == 1)
                    Toast.makeText(this, "Game over",
                    Toast.LENGTH_LONG).show();
                field_new[snake_xy[i][0]][snake_xy[i][1]] = 0;
                snake_xy[i][0] = next;
            }
            //up
            if(snake_dir[i] == 3){
                int next = 0;
                if(snake_xy[i][1]-1 < 0){
                    next = height -1;
                } else {
                    next = snake_xy[i][1]-1;
                }
                field_new[snake_xy[i][0]][next] = 1;
                if(field_new[snake_xy[i][0]][next] == 2)
                    eaten++;
if(field_new[snake_xy[i][0]][next] == 1)
                    Toast.makeText(this, "Game over",
                    Toast.LENGTH_LONG).show();
                field_new[snake_xy[i][0]][snake_xy[i][1]] = 0;
                snake_xy[i][1] = next;
            }
            //down
            if(snake_dir[i] == 4){
                int next = 0;
                if(snake_xy[i][1]+1 >= height){
                    next = 0;
                } else {
                    next = snake_xy[i][1]+1;
                }
                field_new[snake_xy[i][0]][next] = 1;
                if(field_new[snake_xy[i][0]][next] == 2)
                    eaten++;
if(field_new[snake_xy[i][0]][next] == 1)
                    Toast.makeText(this, "Game over",
                    Toast.LENGTH_LONG).show();
                field_new[snake_xy[i][0]][snake_xy[i][1]] = 0;
                snake_xy[i][1] = next;
            }
        }


        //eaten_add
        if(eaten > 0) {
            int lastX = snake_xy[snake_size - 1][0];
            int lastY = snake_xy[snake_size - 1][1];
            int lastDir = snake_dir[snake_size-1];
            //right
            if(lastDir == 1){
                int next =0;
                if(lastX -1 < 0){
                    next = width-1;
                }else{
                    next = lastX -1;
                }
                snake_size++;
                snake_xy[snake_size-1][0] = next;
                snake_xy[snake_size-1][1] = lastY;
                field_new[next][lastY] = 1;
                snake_dir[snake_size-1] = lastDir;

            }
            //left
            if(lastDir == 2){
                int next =0;
                if(lastX +1 >= width){
                    next = 0;
                }else{
                    next = lastX +1;
                }
                snake_size++;
                snake_xy[snake_size-1][0] = next;
                snake_xy[snake_size-1][1] = lastY;
                field_new[next][lastY] = 1;
                snake_dir[snake_size-1] = lastDir;
            }
            //up
            if(lastDir == 3){
                int next =0;
                if(lastY+1 >=height){
                    next = 0;
                }else{
                    next = lastY +1;
                }
                snake_size++;
                snake_xy[snake_size-1][0] = lastX;
                snake_xy[snake_size-1][1] = next;
                field_new[lastX][next] = 1;
                snake_dir[snake_size-1] = lastDir;
            }
            //down
            if(lastDir == 4){
                int next =0;
                if(lastY-1 < 0){
                    next = height-1;
                }else{
                    next = lastY -1;
                }
                snake_size++;
                snake_xy[snake_size-1][0] = lastX;
                snake_xy[snake_size-1][1] = next;
                field_new[lastX][next] = 1;
                snake_dir[snake_size-1] = lastDir;

            }
            eaten--;
        }

        //dir change
        for(int i = snake_size-1;i >0;i--){
            if(snake_dir[i] != snake_dir[i-1]){
                snake_dir[i] = snake_dir[i-1];
                break;
            }
        }
        if(ch%5==0) {
            Random r = new Random();
            if(snake_dir[0] == 1 || snake_dir[0] == 2){
                snake_dir[0] = 3 + (int)(Math.random() * ((4 - 3) + 1));
                Log.i("Dir", ""+snake_dir[0]);
            }
            else
            {
                snake_dir[0] =  1 + (int)(Math.random() * ((2 - 1) + 1));
                Log.i("Dir", ""+snake_dir[0]);
            }

        }
        field_now =field_new;

    }

    public void leftClick(){

    }

    Paint paint = new Paint();
    @Override
    public void onDraw(Canvas canvas) {
        int colors[][] = new int[width][height];
        for(int i = 0;i <width;i++)
        {
            for(int j = 0; j < height;j++)
            {
                if(field_now[i][j] == 0)
                    colors[i][j] = Color.BLACK;
                if(field_now[i][j] == 1)
                    colors[i][j] = Color.GREEN;
                if(field_now[i][j] == 2)
                    colors[i][j] = Color.RED;
            }
        }
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {

                paint.setColor(colors[x][y]);
                canvas.drawRect(x*scale, y*scale, (x+1)*scale, (y+1)*scale, paint);
            }
        }
       // picture.setPixels(field_colors,0,width,0,0,width,height);
       // canvas.drawBitmap(picture,0,0,null);
    }


}
