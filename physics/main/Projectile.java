package main;

import java.awt.*;

public class Projectile {

    private double x;
    private double y;

    public Projectile(int startX, int startY, double x, double y) {
        this.x = startX + x;
        this.y = (startY - 10) - y;
    }

    public void drawProjectile(Graphics g) {
        g.setColor(new Color(40, 40, 150));
        g.fillOval((int) x - 5, (int) y + 5, 10, 10);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

}
