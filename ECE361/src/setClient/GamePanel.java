package setClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import setGame.Card;
import setGame.Game;
import setGame.GameLogic;
import setServer.JSONinterface;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	final static int maxGap = 100;

	// public static PrintWriter out;
	// public static BufferedReader in;

	JButton submitButton = new JButton("[S]ubmit Set");
	JButton clearButton = new JButton("[C]lear Selection");
	JButton startButton = new JButton("Start Game!");
	JButton leaveButton = new JButton("Leave room!");
	GridLayout boardLayout = new GridLayout(3, 5);
	final JPanel gamePanel = new JPanel();
	JPanel controls = new JPanel();

	public static String roomName;
	public static Game curGame;

	private Collection<Card> selectedCards;
	private Map<Card, JToggleButton> cardButtons;
	private Map<Card, Integer> cardInd;
	private boolean cheat = false;

	public GamePanel(String rName) {
		roomName = rName;

		setLayout(new BorderLayout());

		gamePanel.setLayout(boardLayout);

//		controls.setLayout(new GridLayout(1, 4));
		controls.setLayout(new GridBagLayout());

		// Set up components preferred size
		JButton b = new JButton("Just fake button");
		Dimension buttonSize = b.getPreferredSize();
		setPreferredSize(new Dimension((int) (buttonSize.getWidth() * 2.5)
				+ maxGap, (int) (buttonSize.getHeight() * 3.5) + maxGap * 2));

		cardButtons = new HashMap<Card, JToggleButton>();
		cardInd = new HashMap<Card, Integer>();
		selectedCards = new LinkedList<Card>();
		// Add buttons to experiment with Grid Layout

		// Add submit
		JLabel empty = new JLabel();
		controls.add(startButton);
		startButton.setPreferredSize(new Dimension(500, 100));
		controls.add(leaveButton);

		submitButton.setEnabled(false);
		clearButton.setEnabled(false);

		// Process submit button
		Action submitAction = new AbstractAction(submitButton.getLabel()) {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Selected Cards: ");
				for (Card c : selectedCards) {
					System.out.println(c);
				}
				if (selectedCards.size() == 3) {
					System.out.println("Submitting: " + selectedCards);
	
					java.lang.reflect.Type collectionType = new com.google.gson.reflect.TypeToken<Collection<Card>>() {
					}.getType();
	
					String jsonSubmitSet = JSONinterface.genericToJson(
							"submit", selectedCards, collectionType);
					InitGame.out.println(jsonSubmitSet);
					if (GameLogic.isSet(selectedCards))
						System.out.println("SET FOUND");
					else
						System.out.println("INVALID SET");
				} else {
					System.out
							.println("Invalid Set! Sets must contain exactly 3 cards.");
				}
			}
		};
		submitButton.setAction(submitAction);
		

		// Process clear button
		Action clearAction = new AbstractAction(clearButton.getLabel()) {
			public void actionPerformed(ActionEvent e) {
				clearSelected();
			}
		};
		clearButton.setAction(clearAction);

		// Process Start button
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitGame.out.println(JSONinterface.genericToJson("startGame",
						roomName));
				InitGame.out.println(JSONinterface.genericToJson("rooms",
						"updating rooms"));

			}
		});

		// Process leave button
		leaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitGame.out.println(JSONinterface.genericToJson("leave",
						roomName));
				InitGame.changeCards("LOBBY");
			}
		});

		// add(compsToExperiment, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		// JPanel holder = new JPanel();
		// holder.add(arg0)
		add(controls, BorderLayout.SOUTH);
	}

	public void setupGame() {
		int index = 0;
		for (final Card c : curGame.getField().getCards()) {
			if (c == null) {
				continue;
			}
			System.out.println(curGame.getField().getCards());
			final JToggleButton bC = new JToggleButton(getImg(c));
			addElement(c, bC,index);
			gamePanel.revalidate();
			index++;
		}
//		startButton.setEnabled(false);
		controls.remove(startButton); controls.revalidate();
		controls.add(clearButton,0);
		
		submitButton.setPreferredSize(new Dimension(400, 100));
		controls.add(submitButton,0);
		// Bind a keystroke to the button to act as accelerator.
        int c = JComponent.WHEN_IN_FOCUSED_WINDOW;
        KeyStroke submitkey = KeyStroke.getKeyStroke('s');
        KeyStroke clearkey = KeyStroke.getKeyStroke('c');
        
        controls.getInputMap(c).put(submitkey, "SUBMIT");
        controls.getActionMap().put("SUBMIT", submitButton.getAction());
        controls.getInputMap(c).put(clearkey, "CLEAR");
        controls.getActionMap().put("CLEAR", clearButton.getAction());
        
        
        
		submitButton.setEnabled(true);
		clearButton.setEnabled(true);
		
//		FOR CHEATING*****************************************
		if (cheat && !InitGame.runningFromJar) {cheatSet();}
		
	}

	
	public void updateGame()
	{
		clearSelected();

		ArrayList<Card> cardsUpdated = curGame.getField().getCards();
		Set<Card> remCards = new HashSet<Card>();
		
		if (cardsUpdated.size()!=cardButtons.keySet().size()) {
			gamePanel.removeAll();
			selectedCards.removeAll(selectedCards);
			cardButtons.keySet().removeAll(cardButtons.keySet());
			cardInd.keySet().removeAll(cardInd.keySet());
			setupGame();
		} else {
		
			int index = 0;
	//		Analyze cards that are no longer on the field
			for (Card c : cardButtons.keySet()) {
				if (cardsUpdated.contains(c)) {
					System.out.println("Repeat: " + c);
					cardsUpdated.remove(c);
				} else {
					System.out.println(
							"New card: " + c + " @index: " + index);
					remCards.add(c);
				}
				index++;
			}
			
	//		Fill in the voids with new cards
			
			for (Card c : remCards) {
				gamePanel.remove(cardButtons.get(c));
				cardButtons.remove(c);
				if (cardsUpdated.size()!=0) {
					Card pop = cardsUpdated.remove(0);
					final JToggleButton bC = new JToggleButton(getImg(pop));
					addElement(pop, bC, cardInd.get(c));
				}
				cardInd.remove(c);
			}
			
	//		Extra Cards (if greater than usual number of cards)
			if (cardsUpdated.size()!=0) {
				int offset = 1;
				for (Card c : cardsUpdated) {
					final JToggleButton bC = new JToggleButton(getImg(c));
					addElement(c, bC);
					cardInd.put(c, cardInd.size()+offset);
					offset++;
				}
			}
		}
		
//		FOR CHEATING*****************************************
		if (cheat && !InitGame.runningFromJar) {cheatSet();}
	}
	
	public void endGame() {
		// Im not sure if remove all even happens...
		gamePanel.removeAll();
		System.out.println("Game over happened!");
		submitButton.setEnabled(false);
		clearButton.setEnabled(false);
	}

	public void addElement(final Card c, final JToggleButton bC, int... index) {
		System.out.println(index);
		System.out.println(index.length);
		if (index.length == 0) {
			gamePanel.add(bC);
			System.out.println("Adding to end");
		} else {
			gamePanel.add(bC, index[0]);
			cardInd.put(c, index[0]);
			System.out.println("Adding to index: " + index[0]);
		}
		cardButtons.put(c, bC);
		bC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bC.isSelected()) {
					if (selectedCards.size() >= 3) {
						Card pop = ((LinkedList<Card>) selectedCards)
//								.removeFirst();
								.removeLast();
						cardButtons.get(pop).setSelected(false);
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

	private void clearSelected() {
		selectedCards.removeAll(selectedCards);
		for (JToggleButton bC : cardButtons.values())
			bC.setSelected(false);
	}
	
	private ImageIcon getImg(Card c) {
	    ImageIcon card_img;
	    if (InitGame.runningFromJar)
	    {
	        card_img = new ImageIcon(getClass().getResource( "/src/resources/images_cards/" + c.toString() + ".gif"));
	    }
	    else
	    {
	        card_img = new ImageIcon(getClass().getResource( "/resources/images_cards/" + c.toString() + ".gif"));
	    }
            
		return card_img;
	}
	
	private void cheatSet() {
		clearSelected();
		System.out.println("Cheating so hard: ");
		System.out.println("cheatset::" + curGame.getField().getCards());
		for (Card c : getOneSet()) {
			System.out.println(c);
			cardButtons.get(c).setSelected(true);
			selectedCards.add(c);
		}
	}
	
	private ArrayList<Card> getOneSet() {
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.addAll(cardButtons.keySet());
		ArrayList<Card> set = new ArrayList<Card>();
		for (int i = 0; i < cards.size() - 2; i++) {
			for (int j = i + 1; j < cards.size() - 1; j++) {
				for (int k = j + 1; k < cards.size(); k++) {
					if (GameLogic.isSet(cards.get(i), cards.get(j), cards.get(k))) {
						set.add(cards.get(i));
						set.add(cards.get(j));
						set.add(cards.get(k));
						return set;
					}
				}
			}
		}
		return set;
	}
	
	
}





















