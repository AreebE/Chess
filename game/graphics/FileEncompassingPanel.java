package graphics;

import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import chess2.Logic;
import chess2.Piece;
import fileLoader.GameReader;

public class FileEncompassingPanel 
		extends JFrame
		implements FileLoaderDisplay.Updater,
		BoardDrawer.Updater{

	private BoardDrawer board;
	private FileLoaderDisplay display;
	private GameReader reader;
	private boolean fullyLoaded; 
	
	public FileEncompassingPanel(String moveFileName, String fileName, HashMap<Piece.Type, Integer[]> whites, HashMap<Piece.Type, Integer[]> blacks)
    {
		fullyLoaded = false;
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(1, 2));
		board = new BoardDrawer(this, fileName, whites, blacks);
		try {
			reader = new GameReader(moveFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		display = new FileLoaderDisplay(this);
		display.selectItem(0);
		System.out.println(board);
		container.add(board);
		container.add(display);
		this.setContentPane(container);
		fullyLoaded = true;
    }
	
	@Override
	public void advance() {
		// TODO Auto-generated method stub
		reader.advance();
		System.out.println(reader.getCurrentMoveIndex() +", " + display.movesLoaded());
		if (reader.getCurrentMoveIndex() >= display.movesLoaded())
		{
			display.addItem(reader.getCurrentMove());
		}
		board.invalidate();
	}
	
	@Override
	public Piece getPiece(int row, int col) {
		return reader.getPiece(Logic.toCoordinates(col, row));
	}

	@Override
	public void select(int i) {
		// TODO Auto-generated method stub
		if (!fullyLoaded)
		{
			return;
		}
		System.out.println("isdjigajiogj");
		reader.setCurrentMove(i);
		board.invalidate();
		
	}

	@Override
	public void changeFile(String newName) {
		try {
				reader.setFileName(newName);
				board.invalidate();
				display.clear();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot find file");
		}
	}

}
