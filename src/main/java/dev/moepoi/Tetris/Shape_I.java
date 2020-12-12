package dev.moepoi.Tetris;

public class Shape_I extends Tetromino {

    public Shape_I() {
        cells[0] = new Cell(0, 4, Main.I);
        cells[1] = new Cell(0, 3, Main.I);
        cells[2] = new Cell(0, 5, Main.I);
        cells[3] = new Cell(0, 6, Main.I);
    }

}