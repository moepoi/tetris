package dev.moepoi.Tetris;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    private static final long serialVersionUID = 1L;
    
    public static BufferedImage T;
    public static BufferedImage I;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage Z;
    public static BufferedImage L;
    public static BufferedImage J;

    static {
        try {
            T = ImageIO.read(new FileInputStream ("src/resources/T.png"));
            I = ImageIO.read(new FileInputStream ("src/resources/I.png"));
            O = ImageIO.read(new FileInputStream ("src/resources/O.png"));
            S = ImageIO.read(new FileInputStream ("src/resources/S.png"));
            Z = ImageIO.read(new FileInputStream ("src/resources/Z.png"));
            L = ImageIO.read(new FileInputStream ("src/resources/L.png"));
            J = ImageIO.read(new FileInputStream ("src/resources/J.png"));
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Tetromino currentOne = Tetromino.randomOne();
    private Tetromino nextOne = Tetromino.randomOne();

    private final int row = 20;
    private final int col = 10;

    private Cell[][] wall = new Cell[row][col];
    private static final int CELL_SIZE = 26;

    public void drawWall(Graphics g) {
        for (int i=0; i < row; i++) {
            for (int j=0; j < col; j++) {
                int x = CELL_SIZE * j;
                int y = CELL_SIZE * i;

                Cell cell = wall[i][j];
                if (cell == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(cell.getImage(), x, y, CELL_SIZE, CELL_SIZE, null);
                }
            }
        }
    }

    public void drawCurrentOne(Graphics g) {
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            g.drawImage(cell.getImage(), x, y, CELL_SIZE, CELL_SIZE, null);
        }
    }

    public void drawNextOne(Graphics g) {
        Cell[] cells = nextOne.cells;
        for (Cell cell : cells) {
            int x = cell.getCol() * CELL_SIZE + 260;
            int y = cell.getRow() * CELL_SIZE + 26;
            g.drawImage(cell.getImage(), x, y, CELL_SIZE, CELL_SIZE, null);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawWall(g);
        drawCurrentOne(g);
        drawNextOne(g);
    }

    public boolean outOfBound() {
        Cell[] cells = currentOne.cells;
        for(Cell cell:cells) {
            int cellRow = cell.getRow();
            if (cellRow <= 0 || cellRow >= row-1) {
                return true;
            }
        }
        return false;
    }

    private boolean tooLeft() {
        Cell[] cells = currentOne.cells;
        for(Cell cell:cells) {
            int cellCol = cell.getCol();
            if (cellCol <= 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean tooRight() {
        Cell[] cells = currentOne.cells;
        for(Cell cell:cells) {
            int cellCol = cell.getCol();
            if (cellCol >= col-1) {
                return true;
            }
        }
        return false;
    }

    public boolean coincide() {
        Cell[] cells = currentOne.cells;
        for (Cell cell: cells) {
            int _col = cell.getCol();
            int _row = cell.getRow();
            if (wall[_row][_col] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isDrop() {
        Cell[] cells = currentOne.cells;
        for (Cell cell: cells) {
            int _col = cell.getCol();
            int _row = cell.getRow();
            if (_row == row - 1) {
                return false;
            }
            if (wall[_row + 1][_col] != null) {
                return false;
            }
        }
        return true;
    }

    public void stopDrop() {
        Cell[] cells = currentOne.cells;
        for (Cell cell: cells) {
            int _col = cell.getCol();
            int _row = cell.getRow();
            wall[_row][_col] = cell;
        }
    }

    protected void softDropAction() {
        if (isDrop()) {
            currentOne.softDrop();
        } else {
            stopDrop();
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }

    protected void moveLeftAction() {
        if (!coincide() && !tooLeft() && !outOfBound()) {
            currentOne.moveLeft();
        }
    }

    protected void moveRightAction() {
        if (!coincide() && !tooRight() && !outOfBound()) {
            currentOne.moveRight();
        }
    }

    public void start() {
        KeyListener keylist = new KeyAdapter() {
            public void keyPressed(KeyEvent args0) {
                int key = args0.getKeyCode();
                switch(key) {
                    case KeyEvent.VK_DOWN : {
                        softDropAction();
                        break;
                    }
                    case KeyEvent.VK_LEFT : {
                        moveLeftAction();
                        break;
                    }
                    case KeyEvent.VK_RIGHT : {
                        moveRightAction();
                        break;
                    }
                }
                repaint();
            }
        };

        this.addKeyListener(keylist);
        this.requestFocus();

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    if(isDrop())
                        currentOne.softDrop();
                    else {
                        stopDrop();
                        currentOne = nextOne;
                        nextOne = Tetromino.randomOne();
                    }
                    repaint();
                }
            }
        }.start();
    }

    public static void main( String[] args ) {
        JFrame frame = new JFrame("Tetris");
        frame.setVisible(true);
        frame.setSize(530, 580);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main panel = new Main();
        frame.add(panel);

        panel.start();
    }
}