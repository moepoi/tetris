package dev.moepoi.Tetris;

public class Tetromino {
    protected Cell[] cells = new Cell[4];
    protected State[] states;
    protected int index = 10000;

    public Tetromino() {
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public void moveLeft() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].left();
        }
    }

    public void moveRight() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].right();
        }
    }

    public void softDrop() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].down();
        }
    }

    public void rotateRight() {
        index++;
        int n = index % states.length;
        State s = states[n];
        Cell o = cells[0];
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row + s.row1);
        cells[1].setCol(col + s.col1);
        cells[2].setRow(row + s.row2);
        cells[2].setCol(col + s.col2);
        cells[3].setRow(row + s.row3);
        cells[3].setCol(col + s.col3);
    }

    public void rotateLeft() {
        index--;
        int n = index % states.length;
        State s = states[n % 4];
        Cell o = cells[0];
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row + s.row1);
        cells[1].setCol(col + s.col1);
        cells[2].setRow(row + s.row2);
        cells[2].setCol(col + s.col2);
        cells[3].setRow(row + s.row3);
        cells[3].setCol(col + s.col3);
    }

    public static Tetromino randomOne() {
        Tetromino t = null;
        int n = (int) (Math.random() * 7);
        switch (n) {
            case 0:
                t = new Shape_T();
                break;
            case 1:
                t = new Shape_I();
                break;
            case 2:
                t = new Shape_O();
                break;
            case 3:
                t = new Shape_S();
                break;
            case 4:
                t = new Shape_Z();
                break;
            case 5:
                t = new Shape_L();
                break;
            case 6:
                t = new Shape_J();
                break;
        }
        return t;
    }
}
