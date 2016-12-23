
import de.congrace.exp4j.*;
import java.util.ArrayList;

public class Integral {

    // instance variable
    private double a;  // initial limit to the integral
    private double b; //final limit to the integral
    private String equation; //original string
    private double deltaX;
    private boolean isDX;
    private boolean right;
    private boolean left;
    private boolean mid;
    private Calculable eq;

    // ArrayLists for actual volume ins and outs
    private ArrayList xValues = new ArrayList();
    private ArrayList outputValues = new ArrayList();

    /**
     * Constructor for the integral and assigns values and info
     *
     * @param equation this is the unparsed equation string
     * @param a lower limit
     * @param b upper limit
     * @param isDX if dx or dy in terms of dx
     * @param deltaX the step size
     * @param ram ram type(right, left, mid)
     * @throws de.congrace.exp4j.UnknownFunctionException
     * @throws de.congrace.exp4j.UnparsableExpressionException
     */
    public Integral(String equation, double a, double b, boolean isDX, double deltaX, String ram) throws UnknownFunctionException, UnparsableExpressionException {
        this.a = a;
        this.b = b;
        this.equation = equation;
        this.isDX = isDX;
        this.deltaX = deltaX;
        if (ram.trim().toLowerCase().equals("right")) {
            right = true;
        } else if (ram.trim().toLowerCase().equals("left")) {
            left = true;
        } else {
            mid = true;
        }
        equationSetup();

    }

    /**
     *
     * @param equation unparsed equation
     * @param isDX signifies the variable to look for
     * @return true if the equation can be parsed using exp4j
     */
    public static boolean isEquation(String equation, boolean isDX) {
        equation = fixOperands(equation);
        try {
            Calculable eq;
            // checks for x or y depending on isDX
            if (isDX) {
                eq = new ExpressionBuilder(equation).withVariable("x", 0.0).build();
            } else {
                eq = new ExpressionBuilder(equation).withVariable("y", 0.0).build();
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     *
     * @param str unparsed equation
     * @return str with operands fixed for parsing
     */
    public static String fixOperands(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (str.substring(i, i + 1).equals("x") || str.substring(i, i + 1).equals("y")) {
                int p = i;
                while (p >= 0) {
                    p--;
                    if (operandHelper(str.substring(p, p + 1))) {
                        break;
                    } else if (!str.substring(p, p + 1).equals(" ")) {
                        str = str.substring(0, p + 1) + "*" + str.substring(p + 1);
                        i++;
                        break;
                    }
                }
            }
        }
        return str;
    }

    /**
     *
     * @param a string for checking
     * @return whether the string has an acceptable character for the equation
     */
    private static boolean operandHelper(String a) {
        return a.equals("*") || a.equals("+") || a.equals("-") || a.equals("(") || a.equals("/");
    }

    /**
     * Calculates the output value based on a given double
     *
     * @param t the value of the variable
     * @return the output value at that variable
     * @throws UnknownFunctionException
     * @throws UnparsableExpressionException
     */
    public double calc(double t) throws UnknownFunctionException, UnparsableExpressionException {
        if (isDX) {
            eq.setVariable("x", t);
        } else {
            eq.setVariable("y", t);
        }
        return eq.calculate();
    }

    /**
     * Gets the deltaX
     *
     * @return the step size
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * Sets up the calculable equation so it does not have to be instantiated
     * multiple times.
     *
     * @throws UnknownFunctionException
     * @throws UnparsableExpressionException
     */
    public void equationSetup() throws UnknownFunctionException, UnparsableExpressionException {
        double t = 0.0;
        equation = fixOperands(equation);
        if (isDX) {
            eq = new ExpressionBuilder(equation).withVariable("x", t).build();
        } else {
            eq = new ExpressionBuilder(equation).withVariable("y", t).build();
        }
    }

    /**
     * Getter for the equation
     *
     * @return string the equation
     */
    public String getEq() {
        return equation;
    }

    /**
     * Corresponding x or y based on isDX
     *
     * @return "x" if isDX and "y" if !isDX
     */
    public String getWithRespect() {
        if (isDX) {
            return "x";
        } else {
            return "y";
        }
    }

    /**
     * Getter for lower limit
     *
     * @return a the lower limit assigned at construction
     */
    public double getLower() {
        return a;
    }

    /**
     * Getter for upper limit
     *
     * @return b the upper limit assigned at construction
     */
    public double getUpper() {
        return b;
    }

    /**
     * The ram type
     *
     * @return "RIGHT" if RRAM, "LEFT" if LRAM, "MID" if MRAM, or "" if
     * none(impossible to be none)
     */
    public String getRAM() {
        if (right) {
            return "RIGHT";
        }
        if (left) {
            return "LEFT";
        }
        if (mid) {
            return "MID";
        }
        return "";
    }

    /**
     * Getter for the output values
     *
     * @return the output values corresponding to getXValues()
     */
    public ArrayList getOutputs() {
        return outputValues;
    }

    /**
     * Getter for the input values
     *
     * @return the input values corresponding to getOutputs()
     */
    public ArrayList getXValues() {
        return xValues;
    }

    /**
     * Finds the integral using the slightly more accurate Euler's method
     *
     * @return the area of the integral
     * @throws UnknownFunctionException
     * @throws UnparsableExpressionException
     */
    public double getEulers() throws UnknownFunctionException, UnparsableExpressionException {
        double margin = .000005;
        double oldY = 0.0;
        for (double t = a; t <= b; t += margin) {
            double newY = oldY + calc(t) * margin;
            oldY = newY;
        }
        return oldY;
    }

    /**
     * Finds the slope given the right left and middle x values by averaging the
     * two slopes either side of the middle
     *
     * @param left
     * @param mid
     * @param right
     * @return approximate slope
     * @throws UnknownFunctionException
     * @throws UnparsableExpressionException
     */
    public double getSlope(double left, double mid, double right) throws UnknownFunctionException, UnparsableExpressionException {
        double rightSlope = (calc(right) - calc(mid)) / (right - mid);
        double leftSlope = (calc(mid) - calc(left)) / (mid - left);
        return (rightSlope + leftSlope) / 2;
    }

    /**
     * calculates second derivative
     *
     * @param left
     * @param midLeft
     * @param midRight
     * @param right
     * @return second derivative approx.
     * @throws UnknownFunctionException
     * @throws UnparsableExpressionException
     */
    public double get2nd(double left, double mid, double right) throws UnknownFunctionException, UnparsableExpressionException {
        return (getSlope(left * 2 - mid, left, mid) - getSlope(mid, right, right * 2 - mid)) / (left - right);
    }

    /**
     * A very close approximation to the theoretical(Exact) volume
     *
     * @return
     * @throws UnparsableExpressionException
     * @throws UnknownFunctionException
     */
    public double integrateTheoretical() throws UnparsableExpressionException, UnknownFunctionException {
        double sum = calc(a) + calc(b);
        double margin = .000005;
        for (double t = a + margin; t <= b - margin; t += margin) {
            sum += 2 * calc(t);
        }
        return margin / 2 * sum;
    }

    /**
     * Calculates the actual volume given the delta x and other info from the
     * integral
     *
     * @return the area/volume of the integral with the given slice size
     * @throws UnparsableExpressionException
     * @throws UnknownFunctionException
     */
    public double integrateActual() throws UnparsableExpressionException, UnknownFunctionException {
        double sum = 0;
        // adds to sum based on the RAM type
        if (right) {
            for (double t = getLower() + deltaX; t <= getUpper(); t += deltaX) {
                xValues.add(new Double(t));
                outputValues.add(new Double(deltaX * calc(t)));
                sum += calc(t);
            }
        }
        if (left) {
            for (double t = getLower(); t <= getUpper() - deltaX; t += deltaX) {
                xValues.add(new Double(t));
                outputValues.add(new Double(deltaX * calc(t)));
                sum += calc(t);
            }
        }
        if (mid) {
            for (double t = getLower() + (deltaX / 2); t <= getUpper() - (deltaX / 2) + .00025; t += deltaX) {
                xValues.add(new Double(t));
                outputValues.add(new Double(deltaX * calc(t)));
                sum += calc(t);
            }
        }
        // mulitplies because of equation
        sum *= deltaX;
        return sum;
    }

    /**
     * toString
     *
     * @return string of basic info
     */
    public String toString() {
        return "EQ:  " + equation
                + "\na:  " + a
                + "\nb:  " + b
                + "\nram " + left + mid + right
                + "\ndx " + deltaX;
    }

}
