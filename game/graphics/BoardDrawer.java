package graphics;

import chess2.Logic;
import chess2.Piece;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.border.EmptyBorder;

/**
 * The class used to draw a board.
 *
 */
public class BoardDrawer extends JComponent
{
	
	
	private BufferedImage img;
	private HashMap<Piece.Type, Integer[]> whiteBoundaries;
	private HashMap<Piece.Type, Integer[]> blackBoundaries;

	/**
	 * A interface used to update pieces.
	 *
	 */
	public interface Updater 
	{
		public Piece getPiece(int row, int col);
	}
	
//	private String[][] possibleOptions;
	private Updater updater;
	private HashMap<String, Integer> possibleOptions;

    private static final Color LIGHT_TILE = new Color(255, 214, 166);
    private static final Color DARK_TILE = new Color(183, 134, 78);
    private static final Color MOVE_DARK = new Color(120, 183, 87);
    private static final Color MOVE_LIGHT = new Color(180, 233, 137);
    private static final Color ATTACK_LIGHT = new Color(231, 87, 87);
    private static final Color ATTACK_DARK = new Color(159, 20, 20);
    private static final Color PASSENT_LIGHT = new Color(236, 126, 223);
    private static final Color PASSENT_DARK = new Color(160, 11, 143);
    private static final Color CASTLE_LIGHT = new Color(145, 202, 240);
    private static final Color CASTLE_DARK = new Color(28, 128, 195);
    private static final Color BLACK = new Color(0, 0, 0);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final int MARGIN_RATIO = 40;
    private static final int NUM_INTERVALS = 9;
    
    /**
     * The basic constructor for a board drawer.
     * @param u the updater used, which is where this class will get the name of pieces from.
     * @param imgName the file name of the image.
     * @param whites the hashmap containing the boundaries for the white pieces. Must be in the order of left, top, right, down.
     * @param blacks same as whites, but for black pieces. Same restrictions apply.
     */
    public BoardDrawer(Updater u, String imgName, HashMap<Piece.Type, Integer[]> whites, HashMap<Piece.Type, Integer[]> blacks)
    {
//    	EmptyBorder border = new EmptyBorder(1, 1, 1, 1);
//    	this.setBorder(border);
        // ee
    	this.updater = u;
    	try {
//    		System.out.println(imgName);
    		
			this.img = ImageIO.read(new File(imgName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.whiteBoundaries = whites;
    	this.blackBoundaries = blacks;
    	possibleOptions = new HashMap<>();
    }
    
    /**
     * Assign the movement options for a given piece.
     * 
     * @param options a 2D string array, where each row has 2 columns: the first is the place to select, the second is the type of move.
     */
    public void assignOptions(String[][] options)
    {
    	possibleOptions.clear();
    	for (String[] possibility: options)
    	{
    		possibleOptions.put(possibility[0], Integer.parseInt(possibility[1]));
    	}
    }

    /**
     * The method used for painting what the board looks like.
     * 
     * @param g the graphics to draw on.
     */
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
        System.out.println(g);
        betterGraphic.setFont(f);
        betterGraphic.setStroke(new BasicStroke(margin));
        betterGraphic.setColor(Color.BLACK);
        betterGraphic.fillRect(0, 0, width, height);
//        System.out.println(this.getBorder().getBorderInsets(this));
//        System.out.println("\n__________________________________\n" + width + " ; " + height + " - " + margin);
//        betterGraphic.drawRect(0, 0, width, height);
        boolean paintDarkTile = false;
        
        // print the letters for columns and rows
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
        
        // print the movements, squares, and pieces
        yStart = margin + heightInterval; 
        for (int row = 1; row < NUM_INTERVALS; row++)
        {
            startX = margin + widthInterval;
            for (int col = 1; col < NUM_INTERVALS; col++)
            {
            	int effectiveCol = col - 1;
            	int effectiveRow = row - 1;
            	// print background of squares
                betterGraphic.setColor((paintDarkTile)? DARK_TILE: LIGHT_TILE);
                String curPoint = Logic.toCoordinates(effectiveCol, effectiveRow);
                if (possibleOptions.containsKey(curPoint))
                {
                	switch(possibleOptions.get(curPoint))
                	{
                		case Logic.MOVE:
                			betterGraphic.setColor((paintDarkTile)? MOVE_DARK: MOVE_LIGHT);
                			break;
                			
                		case Logic.SIMPLE_CAPTURE:
                			betterGraphic.setColor((paintDarkTile)? ATTACK_DARK: ATTACK_LIGHT);
                			break;
                			
                		case Logic.EN_PASSENT:
                			betterGraphic.setColor((paintDarkTile)? PASSENT_DARK: PASSENT_LIGHT);
                			break;
                			
                		case Logic.CASTLE:
                			betterGraphic.setColor((paintDarkTile)? CASTLE_DARK: CASTLE_LIGHT);
                			break;
                		
                	}
                }
                
                betterGraphic.fillRect(startX, yStart, widthInterval, heightInterval);
                paintDarkTile = !paintDarkTile;
                // print piece
                Piece p = updater.getPiece(effectiveRow, effectiveCol);
                if (p != null)
                {
//                	betterGraphic.setColor(p.getColor().equals(Piece.Color.BLACK)? BLACK: WHITE);
//                	String letter = "";
                	Integer[] boundaries;
                	if (p.getColor().equals(Piece.Color.BLACK))
                	{
                		boundaries = blackBoundaries.get(p.getType());
                	}
                	else 
                	{
                		boundaries = whiteBoundaries.get(p.getType());
                	}
//                	System.out.println(img + ", " + boundaries[0] + ", " + boundaries[1] + ", " + boundaries[2] + ", " + boundaries[3]);
//                	System.out.println(startX + ", " + yStart);
                	betterGraphic.drawImage
                			(
	                			img, 
	                			startX, 
	                			yStart, 
	                			startX + widthInterval, 
	                			yStart + heightInterval,
	                			boundaries[0], 
	                			boundaries[1], 
	                			boundaries[2], 
	                			boundaries[3], 
	                			this
                			);
//                	betterGraphic.drawImage(img, startX, yStart, widthInterval, heightInterval, this);
//                	betterGraphic.drawString(letter, startX + margin, yStart + heightInterval - 3 / 2 * margin);
                }
                startX += widthInterval;
            }
            paintDarkTile = !paintDarkTile;
//            System.out.println(startX + ", " + (yStart + heightInterval));
            yStart += heightInterval;
        }
    }
    
    /**
     * Overriding the invalidating method to remind this method to repaint this component.
     */
    @Override 
    public void invalidate()
    {
    	super.invalidate();
    	paint(getGraphics());
    }
}