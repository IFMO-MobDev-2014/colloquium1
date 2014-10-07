package com.example.vitalik.coloc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by vitalik on 07.10.14.
 */
public class MySurfaceView extends SurfaceView implements Runnable {
    volatile boolean running = false;
    Thread thread = null;
    Paint paint = new Paint();
    private int width = 40;
    private int height = 60;
    private SurfaceHolder holder = getHolder();
    private int pixels[] = new int[40 * 60];
    private int snake[] = null;
    private int snake_length = 3;
    private int score = 0;
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int UP = 2;
    private final int DOWN = 3;
    private boolean game_over = false;
    private int direction = RIGHT;
    Bitmap bitmap = null, scaledBitmap = null;

    public MySurfaceView(Context context) {
        super(context);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        initField();
        paint.setTextSize(40);
        bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.RGB_565 );

    }

    void initField() {
        Random rand = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[y*width + x] = 0xFFFFFF;
            }
        }
        for (int i = 0 ; i < 50; i++) {
			int x = rand.nextInt(40);
			int y = rand.nextInt(60);
			pixels[y * width + x] = 0x00FF00;
		}
		snake = new int[3];
		snake[0] = 30;
		snake[1] = 70;
		snake[2] = 110;
		for (int i = 0; i < snake_length; i++)
			pixels[snake[i]] = 0xFF0000;
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

    @Override
    public void run() {
        int a = 1;
        while (running) {
            if (holder.getSurface().isValid()) {
                a++;
                //long startTime = System.nanoTime();
                Canvas canvas = holder.lockCanvas();
                Update();
                if (a % 3 == 0)
                    changeDirection();
                MyonDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }
	
	public void Update() {
		int newCoor;
		switch (direction) {
			case UP:
				newCoor = snake[snake_length - 1] - width;
                if (newCoor < 0)
				    newCoor += width * 60;
				break;
			case DOWN:
				newCoor = snake[snake_length - 1] + width;
                newCoor %= width * height;
				break;
			case RIGHT:
				newCoor = snake[snake_length - 1] + 1;
				if (newCoor  % width == 0)
					newCoor -= 40;
				break;
			case LEFT:
				newCoor = snake[snake_length - 1] - 1;
				if ((newCoor + 1)  % width == 0)
					newCoor += 40;
				break;
            default:
                newCoor = 0;
		}
		if (pixels[newCoor] == 0x00FF00) {
			int new_snake[] = new int[snake_length + 1];
			for (int i = 0; i < snake_length; i++)
				new_snake[i] = snake[i];
			new_snake[snake_length] = newCoor;
			snake = new_snake;
			snake_length++;
			score++;
		} else if (pixels[newCoor] == 0xFF0000) {
			game_over = true;
		} else {
			pixels[snake[0]] = 0xFFFFFF;
			for (int i = 0; i < snake_length - 1; i++)
				snake[i] = snake[i + 1];
			snake[snake_length - 1] = newCoor;
		}
		pixels[newCoor] = 0xFF0000;
	}
	
	public void changeDirection() {
		Random rand = new Random();
		int a = rand.nextInt(2);
		if (direction == UP || direction == DOWN) {
			if (a == 0)
				direction = LEFT;
			else
				direction = RIGHT;
		} else {
			if (a == 0)
				direction = UP;
			else
				direction = DOWN;
		}
	}
	
    public void MyonDraw(Canvas canvas) {
        if (!game_over) {
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), false);
            canvas.drawBitmap(scaledBitmap, 0, 0, null);
            canvas.drawText("Your Score : " + score, 50, 50, paint);
        } else {
            canvas.drawText("GAME OVER.", 100, 100, paint);
        }
    }

}
