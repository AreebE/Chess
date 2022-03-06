package game.graphics;

import javax.swing.JPanel;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Component;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Container;
import javax.swing.BoxLayout;

public class EncompassingPanel extends JFrame
{
	
	private class RatioSetter implements ComponentListener 
	{
		private static final double MARGIN_OF_ERROR = 0.003; 

		private final int numerator;
		private final int denominator;
		
		/**
		 * Set the ratio for width/height.
		 * 
		 * @param num 	the numerator of this ratio
		 * @param den 	the denominator of this ratio
		 */
		public RatioSetter(int num, int den)
		{
			this.numerator = num;
			this.denominator = den;
		}
		
		// width / height = ratio
		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
			double ratio = numerator * 1.0 / denominator;
			Component component = e.getComponent();

			int width = component.getWidth();
			int height = component.getHeight();

			double currentRatio = width * 1.0 / height;
			if (Math.abs(height * 1.0 / width - ratio) > MARGIN_OF_ERROR)
			{
				// 
//				System.out.println("called, " + width + " and " + height);
				if (width > height)
				{
					width = (int) (height * ratio);
				}
				else 
				{
					height = (int) (width / ratio); 					
				}
				
//				System.out.println("CHANGED, " + width + " and " + height);

				component.setSize(width, height);
			}
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
    public EncompassingPanel()
    {
        super("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));
        BoardDrawer b = new BoardDrawer();
//        b.setSize(new Dimension(400, 400));
//        b.setMaximumSize(new Dimension(400, 400));
//        b.setMinimumSize(new Dimension(400, 400));
        b.addComponentListener(new RatioSetter(1, 1));
//        b.setBo
        
        c.add(b);        
        c.setSize(new Dimension(600, 400));
        c.setMaximumSize(new Dimension(600, 400));
        c.setMinimumSize(new Dimension(600, 400));

        setContentPane(c);
    }
}