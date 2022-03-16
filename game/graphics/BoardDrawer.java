package game.graphics;

import game.chess.Piece;
import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.border.EmptyBorder;

import game.chess.Logic;
import game.chess.Piece;
// Chess Piece Res:
// https://www.freepnglogos.com/images/chess-39335.html 

public class BoardDrawer extends JComponent
{
	
	public interface Updater 
	{
		public Piece getPiece(int row, int col);
	}
	
	private String[][] possibleOptions;
	private Updater updater;

    private static final Color LIGHT_TILE = new Color(255, 214, 166);
    private static final Color DARK_TILE = new Color(183, 134, 78);
    private static final Color SELECTED = new Color(120, 183, 87);
    private static final Color BLACK = new Color(0, 0, 0);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final int MARGIN_RATIO = 40;
    private static final int NUM_INTERVALS = 9;
    
    public BoardDrawer(Updater u)
    {
//    	EmptyBorder border = new EmptyBorder(1, 1, 1, 1);
//    	this.setBorder(border);
        // ee
    	this.updater = u;
    	possibleOptions = new String[0][0];
    }

    public void assignOptions(String[][] options)
    {
    	this.possibleOptions = options;
    }

    @Override 
    public void paint(Graphics g)
    {
        int height = getHeight();
        int width = getWidth();

        int margin = height / MARGIN_RATIO;
        int heightInterval = (height - 2 * margin) / NUM_INTERVALS;
        int widthInterval = (width - 2 * margin) / NUM_INTERVALS;
    	Font f = new Font(Font.MONOSPACED, Font.BOLD, widthInterval);

//        System.out.println(margin * 2);
        Graphics2D betterGraphic = (Graphics2D) g;
        betterGraphic.setFont(f);
        betterGraphic.setStroke(new BasicStroke(margin));
        betterGraphic.setColor(Color.BLACK);
        betterGraphic.fillRect(0, 0, width, height);
//        System.out.println(this.getBorder().getBorderInsets(this));
        System.out.println("\n__________________________________\n" + width + " ; " + height + " - " + margin);
//        betterGraphic.drawRect(0, 0, width, height);
        boolean paintDarkTile = false;
        
        // print the letters
        int yStart = margin + 3 * heightInterval / 4;
        int startX = margin * 2 + widthInterval;
        betterGraphic.setColor(Color.WHITE);
        for (char col = 'a'; col <= 'h'; col++)
        {
        	betterGraphic.drawString(col + "", startX, yStart);
        	startX += widthInterval;
        }
        startX = margin;
        yStart += heightInterval;
        for (char row = '1'; row <= '8'; row++)
        {
        	betterGraphic.drawString(row + "", startX, yStart);
        	yStart += heightInterval;
        }
        
        // print the pieces
        yStart = margin + heightInterval; 
        for (int row = 1; row < NUM_INTERVALS; row++)
        {
            startX = margin + widthInterval;
            for (int col = 1; col < NUM_INTERVALS; col++)
            {
            	int effectiveCol = col - 1;
            	int effectiveRow = row - 1;
                betterGraphic.setColor((paintDarkTile)? DARK_TILE: LIGHT_TILE);
                String curPoint = Logic.toCoordinates(effectiveCol, effectiveRow);
                for (String[] coord: possibleOptions)
                {
                	if (coord[0].equals(curPoint))
                	{
                		betterGraphic.setColor(SELECTED);
                		break;
                	}
                }
                betterGraphic.fillRect(startX, yStart, widthInterval, heightInterval);
                paintDarkTile = !paintDarkTile;
                Piece p = updater.getPiece(effectiveRow, effectiveCol);
                if (p != null)
                {
                	betterGraphic.setColor(p.getColor().equals(Piece.Color.BLACK)? BLACK: WHITE);
                	String letter = "";
                	switch (p.getType())
                	{
                		case PAWN:
                			letter = "p";
                			break;
                		case ROOK:
                			letter = "R";
                			break;
                		case KNIGHT:
                			letter = "H";
                			break;
                		case BISHOP:
                			letter = "B";
                			break;
                		case QUEEN:
                			letter = "Q";
                			break;
                		case KING:
                			letter = "K";
                			break;
                	}
                	betterGraphic.drawString(letter, startX + margin, yStart + heightInterval - 3 / 2 * margin);
                }
                startX += widthInterval;
            }
            paintDarkTile = !paintDarkTile;
//            System.out.println(startX + ", " + (yStart + heightInterval));
            yStart += heightInterval;
        }
    }
    
    @Override 
    public void invalidate()
    {
    	super.invalidate();
    	paint(getGraphics());
    }
}