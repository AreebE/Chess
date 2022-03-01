package game.chess;

/**
     * 
     * @author Areeb Emran
     * 
     * A class for an individual piece.
     */
    
public class Piece 
{   
    public enum Color 
    {
        WHITE,
        BLACK
    }

    public enum Type
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
        private Type(int value)
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

    private Type type;
    private Color color;
    private boolean hasMoved; 
    private int movesMade;

        
    /**
        * A constructor for the piece.
        * 
        * @param type	The type of piece it is.
        * 
        * @param color What color it is.
        */
    public Piece(Type type, Color color)
    {
        this.type = type;
        this.color = color;
        this.hasMoved = false;
        movesMade = 0;
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
    public void setType(Type type)
    {
        this.type = type;
    }
    
    /**
        * A method for getting the piece type.
        * 
        * @return the type this piece is.
        */
    public Type getType()
    {
        return type;
    }
    
    /**
        * Getting the color of this piece.
        * 
        * @return the color
        */
    public Color getColor()
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
        movesMade++;
    }
    
    public int getMovesMade()
    {
    	return movesMade;
    }

}