public class Coin extends Block{
    /**
     * Basic constructor saying that it hasnt ben hit and passes the coordinates
     */
    public Coin( int x, int y ){
        super( x, y );
        hit = false;
    } // end Coin
} // end class