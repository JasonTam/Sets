package setClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import setGame.Card;
import setGame.Game;
import setGame.GameLogic;
import setServer.JSONinterface;


// TODO 
// Need to go through game flow 
// Need event listeners to connect to state of the game

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	final static int maxGap = 100;

//	public static PrintWriter out;
//	public static BufferedReader in;

	JButton submitButton = new JButton("Submit Set");
	JButton clearButton = new JButton("Clear Selection");
	JButton startButton = new JButton("Start Game!");
	JButton leaveButton = new JButton("Leave room!");
	GridLayout boardLayout = new GridLayout(3, 5);
	final JPanel gamePanel = new JPanel();
	
	public static String roomName;
	public static Game curGame;
	private Collection<Card> selectedCards;
	private Map<String, JToggleButton> cardButtons;
	

	public GamePanel(String rName) {
	    roomName = rName;
 
        setLayout(new BorderLayout());
        
		
		
//		TODO The game init should probably be outside
//		final Game game1 = new Game();
		
		gamePanel.setLayout(boardLayout);
		
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(4, 1));

		// Set up components preferred size
		JButton b = new JButton("Just fake button");
		Dimension buttonSize = b.getPreferredSize();
		setPreferredSize(new Dimension((int) (buttonSize
				.getWidth() * 2.5) + maxGap,
				(int) (buttonSize.getHeight() * 3.5) + maxGap * 2));

		cardButtons = new HashMap<String, JToggleButton>();
		selectedCards = new LinkedList<Card>();
		// Add buttons to experiment with Grid Layout
		


		// Add submit
		JLabel empty = new JLabel();
		controls.add(startButton);
		controls.add(submitButton);
		controls.add(clearButton);
		controls.add(leaveButton);
		
		submitButton.setEnabled(false);
		clearButton.setEnabled(false);
		
		
	//	clearButton.setSize(new Dimension(50, 80));

		// Process submit button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selected Cards: ");
				for (Card c : selectedCards) {
					System.out.println(c);
				}
				if (selectedCards.size() == 3) {
					System.out.println("Submitting: " + selectedCards);
					
					java.lang.reflect.Type collectionType = new com.google.gson.reflect.TypeToken<Collection<Card>>(){}.getType();
				    					
					String jsonSubmitSet = JSONinterface.genericToJson("submit", selectedCards, collectionType);
					InitGame.out.println(jsonSubmitSet);
					if (GameLogic.isSet(selectedCards))
						System.out.println("SET FOUND");
					else
						System.out.println("INVALID SET");
				} else {
					System.out
							.println("Invalid Set! Sets must contain exactly 3 cards.");
				}
				/*
				try {
					System.out.println("This was recieved in the client");
					System.out.println(InitGame.in.readLine());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					e1.printStackTrace();
				}
				*/

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
				
				
//		Process Start button
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    InitGame.out.println(JSONinterface.genericToJson("startGame", roomName));
			    InitGame.out.println(JSONinterface.genericToJson("rooms", "updating rooms"));
			    
			}
		});
				
//		Process leave button
		leaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    InitGame.out.println(JSONinterface.genericToJson("leave", roomName));
			    InitGame.changeCards("LOBBY");
			}
		});
				
//		add(compsToExperiment, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
//		JPanel holder = new JPanel();
//		holder.add(arg0)
		add(controls, BorderLayout.SOUTH);
	}
	
		public void setupGame()
		{
			for (final Card c : curGame.getField().getCards()) {
				ImageIcon card_img = new ImageIcon
	                    ("src/resources/images_cards/"+c.toString()+".gif");
				final JToggleButton bC = new JToggleButton(card_img);
	//			final JToggleButton bC = new JToggleButton(c.toString());
				addElement(c, bC);
				gamePanel.revalidate();
			}
			startButton.setEnabled(false);
			submitButton.setEnabled(true);
			clearButton.setEnabled(true);
		}
		
		public void updateGame()
		{
//        	TODO
//        	May want to only send deltas rather than entire game
			gamePanel.removeAll();
			setupGame();
		}
		
		public void addElement(final Card c, final JToggleButton bC) {
			gamePanel.add(bC);
			cardButtons.put(c.toString(), bC);
			bC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (bC.isSelected()) {
						if (selectedCards.size()>=3) {
							Card pop = ((LinkedList<Card>) selectedCards).removeFirst();
							cardButtons.get(pop.toString()).setSelected(false);
						}
						System.out.println(c.toString() + " selected");
						selectedCards.add(c);
					} else {
						System.out.println(c.toString() + " unselected");
						selectedCards.remove(c);
					}
				}
			});
		}
	
}
