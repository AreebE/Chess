package game.chess;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;

public class Logic 
{
    private static final int SIZE = 8;
    
    
    /**
     * 
     * @author Areeb Emran
     * 
     * Types of moves
     */
    public enum Type 
    {
    	MOVE,
    	EN_PASSENT,
    	SIMPLE_CAPTURE,
    	CASTLE,
    	TRANSFORM	
    }
    

    private Piece[][] board;
    
    /**
     * A constructor to create the board. 
     *  
     * First adds the pawns, then the other pieces. Also, here's what the board looks like (without pieces): 
     *  
     *    | a | b | c | d | e | f | g | h |
     *   -+___+___+___+___+___+___+___+___+
     *   8|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___|
     *   7|   |   |   |   |   |   |   |   | 
     *   _|___|___|___|___|___|___|___|___|
     *   6|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___| 
     *   5|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___|    
     *   4|   |   |   |   |   |   |   |   |    
     *   _|___|___|___|___|___|___|___|___|      
     *   3|   |   |   |   |   |   |   |   |     
     *   _|___|___|___|___|___|___|___|___|      
     *   2|   |   |   |   |   |   |   |   | 
     *   _|___|___|___|___|___|___|___|___|
     *   1|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___|      
     *   
     *   White goes on the bottom columns, black goes on the top.
    */
    public Logic()
    {
        System.out.println("e");
        board = new Piece[SIZE][SIZE];
        
        for (int col = 0; col < SIZE; col++)
        {
            setPiece(col, 1, new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
            setPiece(col, 6, new Piece(Piece.Type.PAWN, Piece.Color.BLACK));    
        }

        placePiece("a1", new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        placePiece("b1", new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        placePiece("c1", new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        placePiece("d1", new Piece(Piece.Type.QUEEN, Piece.Color.WHITE));
        placePiece("e1", new Piece(Piece.Type.KING, Piece.Color.WHITE));
        placePiece("f1", new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        placePiece("g1", new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        placePiece("h1", new Piece(Piece.Type.ROOK, Piece.Color.WHITE));

        placePiece("a8", new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
        placePiece("b8", new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        placePiece("c8", new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        placePiece("d8", new Piece(Piece.Type.QUEEN, Piece.Color.BLACK));
        placePiece("e8", new Piece(Piece.Type.KING, Piece.Color.BLACK));
        placePiece("f8", new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        placePiece("g8", new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        placePiece("h8", new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
    }

    /**
     * Place a piece based on the string coordinates of "[a-h][1-8]"
     * * Will fail if the coordinate does not exist (ex. 2f, c-3, n5, and a10)
     * 
     * @param coordinates 	The coordinate to place the piece (ex a3)
     * @param piece			The piece to place, like a white pawn.
     * 
     */
    public void placePiece(String coordinates, Piece piece)
    {
        try 
        {  
            int[] boardCoord = toCoordinates(coordinates);
            setPiece(boardCoord[1], boardCoord[0], piece);
        }
        catch (IndexOutOfBoundsException tme)
        {
            System.out.println("Could not place piece. " + tme.toString());
        }
    }


    /**
     * Print the board, starting with the coordinates and then the piece at that coordinate.
     */
    public String toString()
    {
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                Piece piece = getPiece(col, row);
                System.out.println(toCoordinates(col, row) + " has " + ((piece == null)? "nothing": piece.toString()));
            }
        }
        return "";
    }

    /**
     * Convert a set of coordinates into a string version (row = 6 & col = 2 --> b7)
     * 
     * @param col The exact column on the board array.
     * @param row The exact row on the board array.
     * @return A string of coordinates in the form [a-h][1-8]
     */
    public static String toCoordinates(int col, int row)
    {
        char colRep = (char) ('a' + col);
        char rowRep = (char) ('1' + row);
        return new StringBuilder().append(colRep).append(rowRep).toString();
    }

    /**
     * Converts a string coordinate into exact positions on the board. 
     * 
     * @param coord 	The string coordinate, like a7.
     * @return	the exact board positions in the order {row, col}
     * 
     * @throws IndexOutOfBoundsException 	when the letters wouldn't match correct input (ex. n, j, 9, and 0 don't work.)
     */
    public static int[] toCoordinates(String coord) throws IndexOutOfBoundsException
    {
        char row = coord.charAt(1);
        char col = coord.charAt(0);
        int rowInt = row - '1';
        int colInt = col - 'a';
        if (rowInt < 0 || rowInt >= SIZE || colInt < 0 || colInt >= SIZE)
        {
            throw new IndexOutOfBoundsException();
        }
        return new int[]{rowInt, colInt};
    }

    /**
     * Get the piece at a given location
     * 
     * @param col its column on the board
     * @param row its row on the board.
     * @return The piece at that point.
     */
    private Piece getPiece(int col, int row) 
    {
    	try 
    	{
            return board[row][col];
    	}
    	catch (IndexOutOfBoundsException ioobe)
    	{
    		return null;
    	}
    }
    
    /**
     * Set the piece to a given location
     * 
     * @param col its column on the board
     * @param row its row on the board.
     * @param piece the piece to be moved.
     */
    private void setPiece(int col, int row, Piece piece)
    {
        board[row][col] = piece;
    }
    
    /**
     * A method for getting all possible moves a piece can go to.
     * 
     * @param currentCoord
     * 
     * @return all possible coordinates the piece can go to.
     */
    public ArrayList<String[]> getAllPossibilities(String currentCoord)
    {
    	int row = -1;
    	int col = -1;
    	try 
    	{
    		int[] boardCoord = this.toCoordinates(currentCoord);
    		row = boardCoord[0];
    		col = boardCoord[1];
    	}
    	catch (IndexOutOfBoundsException ioobe)
    	{
    		System.out.println("no possible moves");
    		return null;
    	}
    	
    	Piece p = this.getPiece(col, row);
    	ArrayList<String[]> moves = new ArrayList<>();
    	switch (p.getType())
    	{
    		case PAWN:
    			getPawnMoves(moves, p.hasMovedAlready(), p.getColor(), row, col);
    			break;
    		case ROOK:
    			getRookMoves(moves, p.hasMovedAlready(), p.getColor(), row, col);
    			break;
    		case BISHOP:
    			getBishopMoves(moves, p.getColor(), row, col);
    			break;
    		case KNIGHT:
    			getKnightMoves(moves, p.getColor(), row, col);
    			break;
    		case QUEEN:
    			getQueenMoves(moves, p.getColor(), row, col);
    			break;
    		case KING: 
    			getKingMoves(moves, p.hasMovedAlready(), p.getColor(), row, col);
    			break;
    		
    	}
		return moves;
    }

	private void getKingMoves(
			ArrayList<String[]> moves,
			boolean hasMovedAlready, 
			Piece.Color color, 
			int row, 
			int col) 
	{
		// TODO Auto-generated method stub
	}

	private void getQueenMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		// TODO Auto-generated method stub
	}

	private void getKnightMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		// TODO Auto-generated method stub
	}

	private void getBishopMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		// TODO Auto-generated method stub
	}

	private void getRookMoves(
			ArrayList<String[]> moves,
			boolean hasMovedAlready, 
			Piece.Color color, 
			int row, 
			int col) 
	{
		// TODO Auto-generated method stub
	}

	
	private void getPawnMoves(
			ArrayList<String[]> moves,
			boolean hasMovedAlready, 
			Piece.Color color, 
			int row, 
			int col) 
	{
		int oneTileMove = 1;
		if (color.equals(Piece.Color.BLACK))
		{
			oneTileMove *= -1;
		}
		
		// 1 forward
		if (getPiece(col, row + oneTileMove) == null)
		{
			moves.add(new String[]{Logic.toCoordinates(col, row + oneTileMove), null});
		}
		
		// 2 forward
		if (!hasMovedAlready 
				&& getPiece(col, row + 2 * oneTileMove) == null)
		{
			moves.add(new String[] {Logic.toCoordinates(col, row + 2 * oneTileMove), null});
		}
		
		// corner capture
		Piece leftUp = getPiece(col - oneTileMove, row + oneTileMove);
		if (leftUp != null 
				&& !leftUp.getColor().equals(color))
		{
			String coord = Logic.toCoordinates(col - oneTileMove, row + oneTileMove);
			moves.add(new String[] {coord, coord});
		}
		
		Piece rightUp = getPiece(col, row);
	}

    /**
     * get the cardinal moves.
     * 
     *
     */
    private void getCardinalMoves(int col, int row, Piece.Color color)
    {
        int startCol = convertToCol('a');
        int endCol = convertToCol('h');
        int startRow = convertToRow('1');
        int endRow = convertToRow('8');
        // rightwards movement
        for (int i = col + 1; i <= endCol; i++)
        {
            if (canMakeMove(Type.MOVE, row, i, color))
            {
                
            }
            else if (canMakeMove(Type.ATTACK, row, i, color))
            {
                
            }
            else 
            {
                
            }
        }

        // leftwards movement
        for (int i = col - 1; i >= startCol; i++)
        {
            if (canMakeMove(Type.MOVE, row, i, color))
            {
                
            }
        }

        // downwards movement
        for (int i = row + 1; i <= endRow; i++)
        {
            if (canMakeMove(Type.MOVE, i, col, color))
            {
                
            }
        }

        // rightwards movement
        for (int i = row - 1; i >= startRow; i++)
        {
            if (canMakeMove(Type.MOVE, i, col, color))
            {
                
            }
        }
    }
    
	/**
	 * See if a move can be made. 
	 * 
	 * @param typeOfMove
	 * @param row
	 * @param col
	 * @param color
	 * @return
	 */
	private boolean canMakeMove (
			Type typeOfMove, 
			int row, 
			int col, 
			Piece.Color color)
	{
        if (!Logic.withinRange(row, col))
        {
            return false;
        }
        Piece p;
		switch (typeOfMove)
		{
            case TRANSFORM:
			case MOVE:
                return getPiece(col, row) == null;
                
			case EN_PASSENT:
				p = getPiece(col, row);
                return p != null 
                    && !p.getColor().equals(color)
                    && p.getType().equals(Piece.Type.PAWN);
                
			case SIMPLE_CAPTURE:
				p = getPiece(col, row);
                return p != null 
                        && !p.getColor().equals(color);
                
			case CASTLE:
				int startCol = convertToCol('a');
                int change = 1;
                if (col != convertToCol('b'))
                {
                    startCol = convertToCol('h');
                    change = -1;
                }
                Piece p = getPiece(startCol, row);
                if (p.equals(null))
                {
                    
                }
                int kingPosition = convertToCol('d');
                for (int i = startCol + 1; i < kingPosition; i += change)
                {
                    
                }
				
		}
		return false;
	}
	/*
	 *  public enum Type 
    {
    	MOVE,
    	EN_PASSENT,
    	SIMPLE_CAPTURE,
    	CASTLE,
    	TRANSFORM	
    }
	 */
	
	
	/**
	 * Do a test to see if a row and column is in the right range.
	 * 
	 * @param positionOne
	 * @param positionTwo
	 * @return
	 */
	public static boolean withinRange(int positionOne, int positionTwo)
	{
		return positionOne >= 0 && positionOne < SIZE
				&& positionTwo >= 0 && positionTwo < SIZE;
	}

    private static int convertToRow(char c)
    {
        return (int) (c - '1');
    }

    private static int convertToCol(char c)
    {
        return (int) (c - 'a');
    }
}