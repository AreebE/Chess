
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
//	  blackKing.madeMove();
	  // Test white & black pawn movement moves
//	  printMoves(l.getAllPossibilities("a2"));
//	  printMoves(l.getAllPossibilities("a7"));
//	  printMoves(l.getAllPossibilities("d2"));
//	  printMoves(l.getAllPossibilities("d7"));
	  
//	  printMoves(l.getAllPossibilities("g8"));
	  
	  l.clearBoard();
	  l.placePiece("f1", whiteKing);
	  l.placePiece("c2", blackKing);
//	  printMoves(l.getAllPossibilities("d1"));
//	  l.placePiece("d8", null);
//	  l.placePiece("c2", blackKing);
//	  printMoves(l.getAllPossibilities("d1"));
	  
	  Piece blackRookOne = new Piece(Piece.Type.ROOK, Piece.Color.BLACK);
	  Piece blackRookTwo = new Piece(Piece.Type.ROOK, Piece.Color.BLACK);
	  Piece whiteRookOne = new Piece(Piece.Type.ROOK, Piece.Color.WHITE);
	  Piece blackQueen = new Piece(Piece.Type.QUEEN, Piece.Color.BLACK);
	  
	  Piece whitePawn = new Piece(Piece.Type.PAWN, Piece.Color.WHITE);
	  Piece blackPawn = new Piece(Piece.Type.PAWN, Piece.Color.BLACK);
	  Piece blackBishop = new Piece(Piece.Type.BISHOP, Piece.Color.BLACK);
	  Piece whiteBishop = new Piece(Piece.Type.BISHOP, Piece.Color.WHITE);
	  Piece whiteKnight = new Piece(Piece.Type.KNIGHT, Piece.Color.WHITE);
	  
	  blackRookOne.madeMove();
	  blackRookTwo.madeMove();
	  whiteRookOne.madeMove();
//	  l.placePiece("f5", whiteRookOne);
//	  l.placePiece("c4", blackQueen);
//	  l.placePiece("a4", whiteBishop);
//	  l.placePiece("h4", blackBishop);
//	  l.placePiece("c2", whitePawn);
//	  l.placePiece("c7", whiteRookOne);
//	  l.placePiece("a8", blackRookOne);
//	  l.placePiece("c7", whiteRookOne);
	  l.placePiece("d4", whiteKnight);
//	  l.toString();
//	  printMoves(l.getAllPossibilities("d6"));
//	  printMoves(l.getAllPossibilities("h8"));
//	  printMoves(l.getAllPossibilities("c4"));
	  printMoves(l.getAllPossibilities("d4"));
//	  System.out.println(l
	  l.toString();
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