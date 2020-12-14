package dev.moepoi.Tetris;

public class Shape_Z extends Tetromino {

    public Shape_Z() {
        cells[0] = new Cell(1, 4, Main.Z);
        cells[1] = new Cell(0, 3, Main.Z);
        cells[2] = new Cell(0, 4, Main.Z);
        cells[3] = new Cell(1, 5, Main.Z);
        states = new State[]{new State(0, 0, -1, -1, -1, 0, 0, 1),
            new State(0, 0, -1, 1, 0, 1, 1, 0)};
    }

}