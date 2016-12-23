
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Block implements Comparable<Block> {

    private int row;
    private int column;

    private int value;
    private Score score;

    private ImageIcon image;

    public Block(int row, int column, Score score) {
        this.row = row;
        this.column = column;
        this.score = score;
        int n = (int) (Math.random() * 10);
        if (n < 7) {
            value = 2;
            image = new ImageIcon("2048\\2.png");
        } else {
            value = 4;
            image = new ImageIcon("2048\\4.png");
        }

    }

    public int getValue() {
        return value;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int column) {
        this.column = column;
    }

    public boolean isAddable(Block b) {
        return this.getValue() == b.getValue();
    }

    public void increment() {
        value += value;
        score.setScore(score.getScore() + value);

        switch (value) {
            case 4:
                image = new ImageIcon("2048\\4.png");
                break;
            case 8:
                image = new ImageIcon("2048\\8.png");
                break;
            case 16:
                image = new ImageIcon("2048\\16.png");
                break;
            case 32:
                image = new ImageIcon("2048\\32.png");
                break;
            case 64:
                image = new ImageIcon("2048\\64.png");
                break;
            case 128:
                image = new ImageIcon("2048\\128.png");
                break;
            case 256:
                image = new ImageIcon("2048\\256.png");
                break;
            case 512:
                image = new ImageIcon("2048\\512.png");
                break;
            case 1024:
                image = new ImageIcon("2048\\1024.png");
                break;
            case 2048:
                image = new ImageIcon("2048\\2048.png");
                break;
            case 4096:
                image = new ImageIcon("2048\\4096.png");
                break;

        }

    }

    public void draw(Graphics2D g, int x, int y) {
        Image image = this.image.getImage();
        g.drawImage(image, x, y, null);
    }

    @Override
    public int compareTo(Block b) {
        if (b != null) {
            return this.getValue() - b.getValue();
        } else {
            return -1;
        }
    }

}
