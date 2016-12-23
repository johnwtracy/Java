
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class IntegralWindow extends JFrame {

    // instance vars
    private int num = 1;
    private Integral[] integrals;
    private Handler handle;
    private int count;
    private String fileName;
    private boolean close;
    private GraphingIO graphing;

    // empty panel for formatting
    JPanel empty = new JPanel();

    // TextFields
    private JTextField howMany;
    private JTextField eqT = new JTextField();
    private JTextField aT = new JTextField();
    private JTextField bT = new JTextField();
    private JTextField deltaT = new JTextField();
    private JTextField fileT = new JTextField("calculus");
    private JTextField ramTypeT = new JTextField("MID");

    // labels
    private JLabel howManyLabel;
    private JLabel eq = new JLabel("Integrand To Integrate:");
    private JLabel a = new JLabel("Lower Limit:  ");
    private JLabel b = new JLabel("Upper Limit:  ");
    private JLabel eqNum = new JLabel("Integrand " + (num));
    private JLabel delta = new JLabel("Delta X:");
    private JLabel type = new JLabel("Cross Section Type:");
    private JLabel creating = new JLabel("Creating Excel WorkBook");
    private JLabel file = new JLabel("Excel File Name: ");
    private JLabel ramType = new JLabel("RAM Type");

    // buttons
    private JButton nextHome = new JButton("Next");
    private JButton next = new JButton("Next");
    private JButton help = new JButton("Help");

    // check boxes
    private JRadioButton dx = new JRadioButton("dx", true);
    private JRadioButton dy = new JRadioButton("dy", false);
    private JRadioButton append = new JRadioButton("Append to Existing .xls file", false);
    private ButtonGroup dydx = new ButtonGroup();
    private JProgressBar progressBar;

    /**
     * Starts window and gives first screen
     */
    public IntegralWindow(GraphingIO graphing) {
        super("Excel Creator");
        count = 0;
        setSize(100, 100);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));
        setResizable(false);

        handle = new Handler();
        this.graphing = graphing;

        //pane = new JPanel(new GridLayout(1, 2));
        howMany = new JTextField(20);
        howManyLabel = new JLabel("How Many Integrals?");

        // sets up window properties
        add(howManyLabel);
        add(howMany);
        add(file);
        add(fileT);
        add(append);
        add(nextHome);
        dydx.add(dx);
        dydx.add(dy);
        ramTypeT.setToolTipText("right, left or mid");
        howMany.addActionListener(handle);
        nextHome.addActionListener(handle);
        addWindowListener(new WindowHandler());
        pack();
        setVisible(true);
    }

    /**
     * Removes first screen and starts input sequence
     */
    public void inputScreen() {
        setLayout(new GridLayout(8, 2, 10, 15));
        remove(howMany);
        remove(howManyLabel);
        remove(file);
        remove(fileT);
        remove(empty);
        remove(empty);
        remove(nextHome);
        remove(append);
        add(eqNum);
        add(empty);
        add(eq);
        add(eqT);
        add(a);
        add(aT);
        add(b);
        add(bT);
        add(delta);
        add(deltaT);
        add(dx);
        add(dy);
        add(ramType);
        add(ramTypeT);
        add(help);
        add(next);
        next.addActionListener(handle);
        help.addActionListener(handle);
        pack();
    }

    /**
     * called after taking valid info from an integral input
     */
    public void resetInput() {
        eqNum.setText("Integrand " + (num));
        eqT.setText("");
        aT.setText("");
        bT.setText("");
        pack();
    }

    /**
     * Called after all input for integrals Lets user know the workbook is being
     * created
     */
    public void createScreen() {
        remove(eqNum);
        remove(empty);
        remove(eq);
        remove(eqT);
        remove(a);
        remove(aT);
        remove(b);
        remove(bT);
        remove(dx);
        remove(dy);
        remove(empty);
        remove(type);
        remove(delta);
        remove(deltaT);
        remove(empty);
        remove(ramType);
        remove(ramTypeT);
        remove(help);
        remove(next);
        setLayout(new FlowLayout());
        add(creating);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar);
        pack();
    }

    /**
     * Called by runner when done making file to tell the user the news
     */
    public void excelCreated() {
        remove(creating);
        remove(progressBar);
        setLayout(new FlowLayout());
        add(new JLabel("WorkBook has been created!"));
        addWindowListener(new WindowHandler());
        pack();
    }

    /**
     * Called by runner for the array of integrals
     *
     * @return all the integrals the the user validly input
     */
    public Integral[] getIntegrals() {
        return integrals;
    }

    public boolean isAppend() {
        return append.isSelected();
    }

    /**
     * Getter method for count
     *
     * @return the number of integrals that will be entered
     */
    public int getCount() {
        return count;
    }

    public boolean bothAreClosing() {
        if (graphing == null) {
            return close;
        }
        return close && graphing.isClosing();
    }

    public boolean isClosing() {
        return close;
    }

    /**
     * Getter for the file name
     *
     * @return the prospective file name
     */
    public String getFileName() {
        return fileName.trim();
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

    /**
     * *
     * Private handler for the window class
     */
    private class Handler implements ActionListener {

        /**
         * Checks for the source and carried out the appropriate actions
         */
        public void actionPerformed(ActionEvent e) {
            // source object for the handler
            Object check = e.getSource();
            // checks for valid input
            if (check.equals(nextHome) || check.equals(howMany) || check.equals(fileT)) {
                try {
                    boolean b = close;
                    int num = 0;
                    if (!b) {
                        num = Integer.parseInt(howMany.getText());
                    }
                    // rawinput for the file
                    String temp = fileT.getText().trim();
                    // throws exceotion if null
                    if (temp.equals("")) {
                        throw new Exception();
                    }
                    // assighns file name
                    fileName = fileT.getText().trim();
                    //tests if file exists
                    if (append.isSelected()) {
                        File f = new File(fileName + ".xls");
                        if (!f.exists()) {
                            throw new FileNotFoundException();
                        }
                    }
                    // no more than 20 integrals
                    if (!b && num <= 20 && num > 0) {
                        count = num;
                        howMany.setText("");
                        integrals = new Integral[count];
                    } else if (!b && (num > 20 || num <= 0)) {
                        howMany.setText("");
                        throw new Exception();
                    }
                    close = b;
                    // changes screen for integral info input
                    inputScreen();
                } catch (Exception error) {
                    error.printStackTrace();
                    if (error instanceof FileNotFoundException) {
                        JOptionPane.showMessageDialog(append, "File does not already exist.");
                    } else {
                        howMany.setText("");
                        JOptionPane.showMessageDialog(howMany, "Must be a number between 1 and 20 and a non null file name.");
                    }
                }
            }
            if (check.equals(dx)) {
                dy.setSelected(!dx.isSelected());
            }
            if (check.equals(dy)) {
                dx.setSelected(!dy.isSelected());
            }
            if (check.equals(next)) {
                try {
                    // to make sure the limits are okay
                    if (Double.parseDouble(bT.getText().trim()) <= Double.parseDouble(aT.getText().trim())) {
                        throw new Exception();
                    }
                    // so see if deltaX is improper for the limits
                    if (Double.parseDouble(deltaT.getText().trim())
                            > Double.parseDouble(bT.getText().trim()) - Double.parseDouble(aT.getText().trim())) {
                        throw new Exception();
                    }
                    double aTest = Double.parseDouble(aT.getText().trim());
                    double bTest = Double.parseDouble(bT.getText().trim());
                    double dTest = Double.parseDouble(deltaT.getText().trim());
                    if (!isAcceptable(aTest, bTest, dTest)) {
                        throw new Exception();
                    }
                    // to make sure there it a parseable equation
                    if (!Integral.isEquation(eqT.getText().trim(), dx.isSelected())) {
                        throw new Exception();
                    }
                    // to see if there is more room for integrals
                    if (num < integrals.length + 1) {
                        // adds all the info for an integral
                        integrals[num - 1] = new Integral(eqT.getText().trim(), Double.parseDouble(aT.getText().trim()),
                                Double.parseDouble(bT.getText().trim()), dx.isSelected(),
                                Double.parseDouble(deltaT.getText().trim()), ramTypeT.getText().trim());
                        num++;
                        // resets input screen
                        resetInput();
                        // fi thats the last one it will move to the "create" screen
                        if (num == integrals.length + 1) {
                            createScreen();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(eq, "One of the entries is invalid. Please check all your inputs.");
                }
            }
            if (check.equals(help)) {
                // displays help message
                JOptionPane.showMessageDialog(eq, "Integrand Format: (x or y - 2)^4 / (sin(x or y))  (\"y = \" or \"x = \" is already assumed)"
                        + "\nNote: ENTER YOUR INTEGRAL/INTEGRAND SLOVED ALREADY FOR YOUR CROSSSECTION TYPE"
                        + "\nIN TERMS OF 'x' OR 'y'. NOT BOTH."
                        + "\nSee the bottom of www.objecthunter.net/exp4j/apidocs/index.html for supported "
                        + "operations."
                        + "\nChoose dy or dx and use the corresponding variable."
                        + "\nFields to be used as numbers must have numbers or hexidecimal as entries."
                        + "\nThis will create an Excel woorkbook in the folder that it is run from."
                        + "\nMake sure that the upper limit is greater than the lower limit "
                        + "and that the delta x is less than or equal to the difference between the two."
                        + "\n"
                        + "\n**Very Important** : When using decimals with an absolute value less than 1\n"
                        + "lead the decimal point with a 0. Otherwise it will not work"
                        + "\nFor the most acurate results, we force you to use limits whose "
                        + "difference is divisible by the DeltaX. Also, theorecical volumes are calculated by "
                        + "\na trapezoidal area aproximation with a small delta x so this becomes inacurate with very"
                        + "\nlarge integral values");
            }

        }

    }

    private class WindowHandler implements WindowListener {

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

    }

}
