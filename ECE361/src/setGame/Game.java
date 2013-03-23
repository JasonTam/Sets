package setGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Game {
	Deck deck;
	Field field;
	Dealer dealer;

	int baseSize = 12;
	boolean gameover;

	/*
	 * Constructor. This will generate the deck with a randomly permuted order.
	 * It will then deal out 12 cards out into the field.
	 */
	public Game() {
		gameover = false;
		deck = new Deck();
		field = new Field(baseSize);
		dealer = new Dealer(deck, field);
	}

	public void play() {
		String command = "";
		while (deck.size() > 0 || field.existSet()) {
			dealer.deal();
			// dealer.validateField();
			field.print();
			field.printSets();
			readCommand();
		}
		gameover = true;
		System.out.println("Game over");
	}

	private void readCommand() {
		String userIn = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter space delimited card indices: ");
		try {
			userIn = reader.readLine();
			parseInput(userIn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseInput(String input) {
		String data[] = input.split(" ");
		String command = data[0];
		String values[] = Arrays.copyOfRange(data, 1, data.length);
		routeCommand(command, values);
	}

	private void routeCommand(String command, String[] values) {
		if (command.equals("s")) {
			System.out.println("check");
			submitSet(values);
		}
	}

	private void submitSet(String[] values) {		
		boolean validInput = false;
		int[] indexSet = new int[3];
		int expectedValues = 3;
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		if (values.length == expectedValues) {
			validInput = true;
			for (int i = 0; i < expectedValues; i++) {
				int num = Integer.valueOf(values[i]);
				if (indexList.contains(num) || num > field.size() || num < 0) {
					validInput = false;
					break;
				}
				indexList.add(num);
			}
		}
		
		if (!validInput) {
			System.out.println("Must select 3 distinct valid cards.");
			return;
		}
		else {
			for (int i = 0; i < 3; i++) {
				indexSet[i] = indexList.get(i);
			}
			if (GameLogic.isSet(indextoCard(indexSet))) {
				System.out.println("SET FOUND!");
				dealer.removeCardsFromField(indexSet);
			} else {
				System.out.println("INVALID SET!");
			}
		}
	}

	private Card[] indextoCard(int[] indexSet) {
		Card[] cardSet = new Card[indexSet.length];
		for (int i = 0; i < indexSet.length; i++) {
			cardSet[i] = field.get(indexSet[i]);
		}
		return cardSet;
	}

}
