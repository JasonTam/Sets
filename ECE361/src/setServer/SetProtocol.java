package setServer;
import java.net.*;
import java.io.*;
 
public class SetProtocol {
    private static final int INITIALIZE = 0;
    private static final int LOGIN = 1;
    private static final int LOBBY = 2;
    private static final int GAME = 3;
    
 
 
    private int state = INITIALIZE;

 
    public String processInput(String theInput, SetMultiThread curThread) {
        String theOutput = null;
        
// 		Each condition must always assign a value to theOutput.
//      Initialization of the server
        if (state == INITIALIZE) {
        	theOutput = "Please enter a valid username.";
    		state = LOGIN;
        } 
//      After connecting to server, input name and validate
        else if (state == LOGIN) {
        	if (theInput.toLowerCase().startsWith("name")) {
        		
	        	curThread.setName(theInput.substring(5, theInput.length()));
	        	
	        	System.out.println(curThread.getName());
	        	
//	        	Once you get logged in, you autommatically are entered into the lobby.
	        	SetServer.lobby.join(curThread);
	        	theOutput = "Entering Lobnnby";
        		state = LOBBY;
        	}
        	else {
        		theOutput = "Invalid.";
        	}
//      Enter lobby after getting validated
        }
        else if (state == LOBBY) {
	        	if (theInput.toLowerCase().startsWith("broadcast")) {
        		String message = theInput.toLowerCase().substring(10);
	        	SetServer.broadcast(curThread.currentRoom.getName(), curThread, message);
	        	theOutput = "You just broadcasted a message";
	        }
//	        Create / join a set room
	        else if (theInput.toLowerCase().startsWith("join")) {
	        	String roomName = theInput.toLowerCase().substring(5);

	        	
//	        	If a user tries to enter a room (that is not the lobby) and its full, don't let them in!
	        	if (!roomName.equals(SetServer.lobby.getName()) && SetServer.gameRooms.containsKey(roomName) && SetServer.gameRooms.get(roomName).size() == 2) {
        			theOutput = "This room is full!";
        		}
//	        	If the room doesn't exist, create it and join the room
	        	else if (!SetServer.gameRooms.containsKey(roomName)) {
	        		new GameRoom(roomName, curThread);
	        		theOutput = "Created and Joined room";
	        		state = GAME;
	        	}
//	        	If the room does exist and its not full, join it
	        	else {
	        		SetServer.gameRooms.get(roomName).join(curThread);
	        		theOutput = "Joined existing room";
	        		state = GAME;
	        	}
	    	}
	        else if (theInput.toLowerCase().startsWith("quit")) {
	        	theOutput = "Bye.";
	        }
	        else if (theInput.toLowerCase().startsWith("show rooms")) {
	        	System.out.println(SetServer.gameRooms);
	        	theOutput = "hsowed rooms";
	        }
	        else if (theInput.toLowerCase().startsWith("show users")) {
	        	System.out.println(SetServer.allThreads);
	        	theOutput = "showed users";
	        }
	        else if (theInput.toLowerCase().startsWith("set")) {
	        	theOutput = gameLogic.set();
	        }
	        else {
	        	theOutput = "said nothing";
	        }
        }
        
        else if (state == GAME) {
        	if (theInput.toLowerCase().startsWith("leave")) {
        		curThread.currentRoom.leave(curThread);
        		SetServer.lobby.join(curThread);
        		theOutput = "Leaving Game";
        		state = LOBBY;
        		
        	}
        	else {
        		theOutput = "Entering Game";
        	}
        }

        return theOutput;
    }
}