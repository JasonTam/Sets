package setGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

	private ArrayList<Card> deck = new ArrayList<Card>();

	public Deck() {
		fill();
		shuffle();
	}

	public Card remove(int i) {
		return deck.remove(i);
	}
	
	public int size() {
		return deck.size();
	}

	private void fill() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 3; l++) {
						deck.add(new Card(i, j, k, l));
					}
				}
			}
		}
	}

	private void shuffle() {
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
	}

	public void print() {
		for (int i = 0; i < 81; i++) {
			System.out.println(deck.get(i));
		}
	}
}
