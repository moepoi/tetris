package dev.moepoi.Tetris;

public class Shape_J extends Tetromino {

    public Shape_J() {
        cells[0] = new Cell(0, 4, Main.J);
        cells[1] = new Cell(0, 3, Main.J);
        cells[2] = new Cell(0, 5, Main.J);
        cells[3] = new Cell(1, 5, Main.J);
        states = new State[]{new State(0, 0, 0, 1, 0, -1, -1, -1),
            new State(0, 0, 1, 0, -1, 0, -1, 1),
            new State(0, 0, 0, -1, 0, 1, 1, 1),
            new State(0, 0, -1, 0, 1, 0, 1, -1)};
    }

}