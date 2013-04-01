package setServer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import setGame.Game;

public class GameRoom implements Comparable<GameRoom>
{
    // Used for server-side house keeping
    // Transient means gson will exclude this field
    // which is good (it was creating a stack overflow error)
    
    public transient ConcurrentHashMap<String, SetMultiThread> threadsInRoom = new ConcurrentHashMap<String, SetMultiThread>();
    
	private String room_name;
	private transient Game curGame; 
	public boolean gameStarted = false;
	public Date timeCreated;
	
    public ConcurrentHashMap<String, Date> usersInRoom = new ConcurrentHashMap<String, Date>();
	
    // Starts a game in the currnt room.  Cards are dealt to all users in the room
	public Game startGame()
	{
	    gameStarted = true;
	    curGame = new Game();
	    curGame.start();
	    return curGame;
	    
	}
	
	public Game getCurGame() {
		return curGame;
	}
	
//	Create and join a game room
	public GameRoom (String name, SetMultiThread thread) {
	    timeCreated = new Date();
		room_name = name;
		join(thread);
		SetServer.gameRooms.put(room_name, this);
	}
	
//	Create a game room
	public GameRoom (String name) {
	    timeCreated = new Date();
		room_name = name;
		SetServer.gameRooms.put(room_name, this);
	}
	
//	Join a game room
	public void join(SetMultiThread thread) {
		if (thread.currentRoom != null) {
			thread.currentRoom.leave(thread);
		}
		threadsInRoom.put(thread.getName(), thread);
		usersInRoom.put(thread.getName(), new Date());
		thread.currentRoom = this;
	}

//	Leave a game room
	public void leave(SetMultiThread thread) {
		threadsInRoom.remove(thread.getName());
		usersInRoom.remove(thread.getName());
		if (threadsInRoom.isEmpty() & !getName().equals(SetServer.lobby.getName())) {
			SetServer.gameRooms.remove(getName());
		}
	}
	
	// Setting up an array for use with the front end room buttons
	public static ArrayList<GameRoom> roomHashtoArray(ConcurrentHashMap<String, GameRoom> roomHash)
	{
	    
	    
	    ArrayList<GameRoom> roomsArray = new ArrayList<GameRoom>();
	    Iterator<String> itRooms = roomHash.keySet().iterator();
	    
	    //Filter out the rooms
	    while (itRooms.hasNext())
	    {
	        String key = (String) itRooms.next();
	        GameRoom curRoom = roomHash.get(key);
	        
	        if (curRoom.getName().equals("lobby"))
	        {
	            continue;
	        }
	        
	        
	        roomsArray.add(roomHash.get(key));
	    }
	    
	    //Sort the rooms by date...
	    Collections.sort(roomsArray);
	    
	    return roomsArray;
	}
	
	
	
//	Get room's name
	public String getName() {
		return room_name;
	}

	// Used to sort the tables on the GUI -- most recently created table will show up first.
    public int compareTo(GameRoom game) {
        int lastCmp = game.timeCreated.compareTo(timeCreated);
        return lastCmp;
    }
	
	public String toString() {
	    return getName();
	}
	
}
