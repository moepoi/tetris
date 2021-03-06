package dev.moepoi.Tetris;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main extends JPanel implements FocusListener {

    private static final long serialVersionUID = 1L;
    private int index;
    private int level;
    private int speed;
    private final int interval = 10;

    private Timer timer;
    private static final int[] scoreTable = {0, 1, 10, 100, 500};

    int state = 0;
    private static final int RUNNING = 0;
    private static final int SPLASH = 1;
    private static final int PAUSE = 2;
    private static final int GAME_OVER = 3;
    private static final int QUIT = 4;

    private int score;
    private int lines;

    private final int row = 20;
    private final int col = 10;

    private Cell[][] wall = new Cell[row][col];
    private static final int CELL_SIZE = 34;

    private Tetromino currentOne;
    private Tetromino nextOne;

    private Clip music;
    private Long musicPosition = 0L;

    private static int THEME = 0;

    public static BufferedImage T;
    public static BufferedImage I;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage Z;
    public static BufferedImage L;
    public static BufferedImage J;
    public static BufferedImage splash;
    public static BufferedImage pause;
    public static BufferedImage gameOver;
    public static BufferedImage quit;
    public static BufferedImage background;
    public static AudioInputStream audio;

    private static final String basePath = "src/main/resources/";

    static {
        try {
            T = ImageIO.read(new FileInputStream(basePath + "klee.png"));
            I = ImageIO.read(new FileInputStream(basePath + "keqing.png"));
            O = ImageIO.read(new FileInputStream(basePath + "jean.png"));
            S = ImageIO.read(new FileInputStream(basePath + "barbara.png"));
            Z = ImageIO.read(new FileInputStream(basePath + "diona.png"));
            L = ImageIO.read(new FileInputStream(basePath + "ningguang.png"));
            J = ImageIO.read(new FileInputStream(basePath + "qiqi.png"));
            splash = ImageIO.read(new FileInputStream(basePath + "splash.png"));
            pause = ImageIO.read(new FileInputStream(basePath + "pause.png"));
            gameOver = ImageIO.read(new FileInputStream(basePath + "game-over.png"));
            quit = ImageIO.read(new FileInputStream(basePath + "quit.png"));
            background = ImageIO.read(new FileInputStream(basePath + "background-dark.png"));
            audio = AudioSystem.getAudioInputStream(new File(basePath + "audio.wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Main() {
        addFocusListener(this);
    }

    private void drawState(Graphics g) {
        switch (state) {
            case SPLASH:
                g.drawImage(splash, -15, -15, null);
                break;
            case GAME_OVER:
                g.drawImage(gameOver, -15, -15, null);
                break;
            case PAUSE:
                g.drawImage(pause, -15, -15, null);
                break;
            case QUIT:
                g.drawImage(quit, -15, -15, null);
                break;
        }

    }

    public void drawWall(Graphics g) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
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
            g.drawImage(cell.getImage(), x, y, CELL_SIZE + 3, CELL_SIZE + 3, null);
        }
    }

    private void drawLevel(final Graphics g) {
        int x = 545;
        int y = 196 - 70;
        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        int color = 0x667799;
        g.setColor(new Color(color));
        g.drawString("LEVEL:" + level, x, y);
    }

    private void drawLines(final Graphics g) {
        int x = 545;
        int y = 155 - 70;
        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        int color = 0x667799;
        g.setColor(new Color(color));
        g.drawString("LINES:" + lines, x, y);
    }

    private void drawScore(final Graphics g) {
        int x = 545;
        int y = 115 - 70;
        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        int color = 0x667799;
        g.setColor(new Color(color));
        g.drawString("SCORE:" + score, x, y);
    }

    private void drawGameOver(final Graphics g) {
        int x = 175;
        int y = 470;
        Font f = new Font(Font.SERIF, Font.BOLD, 40);
        g.setFont(f);
        g.setColor(Color.decode("#49371B"));
        g.drawString("SCORE : " + score, x, y);
        g.drawString("LEVEL : " + level, x + 250, y);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (state != SPLASH) {
            g.drawImage(background, 0, 0, null);
            g.setColor(Color.decode("#1B1D8F"));
            drawWall(g);
            drawCurrentOne(g);
            drawNextOne(g);
            drawLines(g);
            drawScore(g);
            drawLevel(g);
            drawState(g);
            if (state == GAME_OVER) {
                drawGameOver(g);
            }
        } else {
            drawState(g);
        }
    }

    protected void processSplash() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    protected void processPause(int key) {
        switch (key) {
            case KeyEvent.VK_R:
                index = 0;
                state = RUNNING;
                resumeMusic();
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
                playMusic();
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
        }
        repaint();
    }

    protected void processQuit(int key) {
        switch (key) {
            case KeyEvent.VK_N:
                index = 0;
                if (state == GAME_OVER) {
                    score = lines = index;
                    wall = new Cell[row][col];
                    this.nextOne = Tetromino.randomOne();
                    this.currentOne = Tetromino.randomOne();
                }
                state = RUNNING;
                resumeMusic();
                break;
            case KeyEvent.VK_Y:
                System.exit(0);
                break;
        }
        repaint();
    }

    private boolean outOfBound() {
        if (this.currentOne == null) {
            return true;
        }
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

    public boolean coincide() {
        if (this.currentOne == null) {
            return false;
        }
        final Cell[] cells = this.currentOne.cells;
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int _row = cell.getRow();
            final int _col = cell.getCol();
            if (wall[_row][_col] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isDrop() {
        final Cell[] cells = currentOne.cells;
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int _row = cell.getRow();
            if (_row == row - 1) {
                return false;
            }
        }
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int _row = cell.getRow();
            final int _col = cell.getCol();
            if (wall[_row + 1][_col] != null) {
                return false;
            }
        }
        return true;
    }

    public void stopDrop() {
        final Cell[] cells = currentOne.cells;
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int _row = cell.getRow();
            final int _col = cell.getCol();
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
                state = GAME_OVER;
                stopMusic();
            } else {
                currentOne = nextOne;
                nextOne = Tetromino.randomOne();
            }
        }
    }

    protected void moveLeftAction() {
        if (currentOne == null) {
            return;
        }
        currentOne.moveLeft();
        if (outOfBound() || coincide()) {
            currentOne.moveRight();
        }
    }

    protected void moveRightAction() {
        if (currentOne == null) {
            return;
        }
        currentOne.moveRight();
        if (outOfBound() || coincide()) {
            currentOne.moveLeft();
        }
    }

    private void rotateAction() {
        currentOne.rotateRight();
        if (outOfBound() || coincide()) {
            currentOne.rotateLeft();
        }
    }

    private boolean fullCells(final int _row) {
        final Cell[] line = wall[_row];
        for (int i = 0; i < line.length; i++) {
            final Cell cell = line[i];
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    private void deleteRow(final int _row) {
        for (int i = _row; i >= 1; i--) {
            System.arraycopy(wall[i - 1], 0, wall[i], 0, col);
        }
        Arrays.fill(wall[0], null);
    }

    private int destroyLines() {
        int lines = 0;
        for (int _row = 0; _row < row; _row++) {
            if (fullCells(_row)) {
                deleteRow(_row);
                lines++;
            }
        }
        return lines;
    }

    private boolean isGameOver() {
        final Cell[] cells = nextOne.cells;
        for (int i = 0; i < cells.length; i++) {
            final Cell cell = cells[i];
            final int _row = cell.getRow();
            final int _col = cell.getCol();
            if (wall[_row][_col] != null) {
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
            state = GAME_OVER;
        } else {
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }

    private void playMusic() {
        music.setMicrosecondPosition(0L);
        music.loop(-1);
    }

    private void pauseMusic() {
        musicPosition = music.getMicrosecondPosition();
        music.stop();
    }

    private void resumeMusic() {
        music.setMicrosecondPosition(musicPosition);
        music.loop(-1);
    }

    private void stopMusic() {
        music.stop();
    }

    private static void switchTheme() {
        try {
            if (THEME == 0) {
                background = ImageIO.read(new FileInputStream(basePath + "background-white.png"));
                THEME = 1;
            } else {
                background = ImageIO.read(new FileInputStream(basePath + "background-dark.png"));
                THEME = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                pauseMusic();
                break;
            case KeyEvent.VK_Q:
                state = QUIT;
                pauseMusic();
                break;
            case KeyEvent.VK_S:
                processGameOver(KeyEvent.VK_S);
                break;
            case KeyEvent.VK_X:
                switchTheme();
                break;
        }
        repaint();
    }

    public void start() {
        state = SPLASH;
        processSplash();
        try {
            music = AudioSystem.getClip();
            music.open(audio);
            playMusic();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
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
                    case QUIT:
                        processQuit(key);
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

    @Override
    public void focusGained(FocusEvent fe) {
        this.requestFocusInWindow();
    }

    @Override
    public void focusLost(FocusEvent fe) {
        if (state == RUNNING) {
            pauseMusic();
            state = PAUSE;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        ImageIcon icon = new ImageIcon(basePath + "barbara.png");
        frame.setIconImage(icon.getImage());
        frame.setVisible(true);
        frame.setSize(800, 720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main panel = new Main();
        frame.add(panel);

        panel.start();
    }
}
