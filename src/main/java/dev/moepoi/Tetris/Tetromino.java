package dev.moepoi.Tetris;

public class Tetromino {
    protected Cell[] cells = new Cell[4];

    public Tetromino() {
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public void moveLeft() {
        for (int i=0; i < cells.length; i++) {
            cells[i].left();
        }
    }

    public void moveRight() {
        for (int i=0; i < cells.length; i++) {
            cells[i].right();
        }
    }

    public void softDrop() {
        for (int i=0; i < cells.length; i++) {
            cells[i].down();
        }
    }

    public static Tetromino randomOne() {
        Tetromino t = null;
        int n = (int) (Math.random() * 7);
        switch(n) {
            case 0: t = new Shape_T(); break;
            case 1: t = new Shape_I(); break;
            case 2: t = new Shape_O(); break;
            case 3: t = new Shape_S(); break;
            case 4: t = new Shape_Z(); break;
            case 5: t = new Shape_L(); break;
            case 6: t = new Shape_J(); break;
        }
        return t;
    }
}
