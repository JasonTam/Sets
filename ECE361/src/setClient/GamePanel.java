package setClient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import setGame.Card;
import setGame.Field;
import setGame.Game;
import setGame.GameLogic;

// TODO 
// Need to go through game flow 
// Need event listeners to connect to state of the game

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	final static int maxGap = 100;

	public static PrintWriter out;
	public static BufferedReader in;

	JButton submitButton = new JButton("Submit Set");
	JButton clearButton = new JButton("Clear Selection");
	GridLayout boardLayout = new GridLayout(3, 5);

	public GamePanel() {
//		TODO The game init should probably be outside
		Game game1 = new Game();
		game1.start();
		
		setLayout(boardLayout);
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(2, 1));

		// Set up components preferred size
		JButton b = new JButton("Just fake button");
		Dimension buttonSize = b.getPreferredSize();
		setPreferredSize(new Dimension((int) (buttonSize
				.getWidth() * 2.5) + maxGap,
				(int) (buttonSize.getHeight() * 3.5) + maxGap * 2));

		final Map<String, JToggleButton> cardButtons = new HashMap<String, JToggleButton>();
		final Collection<Card> selectedCards = new HashSet<Card>();
		// Add buttons to experiment with Grid Layout
		

		for (final Card c : game1.getField().getCards()) {
			ImageIcon card_img = new ImageIcon
                    ("src/resources/images_cards/"+c.toString()+".gif");
			final JToggleButton bC = new JToggleButton(card_img);
//			final JToggleButton bC = new JToggleButton(c.toString());
			add(bC);
			cardButtons.put(c.toString(), bC);
			bC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (bC.isSelected()) {
						System.out.println(c.toString() + " selected");
						selectedCards.add(c);
					} else {
						System.out.println(c.toString() + " unselected");
						selectedCards.remove(c);
					}
				}
			});
		}

		// Add submit
		controls.add(submitButton);
		submitButton.setPreferredSize(new Dimension(50, 80));
		controls.add(clearButton);
		clearButton.setPreferredSize(new Dimension(50, 80));

		// Process submit button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selected Cards: ");
				for (Card c : selectedCards) {
					System.out.println(c);
				}
				if (selectedCards.size() == 3) {
					if (GameLogic.isSet(selectedCards))
						System.out.println("SET FOUND");
					else
						System.out.println("INVALID SET");
				} else {
					System.out
							.println("Invalid Set! Sets must contain exactly 3 cards.");
				}
				try {
					System.out.println("This was recieved in the client");
					System.out.println(in.readLine());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					e1.printStackTrace();
				}

			}
		});
		
		// Process clear button
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selectedCards.removeAll(selectedCards);
						for (JToggleButton bC : cardButtons.values())
							bC.setSelected(false);
					}
				});
				
//		add(compsToExperiment, BorderLayout.NORTH);
		add(new JSeparator(), BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
	}
	
}
