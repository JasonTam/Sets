package setServer;

import java.util.concurrent.ConcurrentHashMap;
import setPlayer.*;
import java.util.ArrayList;
import setGame.Game;

public class GameRoom extends ConcurrentHashMap<String, SetMultiThread> {	
	
	private String roomName;
	private ArrayList<Player> players;
	private Game game;
	private boolean gameStart;

	//	Create and join a game room
	public GameRoom (String name, String playerName, SetMultiThread thread) {
		this.roomName = name;
		Player creator = new Player(playerName);
		this.players.add(creator);
		this.game = new Game();
		this.gameStart = false;
		join(thread);
		SetServer.gameRooms.put(roomName, this);
	}
	
	//	Create a game room
	public GameRoom (String name) {
		roomName = name;
		SetServer.gameRooms.put(roomName, this);
	}
	
	public void startGame()
	{
		this.game.start();
		if(this.gameStart == false) {
			this.gameStart = true;	
		}
	// Send response;
	}
	
	//	Join a game room
	public void join(SetMultiThread thread) {
		if (thread.currentRoom != null) {
			thread.currentRoom.leave(thread);
		}
		put(thread.getName(), thread);
		thread.currentRoom = this;
	}

	//	Leave a game room
	public void leave(SetMultiThread thread) {
		this.remove(thread.getName());
		if (isEmpty() & !getName().equals(SetServer.lobby.getName())) {
			SetServer.gameRooms.remove(getName());
		}
	}
	
	//	Get room's name
	public String getName() {
		return this.roomName;
	}
	
/*	
	public String toString() {
	    return getName();
	}
	*/
	
}