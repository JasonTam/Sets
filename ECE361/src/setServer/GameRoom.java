package setServer;
import java.util.concurrent.ConcurrentHashMap;

import setGame.Game;

public class GameRoom extends ConcurrentHashMap<String, SetMultiThread> {
	private String room_name;
	private ConcurrentHashMap<Integer, Boolean> cards;
	
	public void startGame(String roomName)
	{
	    Game game1 = new Game();
	    game1.start();
	    
	    
	    
	    
	    
	    
//	    Ill expect the set game to send me an INDEX for every card.
//	    The default hash value will be false.
//	    If someone sends me a correct set, the value will cahnge to TRUE.
//	    When a cards hash value is true, anyone else that submits a set using that
//	    card will not be able to use it.
	    
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
	
/*	
	public String toString() {
	    return getName();
	}
	*/
	
}