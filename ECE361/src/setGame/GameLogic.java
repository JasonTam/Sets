package setGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameLogic {

	public static boolean isSet(Card a, Card b, Card c) {
		boolean valid = true;
		for (int i = 0; i < Card.nAtts; i++) {
			if (!(allDiff(i, a, b, c) || allSame(i, a, b, c)))
				valid = false;
		}
		return valid;
	}

	public static boolean isSet(Card[] cardSet) {
		Card a = cardSet[0];
		Card b = cardSet[1];
		Card c = cardSet[2];
		return isSet(a, b, c);
	}

	public static boolean isSet(Collection<Card> selectedCards) {
		if (selectedCards.size() != 3)
			return false;
		else {
			// // I don't know why I'm not allowed to do this! (below)
			// Card[] cardSet = (Card[]) selectedCards.toArray();
			// return isSet(cardSet);
			Card[] cardSet = new Card[3];
			int i = 0;
			for (Card c : selectedCards) {
				cardSet[i++] = c;
			}
			return isSet(cardSet);

		}
	}

	/*
	 * for computational efficiency, may want to combine these two boolean
	 * methods.
	 */
	private static boolean allDiff(int i, Card a, Card b, Card c) {
		if (a != null && b != null && c != null
				&& a.attributes.get(i) != b.attributes.get(i)
				&& a.attributes.get(i) != c.attributes.get(i)
				&& b.attributes.get(i) != c.attributes.get(i)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean allSame(int i, Card a, Card b, Card c) {
		if (a != null && b != null && c != null
				&& a.attributes.get(i) == b.attributes.get(i)
				&& a.attributes.get(i) == c.attributes.get(i)) {
			return true;
		} else {
			return false;
		}
	}
}
