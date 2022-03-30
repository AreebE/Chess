package main;

import chess2.Logic;
import chess2.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.Runnable;
import javax.swing.SwingUtilities;
import graphics.EncompassingPanel;
//import game
class Main {
	
	private static HashMap<Piece.Type, Integer[]> whites = new HashMap<Piece.Type, Integer[]>()
	{{
		this.put(Piece.Type.ROOK, new Integer[] {0, 5, 80, 85});
		this.put(Piece.Type.KNIGHT, new Integer[] {85, 5, 170, 85});
		this.put(Piece.Type.BISHOP, new Integer[] {177, 5, 253, 85});
		this.put(Piece.Type.KING, new Integer[] {262, 5, 342, 85});
		this.put(Piece.Type.QUEEN, new Integer[] {347, 5, 427, 85});
		this.put(Piece.Type.PAWN, new Integer[] {527, 9, 607, 89});
	}};
	
	private static HashMap<Piece.Type, Integer[]> blacks = new HashMap<Piece.Type, Integer[]>()
	{{
		this.put(Piece.Type.ROOK, new Integer[] {0, 104, 80, 184});
		this.put(Piece.Type.KNIGHT, new Integer[] {85, 104, 170, 184});
		this.put(Piece.Type.BISHOP, new Integer[] {177, 104, 253, 184});
		this.put(Piece.Type.KING, new Integer[] {262, 104, 342, 184});
		this.put(Piece.Type.QUEEN, new Integer[] {347, 104, 427, 184});
		this.put(Piece.Type.PAWN, new Integer[] {527, 96, 607, 186});
	}};
  public static void main(String[] args) {
    System.out.println("Hello world!");
//    debugLogic();
    debugGraphics();
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
  
  public static void debugGraphics()
  {
      SwingUtilities.invokeLater(new Runnable()
              {
                  @Override
                  public void run()
                  {
                      EncompassingPanel panel = new EncompassingPanel("res/cropped.png", whites, blacks);
                      panel.pack();
//                      panel.setSize.
                      panel.setVisible(true);
                  }
              });
  }
}