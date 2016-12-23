
import de.congrace.exp4j.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.*;
import javax.swing.*;

public class GraphingWindow extends JFrame {

    private int numSections;
    private int currentFNum = 0;
    private boolean close;
    private Function[] funxRT;
    private Function[] funxLB;
    private Series[] rectangles;
    private String fileName = "";
    private IntegralIO integral;

    // JLabels for GUI
    private JLabel numS = new JLabel("Number of Sections");
    private JLabel eq1 = new JLabel("Top Equation");
    private JLabel eq2 = new JLabel("Bottom Equation");
    private JLabel a = new JLabel("X Lower Bound");
    private JLabel b = new JLabel("X Upper Bound");
    private JLabel ram = new JLabel("RAM Type");
    private JLabel functionNum = new JLabel("Section 1");
    private JLabel sliceSize = new JLabel("Slice Thickness");
    private JLabel creating = new JLabel("Creating Graph");
    private JLabel done = new JLabel("Graph has been created");

    // JTextFields for GUI
    private JTextField numST = new JTextField();
    private JTextField eq1T = new JTextField();
    private JTextField eq2T = new JTextField();
    private JTextField aT = new JTextField();
    private JTextField bT = new JTextField();
    private JTextField ramT = new JTextField("MID");
    private JTextField sliceSizeT = new JTextField();
    private JTextField fileT = new JTextField("Graph");

    // JButtons for GUI
    private JButton nextHome = new JButton("Next");
    private JButton next = new JButton("Next");
    private JButton help = new JButton("Help");
    private JRadioButton dx = new JRadioButton("dx", true);
    private JRadioButton dy = new JRadioButton("dy", false);
    private JRadioButton append = new JRadioButton("Append to an existing file.");
    private ButtonGroup dxdy = new ButtonGroup();

    // JPanels to add and remove
    private JPanel empty = new JPanel();
    private JPanel home = new JPanel(new GridLayout(3, 2, 10, 10));
    private JPanel intake = new JPanel(new GridLayout(9, 2, 10, 10));

    /**
     * Constructor
     */
    public GraphingWindow(IntegralIO integral) {
        setTitle("Create Padowan Graph");
        setSize(200, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        this.integral = integral;

        numST.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                nextFromHome();
            }

        });
        nextHome.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                nextFromHome();
            }

        });
        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                next();
                functionNum.setText("Section " + (currentFNum + 1));
            }

        });
        dx.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                fixOrientation();
            }

        });
        dy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                fixOrientation();
            }

        });
        help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(help, "Made to be opened by Padowan.\n"
                        + "Enter in the two equations.\n"
                        + "When using decimals with an absolute value less than 1\n"
                        + "lead the decimal point with a 0. Otherwise it will not work.\n"
                        + "Any ram type other than RIGHT, LEFT, or MID will be assumed as mid.\n"
                        + "The slice width must be evenly divisible into the difference\n"
                        + "of the upper and lower bounds.\n"
                        + "Note that Padowan cannot solve for irrational numbers.");
            }

        });

        home.add(numS);
        home.add(numST);
        home.add(append);
        home.add(fileT);
        home.add(empty);
        home.add(nextHome);

        intake.add(functionNum);
        intake.add(empty);
        intake.add(eq1);
        intake.add(eq1T);
        intake.add(eq2);
        intake.add(eq2T);
        intake.add(dx);
        intake.add(dy);
        intake.add(b);
        intake.add(bT);
        intake.add(a);
        intake.add(aT);
        intake.add(ram);
        intake.add(ramT);
        intake.add(sliceSize);
        intake.add(sliceSizeT);
        intake.add(help);
        intake.add(next);

        ramT.setToolTipText("LEFT, MID, RIGHT");
        dxdy.add(dx);
        dxdy.add(dy);

        add(home);

        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                close = true;
                setVisible(false);
            }

            public void windowClosed(WindowEvent we) {
            }

            public void windowIconified(WindowEvent we) {
            }

            public void windowDeiconified(WindowEvent we) {
            }

            public void windowActivated(WindowEvent we) {
            }

            public void windowDeactivated(WindowEvent we) {
            }

        });
        pack();

    }

    public void nextFromHome() {
        try {
            numSections = (int) (Integer.parseInt(numST.getText()));
            if (append.isSelected() && (new File(fileT.getText().trim() + ".grf")).exists() == false) {
                throw new IOException();
            }
            fileName = fileT.getText().trim();
            funxRT = new Function[numSections];
            funxLB = new Function[numSections];
            rectangles = new Series[numSections];
            remove(home);
            add(intake);
            pack();
        } catch (Exception e) {
            if (e instanceof IOException) {
                JOptionPane.showMessageDialog(empty, "File does not already exist.");
            } else {
                JOptionPane.showMessageDialog(empty, "Number of sections must be an integer");
            }
        }
    }

    public void nextFromIntake() {
        remove(intake);
        add(creating);
        pack();
    }

    public void done() {
        remove(creating);
        add(done);
        pack();
    }

    public boolean isAcceptable(double a, double b, double d) {
        double margin = 0.0;
        double extra = Math.abs((b - a) % d);
        if (extra <= margin) {
            return true;
        } else {
            return false;
        }
    }

    public void next() {
        try {
            double aTest = Double.parseDouble(aT.getText().trim());
            double bTest = Double.parseDouble(bT.getText().trim());
            double deltaTest = Double.parseDouble(sliceSizeT.getText().trim());
            if (eq1T.getText().trim().equals("") || eq2T.getText().trim().equals("")
                    || aT.getText().trim().equals("") || bT.getText().trim().equals("")
                    || ramT.getText().trim().equals("") || sliceSizeT.getText().trim().equals("")
                    || ramT.getText().trim().equals("")
                    || Double.parseDouble(aT.getText().trim()) >= Double.parseDouble(bT.getText().trim())
                    || !isAcceptable(aTest, bTest, deltaTest)) {
                throw new Exception();
            }
            String eqRT = eq1T.getText().replaceAll(" ", "");
            String eqLB = eq2T.getText().replaceAll(" ", "");
            eqRT = Function.fixOperands(eqRT);
            eqLB = Function.fixOperands(eqLB);
            System.out.println(eqRT + "\n" + eqLB);
            String type = ramT.getText().trim();
            try {
                Calculable eq1 = new ExpressionBuilder(eqRT).withVariable(getOrientation().toLowerCase(), 0.0).build();
                Calculable eq2 = new ExpressionBuilder(eqLB).withVariable(getOrientation().toLowerCase(), 0.0).build();
            } catch (Exception e) {
                throw new Exception();
            }
            double delta = Double.parseDouble(sliceSizeT.getText().trim());
            double from = Double.parseDouble(aT.getText().trim());
            double to = Double.parseDouble(bT.getText().trim());
            boolean isDX = dx.isSelected();
            funxRT[currentFNum] = new Function(from, to, delta, isDX, eqRT, type);
            funxLB[currentFNum] = new Function(from, to, delta, isDX, eqLB, type);
            rectangles[currentFNum] = new Series(funxRT[currentFNum], funxLB[currentFNum]);
            currentFNum++;
            fixOrientation();
            if (currentFNum < numSections) {
                resetIntake();
            } else {
                nextFromIntake();
            }
            pack();
        } catch (Exception e) {
            if (e instanceof IOException) {
                JOptionPane.showMessageDialog(empty, "File does not already exist.");
            } else {
                JOptionPane.showMessageDialog(empty, "Check inputs.");
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isAppend() {
        return append.isSelected();
    }

    public void fixOrientation() {
        a.setText(getOrientation() + " Lower Bound");
        b.setText(getOrientation() + " Upper Bound");
        String RT = "Right ";
        String LB = "Left ";
        if (getOrientation().equals("X")) {
            RT = "Top ";
            LB = "Bottom ";
        }
        eq1.setText(RT + "Equation");
        eq2.setText(LB + "Equation");
    }

    public void resetIntake() {
        eq1T.setText("");
        eq2T.setText("");
        dx.setSelected(true);
        dy.setSelected(false);
        aT.setText("");
        bT.setText("");
    }

    public String getOrientation() {
        String s = "X";
        if (dy.isSelected()) {
            s = "Y";
        }
        return s;
    }

    public Function[] getFunxRT() {
        return funxRT;
    }

    public Function[] getFunxLB() {
        return funxLB;
    }

    public Series[] getRectangles() {
        return rectangles;
    }

    public boolean bothAreClosing() {
        if (integral == null) {
            return close;
        }
        return close && integral.isClosing();
    }

    public boolean isClosing() {
        return close;
    }

}
