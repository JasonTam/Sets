package setGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Game {
	Deck deck;
	ArrayList<Card> field = new ArrayList<Card>();

	int cardsPerField = 12;
	int index = cardsPerField; // Initial field size
	int setsFound = 0;
	boolean gameover = false;
	int[] submission;

	/*
	 * Constructor. This will generate the deck with a randomly permuted order.
	 * It will then deal out 15 cards out into the field.
	 */
	public Game() {
		index = cardsPerField;
		setsFound = 0;
		gameover = false;
		submission = new int[3];
		deck = new Deck();
		fillField();
	}

	public void playGame() {
		while (deck.size() > 0 || existSet()) {
			validateField();
			printField();
			submission = getSubmission();
			if (GameLogic.isSet(indextoCard(submission))) {
				System.out.println("SET FOUND!");
				removeCardsFromField(submission);
				setsFound++;
			} else {
				System.out.println("INVALID SET!");
			}
		}
		gameover = true;
		System.out.println("Game over");
	}

	private void fillField() {
		while (field.size() < cardsPerField) {
			field.add(deck.deal());
		}
	}

	private void removeCardsFromField(int[] indices) {
		for (int i : indices) {
			if (field.size() <= cardsPerField) {
				replaceCard(i);
			} else {
				field.remove(i);
			}
		}
	}

	private void replaceCard(int index) {
		if (deck.size() > 0) {
			field.set(index, deck.deal());
		} else {
		}
	}

	private boolean existSet() {
		boolean exists = false;
		for (int i = 0; i < field.size() - 2; i++) {
			for (int j = i + 1; j < field.size() - 1; j++) {
				for (int k = j + 1; k < field.size(); k++) {
					if (GameLogic.isSet(field.get(i), field.get(j),
							field.get(k))) {
						exists = true;
						System.out.println("(psst) There's a set: " + i + ","
								+ j + "," + k);
					}
				}
			}
		}
		return exists;
	}

	private void validateField() {
		// Deal extra cards if no set on field
		if (!existSet()) {
			dealMoreCardsToField(3);
		}
		/*
		 * else if (index > cardsPerField) { index -= 3; }
		 */
	}

	private void dealMoreCardsToField(int numCards) {
		for (int i = 0; i < numCards; i++) {
			if (deck.size() > 0) {
				field.add(deck.deal());
			}
		}
	}

	private int[] getSubmission() {
		String userIn = null;
		int[] indexSet = new int[3];
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		boolean validInput = false;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));

		while (!validInput) {
			System.out.print("Enter space delimited card indices: ");
			try {
				userIn = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String data[] = userIn.split(" ");
			validInput = true;
			for (int i = 0; i < 3; i++) {
				int num = Integer.valueOf(data[i]);
				if (indexList.contains(num) || num > index || num < 0)
					validInput = false;
				indexList.add(num);
			}
			if (!validInput)
				System.out.println("Must select 3 distinct valid cards");
		}
		for (int i = 0; i < 3; i++)
			indexSet[i] = indexList.get(i);
		return indexSet;
	}

	private Card[] indextoCard(int[] indexSet) {
		Card[] cardSet = new Card[indexSet.length];
		for (int i = 0; i < indexSet.length; i++) {
			cardSet[i] = field.get(indexSet[i]);
		}
		return cardSet;
	}

	private void printField() {
		System.out.println("------[Playing Field]------");
		for (int i = 0; i < field.size(); i++) {
			System.out.println("Card #" + i + ":\t" + field.get(i));
		}
	}

	public int getIndex() {
		return index;
	}

	public int getSetsFound() {
		return setsFound;
	}
}
