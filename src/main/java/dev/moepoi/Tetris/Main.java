package dev.moepoi.Tetris;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    private static final long serialVersionUID = 1L;
    private int index;
    private int level;
    private int speed;
    private int interval = 10;
    private Timer timer;
    private static int[] scoreTable = {0, 1, 10, 100, 500};
    int state = 0;
    private static final int RUNNING = 0;
    private static final int PAUSE = 1;
    private static final int GAME_OVER = 2;
    private int score;
    private int lines;
    
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

    private void drawLevel(final Graphics g) {
        int x = 291;
        int y = 226 + 56;
        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        int color = 0x667799;
        g.setColor(new Color(color));
        g.drawString("LEVEL:" + level, x, y);
    }

    private void drawLines(final Graphics g) {
        int x = 291;
        int y = 226;
        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        int color = 0x667799;
        g.setColor(new Color(color));
        g.drawString("LINES:" + lines, x, y);
    }

    private void drawScore(final Graphics g) {
        int x = 291;
        int y = 165;
        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        int color = 0x667799;
        g.setColor(new Color(color));
        g.drawString("SCORE:" + score, x, y);
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawWall(g);
        drawCurrentOne(g);
        drawNextOne(g);
        drawLines(g);
        drawScore(g);
        drawLevel(g);
    }

    protected void processPause(int key) {
        switch (key) {
            case KeyEvent.VK_C:
                index = 0;
                state = RUNNING;
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
        }
        repaint();
    }

    protected void processGameOver(int key) {
        switch (key) {
            case KeyEvent.VK_S:
                state = RUNNING;
                index = score = lines = 0;
                wall = new Cell[row][col];
                this.nextOne = Tetromino.randomOne();
                this.currentOne = Tetromino.randomOne();
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
        }
        repaint();
    }

    public boolean outOfBound() {
        final Cell[] cells = this.currentOne.cells;
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int _row = cell.getRow();
            final int _col = cell.getCol();
            if (_col < 0 || _col >= col || _row < 0 || _row >= row) {
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
        if (currentOne == null) {
            return;
        }
        if (isDrop()) {
            currentOne.softDrop();
        } else {
            stopDrop();
            final int lines = destroyLines();
            this.score += scoreTable[lines];
            this.lines += lines;
            if (isGameOver()) {
                // System.out.println("Bye!(T_T)");
                state = GAME_OVER;
            } else {
                currentOne = nextOne;
                nextOne = Tetromino.randomOne();
            }
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

    private void rotateAction() {
        currentOne.rotateRight();
        if (outOfBound() || coincide()) {
            currentOne.rotateLeft();
        }
    }

    private boolean fullCells(final int row) {
        final Cell[] line = wall[row];
        for (int i = 0; i < line.length; i++) {
            final Cell cell = line[i];
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    private void deleteRow(final int row) {
        for (int i = row; i >= 1; i--) {
            System.arraycopy(wall[i - 1], 0, wall[i], 0, col);
        }
        Arrays.fill(wall[0], null);
    }

    private int destroyLines() {
        int lines = 0;
        for (int row = 0; row < row; row++) {
            if (fullCells(row)) {
                deleteRow(row);
                lines++;
            }
        }
        return lines;
    }

    private boolean isGameOver() {
        final Cell[] cells = nextOne.cells;
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int row = cell.getRow();
            final int col = cell.getCol();
            if (wall[row][col] != null) {
                return true;
            }
        }

        return false;
    }

    private void hardDropAction() {
        while (isDrop()) {
            currentOne.softDrop();
        }
        stopDrop();
        final int lines = destroyLines();
        this.score += scoreTable[lines];
        this.lines += lines;
        if (isGameOver()) {
            // System.out.println("Bye!(T_T)");
            state = GAME_OVER;
        } else {
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }

    protected void processRunning(int key) {
        switch (key) {
            case KeyEvent.VK_RIGHT:
                moveRightAction();
                break;
            case KeyEvent.VK_LEFT:
                moveLeftAction();
                break;
            case KeyEvent.VK_DOWN:
                softDropAction();
                break;
            case KeyEvent.VK_SPACE:
                hardDropAction();
                break;
            case KeyEvent.VK_UP:
                rotateAction();
                break;
            case KeyEvent.VK_P:
                state = PAUSE;
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
        }
        repaint();
    }

    public void start() {
        nextOne = Tetromino.randomOne();
        currentOne = Tetromino.randomOne();
        state = RUNNING;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                index++;
                level = lines / 100 + 1;
                speed = 41 - level;
                speed = speed <= 0 ? 1 : speed;
                if (index % speed == 0) {
                    if (state == RUNNING) {
                        softDropAction();
                    }
                }
                repaint();
            }
        };
        timer.schedule(task, 0, interval);

        final KeyListener keylist = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                switch (state) {
                    case RUNNING:
                        processRunning(key);
                        break;
                    case PAUSE:
                        processPause(key);
                        break;
                    case GAME_OVER:
                        processGameOver(key);
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {

            }

            public void keyTyped(KeyEvent e) {

            }
        };

        this.addKeyListener(keylist);
        this.requestFocus(true);
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