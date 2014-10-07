package ru.ifmo.md.colloquium1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;


public class MyActivity extends Activity {

    private static final int INIT_TURN = 0;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;
    private static final int INIT_LENGTH = 3;
    private static final int INIT_FOOD_COUNT = 50;

    private static final int UP = 0;
    private static final int RIGHT = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private static final int DIR = 4;

    private static final int FREE = 0xFFFFFF;
    private static final int SNAIL = 0xFF00FF;
    private static final int FOOD = 0xFF0000;

    int buttonHeight;

    class Pair {
        int x, y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Field extends View {
        int[] map;
        int width, height;
        Random rnd = new Random();

        long startTime = 0;
        int frames = 0;

        public Field(Context context) {
            super(context);
            initField();
        }

        public Pair findFree() {
            int x, y;
            do {
                x = rnd.nextInt(WIDTH);
                y = rnd.nextInt(HEIGHT);
            } while (map[x * WIDTH + y] != FREE);
            return new Pair(x, y);
        }

        public void initField() {
            Display display = getWindowManager().getDefaultDisplay();

            Point p = new Point();
            display.getSize(p);

            width = p.x;
            height = p.y - 3 * buttonHeight;

            map = new int[WIDTH * HEIGHT];

            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    map[i * WIDTH + j] = FREE;
                }
            }
            Pair free = findFree();
            snail = new Snail(free.x, free.y);
            snail.drawOnField();

            for (int i = 0; i < INIT_FOOD_COUNT; i++) {
                free = findFree();
                drawFood(free);
            }
        }

        public void drawHead(Pair a) {
            map[a.x * WIDTH + a.y] = SNAIL;
        }

        public void drawFood(Pair a) {
            map[a.x * WIDTH + a.y] = FOOD;
        }

        public void drawFree(Pair a) {
            map[a.x * WIDTH + a.y] = FREE;
        }

        public void checkCollision() {
            if (map[snail.x[snail.head] * WIDTH + snail.y[snail.head]] == FOOD) {
                snail.increaseLength();
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }

            snail.updateCeil();
            field.checkCollision();

            canvas.drawBitmap(map, 0, width, 0, 0, width, height, false, null);

            long currentTime = System.currentTimeMillis();
            frames++;
            int fps = (int)(1000 * frames / (currentTime - startTime + 1));

            Paint paint = new Paint();
            paint.setColor(0xFF00FF);
            paint.setTextSize(50);
            canvas.drawText("fps: " + fps, 30, 100, paint);

            invalidate();
        }
    }


    class Snail {
        int turn;
        int length;
        int head, tail;
        int[] x, y;
        int n;

        public Snail(int x, int y) {
            this.turn = INIT_TURN;
            this.length = INIT_LENGTH;
            this.n = INIT_FOOD_COUNT + INIT_LENGTH;
            this.x = new int[n];
            this.y = new int[n];
            this.head = 0;
            this.tail = 3;
            for (int i = 0; i < INIT_LENGTH; i++) {
                this.x[0] = (x + i) % HEIGHT;
                this.y[0] = (y + i) % HEIGHT;
            }
        }

        public void increaseLength() {
            tail = (tail + 1) % n;
            length++;
        }

        public int getTurn() {
            if (x[0] > x[1]) {
                return RIGHT;
            } else if (x[0] < x[1]) {
                return LEFT;
            } else if (y[0] < y[1]) {
                return UP;
            } else {
                return DOWN;
            }
        }

        public void updateCeil() {
            int lh = head;
            head = (head - 1 + n) % n;
            tail = (tail - 1 + n) % n;
            turn = getTurn();
            x[head] = x[lh];
            y[head] = y[lh];
            if (turn == UP) {
                x[head] = (x[lh] - 1 + WIDTH) % WIDTH;
            } else if (turn == DOWN) {
                x[head] = (x[lh] + 1) % WIDTH;
            } else if (turn == LEFT) {
                y[head] = (y[lh] - 1 + HEIGHT) % HEIGHT;
            } else if (turn == RIGHT) {
                y[head] = (y[lh] + 1) % HEIGHT;
            }
        }

        public void drawOnField() {
            field.drawFree(new Pair(x[tail], y[tail]));
            field.drawHead(new Pair(x[head], y[head]));
        }

        public void turnLeft() {
            turn = getTurn() - 1;
            turn = (turn + DIR) % DIR;
        }

        public void turnRight() {
            turn = getTurn() + 1;
            turn = turn % DIR;
        }
    }

    class LeftButton extends Button {
        public LeftButton(Context context) {
            super(context);
            this.setText("Turn left");
            buttonHeight = 40;
            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    snail.turnLeft();
                }
            });
        }
    }

    class RightButton extends Button {
        public RightButton(Context context) {
            super(context);
            this.setText("Turn left");
            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    snail.turnRight();
                }
            });
        }
    }

    class Reset extends Button {
        public Reset (Context context) {
            super(context);
            this.setText("Reset");
            this.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    field.initField();
                }
            });
        }
    }

    Field field;
    Snail snail;
    LeftButton leftT;
    RightButton rightT;
    Reset reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        leftT = new LeftButton(this);
        rightT = new RightButton(this);
        reset = new Reset(this);
        field = new Field(this);
        ll.addView(leftT);
        ll.addView(rightT);
        ll.addView(reset);
        ll.addView(field);
        setContentView(ll);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
