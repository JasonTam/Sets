package setGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


//TODO need to add set submission
//TODO remove submitted sets from arraylist etc..

public class Game {
	ArrayList<Card> deck = new ArrayList<Card>();
	/*
	Rather than having a field, just play indexing games
	just have an index=15 that may sometimes change to
	18 or 21
	*/
	int index = 15;
	int score = 0;
	boolean gameover = false;

	
	/**
	 * Initializes the game
	 * This will generate the deck with a randomly 
	 * permuted order. It will then deal out 15
	 * cards out into the field.
	 */
	public void init(){
		index = 15;
		score = 0;
		gameover = true;
		for (int i=0; i<(int)Math.pow(Card.nVals, Card.nAtts); i++) {
			deck.add(new Card(i));
		}
//		printDeck();
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
		System.out.println("------[Playing Field]------");
		dispField();
	}
	
	boolean isSet(Card a, Card b, Card c) {
		boolean valid = true;
		for (int i=0; i<Card.nAtts; i++) {
			if (!(allDiff(i,a,b,c)||allSame(i,a,b,c)))
				valid = false;
		}
		return valid;
	}
	

//	for computational efficiency, may want to combine
//	these two boolean methods
	private boolean allDiff(int i, Card a, Card b, Card c) {
		if (a.attributes.get(i)!=b.attributes.get(i)&&
				a.attributes.get(i)!=c.attributes.get(i)&&
				b.attributes.get(i)!=c.attributes.get(i))
			return true;
		else
			return false;
	}

	private boolean allSame(int i, Card a, Card b, Card c) {
		if (a.attributes.get(i)==b.attributes.get(i)&&
				a.attributes.get(i)==c.attributes.get(i))
			return true;
		else
			return false;
	}

	void removeSet(Card a, Card b, Card c) {
		// TODO
	}
	

	
//	Should rarely happen
	void dealXtra(){
		index += 3;
		dispField();
	}
	
	void dispField() {
		for (int i = 0; i<index; i++){
			System.out.println(deck.get(i));
		}
	}
	
//	for troubleshooting
	void printDeck() { 
		for (int i = 0; i<81; i++){
			System.out.println(deck.get(i));
		}
	}
}
