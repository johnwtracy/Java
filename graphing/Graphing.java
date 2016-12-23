
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * *
 * Basic game template. Uses the awt and swing for graphics and window setup
 *
 * @author John Tracy
 */
public class Graphing extends JFrame {

    /*
     Instance variables
     */
    private int startWidth;
    private int startHeight;
    private int scaleX;
    private int scaleY;
    private final int xAxisUnits = 10;
    private final int yAxisUnits = 10;
    private final double margin = .01;

    /*
     Choose which type of graph by setting corresponding boolean var to true
     */
    private final boolean polar = true;
    private final boolean cartesian = true;
    private final boolean parametric = true;

    /**
     * The main method that runs the program
     *
     * @param args
     */
    public static void main(String[] args) {
        Graphing game = new Graphing();
        game.run();
    }

    /**
     * This the the equation, think of this as f of x This is the biggest part
     * and the only thing that needs to be changed to graph
     *
     * @param x
     * @return the operations outcome based on x
     */
    public double f(double x) {
        return Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x;
    }

    /**
     * This the the equation think or this as r or x This is the biggest part
     * and the only thing that needs to be changed to graph
     *
     * @param theta
     * @return the operations outcome based on x
     */
    public double r(double theta) {
        return .1 * theta;
    }

    /**
     * This is the x component
     *
     * @param t
     * @return the y component
     */
    public double x(double t) {
        return Math.sin(t) * (Math.pow(Math.E, Math.cos(t))
                - (2 * Math.cos(4 * t)) - Math.pow(Math.sin(t / 12), 5));
    }

    /**
     * This is the y component
     *
     * @param t
     * @return the y component
     */
    public double y(double t) {
        return Math.cos(t) * (Math.pow(Math.E, Math.cos(t))
                - (2 * Math.cos(4 * t)) - Math.pow(Math.sin(t / 12), 5));
    }

    /**
     * finds the scaling factor for x and y since this can be resized
     */
    public void findScales() {
        scaleX = this.getWidth() / xAxisUnits;
        scaleY = this.getHeight() / yAxisUnits;
    }

    /**
     * Initializes the instance variables
     */
    public void initialize() {
        startHeight = 700;
        startWidth = 700;
        /*
         Graphing basics
         **DO NOT CHANGE**
         */

        setTitle("Graphing Is Fun!!");
        setSize(startWidth, startHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);

    }

    /**
     * Runs the program by drawing the graph and updating the scaling factor
     */
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        initialize();
        while (true) {
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
            }
            findScales();
            draw();
        }
    }

    /**
     * Uses Graphics class to draw
     */
    public void draw() {
        Graphics2D g = (Graphics2D) getGraphics();
        /*
         Below is the basic use of the abstract Graphics class
         and abstract Graphics2D class
         */
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, startWidth, startHeight);
        g.drawImage(drawGraph(), 0, 0, this);
    }

    /**
     * Draws the graph along with the curve
     *
     * @return the image of the graph
     */
    public Image drawGraph() {
        int width = this.getWidth();
        int height = this.getHeight();
        Image graph = new BufferedImage(width, height, Image.SCALE_SMOOTH);
        Graphics g = graph.getGraphics();
        // background of uimage
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int k = -1; k < 2; k++) {
            g.drawLine(0, height / 2 + k, width, height / 2 + k);
        }
        for (int k = -1; k < 2; k++) {
            g.drawLine(width / 2 + k, 0, width / 2 + k, height);
        }
        // draws x and y tick marks
        g.setColor(Color.BLACK);
        for (int k = 0; k < height * .5; k += scaleY) {
            g.drawLine(width / 2 - 5, height / 2 + k, width / 2 + 5, height / 2 + k);
            g.drawLine(width / 2 - 5, height / 2 - k, width / 2 + 5, height / 2 - k);
        }
        for (int k = 0; k < width * .5; k += scaleX) {
            g.drawLine(width / 2 + k, height / 2 - 5, width / 2 + k, height / 2 + 5);
            g.drawLine(width / 2 - k, height / 2 - 5, width / 2 - k, height / 2 + 5);
        }
        // drawing the curve
        g.setColor(Color.BLUE);
        if (cartesian) {
            g.setColor(new Color(20, 20, 150));
            for (double k = -xAxisUnits / 2 + margin; k <= xAxisUnits / 2; k += margin) {
                int x1 = (int) (width / 2 + (k - margin) * scaleX);
                int x2 = (int) (width / 2 + (k) * scaleX);
                int y1 = (int) (height / 2 - f(k - margin) * scaleY);
                int y2 = (int) (height / 2 - f(k) * scaleY);
                g.drawLine(x1, y1, x2, y2);
            }
        }
        if (polar) {
            g.setColor(new Color(20, 150, 20));
            for (double k = margin; k <= 2 * Math.PI; k += margin) {
                int x1 = (int) (width / 2 + (r(k - margin) * Math.cos(k - margin)) * scaleX);
                int x2 = (int) (width / 2 + (r(k) * Math.cos(k)) * scaleX);
                int y1 = (int) (height / 2 - (r(k - margin) * Math.sin(k - margin)) * scaleY);
                int y2 = (int) (height / 2 - (r(k) * Math.sin(k)) * scaleY);
                g.drawLine(x1, y1, x2, y2);
            }
        }
        if (parametric) {
            g.setColor(new Color(180, 20, 20));
            for (double k = -xAxisUnits / 2 + margin; k <= xAxisUnits / 2; k += margin) {
                int x1 = (int) (width / 2 + x(k - margin) * scaleX);
                int x2 = (int) (width / 2 + x(k) * scaleX);
                int y1 = (int) (height / 2 - y(k - margin) * scaleY);
                int y2 = (int) (height / 2 - y(k) * scaleY);
                g.drawLine(x1, y1, x2, y2);
            }
        }
        return graph;
    }

}
