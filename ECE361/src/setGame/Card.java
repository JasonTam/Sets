package setGame;

import java.util.ArrayList;

public class Card {

	/**
	 * Possible values for each attribute are
	 * 0 up to nVals-1
	 */
	public static int nAtts = 4;
	public static int nVals = 3;
	
	
	/**
	 * The attribute values will be stored in an
	 * array list. An example of the contents
	 * of a card's attribute list:
	 * [0][1][2][1]
	 */
	ArrayList<Integer> attributes = new ArrayList<Integer>();

	/**
	 * @param i is the unique index of the card
	 * Since cards are unique, the attributes are
	 * derived by radix decomposition into the
	 * attribute bins of the array list
	 */
	public Card(int i) {
		for (int att=1; att<=nAtts; att++) {
			attributes.add(i%nVals);
			i/=nVals;
		}
	}

	public int getIntId() {
		int id = 0;
		for (int att:attributes)
			id = id*3+att;
		return id;
	}
	
	public String getStrId() {
		String strId = Integer.toString(getIntId());
		strId = (strId.length()<2)?"0"+strId:strId;
		return strId;
	}
	
	@Override public String toString() {
		String ret = "";
		for (Integer i : attributes) {
			ret = ret + i;
		}
		return ret;
	}
	
}
