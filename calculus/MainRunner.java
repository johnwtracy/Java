
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class MainRunner extends JFrame {

    private JRadioButton graph = new JRadioButton("Create a graph with drawn slices.");
    private JRadioButton integral = new JRadioButton("Create an excel file with volumes.");
    private JButton next = new JButton("Next");

    private GraphingIO gWin;
    private IntegralIO iWin;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        new MainRunner();
    }

    public MainRunner() {
        setSize(300, 300);
        setTitle("");
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 20, 20));

        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                next();
            }

        });

        this.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                setVisible(false);
                if (gWin == null && iWin == null) {
                    System.exit(0);
                }
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

        add(graph);
        add(integral);
        add(next);

        pack();

        setVisible(true);
    }

    public void next() {
        if (graph.isSelected() || integral.isSelected()) {
            setVisible(false);
            instantiate();
        } else {
            JOptionPane.showMessageDialog(this, "One or both of the options must be chosen.");
        }
    }

    public void instantiate() {
        try {
            if (graph.isSelected()) {
                gWin = new GraphingIO(new GraphingWindow(iWin));
                // gWin.start();
            }
            if (integral.isSelected()) {
                iWin = new IntegralIO(new IntegralWindow(gWin));
                iWin.start();
                if (graph.isSelected()) {
                    gWin.start();
                    return;
                }
            }
            if (graph.isSelected()) {
                gWin.start();
                return;
            }
        } catch (Exception e) {

        }
    }

}
