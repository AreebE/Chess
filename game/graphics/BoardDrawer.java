package game.chess.graphics;

import game.chess.Piece;
import javax.swing.JComponent


public class BoardDrawer extends JComponent
{
    private Piece[][] board; 

    public BoardDrawer()
    {
        board = null;
    }

    public void assignPieces(Piece[][] board)
    {
        this.board = board;
    }

    @Override 
    public void paint(Graphics g)
    {
        
    }
}