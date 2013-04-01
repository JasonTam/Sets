package setGame;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Card.java 
 * Purpose: Represents a Set card and attributes.
 * @author Jason
 * @author Nick
 */

public class Card {

	/**
	 * Possible values for each attribute are 0 up to nVals-1
	 */
	public static int nAtts = 4;
	public static int nVals = 3;

	/**
	 * The attribute values will be stored in an array list. An example of the
	 * contents of a card's attribute list: [0][1][2][1]
	 */
	private final ArrayList<Integer> attributes = new ArrayList<Integer>();

	public ArrayList<Integer> getAttributes() {
		return attributes;
	}

	/**
	 * Card constructor by radix decomposition. 
	 * @param i
	 *            is the unique index of the card Since cards are unique, the
	 *            attributes are derived by radix decomposition into the
	 *            attribute bins of the array list
	 */
	public Card(int i) {
		for (int att = 1; att <= nAtts; att++) {
			attributes.add(i % nVals);
			i /= nVals;
		}
	}

	/**
	 * Card constructor by explicit attribute assignment. 
	 * @param i 	value of first attribute
	 * @param j 	value of second attribute
	 * @param k 	value of third attribute
	 * @param k 	value of fourth attribute
	 */
	public Card(int i, int j, int k, int l) {
		attributes.add(i);
		attributes.add(j);
		attributes.add(k);
		attributes.add(l);
	}

	/*
	 * public int getIntId() { int id = 0; for (int att:attributes) id =
	 * id*3+att; return id; }
	 * 
	 * public String getStrId() { String strId = Integer.toString(getIntId());
	 * strId = (strId.length()<2)?"0"+strId:strId; return strId; }
	 */
	
	@Override
	public String toString() {
		String ret = "";
		for (int i : attributes) {
			ret = ret + i;
		}
		return ret;
	}
	
//	Not used
	public int uniqueID(Card c) {
		int id = 0;
		for (int att : attributes) {
			id = id*nVals + att;
		}
		return id;
	}
	
//	Needed to overload equals for collection contains
	public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(attributes).
            toHashCode();
    }
	
	public boolean equals(Object c) {
		if (c == null)
            return false;
        if (c == this)
            return true;
        if (c.getClass() != getClass())
            return false;
        
        Card rhs = (Card) c;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(attributes, rhs.attributes).
            isEquals();
    }
	
}
