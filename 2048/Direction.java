
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Direction {

    public final static int DOWN = 270;
    public final static int UP = 90;
    public final static int RIGHT = 0;
    public final static int LEFT = 180;
    private int direction;
    
    public Direction(int direction){
        this.direction = direction;
    }
    
    public Direction(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_DOWN:
                direction = DOWN;
                break;
            case KeyEvent.VK_UP:
                direction = UP;
                break;
            case KeyEvent.VK_RIGHT:
                direction = RIGHT;
                break;
            case KeyEvent.VK_LEFT:
                direction = LEFT;
                break;
            default:
                direction = - 1;
                break;
        }
    }

    public int getDirection(){
        return direction;
    }

}
