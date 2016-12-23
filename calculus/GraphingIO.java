
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class GraphingIO extends Thread {

    private GraphingWindow window;
    private PrintWriter out;

    /**
     *
     * @param window
     * @throws IOException
     */
    public GraphingIO(GraphingWindow window) throws IOException {
        this.window = window;
    }

    /**
     * Creates a graph with the rectangles
     */
    public void run() {
        try {
            Function[] RT = window.getFunxRT();
            Function[] LB = window.getFunxLB();
            Series[] rectangles = window.getRectangles();
            try {
                while (RT == null || LB == null || rectangles == null || window.getFileName().trim().equals("")) {
                    sleep(10);
                    RT = window.getFunxRT();
                    LB = window.getFunxLB();
                    rectangles = window.getRectangles();
                    checkEarlyClosing();
                }
            } catch (InterruptedException e) {
            }
            Scanner readFile;
            String origional = "";
            if (window.isAppend()) {
                readFile = new Scanner(new File(window.getFileName() + ".grf"));
                while (readFile.hasNextLine()) {
                    origional += readFile.nextLine() + "\n";
                }
                readFile.close();
            }
            out = new PrintWriter(new File(window.getFileName().trim() + ".grf"));
            if (window.isAppend()) {
                out.println(origional);
            } else {
                out.println(graphSetup());
            }
            for (int i = 0; i < RT.length; i++) {
                try {
                    while (RT[i] == null || LB[i] == null || rectangles[i] == null) {
                        checkClosing();
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                }
                out.println(RT[i].toString());
                out.println(LB[i].toString());
                out.println(rectangles[i].toString());
            }
            out.close();
            window.done();
            while (!window.bothAreClosing()) {
                try {
                    sleep(10);
                } catch (Exception e) {
                }
            }
            System.exit(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, "Could not create graph.\n"
                    + "Try creating a new one.");
        }
    }

    /**
     * @return the basic setup for a graph with axes
     */
    public static String graphSetup() {
        return ";This file was created by Graph (http://www.padowan.dk)\n"
                + ";Do not change this file from other programs.\n"
                + "[Graph]\n"
                + "Version = 4.4.2.543\n"
                + "MinVersion = 2.5\n"
                + "OS = Windows NT 6.2 \n"
                + "\n"
                + "[Axes]\n"
                + "xMin = -10.0\n"
                + "xMax = 10.0\n"
                + "xTickUnit = 1\n"
                + "xGridUnit = 0.25\n"
                + "xShowGrid = 1\n"
                + "xShowLabel = 0\n"
                + "xAutoTick = 0\n"
                + "xAutoGrid = 0\n"
                + "yMin = -10.0\n"
                + "yMax = 10.0\n"
                + "yTickUnit = 1\n"
                + "yGridUnit = 0.25\n"
                + "yShowGrid = 1\n"
                + "yAutoTick = 0\n"
                + "yAutoGrid = 0\n"
                + "AxesColor = clBlack\n"
                + "GridColor = 0x00FF9999\n"
                + "ShowLegend = 1\n"
                + "Radian = 1\n"
                + "LegendPlacement = 0\n"
                + "LegendPos = 0.0, 0.0\n";
    }

    public void checkEarlyClosing() {
        if (window.isClosing()) {
            try {
                while (!window.bothAreClosing()) {
                    sleep(10);
                }
            } catch (Exception e) {
            }
            System.exit(0);
        }
    }

    public void checkClosing() {
        if (window.isClosing()) {
            out.close();
            try {
                while (!window.bothAreClosing()) {
                    sleep(10);
                }
            } catch (Exception e) {
            }
            System.exit(0);
        }
    }

    public boolean isClosing() {
        return window.isClosing();
    }

}
