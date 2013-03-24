package setGame;

public class test_game {
	
	public static void main(String[] args) {
        System.out.println("Testing Set Game");
        System.out.println("to submit set: s i_0 i_1 i_2");
        System.out.println("to validate field: v");

        Game game1 = new Game();
        game1.start();
        while(!game1.isGameOver()) {
            game1.print();
			game1.readCommand();
        }
        game1.print();
		System.out.println("Game over");
    }
}