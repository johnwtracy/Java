/**
 * Imports a few packages for the applet graphics and the KeyListener
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;
import java.util.ArrayList;

public class Runner2D extends Applet implements Runnable, KeyListener {
    
    /**
     * instance variables for the Runner2D class
     */
    private boolean check;
    private boolean p1Hit;
    private boolean p2Hit;
    private boolean duringPower;
    private boolean duringPower1;
    private boolean twoPlayer;
    private boolean startGame;
    private Thread t;
    private ArrayList< Block > block;
    private long end;
    private long powerStart;
    private long powerStart1;
    private long powerEnd;  
    private long powerEnd1;
    private int dificulty;
    private int dx, dy, dx1, dy1;
    private int blockCount;
    private int score;
    private int score1;
    private int helper;
    private int currentX1;
    private int currentY1;
    private int currentX;
    private int currentY;
    private int width = 800, height = 500;
    
    /**
     *  Acts as the constructer under the Runnable interface.
     *  Initializes the variables and instantiates the objects.
     */
    public void init(){ // acts as the constructer under the Runnable interface
        score = 0;
        score1 = 0;
        dificulty = 5;
        dx = 0;
        dy = 0;
        dx1 = 0;
        dy1 = 0;
        blockCount = 0;
        helper = 1;
        currentX1 = 0;
        currentY1 = 50;
        currentX = 0;
        currentY = height / 2;
        p1Hit = false;
        p2Hit = false;
        startGame = false;
        check = true;
        duringPower = false;
        twoPlayer = false;
        block = new ArrayList< Block >();
        addKeyListener( this );
    } // end init
    
    /**
     * Very basic modification on the start method from 
     * the Runnable interface.
     */
    public void start(){ 
        t = new Thread( this );
        t.start();
    } // end init
    
    /**
     * Uses the run method in the Runnable interface
     */
    public void run(){ 
        while ( true ){
            move();
            if ( !twoPlayer ) check = check( currentX, currentY ); // checks the first player
            else checkTwoPlayer( currentX, currentY, currentX1, currentY1 );
            if ( duringPower ) { // negates any hit if the power is activated
                p1Hit = false;
                powerEnd = System.currentTimeMillis();
                if ( powerEnd - powerStart >= 4000 ) duringPower = false; // ends the power
            } // end else
            if ( duringPower1 ){
                p2Hit = false;
                powerEnd1 = System.currentTimeMillis();
                if ( powerEnd1 - powerStart1 >= 4000 ) duringPower1 = false; // ends the power
            } // end if
            if ( p1Hit && p2Hit ) check = false;
            repaint(); // updates the screen
            if ( !check ) break; // breaks once there is a hit
            
            try {
                t.sleep( 35 ); // halts the thread for 35 milliseconds
            } // end try
            catch ( InterruptedException e ) {} // must be thrown
        } // end while
    } // end run
    
    /**
     * Smilpy calls on the update method
     */
    public void paint( Graphics g ){
        update( g );
    } // end paint
    
    /**
     * Modifies the update method in the Applet superclass
     */
    public void update( Graphics g ){
        g.drawImage( drawImage(), 0, 0, this );
    } // end update
    
    public Image drawImage(){
        Image offScreen = new BufferedImage(width, height, Image.SCALE_SMOOTH);
        Graphics2D g2 = ( Graphics2D ) offScreen.getGraphics(); // casts to 2d graphics
        g2.setBackground( Color.black );
        g2.clearRect( 0, 0, width, height ); // sets window size
        double manageTime = ( int ) ( getElapsedTime() % ( 5000 / ( ( Math.pow( blockCount, .40 ) + helper ) * Math.pow( dificulty, .55 ) ) ) ); 
        // algorithm for making the game get harder asyou go
        if ( check && startGame ){ // if the blocks have not hit the cursor and if the game has been started from the menu=
            /*
             * handles the blocks
             */
            if ( manageTime >= 0 && manageTime <= 35 ) { // adds a new block the the ArrtayList at a rate that will increase with the amount of blacks that have come
                int random1 = ( int ) ( Math.random() * 200 );
                int random = ( int ) ( Math.random() * height );
                Block b1;
                if ( random1 < 4 ) b1 = new Invincibility( width, random  ); // invinsibility block
                else if ( random1 >= 5 && random1 <= 14 ) b1 = new Coin( width, random ); // coin block
                else b1 = new Regular( width, random ); // regular hostile block
                block.add( b1 ); // adds block regarless of type
                blockCount++; // adds to the block count
                if ( !p1Hit ) score++; // increments the score
                if ( !p2Hit ) score1++;
                helper = 0;
            } // end if
            for ( int i = 0; i < blockCount; i++ ){ // "moves" the block across the screen acording to difficulty
                block.get( i ).xPos -= ( int ) ( 2 * Math.pow( dificulty, .65 ) );
            } // end for Cursor
            for ( Block b : block ){ // chooses the color to draw for each block
                if ( b.xPos >= -30 ) { // makes sure only to draw the visible blocks
                    if ( b instanceof Invincibility ) g2.setColor( Color.red ); 
                    if ( b instanceof Regular ) {
                        if ( duringPower && !twoPlayer ) g2.setColor( Color.white );
                        else g2.setColor( Color.blue );
                        if ( twoPlayer ){
                            if( duringPower && !duringPower1 )g2.setColor( Color.orange );
                            else if ( duringPower && duringPower1 ) g2.setColor( Color.white );
                            else if ( !duringPower && duringPower1 ) g2.setColor( Color.magenta ); 
                            else g2.setColor( Color.blue );
                        } // END IF
                    } // end if
                    if ( b instanceof Coin ) g2.setColor( Color.yellow );
                    b.drawSquare( g2 ); // draws the block
                } // end if
            } // end for 
            
            /*
             * handles the player's cursor or two players' cursors
             */
            if ( twoPlayer ){
                if ( !p1Hit ){
                    g2.setColor( Color.green );
                    g2.fillOval( currentX, currentY, 15, 15 ); // draws green cursor
                } // end if
                if ( !p2Hit ){
                    g2.setColor( Color.red );
                    g2.fillOval( currentX1, currentY1, 15, 15 );
                } // end if
            } // end if
            else{
                g2.setColor( Color.green );
                g2.fillOval( currentX, currentY, 15, 15 ); // draws green cursor
            } // end else
            if ( !twoPlayer ){
                g2.setColor( Color.GREEN );
                g2.drawString( "Score:  " + score , width - 100, 40 ); // displays score
            }
            if ( twoPlayer ){ // displays second player's score
                g2.setColor( Color.GREEN );
                g2.drawString( "Player 1 Score:  " + score , width - 140, 40 ); // displays score
                g2.setColor( Color.YELLOW );
                g2.drawString( "Player 2 Score:  " + score1 , width - 140, 60 ); // displays score
            } // end if
            if ( duringPower && !twoPlayer ){ // displays how much longer the powerup has
                g2.setColor( Color.cyan );
                g2.drawString( "Power Duration:  " + ( 4500 - ( powerEnd - powerStart  ) ) / 1000, 20, 20 );
            } // end if
            if ( twoPlayer ){
                if ( duringPower ){ // displays how much longer the powerup has
                    g2.setColor( Color.cyan );
                    g2.drawString( "Player 1 Power Duration:  " + ( 4500 - ( powerEnd - powerStart  ) ) / 1000, 20, 20 );
                } // end if
                if ( duringPower1 ){
                    g2.setColor( Color.PINK );
                    g2.drawString( "Player 2 Power Duration:  " + ( 4500 - ( powerEnd1 - powerStart1  ) ) / 1000, 20, 40 );
                } // end if
            } // end if
            if ( getElapsedTime() < 15000 && twoPlayer ){
                g2.setColor( Color.GREEN );
                g2.drawString( "Player 1 is   GREEN", 50, 50 );
                g2.setColor( Color.RED );
                g2.drawString( "Player 2 is   RED", 200, 50 );
                g2.setColor( Color.WHITE );
                g2.drawString( "When blocks are   WHITE   both players may pass through it", 50, 80 );
                g2.setColor( Color.ORANGE );
                g2.drawString( "When blocks are   ORANGE   Player 1 may pass through it", 50, 110 );
                g2.setColor( Color.MAGENTA );
                g2.drawString( "When blocks are   MANGENTA   Player 2 may pass through it", 50, 140 );
            } // end if
        } // end if
        else if ( !check ){ // if the curso has hit something 
            g2.setColor( Color.CYAN );
            g2.drawString( "GAME OVER", width / 2, height / 2 );
            if ( !twoPlayer ){
                g2.setColor( Color.GREEN );
                g2.drawString( "Score:   " + score, width / 2, height / 2 + 20 );
            } // end if
            else{
                g2.setColor( Color.GREEN );
                g2.drawString( "Player 1 Score:   " + score, width / 2, height / 2 + 20 );
                g2.setColor( Color.RED );
                g2.drawString( "Player 2 Score:   " + score1, width / 2, height / 2 + 40 );
            } // end else
            
        } // end else
        if ( !startGame ){ // basic menu screen
            g2.setColor( Color.CYAN );
            g2.drawString( "**DOUBLE   CLICK   SCREEN**", 180, 50 );
            g2.setColor( Color.green );
            g2.drawString( "Use arrow keys or WASD to navigate. Good Luck!", 130, 100 );
            g2.setColor( Color.green );
            g2.drawString( "Enter 1-3 for difficulty ( Default is 2 ).", 130, 140 );
            g2.setColor( Color.yellow );
            g2.drawString( "Yellow blocks will boost score by 30 points.", 130, 170 );
            g2.setColor( Color.red );
            g2.drawString( "Red blocks will give invincibility for 4 seconds.", 130, 200 );
            g2.setColor( Color.green );
            g2.drawString( "Press  SPACE  to begin.", 195, 260 );
            g2.setColor( Color.green );
            g2.drawString( "Press  ENTER  for two player mode.(**WARNING: OCCASIONAL GLITCHES**)", 130, 290 );
        } // end if
        return offScreen;
    }
    
    /**
     * Defines the KeyPressed abstract method in the KeyListener Interface
     */
    public void keyPressed( KeyEvent e ) {
        int key = e.getKeyCode();
        if ( !twoPlayer ){ // just for single player
            if ( key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A )dx = -7;
            if ( key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) dx = 5;
            if ( key == KeyEvent.VK_UP || key == KeyEvent.VK_W) dy = -5;
            if ( key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) dy = 5;
        } // end if
        else { // for two player
            if ( key == KeyEvent.VK_LEFT )dx = -6;
            if ( key == KeyEvent.VK_RIGHT ) dx = 6;
            if ( key == KeyEvent.VK_UP ) dy = -6;
            if ( key == KeyEvent.VK_DOWN ) dy = 6;
            if ( key == KeyEvent.VK_A )dx1 = -6;
            if ( key == KeyEvent.VK_D) dx1 = 6;
            if ( key == KeyEvent.VK_W) dy1 = -6;
            if ( key == KeyEvent.VK_S) dy1 = 6;
        } // end if
        if ( !startGame ){ // the menu screen posible keystrokes
            if ( key == KeyEvent.VK_1 ) dificulty = 2;
            if ( key == KeyEvent.VK_2 ) dificulty = 5;
            if ( key == KeyEvent.VK_3 ) dificulty = 10;
            if ( key == KeyEvent.VK_ENTER ) { // starts two player mode
                if ( twoPlayer ) twoPlayer = false;
                else twoPlayer = true;
                startGame = true;
            } // end if
            if ( key == KeyEvent.VK_SPACE ) startGame = true;
        } // end if
    } // end keyPressed
    
    /**
     * Defines the KeyReleased abstract method in the KeyListener Interface
     */
    public void keyReleased( KeyEvent e ){
        int key = e.getKeyCode();
        if ( !twoPlayer ){ // for singlePlayer
            if ( key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)dx = 0;
            if ( key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D ) dx = 0;
            if ( key == KeyEvent.VK_UP || key == KeyEvent.VK_W) dy = 0;
            if ( key == KeyEvent.VK_DOWN ||key == KeyEvent.VK_S) dy = 0;
        } // end if
        if ( twoPlayer ){ // for two player
            if ( key == KeyEvent.VK_LEFT )dx = 0;
            if ( key == KeyEvent.VK_RIGHT ) dx = 0;
            if ( key == KeyEvent.VK_UP ) dy = 0;
            if ( key == KeyEvent.VK_DOWN ) dy = 0;
            if ( key == KeyEvent.VK_A )dx1 = 0;
            if ( key == KeyEvent.VK_D) dx1 = 0;
            if ( key == KeyEvent.VK_W) dy1 = 0;
            if ( key == KeyEvent.VK_S) dy1 = 0;
        } // end if
    } // end keyReleased
    
    /**
     * Necessary to satisfy the interface
     */
    public void keyTyped( KeyEvent e ){}
    
    /**
     * Moves the cursor(s) by the assigned value
     */
    public void move(){
        currentX += dx;         // increments position by speed( "moves" cursor
        currentY += dy;
        /*
         * checks to see if the cursor is in the window
         */
        if ( currentX <= 0 ) currentX = 0;
        if ( currentX >= width - 15 ) currentX = width - 15;
        if ( currentY <= 0 ) currentY = 0;
        if ( currentY >= height - 15 ) currentY = height - 15;
        /*
         * same but for second player
         */
        if ( twoPlayer ){ // 
            currentX1 += dx1;
            currentY1 += dy1;
            if ( currentX1 <= 0 ) currentX1 = 0;
            if ( currentX1 >= width - 15 ) currentX1 = width - 15;
            if ( currentY1 <= 0 ) currentY1 = 0;
            if ( currentY1 >= height - 15 ) currentY1 = height - 15;
        } // end if
    } // end move
    
    /**
     * Checks the x and y coordinates for a hit. 
     * Checks from the top left corner of the cursor
     */
    public boolean check( int x, int y ){
        for ( Block b : block ){ // checks every block
            if ( b.xPos > -40 ){ // only checks relavant blocks that the cursor can actually hit
                if ( ( x >= b.xPos && x <= b.xPos + b.width ) || ( x + 15 >= b.xPos && x + 15 <= b.xPos + b.width ) ){ // checks for an overlaping x position
                    if ( ( y >= b.yPos && y <= b.yPos + b.height ) || ( y + 15 >= b.yPos && y + 15 <= b.yPos + b.height ) ){ // checks for an overlapping y position
                        if ( b instanceof Regular && !duringPower ) return false; // changes to a hit
                        if ( b instanceof Invincibility ){ // starts the powerup when hit
                            duringPower = true;
                            powerStart = System.currentTimeMillis();
                            b.setHit( true );
                            break;
                        } // end if
                        if ( b instanceof Coin ) { // adds to score when hit
                            b.setHit( true );
                            score += 30;
                            break;
                        } // end if
                    } // end if
                } // end if
            } // end if
        } // end for
        return true;
    }  // end check()
    
    /**
     * 
     * Checks the x and y coordinates for a hit. 
     * Checks from the top left corner of the cursor
     */
    public void checkTwoPlayer( int x, int y, int x1, int y1 ){
        for ( Block b : block ){ // checks every block
            if ( b.xPos > -40 && !p1Hit ){ // only checks relavant blocks that the cursor can actually hit
                if ( ( x >= b.xPos && x <= b.xPos + b.width ) || ( x + 15 >= b.xPos && x + 15 <= b.xPos + b.width ) ){ // checks for an overlaping x position
                    if ( ( y >= b.yPos && y <= b.yPos + b.height ) || ( y + 15 >= b.yPos && y + 15 <= b.yPos + b.height ) ){ // checks for an overlapping y position
                        if ( b instanceof Regular && !duringPower ){
                            p1Hit = true;
                        } // end if
                        if ( b instanceof Invincibility ){ // starts the powerup when hit
                            duringPower = true;
                            powerStart = System.currentTimeMillis();
                            b.setHit( true );
                            break;
                        } // end if
                        if ( b instanceof Coin ) { // adds to score when hit
                            b.setHit( true );
                            score += 30;
                            break;
                        } // end if
                    } // end if
                } // end if
            } // end if
        } // end for
        for ( Block b : block ){ // checks every block
            if ( b.xPos > -40 && !p2Hit){ // only checks relavant blocks that the cursor can actually hit
                if ( ( x1 >= b.xPos && x1 <= b.xPos + b.width ) || ( x1 + 15 >= b.xPos && x1 + 15 <= b.xPos + b.width ) ){ // checks for an overlaping x position
                    if ( ( y1 >= b.yPos && y1 <= b.yPos + b.height ) || ( y1 + 15 >= b.yPos && y1 + 15 <= b.yPos + b.height ) ){ // checks for an overlapping y position
                        if ( b instanceof Regular && !duringPower1 ) {
                            p2Hit = true; // changes to a hit
                        } // end if
                        if ( b instanceof Invincibility ){ // starts the powerup when hit
                            duringPower1 = true;
                            powerStart1 = System.currentTimeMillis();
                            b.setHit( true );
                            break;
                        } // end if
                        if ( b instanceof Coin ) { // adds to score when hit
                            b.setHit( true );
                            score1 += 30;
                            break;
                        } // end if
                    } // end if
                } // end if
            } // end if
        } // end for
    }  // end check()
    
    /**
     * Gets the total elapsed time from the start of the applet
     */
    public long getElapsedTime(){
        return System.currentTimeMillis() - start;
    } // end getElapsedTime
    
    private final long start = System.currentTimeMillis(); // sets the exact time for the start up
} // end Runner2D


























