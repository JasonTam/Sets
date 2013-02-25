package setGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


//TODO need to detect if there are no sets on the field
// and auto deal 3 new cards if so...

public class Game {
	static ArrayList<Card> deck = new ArrayList<Card>();
	/*
	Rather than having a field, just play indexing games
	just have an index=15 that may sometimes change to
	18 or 21
	*/
	static int cardsPerField = 15;
	static int index = cardsPerField;		// Initial field size
	int score = 0;
	boolean gameover = false;
	int[] submission;

	public void playGame() {
		init();
		validateField();
		dispField();
		while (deck.size()>0) {
			submission = getSubmission();
			 if (isSet(indextoCard(submission))) {
				 removeSet(submission);
				 System.out.println("SET FOUND!");
				 score++;
			 } else {
				 System.out.println("INVALID SET!");
			 }
			 validateField();
			 dispField();
		}
		gameover = true;
		System.out.println("Game over");
	}
	


	/**
	 * Initializes the game
	 * This will generate the deck with a randomly 
	 * permuted order. It will then deal out 15
	 * cards out into the field.
	 */
	public void init(){
		index = cardsPerField;
		score = 0;
		gameover = false;
		submission = new int[3];
		for (int i=0; i<(int)Math.pow(Card.nVals, Card.nAtts); i++) {
			deck.add(new Card(i));
		}
//		printDeck();
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
	}
	
	private int[] getSubmission() {
		String userIn = null;
		int[] indexSet = new int[3];
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		boolean validInput = false;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(!validInput) {
			System.out.print("Enter space delimited card indices: ");
			try {
				userIn = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String data[] = userIn.split(" ");
	        validInput = true;
	        for (int i=0; i<3; i++) {
	        	int num = Integer.valueOf(data[i]);
	        	if (indexList.contains(num)||num>index||num<0)
	        		validInput = false;
	        	indexList.add(num);
	        }
	        if (!validInput)
	        	System.out.println("Must select 3 distinct valid cards");
		}
		for (int i=0;i<3;i++)
			indexSet[i] = indexList.get(i);
		return indexSet;
	}
	
	private Card[] indextoCard(int[] indexSet) {
		Card[] cardSet = new Card[indexSet.length];
		for (int i=0; i<indexSet.length; i++) {
			cardSet[i] = deck.get(indexSet[i]);
		}
			
		return cardSet;
	}
	
	public static boolean isSet(Card a, Card b, Card c) {
		boolean valid = true;
		for (int i=0; i<Card.nAtts; i++) {
			if (!(allDiff(i,a,b,c)||allSame(i,a,b,c)))
				valid = false;
		}
		return valid;
	}
	
	public static boolean isSet(Card[] cardSet) {
		Card a = cardSet[0];
		Card b = cardSet[1];
		Card c = cardSet[2];
		return isSet(a,b,c);
	}
	
	public static boolean isSet(Collection<Card> selectedCards) {
		if (selectedCards.size()!=3)
			return false;
		else {
//			// I don't know why I'm not allowed to do this! (below)
//			Card[] cardSet = (Card[]) selectedCards.toArray();
//			return isSet(cardSet);
			Card[] cardSet = new Card[3];
			int i = 0;
			for (Card c : selectedCards) {
				cardSet[i++] = c;
			}
			return isSet(cardSet);
			
		}
	}
	
	
//	for computational efficiency, may want to combine
//	these two boolean methods
	private static boolean allDiff(int i, Card a, Card b, Card c) {
		if (a.attributes.get(i)!=b.attributes.get(i)&&
				a.attributes.get(i)!=c.attributes.get(i)&&
				b.attributes.get(i)!=c.attributes.get(i))
			return true;
		else
			return false;
	}

	private static boolean allSame(int i, Card a, Card b, Card c) {
		if (a.attributes.get(i)==b.attributes.get(i)&&
				a.attributes.get(i)==c.attributes.get(i))
			return true;
		else
			return false;
	}

	void removeSet(int[] indexSet) {
		for (int i:indexSet) {
			deck.remove(i);
		}
	}
	
	boolean existSet() {
		boolean exists = false;
		for (int i=0; i<index-2; i++) {
			for (int j=i+1; j<index-1; j++) {
				for (int k=j+1; k<index; k++) {
					if (isSet(deck.get(i),deck.get(j),deck.get(k))) {
						exists=true;
						System.out.println("(psst) There's a set: " + i+","+j+","+k);
					}
				}
			}
		}
		return exists;
	}
	
	private void validateField() {
		if (!existSet()) { // Deal extra cards if no set on field
			index += 3;
		} else if (index>cardsPerField) {
			index -= 3;
		}
		
	}
	
	void dispField() {
		System.out.println("------[Playing Field]------");
		for (int i = 0; i<index; i++){
			System.out.println(
					"Card #" + i + ":\t" + deck.get(i));
		}
	}
	
//	Getters
	public static ArrayList<Card> getDeck() {
		return deck;
	}

	public static int getIndex() {
		return index;
	}

	public int getScore() {
		return score;
	}
	
//	for troubleshooting
	void printDeck() { 
		for (int i = 0; i<81; i++){
			System.out.println(deck.get(i));
		}
	}
}
