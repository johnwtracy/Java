
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Handler implements KeyListener {

    private Board board;
    private boolean isPressed;

    public Handler(Board board) {
        this.board = board;
        isPressed = false;
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Used for pushed keys ( ie. Movement ) Usually needed in all games
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Game2048.hasStarted = true;
        }
        if (!isPressed) {
            isPressed = true;
            Direction d = new Direction(e);
            Block[][] blocks1 = board.getBlocks();
            if (board.canMove() && d.getDirection() != -1) {
                board.fill(d);
                board.conjoin(d);
                board.fill(d);
                board.updateAvaliability();
                Block[][] blocks2 = board.getBlocks();
                if (isSame(blocks1, blocks2)) {
                    board.addNew();
                }

            }
        }
    }

    public boolean isSame(Block[][] b1, Block[][] b2) {
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLUMNS; c++) {
                if (b1[r][c] != b2[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isPressed = false;
    }

}
