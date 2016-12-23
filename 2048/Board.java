
import java.util.*;

public class Board {

    private ArrayList<Integer> avaliableRows;
    private ArrayList<Integer> avaliableCols;
    private Block[][] blocks;
    private Score score;

    public static final int COLUMNS = 4;
    public static final int ROWS = 4;

    public Board(Score score) {
        avaliableRows = new ArrayList<Integer>();
        avaliableCols = new ArrayList<Integer>();
        blocks = new Block[ROWS][COLUMNS];
        this.score = score;
        for (int i = 0; i < ROWS; i++) {
            avaliableRows.add(i);
        }
        for (int i = 0; i < COLUMNS; i++) {
            avaliableCols.add(i);
        }
        addNew();
        addNew();
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public boolean isOccupied(int r, int c) {
        if (blocks[r][c] == null) {
            return false;
        } else {
            return true;
        }
    }

    public void updateAvaliability() {
        avaliableRows = new ArrayList<Integer>();
        avaliableCols = new ArrayList<Integer>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (!isOccupied(r, c)) {
                    avaliableRows.add(r);
                    avaliableCols.add(c);
                }
            }
        }
    }

    public boolean canMove() {
        updateAvaliability();
        if (avaliableRows.size() == 0 && avaliableCols.size() == 0) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLUMNS; c++) {
                    Block compare = blocks[r][c];
                    Block b1 = null;
                    Block b2 = null;
                    if (r < ROWS - 1) {
                        b1 = blocks[r + 1][c];
                    }
                    if (c < COLUMNS - 1) {
                        b2 = blocks[r][c + 1];
                    }
                    if ((compare != null && b1 != null) && compare.getValue() == b1.getValue()) {
                        return true;
                    }
                    if ((compare != null && b2 != null) && compare.getValue() == b2.getValue()) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }

    }

    public Block getNewBlock() {
        Block b;
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (this.blocks[r][c] == null) {
                    b = new Block(r, c, score);
                    blocks.add(b);
                }

            }
        }
        int n = (int) (Math.random() * blocks.size());
        if (blocks.size() != 0) {
            return blocks.get(n);
        } else {
            return null;
        }
    }

    public void addNew() {
        Block b = getNewBlock();
        if (b != null) {
            blocks[b.getRow()][b.getCol()] = b;
        }
    }

    public void fill(Direction d) {
        int direction = d.getDirection();
        if (direction == Direction.UP || direction == Direction.DOWN) {
            for (int c = 0; c < COLUMNS; c++) {
                if (direction == Direction.UP) {
                    for (int r = 0; r < ROWS - 1; r++) {
                        Block b = blocks[r][c];
                        int index = r;
                        while (b == null && index < ROWS - 1) {
                            blocks[r][c] = blocks[index + 1][c];
                            blocks[index + 1][c] = null;
                            b = blocks[r][c];
                            index++;
                        }
                        if (blocks[r][c] != null) {
                            blocks[r][c].setRow(r);
                            blocks[r][c].setCol(c);
                        }
                    }
                } else {
                    for (int r = ROWS - 1; r >= 1; r--) {
                        Block b = blocks[r][c];
                        int index = r;
                        while (b == null && index > 0) {
                            blocks[r][c] = blocks[index - 1][c];
                            blocks[index - 1][c] = null;
                            b = blocks[r][c];
                            index--;
                        }
                        if (blocks[r][c] != null) {
                            blocks[r][c].setRow(r);
                            blocks[r][c].setCol(c);
                        }
                    }
                }
            }
        } else if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            for (int r = 0; r < ROWS; r++) {
                if (direction == Direction.LEFT) {
                    for (int c = 0; c < COLUMNS - 1; c++) {
                        Block b = blocks[r][c];
                        int index = c;
                        while (b == null && index < COLUMNS - 1) {
                            blocks[r][c] = blocks[r][index + 1];
                            blocks[r][index + 1] = null;
                            b = blocks[r][c];
                            index++;
                        }
                        if (blocks[r][c] != null) {
                            blocks[r][c].setRow(r);
                            blocks[r][c].setCol(c);
                        }
                    }
                } else {
                    for (int c = COLUMNS - 1; c >= 1; c--) {
                        Block b = blocks[r][c];
                        int index = c;
                        while (b == null && index > 0) {
                            blocks[r][c] = blocks[r][index - 1];
                            blocks[r][index - 1] = null;
                            b = blocks[r][c];
                            index--;
                        }
                        if (blocks[r][c] != null) {
                            blocks[r][c].setRow(r);
                            blocks[r][c].setCol(c);
                        }
                    }
                }
            }
        }
    }

    public void conjoin(Direction d) {
        int direction = d.getDirection();
        if (direction == Direction.UP || direction == Direction.DOWN) {
            for (int c = 0; c < COLUMNS; c++) {
                for (int r = 0; r < ROWS - 1; r++) {
                    Block b1;
                    Block b2;
                    if (direction == Direction.UP) {
                        b1 = blocks[r][c];
                        b2 = blocks[r + 1][c];
                    } else {
                        b1 = blocks[(ROWS - 1) - r][c];
                        b2 = blocks[(ROWS - 1) - (r + 1)][c];
                    }
                    if ((b1 != null && b2 != null) && (b1.getValue() == b2.getValue())) {
                        if (direction == Direction.UP) {
                            b1.increment();
                            blocks[r + 1][c] = null;
                        } else if (direction == Direction.DOWN) {
                            b1.increment();
                            blocks[(ROWS - 1) - (r + 1)][c] = null;
                        }
                    }
                }
            }
        } else if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLUMNS - 1; c++) {
                    Block b1;
                    Block b2;
                    if (direction == Direction.LEFT) {
                        b1 = blocks[r][c];
                        b2 = blocks[r][c + 1];
                    } else {
                        b1 = blocks[r][(COLUMNS - 1) - c];
                        b2 = blocks[r][(COLUMNS - 1) - (c + 1)];
                    }
                    if ((b1 != null && b2 != null) && (b1.getValue() == b2.getValue())) {
                        if (direction == Direction.LEFT) {
                            b1.increment();
                            blocks[r][c + 1] = null;
                        } else if (direction == Direction.RIGHT) {
                            b1.increment();
                            blocks[r][(COLUMNS - 1) - (c + 1)] = null;
                        }
                    }
                }
            }
        }
    }

}
