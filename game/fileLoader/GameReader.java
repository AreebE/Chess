package fileLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chess2.Logic;
import chess2.Piece;

public class GameReader extends Logic 
	implements Logic.PieceTeller{
	private static final char PROMOTION_SYMBOL = '=';
	private static final String FILES = "[a-h]";
	private static final String RANKS = "[1-8]";
	private static final String SPECIAL_PIECES = new StringBuilder("[")
													.append(Logic.KING_CHAR)
													.append(Logic.KNIGHT_CHAR)
													.append(Logic.BISHOP_CHAR)
													.append(Logic.QUEEN_CHAR)
													.append(Logic.ROOK_CHAR)
													.append("]")
													.toString();
	private static final char CAPTURE_CHAR = 'x';
	private static final String POSITION = FILES + RANKS;
	
	private static final Pattern CHECK = Pattern.compile("[\\+]");
	private static final Pattern CHECKMATE = Pattern.compile("\\+\\+|#");
	private static final Pattern PAWN_MOVEMENT = Pattern.compile(POSITION);
	private static final Pattern PAWN_CAPTURE = Pattern.compile(FILES + CAPTURE_CHAR + POSITION);
	private static final Pattern SIMPLE_MOVEMENT = Pattern.compile(
			new StringBuilder()
			.append(SPECIAL_PIECES).append(POSITION)
			.append("|").append(SPECIAL_PIECES).append(CAPTURE_CHAR).append(POSITION)
			.toString()
			);
	
	private static final Pattern SIMPLE_MOVEMENT_RANKS = Pattern.compile(
			new StringBuilder()
			.append(SPECIAL_PIECES).append(RANKS).append(POSITION)
			.append("|").append(SPECIAL_PIECES).append(RANKS).append(CAPTURE_CHAR).append(POSITION)
			.toString()
			);
	
	private static final Pattern SIMPLE_MOVEMENT_FILES = Pattern.compile(
			new StringBuilder()
			.append(SPECIAL_PIECES).append(FILES).append(POSITION)
			.append("|").append(SPECIAL_PIECES).append(FILES).append(CAPTURE_CHAR).append(POSITION)
			.toString()
			);
	
	private static final Pattern SIMPLE_MOVEMENT_POSITION = Pattern.compile(
			new StringBuilder()
			.append(SPECIAL_PIECES).append(POSITION).append(POSITION)
			.append("|").append(SPECIAL_PIECES).append(POSITION).append(CAPTURE_CHAR).append(POSITION)
			.toString()
			);
	
	
	private static final Pattern QUEEN_SIDE_CASTLE = Pattern.compile("O-O-O");
	private static final Pattern KING_SIDE_CASTLE = Pattern.compile("O-O");
	
	private static final Pattern WHITE_WON = Pattern.compile("1-0");
	private static final Pattern BLACK_WON = Pattern.compile("0-1");
	private static final Pattern TIE = Pattern.compile("1/2-1/2");

	private static final Pattern SPACES = Pattern.compile("\\[.+\\]|(\\s*)([\\d]+)(\\.)(\\s*)|(\\s)|(\\n)");
	
	private static final int MOVE = 0;
	private static final int BOARD_LAYOUT = 1;
	private Scanner fileReader;
	private boolean isBlackTurn = false;
	private Piece.Type selectedType = Piece.Type.QUEEN;
	private ArrayList<String[]> moves;
	private int currentMoveIndex;
	
	public GameReader(String moveFileName) throws FileNotFoundException
	{
		super();
		setFileName(moveFileName);
	}
	
	public void setFileName(String newFile) throws FileNotFoundException
	{
		fileReader = new Scanner(new File(newFile));
		fileReader.useDelimiter(SPACES);
		currentMoveIndex = 0;
		processFile();
	}
	
	public void processFile()
	{
//		String completeFile ="";
//		Scanner createAll = new Scanner(new File("res/test"));
//		while (C)
//		SPACES.matcher(FILES).find();
		super.resetBoard();
		moves = new ArrayList<String[]>();
		currentMoveIndex = 0;
		moves.add(new String[] {"", this.toString(), ""});
		isBlackTurn = false;
		while (fileReader.hasNext())
		{
			String move = fileReader.next();
//			completeFile += move + " ";
			if (move.equals(""))
			{
				continue;
			}
//			System.out.print(move + " -- ");
			if (currentMoveIndex != 0)
			{
//				System.out.println("before: " + this.toString() + ", " + this.toString().equals(moves.get(currentMoveIndex - 1)[BOARD_LAYOUT]));
			}
//			System.out.println("\'" + move + "\'");
			String result = getTypeOfMove(move);
			if (result != null)
			{
				move += result;
			}
			moves.add(new String[] {move, this.toString()});
			isBlackTurn = !isBlackTurn;
			currentMoveIndex++;
		}
		currentMoveIndex = 0;
//		System.out.println(completeFile);
//		Matcher matcher = SPACES.matcher(completeFile); 
//		matcher.find();
//		int i = matcher.start();
//		System.out.println(i);

//		while (i < completeFile.length())
//		{
//			int start = matcher.start();
//			int end = matcher.end();
//			System.out.println("\'" +completeFile.substring(start, end) + "\' at " + start + ", " + end);
//			matcher.find();
//			i = matcher.start();
//		}
	}
	

	public void setCurrentMove(int newMove)
	{
		currentMoveIndex = newMove;
		this.setBoard(moves.get(currentMoveIndex)[BOARD_LAYOUT]);
	}
	
	public void advance()
	{
		if (currentMoveIndex < moves.size() - 1)
		{
			setCurrentMove(currentMoveIndex + 1);
		}
	}
	
	public String getTypeOfMove(String move)
	{
		System.out.println(move);
		if (CHECK.matcher(move).find())
		{
//			System.out.println("in check");
			move = move.substring(0, move.length() - 1);
		}
		else if (CHECKMATE.matcher(move).find())
		{
//			System.out.println("in checkmate");
			move = move.substring(0, move.length() - 1);

		}
		
		if (QUEEN_SIDE_CASTLE.matcher(move).find())
		{
			char rank = (isBlackTurn)? '8': '1';
			super.makeMove("e" + rank, "c" + rank, Type.CASTLE.val + "", this);
//			System.out.println("Queen side castle");
		}
		else if (KING_SIDE_CASTLE.matcher(move).find())
		{
			char rank = (isBlackTurn)? '8': '1';
			super.makeMove("e" + rank, "g" + rank, Type.CASTLE.val + "", this);
//			System.out.println("King side castle");
		}
		
		else if (BLACK_WON.matcher(move).find())
		{
			return " -- Black Won!";
		}
		else if (WHITE_WON.matcher(move).find())
		{
			return " -- White won!";
		}
		else if(TIE.matcher(move).find())
		{
			return " -- Stalemate.";
		}
		else if (SIMPLE_MOVEMENT_POSITION.matcher(move).find())
		{
			String startPoint = move.substring(1, 3);
			String endingPoint = move.substring(move.length() - 2);
//			System.out.println(endingPoint);
			ArrayList<int[]> possiblePieces = super.isSpotInDanger(endingPoint, (isBlackTurn)? Piece.Color.WHITE: Piece.Color.BLACK);
			for (int[] startPosition: possiblePieces)
			{
				if (Logic.toCoordinates(startPosition[1], startPosition[0]).equals(startPoint))
				{
					super.makeMove(startPoint, endingPoint, Logic.Type.MOVE.val + "", this);
					break;
				}
			}
			
		
//			System.out.print("simple movement position matched");
		}
		else if (SIMPLE_MOVEMENT_FILES.matcher(move).find())
		{
			String endingPoint = move.substring(move.length() - 2);
			ArrayList<int[]> possiblePieces = super.isSpotInDanger(endingPoint, (isBlackTurn)? Piece.Color.WHITE: Piece.Color.BLACK);
			Piece.Type typeToMove = Logic.getType(move.charAt(0));
			for (int[] startPosition: possiblePieces)
			{
				String startLocation = Logic.toCoordinates(startPosition[1], startPosition[0]);
//				System.out.println(startLocation + ", " + endingPoint);
				if (super.getPiece(startLocation).getType() == typeToMove
						&& Logic.convertToCol(move.charAt(1)) == startPosition[1])
				{
					super.makeMove(startLocation, endingPoint, Logic.Type.MOVE.val + "", this);
					break;
				}
			}
//			System.out.println("simple movement, with files");
		}
		else if (SIMPLE_MOVEMENT_RANKS.matcher(move).find())
		{
			String endingPoint = move.substring(move.length() - 2);
			ArrayList<int[]> possiblePieces = super.isSpotInDanger(endingPoint, (isBlackTurn)? Piece.Color.WHITE: Piece.Color.BLACK);
			Piece.Type typeToMove = Logic.getType(move.charAt(0));
			for (int[] startPosition: possiblePieces)
			{
				String startLocation = Logic.toCoordinates(startPosition[1], startPosition[0]);
				if (super.getPiece(startLocation).getType() == typeToMove
						&& Logic.convertToRow(move.charAt(1)) == startPosition[0])
				{
					super.makeMove(startLocation, endingPoint, Logic.Type.MOVE.val + "", this);
					break;
				}
			}
//			System.out.print("simple movement, ranks");
		}
		else if (SIMPLE_MOVEMENT.matcher(move).find())
		{
//			System.out.println("e");
			String endingPoint = move.substring(move.length() - 2);
			ArrayList<int[]> possiblePieces = super.isSpotInDanger(endingPoint, (isBlackTurn)? Piece.Color.WHITE: Piece.Color.BLACK);
			Piece.Type typeToMove = Logic.getType(move.charAt(0));
			for (int[] startPosition: possiblePieces)
			{
				String startLocation = Logic.toCoordinates(startPosition[1], startPosition[0]);
				if (super.getPiece(startLocation).getType() == typeToMove)
				{
//					System.out.println(startPosition[2] + ", " + Logic.Type.EN_PASSENT.val);
//					System.out.println();
					super.makeMove(startLocation, endingPoint, Logic.Type.MOVE.val + "", this);
					break;
				}
			}
//			System.out.println("simple movement");
		}
		else if (PAWN_CAPTURE.matcher(move).find())
		{
			char file = move.charAt(0);
			String pawnEndpoint = move.substring(2, 4);
			ArrayList<int[]> possiblePieces = super.isSpotInDanger(pawnEndpoint, (isBlackTurn)? Piece.Color.WHITE: Piece.Color.BLACK);
			if (move.length() == 6)
			{
				selectedType = Logic.getType(move.charAt(5));
			}
			for (int[] startPosition: possiblePieces)
			{
				String start = Logic.toCoordinates(startPosition[1], startPosition[0]);
				if (start.charAt(0) == file 
						&& super.getPiece(start).getType().equals(Piece.Type.PAWN))
				{
					ArrayList<String[]> pawnMovements = super.getAllPossibilities(start);
					int i = 0;
					while (pawnMovements.get(i)[0].equals(start))
					{
						i++;

					}
					super.makeMove(start, pawnEndpoint, pawnMovements.get(i)[1], this);
//					System.out.println("pawn captured a piece");

					return null;
				}
			}
			// if there is an en passant:
			int change = (isBlackTurn)? 1: -1;
			char initRank = move.charAt(1);
			super.makeMove(Logic.toCoordinates(file, initRank + change), pawnEndpoint, Logic.Type.EN_PASSENT.val + "", this);
		}
		else if (PAWN_MOVEMENT.matcher(move).find())
		{
			String pawnEndpoint = move.substring(0, 2);
			if (move.length() == 4)
			{
				selectedType = Logic.getType(move.charAt(3));
			}
			char file = move.charAt(0);
			char rank = move.charAt(1);
			int change = (isBlackTurn)? 1: -1;
//			System.out.println(file);
			String[] possibilities = new String[] {
					Logic.toCoordinates(Logic.convertToCol(file), Logic.convertToRow((char) (rank + change))), 
					Logic.toCoordinates(Logic.convertToCol(file), Logic.convertToRow((char) (rank + change * 2)))};
			
			for (String start: possibilities)
			{
//				System.out.println(start + ", " + pawnEndpoint);
				Piece p = super.getPiece(start);
				if (p != null && p.getType().equals(Piece.Type.PAWN))
				{
					super.makeMove(start, pawnEndpoint, Logic.Type.MOVE.val + "", this);
//					System.out.println("made mive");
					break;
				}
			}
//			System.out.println("pawn moved," + super.toString());
		}
		else 
		{
			System.out.println("cannot identify");
		}
		
		
		return null;
	}
	
	public String getCurrentMove()
	{
		return moves.get(currentMoveIndex)[MOVE];
	}

	public int getCurrentMoveIndex()
	{
		return currentMoveIndex;
	}
	@Override
	public Piece.Type askWhatToTransformTo() {
		// TODO Auto-generated method stub
		return selectedType;
	}
}
