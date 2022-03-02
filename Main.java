
import game.chess.Logic;
import game.chess.Piece;

import java.util.ArrayList;
import java.util.Arrays;

class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    debugLogic();
  }
  
  public static void debugLogic()
  {
	  Logic l = new Logic();
//	  l.toString();
	  
	  Piece blackKing = new Piece(Piece.Type.KING, Piece.Color.BLACK);
	  Piece whiteKing = new Piece(Piece.Type.KING, Piece.Color.WHITE);
	  blackKing.madeMove();
	  // Test white & black pawn movement moves
//	  printMoves(l.getAllPossibilities("a2"));
//	  printMoves(l.getAllPossibilities("a7"));
//	  printMoves(l.getAllPossibilities("d2"));
//	  printMoves(l.getAllPossibilities("d7"));
	  
	  l.clearBoard();
	  l.placePiece("d1", whiteKing);
	  l.placePiece("d8", blackKing);
//	  printMoves(l.getAllPossibilities("d1"));
//	  l.placePiece("d8", null);
//	  l.placePiece("c2", blackKing);
//	  printMoves(l.getAllPossibilities("d1"));
	  
	  Piece blackRookOne = new Piece(Piece.Type.ROOK, Piece.Color.BLACK);
	  Piece blackRookTwo = new Piece(Piece.Type.ROOK, Piece.Color.BLACK);
	  Piece whiteRookOne = new Piece(Piece.Type.ROOK, Piece.Color.WHITE);
	  
//	  blackRookOne.madeMove();
	  whiteRookOne.madeMove();
	  l.placePiece("h8", whiteRookOne);
	  l.placePiece("g8", blackRookTwo);
	  
//	  l.placePiece("c7", whiteRookOne);
//	  l.toString();
	  printMoves(l.getAllPossibilities("g8"));
//	  printMoves(l.getAllPossibilities("h8"));
  }
  
  private static void printMoves(ArrayList<String[]> moves)
  {
	  System.out.println("\n Printing moves:");
	  for(String[] pos: moves)
	  {
		  System.out.println(pos[0] + ", type: " + pos[1]);
	  }
	  System.out.println("End of moves!");
  }
}