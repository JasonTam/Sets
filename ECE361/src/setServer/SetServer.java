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
    public static void broadcast(String roomName, SetMultiThread thread, String message) {
//    	Get the first thread in the list, and say "hello";
	    Iterator it = gameRooms.get(roomName).keySet().iterator();
	    while (it.hasNext()) {	
	        String key = (String) it.next();
	        gameRooms.get(roomName).get(key).out.println(thread.getName() + ": " + message);
//	        it.remove(); // avoids a ConcurrentModificationException
	    }
    }
}
