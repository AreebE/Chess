package game.graphics;

import game.chess.Piece;
import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.border.EmptyBorder;


// Chess Piece Res:
// https://www.freepnglogos.com/images/chess-39335.html 

public class BoardDrawer extends JComponent
{
    private Piece[][] board; 

    private static final Color LIGHT_TILE = new Color(255, 214, 166);
    private static final Color DARK_TILE = new Color(183, 134, 78);

    private static final int MARGIN_RATIO = 40;
    private static final int NUM_INTERVALS = 9;
    public BoardDrawer()
    {
    	EmptyBorder border = new EmptyBorder(100, 100, 100, 100);
//    	this.setBorder(border);
        board = null;
        // ee
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

        int margin = height / MARGIN_RATIO;
        int heightInterval = (height - 2 * margin) / NUM_INTERVALS;
        int widthInterval = (width - 2 * margin) / NUM_INTERVALS;

//        System.out.println(margin * 2);
        Graphics2D betterGraphic = (Graphics2D) g;
        betterGraphic.setStroke(new BasicStroke(margin));
        betterGraphic.setColor(Color.BLACK);
        betterGraphic.fillRect(0, 0, width, height);
        System.out.println("\n__________________________________\n" + width + " ; " + height + " - " + margin);
//        betterGraphic.drawRect(0, 0, width, height);
        boolean paintDarkTile = false;
        int yStart = margin; 
        for (int row = 0; row < NUM_INTERVALS; row++)
        {
            int startX = margin;
            for (int col = 0; col < NUM_INTERVALS; col++)
            {
                betterGraphic.setColor((paintDarkTile)? DARK_TILE: LIGHT_TILE);
                betterGraphic.fillRect(startX, yStart, widthInterval, heightInterval);
                paintDarkTile = !paintDarkTile;
                startX += widthInterval;
            }
            
            System.out.println(startX + ", " + (yStart + heightInterval));
            yStart += heightInterval;
        }
    }
}