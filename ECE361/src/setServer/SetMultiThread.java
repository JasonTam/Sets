package setServer;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
 
public class SetMultiThread extends Thread {
    private Socket socket = null;
//    This variable contains what the server is sending the client
    public PrintWriter out;
//    This variable contains what the client is sending the server
    public BufferedReader in;
    public GameRoom currentRoom;
   
 
//    	This constructor accepts the socket as well as an integer, 
//    	which acts as its default name
    public SetMultiThread(Socket socket, int default_name) {
	    super("SetMultiThread");
	    this.socket = socket;
	    this.setName(Integer.toString(default_name));
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
	        SetProtocol sp = new SetProtocol();
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
	
	            
		        if (outputLine.equals("Bye."))
	        		break;
	        
	        }
	        
	        
//	        Simply closing out all of the made connnections.
	        this.currentRoom.leave(this);
	        out.close();
	        in.close();
	        socket.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }

}
