package game.chess;

import java.lang.IndexOutOfBoundsException;
public class Logic 
{
    private static final int SIZE = 8;
    
    /**
     * 
     * @author Areeb Emran
     * 
     * The piece colors: only black and white
     */
    public enum PieceColor 
    {
        WHITE,
        BLACK
    }

    /**
     * 
     * @author Areeb Emran
     *
     * Piece types to help define values.
     */
    public enum PieceType
    {
        PAWN(1),
        ROOK(3),
        KNIGHT(3),
        BISHOP(5),
        QUEEN(9),
        KING(Integer.MAX_VALUE);

        private final int value;

        /**
         * Creates a piece type.
         *
         * @param    value   the value of that piece when captured.
         */
        private PieceType(int value)
        {
            this.value = value;
        }

        /**
         * A way to get the value of a piece.
         * 
         * @return   the value of the piece
         */
        public int getValue()
        {
            return value;
        }
    }

    private Piece[][] board;
    
    /**
     * 
     * @author Areeb Emran
     * 
     * A class for an individual piece.
     */
    private class Piece 
    {   
        public PieceType type;
        public final PieceColor color; 
        public boolean hasMoved; 
        
        /**
         * A constructor for the piece.
         * 
         * @param type	The type of piece it is.
         * 
         * @param color What color it is.
         */
        public Piece(PieceType type, PieceColor color)
        {
            this.type = type;
            this.color = color;
            this.hasMoved = false;
        }

        /**
         * Returning the value of this as a string (no access to position)
         */
        public String toString()
        {
            return type + " of " + color;
        }
        
        /**
         * A method designed exclusively for the pawn when it reaches the end of the board..
         * 
         * @param type The type this piece will change to.
         */
        public void setType(PieceType type)
        {
        	this.type = type;
        }
        
        /**
         * A method for getting the piece type.
         * 
         * @return the type this piece is.
         */
        public PieceType getPieceType()
        {
        	return type;
        }
        
        /**
         * Getting the color of this piece.
         * 
         * @return the color
         */
        public PieceColor getColor()
        {
        	return color;
        }
        
        /**
         * Checking if the piece has already moved (applies to pawns, rooks, and kings)
         *  
         * @return if the piece has moved.
         */
        public boolean hasMovedAlready()
        {
        	return hasMoved;
        }
        
        /**
         * This method has to be called when a piece makes a move.
         */
        public void madeMove()
        {
        	this.hasMoved = true;
        }
    }
    
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
     *   White goes on the top columns, black goes on the bottom.
    */
    public Logic()
    {
        System.out.println("e");
        board = new Piece[SIZE][SIZE];
        
        for (int col = 0; col < SIZE; col++)
        {
            setPiece(col, 1, new Piece(PieceType.PAWN, PieceColor.WHITE));
            setPiece(col, 6, new Piece(PieceType.PAWN, PieceColor.BLACK));    
        }

        placePiece("a1", new Piece(PieceType.ROOK, PieceColor.WHITE));
        placePiece("b1", new Piece(PieceType.KNIGHT, PieceColor.WHITE));
        placePiece("c1", new Piece(PieceType.BISHOP, PieceColor.WHITE));
        placePiece("d1", new Piece(PieceType.QUEEN, PieceColor.WHITE));
        placePiece("e1", new Piece(PieceType.KING, PieceColor.WHITE));
        placePiece("f1", new Piece(PieceType.BISHOP, PieceColor.WHITE));
        placePiece("g1", new Piece(PieceType.KNIGHT, PieceColor.WHITE));
        placePiece("h1", new Piece(PieceType.ROOK, PieceColor.WHITE));

        placePiece("a8", new Piece(PieceType.ROOK, PieceColor.BLACK));
        placePiece("b8", new Piece(PieceType.KNIGHT, PieceColor.BLACK));
        placePiece("c8", new Piece(PieceType.BISHOP, PieceColor.BLACK));
        placePiece("d8", new Piece(PieceType.QUEEN, PieceColor.BLACK));
        placePiece("e8", new Piece(PieceType.KING, PieceColor.BLACK));
        placePiece("f8", new Piece(PieceType.BISHOP, PieceColor.BLACK));
        placePiece("g8", new Piece(PieceType.KNIGHT, PieceColor.BLACK));
        placePiece("h8", new Piece(PieceType.ROOK, PieceColor.BLACK));
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
     * @throws IndexOutOfBoundsException 	when the letters wouldn't match ccorrect input (ex. n, j, 9, and 0 don't work.)
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
        return board[row][col];
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
}