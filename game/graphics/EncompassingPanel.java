package graphics;

import javax.swing.JPanel;

import chess2.Logic;
import chess2.Piece;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.awt.Component;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * A large encompassing panel, containing the display and the board.
 *
 */
public class EncompassingPanel 
		extends JFrame
		implements Display.Updater,
					BoardDrawer.Updater
{
	private static final int MAX_TURNS = 60;
	private int turnsLeft;
	private boolean isBlackTurn;
	private Logic l;
	private BoardDrawer board;
	private Display display;
	
	/**
	 * A ratio setter, used to adjust the size of this component. However, this won't be used anymore.
	 *
	 */
	private class RatioSetter implements ComponentListener 
	{
		private static final double MARGIN_OF_ERROR = 0.003; 

		private final int numerator;
		private final int denominator;
		
		/**
		 * Set the ratio for width/height.
		 * 
		 * @param num 	the numerator of this ratio
		 * @param den 	the denominator of this ratio
		 */
		public RatioSetter(int num, int den)
		{
			this.numerator = num;
			this.denominator = den;
		}
		
		/**
		 *  Resize the component
		 *  @param e some event, I'm not sure what it does. Either way, the only purpose of this is to get the component to adjust.
		 *   width / height = ratio
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
			double ratio = numerator * 1.0 / denominator;
			Component component = e.getComponent();
//	        System.out.println(component.getClass() + ", " + component.getWidth() + "; " + component.getHeight());

			int width = component.getWidth();
			int height = component.getHeight();
//			int height = (ancestor == null)? component.getHeight(): ancestor.getHeight();

			double currentRatio = width * 1.0 / height;
			if (Math.abs(height * 1.0 / width - ratio) > MARGIN_OF_ERROR)
			{
				if (width > height)
				{
					width = (int) (height * ratio);
				}
				else 
				{
					height = (int) (width / ratio); 					
				}
				width = (int) (height * ratio);
				
//				System.out.println("CHANGED, " + width + " and " + height);

				component.setSize(width, height);
				component.invalidate();
			}
	
// 			System.out.println(component.getWidth() + ", " + component.getHeight() + "; " + component.getClass() + ", " + component.getX());
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * A public constructor, simply creating the object.
	 * @param fileName the file for the images
	 * @param whites the boundaries for the white pieces
	 * @param blacks the boundaries for the black pieces.
	 */
    public EncompassingPanel(String fileName, HashMap<Piece.Type, Integer[]> whites, HashMap<Piece.Type, Integer[]> blacks)
    {
        super("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = new JPanel();
        GridLayout f = new GridLayout(1, 2);
//        BoxLayout f = new BoxLayout(c, BoxLayout.X_AXIS);
//        FlowLayout f = new FlowLayout(FlowLayout.CENTER);
        c.setLayout(f);
        board = new BoardDrawer(this, fileName, whites, blacks);
        display = new Display(this);
        l = new Logic();
//        board.setSize(new Dimension(400, 400));
//        b.setMaximumSize(new Dimension(400, 400));
//        b.setMinimumSize(new Dimension(400, 400));
//        board.addComponentListener(new RatioSetter(1, 1));
//        board.setMinimumSize(new Dimension(400, 400));
//        b.setBo
        
        c.add(board);        
//        c.setSize(new Dimension(600, 400));
//        c.setMaximumSize(new Dimension(600, 400));
//        c.setMinimumSize(new Dimension(600, 400));
//        c.add();
        c.add(display);

//        display.addComponentListener(new RatioSetter(2, 1));
//        display.setSize(new Dimension(200, 400));
        this.turnsLeft = MAX_TURNS;
        this.isBlackTurn = false; 
        c.addComponentListener(new RatioSetter(2, 1));
        setContentPane(c);
    }

    /**
     * Get the piece at a given position
     *  
     * @param row the row to get
     * @param col the column to get 
     * @return the piece
     */
	@Override
	public Piece getPiece(int row, int col) {
		return l.getPiece(col, row);
	}
	
	/**
	 * Get the current turn
	 * @return the current turn
	 */
	@Override
	public String getTurn() {
		// TODO Auto-generated method stub
		return (isBlackTurn)? "Black": "White";
	}
	
	/**
	 * Get how many turns are left
	 * @return the turns left
	 */
	@Override
	public int getTurnsLeft() {
		// TODO Auto-generated method stub
		return turnsLeft;
	}
	
	/**
	 * get the points that belong to each side.
	 * @return the number of points each side has.
	 */
	@Override
	public int[] getPoints() {
		// TODO Auto-generated method stub
		int[] points = new int[2];
		points[Display.WHITE] = l.getPoints(Logic.WHITE_KING);
		points[Display.BLACK] = l.getPoints(Logic.BLACK_KING);
		return points;
	}

	/**
	 * called when a move needs to be suggested, which will also update the board.
	 * @param start the position to check for moves from.
	 */
	@Override
	public void suggestMove(String start) {
		// TODO Auto-generated method stub
		display.sendError(""); 
		board.assignOptions(new String[0][0]);
        display.invalidate();
		ArrayList<String[]> options = l.getAllPossibilities(start);
//		System.out.println(start + ", " +l.getPiece(start) + ", " + options.toString());
		
		if (options != null)
		{
			for (String[] possibility: options)
			{
//				System.out.println("Possibility:" + possibility[0]);
			}
			board.assignOptions(options.toArray(new String[options.size()][2]));			
		}
		board.invalidate();

	}

	/**
	 * Perform an action and update what is necessary. 
	 *  
	 * @param firstPos the position to move from
	 * @param secondPos the position to move to
	 */
	@Override
	public void sendAction(String firstPos, String secondPos) {
		ArrayList<String[]> possibilities = l.getAllPossibilities(firstPos);
		String[] solution = null;
		for (String[] possibility: possibilities)
		{
			if (possibility[0].equals(secondPos))
			{
				solution = possibility;
				break;
			}
		}
		if (solution == null)
		{
			return;
		}
		
		Piece p = l.getPiece(firstPos);
        boolean isBlackColor = p.getColor().equals(Piece.Color.BLACK);
        if ((isBlackColor ^ isBlackTurn))
        {
            display.sendError("Wrong person's turn.");
            display.invalidate();
            return;
        }
		turnsLeft--; 

		boolean inCheckmate = !l.makeMove(firstPos, secondPos, solution[1], display);
		if (inCheckmate)
		{
			display.updateWinnerState((isBlackColor)? Display.BLACK: Display.WHITE);
		} else if (turnsLeft == 0)
		{
			int whitePoints = l.getPoints(Logic.WHITE_KING);
			int blackPoints = l.getPoints(Logic.BLACK_KING);
			int winner = (whitePoints > blackPoints)? Display.WHITE: (blackPoints > whitePoints)? Display.BLACK: Display.TIE;
			display.updateWinnerState(winner);
		}
		isBlackTurn = !isBlackTurn;
        board.assignOptions(new String[0][0]); 
		display.invalidate();
		board.invalidate();
	}

	/**
	 * Reset the the whole game, if needed.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		l.resetBoard();
		board.invalidate();
		display.invalidate();
		turnsLeft = MAX_TURNS;
	}
}
