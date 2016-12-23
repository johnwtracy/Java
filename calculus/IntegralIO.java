
import de.congrace.exp4j.*;
import java.io.*;
import java.text.*;
import javax.swing.JOptionPane;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.*;

public class IntegralIO extends Thread {

    // excel and integral instance variable
    private WritableWorkbook wb;
    private WritableSheet[] sheets;
    private Integral[] integrals;
    private DecimalFormat df;
    private IntegralWindow window;

    /**
     * Everything in the constructor
     *
     * @throws IOException
     * @throws WriteException
     * @throws UnparsableExpressionException
     * @throws UnknownFunctionException
     * @throws jxl.read.biff.BiffException
     */
    public IntegralIO(IntegralWindow window) throws IOException, WriteException, UnparsableExpressionException, UnknownFunctionException, BiffException {
        // used to round to three decimal places
        df = new DecimalFormat("#.###");
        this.window = window;
    }

    /**
     * Run method
     */
    public void run() {
        // used to round to three decimal places
        df = new DecimalFormat("#.###");
        // waits for the user to input the number of integrals they have to enter
        while (window.getCount() == 0) {
            checkEarlyClose();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        // defines how many sheets for each integral and creates workbook
        Sheet[] oldSheets;
        Integral[] oldIntegrals;
        // if the excel file is a valid workbook
        try {
            // creates an arry of old integrals from the excel file if the user wants to append
            if (window.isAppend()) {
                Workbook wbOriginal = Workbook.getWorkbook(new java.io.File(window.getFileName() + ".xls"));
                oldSheets = wbOriginal.getSheets();
                oldIntegrals = new Integral[oldSheets.length];
                for (int i = 0; i < oldSheets.length; i++) {
                    Sheet old = oldSheets[i];
                    oldIntegrals[i] = new Integral(old.getCell(1, 2).getContents(),
                            Double.parseDouble(old.getCell(1, 7).getContents().substring(6).trim()),
                            Double.parseDouble(old.getCell(1, 8).getContents().substring(4).trim()),
                            (old.getCell(1, 10).getContents().substring(11).trim().equals("x")),
                            Double.parseDouble(old.getCell(1, 12).getContents().substring(11).trim()),
                            old.getCell(1, 6).getContents().trim());
                }
                wbOriginal.close();
            } else {
                oldSheets = new Sheet[0];
                oldIntegrals = new Integral[0];
            }
        } catch (Exception e) {
            oldSheets = new Sheet[0];
            oldIntegrals = new Integral[0];
        }
        // waits for integrals to be instantiated
        while (window.getIntegrals() == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        // defines integrals from the window
        try {
            wb = Workbook.createWorkbook(new File(window.getFileName() + ".xls"));
            integrals = window.getIntegrals();
            sheets = new WritableSheet[window.getCount() + oldIntegrals.length];
            for (int i = 0; i < sheets.length; i++) {
                checkClosing(oldIntegrals);
                // waits for the corresponding integral to be entered and finished
                if (i < integrals.length) {
                    while (integrals[i] == null) {
                        checkClosing(oldIntegrals);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    }
                    // add Integrals and creates a sheet
                    sheets[i] = wb.createSheet("Sheet" + (i + 1), 0);
                    addIntegral(sheets[i], integrals[i]);
                } else {
                    sheets[i] = wb.createSheet("Sheet" + (i + 1), 0);
                    addIntegral(sheets[i], oldIntegrals[i - integrals.length]);
                }
            }
            // writes to and closes the file
            wb.write();
            wb.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, "Could not write to excel file.");
        }
        // clears prev screen and adds news that it has been created
        window.excelCreated();
        while (true) {
            if (window.bothAreClosing()) {
                System.exit(0);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    public void checkEarlyClose() {
        if (window.isClosing()) {
            try {
                while (!window.bothAreClosing()) {
                    Thread.sleep(10);
                }
                System.exit(0);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Checks if the window is closed during trying to append an existing file
     * Writes the old integrals to the file
     *
     * @param ints the old integrals from the excel sheet
     * @throws IOException
     * @throws WriteException
     * @throws UnparsableExpressionException
     * @throws UnknownFunctionException
     */
    public void checkClosing(Integral[] ints) throws IOException, WriteException, UnparsableExpressionException, UnknownFunctionException {
        // if the window is being closed
        if (window.isClosing()) {
            // creates a writable sheet array for the Integrals
            WritableSheet[] shs = new WritableSheet[ints.length];
            // writes to and creates sheet
            for (int i = 0; i < ints.length; i++) {
                shs[i] = wb.createSheet("Sheet" + (i + 1), 0);
                addIntegral(shs[i], ints[i]);
            }
            // writes and closes the excel file
            if (window.isAppend()) {
                if (ints.length != 0 && ints[0] != null) {
                    wb.write();
                }
            } else {
                // checks if at least one sheet has been made
                if (sheets[0] != null) {
                    wb.write();
                }
            }
            try {
                wb.close();
            } catch (Exception e) {
            }
            // exits the program
            while (!window.bothAreClosing()) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
            }
            System.exit(0);
        }
    }

    public boolean isClosing() {
        return window.isClosing();
    }

    /**
     * Adds an integral to the designated sheet
     *
     * @param s the sheet to be added to
     * @param i the integral to add
     * @throws WriteException
     * @throws UnparsableExpressionException
     * @throws UnknownFunctionException
     */
    public void addIntegral(WritableSheet s, Integral i) throws WriteException, UnparsableExpressionException, UnknownFunctionException {
        // creates labels
        Label eqT = new Label(1, 1, "Integrand");
        Label eq = new Label(1, 2, i.getEq());
        Label type = new Label(1, 4, "RAM Type");
        Label ram = new Label(1, 5, i.getRAM());
        Label from = new Label(1, 7, "From: " + i.getLower());
        Label to = new Label(1, 8, "To: " + i.getUpper());
        Label respect = new Label(1, 10, "Respect to " + i.getWithRespect());
        Label dx = new Label(1, 12, "Step Size: " + i.getDeltaX());
        Label slice = new Label(3, 1, "Slice");
        Label xVal = new Label(4, 1, i.getWithRespect() + " values");
        Label outs = new Label(5, 1, "Slice Volume");
        Label actual = new Label(6, 1, "Actual Volume");
        Label theory = new Label(7, 1, "Theoretical Volume");
        // defines the getXValues() and getOutputs()
        i.integrateActual();
        // creates and adds three numbers; x/y value, the corresponding output value, 
        // and the slice number respectively
        for (int j = 0; j < i.getXValues().size(); j++) {
            jxl.write.Number xval = new jxl.write.Number(4, j + 2, Double.parseDouble(df.format(i.getXValues().get(j))));
            s.addCell(xval);
            jxl.write.Number out = new jxl.write.Number(5, j + 2, Double.parseDouble(df.format(i.getOutputs().get(j))));
            s.addCell(out);
            jxl.write.Number sliceNum = new jxl.write.Number(3, j + 2, j + 1);
            s.addCell(sliceNum);
        }
        // created and adds actual and theoretical volumes
        jxl.write.Number ac = new jxl.write.Number(6, 2,
                Double.parseDouble(df.format(i.integrateActual())));
        s.addCell(ac);
        jxl.write.Number th = new jxl.write.Number(7, 2,
                Double.parseDouble(df.format(i.integrateTheoretical())));
        // adds most cells
        s.addCell(dx);
        s.addCell(respect);
        s.addCell(slice);
        s.addCell(type);
        s.addCell(ram);
        s.addCell(from);
        s.addCell(to);
        s.addCell(actual);
        s.addCell(theory);
        s.addCell(outs);
        s.addCell(th);
        s.addCell(xVal);
        s.addCell(eqT);
        s.addCell(eq);
    }

}
