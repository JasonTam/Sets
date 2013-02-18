package setClient;

import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;

public class MainWindow {
	public MainWindow() {
		JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JLabel emptyLabel = new JLabel("This is the main window");
        emptyLabel.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	public static void main(String[] args){
		new MainWindow();
	}
}
