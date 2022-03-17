package game.graphics;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.BoxLayout;

import game.chess.Logic;
import game.chess.Piece;
import game.chess.Piece.Type;

public class Display extends JPanel
	implements Logic.PieceTeller
{

	public static final int NONE = -1;
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int TIE = 2;
    private String currentErrorMessage;
    private ActionListener confirmActionListener = new ActionListener()
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try 
			{
				String first = initialPosition.getText();
				String second = finalPosition.getText();
				updater.sendAction(first, second);
			}
			catch (NullPointerException npe)
			{
				currentErrorMessage = "Could not make move.";
				Display.this.invalidate();
			}
			initialPosition.setText(null);
			finalPosition.setText(null);
			
		}
	};
	
	public interface Updater 
	{
		public String getTurn();
		public int getTurnsLeft();
		public int[] getPoints();
		public void suggestMove(String start);
		public void sendAction(String firstPos, String secondPos);
		public void reset();
	}
	
	private Updater updater;
	private int winner;
	
	private JTextField currentTurn;
    private JTextField turnsLeft;
    
    
    private JTextField[] pointFields;
    
    private JPanel move;
    private JTextField pawnTransform;
    private JTextField initialPosition;
    private JTextField toText;
    private JTextField finalPosition;
    private JButton confirmAction;

	
    public Display(Updater u)
    {
    	this.updater = u;
    	this.winner = NONE;
    	GridLayout mainLayout = new GridLayout(5, 1);
    	setLayout(mainLayout);
    	
    	
        currentTurn = new JTextField();
        currentTurn.setEnabled(false);
        add(currentTurn);
        
        turnsLeft = new JTextField();
        turnsLeft.setEnabled(false);
        add(turnsLeft);
        this.currentErrorMessage = "";

        // set up points
        JPanel points = new JPanel();
        points.setBackground(Color.DARK_GRAY);
        GridLayout grid = new GridLayout(2, 2);
        points.setLayout(grid);
        
        JTextField whiteSide = new JTextField("White: ");
        whiteSide.setEnabled(false);
        JTextField blackSide = new JTextField("Black: ");
        blackSide.setEnabled(false);
        points.add(whiteSide);
        points.add(blackSide);
        
        pointFields = new JTextField[] {new JTextField(), new JTextField()};
        pointFields[0].setEnabled(false);
        pointFields[1].setEnabled(false);
        points.add(pointFields[0]);
        points.add(pointFields[1]);
        add(points);
        // Set up movement display
        JPanel move = new JPanel();
        BoxLayout moveInput = new BoxLayout(move, BoxLayout.X_AXIS);
        move.setLayout(moveInput);
        initialPosition = new JTextField();
       
        
      
       
        toText = new JTextField();
        toText.setText("  to  ");
        toText.setEnabled(false);
        finalPosition = new JTextField();
        
        move.add(initialPosition);
        move.add(toText);
        move.add(finalPosition);
        add(move);
        
        // Set up Buttons
        JPanel buttonPanel = new JPanel();
        GridLayout buttonGrid = new GridLayout(1, 3);
        buttonPanel.setLayout(buttonGrid);
        
        JButton resetAction = new JButton();
        resetAction.setText("Reset");
        JButton displayAction = new JButton();
        displayAction.setText("Display move");
        confirmAction = new JButton();
        confirmAction.setText("Confirm Move");
       
        confirmAction.addActionListener(confirmActionListener);
        
        displayAction.addActionListener(new ActionListener() 
        {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = initialPosition.getText();
				if (text != null && text.length() != 0)
				{
					updater.suggestMove(text);
				}
				System.out.println("called");
			}
        	
        });
        
        resetAction.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
    			updater.reset();
    			confirmAction.removeActionListener(confirmActionListener);
    			confirmAction.addActionListener(confirmActionListener);
    			System.out.println("reset");
			}
			
		});
        
        JTextField pawnInfo = new JTextField("Transform pawn to:");
        pawnInfo.setEnabled(false);
        pawnTransform = new JTextField("Queen");
        JPanel pawnPanel = new JPanel();
        GridLayout pawnGrid = new GridLayout(2, 1);
        pawnPanel.setLayout(pawnGrid);
        pawnPanel.add(pawnInfo);
        pawnPanel.add(pawnTransform);
        
        buttonPanel.add(resetAction);
        buttonPanel.add(displayAction);
        buttonPanel.add(confirmAction);
        buttonPanel.add(pawnPanel);
        add(buttonPanel);
    }
    
    public void updateWinnerState(int winner)
    {
    	this.winner = winner;
    	if (winner != NONE)
    	{
        	confirmAction.removeActionListener(confirmActionListener);    		
    	}
    	System.out.println("Updated to " + winner);
    }
    
    @Override 
    public void invalidate()
    {
    	super.invalidate();
    	int[] currentPoints = updater.getPoints();
    	int remainingTurns = updater.getTurnsLeft();
    	currentTurn.setText(currentErrorMessage +  " " + updater.getTurn() + "\'s turn.");
    	turnsLeft.setText(remainingTurns + " turns left.");
    	switch (winner)
    	{
    		case WHITE:
    			currentTurn.setText("White won!");
    			break;
    		case BLACK:
    			currentTurn.setText("Black won!");
    			break;
    		case TIE:
    			currentTurn.setText("Draw!");
    			break;
    	}
    	
    	pointFields[WHITE].setText("" + currentPoints[WHITE]);
    	pointFields[BLACK].setText("" + currentPoints[BLACK]);
//    	System.out.println(turnsLeft.getY()+ "    EEEEEEEEEEEEEEEEEEEEEEEEE");
    }
    
    public void sendError(String message)
    {
        currentErrorMessage = message;
    }

	@Override
	public Piece.Type askWhatToTransformTo() {
		try 
		{
			switch(currentErrorMessage.charAt(0))
			{
				case 'R':
				case 'r':
					return Piece.Type.ROOK;
				case 'B':
				case 'b':
					return Piece.Type.BISHOP;
				case 'k':
				case 'K':
					return Piece.Type.KNIGHT;
				case 'q':
				case 'Q':
				default:
					return Piece.Type.QUEEN;
			}
		}
		catch (NullPointerException npe)
		{
			return Piece.Type.QUEEN;
		}
	}
    
}