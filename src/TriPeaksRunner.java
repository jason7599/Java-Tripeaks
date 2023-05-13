package triPeaks;

import javax.swing.JFrame;

public class TriPeaksRunner {
	
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 800;
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("TriPeaks");
		
		BoardPanel board = new BoardPanel();
		
		frame.getContentPane().add(board);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		frame.setResizable(false);
		
		frame.setVisible(true);
		
	}

}
