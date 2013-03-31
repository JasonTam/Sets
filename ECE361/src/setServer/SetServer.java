package setServer;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

/**
 * This file will primarily be used to start the server and 
 * keep track of the threads that are connected.
 * 
 * @author Administrator
 *
 */
public class SetServer {
	public static ConcurrentHashMap<String, SetMultiThread> allThreads = new ConcurrentHashMap<String, SetMultiThread>();
	public static ConcurrentHashMap<String, GameRoom> gameRooms = new ConcurrentHashMap<String, GameRoom>();
	public static GameRoom lobby = new GameRoom("lobby");

	public static void main(String[] args) throws IOException {
		
        ServerSocket serverSocket = null;
        boolean listening = true;
        int PORT = 4444;
        int i = 0;
       
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT + ".");
            System.exit(-1);
        }
        while (listening) {
//        	When a new thread attempts to connect to the server,
//        	add it to the global list of threads.
        	allThreads.put(Integer.toString(i), new SetMultiThread(serverSocket.accept(), i));
        	
//        	Once the thread has been accepted, initiate the connection
        	allThreads.get(Integer.toString(i)).start();
        	
//        	Just using this to keep track of connected threads.
        	System.out.println(i + 1);
        	++i;
        }
        serverSocket.close();
    }
    
//	This will be used to send a message to all specified threads.
    public static void sendChat(String roomName, SetMultiThread thread, String message) {
	    Iterator it = gameRooms.get(roomName).keySet().iterator();
	    while (it.hasNext()) {	
	        String key = (String) it.next();
	        
	        ArrayList<String> data = new ArrayList<String>(); 
	        
	        data.add(thread.getName());
	        data.add(message);
	        
	        gameRooms.get(roomName).get(key).out.println(JSONinterface.genericToJson("chat", data));
//	        it.remove(); // avoids a ConcurrentModificationException
	    }
    }
    
    
    public static void sendUsers(SetProtocol sp)
    {
		sp.theOutput = "USERS|";
	    Iterator itUsers = SetServer.allThreads.keySet().iterator();
	    while (itUsers.hasNext()) {	
	        String user = (String) itUsers.next();
	        sp.theOutput = sp.theOutput.concat(user + "|");
		    //			        it.remove(); // avoids a ConcurrentModificationException
	    }
	    broadcastToAllThreads(sp);
    }
    
    public static void sendLogin(String name, SetProtocol sp)
    {
        System.out.println(name + " logged in");
        
        sp.theOutput = JSONinterface.genericToJson("login", name);
        
	    broadcastToAllThreads(sp);
        
    }
    
    public static void sendLogout(String name, SetProtocol sp)
    {
        System.out.println(name + " logged out");
        sp.theOutput = "LOGOUT|" + name;
	    broadcastToAllThreads(sp);
    }
    
    public static void sendRoomLeave(SetMultiThread curThread, SetProtocol sp)
    {
		sp.theOutput = "ROOMLEAVE|" + curThread.currentRoom.getName();
		curThread.currentRoom.leave(curThread);
		SetServer.lobby.join(curThread);
	    sp.state = SetProtocol.LOBBY;
        
	    broadcastToAllThreads(sp);
	    sendRooms(sp);
    }
    
    public static void sendRoomCreate(String roomName, SetMultiThread curThread, SetProtocol sp)
    {
		new GameRoom(roomName, curThread);
		sp.theOutput = "ROOMCREATE|" + roomName;
	    sp.state = SetProtocol.GAME;
        
	    
	    broadcastToAllThreads(sp);
	    sendRooms(sp);
			    
        
    }
    public static void broadcastToAllThreads(SetProtocol sp)
    {
	    Iterator itThread = SetServer.allThreads.keySet().iterator();
	    
	    while (itThread.hasNext())
	    {
	        String key = (String) itThread.next();
	        allThreads.get(key).out.println(sp.theOutput);
	    }
    }
    
    public static void sendRooms(SetProtocol sp)
    {
        System.out.println(gameRooms);
        
        sp.theOutput = "ROOMS|";
	    Iterator itGame = SetServer.gameRooms.keySet().iterator();
	    while (itGame.hasNext()) {	
	        String roomName = (String) itGame.next();
	        sp.theOutput = sp.theOutput.concat(roomName);
		    Iterator itUser = SetServer.gameRooms.get(roomName).keySet().iterator();
		    while (itUser.hasNext()) {
		    	String userName = (String) itUser.next();
		    	sp.theOutput = sp.theOutput.concat("-" + userName);
		    }
		    sp.theOutput = sp.theOutput.concat("|");
		    //			        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    broadcastToAllThreads(sp);
        
    }
    
}
