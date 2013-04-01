package setServer;
import java.util.concurrent.ConcurrentHashMap;

import setGame.Game;

public class GameRoom extends ConcurrentHashMap<String, SetMultiThread> {
	private String room_name;
	private Game curGame; 

	private ConcurrentHashMap<Integer, Boolean> cards;
	
	public Game startGame()
	{
	    curGame = new Game();
	    curGame.start();
	    return curGame;
	    
	}
	
	public Game getCurGame() {
		return curGame;
	}
	
//	Create and join a game room
	public GameRoom (String name, SetMultiThread thread) {
		room_name = name;
		join(thread);
		SetServer.gameRooms.put(room_name, this);
	}
	
//	Create a game room
	public GameRoom (String name) {
		room_name = name;
		SetServer.gameRooms.put(room_name, this);
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
		return room_name;
	}
	
	public String toString() {
	    return getName();
	}
	
}