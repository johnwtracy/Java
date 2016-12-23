package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import physicsmath.Kinematics;

public class Simulation extends JFrame {

    // Panels for easy understanding
    private JPanel inputPane;
    private JPanel inPane;
    private JPanel labelPane;
    private JPanel graphicsPane;

    // TextFields and Labels for user input
    private JLabel angleLabel;
    private JLabel timeL = new JLabel("Change in Time: ");
    private TextField angle;
    private JLabel initialVLabel;
    private TextField initialV;

    // BUTTONS
    private Button run;
    private Button reset;
    private Button close;
    private Button pause;
    private JSlider timeSlide = new JSlider(1, 1000, 150);

    // Event handler
    private Handler handler;

    private int windowWidth = 500;
    private int windowHeight = 700;
    private ArrayList<Integer> allX;
    private ArrayList<Integer> allY;
    // varibales to be gottenn from textfields
    private Projectile projectile;

    private DecimalFormat df = new DecimalFormat("#.###");

    private double theta;
    private double vo;
    private double vX;
    private double vY;
    private double x;
    private double y;
    private double time;

    private boolean simStart;
    private boolean simPause;
    private double xAxisUnits;
    private double scaleX;
    private double yAxisUnits;
    private double scaleY;

    public Simulation() {
        setSize(windowWidth, windowHeight);
        setTitle("Kinematics Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setLayout(new GridLayout(2, 1));

        // instantiates panels
        inputPane = new JPanel(new GridLayout(1, 2));
        graphicsPane = new JPanel(null);
        inPane = new JPanel(new GridLayout(5, 1, 10, 50));
        labelPane = new JPanel(new GridLayout(5, 1, 10, 50));

        // instantiates textfields and labels and buttons
        angleLabel = new JLabel("Angle: ");
        angle = new TextField("", 10);
        angleLabel = new JLabel("Angle: ");
        initialV = new TextField("", 10);
        initialVLabel = new JLabel("Initial Velocity: ");
        run = new Button("Run");
        reset = new Button("Reset");
        close = new Button("Close");
        pause = new Button("Pause");

        // adds content panes to appropritate panes
        add(graphicsPane);
        add(inputPane);

        inputPane.add(labelPane);
        inputPane.add(inPane);

        labelPane.add(angleLabel);
        inPane.add(angle);
        labelPane.add(initialVLabel);
        inPane.add(initialV);
        inPane.add(timeSlide);
        labelPane.add(timeL);
        inPane.add(run);
        inPane.add(reset);
        labelPane.add(pause);
        labelPane.add(close);
        handler = new Handler();

        // adds the handler to the containters
        angle.addActionListener(handler);
        initialV.addActionListener(handler);
        run.addActionListener(handler);
        reset.addActionListener(handler);
        close.addActionListener(handler);
        pause.addActionListener(handler);

        graphicsPane.setBackground(Color.WHITE);
        projectile = new Projectile(20, 230, x, y);
        allX = new ArrayList<>();
        allY = new ArrayList<>();
    }

    /**
     * Runs to perform graphics on the screen
     */
    public void run() {
        while (true) {
            update();
            draw();
            try {
                Thread.sleep(30);
            } catch (Exception e) {
            }
        }
    }

    /**
     * finds the scaling factor for x and y since this can be resized
     */
    public void findScales() {
        double max = (xAxisUnits > yAxisUnits) ? xAxisUnits : yAxisUnits;
        int ax = (int) max / 5 + 1;
        scaleX = (double) 440 / (5 * ax);
        scaleY = (double) 210 / (5 * ax);
    }

    /**
     * Updates variables
     */
    public void update() {
        double timeMargin = (double) timeSlide.getValue() * timeSlide.getValue() / 100000;
        vX = Kinematics.getVX(vo, theta);
        vY = Kinematics.getVY(vo, theta);
        xAxisUnits = Kinematics.getFar(vo, theta);
        yAxisUnits = Kinematics.getPeak(vo, theta);
        findScales();
        if (simStart && !simPause) {
            if (projectile.getX() > 450
                    || projectile.getY() > 220) {
                done();
            }
            projectile.setX(((Kinematics.getDeltaX(vX, vX, time)) * scaleX) + 20);
            projectile.setY(220 - (Kinematics.getDeltaY(vY, -9.8, time) * scaleY));
            time += timeMargin;
        }
    }

    public void done() {
        time = 0;
    }

    /**
     * Draws to the window
     */
    public void draw() {
        // window graphics
        Graphics gMain = graphicsPane.getGraphics();

        // image graphics
        Image screen = createImage(windowWidth, 235);
        Graphics2D g = (Graphics2D) screen.getGraphics();

        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, windowWidth, graphicsPane.getHeight());
        double x = projectile.getX();
        double y = projectile.getY();
        allX.add((int) x);
        allY.add((int) y);
        g.setColor(Color.RED);
        for (int i = 0; i < allX.size(); i++) {
            int ex = allX.get(i);
            int why = allY.get(i) + 10;
            g.drawLine(ex, why, ex + 1, why);
        }
        drawGraph(g);
        projectile.drawProjectile(g);

        // draws image for better graphics
        gMain.drawImage(screen, 0, 0, graphicsPane);
    }

    /**
     * Draws the axes for the projectile
     *
     * @param g Graphics object to draw to
     */
    public void drawGraph(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(20, 20, 20, 230);
        g.setColor(Color.BLACK);
        g.drawLine(20, 230, 470, 230);
        g.drawString("x position:  " + df.format((projectile.getX() - 20) / scaleX), 350, 20);
        g.drawString("y position:  " + df.format(Math.abs((projectile.getY() - 220) / scaleY)), 350, 40);
        g.drawString("time:  " + df.format(time), 350, 60);
        double max = (xAxisUnits > yAxisUnits) ? xAxisUnits : yAxisUnits;
        int ax = (int) (max / 5 + 1);
        if (simStart) {
            g.drawString(df.format(xAxisUnits), (int) (xAxisUnits * scaleX), 215);
            g.drawString(df.format(yAxisUnits), 35, 230 - (int) (yAxisUnits * scaleY));
        }
        g.drawString(df.format(5 * ax), 450, 228);
        g.drawString(df.format(5 * ax), 21, 28);
    }

    /**
     * *
     * Handlers the actions performed on the TextFields
     */
    private class Handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object check = e.getSource();
            if (check.equals(angle) || check.equals(run) || check.equals(initialV)) {
                try {
                    double num = Double.parseDouble(angle.getText());
                    if (num <= 90 && num >= 0) {
                        theta = num;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(graphicsPane, "Check Angle Input");
                }
            }
            if (check.equals(initialV) || check.equals(run)) {
                try {
                    double num = Double.parseDouble(initialV.getText());
                    vo = num;
                    simStart = true;
                } catch (NumberFormatException error) {
                    JOptionPane.showMessageDialog(graphicsPane, "Check Velocity Input");
                }
            }
            if (check.equals(reset)) {
                vo = 0;
                theta = 0;
                vX = 0;
                vY = 0;
                x = 0;
                y = 0;
                time = 0;
                projectile.setX(20);
                projectile.setY(220);
                allY = new ArrayList<>();
                allX = new ArrayList<>();
                simStart = false;
                simPause = false;
            }
            if (check.equals(close)) {
                System.exit(0);
            }
            if (check.equals(pause)) {
                simPause = !simPause;
                if (simPause) {
                    pause.setLabel("Play");
                } else {
                    pause.setLabel("Pause");
                }
            }
        }

    }

}
