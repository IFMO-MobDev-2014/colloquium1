package ru.ifmo.md.lesson1;

/**
 * Created by Юлия on 07.10.2014.
 */
public class SnakesChecks {
    int o;
    int t;
    SnakesChecks next;
    SnakesChecks previous;

    SnakesChecks(int r, int e) {
        o = r;
        t = e;
    }

    SnakesChecks(int r, int e, SnakesChecks y) {
        o = r;
        t = e;
        next = y;
    }

    SnakesChecks(int r, int e, SnakesChecks y, SnakesChecks q) {
        o = r;
        t = e;
        next = y;
        previous = q;
    }
}
