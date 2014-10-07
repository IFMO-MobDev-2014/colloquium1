package ru.ifmo.md.colloquium1;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class Food {
    private static final int FRUITS_COUNT = 50;

    public static final class Fruit implements Comparable<Fruit> {
        private final int x;
        private final int y;

        private Fruit(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int compareTo(Fruit another) {
            int res = Integer.compare(x, another.x);
            if (res == 0) return Integer.compare(y, another.y);
            return res;
        }
    }

    private final Set<Fruit> fruits = new ConcurrentSkipListSet<Fruit>();

    public Food() {
        generateFruits();
    }

    private void generateFruits() {
        Random rnd = new Random();
        while (fruits.size() < FRUITS_COUNT) {
            int x = rnd.nextInt(Snake.WIDTH);
            int y = rnd.nextInt(Snake.HEIGHT);

            Fruit fruit = new Fruit(x, y);
            if (!fruits.contains(fruit)) fruits.add(fruit);
        }
    }

    public void eaten(Fruit fruit) {
        fruits.remove(fruit);
    }

    public Set<Fruit> getFruits() {
        return fruits;
    }
}
