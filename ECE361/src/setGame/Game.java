package setGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	private Deck deck;
	private Field field;
	public Field getField() {
		return field;
	}

	private Dealer dealer;

	int baseSize = 12;
	boolean gameover;

	/*
	 * Constructor. This will generate the deck with a randomly permuted order.
	 * It will then deal out 12 cards out into the field.
	 */
	public Game() {
		this.gameover = false;
		this.deck = new Deck();
		this.field = new Field(baseSize);
		this.dealer = new Dealer(deck, field);
	}

	public void start() {
		dealer.deal();
	}

	public void readCommand() {
		String userIn = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter command: ");
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
			submitSet(values);
		}
		if (command.equals("v")) {
			validateField();
		}
	}

	private int submitSet(String[] values) {
		boolean validInput = false;
		int[] indexSet = new int[3];
		int expectedValues = 3;
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		if (values.length == expectedValues) {
			validInput = true;
			for (int i = 0; i < expectedValues; i++) {
				int num = Integer.valueOf(values[i]);
				if (indexList.contains(num) || num > field.getCapacity() || num < 0 || field.get(num) == null) {
					validInput = false;
					break;
				}
				indexList.add(num);
			}
		}
		if (!validInput) {
			System.out.println("Must select 3 distinct valid cards.");
			return 2;
		}
		else {
			for (int i = 0; i < 3; i++) {
				indexSet[i] = indexList.get(i);
			}
			if (GameLogic.isSet(field.getCards(indexSet))) {
				System.out.println("SET FOUND!");
				dealer.removeCardsFromField(indexSet);
				dealer.deal();
				return 0;
			} else {
				System.out.println("INVALID SET!");
				return 1;
			}
		}
	}
	
	private void validateField() {
		// Deal extra cards if no set on field
		if (!field.existSet()) {
			dealer.dealExtra(3);
		}
	}
	
	public boolean isGameOver() {
		if (deck.size() > 0 || field.existSet()) {
			gameover = false;
		}
		else {
			gameover = true;
		}
		return gameover;
	}
	
	public void print() {
		field.print();
		field.printSets();
	}
}
