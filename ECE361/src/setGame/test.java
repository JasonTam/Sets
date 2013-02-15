package setGame;

public class test {
	public static void main(String[] args) {
        System.out.println("Testing lots of fail");
        System.out.println(
        		(int)Math.pow(Card.nVals, Card.nAtts)
        		+ " cards");
//        lol(345);

        Card a = new Card(65);
        Card b = new Card(66);
        Card c = new Card(70);
        System.out.println("Card a: " + a.toString());
        System.out.println("Card b: " + b.toString());
        System.out.println("Card c: " + c.toString());
        System.out.println();
        
        Game game1 = new Game();
        boolean setfound;
        setfound = game1.isSet(a, b, c);
        System.out.println("a,b,c form a set? " + setfound);
        
//        game1.init();
        
    }
	
	static void lol(int i) {
		i /= 3;
		System.out.println(i);
	}
}