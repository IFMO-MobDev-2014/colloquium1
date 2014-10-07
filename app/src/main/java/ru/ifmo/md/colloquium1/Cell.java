package ru.ifmo.md.colloquium1;

public class Cell {
    int x, y;
    enum Direction {LEFT, RIGHT, UP, DOWN};
    Direction d;
    Cell(int x, int y, Direction d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }
}
