package game.chess.graphics;

import game.chess.Piece;
import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

// Chess Piece Res:
// https://www.freepnglogos.com/images/chess-39335.html 

public class BoardDrawer extends JComponent
{
    private Piece[][] board; 
    private static final int NUM_INTERVALS = 9;
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
        int height = getHeight();
        int width = getWidth();

        int heightInterval = height / NUM_INTERVALS;
        int widthInterval = width / NUM_INTERVALS;

        Graphics2D betterGraphic = (Graphics2D) g;
        betterGraphic.setStroke(new BasicStroke(5));

        for (int i = 0; i < NUM_INTERVALS; i++)
        {
            betterGraphic.drawLine()        
        }
    }
    
}