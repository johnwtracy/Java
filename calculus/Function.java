
import de.congrace.exp4j.*;
import javax.swing.JOptionPane;

public class Function {

    private double a;
    private double b;
    private double delta;
    private boolean isDX;
    private String eqS;
    private String type;
    private Calculable eq;

    private static int count = 0;

    public Function(double a, double b, double delta, boolean isDX, String eqS, String type) {
        this.a = a;
        this.b = b;
        this.isDX = isDX;
        this.eqS = fixOperands(eqS);
        this.delta = delta;
        this.type = type;
        try {
            String s = "y";
            if (isDX) {
                s = "x";
            }
            eq = new ExpressionBuilder(eqS).withVariable(s, 0.0).build();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not create equation.");
        }
    }

    public double calc(double t) {
        String s = "y";
        if (isDX) {
            s = "x";
        }
        eq.setVariable(s, t);
        return eq.calculate();
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

    public double getLower() {
        return a;
    }

    public double getUpper() {
        return b;
    }

    public double getDelta() {
        return delta;
    }

    public String getRamType() {
        return type;
    }

    public boolean getIsDX() {
        return isDX;
    }

    public String toString() {
        if (isDX) {
            return "[Func" + (count++ - 1) + "]\n"
                    + "FuncType = 0\n"
                    + "y = " + eqS.trim() + "\n"
                    + "From = " + a + "\n"
                    + "To = " + b + "\n"
                    + "Color = clBlack\n"
                    + "Size = 2\n";
        } else {
            return "[Relation" + (count++ - 1) + "]\n"
                    + "Relation = x=" + eqS + "\n"
                    + "Constraints = " + b + "> y >" + a + "\n"
                    + "Style = 5\n"
                    + "Color = clBlack\n"
                    + "Size = 2\n";
        }
    }

}
