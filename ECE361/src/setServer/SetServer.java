package setServer;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


/* For ease of use... hehehe
 * 
 * ip address: 199.98.20.120
 * port: 5122 
 * user: default
 * password: Gir(W8
 */

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
        	// Just sit and wait for new connections
        	new SetMultiThread(serverSocket.accept()).start();
        }
        serverSocket.close();
	}
    
//	This will be used to send a message to all specified threads. 1->chat, 2->leave
    public static void sendChat(String roomName, SetMultiThread curThread, String message,int type) {
        String chatMessage = null;
    	if(type==1)
        {
        	chatMessage = curThread.getName() + ": " + message;
        }
        else if(type==2)
        {
        	chatMessage = curThread.getName() + " " + message;	
        }
        ArrayList<SetMultiThread> roomThreads = getRoomThreads(curThread);
        
        for (SetMultiThread thread : roomThreads)
        {
            thread.out.println( JSONinterface.genericToJson("chat", chatMessage));
        }
        
    }

//  Gets all the threads in the current room
    public static ArrayList<SetMultiThread> getRoomThreads (SetMultiThread thread)
    {
        System.out.println(thread.currentRoom);
        String roomName = thread.currentRoom.getName();
        ArrayList<SetMultiThread> roomThreads = new ArrayList();
	    Iterator it = gameRooms.get(roomName).threadsInRoom.keySet().iterator();
	    while (it.hasNext()) {	
	        String key = (String) it.next();
	        roomThreads.add(gameRooms.get(roomName).threadsInRoom.get(key));
	    }
	    return roomThreads;
    }
    
    // This should send ALL users a list fo users in THEIR ROOM.
    public static void sendRoomUsers()
    {
	    Iterator itUsers = SetServer.allThreads.keySet().iterator();
	    while (itUsers.hasNext()) {	
	        String user = (String) itUsers.next();
	        SetMultiThread curThread = SetServer.allThreads.get(user);
            curThread.out.println( JSONinterface.genericToJson("users", User.getUsersInRoomArray(curThread.currentRoom)));
	    }
	    
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
    
    // Send a list of ALL ROOMS to ALL USERS 
    public static void sendRooms(SetProtocol sp)
    {
        
        sp.theOutput = JSONinterface.genericToJson("rooms", GameRoom.getRoomsArray());
        broadcastToAllThreads(sp);
    }
    
}
    /*
    public static void sendRoomLeave(SetMultiThread curThread, SetProtocol sp)
    {
		sp.theOutput = JSONinterface.genericToJson("roomleave", curThread.currentRoom.getName());
		
		curThread.currentRoom.leave(curThread);
		SetServer.lobby.join(curThread);
        
	    broadcastToAllThreads(sp);
	    sendRooms(sp);
    }
    
    public static void sendRoomCreate(String roomName, SetMultiThread curThread, SetProtocol sp)
    {
		
		sp.theOutput = JSONinterface.genericToJson("roomCreate", roomName);
	    broadcastToAllThreads(sp);
	    sendRooms(sp);
    }
    public static void sendLogout(String name, SetProtocol sp)
    {
        System.out.println(name + " logged out");
        sp.theOutput = JSONinterface.genericToJson("logout", name);
	    broadcastToAllThreads(sp);
    }
    public static void sendLogin(String name, SetProtocol sp)
    {
        System.out.println(name + " logged in");
        
        sp.theOutput = JSONinterface.genericToJson("login", name);
        
	    broadcastToAllThreads(sp);
        
    }
    public static void sendAllUsers(SetProtocol sp)
    {
		ArrayList<String> userList = new ArrayList<String>();
	    Iterator itUsers = SetServer.allThreads.keySet().iterator();
	    while (itUsers.hasNext()) {	
	        String user = (String) itUsers.next();
	        userList.add(user);
//	        sp.theOutput = sp.theOutput.concat(user + "|");
		    //			        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
        sp.theOutput = JSONinterface.genericToJson("users", userList);
        broadcastToAllThreads(sp);
    }
    */
