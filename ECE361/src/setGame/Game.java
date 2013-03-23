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
	Field field;
	Dealer dealer;
	
	int baseSize = 12;
	int setsFound;
	boolean gameover;
	int[] submission;

	/*
	 * Constructor. This will generate the deck with a randomly permuted order.
	 * It will then deal out 12 cards out into the field.
	 */
	public Game() {
		setsFound = 0;
		gameover = false;
		submission = new int[3];
		deck = new Deck();
		field = new Field(baseSize);
		dealer = new Dealer(deck, field);
	}

	public void play() {
		while (deck.size() > 0 || field.existSet()) {
			dealer.deal();
			//dealer.validateField();
			field.print();
			field.printSets();
			submission = getSubmission();
			if (GameLogic.isSet(indextoCard(submission))) {
				System.out.println("SET FOUND!");
				dealer.removeCardsFromField(submission);
				setsFound++;
			} else {
				System.out.println("INVALID SET!");
			}
		}
		gameover = true;
		System.out.println("Game over");
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
				if (indexList.contains(num) || num > field.size() || num < 0)
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

	public int getSetsFound() {
		return setsFound;
	}
}
