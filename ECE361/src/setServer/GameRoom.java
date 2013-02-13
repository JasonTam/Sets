package setServer;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom extends ConcurrentHashMap<String, SetMultiThread> {
	private String room_name;
	
	
//	Create and join a game room
	public GameRoom (String name, SetMultiThread thread) {
		this.room_name = name;
		this.join(thread);
		SetServer.gameRooms.put(this.room_name, this);
	}
	
//	Create a game room
	public GameRoom (String name) {
		this.room_name = name;
		SetServer.gameRooms.put(this.room_name, this);
	}
	
//	Join a game room
	public void join(SetMultiThread thread) {
		if (thread.currentRoom != null) {
			thread.currentRoom.leave(thread);
		}
		this.put(thread.getName(), thread);
		thread.currentRoom = this;
	}

//	Leave a game room
	public void leave(SetMultiThread thread) {
		this.remove(thread.getName());
		if (this.isEmpty() & !this.getName().equals(SetServer.lobby.getName())) {
			SetServer.gameRooms.remove(this.getName());
		}
	}
	
//	Get room's name
	public String getName() {
		return this.room_name;
	}
	
}