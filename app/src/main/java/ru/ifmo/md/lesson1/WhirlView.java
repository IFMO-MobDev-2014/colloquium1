package ru.ifmo.md.lesson1;

    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;
    import android.os.AsyncTask;
    import android.util.AttributeSet;
    import android.view.SurfaceHolder;
    import android.view.SurfaceView;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Random;


    /**
     * Created by thevery on 11/09/14.
     */
    public class WhirlView extends SurfaceView {

        private final int width = 40;
        private final int height = 60;
        boolean [][] field;
        int [] colors;
        float scale_x = 1;
        float scale_y = 1;
        SurfaceHolder holder;
        ArrayList<Point> snake;
        int dir = 1;
        int dir_x = 1;
        int dir_y = 0;
        int score = 0;
        boolean wasted = false;
        TextView textView;

    private class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equal(Point p) {
            return (p.x == x) && (p.y == y);
        }
    }

    private class Toaster extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private Paint paint = new Paint();

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public WhirlView(Context context) {
        super(context);
        holder = getHolder();
    }

    public WhirlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
    }

    public WhirlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        holder = getHolder();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        scale_x = (float) w / width;
        scale_y = (float) h / height;
        colors = new int[width * height];
        initField();
    }

    void initField() {
        field = new boolean[width][height];
        Random rand = new Random();
        int x, y;
        for (int i = 0; i < 50; i++) {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
            while (field[x][y]) {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            }
            field[x][y] = true;
        }
        snake = new ArrayList<Point>();
        snake.add(new Point(1, 1));
        snake.add(new Point(2, 1));
        snake.add(new Point(3, 1));
        score = 0;
        textView.post(new Runnable() {
            public void run() {
                textView.setText("Score: 0");
            }
        });
        wasted = false;
    }

    Point perform(Point p) {
        return new Point((p.x + dir_x + width) % width, (p.y + dir_y + height) % height);
    }

    public int get_x(int d) {
        switch (d) {
            case 1: return 1;
            case 2: return 0;
            case 3: return -1;
            default: return 0;
        }
    }

    public int get_y(int d) {
        switch (d) {
            case 1: return 0;
            case 2: return 1;
            case 3: return 0;
            default: return -1;
        }
    }

    void updateField() {
        if (!wasted) {
            Point p = perform(snake.get(snake.size() - 1));
            if (field[p.x][p.y]) {
                field[p.x][p.y] = false;
                snake.add(p);
                score++;
                if (score == 47)
                    new Toaster().execute("Winner!!!");

                textView.post(new Runnable() {
                    public void run() {
                        textView.setText("Score: ".concat(Integer.toString(score)));
                    }
                });
            } else {
                for (int i = 0; i < snake.size() - 1; i++) {
                    snake.set(i, snake.get(i + 1));
                }
                Point pp = perform(snake.get(snake.size() - 1));
                for (int i = 0; i < snake.size(); i++) {
                    if (pp.equal(snake.get(i))) {
                        wasted = true;
                        new Toaster().execute("Wasted");
                    }
                }
                snake.set(snake.size() - 1, pp);
            }
        }
    }

    public void render(Canvas canvas) {

        Arrays.fill(colors, Color.WHITE);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (field[x][y])
                    colors[x + y * width] = Color.RED;
            }
        }

        for (Point p: snake)
            colors[p.x + p.y * width] = Color.GREEN;

        canvas.save();
        canvas.scale(scale_x, scale_y);
        canvas.drawBitmap(colors, 0, width, 0, 0, width, height, false, paint);
        canvas.restore();
    }

    @Override
    public void onDraw(Canvas canvas) {
        render(canvas);
    }

}
