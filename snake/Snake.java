package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * The classic arcade game snake remade
 *
 * @author John
 */
public class Snake extends JFrame {

    private boolean isRunning;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean hasStarted;
    private boolean isOver;
    private boolean restart;
    private boolean oneKey;
    private boolean redChange;
    private boolean blueChange;
    private boolean greenChange;
    private boolean firstFrame;
    public int gridX;
    public int gridY;
    public int gridSideLength;
    private int add;
    private int difficulty = 100;
    private int change;
    private final int windowWidth = 600;
    private final int windowHeight = 700;
    private int[] x;
    private int[] y;
    private int blocks;
    private int targetX;
    private int targetY;
    private int sections2;
    private ImageIcon startScreen;
    private ImageIcon gameOverScreen;
    private Image start;
    private Image over;
    private int[] topThree;
    private int red;
    private int green;
    private int blue;

    /**
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        Snake game = new Snake();
        game.run();
        System.exit(0);
    }

    /**
     * method which runs the game in a loop
     *
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    @SuppressWarnings("SleepWhileInLoop")
    public void run() throws InterruptedException, IOException {
        setSize(windowWidth, windowHeight);
        initialize();
        while (isRunning) {
            if (restart) {
                initialize();
            }
            draw();
            update();
            Thread.sleep(difficulty);
            oneKey = false;
        }
    }

    /**
     * Initializes the variables as a constructor
     *
     * @throws IOException
     */
    public void initialize() throws IOException {
        isRunning = true;
        up = true;
        down = false;
        left = false;
        right = false;
        hasStarted = false;
        isOver = false;
        restart = false;
        oneKey = false;
        redChange = true;
        blueChange = true;
        greenChange = true;
        firstFrame = true;
        change = 20;
        sections2 = 20;
        gridSideLength = sections2 * change;
        x = new int[1000];
        y = new int[1000];
        add = (int) Math.round(sections2 / 6);
        x[0] = this.getWidth() / 2;
        y[0] = this.getHeight() / 2;
        targetX = (this.getWidth() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
        targetY = (this.getHeight() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
        for (int i = 0; i < blocks; i++) {
            if (targetX == x[i] && targetY == y[i]) {
                targetX = (this.getWidth() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
                targetY = (this.getHeight() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
            }
        }
        blocks = 3;
        startScreen = new ImageIcon("images\\SnakeHome.png");
        start = startScreen.getImage();
        gameOverScreen = new ImageIcon("images\\GameOver.png");
        over = gameOverScreen.getImage();
        topThree = new int[3];
        red = (int) (Math.random() * 256);
        green = (int) (Math.random() * 256);
        blue = (int) (Math.random() * 256);
        setTitle("Snake");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addKeyListener(new KeyHandler());
    }

    /**
     * This method will check for input, move things around and check for win
     * conditions, etc
     *
     * @throws java.io.IOException
     */
    public void update() throws IOException {
        if (hasStarted && !isOver) {
            for (int z = blocks; z > 0; z--) {
                x[z] = x[(z - 1)];
                y[z] = y[(z - 1)];
            }
            if (up)
                y[0] -= change;
            if (down)
                y[0] += change;
            if (left)
                x[0] -= change;
            if (right)
                x[0] += change;

            targetCheck();
            checkCollision();
            if (red >= 253)
                redChange = false;
            else if (red <= 3)
                redChange = true;
            if (green >= 253)
                greenChange = false;
            else if (green <= 3)
                greenChange = true;
            if (blue >= 253)
                blueChange = false;
            else if (blue <= 3)
                blueChange = true;
            if (redChange)
                red += 3;
            else
                red -= 3;
            if (blueChange)
                blue += 3;
            else
                blue -= 3;
            if (greenChange)
                green += 3;
            else
                green -= 3;
        }
    }

    /**
     * Checks if the player has hit the target block
     */
    public void targetCheck() {
        if (Math.abs(x[0] - targetX) < change && Math.abs(y[0] - targetY) < change) {
            targetX = (this.getWidth() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
            targetY = (this.getHeight() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
            blocks += add;
            for (int i = 0; i < blocks; i++) {
                if (targetX == x[i] && targetY == y[i]) {
                    targetX = (this.getWidth() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
                    targetY = (this.getHeight() - (change * sections2)) / 2 + ((int) (Math.random() * (sections2 - 2)) + 1) * change;
                }
            }
        }
    }

    /**
     * Checks if the player has gone out of bounds or hit themselves
     *
     * @throws IOException
     */
    private void checkCollision() throws IOException {
        for (int z = blocks; z > 0; z--) {
            if ((x[0] == x[z]) && (y[0] == y[z])) {
                isOver = true;
                hasStarted = false;
            }
        }
        if (y[0] + change > gridY + gridSideLength) {
            isOver = true;
            hasStarted = false;
        }
        if (y[0] < gridY) {
            isOver = true;
            hasStarted = false;
        }
        if (x[0] + change > gridX + gridSideLength) {
            isOver = true;
            hasStarted = false;
        }
        if (x[0] < gridX) {
            isOver = true;
            hasStarted = false;
        }
        if (isOver) {
            printHighScores();
        }
    }

    /**
     * Prints the high scores and updates the file
     *
     * @throws IOException
     */
    @SuppressWarnings("ConvertToTryWithResources")
    public void printHighScores() throws IOException {
        Scanner readHighScores = new Scanner(new File("highscores.txt"));
        int[] scores = new int[topThree.length + 2];
        int i = 0;
        while (readHighScores.hasNext()) {
            scores[i] = readHighScores.nextInt();
            i++;
        }
        scores[i + 1] = blocks - 3;
        Arrays.sort(scores);
        PrintWriter highScore = new PrintWriter(new File("highscores.txt"));
        int j = 0;
        while (j < 3) {
            highScore.write(scores[ (scores.length - 1) - j] + "  ");
            j++;
        }
        highScore.close();
        for (int k = 0; k < topThree.length; k++) {
            topThree[k] = scores[ (scores.length - 1) - k];
        }
    }

    /**
     * Draws as an image due to better mor smooth graphics
     *
     * @return the image of the board and the snake
     */
    public Image drawAsImage() {
        Image screen = new BufferedImage(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        Graphics2D g = (Graphics2D) screen.getGraphics();
        int startX = (this.getWidth() - start.getWidth(null)) / 2;
        int startY = (this.getHeight() - start.getHeight(null)) / 2;
        int overX = (this.getWidth() - over.getWidth(null)) / 2;
        int overY = (this.getHeight() - over.getHeight(null)) / 2;
        g.setBackground(new Color(red, green, blue));
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (!hasStarted) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(start, startX, startY, null);
        }
        if (hasStarted) {
            drawGrid(g, change, sections2);
            drawBounds(g);
            for (int j = 1; j < blocks; j++) {
                g.setColor(Color.WHITE);
                g.fillRect(x[j], y[j], change, change);
            }
            g.setColor(new Color(128, 128, 128));
            g.fillRect(x[0], y[0], change, change);
            g.setColor(new Color((int) ((255 - red) * .75) + 30, (int) ((255 - green) * .75) + 30, (int) ((255 - blue) * .75) + 30));
            g.fillRect(targetX, targetY, change, change);
            g.setColor(new Color(255 - red, 255 - green, 255 - blue));
            g.drawString("Score:   " + (blocks - 3), gridX + gridSideLength - 50, gridY - 40);
        }
        if (isOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(over, overX, overY - 50, null);
            g.setColor(Color.WHITE);
            g.drawString("SCORE:    " + (blocks - 3), overX + 287, overY + 350);
            for (int i = 0; i < topThree.length; i++) {
                g.drawString("" + topThree[i], overX + 65, overY + 373 + i * 34);
            }
        }
        return screen;
    }

    /**
     * This method will draw everything
     */
    public void draw() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(drawAsImage(), 0, 0, this);
    }

    /**
     * Draws the out of bounds rectangles
     *
     * @param g2
     */
    public void drawBounds(Graphics2D g2) {
        g2.setColor(new Color(60, 20, 20));
        g2.fillRect(gridX - change, gridY - change, gridSideLength + change * 2, change);
        g2.fillRect(gridX - change, gridY - change, change, gridSideLength + change * 2);
        g2.fillRect(gridX - change, gridY + gridSideLength, gridSideLength + change * 2, change);
        g2.fillRect(gridX + gridSideLength, gridY - change, change, gridSideLength + change * 2);
    }

    /**
     * Draws the grid lines
     *
     * @param g2
     * @param width
     * @param sections
     */
    public void drawGrid(Graphics2D g2, int width, int sections) {
        g2.setColor(Color.BLACK);
        int startX = (this.getWidth() - (width * sections)) / 2;
        int startY = (this.getHeight() - (width * sections)) / 2;
        gridX = startX;
        gridY = startY;
        gridSideLength = width * (sections);
        g2.fillRect(startX, startY, gridSideLength, gridSideLength);
        /*
         int i = 0;
         while (i < sections + 1) {
         g2.drawLine(startX + (width * i), startY,
         startX + (width * i), startY + width * sections);
         i++;
         }
         i = 0;
         while (i < sections + 1) {
         g2.drawLine(startX, startY + (width * i),
         startX + width * sections, startY + (width * i));
         i++;
         }
         */
    }

    /**
     * The class that will take user input
     */
    private class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Takes user input
         *
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();

            if (code == KeyEvent.VK_UP && !down && !oneKey) {
                oneKey = true;
                up = true;
                down = false;
                right = false;
                left = false;
            } else if (code == KeyEvent.VK_DOWN && !up && !oneKey) {
                oneKey = true;
                down = true;
                up = false;
                right = false;
                left = false;
            } else if (code == KeyEvent.VK_LEFT && !right && !oneKey) {
                oneKey = true;
                left = true;
                up = false;
                down = false;
                right = false;
            } else if (code == KeyEvent.VK_RIGHT && !left && !oneKey) {
                oneKey = true;
                right = true;
                up = false;
                down = false;
                left = false;
            } else if (code == KeyEvent.VK_ENTER) {
                hasStarted = true;
                if (isOver) {
                    restart = true;
                    isOver = false;
                    hasStarted = false;
                }
            } else if (code == KeyEvent.VK_1 && !hasStarted) {
                difficulty = 150;
            } else if (code == KeyEvent.VK_2 && !hasStarted) {
                difficulty = 100;
            } else if (code == KeyEvent.VK_3 && !hasStarted) {
                difficulty = 45;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
