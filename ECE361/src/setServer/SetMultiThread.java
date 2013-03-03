package setServer;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

import javax.swing.JFrame;

import setClient.GameGrid;

import DBI.DBConnect;
 
public class SetMultiThread extends Thread {
    private Socket socket = null;
//    This variable contains what the server is sending the client
    public PrintWriter out;
//    This variable contains what the client is sending the server
    public BufferedReader in;
    public GameRoom currentRoom = null;
    
    
    public int default_name;
    
    public static DBConnect dbc = new DBConnect();
   
 
//    	This constructor accepts the socket as well as an integer, 
//    	which acts as its default name
    public SetMultiThread(Socket s, int n) {
	    super("SetMultiThread");
	    socket = s;
	    this.setName(Integer.toString(default_name));
	    default_name = n;
    }
    
    public void run() {
 
	    try {
	    	System.out.println(socket);
	        out = new PrintWriter(socket.getOutputStream(), true);
	        in = new BufferedReader(
	                    new InputStreamReader(
	                    		socket.getInputStream()
	            		)
	        );
	 
	        String inputLine, outputLine;
	        
	        SetProtocol sp = new SetProtocol(this);
	       	System.out.println(this);
	        outputLine = sp.processInput(null, this);
	        
//	   		This very first line that is printed actually
//	      	gets taken and is stored as the thread's name
	        out.println(outputLine);
	        
//	        Waiting for input from the client
	        while ((inputLine = in.readLine()) != null) {
//	        	Whatever the client says is automatically piped into the SetProtocol.
//	        	The protocol then handles the request and decides what to do with it,
//	        	and what response to spit back out
		        outputLine = sp.processInput(inputLine, this);
		        out.println(outputLine);
		        System.out.println(outputLine);
	
	            
		        if (outputLine.equals("Bye."))
	        		break;
	        
	        }
	        
	        
//	        Simply closing out all of the made connnections.
	        System.out.println("quiting out of app");
	        if (currentRoom != null)
	        {
	            this.currentRoom.leave(this);
	        }
	        SetServer.allThreads.remove(Integer.toString(default_name));
	        out.close();
	        in.close();
	        socket.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }

}
