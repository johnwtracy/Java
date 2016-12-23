
import java.util.ArrayList;

public class Series {

    private Function RT;
    private Function LB;
    private ArrayList pointsInOrder;
    private boolean isDX;
    private double delta;
    private double a;
    private double b;
    private String ramType;

    private static int count = 0;

    public Series(Function RT, Function LB) {
        this.RT = RT;
        this.LB = LB;
        this.isDX = RT.getIsDX();
        this.delta = RT.getDelta();
        this.a = RT.getLower();
        this.b = RT.getUpper();
        this.ramType = RT.getRamType();
        pointsInOrder = new ArrayList();
        if (isDX) {
            calculatePointsDX();
        } else {
            calculatePointsDY();
        }
    }

    public void calculatePointsDX() {
        double topLeft = 0.0;
        double bottomLeft = 0.0;
        double topRight = 0.0;
        double bottomRight = 0.0;
        double nextTopLeft = 0.0;
        double start = a;
        double end = b;
        double change = delta;
        if (ramType.equalsIgnoreCase("right")) {
            start += delta;
        } else if (ramType.equalsIgnoreCase("left")) {
            start = start;
            end -= delta;
        } else {
            start += delta / 2;
            end -= delta / 2;
            change = -delta / 2;
        }
        for (double t = start; t <= end; t += delta) {
            topLeft = (RT.calc(t) >= LB.calc(t)) ? RT.calc(t) : LB.calc(t);
            topRight = topLeft;
            bottomLeft = (LB.calc(t) < RT.calc(t)) ? LB.calc(t) : RT.calc(t);
            bottomRight = bottomLeft;
            nextTopLeft = (RT.calc(t + delta) >= LB.calc(t + delta)) ? RT.calc(t + delta) : LB.calc(t + delta);
            if (ramType.equalsIgnoreCase("left")) {
                double startTL = t;
                pointsInOrder.add(new Point(startTL, topLeft));
                pointsInOrder.add(new Point(t + delta, topRight));
                pointsInOrder.add(new Point(startTL, topLeft));
                pointsInOrder.add(new Point(startTL, bottomLeft));
                pointsInOrder.add(new Point(t + delta, bottomRight));
                pointsInOrder.add(new Point(t + delta, topRight));
                if (t + delta < end) {
                    pointsInOrder.add(new Point(t + delta, nextTopLeft));
                }
            } else if (ramType.equalsIgnoreCase("right")) {
                double startTL = t - delta;
                pointsInOrder.add(new Point(startTL, topLeft));
                pointsInOrder.add(new Point(t, topRight));
                pointsInOrder.add(new Point(startTL, topLeft));
                pointsInOrder.add(new Point(startTL, bottomLeft));
                pointsInOrder.add(new Point(t, bottomRight));
                pointsInOrder.add(new Point(t, topRight));
                if (t < end) {
                    pointsInOrder.add(new Point(t, nextTopLeft));
                }
            } else {
                double startTL = t + change;
                pointsInOrder.add(new Point(startTL, topLeft));
                pointsInOrder.add(new Point(startTL + delta, topRight));
                pointsInOrder.add(new Point(startTL, topLeft));
                pointsInOrder.add(new Point(startTL, bottomLeft));
                pointsInOrder.add(new Point(startTL + delta, bottomRight));
                pointsInOrder.add(new Point(startTL + delta, topRight));
                if (t + Math.abs(change) < end) {
                    pointsInOrder.add(new Point(startTL + delta, nextTopLeft));
                }
            }
        }
    }

    public void calculatePointsDY() {
        double topLeft = 0.0;
        double bottomLeft = 0.0;
        double topRight = 0.0;
        double bottomRight = 0.0;
        double nextTopLeft = 0.0;
        double start = b;
        double end = a;
        double change = delta;
        if (ramType.equalsIgnoreCase("right")) {
            start = start;
        } else if (ramType.equalsIgnoreCase("left")) {
            start -= delta;
            end = end;
        } else {
            start -= delta / 2;
            end += delta / 2;
            change = delta / 2;
        }
        for (double t = start; t >= end; t -= delta) {
            topLeft = (LB.calc(t) <= RT.calc(t)) ? LB.calc(t) : RT.calc(t);
            bottomLeft = topLeft;
            topRight = (RT.calc(t) > LB.calc(t)) ? RT.calc(t) : LB.calc(t);
            bottomRight = topRight;
            nextTopLeft = (LB.calc(t - delta) <= RT.calc(t - delta)) ? LB.calc(t - delta) : RT.calc(t - delta);
            if (ramType.equalsIgnoreCase("left")) {
                double startL = t + change;

                pointsInOrder.add(new Point(topLeft, startL));
                pointsInOrder.add(new Point(bottomLeft, t));
                pointsInOrder.add(new Point(topLeft, startL));
                pointsInOrder.add(new Point(topRight, startL));
                pointsInOrder.add(new Point(bottomRight, t));
                pointsInOrder.add(new Point(bottomLeft, t));
                if (t < end) {
                    pointsInOrder.add(new Point(nextTopLeft, t));
                }
            } else if (ramType.equalsIgnoreCase("right")) {
                double startL = t;
                pointsInOrder.add(new Point(topLeft, startL));
                pointsInOrder.add(new Point(bottomLeft, t - change));
                pointsInOrder.add(new Point(topLeft, startL));
                pointsInOrder.add(new Point(topRight, startL));
                pointsInOrder.add(new Point(bottomRight, t - change));
                pointsInOrder.add(new Point(bottomLeft, t - change));
                if (t < end) {
                    pointsInOrder.add(new Point(nextTopLeft, t - change));
                }
            } else {
                double startL = t + change;
                pointsInOrder.add(new Point(topLeft, startL));
                pointsInOrder.add(new Point(bottomLeft, t - change));
                pointsInOrder.add(new Point(topLeft, startL));
                pointsInOrder.add(new Point(topRight, startL));
                pointsInOrder.add(new Point(bottomRight, t - change));
                pointsInOrder.add(new Point(bottomLeft, t - change));
                if (t < end) {
                    pointsInOrder.add(new Point(nextTopLeft, t - change));
                }
            }
        }
    }

    public String getAllPoints() {
        String points = "";
        for (int i = 0; i < pointsInOrder.size(); i++) {
            String add = "; ";
            if (i == pointsInOrder.size() - 1) {
                add = "";
            }
            points += pointsInOrder.get(i) + add;
        }
        return points;
    }

    public String toString() {
        return "[PointSeries" + (count++) + "]\n"
                + "FillColor = clBlack\n"
                + "LineColor = clBlack\n"
                + "Size = 1\n"
                + "Style = 0\n"
                + "LineSize = 2\n"
                + "LineStyle = 0\n"
                + "LabelPosition = 1\n"
                + "PointCount = " + pointsInOrder.size() + "\n"
                + "Points = " + getAllPoints();
    }

}
