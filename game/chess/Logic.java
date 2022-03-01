package game.chess;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;

import game.chess.Piece.Color;

public class Logic 
{
    private static final int SIZE = 8;

    
    /**
     * 
     * @author Areeb Emran
     * 
     * Types of moves
     */
    public static  enum Type 
    {
    	MOVE(0),
    	EN_PASSENT(1),
    	SIMPLE_CAPTURE(2),
    	CASTLE(3);
    	
    	public final int val;
    	
    	private Type(int val)
    	{
    		this.val = val;
    	}
    	
    }
    

    private Piece[][] board;
    private String[] kingPositions;
    private static final int BLACK_KING = 1;
    private static final int WHITE_KING = 0;
    
    /**
     * A constructor to create  board. 
     *  
     * First adds  pawns, n  or pieces. Also, here's what  board looks like (without pieces): 
     *  
     *    | a | b | c | d | e | f | g | h |
     *   -+___+___+___+___+___+___+___+___+
     *   1|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___|
     *   2|   |   |   |   |   |   |   |   | 
     *   _|___|___|___|___|___|___|___|___|
     *   3|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___| 
     *   4|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___|    
     *   5|   |   |   |   |   |   |   |   |    
     *   _|___|___|___|___|___|___|___|___|      
     *   6|   |   |   |   |   |   |   |   |     
     *   _|___|___|___|___|___|___|___|___|      
     *   7|   |   |   |   |   |   |   |   | 
     *   _|___|___|___|___|___|___|___|___|
     *   8|   |   |   |   |   |   |   |   |
     *   _|___|___|___|___|___|___|___|___|      
     *   
     *   White goes on  bottom columns, black goes on  top.
    */
    public Logic()
    {
        board = new Piece[SIZE][SIZE];
        kingPositions = new String[2];
        
        for (int col = 0; col < SIZE; col++)
        {
            setPiece(col, 1, new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
            setPiece(col, 6, new Piece(Piece.Type.PAWN, Piece.Color.BLACK));    
        }
        

        placePiece("a1", new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        placePiece("b1", new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        placePiece("c1", new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        placePiece("d1", new Piece(Piece.Type.KING, Piece.Color.WHITE));
        placePiece("e1", new Piece(Piece.Type.QUEEN, Piece.Color.WHITE));
        placePiece("f1", new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        placePiece("g1", new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        placePiece("h1", new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        kingPositions[WHITE_KING] = "e1";
        
        placePiece("a8", new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
        placePiece("b8", new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        placePiece("c8", new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        placePiece("d8", new Piece(Piece.Type.KING, Piece.Color.BLACK));
        placePiece("e8", new Piece(Piece.Type.QUEEN, Piece.Color.BLACK));
        placePiece("f8", new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        placePiece("g8", new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        placePiece("h8", new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
        kingPositions[BLACK_KING] = "e8";
    }

    /**
     * Place a piece based on  string coordinates of "[a-h][1-8]"
     * * Will fail if  coordinate does not exist (ex. 2f, c-3, n5, and a10)
     * 
     * @param coordinates 	 coordinate to place  piece (ex a3)
     * @param piece			 piece to place, like a white pawn.
     * 
     */
    public void placePiece(String coordinates, Piece piece)
    {
        try 
        {  
            int[] boardCoord = toCoordinates(coordinates);
            setPiece(boardCoord[1], boardCoord[0], piece);
            if (piece.getType().equals(Piece.Type.KING))
            {
            	int indexToUse = (piece.getColor().equals(Piece.Color.BLACK))? Logic.BLACK_KING: Logic.WHITE_KING;
            	kingPositions[indexToUse] = Logic.toCoordinates(boardCoord[1], boardCoord[0]);
            }
        }
        catch (IndexOutOfBoundsException | NullPointerException tme)
        {
            System.out.println("Could not place piece. " + tme.toString());
        }
    }


    /**
     * Print  board, starting with  coordinates and n  piece at that coordinate.
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
     * @param col  exact column on  board array.
     * @param row  exact row on  board array.
     * @return A string of coordinates in  form [a-h][1-8]
     */
    public static String toCoordinates(int col, int row)
    {
        char colRep = (char) ('a' + col);
        char rowRep = (char) ('1' + row);
        return new StringBuilder().append(colRep).append(rowRep).toString();
    }

    /**
     * Converts a string coordinate into exact positions on  board. 
     * 
     * @param coord 	 string coordinate, like a7.
     * @return	 exact board positions in  order {row, col}
     * 
     * @throws IndexOutOfBoundsException 	when  letters wouldn't match correct input (ex. n, j, 9, and 0 don't work.)
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
     * Get  piece at a given location
     * 
     * @param col its column on  board
     * @param row its row on  board.
     * @return  piece at that point.
     */
    private Piece getPiece(String coordinate) 
    {
    	int[] coordinates = Logic.toCoordinates(coordinate);
    	return getPiece(coordinates[1], coordinates[0]);
    }

    /**
     * Get  piece at a given location
     * 
     * @param col its column on  board
     * @param row its row on  board.
     * @return  piece at that point.
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
     * Set  piece to a given location
     * 
     * @param col its column on  board
     * @param row its row on  board.
     * @param piece  piece to be moved.
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
     * @return all possible coordinates  piece can go to.
     */
    public ArrayList<String[]> getAllPossibilities(String currentCoord)
    {
    	int row = -1;
    	int col = -1;
    	try 
    	{
    		int[] boardCoord = Logic.toCoordinates(currentCoord);
    		row = boardCoord[0];
    		col = boardCoord[1];
    	}
    	catch (IndexOutOfBoundsException ioobe)
    	{
    		System.out.println("no possible moves");
    		return null;
    	}
    	
   
    	Piece p = this.getPiece(col, row);
    	if (p == null) 
    	{
    		return new ArrayList<String[]>();
    	}
    	Piece.Color color = p.getColor();
    	int[] kingCoord = Logic.toCoordinates(kingPositions[(color.equals(Piece.Color.BLACK))? Logic.BLACK_KING: Logic.WHITE_KING]);
    	this.setPiece(col, row, null);
    	boolean canMakeMove = !isSpotInDanger(kingCoord[0], kingCoord[1], color) || p.getType().equals(Piece.Type.KING);
		this.setPiece(col, row, p);
    	if (!canMakeMove)
    	{
    		System.out.println("danger");
    		return new ArrayList<String[]>();
    	}
    	
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
		// Check for castles
		if (canMakeMove(Type.CASTLE, row, Logic.convertToCol('a'), color)
				&& !isSpotInDanger(row, Logic.convertToCol('b'), color))
		{
			System.out.println(isSpotInDanger(row, Logic.convertToCol('b'), color));
			moves.add(new String[] {Logic.toCoordinates(Logic.convertToCol('b'), row), Integer.toString(Type.CASTLE.val)});
		}
		
		if (canMakeMove(Type.CASTLE, row, Logic.convertToCol('h'), color)
				&& !isSpotInDanger(row, Logic.convertToCol('f'), color))
		{
			moves.add(new String[] {Logic.toCoordinates(Logic.convertToCol('f'), row), Integer.toString(Type.CASTLE.val)});
		}
		
		int[][] possibleOptions = new int[][] 
				{
					{row + 1, col},
					{row + 1, col + 1},
					{row + 1, col - 1},
					{row, col},
					{row, col + 1},
					{row, col - 1},
					{row - 1, col},
					{row - 1, col + 1},
					{row - 1, col - 1}
				};
		for (int[] coordinatePair: possibleOptions)
		{
			
			if (!isSpotInDanger(coordinatePair[0], coordinatePair[1], color))
			{
				performCaptureAndMoveCheck(moves, coordinatePair[1], coordinatePair[0], color);
			}
		}
	}

	private void getQueenMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		getCardinalMoves(moves, col, row, color);
		getDiagonalMoves(moves, col, row, color);
	}

	private void getKnightMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		int[][] possibleMoves = new int[][]
				{
						{row + 1, col + 2},
						{row + 2, col + 1},
						{row - 1, col + 2},
						{row - 2, col + 1},
						{row + 1, col - 2},
						{row + 2, col - 1},
						{row - 1, col - 2},
						{row - 2, col - 1}
				};
				
		for (int[] coordinates: possibleMoves)
		{
			performCaptureAndMoveCheck(moves, coordinates[0], coordinates[1], color);
		}
	}

	private void getBishopMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		getDiagonalMoves(moves, col, row, color);
	}

	private void getRookMoves(
			ArrayList<String[]> moves,
			boolean hasMovedAlready, 
			Piece.Color color, 
			int row, 
			int col) 
	{
		getCardinalMoves(moves, col, row, color);
		if (!hasMovedAlready 
				&& canMakeMove(Type.CASTLE, row, col, color))
		{
			int colToMove = (col == Logic.convertToCol('a'))? Logic.convertToCol('c'): Logic.convertToCol('e'); 
			moves.add(new String[] {Logic.toCoordinates(colToMove, row), Integer.toString(Type.CASTLE.val)});
		}
	}

	
	/**
	 * Get all moves of a pawn 
	 * 
	 * @param moves
	 * @param hasMovedAlready
	 * @param color
	 * @param row
	 * @param col
	 */
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
		
		// 1 && 2 forward
		if (this.canMakeMove(Type.MOVE, row + oneTileMove, col, color))
		{
			moves.add(new String[]{Logic.toCoordinates(col, row + oneTileMove), Integer.toString(Type.MOVE.val)});
			if (!hasMovedAlready
					&& this.canMakeMove(Type.MOVE, row + 2 * oneTileMove, col, color))
			{
				moves.add(new String[] {Logic.toCoordinates(col, row + 2 * oneTileMove), Integer.toString(Type.MOVE.val)});
			}
		}
		
		
		// corner capture
		int[][] possibleMoves = new int[][]
				{
					{row + oneTileMove, col + 1},
					{row + oneTileMove, col - 1}
				};
		
		for (int[] coordinates: possibleMoves)
		{
			if (this.canMakeMove(Type.SIMPLE_CAPTURE, coordinates[0], coordinates[1], color))
			{
				moves.add(new String[] {Logic.toCoordinates(coordinates[1], coordinates[0]), Integer.toString(Type.SIMPLE_CAPTURE.val)});
			}
		}
				
		
		// en passant
		if (row == Logic.convertToRow('5'))
		{
			possibleMoves = new int[][]
					{
						{row + oneTileMove, col + 1},
						{row + oneTileMove, col - 1}
					};
			for (int[] coordinates: possibleMoves)
			{
				if (this.canMakeMove(Type.EN_PASSENT, coordinates[0], coordinates[1], color))
				{
					moves.add(new String[] {Logic.toCoordinates(coordinates[1], coordinates[0]), Integer.toString(Type.EN_PASSENT.val)});
				}
			}
		}
	}

    /**
     * get  cardinal moves.
     * 
     *
     */
    private void getCardinalMoves(
    		ArrayList<String[]> moves,
    		int col, 
    		int row, 
    		Piece.Color color)
    {
        int startCol = convertToCol('a');
        int endCol = convertToCol('h');
        int startRow = convertToRow('1');
        int endRow = convertToRow('8');
        // rightwards movement
        
        int i = col + 1;
        while (i <= endCol && performCaptureAndMoveCheck(moves, i, row, color))
        {
        	i++;
        }

        // leftwards movement
        i = col - 1;
        while (i >= startCol && performCaptureAndMoveCheck(moves, i, row, color))
        {
           i--;
        }

        // downwards movement
        i = row + 1;
        while (i <= endRow && performCaptureAndMoveCheck(moves, col, i, color))
        {
            i++;
        }

        // rightwards movement
        i = row - 1;
        while (i >= startRow && performCaptureAndMoveCheck(moves, col, i, color))
        {
            i--;
        }
    }
    
    /**
     * Getting  diagonal moves.
     * 
     * @param moves
     * @param col
     * @param row
     * @param color
     */
    private void getDiagonalMoves(
    		ArrayList<String[]> moves,
    		int col, 
    		int row, 
    		Piece.Color color)
    {
        int startCol = convertToCol('a');
        int endCol = convertToCol('h');
        int startRow = convertToRow('1');
        int endRow = convertToRow('8');
        // rightwards movement
        
        int i = col + 1;
        int j = row + 1;
        while (i <= endCol 
        		&& j <= endRow 
        		&& performCaptureAndMoveCheck(moves, i, j, color))
        {
        	i++;
        	j++;
        }

        // leftwards movement
        i = col - 1;
        j = row + 1;
        while (i >= startCol 
        		&& j <= endRow
        		&& performCaptureAndMoveCheck(moves, i, j, color))
        {
           i--;
           j++;
        }

        // downwards movement
        i = col + 1;
        j = row - 1;
        while (i <= endCol 
        		&& j >= startRow
        		&& performCaptureAndMoveCheck(moves, i, j, color))
        {
            i++;
            j--;
        }

        // rightwards movement
        i = col - 1;
        j = row - 1;
        while (i >= startCol 
        		&& j >= startRow
        		&& performCaptureAndMoveCheck(moves, i, j, color))
        {
            i--;
            j--;
        }
    }
    
    /**
     * 
     * 
     */
    
    private boolean performCaptureAndMoveCheck(
    		ArrayList<String[]> moves,
    		int col, 
    		int row, 
    		Piece.Color color)
    {
    	 if (canMakeMove(Type.MOVE, row, col, color))
         {
             moves.add(new String[] {Logic.toCoordinates(col, row), Integer.toString(Type.MOVE.val)});
             return true;
         }
         else if (canMakeMove(Type.SIMPLE_CAPTURE, row, col, color))
         {
         	moves.add(new String[] {Logic.toCoordinates(col, row), Integer.toString(Type.SIMPLE_CAPTURE.val)});
         	return false;
         }
         else 
         {
         	return false;
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
			case MOVE:
                return getPiece(col, row) == null;
                
			case EN_PASSENT:
				p = getPiece(col - 1, row);
                return p != null 
                    && !p.getColor().equals(color)
                    && p.getType().equals(Piece.Type.PAWN)
                    && p.getMovesMade() == 1;
                
			case SIMPLE_CAPTURE:
				p = getPiece(col, row);
                return p != null 
                        && !p.getColor().equals(color);
                
			case CASTLE:
				int startCol = convertToCol('a');
                int change = 1;
                if (col != convertToCol('a'))
                {
                    startCol = convertToCol('h');
                    change = -1;
                }
                
                int kingCol = convertToCol('d');
                Piece rook = getPiece(startCol, row);
                Piece king = getPiece(kingCol, row);
//                System.out.println(startCol + " , " + row);
                if (king == null
                		|| rook == null
                		|| !rook.getType().equals(Piece.Type.ROOK)
                		|| !king.getType().equals(Piece.Type.KING)
                		|| rook.hasMovedAlready()
                		|| king.hasMovedAlready()
                		|| !king.getColor().equals(rook.getColor())
                		)
                {
                	System.out.println("e");
                    return false;
                }
                
                for (int i = startCol + change; i != kingCol; i += change)
                {
                	p = getPiece(i, row);
                    if (p != null)
                    {
                        return false;
                    }
                }
                return true;
				
		}
		return false;
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param color
	 * @return
	 */
	private boolean isSpotInDanger(int row, int col, Color color)
	{
		int[][] knightAttacks = new int[][]
				{
					{row + 1, col + 2},
					{row + 2, col + 1},
					{row - 1, col + 2},
					{row - 2, col + 1},
					{row + 1, col - 2},
					{row + 2, col - 1},
					{row - 1, col - 2},
					{row - 2, col - 1}
				};
		
		for (int[] knightCoord: knightAttacks)
		{
			Piece knight = this.getPiece(knightCoord[1], knightCoord[0]);
			if (knight != null
					&& knight.getType().equals(Piece.Type.KNIGHT)
					&& !color.equals(knight.getColor()))
			{
				return true;
			}
		}
		
		int change = (color.equals(Piece.Color.BLACK))? 1: -1;
		int[][] pawnAttacks = new int[][]
				{
					{row + change, col + 1},
					{row + change, col - 1}
				};
		for (int[] pawnCoord: pawnAttacks)
		{
			Piece pawn = this.getPiece(pawnCoord[1], pawnCoord[0]);
			if (pawn != null
					&& pawn.getType().equals(Piece.Type.PAWN)
					&& !color.equals(pawn.getColor()))
			{
				return true;
			}
		}
				
		final int CARDINAL = 0;
		final int DIAGONAL = 1;
		int[][] directionalChecks = new int[][] 
				{
					{row, col + 1, 0, 1, CARDINAL},
					{row, col - 1, 0, -1, CARDINAL},
					{row + 1, col, 1, 0, CARDINAL},
					{row - 1, col, -1, 0, CARDINAL},
					{row + 1, col + 1, 1, 1, DIAGONAL},
					{row + 1, col - 1, 1, -1, DIAGONAL},
					{row - 1, col + 1, -1, 1, DIAGONAL},
					{row - 1, col - 1, -1, -1, DIAGONAL}
				};
		System.out.println(Logic.toCoordinates(col, row));
		for(int[] directionParts: directionalChecks)
		{
			int curRow = directionParts[0];
			int curCol = directionParts[1];
			int changeRow = directionParts[2];
			int changeCol = directionParts[3];
			int type = directionParts[4];
			while (Logic.withinRange(curRow, curCol))
			{
				
				Piece p = getPiece(curCol, curRow);
//				System.out.println(Logic.toCoordinates(curCol, curRow) + ((p == null)? " null": " " + p.toString()));
				if (p != null)
				{
					if (p.getColor().equals(color))
					{
						break;
					}
					else 
					{
						Piece.Type pieceType = p.getType();
						if 
						(
								pieceType.equals(Piece.Type.QUEEN)
								|| (type == CARDINAL && pieceType.equals(Piece.Type.ROOK))
								|| (type == DIAGONAL && pieceType.equals(Piece.Type.BISHOP))
						)
						{
							return true;
						}
						break;
					}
				}
				curRow += changeRow;
				curCol += changeCol;
			}
		}
		
		int[] orKingCoord = Logic.toCoordinates((color.equals(Piece.Color.BLACK))? kingPositions[Logic.WHITE_KING]: kingPositions[Logic.BLACK_KING]);
		return Math.abs(orKingCoord[0] - row) <= 1
				&& Math.abs(orKingCoord[1] - col) <= 1;
				
	}
	
	/**
	 * Do a test to see if a row and column is in  right range.
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
    
    public void clearBoard()
    {
    	board = new Piece[8][8];
    }
}