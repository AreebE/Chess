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

public class Display extends JPanel
{

	public static final int NONE = -1;
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	
	
	public interface Updater 
	{
		public String getTurn();
		public int getTurnsLeft();
		public int[] getPoints();
		public void suggestMove(String start);
		public void sendAction(String firstPos, String secondPos);
	}
	
	private Updater updater;
	private int winner;
	
	private JTextField currentTurn;
    private JTextField turnsLeft;
    
    
    private JTextField[] pointFields;
    
    private JPanel move;
    private JTextField initialPosition;
    private JTextField toText;
    private JTextField finalPosition;
    private JButton confirmAction;

	
    public Display(Updater u)
    {
    	this.updater = u;
    	GridLayout mainLayout = new GridLayout(5, 1);
    	setLayout(mainLayout);
    	
    	
        currentTurn = new JTextField();
        currentTurn.setEnabled(false);
        add(currentTurn);
        
        turnsLeft = new JTextField();
        turnsLeft.setEnabled(false);
        add(turnsLeft);
        
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
        initialPosition.getDocument().addDocumentListener(new DocumentListener()
        		{

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						String text = initialPosition.getText();
						if (text != null && text.length() != 0)
						{
							updater.suggestMove(text);
						}
						System.out.println("called");
					}
        	
        		});
        
        initialPosition.addActionListener(new ActionListener()
        		{

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String text = initialPosition.getText();
						if (text != null && text.length() != 0)
						{
							updater.suggestMove(text);
						}
						System.out.println("called");
					}
        			
        		});
        initialPosition.addInputMethodListener(new InputMethodListener() 
        		{

					@Override
					public void inputMethodTextChanged(InputMethodEvent event) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void caretPositionChanged(InputMethodEvent event) {
						// TODO Auto-generated method stub
						String text = initialPosition.getText();
						if (text != null && text.length() != 0)
						{
							updater.suggestMove(text);
						}
						System.out.println("called");
					}
        			
        		});
        toText = new JTextField();
        toText.setText("  to  ");
        finalPosition = new JTextField();
        
        move.add(initialPosition);
        move.add(toText);
        move.add(finalPosition);
        add(move);
        
        // Set up confirm button
        confirmAction = new JButton();
        confirmAction.addActionListener(new ActionListener()
        		{

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String first = initialPosition.getText();
						String second = finalPosition.getText();
						updater.sendAction(first, second);
						initialPosition.setText(null);
						finalPosition.setText(null);
					}	
        	
        		});
        add(confirmAction);
    }
    
    public void updateWinnerState(int winner)
    {
    	this.winner = winner;
    }
    
    @Override 
    public void invalidate()
    {
    	super.invalidate();
    	int[] currentPoints = updater.getPoints();
    	int remainingTurns = updater.getTurnsLeft();
    	currentTurn.setText(updater.getTurn());
    	turnsLeft.setText(remainingTurns + " turns left.");
    	pointFields[WHITE].setText("" + currentPoints[WHITE]);
    	pointFields[BLACK].setText("" + currentPoints[BLACK]);
//    	System.out.println(turnsLeft.getY()+ "    EEEEEEEEEEEEEEEEEEEEEEEEE");
    }
    
}