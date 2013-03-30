package setGame;

import java.util.ArrayList;

public class Field {
	
	private ArrayList<Card> field;
	private int baseSize;
	
	public Field(int baseSize) {
		field = new ArrayList<Card>(baseSize);
		this.baseSize = baseSize;
	}

	public void add(Card card) {
		field.add(card);
	}
	
	public ArrayList<Card> getCards() {
		return field;
	}
	
	public int cardCount() {
		int size = 0;
		for (Card card : field) {
			if (card != null) {
				size++;
			}
		}
		return size;	
	}
	
	public void remove(int index) {
		field.remove(index);
	}
	
	public void set(int index, Card card) {
		field.set(index, card);
	}
	
	public Card get(int index) {
		return field.get(index);
	}
	
	public int indexOf(Card card) {
		return field.indexOf(card);
	}
 	
	public int getBaseSize() {
		return baseSize;
	}
	
	public int getCapacity() {
		return field.size();
	}
	
	public void print() {
		System.out.println("------[Playing Field]------");
		for (int i = 0; i < field.size(); i++) {
			System.out.println("Card #" + i + ":\t" + field.get(i));
		}
	}
	
	public ArrayList<String> findSets() {
		ArrayList<String> sets = new ArrayList<String>();
		for (int i = 0; i < field.size() - 2; i++) {
			for (int j = i + 1; j < field.size() - 1; j++) {
				for (int k = j + 1; k < field.size(); k++) {
					if (GameLogic.isSet(field.get(i), field.get(j), field.get(k))) {
						sets.add("" + i + " " + j + " " + k);
					}
				}
			}
		}
		return sets;
	}
	
	public boolean existSet() {
		boolean exists = false;
		ArrayList<String> sets = findSets();
		if (sets.size() > 0) {
			exists = true;
		}
		return exists;
	}
	

	public Card[] getCards(int[] indexSet) {
		Card[] cardSet = new Card[indexSet.length];
		for (int i = 0; i < indexSet.length; i++) {
			cardSet[i] = field.get(indexSet[i]);
		}
		return cardSet;
	}
	
	public void printSets() {
		ArrayList<String> sets = findSets();
		System.out.println("------[Sets]------");
		for (String set : sets) {
			System.out.println(set);
		}
	}
}
