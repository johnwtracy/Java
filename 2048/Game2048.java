
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * *
 * Basic 2048 game.
 *
 * @author John Tracy
 */
public class Game2048 extends JFrame {

    /*
     Instance variables
     */
    private int WIDTH;
    private int HEIGHT;
    private boolean isRunning;
    public static boolean hasStarted;
    private Handler keys;
    private Board game;
    private Block[][] blocks;
    private Score score;

    private Block block;

    /**
     * The main method that runs the program
     *
     * @param args
     */
    public static void main(String[] args) {
        Game2048 game = new Game2048();
        game.run();
        System.exit(0);
    }

    /**
     * Initializes the instance variables
     */
    public void initialize() {
        hasStarted = false;
        isRunning = true;
        score = new Score();
        game = new Board(score);
        keys = new Handler(game);
        HEIGHT = 600;
        WIDTH = 600;
        block = new Block(0, 0, score);

        /*
         Window basics
         **DO NOT CHANGE**
         */
        setTitle("2048");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        addKeyListener(keys);
    }

    /**
     * Runs an infinite loop with a sleep time
     */
    public void run() {
        initialize();
        while (isRunning) {
            update();
            draw();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Updates variables that the the draw method uses
     */
    public void update() {
    }

    /**
     * Uses Graphics class to draw
     */
    public void draw() {
        Graphics2D g = (Graphics2D) this.getGraphics();
        g.drawImage(drawAsImage(), 0, 0, this);
    }

    public Image drawAsImage() {
        Image i = createImage(WIDTH, HEIGHT);
        Graphics2D g = (Graphics2D) i.getGraphics();
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, WIDTH, HEIGHT);
        int x = (int) ((WIDTH - 600) * .5);
        int y = (int) ((HEIGHT - 600) * .5);
        if (!hasStarted) {
            g.drawImage((new ImageIcon("2048\\HomeScreen.png")).getImage(), x, y, this);
        } else {
            if (game.canMove()) {
                g.setColor(new Color(140, 140, 140));
                blocks = game.getBlocks();
                int startX = (int) ((WIDTH - Board.COLUMNS * 120) * .5);
                int startY = (int) ((HEIGHT - Board.ROWS * 120) * .5);
                for (int r = 0; r < Board.ROWS; r++) {
                    for (int c = 0; c < Board.COLUMNS; c++) {
                        if (blocks[r][c] != null) {
                            //drawNumber(blocks[r][c], startX + 80 * c, startY + 80 * r, g);
                            blocks[r][c].draw(g, startX + 120 * c, startY + 120 * r);
                        } else {
                            g.fillRect(startX + 120 * c, startY + 120 * r, 120, 120);
                        }
                    }
                }
                g.setColor(Color.RED);
                g.drawString("Score:  " + score.getScore() + "", 20, 40);
            } else {
                ImageIcon ii = new ImageIcon("2048\\GameOver2048.png");
                g.drawImage(ii.getImage(), x, y, this);
            }
        }
        return i;
    }

    public void drawNumber(Block b, int x, int y, Graphics2D g) {
        g.drawString(b.getValue() + "", x, y);
    }

}
