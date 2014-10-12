package ru.ifmo.md.colloquium1;

/**
 * Created by pokrasko on 07.10.14.
 */
public class IntPair {
    private int first;
    private int second;

    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public IntPair(IntPair other) {
        this.first = other.first();
        this.second = other.second();
    }

    public int first() {
        return first;
    }

    public int second() {
        return second;
    }
    
    public static IntPair add(IntPair first, IntPair second, int module1, int module2) {
        int r1 = first.first() + second.first();
        if (r1 < 0) {
            r1 = module1 - 1;
        } else if (r1 == module1) {
            r1 = 0;
        }
        int r2 = first.second() + second.second();
        if (r2 < 0) {
            r2 = module2 - 1;
        } else if (r2 == module2) {
            r2 = 0;
        }
        return new IntPair(r1, r2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        IntPair other = (IntPair) o;
        if (this.first() != other.first())
            return false;
        if (this.second() != other.second())
            return false;
        return true;
    }
}
