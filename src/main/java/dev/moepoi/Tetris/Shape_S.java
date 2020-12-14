package dev.moepoi.Tetris;

public class Shape_S extends Tetromino {

    public Shape_S() {
        cells[0] = new Cell(0, 4, Main.S);
        cells[1] = new Cell(0, 5, Main.S);
        cells[2] = new Cell(1, 3, Main.S);
        cells[3] = new Cell(1, 4, Main.S);
        states = new State[]{new State(0, 0, 0, 1, 1, -1, 1, 0),
            new State(0, 0, -1, 0, 1, 1, 0, 1)};
    }

}