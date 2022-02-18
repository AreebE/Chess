package game.chess;

import java.lang.IndexOutOfBoundsException;
public class Logic 
{
    private static final int SIZE = 8;
    
    public enum PieceColor 
    {
        WHITE,
        BLACK
    }

    public enum PieceType
    {
        PAWN(1),
        ROOK(3),
        KNIGHT(3),
        BISHOP(5),
        QUEEN(9),
        KING(Integer.MAX_VALUE);

        private final int value;

        /*
        * Creates a piece type.
        *
        * @param    value   the value of that piece when captured.
        */
        private PieceType(int value)
        {
            this.value = value;
        }

        /*
        * @return   the value of the piece
        */
        public int getValue()
        {
            return value;
        }
    }

    private Piece[][] board;

    private class Piece 
    {   
        public PieceType type;
        public PieceColor color; 

        public Piece(PieceType type, PieceColor color)
        {
            this.type = type;
            this.color = color;
        }

        public String toString()
        {
            return type + " of " + color;
        }
    }
    /*
     * A constructor to create the board.
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

    public void placePiece(String coordinates, Piece piece)
    {
        try 
        {  
            int[] boardCoord = toCoordinates(coordinates);
            setPiece(boardCoord[0], boardCoord[1], piece);
        }
        catch (IndexOutOfBoundsException tme)
        {
            System.out.println("Could not place piece. " + tme.toString());
        }
    }


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

    public static String toCoordinates(int col, int row)
    {
        char colRep = (char) ('a' + col);
        char rowRep = (char) ('1' + row);
        return new StringBuilder().append(colRep).append(rowRep).toString();
    }

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

    private Piece getPiece(int col, int row)
    {
        return board[row][col];
    }

    private void setPiece(int col, int row, Piece piece)
    {
        board[row][col] = piece;
    }
}