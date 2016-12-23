
public class Tester {

    public static void main(String[] args) {
        Board board = new Board(new Score());
        Integer a = 128;
        Integer b = 128;
        System.out.println(a.equals( b));
        /*
        while (true) {
            
            Block[][] blocks = board.getBlocks();
            for (Block[] bA : blocks) {
                for (Block b : bA) {
                    if (b != null) {
                        System.out.print(b.getValue() + "\t");
                    } else {
                        System.out.print("0" + "\t");
                    }
                }
                System.out.println();
            }
            System.out.println();
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            Direction d = new Direction(Direction.RIGHT);
            if (board.canMove()) {
                board.fill(d);
                board.conjoin(d);
                board.addNew();
            }
            System.out.print('\r');
            
           
        }
        */
        
    }

}
