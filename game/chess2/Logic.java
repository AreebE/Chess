package chess2;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Arrays;

import chess2.Piece.Color;
import chess2.Piece.Type;

public class Logic 
{
	
	public static final char KNIGHT_CHAR = 'N';
	public static final char ROOK_CHAR = 'R';
	public static final char BISHOP_CHAR = 'B';
	public static final char QUEEN_CHAR = 'Q';
	public static final char KING_CHAR = 'K';
	public static final char PAWN_CHAR = 'P';
	
    private static final Piece WHITE_PLACEHOLDER = new Piece(Piece.Type.PAWN, Piece.Color.WHITE);
    private static final Piece BLACK_PLACEHOLDER = new Piece(Piece.Type.PAWN, Piece.Color.BLACK);
    private static final int SIZE = 8;
    
    
    private static final int DIAGONAL = 0;
    private static final int CARDINAL = 1;
    private static final int KNIGHT = 2;
    private static final int PAWN = 3;
    private static final int KING = 4;
    
    private static final int POSSIBLE = 0;
    private static final int IMPOSSIBLE = 1;
    private static final int KING_IN_CHECK = 2;

    
    /**
     * A interface to get the name of the piece to transform a pawn into once it reaches the end of the board.
     *
     */
    public interface PieceTeller 
    {
    	public Piece.Type askWhatToTransformTo();
    }
    
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
    
    public static final int MOVE = 0;
    public static final int EN_PASSENT = 1;
    public static final int SIMPLE_CAPTURE = 2;
    public static final int CASTLE = 3;
    
    private int[] points; /////////////

    private Piece[][] board;
    private String[] kingPositions;
    public static final int BLACK_KING = 1;
    public static final int WHITE_KING = 0;
    
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
        resetBoard();

    }
    
    /**
     * Reset the board to its original condition
     */
    public void resetBoard()
    {
        clearBoard();
        points = new int[2];
        for (int col = 0; col < SIZE; col++)
        {
            setPiece(col, Logic.convertToRow('2'), new Piece(Piece.Type.PAWN, Piece.Color.WHITE));
            setPiece(col, Logic.convertToRow('7'), new Piece(Piece.Type.PAWN, Piece.Color.BLACK));    
        }
        

        placePiece("a1", new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        placePiece("b1", new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        placePiece("c1", new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        placePiece("e1", new Piece(Piece.Type.KING, Piece.Color.WHITE));
        placePiece("d1", new Piece(Piece.Type.QUEEN, Piece.Color.WHITE));
        placePiece("f1", new Piece(Piece.Type.BISHOP, Piece.Color.WHITE));
        placePiece("g1", new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE));
        placePiece("h1", new Piece(Piece.Type.ROOK, Piece.Color.WHITE));
        
        placePiece("a8", new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
        placePiece("b8", new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        placePiece("c8", new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        placePiece("e8", new Piece(Piece.Type.KING, Piece.Color.BLACK));
        placePiece("d8", new Piece(Piece.Type.QUEEN, Piece.Color.BLACK));
        placePiece("f8", new Piece(Piece.Type.BISHOP, Piece.Color.BLACK));
        placePiece("g8", new Piece(Piece.Type.KNIGHT, Piece.Color.BLACK));
        placePiece("h8", new Piece(Piece.Type.ROOK, Piece.Color.BLACK));
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
//            System.out.println("Could not place piece. " + tme.toString());
        }
    }


    /**
     * Print  board, starting with  coordinates and n  piece at that coordinate.
     */
    public String toString()
    {
        StringBuilder boardLayout = new StringBuilder();
        for (int row = 0; row < 8; row++)
        {
        	for (int col = 0; col < 8; col++)
        	{
        		Piece current = this.getPiece(col, row);
        		char pieceChar = '0';
        		if (current == null)
        		{
        	
        			while (current == null && col < 8)
        			{
        				col++;
        				current = this.getPiece(col, row);
        				pieceChar++;
        			}
        			boardLayout.append(pieceChar);
        			if (col == 8)
        			{
        				continue;
        			}
        		}
        		switch(current.getType())
        		{
        			case QUEEN:
        				pieceChar = this.QUEEN_CHAR;
        				break;
        			case BISHOP:
        				pieceChar = this.BISHOP_CHAR;
        				break;
        			case KING:
        				pieceChar = this.KING_CHAR;
        				break;
        			case PAWN:
        				pieceChar = this.PAWN_CHAR;
        				break;
        			case ROOK:
        				pieceChar = this.ROOK_CHAR;
        				break;
        			case KNIGHT:
        				pieceChar = this.KNIGHT_CHAR;
        				break;
        		}
        		if (current.getColor().equals(Piece.Color.BLACK))
        		{
        			pieceChar = Character.toLowerCase(pieceChar);
        		}
        		boardLayout.append(pieceChar);
        	}
        	if (row <= 7)
        	{
        		boardLayout.append("/");
        	}
        }
        return boardLayout.toString();
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
     * @param coordinate its coordinate
     * @return  piece at that point.
     */
    public Piece getPiece(String coordinate) 
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
    public Piece getPiece(int col, int row) 
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
    		return null;
    	}
    	
    	Piece.Color color = p.getColor();
    	int[] kingCoord = Logic.toCoordinates(kingPositions[(color.equals(Piece.Color.BLACK))? Logic.BLACK_KING: Logic.WHITE_KING]);
    	this.setPiece(col, row, null);
    	
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
    			getKingMoves(moves, p.hasMovedAlready(), p.getColor(), row, col, p);
    			break;
    		
    	}
		this.setPiece(col, row, p);
		return moves;
    }
    
    /**
     * Get all moves a king can make (castling and moving one space in any direction)
     * 
     * @param moves		The list of moves
     * @param hasMovedAlready	Whether the king is able to move
     * @param color		The color of the king
     * @param row		The row the king is on
     * @param col		The column the king is on
     * @param p			the king piece (only so the castling method works properly)
     */
	private void getKingMoves(
			ArrayList<String[]> moves,
			boolean hasMovedAlready, 
			Piece.Color color, 
			int row, 
			int col,
			Piece p) 
	{
		// Check for castles
		setPiece(col, row, p);
//		System.out.println(canMakeMove(Type.CASTLE, row, Logic.convertToCol('a'), color));
		if (canMakeMove(Type.CASTLE, row, Logic.convertToCol('a'), color) == POSSIBLE)
		{
//			System.out.println(isSpotInDanger(row, Logic.convertToCol('b'), color));
			moves.add(new String[] {Logic.toCoordinates(Logic.convertToCol('c'), row), Integer.toString(Type.CASTLE.val)});
		}
		
		if (canMakeMove(Type.CASTLE, row, Logic.convertToCol('h'), color) == POSSIBLE)
		{
			moves.add(new String[] {Logic.toCoordinates(Logic.convertToCol('g'), row), Integer.toString(Type.CASTLE.val)});
		}
		setPiece(col, row, null);
		
		int[][] possibleOptions = new int[][] 
				{
					{row + 1, col},
					{row + 1, col + 1},
					{row + 1, col - 1},
					{row, col + 1},
					{row, col - 1},
					{row - 1, col},
					{row - 1, col + 1},
					{row - 1, col - 1}
				};
		for (int[] coordinatePair: possibleOptions)
		{
			performCaptureAndMoveCheck(moves, coordinatePair[1], coordinatePair[0], color, true);
		}
	}

	/**
	 * All possible moves of a queen (Cardinal and Diagonal)
	 * 
	 * @param moves 	the list of moves
	 * @param color		the color of the queen
	 * @param row		the initial row
	 * @param col		the initial column
	 */
	private void getQueenMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		getCardinalMoves(moves, col, row, color);
		getDiagonalMoves(moves, col, row, color);
	}

	/**
	 * All possible moves of a knight (L shapes)
	 * 
	 * @param moves		the list of moves
	 * @param color		the color of the knight
	 * @param row		the row the knight is at
	 * @param col		the column the knight is at
	 */
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
			performCaptureAndMoveCheck(moves, coordinates[1], coordinates[0], color);
		}
	}

	/**
	 * Get all moves of a bishop (diagonal)
	 * 
	 * @param moves		the list of moves
	 * @param color		The color of the bishop
	 * @param row		the initial row
	 * @param col		the initial column
	 */
	private void getBishopMoves(
			ArrayList<String[]> moves,
			Piece.Color color, 
			int row, 
			int col) 
	{
		getDiagonalMoves(moves, col, row, color);
	}

	/**
	 * Get all moves of a rook (cardinal and castling)
	 * 
	 * @param moves		The list of moves to add to.
	 * @param hasMovedAlready	Whether the rook has already moved
	 * @param color		The color of the piece
	 * @param row		The row the rook starts at 
	 * @param col		The starting column
	 */
	private void getRookMoves(
			ArrayList<String[]> moves,
			boolean hasMovedAlready, 
			Piece.Color color, 
			int row, 
			int col) 
	{
		getCardinalMoves(moves, col, row, color);
	}

	
	/**
	 * Get all moves of a pawn (diagonally, en-passant, forward one, and forward 2)
	 * 
	 * @param moves		the list of moves to add to.
	 * @param hasMovedAlready	Whether the pawn has moved before or not
	 * @param color		The color of the pawn
	 * @param row		The starting row of this pawn
	 * @param col		The starting column of this pawn
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
		if (this.canMakeMove(Type.MOVE, row + oneTileMove, col, color) == POSSIBLE)
		{
			moves.add(new String[]{Logic.toCoordinates(col, row + oneTileMove), Integer.toString(Type.MOVE.val)});
			if (!hasMovedAlready
					&& this.canMakeMove(Type.MOVE, row + 2 * oneTileMove, col, color) == POSSIBLE)
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
			if (this.canMakeMove(Type.SIMPLE_CAPTURE, coordinates[0], coordinates[1], color) == POSSIBLE)
			{
				moves.add(new String[] {Logic.toCoordinates(coordinates[1], coordinates[0]), Integer.toString(Type.SIMPLE_CAPTURE.val)});
			}
		}
				
		
		// en passant
		if (
				(row == Logic.convertToRow('5') && color.equals(Piece.Color.WHITE))
				|| (row == Logic.convertToRow('4') && color.equals(Piece.Color.BLACK))
			)
		{
			possibleMoves = new int[][]
					{
						{row + oneTileMove, col + 1},
						{row + oneTileMove, col - 1}
					};
			for (int[] coordinates: possibleMoves)
			{
				if (this.canMakeMove(Type.EN_PASSENT, coordinates[0], coordinates[1], color) == POSSIBLE)
				{
					moves.add(new String[] {Logic.toCoordinates(coordinates[1], coordinates[0]), Integer.toString(Type.EN_PASSENT.val)});
				}
			}
		}
	}

    /**
     * get all cardinal (or horizontal & vertical) moves.
     * 
     * @param moves		The list of moves to append entries to
     * @param col		the initial column
     * @param row 		The initial row
     * @param color		the color of this piece.
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
     * Getting all possible diagonal moves.
     * 
     * @param moves the moves array to add entries to.
     * @param col	the starting column of the piece
     * @param row	the starting row of the piece.
     * @param color	the color of this piece.
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
     * Check the other one (this is an overloaded method)
     * @param moves
     * @param col
     * @param row
     * @param color
     * @return
     */
    private boolean performCaptureAndMoveCheck(
    		ArrayList<String[]> moves,
    		int col, 
    		int row, 
    		Piece.Color color)
    {
    	return performCaptureAndMoveCheck(moves, col, row, color, false);
    }
    
    /**
     * A method to see if a piece can either move to the place or attack the piece already present.
     * If so, it adds the piece to the moves array it was given.
     * 
     * @param col	the column to move to.
     * @param row	the row to move to.
     * @param color 	the color of this piece.
     * 
     * @return if the moving piece is able to 'keep going.' This largely applies to the bishops, queens, and rooks since they can't move past friendly pieces.
     * 
     */
    private boolean performCaptureAndMoveCheck(
    		ArrayList<String[]> moves,
    		int col, 
    		int row, 
    		Piece.Color color,
    		boolean kingIsMakingMove)
    {
    	int result = canMakeMove(Type.MOVE, row, col, color, kingIsMakingMove);
//    	System.out.println(result);
    	switch(result)
    	{
    		case POSSIBLE:
    			moves.add(new String[] {Logic.toCoordinates(col, row), Integer.toString(Type.MOVE.val)}); 
    		case KING_IN_CHECK:
    			return true;
    		case IMPOSSIBLE:
    			int attackResult = canMakeMove(Type.SIMPLE_CAPTURE, row, col, color, kingIsMakingMove);
    			if (attackResult == POSSIBLE)
    			{
    				 moves.add(new String[] {Logic.toCoordinates(col, row), Integer.toString(Type.SIMPLE_CAPTURE.val)}); 
    			}
    			return false;
    	}
    	return true;
    }
    
    
    /**
     * An overloaded method: better to check the original. This is for pieces that aren't the king.
     * 
     * @param typeOfMove	the type of move
     * @param row		the row to move to
     * @param col		the column to move to
     * @param color		The color of the piece making the move
     * 
     * @return	3 different outcomes (check the other version)
     */
    private int canMakeMove (
    		Type typeOfMove,
    		int row,
    		int col,
    		Piece.Color color)
    {
    	return canMakeMove(typeOfMove, row, col, color, false);
    }
    
	/**
	 * See if a move can be made. *
	 * 
	 * There are also four types of moves this can process: 
	 * 	
	 * 		* Move -- can the piece move to an empty space?
	 * 		* En-passant -- Can the pawn capture the upcoming pawn?**
	 * 		* Simple-capture -- Can the piece capture whatever piece is on the space***?
	 * 		* Castle -- Can the king and rook make a castle option?
	 * 
	 * * It should be noted the check requires the piece to be removed from its original position.
	 * ** This presumes the attacking pawn is in the right position
	 * *** It presumes other conditions are met (ex. if a rook has line-of-sight of the position)
	 * 
	 * @param typeOfMove	What move to check for
	 * @param row		The row the piece wants to move to/on.
	 * @param col		The column the piece wants to move to, with one exception with the castle. 
	 * 					* For the castle, the column tells the position of the rook to check.
	 * @param color		The color of the piece making the move
	 * @param isKingMakingMove Whether it is the king making the move or not.
	 * 
	 * @return 	It will return 3 different outcomes: 
	 * 		* If the move can be made.
	 * 	 	* If the move can theoretically be made, BUT the move would place the friendly king in check. 
	 * 		* If the move cannot be made. 
	 */
	private int canMakeMove (
			Type typeOfMove, 
			int row, 
			int col, 
			Piece.Color color,
			boolean isKingMakingMove)
	{
	
        if (!Logic.withinRange(row, col))
        {
            return IMPOSSIBLE;
        }
        Piece p;
        Piece friendly = (color.equals(Piece.Color.WHITE))? WHITE_PLACEHOLDER: BLACK_PLACEHOLDER;
//        private Color oppColor
		boolean kingInCheck = false;
		int result = IMPOSSIBLE;
		int change; ////////////////
        switch (typeOfMove)
		{
			case MOVE:
                if (getPiece(col, row) == null)
                {
                    result = POSSIBLE;

                    setPiece(col, row, friendly);
                    
                    kingInCheck = (isKingMakingMove)? isSpotInDanger(row, col, color).size() != 0: isSpotInDanger(color);
                    setPiece(col, row, null);   
                  
                }
                break;
                
			case EN_PASSENT:
				change = (color.equals(Piece.Color.BLACK))? 1: -1; ////////////////
				p = getPiece(col, row + change);
//				System.out.println("Checking " + Logic.toCoordinates(col, row + change) + " for en passent" );
//				System.out.println((p == null)? "No pawn here": "Pawn is color " + p.getColor() + " and has made " + p.getMovesMade());
                if (p != null 
                    && !p.getColor().equals(color)
                    && p.getType().equals(Piece.Type.PAWN)
                    && p.getMovesMade() == 1)
                {
                	result = POSSIBLE;
                	
                	setPiece(col, row, friendly);
                	setPiece(col, row + change, null);
                    kingInCheck = isSpotInDanger(color);
                	setPiece(col, row, null);
                	setPiece(col, row + change, p);
                	
                }
                break;
                
			case SIMPLE_CAPTURE:
				p = getPiece(col, row);
				if (p != null 
						&& !p.getColor().equals(color))
				{
					result = POSSIBLE;
					
					setPiece(col, row, friendly);
					kingInCheck = (isKingMakingMove)? isSpotInDanger(row, col, color).size() != 0: isSpotInDanger(color);
					setPiece(col, row, p);
				}
				
				break;
                
			case CASTLE:
				int startCol = convertToCol('a');
                change = 1; ////////////
                boolean onLeftSide = col == convertToCol('a');
                if (!onLeftSide)
                {
                    startCol = convertToCol('h');
                    change = -1;
                }
                
                int kingCol = convertToCol('e');
                Piece rook = getPiece(startCol, row);
                Piece king = getPiece(kingCol, row);
//                System.out.println(startCol + " , " + row);
//            	System.out.println(col + " , " + row + "; " + typeOfMove.toString() + " --  " + color.toString() + " = " + isKingMakingMove);

                if (king == null
                		|| rook == null
                		|| !rook.getType().equals(Piece.Type.ROOK)
                		|| !king.getType().equals(Piece.Type.KING)
                		|| rook.hasMovedAlready()
                		|| king.hasMovedAlready()
                		|| !king.getColor().equals(rook.getColor())
                		)
                {
                	break;
                }
                
//                System.out.println(startCol);
                for (int i = startCol + change; i != kingCol; i += change)
                {
                	p = getPiece(i, row);
//                	System.out.println(Logic.toCoordinates(startCol, row));
//                	System.out.println(i +", " + change);
//                	System.out.println(isSpotInDanger(row, startCol, color));
                    if (p != null || isSpotInDanger(row, i, color).size() != 0)
                    {
                    	return IMPOSSIBLE;
                    }
                }
                result = POSSIBLE;
                
                int kingNewCol = (onLeftSide)? convertToCol('c'): convertToCol('g');
                int rookNewCol = kingNewCol + change;
                
                setPiece(kingCol, row, null);
                setPiece(kingNewCol, row, king);
                setPiece(rookNewCol, row, rook);
                kingInCheck = isSpotInDanger(color);
                setPiece(kingCol, row, king);
                setPiece(kingNewCol, row, null);
                setPiece(rookNewCol, row, null);
                
                break;
		}  
        
		
		return (kingInCheck)? KING_IN_CHECK: result;
	}
	
	
	/**
	 * A convenience check to see if the king is in danger.
	 * 
	 * @param kingColor The color of the king.
	 * @return	If the king is in danger.
	 */
    private boolean isSpotInDanger(Piece.Color kingColor)
    {
        int index = kingColor.equals(Piece.Color.WHITE)? Logic.WHITE_KING: Logic.BLACK_KING;
        int[] coord = Logic.toCoordinates(kingPositions[index]);
        return isSpotInDanger(coord[0], coord[1], kingColor).size() != 0;
    }
    
    /**
     * Another check to see what pieces are attacking this square
     * @param position the position
     * @param color determines the color of the pieces returned (ex. if this is Piece.Color.WHITE, it will return all pieces that are of Piece.Color.BLACK)
     * @return All pieces attacking this. 	  		
     * 			Each entry is in the format in {row, col, type}
     */
    protected ArrayList<int[]> isSpotInDanger(String position, Piece.Color color)
    {
    	int[] positions = Logic.toCoordinates(position);
		return isSpotInDanger(positions[0], positions[1], color);
    }
    
	/**
	 * Do a check to see if the specific spot can be attacked by any piece.
	 * 
	 * @param row		The row of the space
	 * @param col		The column of the space
	 * @param color		The friendly side. (and consequently, which color could attack the king)
	 * @return		All pieces that are attacking this spot. There could be multiple, but it prioritizes in this order:
	 * 	
	 * 				* Knight 
	 *  			* Pawn
	 *  			* Queen 
	 *  			* Bishop
	 *  			* Rook
	 *  			* King
	 *  			
	 *  			Each entry is in the format in {row, col, type}
	 */
	protected ArrayList<int[]> isSpotInDanger(int row, int col, Color color)
	{
//		System.out.println(Logic.toCoordinates(col, row));
		ArrayList<int[]> attackingPieces = new ArrayList<>();
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
				attackingPieces.add(new int[] {knightCoord[0], knightCoord[1], KNIGHT});
			}
		}
		
		int change = (color.equals(Piece.Color.BLACK))? -1: 1; ////////
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
				attackingPieces.add(new int[] {pawnCoord[0], pawnCoord[1], PAWN});
			}
		}
				
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
//		System.out.println(Logic.toCoordinates(col, row));
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
							attackingPieces.add(new int[]{curRow, curCol, type});
						}
						break;
					}
				}
				curRow += changeRow;
				curCol += changeCol;
			}
		}
		
		String coordinates = (color.equals(Piece.Color.BLACK))? kingPositions[Logic.WHITE_KING]: kingPositions[Logic.BLACK_KING];
		int[] otherKingCoord = Logic.toCoordinates(coordinates);
		int[] kingAttackValues = new int[]{otherKingCoord[0], otherKingCoord[1], KING};
		Piece king = this.getPiece(coordinates);
		if( (Math.abs(otherKingCoord[0] - row) <= 1
				&& Math.abs(otherKingCoord[1] - col) <= 1)
				)
		{
			attackingPieces.add(kingAttackValues);			
		}
		
		return attackingPieces;
	}
	
	
	/**
	 * See if the king is in check and either has the possibility to make a move or another piece can move to take the hit.
	 *  
	 * @param color 	The color of the king.
	 * @param checkingPieceCoord	The coordinates of the piece that is placing the king in check.
	 * @return	if the king of the color given is in checkmate.
	 */
	private boolean inCheckmate(Piece.Color color, int[] checkingPieceCoord)
	{
//		System.out.println(Arrays.toString(kingPositions));
        String chosenKing = kingPositions[(color.equals(Piece.Color.BLACK))? BLACK_KING: WHITE_KING];
		int[] kingCoord = Logic.toCoordinates(chosenKing);
        ArrayList<String[]> kingMovements = getAllPossibilities(chosenKing);
        if (kingMovements == null || kingMovements.size() == 0)
		{
			int rowChange = kingCoord[0] < checkingPieceCoord[0] ? 1 : (kingCoord[0] == checkingPieceCoord[0]? 0: -1);
			int colChange = kingCoord[1] < checkingPieceCoord[1] ? 1 : (kingCoord[1] == checkingPieceCoord[1]? 0: -1);
//			System.out.println("checking " + checkingPieceCoord[2] + ", " + rowChange + "; " + colChange);

			switch(checkingPieceCoord[2])
			{
				case CARDINAL:
				case DIAGONAL:
					int[] currentSpot = new int[] {kingCoord[0] + rowChange, kingCoord[1] + colChange};

					while (currentSpot[0] != checkingPieceCoord[0] 
							|| currentSpot[1] != checkingPieceCoord[1])
					{
						if (canBlockSpot(currentSpot, color))
						{

							return false; /////
						}
						currentSpot[0] += rowChange;
						currentSpot[1] += colChange;

					}					
				case KING:
				case KNIGHT:
				case PAWN:
					if (canBlockSpot(checkingPieceCoord, color))
					{
						return false; ////////
					}
					return true; //////
			}
		}
		return false; //////
	}
	
	/**
	 * See if anything can block this spot
	 * @param coord the spot to block
	 * @param c the color of the piece
	 * @return if the spot can be blocked
	 */
	private boolean canBlockSpot(int[] coord, Color c)
	{
		ArrayList<int[]> coordinatesOfAttackers = this.isSpotInDanger(coord[0], coord[1], c) ;
		String spot = Logic.toCoordinates(coord[1], coord[0]);
//		System.out.println(spot);
//		for (int[] coords: coordinatesOfAttackers)
//		{
//			System.out.println(Logic.toCoordinates(coord[1], coord[0]));
//		}
		for (int[] attacker: coordinatesOfAttackers)
		{
			ArrayList<String[]> possibilities = getAllPossibilities(Logic.toCoordinates(attacker[1], attacker[0]));
			for (String[] move: possibilities)
			{
				if (move[0].equals(spot))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Do a test to see if a row and column is in  right range.
	 * 
	 * @param positionOne	Either row or column
	 * @param positionTwo	Either row or column.
	 * @return	If the rows and columns are in the right range.
	 */
	public static boolean withinRange(int positionOne, int positionTwo)
	{
		return positionOne >= 0 && positionOne < SIZE
				&& positionTwo >= 0 && positionTwo < SIZE;
	}

	
	/**
	 * Convert a character to its respective row (1-8)
	 * 
	 * @param r the character given
	 * @return Its row index.
	 */
    public static int convertToRow(char r)
    {
        return (int) (r - '1');
    }

    /**
     * Convert a character to its respective column (a-h)
     * 
     * @param c the character given
     * @return Its corresponding column index.
     */
    public static int convertToCol(char c)
    {
        return (int) (c - 'a');
    }
    
    /**
     * A debug method for clearing the board
     */
    public void clearBoard()
    {
    	board = new Piece[8][8];
    }
    
    /**
     * Get the points on a given side
     * @param side the side to get points from
     * @return the points that side has
     */
    public int getPoints(int side)
    {
        return points[side];
    }

    /**
     * Make a move
     * 
     * @param initSpot the spot to start from
     * @param finalSpot the spot to move to
     * @return if the player can make other moves
     */
    ////////////
    public boolean makeMove(String initSpot, String finalSpot, String type, PieceTeller teller)
    {
    	// initial preparation
    	Piece moving = getPiece(initSpot);
    	Integer typeOfMove = Integer.parseInt(type);
        Piece.Color currentColor = moving.getColor();
        moving.madeMove();

    	System.out.println(initSpot + ", " + finalSpot + ", " + type);
        // Getting the capture coordinatse
    	int[] captureCoordinates = Logic.toCoordinates(finalSpot);
    	if (typeOfMove == Type.EN_PASSENT.val)
    	{
    		int change = (currentColor.equals(Piece.Color.BLACK))? 1: -1;
    		captureCoordinates[0] += change; ////////
    	}
    	String captureSpot = Logic.toCoordinates(captureCoordinates[1], captureCoordinates[0]);
    	System.out.println(captureSpot);
    	
    	// Seeing the captured piece, then placing some numm pieces
        Piece captured = getPiece(captureSpot);
        if (captured != null)
        {
            points[currentColor.equals(Piece.Color.BLACK)? BLACK_KING: WHITE_KING] += captured.getValue();
        }
        placePiece(initSpot, null);
        placePiece(captureSpot, null);
        
        //moving the initial piece
        if (moving.getType().equals(Piece.Type.PAWN) && 
        		(
        				(currentColor.equals(Piece.Color.BLACK) && finalSpot.charAt(1) == '1')
        				|| (currentColor.equals(Piece.Color.WHITE)) && finalSpot.charAt(1) == '8')
        		)
        {
        	moving.setType(teller.askWhatToTransformTo());
        }
        placePiece(finalSpot, moving);
        
        // check for castle
        if (typeOfMove == Type.CASTLE.val)
        {
        	int[] initialKingPos = Logic.toCoordinates(initSpot);
        	int[] finalKingPos = Logic.toCoordinates(finalSpot);
        	boolean kingInFartherLocation = finalKingPos[1] < initialKingPos[1];
        	String initialRookPos = Logic.toCoordinates((kingInFartherLocation)? Logic.convertToCol('a'): Logic.convertToCol('h'), initialKingPos[0]);
        	String finalRookPos = Logic.toCoordinates((kingInFartherLocation)? Logic.convertToCol('d'): Logic.convertToCol('f'), initialKingPos[0]);
        	Piece rook = getPiece(initialRookPos);
        	rook.madeMove();
        	placePiece(finalRookPos, rook);
        	placePiece(initialRookPos, null);
        }
        
        //check if other king is in check.
        Piece.Color oppColor = (currentColor.equals(Piece.Color.BLACK)? Piece.Color.WHITE: Piece.Color.BLACK);
        int[] kingCoord = Logic.toCoordinates(kingPositions[oppColor.equals(Piece.Color.BLACK)? BLACK_KING: WHITE_KING]);
        ArrayList<int[]> attackerOfKing = isSpotInDanger(kingCoord[0], kingCoord[1], oppColor);
        return (attackerOfKing.size() == 0)? true: !inCheckmate(oppColor, attackerOfKing.get(0));
    }
    //////////

    protected void setBoard(String positions)
    {
    	int row = 0;
    	int col = 0;
    	for (int i = 0; i < positions.length(); i++)
    	{
    		char letter = positions.charAt(i);
    		while (letter != '/')
    		{
    			if (letter - '1' >= 0 
    					&& '8' - letter >= 0 )
    			{
    				for (int j = 0; j < letter - '1' + 1; j++)
    				{
    					this.setPiece(col, row, null);
    					col++;
    				}
    			}
    			else 
    			{
    				boolean isLowerCase = Character.isLowerCase(letter);
        			Piece.Type typeToMake = null;
        			switch(Character.toUpperCase(letter))
        			{
        				case PAWN_CHAR:
        					typeToMake = Piece.Type.PAWN;
        					break;
        				case BISHOP_CHAR:
        					typeToMake = Piece.Type.BISHOP;
        					break;
        				case QUEEN_CHAR:
        					typeToMake = Piece.Type.QUEEN;
        					break;
        				case KING_CHAR:
        					typeToMake = Piece.Type.KING;
        					break;
        				case ROOK_CHAR:
        					typeToMake = Piece.Type.ROOK;
        					break;
        				case KNIGHT_CHAR:
        					typeToMake = Piece.Type.KNIGHT;
        					break;
        			}
        			this.setPiece(col, row, new Piece(typeToMake, (isLowerCase)? Piece.Color.BLACK: Piece.Color.WHITE));
        			col++;
    			}
    			i++;
    			letter = positions.charAt(i);
    		}
    		col = 0;
    		row++;
    	}
    }
    
	public static Piece.Type getType(char charAt) {
		// TODO Auto-generated method stub
		switch (charAt)
		{
			case Logic.BISHOP_CHAR:
				return Piece.Type.BISHOP;
			case Logic.KING_CHAR:
				return Piece.Type.KING;
			case Logic.KNIGHT_CHAR:
				return Piece.Type.KNIGHT;
			case Logic.QUEEN_CHAR:
				return Piece.Type.QUEEN;
			case Logic.ROOK_CHAR:
				return Piece.Type.ROOK;
		}
		return null;
	}
}
