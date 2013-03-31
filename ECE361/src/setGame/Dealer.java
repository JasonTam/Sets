package setGame;

import java.util.Collection;

/**
 * Dealer.java Purpose: Handles interactions between deck and field.
 * 
 * @author Nick
 */
public class Dealer {

	private Deck deck;
	private Field field;

	/**
	 * @param deck
	 * @param field
	 */
	public Dealer(Deck deck, Field field) {
		this.deck = deck;
		this.field = field;
	}

	public void deal() {
		while (field.cardCount() < field.getBaseSize() && deck.size() > 0) {
			dealCard();
		}
	}

	public void dealExtra(int numCards) {
		for (int i = 0; i < numCards; i++) {
			dealCard();
		}
	}

	private void dealCard() {
		if (deck.size() > 0) {
			int indexOfNull = field.indexOf(null);
			if (0 <= indexOfNull && indexOfNull < field.getBaseSize()) {
				field.set(indexOfNull, deck.remove(0));
			} else {
				field.add(deck.remove(0));
			}
		}
	}

	public void removeCardsFromField(int[] indices) {
		for (int index : indices) {
			field.set(index, null);
		}
	}
	
	public void removeCardsFromField(Collection<Card> cards) {
		for (Card c : cards)
			field.set(field.indexOf(c), null);
	}
	
	
}
