package setClient;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

	// public static PrintWriter out;
	// public static BufferedReader in;

	JButton submitButton = new JButton("Submit Set");
	JButton clearButton = new JButton("Clear Selection");
	JButton startButton = new JButton("Start Game!");
	JButton leaveButton = new JButton("Leave room!");
	GridLayout boardLayout = new GridLayout(3, 5);
	final JPanel gamePanel = new JPanel();

	public static String roomName;
	public static Game curGame;

	private Collection<Card> selectedCards;
	private Map<Card, JToggleButton> cardButtons;
	private Map<Card, Integer> cardInd;

	public GamePanel(String rName) {
		roomName = rName;

		setLayout(new BorderLayout());

		// TODO The game init should probably be outside
		// final Game game1 = new Game();

		gamePanel.setLayout(boardLayout);

		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(4, 1));

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
		controls.add(submitButton);
		controls.add(clearButton);
		controls.add(leaveButton);

		submitButton.setEnabled(false);
		clearButton.setEnabled(false);

		// clearButton.setSize(new Dimension(50, 80));

		// Process submit button
		submitButton.addActionListener(new ActionListener() {
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
				/*
				 * try { System.out.println("This was recieved in the client");
				 * System.out.println(InitGame.in.readLine()); } catch
				 * (IOException e1) { e1.printStackTrace(); } catch
				 * (NullPointerException e1) { e1.printStackTrace(); }
				 */

			}
		});

		// Process clear button
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearSelected();
			}
		});

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
			// JOptionPane.showMessageDialog(this,
			// getClass().getResource("/src/resources/images_cards/"+c.toString()+".gif"));
			// Use this config for the runnable jar
			// (getClass().getResource("/resources/images_cards/"+c.toString()+".gif"));
			final JToggleButton bC = new JToggleButton(getImg(c));
			addElement(c, bC,index);
			gamePanel.revalidate();
			index++;
		}
		startButton.setEnabled(false);
		submitButton.setEnabled(true);
		clearButton.setEnabled(true);
	}

//	public void updateGame() {
//		// TODO
//		// May want to only send deltas rather than entire game
//		clearSelected();
//		gamePanel.removeAll();
//		setupGame();
//	}

	/*public void brkn_updateGame() {
		clearSelected();

		ArrayList<Card> cardsUpdated = curGame.getField().getCards();

		Object[] cardsOnField = cardButtons.keySet().toArray();
		for (int index = 0; index < cardButtons.size(); index++) {
			if (index > cardsUpdated.size()) {
				break;
			}
			String curCardValue = cardsOnField[index].toString();
			Card newCard = cardsUpdated.get(index);
			String newCardValue = newCard.toString();
			System.out.println(newCardValue + " " + curCardValue);
			if (curCardValue == newCardValue) {
				// do nothing
				System.out.println("Already contains: " + curCardValue);
				continue;
			} else {
				System.out.println("New card: " + newCardValue);
				// System.out.println(curGame.getField().getCards());
				ImageIcon card_img = new ImageIcon(getClass().getResource(
						"/resources/images_cards/" + newCardValue + ".gif"));
				final JToggleButton bC = new JToggleButton(card_img);
				addElement(newCard, bC, index);
				gamePanel.revalidate();
			}
		}
	}*/

	/*public void updateGame() {
		clearSelected();

		ArrayList<Card> cardsUpdated = curGame.getField().getCards();
//		Set<Card> cardsCurrent = cardButtons.keySet();
		Object[] cardsCurrent = cardButtons.keySet().toArray();
		Map<Card,Integer> remCards = new HashMap<Card,Integer>();
		Set<Card> sameCards = new HashSet<Card>();
		
//		for (Card c : cardsCurrent) {
		System.out.println("SIZE OF UPDATE1: " + cardsUpdated.size());
		for (int i = 0; i < cardButtons.size(); i++) {
			Card c = (Card) cardsCurrent[i];
			if (!cardsUpdated.contains(c)) {
				remCards.put(c,i);
				System.out.println("remove index add: " + i);
			} else {
				sameCards.add(c);
			}
		}
		for (Card c : sameCards)
			cardsUpdated.remove(c);
		
		System.out.println("SIZE OF UPDATE2: " + cardsUpdated.size());
		for (Card c : remCards.keySet()) {
			System.out.println("removing index: " + remCards.get(c));
			gamePanel.remove(cardButtons.get(c));
			cardButtons.remove(c);
			Card pop = cardsUpdated.remove(0);
			
			final JToggleButton bC = new JToggleButton(getImg(pop));
			addElement(pop, bC, remCards.get(c));
		}
		
		System.out.println("CARDS REMAINING TO UPDATE: " + cardsUpdated.size());
		
		for (Card c : cardsUpdated) {
			final JToggleButton bC = new JToggleButton(getImg(c));
			addElement(c, bC);
		}

	}
*/	
/*	
	public void updateGame()
	{
		clearSelected();

		ArrayList<Card> cardsUpdated = curGame.getField().getCards();
		Map<Card,Integer> remCards = new HashMap<Card,Integer>();
		
		int index = 0;
//		Analyze cards that are no longer on the field
		for (Card c : cardButtons.keySet()) {
			if (cardsUpdated.contains(c)) {
				System.out.println("Repeat: " + c);
				cardsUpdated.remove(c);
			} else {
				System.out.println(
						"New card: " + c + " @index: " + index);
				remCards.put(c,index);
			}
			index++;
		}
		System.out.println("CARDS TO UPDATE2: " + cardsUpdated.size());
		for (Card c : remCards.keySet()) {
			gamePanel.remove(cardButtons.get(c));
			cardButtons.remove(c);
			Card pop = cardsUpdated.remove(0);
			final JToggleButton bC = new JToggleButton(getImg(pop));
			addElement(pop, bC, remCards.get(c));
		}
		
		System.out.println("CARDS REMAINING TO UPDATE: " + cardsUpdated.size());
//		Extra Cards
		for (Card c : cardsUpdated) {
			final JToggleButton bC = new JToggleButton(getImg(c));
			addElement(c, bC);
		}
	}
	*/
	
	
	public void updateGame()
	{
		clearSelected();

		ArrayList<Card> cardsUpdated = curGame.getField().getCards();
		Set<Card> remCards = new HashSet<Card>();
		
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
			Card pop = cardsUpdated.remove(0);
			final JToggleButton bC = new JToggleButton(getImg(pop));
			addElement(pop, bC, cardInd.get(c));
			cardInd.remove(c);
		}
		
//		Extra Cards (if greater than usual number of cards)
		int offset = 1;
		for (Card c : cardsUpdated) {
			final JToggleButton bC = new JToggleButton(getImg(c));
			addElement(c, bC);
			cardInd.put(c, cardInd.size()+offset);
			offset++;
		}
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
								.removeFirst();
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
		ImageIcon card_img = new ImageIcon(getClass().getResource(
				"/resources/images_cards/" + c.toString() + ".gif"));
		return card_img;
	}
	
	
}
