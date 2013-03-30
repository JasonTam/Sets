package setPlayer;

public class Player {
	private String playerName;
	private int currentScore;
	private int setsFound;
	private int penalties;
	
	public Player(String playerName) {
		this.playerName = playerName;
		this.currentScore = 0;
		this.setsFound = 0;
		this.penalties = 0;
	}
	
	public int plusSet() {
		this.setsFound = this.setsFound + 1;
		updateScore();
		return this.setsFound;
	}
	
	public int plusPenalty() {
		this.penalties = this.penalties + 1;
		updateScore();
		return this.penalties;
	}
	
	private void updateScore() {
		this.currentScore = this.setsFound + this.penalties;
	}
	
	public int getScore() {
		return this.currentScore;
	}
}
