package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import fileLoader.GameReader;

// Add the autotimer + notifier of who won.
public class FileLoaderDisplay extends JPanel
{

	private static final Color BLACK = new Color(0, 0, 0);
	private static final Color WHITE = new Color(255, 255, 255);
	private static final Color DARK_BLUE = new Color(15, 109, 163);
	private static final Color LIGHT_BLUE = new Color(141, 204, 240);
	
	private class TimerTaskTemplate extends TimerTask
	{

		private Updater u;
		public TimerTaskTemplate(Updater u)
		{
			this.u = u;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			u.advance();
			if (list.getSelectedIndex() < listOfItems.size() - 1)
			{
				selectItem(list.getSelectedIndex() + 1);
			}

		}
		
	}
	private class CellRenderer
								extends JLabel
								implements ListCellRenderer<String>
	{

		@Override
		public Component getListCellRendererComponent(
				JList<? extends String> list, 
				String value, 
				int index,
				boolean isSelected, 
				boolean cellHasFocus) {
			this.setText(value);
			this.setOpaque(true);
			
			Font oldFont = getFont();
			this.setFont(new Font(oldFont.getName(), oldFont.getStyle(), 20));
			boolean isDark = index % 2 == 1;
			if (isSelected)
			{
				this.setBackground((isDark)? DARK_BLUE: LIGHT_BLUE);
			}
			else 
			{
				this.setBackground((isDark)? BLACK: WHITE);
			}
			this.setForeground((isDark)? WHITE: BLACK);

			return this;
		}
		
	}
	public interface Updater
	{
		public void advance();
		public void select(int i);
		public void changeFile(String newNAme);
	}
	private DefaultListModel<String> listOfItems;
	private ListModel<String> aa;
	private JFileChooser fileChooser;
	private JList<String> list;
	
	
	public FileLoaderDisplay(Updater u)
	{
		this.setLayout(new GridLayout(3, 1));
		list = new JList<>();
		this.listOfItems = new DefaultListModel<>();
		listOfItems.addElement("Start:");
		list.setCellRenderer(new CellRenderer());
		list.addListSelectionListener(new ListSelectionListener()
				{

					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
//						System.out.println("e");
						u.select(list.getSelectedIndex());
						selectItem(list.getSelectedIndex());
					}
				
				});
		list.setModel(listOfItems);
//		list.setSelectedIndex(0);
		JButton button = new JButton();
		button.setText("Advance");
		button.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						u.advance();
//						System.out.println(list.getSelectedIndex() + 1 + " anno");
						if (list.getSelectedIndex() < listOfItems.size() - 1)
						{
							selectItem(list.getSelectedIndex() + 1);
						}
					
					}
				
				});
	
		JScrollPane scroller = new JScrollPane();
		scroller.setViewportView(list);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PGN files (*.pgn)", "*.pgn");  
		JButton fileOpener = new JButton();
		fileOpener.setText("Choose File:");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileOpener.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						int result = fileChooser.showOpenDialog(FileLoaderDisplay.this);
						if (result == JFileChooser.APPROVE_OPTION)
						{
							System.out.println(fileChooser.getSelectedFile().toString());
							u.changeFile(fileChooser.getSelectedFile().toString());
							System.out.println("e");
						}
					}
			
				}
		);
		
		JButton autoAdvanceButton = new JButton();
		autoAdvanceButton.setText("Auto advance:");
	
		boolean[] isAuto = new boolean[] {false};
		autoAdvanceButton.addActionListener(new ActionListener() {
			private Timer timer = new Timer();
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				isAuto[0] = !isAuto[0];
//				System.out.println(timer);
				if (isAuto[0])
				{
					timer.schedule(new TimerTaskTemplate(u), 0, 1000);
				}
				else 
				{
					timer.cancel();
					timer = new Timer();
				
				}
			}
			
		});
		JPanel bottomRow = new JPanel();
		bottomRow.setLayout(new GridLayout(1, 2));
		bottomRow.add(autoAdvanceButton);
		bottomRow.add(fileOpener);
		this.add(scroller);
		this.add(button);
		this.add(bottomRow);
	}
	

	public void addItem(String newItem)
	{
		listOfItems.addElement(newItem);
//		selectItem(listOfItems.size() - 1);
	
	}
	
	public int movesLoaded()
	{
		return listOfItems.size();
	}
	
	public void clear()
	{
		listOfItems.clear();
		listOfItems.addElement("Start:");
	}
	
	public void selectItem (int index)
	{
//		System.out.println("aknkjn " + index);
		if (index < listOfItems.size() && index >= 0)
		{
			list.setSelectedIndex(index);
		}
	}
}
