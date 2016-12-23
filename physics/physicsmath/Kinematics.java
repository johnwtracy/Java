package physicsmath;

public class Kinematics {

    private Kinematics() {
    }

    public static double getPeak(double vo, double theta) {
        return (Math.pow(getVY(vo, theta), 2)) / (2 * 9.8);
    }

    public static double getFar(double vo, double theta) {
        return 2 * (getVX(vo, theta) * getVY(vo, theta)) / 9.8;
    }

    public static double getVY(double vo, double theta) {
        theta *= 3.141592 / 180;
        return vo * Math.sin(theta);
    }

    public static double getVX(double vo, double theta) {
        theta *= 3.141592 / 180;
        return vo * Math.cos(theta);
    }

    public static double getDeltaX(double vo, double v, double t) {
        return ((vo + v) / 2) * t;
    }

    public static double getDeltaY(double vo, double a, double t) {
        return vo * t + .5 * a * t * t;
    }

    public static double getFinalVelocityY(double vo, double a, double t) {
        return vo + a * t;
    }

    public static double getFinalVelocityY(boolean b, double vo, double a, double dx) {
        return Math.sqrt(vo * vo + 2 * a * dx);
    }

}
