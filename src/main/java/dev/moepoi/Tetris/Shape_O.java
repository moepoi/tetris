package dev.moepoi.Tetris;

public class Shape_O extends Tetromino {

    public Shape_O() {
        cells[0] = new Cell(0, 4, Main.O);
        cells[1] = new Cell(0, 5, Main.O);
        cells[2] = new Cell(1, 4, Main.O);
        cells[3] = new Cell(1, 5, Main.O);
        states = new State[]{new State(0, 0, 0, 1, 1, 0, 1, 1)};
    }

}