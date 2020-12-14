package dev.moepoi.Tetris;

public class Shape_L extends Tetromino {

    public Shape_L() {
        cells[0] = new Cell(0, 4, Main.L);
        cells[1] = new Cell(0, 3, Main.L);
        cells[2] = new Cell(0, 5, Main.L);
        cells[3] = new Cell(1, 3, Main.L);
        states = new State[]{new State(0, 0, 0, 1, 0, -1, -1, 1),
            new State(0, 0, 1, 0, -1, 0, 1, 1),
            new State(0, 0, 0, -1, 0, 1, 1, -1),
            new State(0, 0, -1, 0, 1, 0, -1, -1)};
    }

}