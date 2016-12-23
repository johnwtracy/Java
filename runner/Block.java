import javax.swing.*;
import java.awt.*;
public abstract class Block {
    /**
     * Instance varbales
     */
    public int width;
    public int height;
    public int xPos;
    public int yPos;
    protected boolean hit;
    
    /**
     * Constructor 
     */
    public Block( int x, int y ){
        xPos = x;
        yPos = y;
        width = 30;
        height = 30;
    } // end Block
    
    /**
     * Draw method for each block
     */
    public void drawSquare( Graphics2D g2 ){
        if ( !hit ) g2.fillRect( xPos, yPos, width, height );
        else xPos = - 40;
    } // end drawSquare
    
    /**
     * Allow for changing the boolean "hit"
     */
    public void setHit( boolean b ){
        hit = b;
    } // end setHit
    
} // end Block