package dev.moepoi.Tetris;

public class Shape_T extends Tetromino {

    public Shape_T() {
        cells[0] = new Cell(0, 4, Main.T);
        cells[1] = new Cell(0, 3, Main.T);
        cells[2] = new Cell(0, 5, Main.T);
        cells[3] = new Cell(1, 4, Main.T);
    }

}
